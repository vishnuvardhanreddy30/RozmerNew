package com.rozmer.service.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private String errorcode;
    private HttpStatus httpStatus = HttpStatus.OK;
    private String message;


    public ServiceException(String errorcode) {

        super();
        this.errorcode = errorcode;
    }

    public ServiceException(String errorcode , HttpStatus httpStatus) {

        super();
        this.errorcode = errorcode;
        this.httpStatus = httpStatus;
    }

    public ServiceException(String errorcode , String message , Throwable cause) {

        super(message, cause);
        this.errorcode = errorcode;
    }

    public ServiceException(String errorcode , HttpStatus httpStatus , String message , Throwable cause) {

        super(message , cause);
        this.errorcode = errorcode;
        this.httpStatus = httpStatus;
    }
    
}
