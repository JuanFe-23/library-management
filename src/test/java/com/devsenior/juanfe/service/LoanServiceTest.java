package com.devsenior.juanfe.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.devsenior.juanfe.Exceptions.BookNotAvailableException;
import com.devsenior.juanfe.Exceptions.NotFoundException;
import com.devsenior.juanfe.model.Book;
import com.devsenior.juanfe.model.Loan;
import com.devsenior.juanfe.model.LoanState;
import com.devsenior.juanfe.model.User;

public class LoanServiceTest {

    private LoanService service;
    private BookService bookService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        bookService = Mockito.mock(BookService.class);
        userService = Mockito.mock(UserService.class);
        service = new LoanService(bookService, userService);
    }

    @DisplayName("Agragar un prestamo con un usuario y un libro existente")
    @Test
    void testLoanBookWhenExistingUserAndBook() throws NotFoundException, BookNotAvailableException {

        // GIVEN

        var userId = "100747";
        var isbn = "17112423";
        var mockUser = new User(userId, "Juan Linares", "juanzlinares@devsenior.com");
        var mockBook = new Book(isbn, "Aprendiendo de las pruebas unitarias", "Cesar Diaz");

        Mockito.when(userService.getUserById(userId)).thenReturn(mockUser);
        Mockito.when(bookService.getBookByIsbn(isbn)).thenReturn(mockBook);

        // WHEN

        service.loanBook(userId, isbn);

        // THEN

        var loan = service.getAllLoans();
        assertEquals(1, loan.size());

        var loans = loan.get(0);
        assertNotNull(loans.getUser());
        assertNotNull(loans.getBook());
        assertEquals(LoanState.STARTED, loans.getState());

    }

    @DisplayName("Agregar un prestamo nuevo con un libro ya prestado")
    @Test
    void testLoanBookWhenBookAlreadyLoaned() throws NotFoundException, BookNotAvailableException {

        // GIVEN

        var userId = "100747";
        var isbn = "17112423";
        var mockUser = new User(userId, "Juan Linares", "juanzlinares@devsenior.com");
        var mockBook = new Book(isbn, "Aprendiendo de las pruebas unitarias", "Cesar Diaz");

        Mockito.when(userService.getUserById(userId)).thenReturn(mockUser);
        Mockito.when(bookService.getBookByIsbn(isbn)).thenReturn(mockBook);

        service.loanBook(userId, isbn);

        var UserId2 = "100748";
        var mockUser2 = new User(UserId2, "Sara Escobar", "saraescobar@devsenior.com");

        Mockito.when(userService.getUserById(UserId2)).thenReturn(mockUser2);

        // WHEN - THEN

        assertThrows(BookNotAvailableException.class, () -> service.loanBook(UserId2, isbn));

    }

    @DisplayName("Agragar un prestamo con un usuario y un libro inexistente")
    @Test
    void testLoanBookWhenNotExistingUserAndBook() throws NotFoundException {

        // GIVEN

        var userId = "100747";
        var isbn = "17112423";

        Mockito.when(userService.getUserById(userId))
                .thenThrow(new NotFoundException("No existe el usuario con el id: " + userId));
        Mockito.when(bookService.getBookByIsbn(isbn))
                .thenThrow(new NotFoundException("No fue encontrado el libro con el isbn: " + isbn));

        // WHEN - THEN

        assertThrows(NotFoundException.class, () -> service.loanBook(userId, isbn));

    }
    

    @DisplayName("Devolver un prestamo existente")
    @Test
    void testReturnBook() throws NotFoundException {

        // GIVEN

        var userId = "100747";
        var isbn = "17112423";

        var mockUser = new User(userId, "Juan Linares", "juanzlinares@devsenior.com");
        var mockBook = new Book(isbn, "Aprendiendo de las pruebas unitarias", "Cesar Diaz");

        Loan loan = new Loan(mockUser, mockBook);
        loan.setState(LoanState.STARTED);

        service.getAllLoans().add(loan);

        // WHEN

        service.returnBook(userId, isbn);

        // THEN

        assertEquals(userId, loan.getUser().getId());
        assertEquals(isbn, loan.getBook().getIsbn());
        assertEquals(LoanState.FINISHED, loan.getState());

    }

    @DisplayName("Devolver un prestamo inexistente")
    @Test
    void testReturnBookWhenNotExistingLoan() throws NotFoundException {

        // GIVEN

        var userId = "100747";
        var isbn = "17112423";

        // WHEN - THEN

        assertThrows(NotFoundException.class, () -> service.returnBook(userId, isbn));

    }

    @DisplayName("Obtener todos los prestamos")
    @Test
    void testGetAllLoans() throws NotFoundException, BookNotAvailableException {
        // GIVEN

        var userId = "100747";
        var isbn = "17112423";
        var userId2 = "100748";
        var isbn2 = "17112424";

        var mockUser = new User(userId, "Juan Linares", "juanzlinares@devsenior.com");
        var mockUser2 = new User(userId2, "Sara Escobar", "saraescobar@devsenior.com");
        var mockBook = new Book(isbn, "Aprendiendo de las pruebas unitarias", "Cesar Diaz");
        var mockBook2 = new Book(isbn2, "Incendiario", "Itiel Arroyo");

        Mockito.when(userService.getUserById(userId)).thenReturn(mockUser);
        Mockito.when(userService.getUserById(userId2)).thenReturn(mockUser2);
        Mockito.when(bookService.getBookByIsbn(isbn)).thenReturn(mockBook);
        Mockito.when(bookService.getBookByIsbn(isbn2)).thenReturn(mockBook2);

        service.loanBook(userId, isbn);
        service.loanBook(userId2, isbn2);

        // WHEN

        var loans = service.getAllLoans();

        // tHEN

        assertNotNull(loans);
        assertEquals(2, loans.size());

    }

    @DisplayName("Obtener prestamos inexistentes")
    @Test
    void testGetAllLoansWhenEmpty() {
        // GIVEN

        var loans = service.getAllLoans();

        // WHEN - THEN

        assertNotNull(loans);
        assertEquals(0, loans.size());
        assertTrue(loans.isEmpty());
    }

    @DisplayName("Obtener prestamos de un usuario existente")
    @Test
    void testGetLoansByUserId() throws NotFoundException, BookNotAvailableException {

        // GIVEN

        var userId = "100747";
        var isbn = "17112423";

        var mockUser = new User(userId, "Juan Linares", "juanzlinares@devsenior.com");
        var mockBook = new Book(isbn, "Aprendiendo de las pruebas unitarias", "Cesar Diaz");

        Mockito.when(userService.getUserById(userId)).thenReturn(mockUser);
        Mockito.when(bookService.getBookByIsbn(isbn)).thenReturn(mockBook);

        service.loanBook(userId, isbn);

        // WHEN

        var loan = service.getLoanByUser(userId);

        // THEN

        assertNotNull(loan);
        assertEquals(userId, loan.getUser().getId());
        assertEquals(isbn, loan.getBook().getIsbn());
        
    }

    @DisplayName("Obtener prestamos de un usuario inexistente")
    @Test
    void testGetLoansByUserIdWhenNotExistingUser() throws NotFoundException, BookNotAvailableException {

        // GIVEN

        var userId = "100747851";

        // WHEN - THEN

        assertThrows(NotFoundException.class, () -> service.getLoanByUser(userId));

    }




}
