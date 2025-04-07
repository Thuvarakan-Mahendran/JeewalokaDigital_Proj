// import { Navigate, Outlet } from "react-router-dom";
// import { useAuth } from "../api/AuthProvider";

// const ProtectedRoute = ({ allowedRoles }) => {
//     const { accessToken, role } = useAuth();

//     if (!accessToken) {
//         return <Navigate to="/login" />;
//     }

//     if (!allowedRoles.includes(role)) {
//         return <Navigate to="/unauthorized" />;
//     }

//     return <Outlet />;
// };

// export default ProtectedRoute;
