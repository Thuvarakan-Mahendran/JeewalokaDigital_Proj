import { useState, useEffect, useRef, useContext } from "react";
import './LoginForm.css';
import axios from "axios";
import logo from '../Login/logo.jpg';
import lgphoto from '../Login/loginphoto.jpg';
import { AuthContext } from "../../Context/AuthContext";
const LOGIN_URL = '/api/auth/login'



const LoginPage = () => {
  const [user, setUser] = useState({
    username: '',
    password: ''
  })

  const handleChange = (e) => {
    const { name, value } = e.target
    setUser(prev => ({
      ...prev,
      [name]: value
    }))
  }

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post('', user);

      // Store the token in localStorage
      localStorage.setItem('token', response.data.token);

      // Redirect or update state
      window.location.href = '/dashboard'
    } catch (error) {
      console.error('Login failed:', error.response.data)
    }
  }

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
          <form onSubmit={handleSubmit}>
            {/* <span className={statusHolder}>{loginStatus}</span> {/* Check this work or not */}
            {/* Input Fields */}
            <label className="block text-gray-700 mb-1">Username</label>
            <input
              type="text"
              id="username"
              placeholder="Enter your username"
              className="w-full p-2 border border-gray-300 rounded-lg mb-4"
              onChange={handleChange}
            />

            <label className="block text-gray-700 mb-1">Password</label>
            <div className="relative w-full">
              <input
                type="password"
                id="password"
                className="w-full p-2 border border-gray-300 rounded-lg pr-10"
                placeholder="••••••"
                onChange={handleChange}
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
