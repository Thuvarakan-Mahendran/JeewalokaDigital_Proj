package com.jeewaloka.digital.jeewalokadigital.dto;

public class ApiResponse<T> {

    private int statusCode;
    private String status;
    private String message;
    private T data;

    // Constructor for success
    public ApiResponse(int statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.status = "success";
        this.message = message;
        this.data = data;
    }

    // Constructor for error
    public ApiResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.status = "error";
        this.message = message;
    }

    // Getters and setters
    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
