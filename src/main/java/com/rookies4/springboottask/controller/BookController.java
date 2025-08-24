package com.rookies4.springboottask.controller;

import com.rookies4.springboottask.controller.dto.BookDTO;
import static com.rookies4.springboottask.controller.dto.BookDTO.BookResponse;
import com.rookies4.springboottask.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable Long id) {
        BookResponse bookById = bookService.getBookById(id);
        return ResponseEntity.ok(bookById);
    }

    @GetMapping("/isbn/{isbn}/")
    public ResponseEntity<BookResponse> getBookByIsbn(@PathVariable String isbn) {
        BookResponse bookByIsbn = bookService.getBookByIsbn(isbn);
        return ResponseEntity.ok(bookByIsbn);
    }

    @GetMapping("/author/{author}/")
    public ResponseEntity<List<BookResponse>> getBookByAuthor(@PathVariable String author) {
        List<BookResponse> bookByAuthor = bookService.getBookByAuthor(author);
        return ResponseEntity.ok(bookByAuthor);
    }

    @PostMapping
    public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookDTO.BookCreateRequest request) {
        BookResponse createdBook = bookService.createBook(request);
        return ResponseEntity.ok(createdBook);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(@PathVariable Long id,
                                                   @Valid @RequestBody BookDTO.BookUpdateRequest request) {
        return ResponseEntity.ok(bookService.updateBook(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}

