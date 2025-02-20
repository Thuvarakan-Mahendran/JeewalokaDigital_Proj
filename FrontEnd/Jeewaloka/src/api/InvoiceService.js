import axios from "./axiosInstance";

export const getBills = async () => {
    const response = await axios.get("Bill/getBills");
    return response.data;
};

export const saveBill = async (invoice) => {
    const response = await axios.post("Bill/saveBill", invoice);
    return response.data;
};

// export const editSupplier = async (supplier) => {
//     const response = await axios.put(`suppliers/editsupplier/${supplier.supplierId}`, supplier);
//     return response.data;
// };

// export const deleteSupplier = async (id) => {
//     const response = await axios.delete(`suppliers/deletesupplier/${id}`);
//     return response.data;
// };