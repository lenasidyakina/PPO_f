package ru.bmstu.iu7.src.managers;

import ru.bmstu.iu7.API.IUserRepository;
import ru.bmstu.iu7.API.model.User;

public class UserManager
{
    public UserManager(IUserRepository userRepository)
    {
        m_user_repository = userRepository;
    }

    public User register(String username, String password) throws Exception
    {
        if (m_user_repository.findUser(username, password) == null)
            return m_user_repository.createUser(username, password);
        else
            throw new Exception("User already exists");
    }

    public User authorize(String username, String password) throws Exception {
        return m_user_repository.findUser(username, password);
    }
    private final IUserRepository m_user_repository;
}
