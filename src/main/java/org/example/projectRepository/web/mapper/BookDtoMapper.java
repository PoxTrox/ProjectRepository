package org.example.projectRepository.web.mapper;


import lombok.experimental.UtilityClass;
import org.example.projectRepository.book.model.Book;
import org.example.projectRepository.web.dto.BookEditRequest;

@UtilityClass
public class BookDtoMapper {

    public static BookEditRequest bookToBookEditRequest(Book book) {


        return  BookEditRequest.builder()
                .title(book.getTitle())
                .price(book.getPrice())
                .firstName(book.getAuthor().getFirstName())
                .lastName(book.getAuthor().getLastName())
                .build();
    }
}
