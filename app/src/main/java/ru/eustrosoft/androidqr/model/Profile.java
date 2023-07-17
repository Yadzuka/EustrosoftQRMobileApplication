package ru.eustrosoft.androidqr.model;

import java.util.Date;
import java.util.UUID;

public class Profile {

    private UUID id;
    private Date date;
    private String name;
    private String password;

    public Profile() {
        this(UUID.randomUUID());
    }

    public Profile(UUID id) {
        this.id = id;
        date = new Date();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
