// import { createContext, useState, useEffect, useContext } from "react";
// import api from "./axiosInstance"

// const AuthContext = createContext(null);

// export function AuthProvider({ children }) {
//     const [user, setUser] = useState(null);
//     const [accessToken, setAccessToken] = useState(localStorage.getItem("accessToken") || null)
//     const [role, setRole] = useState(null)

//     useEffect(() => {
//         if (accessToken) {
//             fetchUserDetails();
//         }
//     }, [accessToken])

//     const fetchUserDetails = async () => {
//         try {
//             const response = await api.get("/user", {
//                 headers: { Authorization: `Bearer ${accessToken}` },
//             });
//             setUser(response.data);
//             setRole(response.data.role);
//         } catch (error) {
//             console.error("session fetch failed: ", error)
//             logout();
//         }
//     }

//     const login = async (credentials) => {
//         try {
//             const response = await api.post("/auth/login", credentials);
//             const accessToken = response.headers.getAuthorization.split(" ")[1];
//             localStorage.setItem("accessToken", accessToken);
//             setAccessToken(accessToken);
//             fetchUserDetails();
//         } catch (error) {
//             console.error("login failed: ", error);
//             throw error;
//         }
//     }

//     const logout = async () => {
//         try {
//             await api.post("/auth/logout");
//         } catch (error) {
//             console.error("logout failed: ", error);
//         }
//         setUser(null);
//         setAccessToken(null);
//         setRole(null);
//         localStorage.removeItem("accessToken");
//     }

//     const refreshAccessToken = async () => {
//         try {
//             const response = await api.post("/auth/refresh-token");
//             const newAccessToken = response.data;
//             localStorage.setItem("accessToken", newAccessToken);
//             setAccessToken(newAccessToken);
//             return newAccessToken;
//         } catch (error) {
//             console.error("refresh token failed: ", error);
//             logout();
//         }
//     }

//     return (
//         <AuthContext.Provider value={{ user, accessToken, role, login, logout, refreshAccessToken }}>
//             {children}
//         </AuthContext.Provider>

//     )
// }

// export function useAuth() {
//     return useContext(AuthContext);
// }