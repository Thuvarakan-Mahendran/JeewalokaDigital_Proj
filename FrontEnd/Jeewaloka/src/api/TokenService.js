const ACCESS_TOKEN_KEY = 'accessToken'

export const setAccessToken = (accessToken) => {
    console.log("access token is set to be ", accessToken)
    localStorage.setItem(ACCESS_TOKEN_KEY, accessToken)
}

export const getAccessToken = () => {
    return localStorage.getItem(ACCESS_TOKEN_KEY)
}

export const removeAccessToken = () => {
    localStorage.removeItem(ACCESS_TOKEN_KEY)
}