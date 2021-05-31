package com.flexisaf.ses.model;

public class Response {
    public static int SUCCESS = 0;
    public static int FAILED = 1;

    private Object data;
    
    private String message;
    private int status;

    public Response() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
    
    
    
    
}
