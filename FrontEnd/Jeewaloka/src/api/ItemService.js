import axios from "./axiosInstance";

export const getItems = async () => {
    const response = await axios.get("items/getitems");
    return response.data;
};

export const saveItem = async (item) => {
    const response = await axios.post("items/saveitem", item);
    return response.data;
};

export const editItem = async (item) => {
    const response = await axios.put(`items/edititem/${item.itemCode}`, item);
    return response.data;
};

export const deleteItem = async (id) => {
    const response = await axios.delete(`items/deleteitem/${id}`);
    return response.data;
};
