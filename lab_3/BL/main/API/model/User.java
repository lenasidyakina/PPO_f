package ru.bmstu.iu7.API.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class User
{
    @Id
    public int id;
    public String name;
    public int age;
    public boolean gender;
    public String password;

    public User(){}
    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
