package ru.bmstu.iu7;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import ru.bmstu.iu7.API.IML_port;
import ru.bmstu.iu7.API.model.*;
import ru.bmstu.iu7.impl.*;
import ru.bmstu.iu7.src.MainManager;

import java.io.Console;
import java.io.PrintStream;
import java.util.*;


@SpringBootApplication
public class Main {

    @Value("${olama-host}")
    private static String olamaHost;

    @Value("${ml.api.url}")
    private static String mlApiUrl;

    @Value("${ml.api.content-type}")

    static Console con;

    static AUser test_add_user(MainManager  m, String name, String pwd) throws Exception {
        return m.getM_user_manager().register(name, pwd);
    }

    static ATag test_add_tag(MainManager  m, String tag) throws Exception {
        return m.getM_que_manager().append_tag("tag");
    }

    static AQuestion test_add_variant_question(MainManager  m, String text, List<ATag> tags) throws Exception {
        return m.getM_que_manager().append_question(VARIANT_QUESTION_KIND, text, tags);
    }

    static AQuestion test_add_extended_question(MainManager  m, String text) throws Exception {
        return m.getM_que_manager().append_question(EXTENDED_QUESTION_KIND, text, new ArrayList<ATag>(){});
    }

    static AQuestionnaire add_questionnaire(MainManager  m, AUser user) throws Exception {
        AInformation info1 = emulate_information(m);
        AInformation info2 = emulate_information(m);
        return m.getM_que_manager().create(user, info1, info2);
    }

    static AInformation emulate_information(MainManager  m) throws Exception
    {
        ATag walking_tag = m.getM_que_manager().append_tag("walking");
        ATag watching_tag = m.getM_que_manager().append_tag("watching TV");
        ATag swimming_tag = m.getM_que_manager().append_tag("swimming");
        ATag sleeping_tag = m.getM_que_manager().append_tag("sleeping");

        AQuestion q_1 = m.getM_que_manager().append_question(VARIANT_QUESTION_KIND,
                "Do you love swimming or watching TV?", new ArrayList<>(Arrays.asList(watching_tag, swimming_tag)));
        AQuestion q_2 =  m.getM_que_manager().append_question(EXTENDED_QUESTION_KIND,
                "How do you like to spend your time?", new ArrayList<>(Arrays.asList(walking_tag, sleeping_tag)));

        String array[] = new String[] {
                "I like walking", "I like sleeping", "I like walking and sleeping"
        };


        Random random = new Random();
        List<AVariantAnswer> variantAnswers = new ArrayList<>();
        List<AExtendedAnswer> extendedAnswers = new ArrayList<>();
        for (AQuestion q : m.getM_que_manager().get_all_questions()) {
            if (!q.getIs_extended()) {
                // !kind = variant
                int rnd = random.nextInt(2);
                AVariantAnswer an;
                if (rnd == 0) {
                    an = new AVariantAnswer(0L,2, watching_tag, q);
                }
                else {
                    an = new AVariantAnswer(0L,2, swimming_tag, q);
                }
                variantAnswers.add(an);
            } else {
                // kind = extended
                int rnd = random.nextInt(3);
                System.out.println(q.getQuestion());
                String answer = array[rnd];
                List<ATag> tags = new ArrayList<>();
                // todo: make tags somehow, weight
                AExtendedAnswer an = new AExtendedAnswer(0L, q, 1, answer, tags);
                extendedAnswers.add(an);
            }
        }
        return new AInformation(0L, variantAnswers, extendedAnswers);
    }

    static String askVariant(AQuestion q)
    {
        int cnt;
        while(true) {
            System.out.println(q.getQuestion());
            cnt = 1;
            for (ATag t : q.getTags()) {
                System.out.println(String.valueOf(cnt++) + " - " + t.getName());
            }
            try {
                cnt = Integer.parseInt(con.readLine());
            }
            catch (Exception e) {
                cnt = 0;
            }
            if ((cnt > 0) && (cnt <= q.getTags().size()))
                break;
        };
        return q.getTags().get(cnt - 1).getName();
    }


    static AInformation quest(MainManager  m, String msg) throws Exception
    {
        System.out.println(msg);
        List<AVariantAnswer> variantAnswers = new ArrayList<>();
        List<AExtendedAnswer> extendedAnswers = new ArrayList<>();
        for (AQuestion q : m.getM_que_manager().get_all_questions()) {
            if (!q.getIs_extended()) {
                // !kind = variant
                String answer = askVariant(q);
                ATag tag = m.getM_que_manager().find_tag(answer);
                System.out.println("Введите вес этого вопроса (от 1 до 10):");
                String x = con.readLine();
                int weight = Integer.parseInt(x);
                // todo: weight
                AVariantAnswer an = new AVariantAnswer(0L,weight, tag, q);
                variantAnswers.add(an);
            } else {
                // kind = extended
                System.out.println(q.getQuestion());
                String answer = con.readLine();
                List<ATag> tags = new ArrayList<>();
                System.out.println("Введите вес этого вопроса (от 1 до 10):");
                String x = con.readLine();
                int weight = Integer.parseInt(x);
                // todo: make tags somehow, weight
                AExtendedAnswer an = new AExtendedAnswer(0L, q, weight, answer, tags);
                extendedAnswers.add(an);
            }
        }
        return new AInformation(0L, variantAnswers, extendedAnswers);
    }

    static void test_user(MainManager  m, int n) throws Exception {
        AUser u = test_add_user(m, "user" + String.valueOf(n), "user" + String.valueOf(n));
        for (int i = 0; i < 2; i++) {
            add_questionnaire(m, u);
            System.out.println("questionnarie added: " + i);
        }
    }
    static void test(MainManager  m) throws Exception {
        for (int i = 0; i < 2; i++) {
            test_user(m, i);
        }
    }

    static final boolean VARIANT_QUESTION_KIND = false;
    static final boolean EXTENDED_QUESTION_KIND = true;

    static void basic(MainManager m) throws Exception
    {
        ATag walking_tag = m.getM_que_manager().append_tag("walking");
        ATag watching_tag = m.getM_que_manager().append_tag("watching TV");
        ATag swimming_tag = m.getM_que_manager().append_tag("swimming");
        ATag sleeping_tag = m.getM_que_manager().append_tag("sleeping");

        AQuestion q_1 = m.getM_que_manager().append_question(VARIANT_QUESTION_KIND,
                "Do you love swimming or watching TV?", new ArrayList<>(Arrays.asList(watching_tag, swimming_tag)));
        AQuestion q_2 =  m.getM_que_manager().append_question(EXTENDED_QUESTION_KIND,
                "How do you like to spend your time?", new ArrayList<>(Arrays.asList(walking_tag, sleeping_tag)));

        //System.out.println("Test console application");
        AUser user = null;
        while(true) {
            if (user == null) {
                System.out.println("1 - зарегистрироваться");
                System.out.println("2 - войти");
                String x = con.readLine();
                if (x.equals("1")) {
                    System.out.println("логин: ");
                    String name = con.readLine();
                    System.out.println("пароль: ");
                    String pwd = con.readLine();
                    AUser u = m.getM_user_manager().register(name, pwd);
                    if (u == null) {
                        System.out.println("Регистрация не удалась");
                    }
                    else {
                        System.out.println("Пользователь успешно зарегистрирован");
                    }
                }
                else if (x.equals("2")) {
                    System.out.println("логин: ");
                    String name = con.readLine();
                    System.out.println("пароль: ");
                    String pwd = con.readLine();
                    user = m.getM_user_manager().authorize(name, pwd);
                    if (user == null) {
                        System.out.println("Не удалось войти");
                    }
                    else {
                        m.getM_que_manager().clear_req_cache();
                    }
                }
            }
            else {
                System.out.println("1 - создать анкету");
                System.out.println("2 - выйти");
                System.out.println("3 - удалить учётную запись");
                //System.out.println("d - delete my questionnaires");
                System.out.println("4 - получить список потенциальных друзей");
                System.out.println("5 - получить список самых популярных тегов по вопросам");
                System.out.println("6 - вывести избранное");
                System.out.println("7 - вывести чёрный список");

                String x = con.readLine();
                if (x.equals("2")) {
                    user = null;
                }
                else if (x.equals("3")) {
                    m.getM_user_manager().delete(user.getId());
                    user = null;
                }
                else if (x.equals("1")) {
                    AInformation info1 = quest(m,  "Часть 1. Ответь на вопросы от своего лица.");
                    AInformation info2 = quest(m,  "Часть 2. Ответь на вопросы от лица потенциального друга.");
                    AQuestionnaire questionnaire = m.getM_que_manager().create(user, info1, info2);
                    System.out.println("Анкета успешно создана");
                }
                else if (x.equals("d")) {
                    m.getM_que_manager().delete_user_questionnaires(user.getId());
                }
                else if (x.equals("4")) {
                    AQuestionnaire activeQuest = null;
                    List<AQuestionnaire> list = m.getM_que_manager().get_user_questionnaies(user.getId());
                    if (list.size() > 0) {
                        if (list.size() == 1)
                            activeQuest = list.get(0);
                        else {
                            System.out.println("Выберете анкету:");
                            int ctx = 1;
                            for (AQuestionnaire q : list) {
                                    System.out.println(String.valueOf(ctx++));

                            }
                            try {
                                String idx = con.readLine();
                                int n = Integer.parseInt(idx);
                                activeQuest = list.get(n - 1);
                            }
                            catch (Exception e) {
                                activeQuest = null;
                            }
                        }
                        if (activeQuest != null) {
                            m.getM_que_manager().set_active_questionnaire(activeQuest);
                            System.out.println("Ваши потенциальные друзья:");

                            //long startTime = System.nanoTime(); // начало замера времени

                            List<AQuestionnaire> qs = m.getM_rec_manager().get_friends();

                            //long endTime = System.nanoTime(); // конец замера времени
                            //long duration = endTime - startTime; // длительность в наносекундах

                            //System.out.println("Time taken to get friends: " + duration + " ns (" + (duration / 1_000_000.0) + " ms)");
                            List<Long> help_list = new ArrayList<>();
                            for (AQuestionnaire q : qs) {
                                if ((!activeQuest.getUser().getId().equals(q.getUser().getId())) && (!help_list.contains(q.getId()))
                                && (!activeQuest.getBlackList().contains(q)) && (!activeQuest.getFavList().contains(q))) {
                                    System.out.println(q.getUser().getName() + " " + q.getId());
                                    help_list.add(q.getId());
                                }
                            }

                            System.out.println("1 - добавить в чёрный список");
                            System.out.println("2 - добавить в избранное");
                            System.out.println("3 - выход");
                            String y = con.readLine();
                            if (y.equals("3")) {
                                continue;
                            }
                            else if (y.equals("1")) {
                                System.out.println("Выберете номер анкеты:");
                                help_list = new ArrayList<>();
                                for (AQuestionnaire q : qs) {
                                    if ((!activeQuest.getUser().getId().equals(q.getUser().getId())) && (!help_list.contains(q.getId()))
                                            && (!activeQuest.getBlackList().contains(q)) && (!activeQuest.getFavList().contains(q))) {
                                        System.out.println(q.getUser().getName() + " " + q.getId());
                                        help_list.add(q.getId());
                                    }
                                }
                                String z = con.readLine();
                                Long number = 0L;
                                try {
                                    number = Long.parseLong(z);
                                }
                                catch (NumberFormatException e) {
                                }
                                AQuestionnaire quest = m.getM_que_manager().findQuestionnaire(number);
                                m.getM_que_manager().add_black(quest);
                            }
                            else if (y.equals("2")) {
                                System.out.println("Выберете номер анкеты:");
                                help_list = new ArrayList<>();
                                for (AQuestionnaire q : qs) {
                                    if ((!activeQuest.getUser().getId().equals(q.getUser().getId())) && (!help_list.contains(q.getId()))
                                            && (!activeQuest.getBlackList().contains(q)) && (!activeQuest.getFavList().contains(q))) {
                                        System.out.println(q.getUser().getName() + " " + q.getId());
                                        help_list.add(q.getId());
                                    }
                                }
                                String z = con.readLine();
                                Long number = 0L;
                                try {
                                    number = Long.parseLong(z);
                                }
                                catch (NumberFormatException e) {
                                }
                                AQuestionnaire quest = m.getM_que_manager().findQuestionnaire(number);
                                m.getM_que_manager().add_fav(quest);
                            }
                        }

                    }
                }

                else if (x.equals("5")) {
                    List<String> list = m.getM_que_manager().findTopQuestionTags();
                    Integer i = 0;
                    for (String q : list) {
                        System.out.println("Question " + i + "->" +q);
                        i++;
                    }
                }
                else if (x.equals("6")) {
                    AQuestionnaire activeQuest = null;
                    List<AQuestionnaire> list = m.getM_que_manager().get_user_questionnaies(user.getId());
                    if (list.size() > 0) {
                        if (list.size() == 1)
                            activeQuest = list.get(0);
                        else {
                            System.out.println("Выберете номер анкеты:");
                            int ctx = 1;
                            for (AQuestionnaire q : list) {
                                System.out.println(String.valueOf(ctx++));
                            }
                            try {
                                String idx = con.readLine();
                                int n = Integer.parseInt(idx);
                                activeQuest = list.get(n - 1);
                            } catch (Exception e) {
                                activeQuest = null;
                            }
                        }
                        if (activeQuest != null) {
                            List<AQuestionnaire> fav_list = activeQuest.getFavList();
                            for (AQuestionnaire q : fav_list) {
                                System.out.println(q.getUser().getName() + " " + q.getId());
                            }
                        }
                    }
                }
                else if (x.equals("7")) {
                    AQuestionnaire activeQuest = null;
                    List<AQuestionnaire> list = m.getM_que_manager().get_user_questionnaies(user.getId());
                    if (list.size() > 0) {
                        if (list.size() == 1)
                            activeQuest = list.get(0);
                        else {
                            System.out.println("Выберете номер анкеты:");
                            int ctx = 1;
                            for (AQuestionnaire q : list) {
                                System.out.println(String.valueOf(ctx++));
                            }
                            try {
                                String idx = con.readLine();
                                int n = Integer.parseInt(idx);
                                activeQuest = list.get(n - 1);
                            } catch (Exception e) {
                                activeQuest = null;
                            }
                        }
                        if (activeQuest != null) {
                            List<AQuestionnaire> fav_list = activeQuest.getBlackList();
                            for (AQuestionnaire q : fav_list) {
                                System.out.println(q.getUser().getName() + " " + q.getId());
                            }
                        }
                    }
                }

            }
        }
    }
//I like sleeping ang Walking
//I like sleeping
//I like Walking
    public static void main(String[] args) throws Exception {

        System.setOut(new PrintStream(System.out, true, "UTF-8"));
        System.setErr(new PrintStream(System.err, true, "UTF-8"));
        con = System.console();

        System.out.println("Консольный тест 2");

        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);

        //SpringUserrRepository springUserRepository = context.getBean(SpringUserRepository.class);
        //SpringQuestionnaireRepository springQuestionnaireRepository = context.getBean(SpringQuestionnaireRepository.class);

        DBAPI api = new DBAPI();
        api.m_extendedAnswerRepository = context.getBean(SpringExtendedAnswerRepository.class);
        api.m_questionnaireRepository = context.getBean(SpringQuestionnaireRepository.class);
        api.m_variantAnswerRepository = context.getBean(SpringVariantAnswerRepository.class);
        api.m_userRepository = context.getBean(SpringUserRepository.class);
        api.m_informationRepository = context.getBean(SpringInformationRepository.class);
        api.m_questionRepository = context.getBean(SpringQuestionRepository.class);
        api.m_variantAnswerRepository = context.getBean(SpringVariantAnswerRepository.class);
        api.m_tagRepository = context.getBean(SpringTagRepository.class);
        api.m_reqCacheRepository = context.getBean(SpringReqCacheRepository.class);

        UserRepository userRepository = new UserRepository(api.m_userRepository);
        ReqCacheRepository reqCacheRepository = new ReqCacheRepository(api.m_reqCacheRepository);

        QuestionnaireRepository dataRepository = new QuestionnaireRepository(api);
        //IML_port iml_port = new ML_port();
        //dataRepository.clearAll();

        MainManager m = new MainManager(new ML_port(olamaHost),userRepository, dataRepository,
                reqCacheRepository);


        //test(m);
        basic(m);

    }

}

