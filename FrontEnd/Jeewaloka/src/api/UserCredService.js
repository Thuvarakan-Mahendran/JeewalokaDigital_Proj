import axios from "./axiosInstance";

// export const getUsers = async () => {
//     const response = await axios.get("users/getusers");
//     return response.data;
// };

export const saveUserCred = async (userCred) => {
    const response = await axios.post("userCreds/adduserCred", userCred);
    return response.data;
};

export const getUserID = async (userCred) => {
    const response = await axios.post("userCreds/findUserID/${userCred.username}", userCred);
    return response.data;
};

// export const editsUser = async (user) => {
//     const response = await axios.put(`users/updateUser/${user.uid}`, user);
//     return response.data;
// };

// export const deleteUser = async (id) => {
//     const response = await axios.delete(`users/userdel/${id}`);
//     return response.data;
// };