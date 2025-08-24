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
//@RequestMapping("/api/books")
public class BookRestController {
    private final BookRepository bookRepository;
    //새 도서 등록
//    @PostMapping
//    public Book createBook(@RequestBody Book book) {
//        return bookRepository.save(book);
//    }
    // 도서 등록
    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        Book savedBook = bookRepository.save(book);
        return new ResponseEntity<>(savedBook, HttpStatus.CREATED); //CREATED 201
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
    // 저자명으로 도서 조회
    @GetMapping("/author/{author}")
    public List<Book> getBooksByAuthor(@PathVariable String author) {
        return bookRepository.findByAuthor(author);
    }
    //도서 정보 수정
//    @PutMapping("/{id}")
//    public Book updateBook(@PathVariable Long id, @RequestBody Book bookDetail) {
//        Book existBook = getExistBook(id);
//        existBook.setPrice(bookDetail.getPrice());
//        Book updateBook = bookRepository.save(existBook);
//        return updateBook;
//    }
    // 도서 정보 수정
    @PatchMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book bookDetail) {
        Book existBook = getExistBook(id);
        existBook.setPrice(bookDetail.getPrice());

        Book updatedBook = bookRepository.save(existBook);
        return ResponseEntity.ok(updatedBook);
    }
    //도서 삭제
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
//        Book existBook = getExistBook(id);
//        bookRepository.delete(existBook);
//        return ResponseEntity.ok("Book이 삭제되었습니다.");
//    }
    // 도서 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        //매칭돠는 Book 이 없으면
        if (!bookRepository.existsById(id)) {
            return ResponseEntity.notFound().build(); //404
        }
        bookRepository.deleteById(id);
        return ResponseEntity.noContent().build(); //204
    }

    private Book getExistBook(Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        Book existBook = optionalBook
                .orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
        return existBook;
    }

}