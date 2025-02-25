package com.devsenior.juanfe.service;

import java.util.ArrayList;
import java.util.List;

import com.devsenior.juanfe.Exceptions.NotFoundException;
import com.devsenior.juanfe.model.Book;

public class BookService {

    private List<Book> books;

    public BookService(){
        books = new ArrayList<>();
    }

    public void addBook(String isbn, String title, String author) {

        books.add(new Book(isbn, title, author));
    }

    public List<Book> getAllBooks() {
        return books;
    }

    public Book getBookByIsbn(String isbn) throws NotFoundException {
        for (Book book : books) {
            if (book.getIsbn().equals(isbn)) {
                return book;

            }

        }

        throw new NotFoundException("No fue encontrado el libro con el isbn: " + isbn);
    }

    public void deleteBookByIsbn(String isbn) throws NotFoundException {
        for (Book book : books) {
            if (book.getIsbn().equals(isbn)) {
                books.remove(book);
                return;
            }

        }

        throw new NotFoundException("No se pudo borrar porque nofue encontrado el libro con el isbn: " + isbn);

    }

}
