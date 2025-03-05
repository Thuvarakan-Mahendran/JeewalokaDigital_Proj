import axios from "./axiosInstance";

export const getRetailers = async () => {
    const response = await axios.get("retailer/getAllRetailers");
    return response.data;
};

// export const saveRetailer = async (supplier) => {
//     const response = await axios.post("suppliers/savesupplier", supplier);
//     return response.data;
// };

// export const editRetailer = async (supplier) => {
//     const response = await axios.put(`suppliers/editsupplier/${supplier.supplierId}`, supplier);
//     return response.data;
// };

// export const deleteRetailer = async (id) => {
//     const response = await axios.delete(`suppliers/deletesupplier/${id}`);
//     return response.data;
// };