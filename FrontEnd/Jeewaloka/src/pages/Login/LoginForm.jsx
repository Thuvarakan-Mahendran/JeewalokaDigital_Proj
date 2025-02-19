import { useState } from "react";
import './LoginForm.css';
import { useNavigate } from "react-router-dom";

const LoginPage = () => {
  // useState hooks to store inputs
  const [loginUserName, setLoginUserName] = useState('');
  const [loginPassword, setLoginPassword] = useState('');
  const navigateTo = useNavigate();

  const loginSubmit = (e) => {
    e.preventDefault();
    // Navigate to the dashboard without authentication
    navigateTo('/dashboard');

    // Clear input fields
    setLoginUserName('');
    setLoginPassword('');
  };

  return (
    <div className="flex justify-center items-center min-h-screen bg-gray-100">
      <div className="bg-white p-8 rounded-2xl shadow-lg flex">
        {/* Login Section */}
        <div className="flex flex-col justify-center">
          <form onSubmit={loginSubmit}>
            {/* Input Fields */}
            <label className="block text-gray-700 mb-1">Username</label>
            <input
              type="text"
              id="username"
              placeholder="Enter your username"
              className="w-full p-2 border border-gray-300 rounded-lg mb-4"
              value={loginUserName}
              onChange={(event) => setLoginUserName(event.target.value)}
            />

            <label className="block text-gray-700 mb-1">Password</label>
            <input
              type="password"
              id="password"
              className="w-full p-2 border border-gray-300 rounded-lg"
              placeholder="••••••"
              value={loginPassword}
              onChange={(event) => setLoginPassword(event.target.value)}
            />

            {/* Login Button */}
            <button
              type="submit"
              className="w-full bg-blue-600 text-white py-2 rounded-lg mt-6 font-semibold hover:bg-blue-700"
            >
              Login
            </button>
          </form>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
