// import jwtDecode from "jwt-decode";

// export const decodeToken = (token) => {
//     try {
//         return jwtDecode(token);
//     } catch (error) {
//         console.error("Error decoding token:", error);
//         return null;
//     }
// };

// export const isTokenExpired = (token) => {
//     const decoded = decodeToken(token);
//     if (!decoded?.exp) return true;
//     return decoded.exp * 1000 < Date.now();
// };