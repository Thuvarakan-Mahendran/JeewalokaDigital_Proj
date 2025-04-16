// import axios from "./axiosInstance";
import api from "./api"

export const login = async (usercred) => {
    try {
        const response = await api.post("auth/login", usercred);
        // return {
        //     accessToken: response.headers.getAuthorization.split("Bearer ")[1]
        // }
        // const authHeader = response.headers['authorization'] || response.headers['Authorization']
        // if (authHeader && authHeader.startsWith('Bearer ')) {
        //     const accessToken = authHeader.split('Bearer ')[1]
        //     return { accessToken }
        // }
        return response.data;
    } catch (error) {
        throw error.response?.data || "Login failed"
    }
}

export const logout = async () => {
    try {
        const response = await api.post("auth/logout")
        return response.data
    } catch (error) {
        console.error("logout failed: ", error)
    }
}

export const refreshToken = async () => {
    try {
        const response = await api.post("auth/refresh-token")
        return response.data
    } catch (error) {
        throw error.response?.data || "Token refresh failed"
    }
}

export const getSessions = async () => {
    try {
        const response = await api.get("auth/sessions")
        return response.data
    } catch (error) {
        console.error("Get all the sessions failed: ", error)
    }
}

export const revokeAllSessions = async () => {
    try {
        const response = await api.delete("auth/revokeAllSessions")
        return response.data
    } catch (error) {
        console.error("Revoke all the sessions failed: ", error)
    }
}

export const revokeSession = async () => {
    try {
        const response = await api.delete("auth/revoke-session")
        return response.data
    } catch (error) {
        console.error("Revoke session failed: ", error)
    }
}