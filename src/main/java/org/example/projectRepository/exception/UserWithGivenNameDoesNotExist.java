package org.example.projectRepository.exception;

public class UserWithGivenNameDoesNotExist extends RuntimeException {

    public UserWithGivenNameDoesNotExist() {
    }

    public UserWithGivenNameDoesNotExist(String message) {
        super(message);
    }
}
