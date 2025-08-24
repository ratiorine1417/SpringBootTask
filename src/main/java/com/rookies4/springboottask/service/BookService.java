package com.rookies4.springboottask.service;

import static com.rookies4.springboottask.controller.dto.BookDTO.*;

import com.rookies4.springboottask.entity.Book;
import com.rookies4.springboottask.exception.BusinessException;
import com.rookies4.springboottask.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;

    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(book -> BookResponse.from(book)).toList();
    }

    public BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
        return BookResponse.from(book);
    }

    public BookResponse getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
        return BookResponse.from(book);
    }

    public List<BookResponse> getBookByAuthor(String author) {
        return bookRepository.findByAuthor(author).stream()
                .map(book -> BookResponse.from(book)).toList();
    }

    @Transactional
    public BookResponse createBook(BookCreateRequest request) {
        bookRepository.findByIsbn(request.getIsbn()); //exception 발생 코드 짜기
        Book savedBook = bookRepository.save(request.toEntity());
        return BookResponse.from(savedBook);
    }

    @Transactional
    public BookResponse updateBook(Long id, BookUpdateRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
        if (request.getTitle() != null) {
            book.setTitle(request.getTitle());
        }
        if (request.getAuthor() != null) {
            book.setAuthor(request.getAuthor());
        }
        if (request.getPrice() != null) {
            book.setPrice(request.getPrice());
        }
        if (request.getPublishDate() != null) {
            book.setPublishDate(request.getPublishDate());
        }
        bookRepository.save(book);
        return BookResponse.from(book);
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
        bookRepository.delete(book);
    }

}
