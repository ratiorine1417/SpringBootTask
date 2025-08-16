package com.rookies4.springboottask.repository;

import com.rookies4.springboottask.entity.Book;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class BookRepositoryTest {
    @Autowired
    BookRepository bookRepository;

    @Test
    @Transactional
    @Rollback(value = false)
    @Disabled
    void testCreateBook() {
        //첫번째 도서 등록
        Book book1 = new Book();
        book1.setTitle("스프링 부트 입문");
        book1.setAuthor("홍길동");
        book1.setIsbn("9788956746425");
        book1.setPrice(30000);
        book1.setPublishDate(LocalDate.of(2025, 5, 7));

        //두번째 도서 등록
        Book book2 = new Book();
        book2.setTitle("JPA 프로그래밍");
        book2.setAuthor("박둘리");
        book2.setIsbn("9788956746432");
        book2.setPrice(35000);
        book2.setPublishDate(LocalDate.of(2025, 4, 30));

        List<Book> savedBooks = bookRepository.saveAll(Arrays.asList(book1, book2));

        // 저장된 책의 개수가 2개인지 검증
        assertThat(savedBooks).hasSize(2);

        //등록된 Book 엔티티객체가 Null이 아닌지를 검증하기
        for (Book savedBook: savedBooks) {
            assertThat(savedBook).isNotNull();
        }
        //등록된 Book isbn값이 동일한지 검증하기
        assertThat(savedBooks.get(0).getIsbn()).isEqualTo("9788956746425");
        assertThat(savedBooks.get(1).getIsbn()).isEqualTo("9788956746432");
    }

    @Test
    void testFindByIsbn() {
        //존재하는 경우
        Optional<Book> bookByIsbn = bookRepository.findByIsbn("9788956746425");
        Book existBook = bookByIsbn.orElseGet(() -> new Book());
        assertThat(existBook.getIsbn()).isEqualTo("9788956746425");

        //존재하지 않는 경우
        Book notFoundBook = bookRepository.findByIsbn("9788956741111").orElseGet(() -> new Book());
        assertThat(notFoundBook.getIsbn()).isNull();
    }

    @Test
    void testFindByAuthor() {
        //존재하는 경우
        List<Book> booksByAuthor = bookRepository.findByAuthor("박둘리");
        assertThat(booksByAuthor).isNotEmpty();
        for (Book bookByAuthor: booksByAuthor) {
            assertThat(bookByAuthor.getAuthor()).isEqualTo("박둘리");
        }

        //존재하지 않는 경우
        List<Book> notFoundBooks = bookRepository.findByAuthor("치미");
        assertThat(notFoundBooks).isEmpty();
    }

    @Test
    void testUpdateBook() {
        Book book = bookRepository.findByIsbn("9788956746425")
                .orElseThrow(() -> new RuntimeException("Book Not Found"));
        book.setPrice(25000);
        bookRepository.save(book);
        Book cdPrice = bookRepository.findByIsbn("9788956746425")
                .orElseThrow(() -> new RuntimeException("Book Not Found"));
        assertThat(cdPrice.getPrice()).isEqualTo(25000);
    }

    @Test
    void testDeleteBook() {
        Book foundBook = bookRepository.findByIsbn("9788956746425")
                .orElseThrow(() -> new RuntimeException("Book Not Found"));
        bookRepository.delete(foundBook);
        assertThat(bookRepository.findByIsbn("9788956746425")).isEmpty();
    }

}