import React from "react";

const LoginPage = () => {
  return (
    <div className="flex justify-center items-center min-h-screen bg-gray-100">
      <div className="bg-white p-8 rounded-2xl shadow-lg flex">
        {/* Photo Section */}
        <div className="w-64 h-64 bg-black text-white flex items-center justify-center text-xl font-bold rounded-xl">
          Photo
        </div>
        
        {/* Login Section */}
        <div className="ml-8 flex flex-col justify-center">
          {/* Logo */}
          <div className="bg-black text-white text-center py-2 px-6 text-lg font-semibold rounded-lg mb-6">
            Logo
          </div>
          
          {/* Input Fields */}
          <label className="block text-gray-700 mb-1">Username</label>
          <input
            type="text"
            placeholder="Enter your username"
            className="w-full p-2 border border-gray-300 rounded-lg mb-4"
          />
          
          <label className="block text-gray-700 mb-1">Password</label>
          <div className="relative w-full">
            <input
              type="password"
              className="w-full p-2 border border-gray-300 rounded-lg pr-10"
              placeholder="••••••"
            />
          </div>
          
          {/* Login Button */}
          <button className="w-full bg-blue-600 text-white py-2 rounded-lg mt-6 font-semibold hover:bg-blue-700">
            Login
          </button>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
