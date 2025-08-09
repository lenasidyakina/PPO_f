package ru.bmstu.iu7.API.model;

public class AUser
{
    public Long id;
    public String name;
    public int age;
    public boolean gender;
    public String password;
    public String role;

    public AUser(){}
    public AUser(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public AUser(Long id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public AUser(Long id, String name, int age, boolean gender, String password, String role) {
        this.name = name;
        this.password = password;
        this.id = id;
        this.age = age;
        this.gender = gender;
        this.role = role;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return this.age;
    }
    public boolean isGender() {
        return this.gender;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
