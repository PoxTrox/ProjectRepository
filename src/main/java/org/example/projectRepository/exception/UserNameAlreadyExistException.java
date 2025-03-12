package org.example.projectRepository.exception;

public class UserNameAlreadyExistException extends RuntimeException {

    public UserNameAlreadyExistException() {
    }

    public UserNameAlreadyExistException(String message) {
        super(message);
    }
}
