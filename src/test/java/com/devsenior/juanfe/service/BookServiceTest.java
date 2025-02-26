package com.devsenior.juanfe.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.devsenior.juanfe.Exceptions.NotFoundException;
import com.devsenior.juanfe.model.Book;

public class BookServiceTest {

    private BookService service;

    @BeforeEach
    void setUp() {
        service = new BookService();
    }

    @Test
    void testAddBook() throws NotFoundException {

        // GIVEN -> PREPARAR LOS DATOS DE LA PRUEBA

        var isbn = "1234171124";
        var title = "Aprendiendo de las pruebas unitarias";
        var author = "Cesar Diaz";

        // WHEN -> Ejecutar el método a probar

        service.addBook(isbn, title, author);

        // THEN -> Verificar el resultado de la prueba

        var book = service.getBookByIsbn(isbn);
        assertNotNull(book);
        assertEquals(title, book.getTitle());
        assertEquals(author, book.getAuthor());
    }

    @Test
    void testDeleteExistingBook() throws NotFoundException {

        // GIVEN

        var isbn = "1234171124";
        var title = "Aprendiendo de las pruebas unitarias";
        var author = "Cesar Diaz";

        service.addBook(isbn, title, author);

        // WHEN

        service.deleteBookByIsbn(isbn);

        // THEN

        try {
            service.getBookByIsbn(isbn);
            fail();
        } catch (NotFoundException e) {
            assertTrue(true);
        }

    }

    @Test
    void testDeleteNonExistingBook() {

        // GIVEN

        var isbn = "1234171124";

        // WHEN - THEN

        assertThrows(NotFoundException.class, () -> service.deleteBookByIsbn(isbn));

    }

    @Test
    void testGetAllBooks() {

        // GIVEN

        service.addBook("1234171124", "Aprendiendo de las pruebas unitarias", "Cesar Diaz");
        service.addBook("123551", "Libro dos", "Pepito Perez");

        // WHEN

        List<Book> books = service.getAllBooks();

        // THEN

        assertNotNull(books);
        assertEquals(2, books.size());


    }

    @Test
    void testGetBookByIsbn() throws NotFoundException {

        // GIVEN

        var isbn = "1234171124";
        var title = "Aprendiendo de las pruebas unitarias";
        var author = "Cesar Diaz";

        service.addBook(isbn, title, author);

        // WHEN

        Book book = service.getBookByIsbn(isbn);

        // THEN

        assertNotNull(book);
        assertEquals(title, book.getTitle());
        assertEquals(author, book.getAuthor());
        

    }

    @Test
    void testGetNonExistingBookByIsbn() {

        // GIVEN

        var isbn = "1234171124";

        // WHEN - THEN

        NotFoundException exception = assertThrows(NotFoundException.class, () -> service.getBookByIsbn(isbn));

        assertEquals("No fue encontrado el libro con el isbn: " + isbn, exception.getMessage());

    }


    
}
