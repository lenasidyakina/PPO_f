package ru.bmstu.iu7;

import ru.bmstu.iu7.API.IUserRepository;
import ru.bmstu.iu7.API.model.AUser;
import ru.bmstu.iu7.impl.ModelFactory;
import ru.bmstu.iu7.impl.SpringUserRepository;
import ru.bmstu.iu7.impl.model.User;

//@Component
public class UserRepository implements IUserRepository {

    //@Autowired
    private SpringUserRepository m_springUserRepository;


    public UserRepository(SpringUserRepository springUserRepository) {
       this.m_springUserRepository = springUserRepository;
    }

    @Override
    public AUser createUser(String name, String password) throws Exception
    {
        //todo: age, gender
        User u = new User(name, password);
        u = m_springUserRepository.save(u);
        return ModelFactory.User2AUser(u);
    }

    @Override
    public AUser findUser(String name) throws Exception
    {
        return ModelFactory.User2AUser(m_springUserRepository.findByName(name));
    }

    @Override
    public void delete(Long id) throws Exception
    {
        m_springUserRepository.deleteById(id);
    }

    @Override
    public AUser update(AUser user) throws Exception
    {
        m_springUserRepository.save(ModelFactory.AUser2User(user));
        return null;
    }
}
