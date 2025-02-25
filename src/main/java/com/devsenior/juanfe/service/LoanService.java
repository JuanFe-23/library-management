package com.devsenior.juanfe.service;

import java.util.ArrayList;
import java.util.List;

import com.devsenior.juanfe.Exceptions.BookNotAvailableException;
import com.devsenior.juanfe.Exceptions.NotFoundException;
import com.devsenior.juanfe.model.Book;
import com.devsenior.juanfe.model.Loan;
import com.devsenior.juanfe.model.LoanState;
import com.devsenior.juanfe.model.User;

public class LoanService {

    private List<Loan> loans;
    private BookService bookService;
    private UserService userService;

    public LoanService(BookService bookService, UserService userService) {
        this.bookService = bookService;
        this.userService = userService;
        this.loans = new ArrayList<>();
    }

    public void loanBook(String userId, String isbn) throws NotFoundException, BookNotAvailableException {
        User user = userService.getUserById(userId);
        Book book = bookService.getBookByIsbn(isbn);

        for (Loan loan : loans) {
            if (loan.getBook().getIsbn().equals(isbn) && loan.getState().equals(LoanState.STARTED)) {
                throw new BookNotAvailableException("El libro con isbn: " + isbn + " ya esta prestado");

            }
        }

        loans.add(new Loan(user, book));

    }

    public void returnBook(String userId, String isbn) throws NotFoundException {
        for (Loan loan : loans) {
            if (loan.getUser().getId().equals(userId) && loan.getBook().getIsbn().equals(isbn)
                    && loan.getState().equals(LoanState.STARTED)) {
                loan.setState(LoanState.FINISHED);
                return;
            }
        }

        throw new NotFoundException(
                "No hay ningun prestamo del libro con isbn: " + isbn + " del usuario con id: " + userId);

    }

    public List<Loan> getAllLoans() {
        return loans;
    }

    public Loan getLoanByUser(String userId) throws NotFoundException {
        for (Loan loan : loans) {
            if (loan.getUser().getId().equals(userId)) {
                return loan;
            }
        }
        throw new NotFoundException("No hay ningun prestamo del usuario con id: " + userId);
    }

}
