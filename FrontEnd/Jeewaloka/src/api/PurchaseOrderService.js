import axios from "./axiosInstance";

export const createPurchaseOrder = async (purchaseOrder) => {
  try {
    console.log("Sending Purchase Order to API:", purchaseOrder); // Log purchase order details
    const response = await axios.post("purchaseorder/createpurchaseorder", purchaseOrder);
    return response.data;
  } catch (error) {
    console.error("Error creating purchase order:", error.response ? error.response.data : error.message);
    throw error;
  }
};

export const getPurchaseOrder = async (id) => {
  try {
    console.log(`Fetching Purchase Order with ID: ${id}`); // Log ID before fetching
    const response = await axios.get(`purchaseorder/getpurchaseorder/${id}`);
    console.log("Fetched Purchase Order:", response.data); // Log fetched data
    return response.data;
  } catch (error) {
    console.error("Error fetching purchase order:", error.response ? error.response.data : error.message);
    throw error;
  }
};

export const getAllPurchaseOrders = async () => {
  try {
    console.log("Fetching all purchase orders...");
    const response = await axios.get("purchaseorder/getallpurchaseorders");
    console.log("All Purchase Orders:", response.data);
    return response.data;
  } catch (error) {
    console.error("Error fetching all purchase orders:", error.response ? error.response.data : error.message);
    throw error;
  }
};

export const updatePurchaseOrder = async (id, purchaseOrder) => {
  try {
    console.log(`Updating Purchase Order ID: ${id} with Data:`, purchaseOrder);
    const response = await axios.put(`purchaseorder/editpurchaseorder/${id}`, purchaseOrder);
    console.log("Update Response:", response.data);
    return response.data;
  } catch (error) {
    console.error("Error updating purchase order:", error.response ? error.response.data : error.message);
    throw error;
  }
};

export const deletePurchaseOrder = async (id) => {
  try {
    console.log(`Deleting Purchase Order ID: ${id}`);
    const response = await axios.delete(`purchaseorder/deletepurchaseorder/${id}`);
    console.log("Delete Response:", response.data);
    return response.data;
  } catch (error) {
    console.error("Error deleting purchase order:", error.response ? error.response.data : error.message);
    throw error;
  }
};
