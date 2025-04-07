import React, { useState, useEffect, useContext } from 'react';
import { getSessions, revokeAllSessions, revokeSession } from "../../api/AuthService";
import { AuthContext } from '../../Context/AuthContext';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import {
  faTimes,
} from '@fortawesome/free-solid-svg-icons'
import { useWebSocket } from '../../Context/WebSocketContext';
// import HeartbeatComponent from '../../Components/HeartbeatComponent'
// import { AlignCenterVertical } from 'lucide-react';

const DashboardPage = () => {
  // Local state for sessions, loading and errors.
  const [sessions, setSessions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [revokeLoading, setRevokeLoading] = useState(false);
  const { user, logout } = useContext(AuthContext);
  const { isConnected, onlineUsers } = useWebSocket();

  const isOnline = isConnected && onlineUsers.has(user.username);
  const statusText = isConnected ? (isOnline ? 'Online' : 'Offline') : 'Connecting...';
  const statusColor = isConnected ? (isOnline ? 'green' : 'grey') : 'orange';

  // Function to fetch all active sessions.
  const fetchSessions = async () => {
    try {
      setLoading(true);
      const data = await getSessions();
      // console.log("response data is ", data);
      // Response is expected to be an array of session details (without the token itself).
      setSessions(data);
    } catch (err) {
      console.error('Error fetching sessions:', err);
      setError('Failed to load sessions');
    } finally {
      setLoading(false);
    }
  };

  const handleRevokeSession = async () => {
    try {
      setRevokeLoading(true);
      await revokeSession();
    } catch (err) {
      console.error('Error revoking specific session:', err);
      setError('Failed to revoke specific session');
    } finally {
      setRevokeLoading(false);
    }
  }

  // Function to revoke all sessions.
  const handleRevokeAllSessions = async () => {
    try {
      setRevokeLoading(true);
      await revokeAllSessions();
      // After successful revocation, clear the session list.
      setSessions([]);
      logout();
    } catch (err) {
      console.error('Error revoking sessions:', err);
      setError('Failed to revoke sessions');
    } finally {
      setRevokeLoading(false);
    }
  };

  // Load sessions when the component mounts.
  useEffect(() => {
    fetchSessions();
  }, []);

  return (
    <div style={{ padding: '20px' }}>
      <h1>Active Sessions</h1>

      {loading ? (
        <p>Loading sessions...</p>
      ) : error ? (
        <p style={{ color: 'red' }}>{error}</p>
      ) : sessions.length === 0 ? (
        <p>No active sessions found.</p>
      ) : (
        sessions.map((session, index) => (
          <div
            key={index}
            // style={{
            //   display: 'flex',
            //   border: '1px solid #ccc',
            //   borderRadius: '8px',
            //   padding: '10px',
            //   marginBottom: '10px',
            //   backgroundColor: '#f9f9f9',
            //   justifyContent: 'space-between'
            // }}
            className='border border-gray-300 rounded-lg p-3 m-3 bg-gray-100 flex justify-between items-center'
          >
            <div>
              <strong>IP:</strong> {session.ip} <br />
              <strong>User Agent:</strong> {session.userAgent}
              {/* <p>
              <strong>Created At:</strong>{' '}
              {new Date(parseInt(session.createdAt, 10)).toLocaleString()}
            </p> */}
            </div>
            <div onClick={handleRevokeSession}
              className='items-center'
            >
              <FontAwesomeIcon icon={faTimes} className='text-gray-600 cursor-pointer text-2xl' />
            </div>
            {/* </div> */}
          </div>
        ))
      )}

      <button
        onClick={handleRevokeAllSessions}
        disabled={revokeLoading}
        style={{
          padding: '10px 20px',
          fontSize: '16px',
          backgroundColor: '#ff4d4f',
          color: 'white',
          border: 'none',
          borderRadius: '5px',
          cursor: 'pointer'
        }}
      >
        {revokeLoading ? 'Revoking...' : 'Revoke All Sessions'}
      </button>
      {/* <div>
        <h1>Online users</h1>
        <HeartbeatComponent />
      </div> */}
      <span style={{ color: statusColor, fontWeight: 'bold' }}>
        ({statusText})
      </span>
    </div >
  );
};

export default DashboardPage;
