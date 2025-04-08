// import React, { useEffect, useState, useContext } from 'react';
// import { Client } from '@stomp/stompjs';
// import { AuthContext } from '../Context/AuthContext'
// import { getAccessToken } from '../api/TokenService';

// const HeartbeatComponent = () => {
//     const { user } = useContext(AuthContext);
//     const [stompClient, setStompClient] = useState(null);
//     const [onlineUsers, setOnlineUsers] = useState({});

//     // Obtain the JWT token and username from AuthContext/TokenService
//     const jwtToken = getAccessToken();
//     const username = user ? user.username : null;

//     useEffect(() => {
//         if (!jwtToken || !username) return; // Skip if no token or user

//         // Initialize the STOMP client with the JWT token for authentication
//         const client = new Client({
//             brokerURL: 'ws://localhost:8080/ws',
//             connectHeaders: {
//                 Authorization: `Bearer ${jwtToken}`
//             },
//             debug: str => console.log(str),
//             reconnectDelay: 5000,
//             onConnect: () => {
//                 console.log('Connected to WebSocket');
//                 // Subscribe to heartbeat acknowledgments
//                 client.subscribe('/topic/heartbeat', message => {
//                     const heartbeatMsg = JSON.parse(message.body);
//                     setOnlineUsers(prev => ({
//                         ...prev,
//                         [heartbeatMsg.username]: true
//                     }));
//                 });
//                 // Subscribe to presence updates (for users going offline)
//                 client.subscribe('/topic/presence', message => {
//                     const presenceMsg = JSON.parse(message.body);
//                     setOnlineUsers(prev => {
//                         const updated = { ...prev };
//                         delete updated[presenceMsg.username];
//                         return updated;
//                     });
//                 });
//             },
//         });

//         client.activate();
//         setStompClient(client);

//         return () => {
//             if (client) client.deactivate();
//         };
//     }, [jwtToken, username]);

//     useEffect(() => {
//         // Send a heartbeat every 10 seconds
//         const interval = setInterval(() => {
//             if (stompClient && stompClient.connected && username) {
//                 const heartbeatMsg = {
//                     username: username,
//                     timestamp: Date.now()
//                 };
//                 stompClient.publish({
//                     destination: '/app/heartbeat',
//                     body: JSON.stringify(heartbeatMsg)
//                 });
//                 console.log('Heartbeat sent:', heartbeatMsg);
//             }
//         }, 10000);

//         return () => clearInterval(interval);
//     }, [stompClient, username]);

//     return (
//         <div>
//             <h2>User Presence</h2>
//             <ul>
//                 {Object.keys(onlineUsers).map(userKey => (
//                     <li key={userKey}>
//                         {userKey} {userKey === username && ' (You)'}
//                     </li>
//                 ))}
//             </ul>
//         </div>
//     );
// };

// export default HeartbeatComponent;
