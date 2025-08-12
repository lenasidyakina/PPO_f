package ru.bmstu.iu7.API;


import ru.bmstu.iu7.API.model.AUser;

public interface IUserRepository {
    AUser createUser(String name, String password) throws Exception;
    AUser findUser(String name) throws Exception;
    void delete(Long id) throws Exception;
    AUser update(AUser user) throws Exception;
}
