package com.carlos.datacenter.exception;

import lombok.Setter;

@Setter
public class DatacenterSqlException extends RuntimeException {
    private static final long serialVersionUID = 5072304707119333642L;
    /**
     * -- SETTER --
     *  设置 message
     */
    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    public DatacenterSqlException(String message) {
        this.message = message;
    }

}
