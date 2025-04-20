// import api from './axiosInstance'
// import { getAccessToken, setAccessToken, removeAccessToken } from './TokenService'
// import { refreshToken, logout } from './AuthService'
// // import { logout } from './AuthProvider'


// // --- CONCURRENCY CONTROL ---
// let isRefreshing = false; // Flag to indicate if a refresh is already in progress
// let failedQueue = [];     // Queue to hold requests that failed while refresh was in progress

// // Function to process the queue after refresh attempt
// const processQueue = (error, token = null) => {
//     failedQueue.forEach(prom => {
//         if (error) {
//             prom.reject(error); // Reject waiting requests if refresh failed
//         } else {
//             prom.resolve(token); // Resolve waiting requests with the new token if refresh succeeded
//         }
//     });
//     failedQueue = []; // Clear the queue
// };
// // --- END CONCURRENCY CONTROL ---




// // Add request interceptor to attach access token
// api.interceptors.request.use((config) => {
//     const accessToken = getAccessToken()
//     console.log("intercepter access token ", accessToken)
//     if (accessToken) {
//         console.log("attached access token is ", accessToken)
//         config.headers.Authorization = `Bearer ${accessToken}`
//     }
//     return config
// }, (error) => {
//     return Promise.reject(error);
// })


// // Refresh Access Token once it's expired
// // api.interceptors.response.use(
// //     (response) => response,
// //     async (error) => {
// //         const originalRequest = error.config;
// //         console.log("inside the interceptor to create new access token")
// //         if (error.response && error.response.status === 401 && !originalRequest._retry) {
// //             originalRequest._retry = true;
// //             console.log("inside the if statement")
// //             try {
// //                 // const { refreshAccessToken } = useAuth();
// //                 const newAccessToken = await refreshToken();
// //                 setAccessToken(newAccessToken);
// //                 originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;
// //                 console.log("try,new access token was added")
// //                 return api(originalRequest);
// //             } catch (refreshError) {
// //                 console.log("error occured now in catch")
// //                 removeAccessToken();
// //                 console.error("Refresh Failed. Logging out ... ");
// //                 await logout()
// //                 window.location.href = '/login'
// //                 return Promise.reject(refreshError)
// //             }
// //         }
// //         return Promise.reject(error);
// //     }
// // )


// // api.interceptors.response.use(
// //     (response) => response,
// //     async (error) => {
// //         const originalRequest = error.config;
// //         // Define the relative paths that should NOT trigger a refresh attempt
// //         const refreshTokenUrlPath = 'auth/refresh-token';
// //         const logoutUrlPath = 'auth/logout'; // <-- Define logout path

// //         // Check if the failed request URL is one we want to ignore for refresh attempts
// //         const isIgnoredUrl = originalRequest.url === refreshTokenUrlPath || originalRequest.url === logoutUrlPath; // <-- Check against both

// //         console.log(`Response Interceptor: Error Status: ${error.response?.status} for URL: ${originalRequest.url}`);

// //         // --- CONDITIONS TO ATTEMPT REFRESH ---
// //         // 1. We have an error response with status 401
// //         // 2. This specific request config hasn't been retried yet
// //         // 3. The URL of the failed request IS NOT the refresh token URL OR the logout URL
// //         if (error.response?.status === 401 &&
// //             !originalRequest._retry &&
// //             !isIgnoredUrl) // <-- Use the combined check
// //         {
// //             console.log(`Response Interceptor: Caught 401 for non-refresh/non-logout request: ${originalRequest.url}. Attempting refresh.`);
// //             originalRequest._retry = true; // Mark this request config as retried

// //             try {
// //                 const newAccessToken = await refreshToken(); // This uses api.post internally
// //                 console.log("Response Interceptor: Token refresh successful.");
// //                 setAccessToken(newAccessToken);

// //                 // Update header for the *current* original request being handled
// //                 api.defaults.headers.common['Authorization'] = `Bearer ${newAccessToken}`; // Update default for subsequent requests
// //                 originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;

// //                 console.log("Response Interceptor: Retrying original request:", originalRequest.url);
// //                 return api(originalRequest); // Retry the original request

// //             } catch (refreshError) {
// //                 // This catch block handles errors specifically from the refreshToken() call
// //                 console.error("Response Interceptor: Refresh token attempt failed:", refreshError?.response?.data || refreshError?.message || refreshError);

// //                 // Logout logic - initiated because refresh failed
// //                 removeAccessToken();
// //                 try {
// //                     // We call logout, but we DON'T await it typically if redirecting immediately
// //                     // Also, we don't want a failure here to re-trigger the interceptor's refresh logic
// //                     console.log("Response Interceptor: Calling logout API (fire and forget).");
// //                     logout(); // Call it, but don't await if redirecting right after
// //                 } catch (logoutErr) {
// //                     // Log secondary error, but proceed with redirect
// //                     console.error("Response Interceptor: Error during logout API call:", logoutErr);
// //                 }
// //                 console.log("Response Interceptor: Redirecting to login due to refresh failure.");
// //                 window.location.href = '/login'; // Redirect
// //                 // Crucially, reject the promise associated with the *original* request
// //                 // that first triggered the 401. We give it the refreshError details.
// //                 return Promise.reject(refreshError);
// //             }
// //         } // End of the main 'if' for handling 401 on eligible requests

// //         // If conditions aren't met (not 401, already retried, or ignored URL), reject the error.
// //         console.log(`Response Interceptor: Not attempting refresh/retry for ${originalRequest.url}. Status: ${error.response?.status}, Retried: ${originalRequest._retry}, Ignored URL: ${isIgnoredUrl}`);
// //         return Promise.reject(error);
// //     }
// // );



// // --- Response Interceptor ---
// api.interceptors.response.use(
//     (response) => response, // Pass through successful responses
//     async (error) => {
//         const originalRequest = error.config;
//         const refreshTokenUrlPath = 'auth/refresh-token';
//         const logoutUrlPath = 'auth/logout';
//         const isIgnoredUrl = originalRequest.url === refreshTokenUrlPath || originalRequest.url === logoutUrlPath;

//         console.log(`Response Interceptor: Error Status: ${error.response?.status} for URL: ${originalRequest.url}`);

//         // --- Main Refresh Condition ---
//         if (error.response?.status === 401 && !isIgnoredUrl) // Check for 401 and ensure it's not an ignored URL
//         {
//             // --- Concurrency Check ---
//             if (isRefreshing) {
//                 // If already refreshing, queue this request instead of starting another refresh
//                 console.log(`Response Interceptor: Refresh in progress. Queueing request for ${originalRequest.url}`);
//                 return new Promise((resolve, reject) => {
//                     failedQueue.push({ resolve, reject });
//                 }).then(token => {
//                     // This runs after the ongoing refresh completes (successfully)
//                     console.log(`Response Interceptor: Resuming queued request for ${originalRequest.url} with new token.`);
//                     originalRequest.headers['Authorization'] = 'Bearer ' + token;
//                     originalRequest._retry = true; // Mark as retried *after* getting new token
//                     return api(originalRequest); // Retry the request
//                 }).catch(err => {
//                     // This runs if the ongoing refresh attempt ultimately failed
//                     console.error(`Response Interceptor: Refresh failed for queued request ${originalRequest.url}. Propagating error.`);
//                     return Promise.reject(err); // Don't retry, propagate the refresh error
//                 });
//             }
//             // --- End Concurrency Check ---


//             // --- Initiate Refresh ---
//             // Only the *first* 401 gets here if multiple occur concurrently
//             console.log(`Response Interceptor: Initiating token refresh for request: ${originalRequest.url}`);
//             originalRequest._retry = true; // Mark original request config (important if retry fails later)
//             isRefreshing = true;           // Set the flag

//             return new Promise(async (resolve, reject) => { // Wrap refresh logic in a promise
//                 try {
//                     const newAccessToken = await refreshToken();
//                     console.log("Response Interceptor: Token refresh successful.");
//                     setAccessToken(newAccessToken);
//                     api.defaults.headers.common['Authorization'] = `Bearer ${newAccessToken}`; // Update defaults

//                     // Process the queue with the new token
//                     processQueue(null, newAccessToken);

//                     // Resolve the promise for the *original* request retry
//                     console.log(`Response Interceptor: Resolving original request retry for ${originalRequest.url}`);
//                     originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;
//                     resolve(api(originalRequest)); // Resolve with the retried request

//                 } catch (refreshError) {
//                     console.error("Response Interceptor: Refresh token attempt failed:", refreshError?.response?.data || refreshError?.message || refreshError);

//                     // Process the queue with the error
//                     processQueue(refreshError, null);

//                     // Logout logic
//                     removeAccessToken();
//                     try {
//                         logout(); // Fire-and-forget
//                     } catch (logoutErr) {
//                         console.error("Response Interceptor: Error during logout API call:", logoutErr);
//                     }
//                     console.log("Response Interceptor: Redirecting to login due to refresh failure.");
//                     window.location.href = '/login';

//                     // Reject the promise associated with the *original* request
//                     reject(refreshError);

//                 } finally {
//                     // IMPORTANT: Reset the flag *after* the refresh attempt (success or fail)
//                     // and after processing the queue.
//                     isRefreshing = false;
//                     console.log("Response Interceptor: Refresh attempt finished.");
//                 }
//             }); // End of the promise wrapping the refresh logic

//         } // End of main 'if' for 401 handling

//         console.log(`Response Interceptor: Not attempting refresh/retry for ${originalRequest.url}. Status: ${error.response?.status}, Ignored URL: ${isIgnoredUrl}`);
//         return Promise.reject(error); // Reject errors that don't meet the criteria
//     }
// );



// export default api;



import api from './axiosInstance'; // Use your actual axios instance import
import { getAccessToken, setAccessToken, removeAccessToken } from './TokenService';
// Import refreshToken function directly
import { refreshToken as refreshAuthToken } from './AuthService';
// Import logout function to handle final failure
// You might need to adapt this if logout is managed purely by AuthContext
// Option 1: Direct import (if AuthService.logout handles necessary cleanup)
import { logout as logoutUser } from './AuthService';
// Option 2: Get logout from context (more complex setup needed to pass context here)
// import { AuthContext } from '../Context/AuthContext'; // Example path

let isRefreshing = false;
let failedQueue = [];

const processQueue = (error, token = null) => {
    failedQueue.forEach(prom => {
        if (error) {
            prom.reject(error);
        } else {
            prom.resolve(token);
        }
    });
    failedQueue = [];
};

// Request Interceptor (Existing) - Adds the current token to requests
// api.interceptors.request.use(
//     (config) => {
//         const accessToken = getAccessToken();
//         // console.log("Request Interceptor - Token:", accessToken); // Debug
//         if (accessToken) {
//             config.headers['Authorization'] = `Bearer ${accessToken}`;
//             // console.log("Request Interceptor - Header Set:", config.headers['Authorization']); // Debug
//         }
//         return config;
//     },
//     (error) => {
//         return Promise.reject(error);
//     }
// );


api.interceptors.request.use(
    (config) => {
        // Check if the Authorization header is already set (e.g., by the retry logic)
        // Use a case-insensitive check for the header key
        const authHeaderKey = Object.keys(config.headers).find(key => key.toLowerCase() === 'authorization');

        if (!authHeaderKey) { // Only add the header if it doesn't exist yet
            const accessToken = getAccessToken();
            // console.log("Request Interceptor - Token from localStorage:", accessToken); // Debug
            if (accessToken) {
                config.headers['Authorization'] = `Bearer ${accessToken}`;
                // console.log("Request Interceptor - Header Set by Request Interceptor:", config.headers['Authorization']); // Debug
            }
        } else {
            console.log("Request Interceptor - Authorization header already present, not overwriting:", config.headers[authHeaderKey]); // Debug log
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);


// Response Interceptor (New) - Handles token expiry and refresh
api.interceptors.response.use(
    (response) => {
        // Any status code that lie within the range of 2xx cause this function to trigger
        console.log("response has status code ", response)
        return response;
    },
    async (error) => {
        // Any status codes that falls outside the range of 2xx cause this function to trigger
        const originalRequest = error.config;
        console.log("Response Interceptor Error Status:", error.response?.status); // Debug
        // console.log("Original Request URL:", originalRequest.url); // Debug

        // Check if it's a 401 error and not a retry request,
        // and the failed request was not for the refresh token endpoint itself
        console.log("going for if statement")
        if (error.response?.status === 401 && !originalRequest._retry && originalRequest.url !== '/auth/refresh-token') {
            console.log(isRefreshing);
            // Prevent multiple refresh attempts concurrently
            if (isRefreshing) {
                console.log("Response Interceptor - Already refreshing, queuing request:", originalRequest.url); // Debug
                return new Promise(function (resolve, reject) {
                    failedQueue.push({ resolve, reject });
                }).then(token => {
                    console.log("Response Interceptor - Resuming queued request with new token:", originalRequest.url); // Debug
                    originalRequest.headers['Authorization'] = 'Bearer ' + token;
                    return api(originalRequest); // Retry with new token
                }).catch(err => {
                    return Promise.reject(err); // Propagate refresh failure
                });
            }

            console.log("Response Interceptor - Access token expired or invalid. Attempting refresh."); // Debug
            originalRequest._retry = true; // Mark as retried
            isRefreshing = true;

            try {
                // Call your refresh token endpoint
                const newAccessToken = await refreshAuthToken(); // This function should return only the new access token string
                console.log("Response Interceptor - Refresh successful. New Token:", newAccessToken); // Debug

                // Update token in localStorage
                setAccessToken(newAccessToken);

                // Update the Authorization header for the original request
                originalRequest.headers['Authorization'] = `Bearer ${newAccessToken}`;

                // Update the default Authorization header for subsequent requests
                api.defaults.headers.common['Authorization'] = `Bearer ${newAccessToken}`;

                // Process the queue with the new token BEFORE retrying the original request
                processQueue(null, newAccessToken);

                // Retry the original request with the new token
                console.log("Response Interceptor - Retrying original request:", originalRequest.url); // Debug
                return api(originalRequest);

            } catch (refreshError) {
                console.error("Response Interceptor - Token refresh failed:", refreshError); // Debug

                // Process the queue with the error
                processQueue(refreshError, null);

                // Refresh failed, logout user
                removeAccessToken(); // Clear invalid token
                // Trigger logout - Choose ONE method:
                // Method 1: Call AuthService logout (if it handles redirects etc.)
                try {
                    await logoutUser(); // Assuming this clears cookies/server session
                    window.location.href = '/login'; // Force redirect if not handled by logoutUser
                } catch (logoutErr) {
                    console.error("Logout after refresh failure failed:", logoutErr);
                    window.location.href = '/login'; // Force redirect anyway
                }
                // Method 2: If logout is managed by AuthContext, you'd need a way
                // to call the context's logout function here. This often involves
                // event emitters or passing context down, which complicates the interceptor.
                // Example (conceptual): globalEventEmitter.emit('logout');

                return Promise.reject(refreshError); // Reject the original request's promise
            } finally {
                isRefreshing = false;
                console.log("Response Interceptor - Refreshing status set to false."); // Debug
            }
        } else if (error.response?.status === 401 && originalRequest.url === '/auth/refresh-token') {
            // Specific handling if the refresh token endpoint itself returns 401
            console.error("Response Interceptor - Refresh token is invalid or expired. Logging out.");
            removeAccessToken();
            // Trigger logout (similar to above catch block)
            try {
                await logoutUser();
                window.location.href = '/login';
            } catch (logoutErr) {
                console.error("Logout after refresh failure failed:", logoutErr);
                window.location.href = '/login';
            }
            return Promise.reject(error);
        }

        // For errors other than 401, just return the error
        return Promise.reject(error);
    }
);

export default api; // Ensure you export the configured instance