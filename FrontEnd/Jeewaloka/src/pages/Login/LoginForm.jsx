import React, {useEffect, useState} from "react";
import './LoginForm.css'
import {useNavigate} from "react-router-dom" 
import Axios from "axios";
 





import logo from ''
import lgphoto from ''





const LoginPage = () => {
      //usestate hook to store inputs
      const [loginUserName, setLoginUserName]=useState('')
      const [loginPassword, setLoginPassword]=useState('')
      const navigateTo= useNavigate()

      //show the massage to the user
      const [loginStatus, setLoginStatus]=useState('')
      const [statusHolder, setStatusHolder]=useState('message')

  const loginUser=(e)=>{

   e.preventDefault();
      Axios.post('http://localhost:8080/api' ,{
        LoginUserName:loginUserName,
        LoginPassword:loginPassword
      }).then((response)=>{
         console.log()
         if(response.data.message || loginUserName=='' || loginPassword==''){
           navigateTo('/')     // same login page 
           setLoginStatus(`Credentials Don't Exist!`)    
          }
         else{
           navigateTo('/dashboard') //after login succsess page"dashboard"

          }
          

     })


  } 


  useEffect(()=>{
    if(loginStatus !== ''){
      setStatusHolder('showMessage')
      setTimeout(()=>{
        setStatusHolder('message')
      },4000)
    }
  },[loginStatus])

  const loginSubmit =()=>{
    setLoginUserName('')
    setLoginPassword('')
  }



  return (
      <div className="flex justify-center items-center min-h-screen bg-gray-100">
        <div className="bg-white p-8 rounded-2xl shadow-lg flex">
          {/* Photo Section */}
          <div className="w-64 h-64 bg-black text-white flex items-center justify-center text-xl font-bold rounded-xl">
          <img src={lgphoto} alt="Company Image" />
          </div>
        
         {/* Login Section */}
         <div className="ml-8 flex flex-col justify-center">
            {/* Logo */}
           <div className="bg-black text-white text-center py-2 px-6 text-lg font-semibold rounded-lg mb-6">
             <img src={logo} alt="Logo Image" />
           </div>
            <form onSubmit={loginSubmit}>
              <span className={setStatusHolder }>{loginStatus}</span> {/* chek this work or not */}
             {/* Input Fields */}
              <label className="block text-gray-700 mb-1">Username</label>
              <input
                type="text"
                id="username"
                placeholder="Enter your username"
                className="w-full p-2 border border-gray-300 rounded-lg mb-4"
                onChange={(event)=>{
                  setLoginUserName(event.target.value)
                }}
              />
          
              <label className="block text-gray-700 mb-1">Password</label>
              <div className="relative w-full">
               <input
                 type="password"
                 id="password"
                  className="w-full p-2 border border-gray-300 rounded-lg pr-10"
                 placeholder="••••••"
                 onChange={(event)=>{
                    setLoginPassword(event.target.value)
                  }}
                />
              </div>
              
              {/* Login Button */}
              <button type="submit" className="w-full bg-blue-600 text-white py-2 rounded-lg mt-6 font-semibold hover:bg-blue-700" 
              onClick={loginUser}>
               Login
              </button>
            </form>
          </div>
        </div>
      </div>
  );
};

export default LoginPage;
