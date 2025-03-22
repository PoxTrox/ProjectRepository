package org.example.projectRepository.exception;

public class MediaAlreadyExist extends RuntimeException{

    public MediaAlreadyExist(String message) {
        super(message);
    }

    public MediaAlreadyExist(String message, Throwable cause) {
        super(message, cause);
    }
}
