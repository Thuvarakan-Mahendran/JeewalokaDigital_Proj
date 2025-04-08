import axios from "./axiosInstance"

export const saveBillItem = async () => {
    const response = await axios.post("BillItem/createBillItems")
    return response.data
}