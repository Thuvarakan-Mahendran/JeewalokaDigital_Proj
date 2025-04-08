import React, { useState, useContext, useEffect } from "react";
import './LoginForm.css';
// import axios from "axios";
// import { useHistory } from 'react-router-dom'
import { login } from '../../api/AuthService'
import { setAccessToken } from '../../api/TokenService'
import { jwtDecode } from "jwt-decode";
import logo from './logo.png';
import lgphoto from './loginphoto.jpg';
import { AuthContext } from "../../Context/AuthContext";
import { useNavigate } from 'react-router-dom';



const LoginPage = () => {
  const [userCred, setUserCred] = useState({
    username: '',
    password: ''
  })
  const [error, setError] = useState('');
  const { setUser } = useContext(AuthContext);
  const navigate = useNavigate();
  // const history = useHistory();

  // useEffect(() => {

  // }, [userCred])

  const handleChange = (e) => {
    const { name, value } = e.target
    // consol e.log(value)
    setUserCred(prev => ({
      ...prev,
      [name]: value
    }))
  }

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('')
    try {
      // console.log(userCred)
      const response = await login(userCred);
      console.log(response)
      setAccessToken(response)
      const decoded = jwtDecode(response);
      console.log("username is ", decoded.sub)
      console.log("role is ", decoded.role)
      setUser({ username: decoded.sub, role: decoded.role });
      // history.push('/home');
      // Store the token in localStorage
      // localStorage.setItem('token', response.data.token);

      // Redirect or update state
      // window.location.href = '/dashboard'
      navigate('/dashboard', { replace: true })
    } catch (error) {
      console.error('Login failed:', error)
      setError('Invalid username or password')
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
              name="username"
              placeholder="Enter your username"
              className="w-full p-2 border border-gray-300 rounded-lg mb-4"
              value={userCred.username}
              onChange={handleChange}
              required
            />

            <label className="block text-gray-700 mb-1">Password</label>
            <div className="relative w-full">
              <input
                type="password"
                id="password"
                name="password"
                className="w-full p-2 border border-gray-300 rounded-lg pr-10"
                placeholder="••••••"
                value={userCred.password}
                onChange={handleChange}
                required
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
