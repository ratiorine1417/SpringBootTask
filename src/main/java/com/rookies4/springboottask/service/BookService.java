package com.rookies4.springboottask.service;

import com.rookies4.springboottask.controller.dto.BookDTO;
import com.rookies4.springboottask.entity.Book;
import com.rookies4.springboottask.entity.BookDetail;
import com.rookies4.springboottask.exception.BusinessException;
import com.rookies4.springboottask.exception.ErrorCode;
import com.rookies4.springboottask.repository.BookDetailRepository;
import com.rookies4.springboottask.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BookDetailRepository bookDetailRepository;

    public List<BookDTO.Response> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(BookDTO.Response::fromEntity)
                .toList();
    }

    public BookDTO.Response getBookById(Long id) {
        Book book = bookRepository.findByIdWithBookDetail(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Book", "id", id));
        return BookDTO.Response.fromEntity(book);
    }

    public BookDTO.Response getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbnWithBookDetail(isbn)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Book", "isbn", isbn));
        return BookDTO.Response.fromEntity(book);
    }

    public List<BookDTO.Response> getBooksByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author).stream()
                .map(BookDTO.Response::fromEntity).toList();
    }

    public List<BookDTO.Response> getBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(BookDTO.Response::fromEntity).toList();
    }

    @Transactional
    public BookDTO.Response createBook(BookDTO.Request request) {
        if(bookRepository.existsByIsbn(request.getIsbn())) {
            throw new BusinessException(ErrorCode.ISBN_DUPLICATE, request.getIsbn());
        }

        Book bookEntity = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .price(request.getPrice())
                .publishDate(request.getPublishDate())
                .build();

        if(request.getDetailRequest() != null) {
            BookDetail bookDetailEntity = BookDetail.builder()
                    .description(request.getDetailRequest().getDescription())
                    .language(request.getDetailRequest().getLanguage())
                    .pageCount(request.getDetailRequest().getPageCount())
                    .publisher(request.getDetailRequest().getPublisher())
                    .coverImageUrl(request.getDetailRequest().getCoverImageUrl())
                    .edition(request.getDetailRequest().getEdition())
                    .book(bookEntity)
                    .build();
            bookEntity.setBookDetail(bookDetailEntity);
        }

        Book savedBook = bookRepository.save(bookEntity);
        return BookDTO.Response.fromEntity(savedBook);
    }

    @Transactional
    public BookDTO.Response updateBook(Long id, BookDTO.Request request) {
        Book book = bookRepository.findByIdWithBookDetail(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Book", "id", id));
        if (!book.getIsbn().equals(request.getIsbn()) &&
                bookRepository.existsByIsbn(request.getIsbn())) {
            throw new BusinessException(ErrorCode.ISBN_DUPLICATE, request.getIsbn());
        }

        if (request.getTitle() != null) {
            book.setTitle(request.getTitle());
        }
        if (request.getAuthor() != null) {
            book.setAuthor(request.getAuthor());
        }
        if (request.getIsbn() != null) {
            book.setIsbn(request.getIsbn());
        }
        if (request.getPrice() != null) {
            book.setPrice(request.getPrice());
        }
        if (request.getPublishDate() != null) {
            book.setPublishDate(request.getPublishDate());
        }

        if (request.getDetailRequest() != null) {
            BookDetail bookDetail = book.getBookDetail();

            if (bookDetail == null) {
                bookDetail = new BookDetail();
                bookDetail.setDescription(request.getDetailRequest().getDescription());
                bookDetail.setLanguage(request.getDetailRequest().getLanguage());
                bookDetail.setPageCount(request.getDetailRequest().getPageCount());
                bookDetail.setPublisher(request.getDetailRequest().getPublisher());
                bookDetail.setCoverImageUrl(request.getDetailRequest().getCoverImageUrl());
                bookDetail.setEdition(request.getDetailRequest().getEdition());
                bookDetail.setBook(book);
                book.setBookDetail(bookDetail);
            }

            if (request.getDetailRequest().getDescription() != null) {
                bookDetail.setDescription(request.getDetailRequest().getDescription());
            }
            if (request.getDetailRequest().getLanguage() != null) {
                bookDetail.setLanguage(request.getDetailRequest().getLanguage());
            }
            if (request.getDetailRequest().getPageCount() != null) {
                bookDetail.setPageCount(request.getDetailRequest().getPageCount());
            }
            if (request.getDetailRequest().getPublisher() != null) {
                bookDetail.setPublisher(request.getDetailRequest().getPublisher());
            }
            if (request.getDetailRequest().getCoverImageUrl() != null) {
                bookDetail.setCoverImageUrl(request.getDetailRequest().getCoverImageUrl());
            }
            if (request.getDetailRequest().getEdition() != null) {
                bookDetail.setEdition(request.getDetailRequest().getEdition());
            }
        }

        Book updatedBook = bookRepository.save(book);
        return  BookDTO.Response.fromEntity(updatedBook);
    }

    @Transactional
    public BookDTO.Response updatePatchBook(Long id, BookDTO.PatchRequest request) {
        Book book = bookRepository.findByIdWithBookDetail(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Book", "id", id));

        if (request.getIsbn() != null) {
            request.getIsbn().ifPresent(isbn -> {
                if (!isbn.equals(book.getIsbn()) && bookRepository.existsByIsbn(isbn)) {
                    throw new BusinessException(ErrorCode.ISBN_DUPLICATE, isbn);
                }
                book.setIsbn(isbn);
            });
        }
        if (request.getTitle() != null) {
            request.getTitle().ifPresent(book::setTitle);
        }
        if (request.getAuthor() != null) {
            request.getAuthor().ifPresent(book::setAuthor);
        }
        if (request.getPrice() != null) {
            request.getPrice().ifPresent(book::setPrice);
        }
        if (request.getPublishDate() != null) {
            request.getPublishDate().ifPresent(book::setPublishDate);
        }

        if (request.getDetailRequest() != null) {
            request.getDetailRequest().ifPresent(detailRequest -> {
                BookDetail bookDetail = book.getBookDetail();
                if (bookDetail == null) {
                    bookDetail = new BookDetail();
//                bookDetail.setDescription(detailRequest.getDescription());
//                bookDetail.setLanguage(detailRequest.getLanguage());
//                bookDetail.setPageCount(detailRequest.getPageCount());
//                bookDetail.setPublisher(detailRequest.getPublisher());
//                bookDetail.setCoverImageUrl(detailRequest.getCoverImageUrl());
//                bookDetail.setEdition(detailRequest.getEdition());
                    bookDetail.setBook(book);
                    book.setBookDetail(bookDetail);
                }

                if (detailRequest.getDescription() != null) {
                    bookDetail.setDescription(detailRequest.getDescription());
                }
                if (detailRequest.getLanguage() != null) {
                    bookDetail.setLanguage(detailRequest.getLanguage());
                }
                if (detailRequest.getPageCount() != null) {
                    bookDetail.setPageCount(detailRequest.getPageCount());
                }
                if (detailRequest.getPublisher() != null) {
                    bookDetail.setPublisher(detailRequest.getPublisher());
                }
                if (detailRequest.getCoverImageUrl() != null) {
                    bookDetail.setCoverImageUrl(detailRequest.getCoverImageUrl());
                }
                if (detailRequest.getEdition() != null) {
                    bookDetail.setEdition(detailRequest.getEdition());
                }
            });
        }

        Book updatedBook = bookRepository.save(book);
        return  BookDTO.Response.fromEntity(updatedBook);
    }

    @Transactional
    public BookDTO.Response updatePatchBookDetail(Long id, BookDTO.BookDetailPatchRequest request) {
        BookDetail bookDetail = bookDetailRepository.findByIdWithBook(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Book", "id", id));

        if (request.getDescription() != null) {
            bookDetail.setDescription(request.getDescription());
        }
        if (request.getLanguage() != null) {
            bookDetail.setLanguage(request.getLanguage());
        }
        if (request.getPageCount() != null) {
            bookDetail.setPageCount(request.getPageCount());
        }
        if (request.getPublisher() != null) {
            bookDetail.setPublisher(request.getPublisher());
        }
        if (request.getCoverImageUrl() != null) {
            bookDetail.setCoverImageUrl(request.getCoverImageUrl());
        }
        if (request.getEdition() != null) {
            bookDetail.setEdition(request.getEdition());
        }
        BookDetail updatedBook = bookDetailRepository.save(bookDetail);
        return BookDTO.Response.fromEntity(updatedBook.getBook());
    }


    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findByIdWithBookDetail(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Book", "id", id));
        bookRepository.delete(book);
    }

}
