import React, { useState } from 'react'
import { Button } from '@mui/material'
import Stack from '@mui/material'
import TextField from '@mui/material'

const testLogin = () => {
    const [user, setUser] = useState({
        username: '',
        password: ''
    })
    const [isAuthenticated, setIsAuthenticated] = useState(false)
    const handleChange = (e) => {
        const { name, value } = e.target
        setUser(prev => ({
            ...prev,
            [name]: value
        }))
    }

    const handleLogin = () => {
        setIsAuthenticated(true)
    }
    return (
        <Stack>
            <TextField
                type="text"
                name="username"
                label="Username"
                value={user.username}
                onChange={handleChange} />
            <TextField
                type="password"
                name="password"
                label="Password"
                value={user.password}
                onChange={handleChange} />
            <Button
                variant="outlined"
                color="primary"
                onClick={handleLogin}
            >
                Login
            </Button>
        </Stack>
    )
}

export default testLogin
