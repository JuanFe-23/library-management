package com.devsenior.juanfe.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.devsenior.juanfe.Exceptions.NotFoundException;
import com.devsenior.juanfe.Exceptions.UserAlreadyExistsException;

public class UserServiceTest {

    private UserService service;

    @BeforeEach
    void setUp() {
        service = new UserService();
    }

    @Test
    void testAddUser() throws NotFoundException, UserAlreadyExistsException {

        // GIVEN

        var userId = "100747";
        var name = "Juan Linares";
        var email = "juanzlinares@devsenior.com";

        // WHEN

        service.addUser(userId, name, email);

        // THEN

        var user = service.getUserById(userId);
        assertNotNull(user);
        assertEquals(userId, user.getId());
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
    }

    @DisplayName("Añadir un usuario con fecha de registro")
    @Test
    void testAddUserWithRegistrationDate() throws NotFoundException, UserAlreadyExistsException {

        // GIVEN

        var userId = "100747";
        var name = "Juan Linares";
        var email = "juanzlinares@devsenior.com";
        var registerDate = LocalDate.now();

        // WHEN

        service.addUser(userId, name, email, registerDate);

        // THEN

        var user = service.getUserById(userId);
        assertNotNull(user);
        assertEquals(userId, user.getId());        
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
        assertEquals(registerDate, user.getRegisDate());
    }

    @DisplayName("Intentar añadir un usuario que ya existe")
    @Test
    void testAddUserWhenUserAlreadyExists() throws NotFoundException, UserAlreadyExistsException {

        // GIVEN

        var userId = "100747";
        var name = "Juan Linares";
        var email = "juanzlinares@devsenior.com";

        service.addUser(userId, name, email);

        var userId2 = "100747";
        var name2 = "Fernando Linares";
        var email2 = "ferlin@devsenior.com";

        // WHEN - THEN

        assertThrows(UserAlreadyExistsException.class, () -> service.addUser(userId2, name2, email2));
    }

    @DisplayName("Intentar añadir un usuario que ya existe y con fecha de registro")
    @Test
    void testAddUserWhenUserAlreadyExistsWithRegistrationDate() throws NotFoundException, UserAlreadyExistsException {

        // GIVEN

        var userId = "100747";
        var name = "Juan Linares";
        var email = "juanzlinares@devsenior.com";
        var registerDate = LocalDate.now();

        service.addUser(userId, name, email, registerDate);

        var userId2 = "100747";
        var name2 = "Fernando Linares";
        var email2 = "ferlin@devsenior.com";

        // WHEN - THEN

        assertThrows(UserAlreadyExistsException.class, () -> service.addUser(userId2, name2, email2, registerDate));
    }

    @Test
    void testUpdateUserEmail() throws NotFoundException, UserAlreadyExistsException {

        // GIVEN

        var userId = "100747";
        var name = "Juan Linares";
        var email = "juanzlinares@devsenior.com";

        service.addUser(userId, name, email);

        // WHEN

        var newEmail = "juanzlinares@devsenior2.com";
        service.updateUserEmail(userId, newEmail);

        // THEN

        var user = service.getUserById(userId);
        assertNotNull(user);
        assertEquals(userId, user.getId());
        assertEquals(name, user.getName());
        assertEquals(newEmail, user.getEmail());
    }

    @Test
    void testUpdateName() throws NotFoundException, UserAlreadyExistsException {

        // GIVEN

        var userId = "100747";
        var name = "Juan Linares";
        var email = "juanzlinares@devsenior.com";

        service.addUser(userId, name, email);

        // WHEN

        var newName = "Fernando Linares";
        service.updateUserName(userId, newName);

        // THEN

        var user = service.getUserById(userId);
        assertNotNull(user);
        assertEquals(userId, user.getId());
        assertEquals(newName, user.getName());

    }

    @DisplayName("Eliminar un usuario existente")
    @Test
    void testDeleteUser() throws NotFoundException, UserAlreadyExistsException {

        // GIVEN

        var userId = "100747";
        var name = "Juan Linares";
        var email = "juanzlinares@devsenior.com";

        service.addUser(userId, name, email);

        // WHEN

        service.deleteUserById(userId);

        // THEN

        try {
            service.getUserById(userId);
            fail();
        } catch (NotFoundException e) {
            assertTrue(true);
        }
    }

    @DisplayName("Eliminar un usuario inexistente")
    @Test
    void testDeleteUserWhenNotExistingUser() throws NotFoundException {

        // GIVEN

        var userId = "100747";

        // WHEN - THEN

        assertThrows(NotFoundException.class, () -> service.deleteUserById(userId));

    }

    @Test
    void testGetUserById() throws NotFoundException, UserAlreadyExistsException {

        // GIVEN

        var userId = "100747";
        var name = "Juan Linares";
        var email = "juanzlinares@devsenior.com";

        service.addUser(userId, name, email);

        // WHEN

        var user = service.getUserById(userId);

        // THEN

        assertNotNull(user);
        assertEquals(1, service.getAllUsers().size());
        assertEquals(userId, user.getId());
        assertEquals(name, user.getName()); 
        assertEquals(email, user.getEmail()); 
    }

    @DisplayName("Obtener un usuario por id pero inexistente")
    @Test
    void testGetUserByIdWhenNotExistingUser() throws NotFoundException {

        // GIVEN

        var userId = "100747";

        // WHEN - THEN

        assertThrows(NotFoundException.class, () -> service.getUserById(userId));
        
    }


    @Test
    void testGetAllUsers() throws NotFoundException, UserAlreadyExistsException {

        // GIVEN

        service.addUser("100747", "Juan Linares", "juanzlinares@devsenior.com");
        service.addUser("100748", "Fernando Linares", "ferlin@devsenior.com");
        service.addUser("100749", "Cesar Diaz", "cesardiaz@devsenior.com");

        // WHEN

        var users = service.getAllUsers();

        // THEN

        assertNotNull(users);
        assertEquals(3, users.size());
    }

    @Test
    void testGetNonExistingUserById() throws NotFoundException {

        // GIVEN

        var userId = "100747";

        // WHEN - THEN

        assertThrows(NotFoundException.class, () -> service.getUserById(userId));

    }
}
