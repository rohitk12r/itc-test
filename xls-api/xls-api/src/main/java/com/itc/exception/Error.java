package com.itc.exception;

import lombok.Data;

/**
 * It is holding exception details.
 * 
 * @author RohitSharma
 *
 */
@Data
public class Error {

    /**
     * It is store status code for rest services.
     */
    private Integer statusCode;

    /**
     * It is store for error.
     */
    private String error;

    /**
     * It is store for error message.
     */
    private String message;

}
