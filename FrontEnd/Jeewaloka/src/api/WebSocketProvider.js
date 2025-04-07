// import React, { createContext, useContext, useEffect, useState, useRef, useCallback } from 'react';
// import { Client } from '@stomp/stompjs';
// import SockJS from 'sockjs-client';

// const WebSocketContext = createContext(null);

// export const useWebSocket = () => useContext(WebSocketContext);

// // Helper to create the SockJS connection factory
// const socketFactory = () => {
//     // Make sure the URL matches your Spring Boot endpoint
//     return new SockJS('http://localhost:8080/ws'); // Adjust port if needed
// };

// export const WebSocketProvider = ({ children }) => {
//     const [stompClient, setStompClient] = useState(null);
//     const [isConnected, setIsConnected] = useState(false);
//     const [onlineUsers, setOnlineUsers] = useState(new Set()); // Store online user IDs
//     const subscriptions = useRef({}); // Keep track of subscriptions

//     const connect = useCallback(() => {
//         // Retrieve the access token from local storage
//         const accessToken = localStorage.getItem('accessToken');
//         if (!accessToken) {
//             console.error("No access token found. Cannot connect WebSocket.");
//             setIsConnected(false);
//             return;
//         }

//         const client = new Client({
//             // Use the SockJS factory
//             webSocketFactory: socketFactory,

//             // Headers for STOMP CONNECT frame
//             connectHeaders: {
//                 Authorization: `Bearer ${accessToken}`,
//                 // You might need to pass token as query param for SockJS handshake if interceptor checks there
//                 // token: accessToken  // Uncomment if needed and adjust interceptor
//             },

//             // Debugging messages
//             debug: function (str) {
//                 console.log('STOMP Debug:', str);
//             },

//             // Heartbeat (optional but recommended)
//             reconnectDelay: 5000, // Try to reconnect every 5 seconds
//             heartbeatIncoming: 4000,
//             heartbeatOutgoing: 4000,
//         });

//         client.onConnect = (frame) => {
//             console.log('WebSocket Connected:', frame);
//             setIsConnected(true);
//             setStompClient(client); // Store the client instance

//             // --- Subscribe to Presence Updates ---
//             const presenceSub = client.subscribe('/topic/presence', (message) => {
//                 try {
//                     const update = JSON.parse(message.body);
//                     console.log('Presence update received:', update);
//                     setOnlineUsers(prevUsers => {
//                         const newUsers = new Set(prevUsers);
//                         if (update.status === 'online') {
//                             newUsers.add(update.userId);
//                         } else if (update.status === 'offline') {
//                             newUsers.delete(update.userId);
//                         }
//                         return newUsers;
//                     });
//                 } catch (error) {
//                     console.error("Failed to parse presence message:", message.body, error);
//                 }
//             });
//             subscriptions.current['presence'] = presenceSub;

//             // --- Subscribe to User-Specific Notifications (for later) ---
//             // Assumes your JWT/Principal correctly identifies the user for '/user' destinations
//             const notificationSub = client.subscribe('/user/queue/notifications', (message) => {
//                 console.log("Notification received:", message.body);
//                 // Handle notification display logic here
//             });
//             subscriptions.current['notifications'] = notificationSub;


//             // --- Fetch initial online users ---
//             fetchInitialOnlineUsers(accessToken);

//         };

//         client.onStompError = (frame) => {
//             console.error('Broker reported error: ' + frame.headers['message']);
//             console.error('Additional details: ' + frame.body);
//             setIsConnected(false);
//             // Consider logout or showing an error message
//         };

//         client.onWebSocketError = (error) => {
//             console.error('WebSocket Error:', error);
//             setIsConnected(false);
//             // Connection closed or couldn't connect
//         };

//         client.onWebSocketClose = (event) => {
//             console.log('WebSocket Closed:', event);
//             setIsConnected(false);
//             // Clear state, attempt reconnect handled by stompjs based on reconnectDelay
//             setOnlineUsers(new Set()); // Clear online users on disconnect
//             subscriptions.current = {}; // Clear subscriptions refs
//         };


//         client.activate(); // Start the connection attempt

//         // Store client instance immediately to allow disconnect function
//         // But connection state is handled by callbacks
//         // setStompClient(client); // -> Moved inside onConnect

//     }, []); // Add dependencies if needed (e.g., if token changes require reconnect)


//     const disconnect = useCallback(() => {
//         if (stompClient) {
//             // Unsubscribe from all topics
//             Object.values(subscriptions.current).forEach(sub => sub.unsubscribe());
//             subscriptions.current = {};

//             stompClient.deactivate().then(() => {
//                 console.log('WebSocket Deactivated');
//                 setIsConnected(false);
//                 setStompClient(null);
//                 setOnlineUsers(new Set());
//             }).catch(error => {
//                 console.error('Error during WebSocket deactivation:', error);
//                 // Force state update even if deactivation fails
//                 setIsConnected(false);
//                 setStompClient(null);
//                 setOnlineUsers(new Set());
//             });

//         }
//     }, [stompClient]);

//     // Function to fetch initial online users via REST
//     const fetchInitialOnlineUsers = async (token) => {
//         try {
//             // Adjust URL and headers as needed for your API
//             const response = await fetch('http://localhost:8080/api/presence/online', {
//                 headers: {
//                     'Authorization': `Bearer ${token}`
//                 }
//             });
//             if (!response.ok) {
//                 throw new Error(`HTTP error! status: ${response.status}`);
//             }
//             const onlineUserIds = await response.json();
//             console.log('Initial online users:', onlineUserIds);
//             setOnlineUsers(new Set(onlineUserIds));
//         } catch (error) {
//             console.error("Failed to fetch initial online users:", error);
//         }
//     };


//     // Auto-connect on mount and disconnect on unmount
//     useEffect(() => {
//         // Only connect if not already connected/connecting
//         // This check might need refinement depending on component lifecycle
//         if (!isConnected && !stompClient) {
//             connect();
//         }

//         // Cleanup function on component unmount
//         return () => {
//             console.log("WebSocketProvider unmounting, disconnecting...");
//             disconnect();
//         };
//     }, [connect, disconnect, isConnected, stompClient]); // Dependencies


//     const value = {
//         stompClient,
//         isConnected,
//         onlineUsers, // Provide the set of online users
//         // You can add methods here to send messages if needed later
//         // e.g., sendMessage: (destination, body) => stompClient?.publish({ destination, body: JSON.stringify(body) })
//     };

//     return (
//         <WebSocketContext.Provider value={value}>
//             {children}
//         </WebSocketContext.Provider>
//     );
// };