import axios from "./axiosInstance";

export const getSellers = async () => {
    const response = await axios.get("retailer/getAllRetailers");
    return response.data;
};

export const saveSellers = async (retailer) => {
    const response = await axios.post("retailer/createRetailer", retailer);
    return response.data;
};

export const editSeller = async (retailer) => {
    const response = await axios.put(`retailer/updateRetailer/${retailer.retailerId}`, retailer);
    return response.data;
};

export const deleteSeller = async (id) => {
    const response = await axios.delete(`retailer/deleteRetailer/${id}`);
    return response.data;
};