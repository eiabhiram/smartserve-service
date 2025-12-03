package com.smartops.smartserve.exception;

public class SSBusinessException extends RuntimeException {

    private Object[] args;

    public SSBusinessException(String message) {
        super(message);
    }

    public SSBusinessException(String message, Object[] args) {
        super(message);
        this.args = args;
    }

    public SSBusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public Object[] getArgs() {
        return args;
    }
}
