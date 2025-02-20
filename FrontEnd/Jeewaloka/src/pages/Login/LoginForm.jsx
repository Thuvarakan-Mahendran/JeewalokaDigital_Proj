import { useState } from "react";
import './LoginForm.css';
import { useNavigate } from "react-router-dom";
//import Axios from "axios";
//import React, { useEffect, useState } from "react";
import logo from '../Login/logo.jpg'; // Provide the correct path for logo
import lgphoto from '../Login/loginphoto.jpg'; // Provide the correct path for the photo

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
   // loginUser(e); // Call the login function
  };
  // show the message to the user
 /* const [loginStatus, setLoginStatus] = useState('');
  const [statusHolder, setStatusHolder] = useState('message');
 */
 /* const loginUser = (e) => {
    e.preventDefault();
    Axios.post('http://localhost:8080/api', {
      LoginUserName: loginUserName,
      LoginPassword: loginPassword,
    }).then((response) => {
      if (response.data.message || loginUserName === '' || loginPassword === '') {
        navigateTo('/'); // same login page 
        setLoginStatus(`Credentials Don't Exist!`);
      } else {
        navigateTo('/dashboard'); // after login success page "dashboard"
      }
    });
  }; 
  */
 /* useEffect(() => {
    if (loginStatus !== '') {
      setStatusHolder('showMessage');
      setTimeout(() => {
        setStatusHolder('message');
      }, 4000);
    }
  }, [loginStatus]);
*/


  return (
    <div className="flex justify-center items-center min-h-screen bg-gray-100">
      <div className="bg-white p-8 rounded-2xl shadow-lg flex">
        {/* Photo Section */}
        <div className="w-64 h-64 flex items-center justify-center text-xl font-bold rounded-xl overflow-hidden">
          <img src={lgphoto} alt="Company Image" className="w-full h-full object-cover" onError={(e) => e.target.style.display = 'none'} />
        </div>

        {/* Login Section */}
        <div className="ml-8 flex flex-col justify-center">
          {/* Logo */}
          <div className="flex justify-center mb-6">
            <img src={logo} alt="Logo Image" className="w-24 h-24" onError={(e) => e.target.style.display = 'none'} />
          </div>
          <form onSubmit={loginSubmit}>
           {/* <span className={statusHolder}>{loginStatus}</span> {/* Check this work or not */}     
            {/* Input Fields */}
            <label className="block text-gray-700 mb-1">Username</label>
            <input
              type="text"
              id="username"
              placeholder="Enter your username"
              className="w-full p-2 border border-gray-300 rounded-lg mb-4"
              onChange={(event) => setLoginUserName(event.target.value)}
            />

            <label className="block text-gray-700 mb-1">Password</label>
            <div className="relative w-full">
              <input
                type="password"
                id="password"
                className="w-full p-2 border border-gray-300 rounded-lg pr-10"
                placeholder="••••••"
                onChange={(event) => setLoginPassword(event.target.value)}
              />
            </div>

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
