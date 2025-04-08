import { Axis3D } from "lucide-react";
// import axios from "./axiosInstance";
import api from "./api"

// export const getUsers = async () => {
//     const response = await axios.get("users/getusers");
//     return response.data;
// };

export const saveUserCred = async (userCred) => {
    const response = await api.post("userCreds/adduserCred", userCred);
    return response.data;
};

export const getUserID = async (userCred) => {
    const response = await api.get(`userCreds/findUserID/${userCred.username}`, userCred);
    return response.data;
};

// export const getCredRole = async (userCred) => {
//     const response = await axios.get(`userCreds/getCredRole/${userCred.username}`, userCred);
//     return response.data;
// };

export const getUserCreds = async () => {
    const response = await api.get("userCreds/getUserCreds");
    return response.data;
};

// export const editsUser = async (user) => {
//     const response = await axios.put(`users/updateUser/${user.uid}`, user);
//     return response.data;
// };

export const deleteUserCred = async (id) => {
    const response = await api.delete(`userCreds/deleteUserCred/${id}`);
    return response.data;
};