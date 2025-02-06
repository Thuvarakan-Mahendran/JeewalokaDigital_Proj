import axios from "./axiosInstance";

const GRNService = {
  createGRN: async (grnData) => {
    return axios.post("/grns/create", grnData);
  },

  getGRN: async (id) => {
    return axios.get(`/grns/${id}`);
  },

  getAllGRNs: async () => {
    return axios.get("/grns/all");
  },

  updateGRN: async (id, grnData) => {
    return axios.put(`/grns/update/${id}`, grnData);
  },

  deleteGRN: async (id) => {
    return axios.delete(`/grns/delete/${id}`);
  },
};

export default GRNService;
