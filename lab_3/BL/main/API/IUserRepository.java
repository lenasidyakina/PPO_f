package ru.bmstu.iu7.API;

import ru.bmstu.iu7.API.model.User;

public interface IUserRepository {
    User createUser(String name, String password) throws Exception;
    User findUser(String name, String password) throws Exception;
    void delete(int id) throws Exception;
    User update(User user) throws Exception;
}
