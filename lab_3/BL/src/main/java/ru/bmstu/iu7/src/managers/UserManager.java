package ru.bmstu.iu7.src.managers;

import ru.bmstu.iu7.API.IUserRepository;
import ru.bmstu.iu7.API.model.AUser;

public class UserManager
{
    public UserManager(IUserRepository userRepository)
    {
        m_user_repository = userRepository;
    }

    public AUser register(String username, String password) throws Exception
    {
        if (m_user_repository.findUser(username, password) == null)
            return m_user_repository.createUser(username, password);
        else
            throw new Exception("User already exists");
    }

    public AUser authorize(String username, String password) throws Exception {
        return m_user_repository.findUser(username, password);
    }

    public void delete(Long Id) throws Exception {
        m_user_repository.delete(Id);
    }
    private final IUserRepository m_user_repository;
}
