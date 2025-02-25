package com.devsenior.juanfe.model;

import java.time.LocalDate;

public class User {

    private String id;
    private String name;
    private String email;
    private LocalDate regisDate;

    public User(String id, String name, String email) {
        this(id, name, email, LocalDate.now());

    }

    public User(String id, String name, String email, LocalDate regisDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.regisDate = regisDate;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getRegisDate() {
        return regisDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    

}
