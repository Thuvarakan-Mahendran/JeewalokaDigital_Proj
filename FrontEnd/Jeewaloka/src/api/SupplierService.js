import axios from "./axiosInstance";

export const getSuppliers = async () => {
    const response = await axios.get("suppliers/getallsuppliers");
    return response.data;
};

export const saveSupplier = async (supplier) => {
    const response = await axios.post("suppliers/savesupplier", supplier);
    return response.data;
};

export const editSupplier = async (supplier) => {
    const response = await axios.put(`suppliers/editsupplier/${supplier.supplierId}`, supplier);
    return response.data;
};

export const deleteSupplier = async (id) => {
    const response = await axios.delete(`suppliers/deletesupplier/${id}`);
    return response.data;
};