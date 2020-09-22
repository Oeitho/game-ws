package com.oeitho.exception;

public class GameNotFoundException extends Exception {
    
    private static final long serialVersionUID = 1685372123345922658L;

    public GameNotFoundException(final String errorMessage) {
        super(errorMessage);
    }
    
}