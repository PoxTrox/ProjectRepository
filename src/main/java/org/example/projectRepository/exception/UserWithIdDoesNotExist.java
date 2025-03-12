package org.example.projectRepository.exception;

public class UserWithIdDoesNotExist extends RuntimeException {

    public UserWithIdDoesNotExist() {
    }

    public UserWithIdDoesNotExist(String message) {
        super(message);
    }
}
