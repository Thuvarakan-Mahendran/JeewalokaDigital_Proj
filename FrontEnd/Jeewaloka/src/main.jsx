import React from "react";
import ReactDOM from "react-dom/client";
import App from "./App";
import { AuthProvider } from './Context/AuthContext'
import { WebSocketProvider } from "./Context/WebSocketContext";

ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <AuthProvider>
      <WebSocketProvider>
        <App />
      </WebSocketProvider>
    </AuthProvider>
  </React.StrictMode>
);

