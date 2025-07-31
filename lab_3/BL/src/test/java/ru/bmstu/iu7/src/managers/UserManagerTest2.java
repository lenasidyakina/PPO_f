package ru.bmstu.iu7.src.managers;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.bmstu.iu7.API.IUserRepository;
import ru.bmstu.iu7.API.model.AUser;

@ExtendWith(MockitoExtension.class)
class UserManagerTest2 {
    @Mock
    IUserRepository i_user_repository;

    @org.junit.jupiter.api.Test
    void register() throws Exception {
        UserManager user_manager = new UserManager(i_user_repository);
        user_manager.register("Lena", "Lena12345");
        Mockito.verify(i_user_repository).createUser("Lena", "Lena12345");
        Mockito.verify(i_user_repository).findUser("Lena", "Lena12345");
    }

    @org.junit.jupiter.api.Test
    public void authorize() throws Exception {
        UserManager user_manager = new UserManager(i_user_repository);
        user_manager.authorize("Lena", "Lena12345");
        Mockito.verify(i_user_repository).findUser("Lena", "Lena12345");
    }

    @org.junit.jupiter.api.Test
    public void register_2() throws Exception {
        AUser user = new AUser("Lena", "Lena12345");
        Mockito.lenient().doReturn(user).when(i_user_repository).findUser("Lena", "Lena12345");
        UserManager user_manager = new UserManager(i_user_repository);
        try {
            user_manager.register("Lena", "Lena12345");
        }
        catch (Exception e) {}
        Mockito.verify(i_user_repository).findUser("Lena", "Lena12345");
    }
}