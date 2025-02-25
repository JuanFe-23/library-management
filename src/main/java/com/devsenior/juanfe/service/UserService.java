package com.devsenior.juanfe.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.devsenior.juanfe.Exceptions.NotFoundException;
import com.devsenior.juanfe.model.User;

public class UserService {

    private List<User> users = new ArrayList<>();

    public void addUser(String id, String name, String email) {
        users.add(new User(id, name, email));
    }

    public void addUser(String id, String name, String email, LocalDate registerDate) {
        users.add(new User(id, name, email, registerDate));
    }

    public List<User> getAllUsers() {
        return users;
    }

    public User getUserById(String id) throws NotFoundException {
        for (User user : users) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        
        throw new NotFoundException("No existe el usuario con el id: " + id);
    }

    public void updateUserEmail(String id, String email) throws NotFoundException{
       var user = getUserById(id);
        user.setEmail(email);
    }

    public void updateUserName(String id, String name) throws NotFoundException{
        var user = getUserById(id);
         user.setName(name);
     }

     public void deleteUserById(String id) throws NotFoundException{
        var user = getUserById(id);
        users.remove(user);
     }


}
