package com.yunjin.datacenter.exception;

public class DatacenterInstanceException extends RuntimeException {
    private static final long serialVersionUID = 3196735788466764225L;
    public String code;
    public final String message;
    public String requestId;

    public DatacenterInstanceException(String message) {
        super(message);
        this.message = message;
    }

    public DatacenterInstanceException(String code, String message) {
        super("[" + code + "] " + message);
        this.message = message;
        this.code = code;
    }

    public DatacenterInstanceException(String code, String message, String requestId) {
        super("[" + code + "] " + message);
        this.message = message;
        this.code = code;
        this.requestId = requestId;
    }

    public DatacenterInstanceException(Exception e) {
        super(e);
        message = e.getMessage();
    }
}
