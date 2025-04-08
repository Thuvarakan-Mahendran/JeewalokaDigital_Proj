// import axios from "./axiosInstance";
import api from "./api"

export const getUsers = async () => {
    const response = await api.get("users/getusers");
    return response.data;
};

export const saveUser = async (user) => {
    const response = await api.post("users/adduser", user);
    return response.data;
};

export const editsUser = async (user) => {
    const response = await api.put(`users/updateUser/${user.uid}`, user);
    return response.data;
};

export const deleteUser = async (id) => {
    const response = await api.delete(`users/userdel/${id}`);
    return response.data;
};