package com.rookies4.springboottask.controller;

import com.rookies4.springboottask.entity.Book;
import com.rookies4.springboottask.exception.BusinessException;
import com.rookies4.springboottask.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookRestController {
    private final BookRepository bookRepository;
    //새 도서 등록
    @PostMapping
    public Book createBook(@RequestBody Book book) {
        return bookRepository.save(book);
    }
    //모든 도서 조회
    @GetMapping
    public List<Book> getBooks() {
        return bookRepository.findAll();
    }
    //ID로 특정 도서 조회
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); //headers에서만 status(404) 확인 가능
    }
    //ISBN으로 도서 조회
    @GetMapping("/isbn/{isbn}/")
    public Book getBookByIsbn(@PathVariable String isbn) {
        Book existBook = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
        return existBook;
    }
    //도서 정보 수정
    @PutMapping("/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody Book bookDetail) {
        Book existBook = getExistBook(id);
        existBook.setPrice(bookDetail.getPrice());
        Book updateBook = bookRepository.save(existBook);
        return updateBook;
    }
    //도서 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        Book existBook = getExistBook(id);
        bookRepository.delete(existBook);
        return ResponseEntity.ok("Book이 삭제되었습니다.");
    }

    private Book getExistBook(Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        Book existBook = optionalBook
                .orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
        return existBook;
    }

}