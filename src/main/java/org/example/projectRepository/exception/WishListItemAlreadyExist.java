package org.example.projectRepository.exception;


public class WishListItemAlreadyExist extends RuntimeException{

    public WishListItemAlreadyExist(String message) {
        super(message);
    }

    public WishListItemAlreadyExist(String message, Throwable cause) {
        super(message, cause);
    }
}
