import React, { useContext } from 'react';
import { Navigate } from 'react-router-dom';
import { AuthContext } from '../Context/AuthContext';

const ProtectedRoute = ({ children, roles }) => {
    const { user } = useContext(AuthContext);

    if (!user) {
        return <Navigate to="/login" replace />;
    }

    // If the user's role is not in the allowed roles, redirect to an unauthorized page.
    if (roles && !roles.includes(user.role)) {
        return <Navigate to="/unauthorized" replace />;
    }

    return children;

    // return (
    //     <Route
    //         {...rest}
    //         render={(props) => {
    //             if (!user) return <Redirect to="/login" />;
    //             if (roles && !roles.includes(user.role))
    //                 return <Redirect to="/unauthorized" />;
    //             return <Component {...props} />;
    //         }}
    //     />
    // );
};

export default ProtectedRoute;




// import React from 'react';
// import { Navigate } from 'react-router-dom';

// const ProtectedRoute = ({ children }) => {
//     const token = localStorage.getItem('token');

//     if (!token) {
//         return <Navigate to="/login" replace />;
//     }

//     return children;
// };

// export default ProtectedRoute;