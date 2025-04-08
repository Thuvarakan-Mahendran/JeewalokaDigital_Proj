import React, { createContext, useContext, useEffect, useState, useRef, useCallback } from 'react';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { AuthContext } from './AuthContext'; // Import AuthContext
import { getAccessToken } from '../api/TokenService'; // Get token service
import api from '../api/axiosInstance'; // Use your configured axios instance

const WebSocketContext = createContext(null);

export const useWebSocket = () => useContext(WebSocketContext);

const socketFactory = () => {
    // Ensure URL matches your backend endpoint in WebSocketConfig
    return new SockJS('http://localhost:8080/ws'); // Adjust port if needed
};

export const WebSocketProvider = ({ children }) => {
    const { user } = useContext(AuthContext); // Get user from AuthContext
    const [stompClient, setStompClient] = useState(null);
    const [isConnected, setIsConnected] = useState(false);
    const [onlineUsers, setOnlineUsers] = useState(new Set()); // Store online usernames
    const subscriptions = useRef({});
    const clientRef = useRef(null); // Ref to hold the client instance persistently

    // Function to fetch initial list of online users
    const fetchInitialOnlineUsers = useCallback(async () => {
        if (!user) return; // Don't fetch if not logged in
        try {
            console.log('Fetching initial online users...');
            // Use your existing axios instance (api.js) which includes auth interceptor
            const response = await api.get('/presence/online');
            if (response.data && Array.isArray(response.data)) {
                console.log('Initial online users received:', response.data);
                setOnlineUsers(new Set(response.data));
            } else {
                console.warn('Received unexpected format for online users:', response.data);
                setOnlineUsers(new Set()); // Initialize empty if data is bad
            }
        } catch (error) {
            console.error("Failed to fetch initial online users:", error);
            // Handle error appropriately, maybe clear the list or show a message
            setOnlineUsers(new Set());
        }
    }, [user]); // Depend on user to ensure auth


    const connect = useCallback(() => {
        // Only connect if logged in and not already connected/connecting
        if (!user || isConnected || clientRef.current?.active) {
            console.log(`WebSocket connect skipped: user=${!!user}, isConnected=${isConnected}, clientActive=${!!clientRef.current?.active}`);
            return;
        }

        const accessToken = getAccessToken();
        if (!accessToken) {
            console.error("WebSocket: No access token found, cannot connect.");
            return;
        }

        console.log("WebSocket: Attempting to connect...");
        const client = new Client({
            webSocketFactory: socketFactory,
            connectHeaders: {
                Authorization: `Bearer ${accessToken}`,
            },
            debug: (str) => { console.log('STOMP DEBUG:', str); },
            reconnectDelay: 10000, // Increased delay
            heartbeatIncoming: 4000,
            heartbeatOutgoing: 4000,
        });

        client.onConnect = (frame) => {
            console.log('WebSocket: Connected successfully.', frame);
            setIsConnected(true);
            setStompClient(client); // Keep stompClient state for potential direct use
            clientRef.current = client; // Store in ref

            // --- Subscribe to Presence Updates ---
            try {
                const presenceSub = client.subscribe('/topic/presence', (message) => {
                    try {
                        const update = JSON.parse(message.body); // { username: "...", status: "online/offline" }
                        console.log('Presence update received:', update);
                        if (update.username) {
                            setOnlineUsers(prevUsers => {
                                const newUsers = new Set(prevUsers);
                                if (update.status === 'online') {
                                    newUsers.add(update.username);
                                } else if (update.status === 'offline') {
                                    newUsers.delete(update.username);
                                }
                                return newUsers;
                            });
                        }
                    } catch (e) {
                        console.error('Error parsing presence message:', message.body, e);
                    }
                });
                subscriptions.current['presence'] = presenceSub;
                console.log('Subscribed to /topic/presence');
            } catch (subError) {
                console.error('Failed to subscribe to /topic/presence:', subError);
            }


            // --- Subscribe to User-Specific Notifications (Placeholder for later) ---
            // Note: Spring maps /user/queue/notifications based on the Principal name
            try {
                const notificationSub = client.subscribe('/user/queue/notifications', (message) => {
                    console.log("User-specific notification received:", message.body);
                    // TODO: Handle incoming notifications (e.g., show toast, update state)
                });
                subscriptions.current['notifications'] = notificationSub;
                console.log('Subscribed to /user/queue/notifications');
            } catch (subError) {
                console.error('Failed to subscribe to /user/queue/notifications:', subError);
            }

            // Fetch the current list of online users AFTER connecting and subscribing
            fetchInitialOnlineUsers();
        };

        client.onStompError = (frame) => {
            console.error('WebSocket: STOMP Error', frame.headers['message'], frame.body);
            setIsConnected(false);
            // Consider if logout is needed on specific STOMP errors (e.g., auth failure)
        };

        client.onWebSocketError = (error) => {
            console.error('WebSocket: Connection Error', error);
            setIsConnected(false);
        };

        client.onWebSocketClose = (event) => {
            console.log('WebSocket: Connection Closed', event);
            setIsConnected(false);
            setOnlineUsers(new Set()); // Clear online users on disconnect
            subscriptions.current = {};
            // clientRef.current = null; // Clear ref here? Stompjs handles reconnect based on 'active' state maybe
            // setStompClient(null); // Stompjs handles the client object internally for reconnects
        };

        clientRef.current = client; // Store client in ref immediately
        client.activate();

    }, [user, isConnected, fetchInitialOnlineUsers]); // Dependencies: connect if user changes or connection drops


    const disconnect = useCallback(() => {
        console.log("WebSocket: Attempting to disconnect...");
        const client = clientRef.current; // Use the client from the ref
        if (client && client.active) {
            // Unsubscribe explicitly (good practice)
            Object.values(subscriptions.current).forEach(sub => {
                if (sub && typeof sub.unsubscribe === 'function') {
                    try { sub.unsubscribe(); } catch (e) { console.warn("Error unsubscribing:", e); }
                }
            });
            subscriptions.current = {};

            client.deactivate().then(() => {
                console.log('WebSocket: Deactivated successfully.');
            }).catch(error => {
                console.error('WebSocket: Error during deactivation:', error);
            }).finally(() => {
                // Ensure state is updated regardless of deactivate success/failure
                setIsConnected(false);
                setStompClient(null); // Clear state
                clientRef.current = null; // Clear ref
                setOnlineUsers(new Set()); // Clear list on manual disconnect
            });
        } else {
            console.log("WebSocket: Already disconnected or no active client.");
            // Ensure state reflects disconnected status if called redundantly
            setIsConnected(false);
            setStompClient(null);
            clientRef.current = null;
            setOnlineUsers(new Set());
        }
    }, []); // No dependencies needed for disconnect logic itself


    // Effect to manage connection based on auth state
    useEffect(() => {
        if (user) {
            // User logged in, attempt connection
            connect();
        } else {
            // User logged out, ensure disconnection
            disconnect();
        }

        // Cleanup on component unmount
        return () => {
            console.log("WebSocketProvider unmounting, ensuring disconnect...");
            // This ensures disconnect even if user state didn't trigger it before unmount
            disconnect();
        };
    }, [user]); // Depend on user state and the connect/disconnect functions

    const value = {
        stompClient, // Expose client if needed, but prefer specific actions
        isConnected,
        onlineUsers,
        // Add functions here later if needed to send messages via WebSocket
        // e.g., sendNotification: (payload) => stompClient?.publish({ destination: '/app/notify', body: JSON.stringify(payload) })
    };

    return (
        <WebSocketContext.Provider value={value}>
            {children}
        </WebSocketContext.Provider>
    );
};