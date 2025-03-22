package org.example.projectRepository.exception;

public class BookAlreadyExist extends RuntimeException {


    public BookAlreadyExist(String message) {
        super(message);
    }

    public BookAlreadyExist(String message, Throwable cause) {
        super(message, cause);
    }
}
