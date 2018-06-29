package com.app.pack.Exception;

/**
 *
 * Custom exception class to throw InvalidInputException if any fo the user input falls out of specified range.
 *
 */
public class InvalidInputException extends Exception {

    public InvalidInputException (String field) {
        super("Check " + field );
    }
}
