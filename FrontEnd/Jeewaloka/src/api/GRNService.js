import api from "./axiosInstance";

export const createGRN = async (grnData) => {
  try {
    const response = await api.post("/grns/creategrn", grnData);
    return response.data;
  } catch (error) {
    console.error("Error creating GRN:", error);
    throw error;
  }
};

export const getGRN = async (id) => {
  try {
    const response = await api.get(`/grns/getgrn/${id}`);
    return response.data;
  } catch (error) {
    console.error("Error fetching GRN:", error);
    throw error;
  }
};

export const getAllGRNs = async () => {
  try {
    const response = await api.get("/grns/getallgrns");
    return response.data;
  } catch (error) {
    console.error("Error fetching all GRNs:", error);
    throw error;
  }
};

export const updateGRN = async (id, grnData) => {
  try {
    const response = await api.put(`/grns/editgrn/${id}`, grnData);
    return response.data;
  } catch (error) {
    console.error("Error updating GRN:", error);
    throw error;
  }
};

export const deleteGRN = async (id) => {
  try {
    const response = await api.delete(`/grns/deletegrn/${id}`);
    return response.data;
  } catch (error) {
    console.error("Error deleting GRN:", error);
    throw error;
  }
};
