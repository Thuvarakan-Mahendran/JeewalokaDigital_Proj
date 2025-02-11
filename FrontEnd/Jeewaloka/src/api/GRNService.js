import api from "./axiosInstance";

const GRNService = {
  createGRN: async (grnData) => {
    // Make sure the endpoint matches the controller mapping
    return api.post("/grns/creategrn", grnData);
  },

  getGRN: async (id) => {
    // Correct the URL for fetching a single GRN by ID
    return api.get(`/grns/getgrn/${id}`);
  },

  getAllGRNs: async () => {
    // Correct the URL for fetching all GRNs
    return api.get("/grns/getallgrns");
  },

  updateGRN: async (id, grnData) => {
    // Correct the URL for updating a GRN by ID
    return api.put(`/grns/editgrn/${id}`, grnData);
  },

  deleteGRN: async (id) => {
    // Correct the URL for deleting a GRN by ID
    return api.delete(`/grns/deletegrn/${id}`);
  },
};

export default GRNService;
