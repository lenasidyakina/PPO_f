import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import ru.bmstu.iu7.Main;
import ru.bmstu.iu7.impl.*;
import ru.bmstu.iu7.impl.model.User;


import java.util.Optional;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureDataJdbc
@EnableJdbcRepositories(basePackages = "ru.bmstu.iu7")
@ContextConfiguration(classes = {
        SpringQuestionnaireRepository.class,
        SpringExtendedAnswerRepository.class,
        SpringInformationRepository.class,
        SpringQuestionRepository.class,
        SpringReqCacheRepository.class,
        SpringTagRepository.class,
        SpringUserRepository.class,
        SpringVariantAnswerRepository.class
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class,
})
@ComponentScan("ru.bmstu.iu7")
public class DatabaseTest {
    @Autowired
    private SpringUserRepository userRepository;

    @Test
    @DatabaseSetup(value = "classpath:db_test_data.xml")
    public void testFindById_success() throws Exception {

        Optional<User> actual = userRepository.findById(1L);
        Optional<User> expected = Optional.of(
                new User(1L, "user", 11, true, "test", "admin"));
        Assertions.assertEquals(expected, actual);

    }
}