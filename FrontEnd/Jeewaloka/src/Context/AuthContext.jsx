// import React, { createContext, useState, useEffect } from 'react';
// import { jwtDecode } from 'jwt-decode';
// import { getAccessToken, removeAccessToken } from '../api/TokenService';
// import { logout as logoutService } from '../api/AuthService';

// export const AuthContext = createContext();

// export const AuthProvider = ({ children }) => {
//     const [user, setUser] = useState(null);

//     // Decode access token on app load if available
//     useEffect(() => {
//         const token = getAccessToken();
//         if (token) {
//             try {
//                 const decoded = jwtDecode(token);
//                 setUser({ username: decoded.username, role: decoded.role });
//                 console.log("user role stored in useContext is ", decoded.role)
//             } catch (error) {
//                 console.error('Invalid token:', error);
//                 removeAccessToken();
//             }
//         }
//     }, []);

//     const logout = async () => {
//         try {
//             await logoutService();
//         } catch (err) {
//             console.error('Logout error:', err);
//         }
//         removeAccessToken();
//         setUser(null);
//         window.location.href = '/login';
//     };

//     return (
//         <AuthContext.Provider value={{ user, setUser, logout }}>
//             {children}
//         </AuthContext.Provider>
//     );
// };



// import React, { createContext, useState, useEffect, useCallback } from 'react';
// import { jwtDecode } from 'jwt-decode';
// // Assuming you have a setAccessToken function alongside getAccessToken
// import { getAccessToken, removeAccessToken, setAccessToken } from '../api/TokenService';
// import { logout as logoutService } from '../api/AuthService';
// // Import useNavigate for smoother navigation (optional but recommended)
// // import { useNavigate } from 'react-router-dom';

// export const AuthContext = createContext();

// export const AuthProvider = ({ children }) => {
//     const [user, setUser] = useState(null);
//     const [loading, setLoading] = useState(true); // <-- Add loading state, default true
//     // const navigate = useNavigate(); // <-- Hook for navigation

//     // Use useCallback for logout to ensure stable function reference if passed down
//     const logout = useCallback(async () => {
//         try {
//             // Optional: Call backend logout endpoint if it invalidates the token server-side
//             await logoutService();
//         } catch (err) {
//             console.error('Backend logout error:', err);
//             // Decide if you want to proceed with frontend logout even if backend fails
//         } finally {
//             // Always clear frontend state and token
//             removeAccessToken();
//             setUser(null);
//             // navigate('/login'); // <-- Use navigate for SPA navigation
//             window.location.href = '/login'; // <-- Avoid this if using React Router
//         }
//     }, []); // Dependency array includes navigate

//     // Function to handle login success
//     // You would call this from your login component after a successful API call
//     const login = useCallback((token) => {
//         try {
//             setAccessToken(token); // Store the new token
//             const decoded = jwtDecode(token);
//             // Check expiration *before* setting user
//             // const isExpired = decoded.exp * 1000 < Date.now();
//             const isExpired = decoded.exp < Date.now();
//             if (isExpired) {
//                 console.error('Login successful but token is expired.');
//                 logout(); // Trigger logout if token is already expired
//                 return; // Stop processing
//             }
//             setUser({ username: decoded.username, role: decoded.role });
//             console.log("User logged in, role:", decoded.role);
//             // Optionally navigate to dashboard or home page after login
//             // navigate('/dashboard');
//         } catch (error) {
//             console.error('Failed to process token on login:', error);
//             removeAccessToken(); // Clean up if decoding/storing fails
//             setUser(null);
//         }
//     }, [logout]); // Include logout in dependencies


//     // Check token on initial load / refresh
//     useEffect(() => {
//         let isMounted = true; // Flag to prevent state update on unmounted component
//         setLoading(true); // Start loading

//         const checkAuth = () => {
//             const token = getAccessToken();
//             if (token) {
//                 try {
//                     const decoded = jwtDecode(token);
//                     // *** Check for token expiration ***
//                     const isExpired = decoded.exp < Date.now();

//                     // if (isExpired) {
//                     //     console.log('Token expired, removing.');
//                     //     removeAccessToken();
//                     //     setUser(null); // Ensure user is null if token expired
//                     // } else if 
//                     if (isMounted) {
//                         // Only set user if component is still mounted and token is valid
//                         setUser({ username: decoded.sub, role: decoded.role });
//                         console.log("Auth check: User restored from token. Role:", decoded.role);
//                     }
//                 } catch (error) {
//                     console.error('Invalid token found:', error);
//                     removeAccessToken();
//                     setUser(null); // Ensure user is null if token is invalid
//                 }
//             } else {
//                 setUser(null); // No token found, ensure user is null
//             }

//             if (isMounted) {
//                 setLoading(false); // Finish loading *after* check is complete
//             }
//         };

//         checkAuth();

//         // Cleanup function to run if the component unmounts during the check
//         return () => {
//             isMounted = false;
//         };
//     }, []); // Empty dependency array ensures this runs only once on mount

//     // Render a loading indicator or null while checking auth status
//     // This prevents ProtectedRoute from rendering and redirecting prematurely
//     if (loading) {
//         // You can return null or a proper loading spinner component
//         return <div>Loading authentication...</div>;
//     }

//     // Provide context value (including login and improved logout)
//     // Avoid providing setUser directly if possible
//     return (
//         <AuthContext.Provider value={{ user, setUser, login, logout, loading }}>
//             {children}
//         </AuthContext.Provider>
//     );
// };


import React, { createContext, useState, useEffect, useCallback } from 'react';
import { jwtDecode } from 'jwt-decode';
import { getAccessToken, removeAccessToken, setAccessToken } from '../api/TokenService';
import { logout as logoutService } from '../api/AuthService';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    // Initial state: loading is true, user is null
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    // --- Logout Callback (Seems OK) ---
    const logout = useCallback(async () => {
        // ... (keep existing logout logic) ...
        try {
            await logoutService();
        } catch (err) {
            console.error('Backend logout error:', err);
        } finally {
            removeAccessToken();
            setUser(null);
            // Consider using react-router navigation if applicable
            window.location.href = '/login';
        }
    }, []);

    // --- Login Callback (Seems OK, ensure 'sub'/'username' consistency) ---
    const login = useCallback((token) => {
        try {
            setAccessToken(token);
            const decoded = jwtDecode(token);
            // *** Use Date.now() for comparison ***
            const isExpired = decoded.exp * 1000 < Date.now();
            if (isExpired) {
                console.error('Login successful but token is expired.');
                // *** No need to await logoutService here, just clear frontend ***
                removeAccessToken();
                setUser(null);
                window.location.href = '/login'; // Redirect immediately
                return;
            }
            // *** Be consistent: Use 'sub' if that's what useEffect uses ***
            setUser({ username: decoded.sub, role: decoded.role }); // Assuming 'sub' holds the username
            console.log("User logged in, role:", decoded.role);
        } catch (error) {
            console.error('Failed to process token on login:', error);
            removeAccessToken();
            setUser(null);
        }
    }, []); // Removed logout dependency - login doesn't directly call logout on success


    // --- Initial Auth Check Effect (REVISED) ---
    useEffect(() => {
        let isMounted = true;
        // No setLoading(true) here - initial state is already true

        const checkAuth = () => {
            const token = getAccessToken();
            let currentUser = null; // Assume logged out initially

            if (token) {
                try {
                    const decoded = jwtDecode(token);
                    // *** Correct expiry check using milliseconds ***
                    const isExpired = decoded.exp * 1000 < Date.now();

                    if (isExpired) {
                        console.log('Auth check: Token expired, removing.');
                        removeAccessToken(); // Clean up expired token
                        // currentUser remains null
                    } else {
                        // Token exists and is valid
                        // *** Use consistent field for username (e.g., 'sub') ***
                        currentUser = { username: decoded.sub, role: decoded.role };
                        console.log("Auth check: User restored from valid token. Role:", decoded.role);
                    }
                } catch (error) {
                    // Token exists but is invalid (decoding failed)
                    console.error('Auth check: Invalid token found, removing.', error);
                    removeAccessToken(); // Clean up invalid token
                    // currentUser remains null
                }
            } else {
                // No token found
                console.log("Auth check: No token found.");
                // currentUser remains null
            }

            // Only update state if the component is still mounted
            if (isMounted) {
                setUser(currentUser); // Set the user (or null) based on checks
                setLoading(false);    // Set loading to false *after* all checks and user state are set
            }
        };

        checkAuth();

        // Cleanup function
        return () => {
            isMounted = false;
            console.log("AuthProvider effect cleanup"); // Add log for debugging
        };
    }, []); // Empty dependency array ensures this runs only once on initial mount

    console.log(`AuthProvider rendering: loading=${loading}, user=${user?.username}`); // Add log

    // --- Conditional Rendering (Remains the Same) ---
    // Render loading indicator while checking auth status
    if (loading) {
        return <div>Loading authentication...</div>;
    }

    // --- Provide Context (Remains the Same) ---
    // Provide context value once loading is false
    return (
        <AuthContext.Provider value={{ user, setUser, login, logout, loading }}>
            {children}
        </AuthContext.Provider>
    );
};


// import React, { createContext, useState, useEffect, Children } from 'react'
// import { logout, refreshToken } from "../api/AuthService"

// export const AuthContext = createContext()

// export const AuthProvider = ({ Children }) => {
//     const [authState, setAuthState] = useState({
//         accessToken: localStorage.getItem("accessToken"),
//         user: null,
//         isAuthenticated: false,
//     })

//     useEffect(() => {
//         const initializeAuth = async () => {
//             if (authState.accessToken) {
//                 try {
//                     const decoded = decodeToken(authState.accessToken)
//                     setAuthState((prev) => ({
//                         ...prev,
//                         user: decoded,
//                         isAuthenticated: true,
//                     }))
//                 } catch (error) {
//                     await handleTokenRefresh()
//                 }
//             }
//         }
//         initializeAuth()
//     }, [])

//     const handleTokenRefresh = async () => {
//         try {
//             const newToken = await refreshToken()
//             localStorage.setItem("accessToken", newToken)
//             const decoded = decodeToken(newToken)
//             setAuthState({
//                 accessToken: newToken,
//                 user: decoded,
//                 isAuthenticated: true,
//             })
//         } catch (error) {
//             logout()
//         }
//     }

//     const login = (accessToken) => {
//         localStorage.setItem("accessToken", accessToken)
//         const decoded = decodeToken(accessToken)
//         setAuthState({
//             accessToken,
//             user: decoded,
//             isAuthenticated: true,
//         })
//     }

//     const logout = () => {
//         localStorage.removeItem("accessToken")
//         setAuthState({
//             accessToken: null,
//             user: null,
//             isAuthenticated: false,
//         })
//         logoutApi()
//     }

//     const logoutApi = async () => {
//         try {
//             logout()
//         } catch (error) {
//             console.error("error during logout: ", error)
//         }
//     }

//     return (
//         <AuthContext.Provider value={{
//             ...authState,
//             login,
//             logout,
//             refreshToken: handleTokenRefresh,
//         }}
//         >
//             {children}
//         </AuthContext.Provider>
//     )
// }