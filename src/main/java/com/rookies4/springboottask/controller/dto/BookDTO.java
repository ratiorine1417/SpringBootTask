package com.rookies4.springboottask.controller.dto;

import com.rookies4.springboottask.entity.Book;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

public class BookDTO {

    // 도서 생성 시 사용되는 DTO
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookCreateRequest {
        @NotBlank(message = "Title is required")
        @Size(max = 200, message = "Title cannot exceed 200 characters")
        private String title;

        @NotBlank(message = "Author is required")
        @Size(max = 200, message = "Author cannot exceed 200 characters")
        private String author;

        @NotBlank(message = "Isbn is required")
        @Size(max = 13, message = "Isbn cannot exceed 13 characters")
        @Pattern(regexp = "^(97[89])?\\d{9}(\\d|X)$", message = "유효한 Isbn 형식이 아닙니다.")
        private String isbn;

        @NotNull(message = "Price is required")
        @Min(0)
        private Integer price;

        @NotNull(message = "PublishDate is required")
        private LocalDate publishDate;

        public Book toEntity() {
            Book book = new Book();
            book.setTitle(this.title);
            book.setAuthor(this.author);
            book.setIsbn(this.isbn);
            book.setPrice(this.price);
            book.setPublishDate(this.publishDate);
            return book;
        }
    }
    
    // 도서 정보 업데이트 시 사용되는 DTO
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookUpdateRequest {
        //@NotNull(message = "Price is required")
        @Min(0)
        private Integer price;

        //@NotBlank(message = "Title is required")
        @Size(max = 200, message = "Title cannot exceed 200 characters")
        private String title;

        //@NotBlank(message = "Author is required")
        @Size(max = 200, message = "Author cannot exceed 200 characters")
        private String author;

        //@NotNull(message = "PublishDate is required")
        private LocalDate publishDate;
    }

    // 클라이언트에게 반환되는 도서 정보 DTO
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookResponse {
        private Long id;
        private String title;
        private String author;
        private String isbn;
        private Integer price;
        private LocalDate publishDate;

        public static BookResponse from(Book book) {
            return BookResponse.builder()
                    .id(book.getId())
                    .title(book.getTitle())
                    .author(book.getAuthor())
                    .isbn(book.getIsbn())
                    .price(book.getPrice())
                    .publishDate(book.getPublishDate())
                    .build();
        }
    }
}
