package org.example.projectRepository.web;

import org.example.projectRepository.exception.BookAlreadyExist;
import org.example.projectRepository.exception.UserNameAlreadyExistException;
import org.example.projectRepository.exception.UserWithGivenNameDoesNotExist;
import org.example.projectRepository.exception.UserWithIdDoesNotExist;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(UserNameAlreadyExistException.class)
    public String handlerUserNameAlreadyExist(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("alreadyExist", "Username already exist");

        return "redirect:/register";
    }

    @ExceptionHandler(BookAlreadyExist.class)
    public String handlerBookAlreadyExist(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("alreadyExist", "Book already exist");
        return "redirect:/books/add";
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({UserWithGivenNameDoesNotExist.class,
            AccessDeniedException.class,
            MethodArgumentTypeMismatchException.class,
            NoResourceFoundException.class,
            UserWithIdDoesNotExist.class,
            UserWithGivenNameDoesNotExist.class

    })
    public ModelAndView handleNotFound(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("404NotFound");
        return modelAndView;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception exception) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("500EInternal");
        modelAndView.addObject("exception", exception.getClass().getSimpleName());
        return modelAndView;
    }
}
