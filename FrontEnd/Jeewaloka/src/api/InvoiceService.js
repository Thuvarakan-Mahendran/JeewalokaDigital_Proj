import axios from "./axiosInstance";

export const getBills = async () => {
    const response = await axios.get("Bill/getBills");
    return response.data;
};

export const saveBill = async (invoice) => {
    const response = await axios.post("Bill/createBill", invoice);
    return response.data;
};

export const deleteBill = async (id) => {
    const response = await axios.delete(`Bill/deleteBill/${id}`);
    return response.data;
};