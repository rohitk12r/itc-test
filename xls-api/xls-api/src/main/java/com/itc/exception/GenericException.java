package com.itc.exception;

/**
 * Custom Exception Class used to call during to call Service
 * 
 * @author Rohit Sharma
 */
public abstract class GenericException extends Throwable {
    private String error;
    private String message;

    /**
     * This is serial
     */
    private static final long serialVersionUID = 8820350075913621700L;

    public GenericException() {
        super();
    }

    public GenericException(String message) {
        super(message);
        this.message = message;
    }

    public GenericException(String error, String message, Throwable cause) {
        super(message, cause);
        this.error = error;
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

}
