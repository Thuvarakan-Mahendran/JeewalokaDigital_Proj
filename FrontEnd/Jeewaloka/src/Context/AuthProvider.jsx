import { useState } from "react"

const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(() => {
        const savedUser = localStorage.getItem('user')
        return savedUser ? JSON.parse(savedUser) : null
    })

    const isAuthenticated = !!user

    const login = async (credentials) => {
        try {
            const response = await fetch('/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(credentials),
            })

            if (!response.ok) {
                const error = await response.json()
                throw new Error(error.message || 'Login failed')
            }

            const data = await response.json()

            localStorage.setItem('token', data.token)
            localStorage.setItem('user', JSON.stringify(data.user))
            setUser(data.user)

            return { success: true, user: data.user }
        } catch (error) {
            return { success: false, error: error.message }
        }
    }

    const logout = () => {
        localStorage.removeItem('token')
        localStorage.removeItem('user')
        setUser(null);
    }

    const contextValue = {
        user,
        login,
        logout,
        isAuthenticated
    }

    return (
        <AuthContext.Provider value={contextValue}>
            {children}
        </AuthContext.Provider>
    )
}

export default AuthProvider