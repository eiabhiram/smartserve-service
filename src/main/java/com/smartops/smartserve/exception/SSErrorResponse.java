package com.smartops.smartserve.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SSErrorResponse {
    
    private String errorCode;
    private String message;
    private Object[] args;
    
    public SSErrorResponse(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
        this.args = null;
    }
}
