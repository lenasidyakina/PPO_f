import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import ru.bmstu.iu7.*;
import ru.bmstu.iu7.API.model.*;
import ru.bmstu.iu7.impl.*;
import ru.bmstu.iu7.src.controllers.QuestionnaireController;
import ru.bmstu.iu7.src.managers.QuestionnaireManager;
import ru.bmstu.iu7.src.managers.UserManager;
import ru.bmstu.iu7.src.managers.RecManager;
import ru.bmstu.iu7.DBAPI;


import java.util.ArrayList;
import java.util.List;


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
@Transactional
public class DatabaseTest {
    @Autowired
    private SpringUserRepository userRepository;
    @Autowired
    private SpringReqCacheRepository reqCacheRepository;
    @Autowired
    private SpringQuestionnaireRepository questionnaireRepository;
    @Autowired
    private SpringExtendedAnswerRepository extendedAnswerRepository;
    @Autowired
    private SpringVariantAnswerRepository variantAnswerRepository;
    @Autowired
    private SpringQuestionRepository questionRepository;
    @Autowired
    private SpringTagRepository tagRepository;
    @Autowired
    private SpringInformationRepository informationRepository;

    @Test
    public void register() throws Exception {

        UserRepository xuserRepository = new UserRepository(userRepository);
        AUser expected = new AUser("Lena", "Lena12345");
        UserManager user_manager = new UserManager(xuserRepository);
        user_manager.register("Lena", "Lena12345");
        AUser actual = xuserRepository.findUser("Lena");
        Assertions.assertEquals(expected.getName(), actual.getName());
    }

    @Test
    public void authorize() throws Exception {
        UserRepository xuserRepository = new UserRepository(userRepository);
        UserManager user_manager = new UserManager(xuserRepository);
        user_manager.register("Lenaa", "Lena12345");
        AUser actual = xuserRepository.findUser("Lenaa");

        AUser real_user = user_manager.authorize("Lenaa", "Lena12345");
        Assertions.assertEquals(real_user.getId(), actual.getId());
    }

    @Test
    public void get_friends() throws Exception {
        DBAPI api = new DBAPI();
        UserRepository xuserRepository = new UserRepository(userRepository);
        ReqCacheRepository xreqCacheRepository = new ReqCacheRepository(reqCacheRepository);

        api.m_extendedAnswerRepository = extendedAnswerRepository;
        api.m_questionnaireRepository = questionnaireRepository;
        api.m_variantAnswerRepository = variantAnswerRepository;
        api.m_userRepository = userRepository;
        api.m_informationRepository = informationRepository;
        api.m_questionRepository = questionRepository;
        api.m_tagRepository = tagRepository;
        api.m_reqCacheRepository = reqCacheRepository;

        QuestionnaireRepository xquestRepository = new QuestionnaireRepository(api);

        UserManager user_manager = new UserManager(xuserRepository);

        AUser actual = user_manager.register("Lenaaaaa", "Lena12345");

        QuestionnaireController questionnaire_controller = new QuestionnaireController(new ML_port("http://localhost:11434/api/generate"), xquestRepository, xreqCacheRepository);
        QuestionnaireManager questionnaireManager = new QuestionnaireManager(questionnaire_controller);

        RecManager req_manager = new RecManager(questionnaire_controller);

        List<AVariantAnswer> variantAnswers1 = new ArrayList<>();
        List<AExtendedAnswer> extendedAnswers1 = new ArrayList<>();
        for (AQuestion q : questionnaireManager.get_all_questions()) {
            if (!q.getIs_extended()) {
                String answer = "watching TV";
                ATag tag = questionnaireManager.find_tag(answer);
                int weight = 1;
                AVariantAnswer an = new AVariantAnswer(weight, tag, q);
                variantAnswers1.add(an);
            } else {
                List<ATag> tags = new ArrayList<>();
                int weight = 1;
                AExtendedAnswer an = new AExtendedAnswer(q, weight, "I like walking my dog and sleeping.", tags);
                extendedAnswers1.add(an);
            }
        }
        AInformation info1 = new AInformation(variantAnswers1, extendedAnswers1);

        List<AVariantAnswer> variantAnswers2 = new ArrayList<>();
        List<AExtendedAnswer> extendedAnswers2 = new ArrayList<>();
        for (AQuestion q : questionnaireManager.get_all_questions()) {
            if (!q.getIs_extended()) {
                String answer = "swimming";
                ATag tag = questionnaireManager.find_tag(answer);
                int weight = 1;
                AVariantAnswer an = new AVariantAnswer(weight, tag, q);
                variantAnswers2.add(an);
            } else {
                List<ATag> tags = new ArrayList<>();
                int weight = 1;
                AExtendedAnswer an = new AExtendedAnswer(q, weight, "I like walking my dog.", tags);
                extendedAnswers2.add(an);
            }
        }
        AInformation info2 = new AInformation(variantAnswers2, extendedAnswers2);
        AQuestionnaire quest = questionnaireManager.create(actual, info2, info1);

        AUser actual1 = user_manager.register("Lenaaa1", "Lena12345");

        List<AVariantAnswer> variantAnswers11 = new ArrayList<>();
        List<AExtendedAnswer> extendedAnswers11 = new ArrayList<>();
        for (AQuestion q : questionnaireManager.get_all_questions()) {
            if (!q.getIs_extended()) {
                String answer = "swimming";
                ATag tag = questionnaireManager.find_tag(answer);
                int weight = 1;
                AVariantAnswer an = new AVariantAnswer(weight, tag, q);
                variantAnswers11.add(an);
            } else {
                List<ATag> tags = new ArrayList<>();
                int weight = 1;
                AExtendedAnswer an = new AExtendedAnswer(q, weight, "I like walking my dog.", tags);
                extendedAnswers11.add(an);
            }
        }
        AInformation info11 = new AInformation(variantAnswers11, extendedAnswers11);

        List<AVariantAnswer> variantAnswers22 = new ArrayList<>();
        List<AExtendedAnswer> extendedAnswers22 = new ArrayList<>();
        for (AQuestion q : questionnaireManager.get_all_questions()) {
            if (!q.getIs_extended()) {
                String answer = "swimming";
                ATag tag = questionnaireManager.find_tag(answer);
                int weight = 1;
                AVariantAnswer an = new AVariantAnswer(weight, tag, q);
                variantAnswers22.add(an);
            } else {
                List<ATag> tags = new ArrayList<>();
                int weight = 1;
                AExtendedAnswer an = new AExtendedAnswer(q, weight, "I like walking my dog.", tags);
                extendedAnswers22.add(an);
            }
        }
        AInformation info22 = new AInformation(variantAnswers22, extendedAnswers22);
        questionnaireManager.create(actual1, info22, info11);
        questionnaire_controller.set_active_questionnaire(quest);

        RecManager.ScheduledTask task = req_manager.new ScheduledTask();
        task.run();
        List<AQuestionnaire> friends_list = questionnaire_controller.get_quest_in_cache();
        Assertions.assertEquals("Lenaaa1", friends_list.getFirst().getUser().getName());
    }

    @Test
    @DatabaseSetup(value = "classpath:db_test_data.xml")
    public void create() throws Exception {
        DBAPI api = new DBAPI();
        UserRepository xuserRepository = new UserRepository(userRepository);
        ReqCacheRepository xreqCacheRepository = new ReqCacheRepository(reqCacheRepository);

        api.m_extendedAnswerRepository = extendedAnswerRepository;
        api.m_questionnaireRepository = questionnaireRepository;
        api.m_variantAnswerRepository = variantAnswerRepository;
        api.m_userRepository = userRepository;
        api.m_informationRepository = informationRepository;
        api.m_questionRepository = questionRepository;
        api.m_tagRepository = tagRepository;
        api.m_reqCacheRepository = reqCacheRepository;

        QuestionnaireRepository xquestRepository = new QuestionnaireRepository(api);
        QuestionnaireController questionnaire_controller = new QuestionnaireController(new ML_port("http://localhost:11434/api/generate"), xquestRepository, xreqCacheRepository);
        QuestionnaireManager questionnaireManager = new QuestionnaireManager(questionnaire_controller);

        UserManager user_manager = new UserManager(xuserRepository);
        AUser actual1 = user_manager.register("Lenaaa12", "Lena12345");

        List<AVariantAnswer> variantAnswers11 = new ArrayList<>();
        List<AExtendedAnswer> extendedAnswers11 = new ArrayList<>();
        for (AQuestion q : questionnaireManager.get_all_questions()) {
            if (!q.getIs_extended()) {
                String answer = "swimming";
                ATag tag = questionnaireManager.find_tag(answer);
                int weight = 1;
                AVariantAnswer an = new AVariantAnswer(weight, tag, q);
                variantAnswers11.add(an);
            } else {
                List<ATag> tags = new ArrayList<>();
                int weight = 1;
                AExtendedAnswer an = new AExtendedAnswer(q, weight, "I like walking my dog.", tags);
                extendedAnswers11.add(an);
            }
        }
        AInformation info11 = new AInformation(variantAnswers11, extendedAnswers11);

        List<AVariantAnswer> variantAnswers22 = new ArrayList<>();
        List<AExtendedAnswer> extendedAnswers22 = new ArrayList<>();
        for (AQuestion q : questionnaireManager.get_all_questions()) {
            if (!q.getIs_extended()) {
                String answer = "swimming";
                ATag tag = questionnaireManager.find_tag(answer);
                int weight = 1;
                AVariantAnswer an = new AVariantAnswer(weight, tag, q);
                variantAnswers22.add(an);
            } else {
                List<ATag> tags = new ArrayList<>();
                int weight = 1;
                AExtendedAnswer an = new AExtendedAnswer(q, weight, "I like walking my dog.", tags);
                extendedAnswers22.add(an);
            }
        }
        AInformation info22 = new AInformation(variantAnswers22, extendedAnswers22);
        AQuestionnaire q = questionnaireManager.create(actual1, info22, info11);
        AQuestionnaire quest_bd = xquestRepository.findQuestionnaire(q.getId());
        Assertions.assertEquals(q.getUser().getName(), quest_bd.getUser().getName());
    }
}