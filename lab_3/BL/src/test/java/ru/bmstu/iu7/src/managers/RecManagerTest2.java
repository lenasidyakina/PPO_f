package ru.bmstu.iu7.src.managers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.bmstu.iu7.API.IML_port;
import ru.bmstu.iu7.API.IQuestionnaireRepository;
import ru.bmstu.iu7.API.IReqCacheRepository;
import ru.bmstu.iu7.API.model.*;
import ru.bmstu.iu7.src.controllers.QuestionnaireController;

import java.util.*;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RecManagerTest2 {

    @Mock
    IML_port iml_port;
    @Mock
    IQuestionnaireRepository questionnaire_repository;

    @Mock
    IReqCacheRepository req_cache_repository;

    private static Logger log = Logger.getLogger(RecManagerTest2.class.getName());


    @Test
    void get_friends() throws Exception {
        AInformation cur_info = new AInformation((long)0,
                new ArrayList<AVariantAnswer>(Arrays.asList(
                        new AVariantAnswer(2, new ATag((long)0, "walking"),
                                new AQuestion((long)0, "Do you love walking or swimming?", new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)0, "walking"), new ATag((long)1, "swimming"))), false)))),
                new ArrayList<AExtendedAnswer>(Arrays.asList(
                        new AExtendedAnswer(new AQuestion((long)1, "How do you like to spend your time? ",
                                new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)2, "walking"), new ATag((long)3, "sleeping"))), true) ,2, "I like walking my dog and sleeping",
                                new ArrayList<ATag>(Arrays.asList(new ATag((long)2, "walking")))))));

        AInformation cur_info_search = new AInformation((long)1,
                new ArrayList<AVariantAnswer>(Arrays.asList(
                        new AVariantAnswer(2, new ATag((long)0, "walking"),
                                new AQuestion((long)0, "Do you love walking or swimming?", new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)0, "walking"), new ATag((long)1, "swimming"))), false)))),
                new ArrayList<AExtendedAnswer>(Arrays.asList(
                        new AExtendedAnswer(new AQuestion((long)1, "How do you like to spend your time? ",
                                new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)2, "walking"), new ATag((long)3, "sleeping"))), true) ,2, "I like walking my dog and sleeping",
                                new ArrayList<ATag>(Arrays.asList(new ATag((long)3, "sleeping")))))));

        AInformation info = new AInformation((long)2,
                new ArrayList<AVariantAnswer>(Arrays.asList(
                        new AVariantAnswer(2, new ATag((long)0, "walking"),
                                new AQuestion((long)0, "Do you love walking or swimming?", new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)0, "walking"), new ATag((long)1, "swimming"))), false)))),
                new ArrayList<AExtendedAnswer>(Arrays.asList(
                        new AExtendedAnswer(new AQuestion((long)1, "How do you like to spend your time? ",
                                new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)2, "walking"), new ATag((long)3, "sleeping"))), true) ,2, "I like walking my dog.",
                                new ArrayList<ATag>(Arrays.asList(new ATag((long)2, "walking")))))));

        AInformation info_search = new AInformation((long)3,
                new ArrayList<AVariantAnswer>(Arrays.asList(
                        new AVariantAnswer(2, new ATag((long)0, "walking"),
                                new AQuestion((long)0, "Do you love walking or swimming?", new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)0, "walking"), new ATag((long)1, "swimming"))), false)))),
                new ArrayList<AExtendedAnswer>(Arrays.asList(
                        new AExtendedAnswer(new AQuestion((long)1, "How do you like to spend your time? ",
                                new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)2, "walking"), new ATag((long)3, "sleeping"))), true) ,2, "I like walking my dog.",
                                new ArrayList<ATag>(Arrays.asList(new ATag((long)3, "sleeping")))))));

        AUser user = new AUser(0L,"Lena", "Lena12345");
        List<AQuestionnaire> black_list = new ArrayList<>();
        List<AQuestionnaire> fav_list = new ArrayList<>();
        QuestionnaireController real_qc = new QuestionnaireController(iml_port, questionnaire_repository, req_cache_repository);
        QuestionnaireController qc = Mockito.spy(real_qc);
        AQuestionnaire quest  = new AQuestionnaire((long)0, user, info, info_search, black_list, fav_list);
        qc.set_active_questionnaire(quest);

        AUser user_other = new AUser(1L,"Ira", "12345");
        List<AQuestionnaire> black_list_other = new ArrayList<>();
        List<AQuestionnaire> fav_list_other = new ArrayList<>();
        AQuestionnaire quest_other  = new AQuestionnaire((long)1, user_other, cur_info, cur_info_search, black_list_other, fav_list_other);

        RecManager rec_manager = new RecManager(qc);
        Mockito.lenient().doReturn(List.of(quest_other)).when(qc).get_questionnairies(0, 1000, 0L);
        Mockito.lenient().doReturn(List.of()).when(qc).get_questionnairies(1000, 2000, 0L);
        Mockito.lenient().doReturn(List.of(new AReqCache(0L,0.1, quest, quest_other))).when(req_cache_repository).findAll(quest);
        Mockito.when(req_cache_repository.insert(0.1, quest, quest_other))
                .thenReturn(new AReqCache(0L, 0.1, quest, quest_other));
        Mockito.lenient().doReturn(quest_other).when(questionnaire_repository).findQuestionnaire(1L);

        List<AQuestionnaire> req_list = rec_manager.get_friends();
        AQuestionnaire questionnaire_test  = new AQuestionnaire((long)1, user, cur_info, cur_info_search, black_list_other, fav_list_other);

        assertEquals(req_list.getFirst(), questionnaire_test);
    }

    @Test
    void get_friends_1() throws Exception {

        AInformation info = new AInformation((long)2,
                new ArrayList<AVariantAnswer>(Arrays.asList(
                        new AVariantAnswer(2, new ATag((long)0, "walking"),
                                new AQuestion((long)0, "Do you love walking or swimming?", new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)0, "walking"), new ATag((long)1, "swimming"))), false)))),
                new ArrayList<AExtendedAnswer>(Arrays.asList(
                        new AExtendedAnswer(new AQuestion((long)1, "How do you like to spend your time? ",
                                new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)2, "walking"), new ATag((long)3, "sleeping"))), true) ,2, "I like walking my dog.",
                                new ArrayList<ATag>(Arrays.asList(new ATag((long)2, "walking")))))));

        AInformation info_search = new AInformation((long)3,
                new ArrayList<AVariantAnswer>(Arrays.asList(
                        new AVariantAnswer(2, new ATag((long)0, "walking"),
                                new AQuestion((long)0, "Do you love walking or swimming?", new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)0, "walking"), new ATag((long)1, "swimming"))), false)))),
                new ArrayList<AExtendedAnswer>(Arrays.asList(
                        new AExtendedAnswer(new AQuestion((long)1, "How do you like to spend your time? ",
                                new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)2, "walking"), new ATag((long)3, "sleeping"))), true) ,2, "I like walking my dog.",
                                new ArrayList<ATag>(Arrays.asList(new ATag((long)3, "sleeping")))))));

        AUser user = new AUser(0L,"Lena", "Lena12345");
        List<AQuestionnaire> black_list = new ArrayList<>();
        List<AQuestionnaire> fav_list = new ArrayList<>();
        QuestionnaireController real_qc = new QuestionnaireController(iml_port, questionnaire_repository, req_cache_repository);
        QuestionnaireController qc = Mockito.spy(real_qc);
        AQuestionnaire quest  = new AQuestionnaire((long)0, user, info, info_search, black_list, fav_list);
        qc.set_active_questionnaire(quest);

        RecManager rec_manager = new RecManager(qc);
        Mockito.lenient().doReturn(List.of()).when(qc).get_questionnairies(0, 1000, 0L);
        Mockito.lenient().doReturn(List.of()).when(req_cache_repository).findAll(quest);

        List<AQuestionnaire> req_list = rec_manager.get_friends();
        assertEquals(req_list.size(), 0);
    }

    @Test
    void information_comparison() throws Exception {

        AInformation info1 = new AInformation((long)0,
                new ArrayList<AVariantAnswer>(Arrays.asList(
                        new AVariantAnswer(2, new ATag((long)0, "walking"),
                                new AQuestion((long)0, "Do you love walking or swimming?", new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)0, "walking"), new ATag((long)1, "swimming"))), false)))),
                new ArrayList<AExtendedAnswer>(Arrays.asList(
                        new AExtendedAnswer(new AQuestion((long)1, "How do you like to spend your time? ",
                                new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)2, "walking"), new ATag((long)3, "sleeping"))), true) ,2, "I like walking my dog.",
                                new ArrayList<ATag>(Arrays.asList(new ATag((long)2, "walking")))))));

        AInformation info2 = new AInformation((long)1,
                new ArrayList<AVariantAnswer>(Arrays.asList(
                        new AVariantAnswer(2, new ATag((long)0, "swimming"),
                                new AQuestion((long)0, "Do you love walking or swimming?", new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)0, "walking"), new ATag((long)1, "swimming"))), false)))),
                new ArrayList<AExtendedAnswer>(Arrays.asList(
                        new AExtendedAnswer(new AQuestion((long)1, "How do you like to spend your time? ",
                                new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)2, "walking"), new ATag((long)3, "sleeping"))), true) ,2, "I like walking my dog.",
                                new ArrayList<ATag>(Arrays.asList(new ATag((long)2, "walking")))))));
        AUser user = new AUser("Lena", "Lena12345");
        List<AQuestionnaire> black_list = new ArrayList<>();
        List<AQuestionnaire> fav_list = new ArrayList<>();
        QuestionnaireController qc = new QuestionnaireController(iml_port, questionnaire_repository, req_cache_repository);
        AQuestionnaire quest  = new AQuestionnaire((long)0, user, info1, info2, black_list, fav_list);
        qc.set_active_questionnaire(quest);
        //qc.set_req_cache(new AReqCache(quest.getId(), List.of()));
        RecManager rec_manager = new RecManager(qc);

        int coeff = rec_manager.information_comparison(info1, info2);
        assertEquals(coeff, 2);


    }

    @Test
    void information_comparison_2() throws Exception {
        AInformation info1 = new AInformation((long)0,
                new ArrayList<AVariantAnswer>(Arrays.asList(
                        new AVariantAnswer(2, new ATag((long)0, "walking"),
                                new AQuestion((long)0, "Do you love walking or swimming?", new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)0, "walking"), new ATag((long)1, "swimming"))), false)))),
                new ArrayList<AExtendedAnswer>(Arrays.asList(
                        new AExtendedAnswer(new AQuestion((long)1, "How do you like to spend your time? ",
                                new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)2, "walking"), new ATag((long)3, "sleeping"))), true) ,2, "I like walking my dog.",
                                new ArrayList<ATag>(Arrays.asList(new ATag((long)2, "walking")))))));

        AInformation info2 = new AInformation((long)1,
                new ArrayList<AVariantAnswer>(Arrays.asList(
                        new AVariantAnswer(3, new ATag((long)0, "swimming"),
                                new AQuestion((long)0, "Do you love walking or swimming?", new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)0, "walking"), new ATag((long)1, "swimming"))), false)))),
                new ArrayList<AExtendedAnswer>(Arrays.asList(
                        new AExtendedAnswer(new AQuestion((long)1, "How do you like to spend your time? ",
                                new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)2, "walking"), new ATag((long)3, "sleeping"))), true) ,2, "I like walking my dog.",
                                new ArrayList<ATag>(Arrays.asList(new ATag((long)3, "sleeping")))))));
        AUser user = new AUser("Lena", "Lena12345");
        List<AQuestionnaire> black_list = new ArrayList<>();
        List<AQuestionnaire> fav_list = new ArrayList<>();
        QuestionnaireController qc = new QuestionnaireController(iml_port, questionnaire_repository, req_cache_repository);
        AQuestionnaire quest  = new AQuestionnaire((long)0, user, info1, info2, black_list, fav_list);
        qc.set_active_questionnaire(quest);
        //qc.set_req_cache(new AReqCache(quest.getId(), List.of()));
        RecManager rec_manager = new RecManager(qc);

        int coeff = rec_manager.information_comparison(info1, info2);
        assertEquals(coeff, 0);
    }

    @Test
    void information_comparison_3() throws Exception {
        AInformation info1 = new AInformation((long)0,
                new ArrayList<AVariantAnswer>(Arrays.asList(
                        new AVariantAnswer(3, new ATag((long)1, "swimming"),
                                new AQuestion((long)0, "Do you love walking or swimming?", new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)0, "walking"), new ATag((long)1, "swimming"))), false)))),
                new ArrayList<AExtendedAnswer>(Arrays.asList(
                        new AExtendedAnswer(new AQuestion((long)1, "How do you like to spend your time? ",
                                new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)2, "walking"), new ATag((long)3, "sleeping"))), true) ,2, "I like walking my dog and sleeping",
                                new ArrayList<ATag>(Arrays.asList(new ATag((long)3, "sleeping")))))));

        AInformation info2 = new AInformation((long)1,
                new ArrayList<AVariantAnswer>(Arrays.asList(
                        new AVariantAnswer(3, new ATag((long)1, "swimming"),
                                new AQuestion((long)0, "Do you love walking or swimming?", new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)0, "walking"), new ATag((long)1, "swimming"))), false)))),
                new ArrayList<AExtendedAnswer>(Arrays.asList(
                        new AExtendedAnswer(new AQuestion((long)1, "How do you like to spend your time? ",
                                new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)2, "walking"), new ATag((long)3, "sleeping"))), true) ,2, "I like walking my dog and sleeping",
                                new ArrayList<ATag>(Arrays.asList(new ATag((long)3, "sleeping")))))));

        AUser user = new AUser("Lena", "Lena12345");
        List<AQuestionnaire> black_list = new ArrayList<>();
        List<AQuestionnaire> fav_list = new ArrayList<>();
        QuestionnaireController qc = new QuestionnaireController(iml_port, questionnaire_repository, req_cache_repository);
        AQuestionnaire quest  = new AQuestionnaire((long)0, user, info1, info2, black_list, fav_list);
        qc.set_active_questionnaire(quest);
        //qc.set_req_cache(new AReqCache(quest.getId(), List.of()));
        RecManager rec_manager = new RecManager(qc);

        int coeff = rec_manager.information_comparison(info1, info2);
        assertEquals(coeff, 5);
    }

    @Test
    void get_friends_2() throws Exception {
        AInformation cur_info = new AInformation((long)0,
                new ArrayList<AVariantAnswer>(Arrays.asList(
                        new AVariantAnswer(2, new ATag((long)0, "walking"),
                                new AQuestion((long)0, "Do you love walking or swimming?", new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)0, "walking"), new ATag((long)1, "swimming"))), false)))),
                new ArrayList<AExtendedAnswer>(Arrays.asList(
                        new AExtendedAnswer(new AQuestion((long)1, "How do you like to spend your time? ",
                                new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)2, "walking"), new ATag((long)3, "sleeping"))), true) ,2, "I like walking my dog and sleeping",
                                new ArrayList<ATag>(Arrays.asList(new ATag((long)2, "walking")))))));

        AInformation cur_info_search = new AInformation((long)1,
                new ArrayList<AVariantAnswer>(Arrays.asList(
                        new AVariantAnswer(2, new ATag((long)0, "walking"),
                                new AQuestion((long)0, "Do you love walking or swimming?", new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)0, "walking"), new ATag((long)1, "swimming"))), false)))),
                new ArrayList<AExtendedAnswer>(Arrays.asList(
                        new AExtendedAnswer(new AQuestion((long)1, "How do you like to spend your time? ",
                                new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)2, "walking"), new ATag((long)3, "sleeping"))), true) ,2, "I like walking my dog and sleeping",
                                new ArrayList<ATag>(Arrays.asList(new ATag((long)3, "sleeping")))))));

        AInformation info_2 = new AInformation((long)0,
                new ArrayList<AVariantAnswer>(Arrays.asList(
                        new AVariantAnswer(2, new ATag((long)0, "walking"),
                                new AQuestion((long)0, "Do you love walking or swimming?", new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)0, "walking"), new ATag((long)1, "swimming"))), false)))),
                new ArrayList<AExtendedAnswer>(Arrays.asList(
                        new AExtendedAnswer(new AQuestion((long)1, "How do you like to spend your time? ",
                                new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)2, "walking"), new ATag((long)3, "sleeping"))), true) ,2, "I like walking my dog and sleeping",
                                new ArrayList<ATag>(Arrays.asList(new ATag((long)2, "walking")))))));

        AInformation info_search_2 = new AInformation((long)1,
                new ArrayList<AVariantAnswer>(Arrays.asList(
                        new AVariantAnswer(2, new ATag((long)0, "walking"),
                                new AQuestion((long)0, "Do you love walking or swimming?", new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)0, "walking"), new ATag((long)1, "swimming"))), false)))),
                new ArrayList<AExtendedAnswer>(Arrays.asList(
                        new AExtendedAnswer(new AQuestion((long)1, "How do you like to spend your time? ",
                                new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)2, "walking"), new ATag((long)3, "sleeping"))), true) ,2, "I like walking my dog and sleeping",
                                new ArrayList<ATag>(Arrays.asList(new ATag((long)3, "sleeping")))))));

        AInformation info = new AInformation((long)2,
                new ArrayList<AVariantAnswer>(Arrays.asList(
                        new AVariantAnswer(2, new ATag((long)0, "walking"),
                                new AQuestion((long)0, "Do you love walking or swimming?", new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)0, "walking"), new ATag((long)1, "swimming"))), false)))),
                new ArrayList<AExtendedAnswer>(Arrays.asList(
                        new AExtendedAnswer(new AQuestion((long)1, "How do you like to spend your time? ",
                                new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)2, "walking"), new ATag((long)3, "sleeping"))), true) ,2, "I like walking my dog.",
                                new ArrayList<ATag>(Arrays.asList(new ATag((long)2, "walking")))))));

        AInformation info_search = new AInformation((long)3,
                new ArrayList<AVariantAnswer>(Arrays.asList(
                        new AVariantAnswer(2, new ATag((long)0, "walking"),
                                new AQuestion((long)0, "Do you love walking or swimming?", new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)0, "walking"), new ATag((long)1, "swimming"))), false)))),
                new ArrayList<AExtendedAnswer>(Arrays.asList(
                        new AExtendedAnswer(new AQuestion((long)1, "How do you like to spend your time? ",
                                new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)2, "walking"), new ATag((long)3, "sleeping"))), true) ,2, "I like walking my dog.",
                                new ArrayList<ATag>(Arrays.asList(new ATag((long)3, "sleeping")))))));

        AUser user = new AUser(0L,"Lena", "Lena12345");
        List<AQuestionnaire> black_list = new ArrayList<>();
        List<AQuestionnaire> fav_list = new ArrayList<>();
        QuestionnaireController real_qc = new QuestionnaireController(iml_port, questionnaire_repository, req_cache_repository);
        QuestionnaireController qc = Mockito.spy(real_qc);
        AQuestionnaire quest  = new AQuestionnaire((long)0, user, info, info_search, black_list, fav_list);
        qc.set_active_questionnaire(quest);

        AUser user_other = new AUser(1L,"Ira", "12345");
        List<AQuestionnaire> black_list_other = new ArrayList<>();
        List<AQuestionnaire> fav_list_other = new ArrayList<>();
        AQuestionnaire quest_other  = new AQuestionnaire((long)1, user_other, cur_info, cur_info_search, black_list_other, fav_list_other);

        List<AQuestionnaire> black_list_2 = new ArrayList<>();
        List<AQuestionnaire> fav_list_2 = new ArrayList<>();
        AQuestionnaire quest_2  = new AQuestionnaire((long)2, user_other, info_2, info_search_2, black_list_2, fav_list_2);


        RecManager rec_manager = new RecManager(qc);
        Mockito.lenient().doReturn(List.of(quest_other, quest_2)).when(qc).get_questionnairies(0, 1000, 0L);
        Mockito.lenient().doReturn(List.of()).when(qc).get_questionnairies(1000, 2000, 0L);
        Mockito.lenient().doReturn(List.of(new AReqCache(0L,0.1, quest, quest_other), new AReqCache(1L,0.1, quest, quest_2))).when(req_cache_repository).findAll(quest);
        Mockito.when(req_cache_repository.insert(0.1, quest, quest_other))
                .thenReturn(new AReqCache(0L, 0.1, quest, quest_other));
        Mockito.when(req_cache_repository.insert(0.1, quest, quest_2))
                .thenReturn(new AReqCache(1L, 0.1, quest, quest_2));
        Mockito.lenient().doReturn(quest_other).when(questionnaire_repository).findQuestionnaire(1L);
        Mockito.lenient().doReturn(quest_2).when(questionnaire_repository).findQuestionnaire(2L);

        List<AQuestionnaire> req_list = rec_manager.get_friends();
        AQuestionnaire questionnaire_test  = new AQuestionnaire((long)1, user, cur_info, cur_info_search, black_list_other, fav_list_other);

        AQuestionnaire questionnaire_test_2  = new AQuestionnaire((long)2, user_other, info_2, info_search_2, black_list_2, fav_list_2);

        assertEquals(req_list.size(), 2);
        assertEquals(req_list.getFirst(), questionnaire_test);
        assertEquals(req_list.getLast(), questionnaire_test_2);
    }

    @Test
    void get_friends_3() throws Exception {
        AInformation cur_info = new AInformation((long)0,
                new ArrayList<AVariantAnswer>(Arrays.asList(
                        new AVariantAnswer(2, new ATag((long)0, "walking"),
                                new AQuestion((long)0, "Do you love walking or swimming?", new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)0, "walking"), new ATag((long)1, "swimming"))), false)))),
                new ArrayList<AExtendedAnswer>(Arrays.asList(
                        new AExtendedAnswer(new AQuestion((long)1, "How do you like to spend your time? ",
                                new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)2, "walking"), new ATag((long)3, "sleeping"))), true) ,2, "I like walking my dog and sleeping",
                                new ArrayList<ATag>(Arrays.asList(new ATag((long)2, "walking")))))));

        AInformation cur_info_search = new AInformation((long)1,
                new ArrayList<AVariantAnswer>(Arrays.asList(
                        new AVariantAnswer(2, new ATag((long)0, "walking"),
                                new AQuestion((long)0, "Do you love walking or swimming?", new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)0, "walking"), new ATag((long)1, "swimming"))), false)))),
                new ArrayList<AExtendedAnswer>(Arrays.asList(
                        new AExtendedAnswer(new AQuestion((long)1, "How do you like to spend your time? ",
                                new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)2, "walking"), new ATag((long)3, "sleeping"))), true) ,2, "I like walking my dog and sleeping",
                                new ArrayList<ATag>(Arrays.asList(new ATag((long)3, "sleeping")))))));

        AInformation info_2 = new AInformation((long)0,
                new ArrayList<AVariantAnswer>(Arrays.asList(
                        new AVariantAnswer(2, new ATag((long)0, "walking"),
                                new AQuestion((long)0, "Do you love walking or swimming?", new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)0, "walking"), new ATag((long)1, "swimming"))), false)))),
                new ArrayList<AExtendedAnswer>(Arrays.asList(
                        new AExtendedAnswer(new AQuestion((long)1, "How do you like to spend your time? ",
                                new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)2, "walking"), new ATag((long)3, "sleeping"))), true) ,2, "I like walking my dog and sleeping",
                                new ArrayList<ATag>(Arrays.asList(new ATag((long)3, "sleeping")))))));

        AInformation info_search_2 = new AInformation((long)1,
                new ArrayList<AVariantAnswer>(Arrays.asList(
                        new AVariantAnswer(2, new ATag((long)0, "walking"),
                                new AQuestion((long)0, "Do you love walking or swimming?", new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)0, "walking"), new ATag((long)1, "swimming"))), false)))),
                new ArrayList<AExtendedAnswer>(Arrays.asList(
                        new AExtendedAnswer(new AQuestion((long)1, "How do you like to spend your time? ",
                                new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)2, "walking"), new ATag((long)3, "sleeping"))), true) ,2, "I like walking my dog and sleeping",
                                new ArrayList<ATag>(Arrays.asList(new ATag((long)3, "sleeping")))))));

        AInformation info = new AInformation((long)2,
                new ArrayList<AVariantAnswer>(Arrays.asList(
                        new AVariantAnswer(2, new ATag((long)0, "walking"),
                                new AQuestion((long)0, "Do you love walking or swimming?", new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)0, "walking"), new ATag((long)1, "swimming"))), false)))),
                new ArrayList<AExtendedAnswer>(Arrays.asList(
                        new AExtendedAnswer(new AQuestion((long)1, "How do you like to spend your time? ",
                                new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)2, "walking"), new ATag((long)3, "sleeping"))), true) ,2, "I like walking my dog.",
                                new ArrayList<ATag>(Arrays.asList(new ATag((long)2, "walking")))))));

        AInformation info_search = new AInformation((long)3,
                new ArrayList<AVariantAnswer>(Arrays.asList(
                        new AVariantAnswer(2, new ATag((long)0, "walking"),
                                new AQuestion((long)0, "Do you love walking or swimming?", new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)0, "walking"), new ATag((long)1, "swimming"))), false)))),
                new ArrayList<AExtendedAnswer>(Arrays.asList(
                        new AExtendedAnswer(new AQuestion((long)1, "How do you like to spend your time? ",
                                new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)2, "walking"), new ATag((long)3, "sleeping"))), true) ,2, "I like walking my dog.",
                                new ArrayList<ATag>(Arrays.asList(new ATag((long)3, "sleeping")))))));

        AUser user = new AUser(0L,"Lena", "Lena12345");
        List<AQuestionnaire> black_list = new ArrayList<>();
        List<AQuestionnaire> fav_list = new ArrayList<>();
        QuestionnaireController real_qc = new QuestionnaireController(iml_port, questionnaire_repository, req_cache_repository);
        QuestionnaireController qc = Mockito.spy(real_qc);
        AQuestionnaire quest  = new AQuestionnaire((long)0, user, info, info_search, black_list, fav_list);
        qc.set_active_questionnaire(quest);

        AUser user_other = new AUser(1L,"Ira", "12345");
        List<AQuestionnaire> black_list_other = new ArrayList<>();
        List<AQuestionnaire> fav_list_other = new ArrayList<>();
        AQuestionnaire quest_other  = new AQuestionnaire((long)1, user_other, cur_info, cur_info_search, black_list_other, fav_list_other);

        List<AQuestionnaire> black_list_2 = new ArrayList<>();
        List<AQuestionnaire> fav_list_2 = new ArrayList<>();
        AQuestionnaire quest_2  = new AQuestionnaire((long)2, user_other, info_2, info_search_2, black_list_2, fav_list_2);


        RecManager rec_manager = new RecManager(qc);
        Mockito.lenient().doReturn(List.of(quest_other, quest_2)).when(qc).get_questionnairies(0, 1000, 0L);
        Mockito.lenient().doReturn(List.of()).when(qc).get_questionnairies(1000, 2000, 0L);
        Mockito.lenient().doReturn(List.of(new AReqCache(0L,0.1, quest, quest_other), new AReqCache(1L,0.13333333333333333, quest, quest_2))).when(req_cache_repository).findAll(quest);
        Mockito.when(req_cache_repository.insert(0.1, quest, quest_other))
                .thenReturn(new AReqCache(0L, 0.1, quest, quest_other));
        Mockito.when(req_cache_repository.insert(0.13333333333333333, quest, quest_2))
                .thenReturn(new AReqCache(1L, 0.13333333333333333, quest, quest_2));
        Mockito.lenient().doReturn(quest_other).when(questionnaire_repository).findQuestionnaire(1L);
        Mockito.lenient().doReturn(quest_2).when(questionnaire_repository).findQuestionnaire(2L);

        List<AQuestionnaire> req_list = rec_manager.get_friends();
        AQuestionnaire questionnaire_test  = new AQuestionnaire((long)1, user, cur_info, cur_info_search, black_list_other, fav_list_other);

        AQuestionnaire questionnaire_test_2  = new AQuestionnaire((long)2, user_other, info_2, info_search_2, black_list_2, fav_list_2);

        assertEquals(req_list.size(), 2);
        assertEquals(req_list.getFirst(), questionnaire_test_2);
        assertEquals(req_list.getLast(), questionnaire_test);
    }

    @Test
    void get_friends_4() throws Exception {
        AInformation info_2  = new AInformation((long)0,
                new ArrayList<AVariantAnswer>(Arrays.asList(
                        new AVariantAnswer(2, new ATag((long)0, "walking"),
                                new AQuestion((long)0, "Do you love walking or swimming?", new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)0, "walking"), new ATag((long)1, "swimming"))), false)))),
                new ArrayList<AExtendedAnswer>(Arrays.asList(
                        new AExtendedAnswer(new AQuestion((long)1, "How do you like to spend your time? ",
                                new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)2, "walking"), new ATag((long)3, "sleeping"))), true) ,2, "I like walking my dog and sleeping",
                                new ArrayList<ATag>(Arrays.asList(new ATag((long)2, "walking")))))));

        AInformation info_search_2 = new AInformation((long)1,
                new ArrayList<AVariantAnswer>(Arrays.asList(
                        new AVariantAnswer(2, new ATag((long)0, "walking"),
                                new AQuestion((long)0, "Do you love walking or swimming?", new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)0, "walking"), new ATag((long)1, "swimming"))), false)))),
                new ArrayList<AExtendedAnswer>(Arrays.asList(
                        new AExtendedAnswer(new AQuestion((long)1, "How do you like to spend your time? ",
                                new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)2, "walking"), new ATag((long)3, "sleeping"))), true) ,2, "I like walking my dog and sleeping",
                                new ArrayList<ATag>(Arrays.asList(new ATag((long)3, "sleeping")))))));

        AInformation cur_info = new AInformation((long)0,
                new ArrayList<AVariantAnswer>(Arrays.asList(
                        new AVariantAnswer(2, new ATag((long)0, "walking"),
                                new AQuestion((long)0, "Do you love walking or swimming?", new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)0, "walking"), new ATag((long)1, "swimming"))), false)))),
                new ArrayList<AExtendedAnswer>(Arrays.asList(
                        new AExtendedAnswer(new AQuestion((long)1, "How do you like to spend your time? ",
                                new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)2, "walking"), new ATag((long)3, "sleeping"))), true) ,2, "I like walking my dog and sleeping",
                                new ArrayList<ATag>(Arrays.asList(new ATag((long)3, "sleeping")))))));

        AInformation cur_info_search = new AInformation((long)1,
                new ArrayList<AVariantAnswer>(Arrays.asList(
                        new AVariantAnswer(2, new ATag((long)0, "walking"),
                                new AQuestion((long)0, "Do you love walking or swimming?", new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)0, "walking"), new ATag((long)1, "swimming"))), false)))),
                new ArrayList<AExtendedAnswer>(Arrays.asList(
                        new AExtendedAnswer(new AQuestion((long)1, "How do you like to spend your time? ",
                                new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)2, "walking"), new ATag((long)3, "sleeping"))), true) ,2, "I like walking my dog and sleeping",
                                new ArrayList<ATag>(Arrays.asList(new ATag((long)3, "sleeping")))))));

        AInformation info = new AInformation((long)2,
                new ArrayList<AVariantAnswer>(Arrays.asList(
                        new AVariantAnswer(2, new ATag((long)0, "walking"),
                                new AQuestion((long)0, "Do you love walking or swimming?", new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)0, "walking"), new ATag((long)1, "swimming"))), false)))),
                new ArrayList<AExtendedAnswer>(Arrays.asList(
                        new AExtendedAnswer(new AQuestion((long)1, "How do you like to spend your time? ",
                                new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)2, "walking"), new ATag((long)3, "sleeping"))), true) ,2, "I like walking my dog.",
                                new ArrayList<ATag>(Arrays.asList(new ATag((long)2, "walking")))))));

        AInformation info_search = new AInformation((long)3,
                new ArrayList<AVariantAnswer>(Arrays.asList(
                        new AVariantAnswer(2, new ATag((long)0, "walking"),
                                new AQuestion((long)0, "Do you love walking or swimming?", new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)0, "walking"), new ATag((long)1, "swimming"))), false)))),
                new ArrayList<AExtendedAnswer>(Arrays.asList(
                        new AExtendedAnswer(new AQuestion((long)1, "How do you like to spend your time? ",
                                new ArrayList<ATag>(
                                        Arrays.asList(new ATag((long)2, "walking"), new ATag((long)3, "sleeping"))), true) ,2, "I like walking my dog.",
                                new ArrayList<ATag>(Arrays.asList(new ATag((long)3, "sleeping")))))));

        AUser user = new AUser(0L,"Lena", "Lena12345");
        List<AQuestionnaire> black_list = new ArrayList<>();
        List<AQuestionnaire> fav_list = new ArrayList<>();
        QuestionnaireController real_qc = new QuestionnaireController(iml_port, questionnaire_repository, req_cache_repository);
        QuestionnaireController qc = Mockito.spy(real_qc);
        AQuestionnaire quest  = new AQuestionnaire((long)0, user, info, info_search, black_list, fav_list);
        qc.set_active_questionnaire(quest);

        AUser user_other = new AUser(1L,"Ira", "12345");
        List<AQuestionnaire> black_list_other = new ArrayList<>();
        List<AQuestionnaire> fav_list_other = new ArrayList<>();
        AQuestionnaire quest_other  = new AQuestionnaire((long)1, user_other, cur_info, cur_info_search, black_list_other, fav_list_other);

        List<AQuestionnaire> black_list_2 = new ArrayList<>();
        List<AQuestionnaire> fav_list_2 = new ArrayList<>();
        AQuestionnaire quest_2  = new AQuestionnaire((long)2, user_other, info_2, info_search_2, black_list_2, fav_list_2);


        RecManager rec_manager = new RecManager(qc);
        Mockito.lenient().doReturn(List.of(quest_other, quest_2)).when(qc).get_questionnairies(0, 1000, 0L);
        Mockito.lenient().doReturn(List.of()).when(qc).get_questionnairies(1000, 2000, 0L);
        Mockito.lenient().doReturn(List.of(new AReqCache(0L,0.13333333333333333, quest, quest_other), new AReqCache(1L,0.1, quest, quest_2))).when(req_cache_repository).findAll(quest);
        Mockito.when(req_cache_repository.insert(0.13333333333333333, quest, quest_other))
                .thenReturn(new AReqCache(0L, 0.13333333333333333, quest, quest_other));
        Mockito.when(req_cache_repository.insert(0.1, quest, quest_2))
                .thenReturn(new AReqCache(1L, 0.1, quest, quest_2));
        Mockito.lenient().doReturn(quest_other).when(questionnaire_repository).findQuestionnaire(1L);
        Mockito.lenient().doReturn(quest_2).when(questionnaire_repository).findQuestionnaire(2L);

        List<AQuestionnaire> req_list = rec_manager.get_friends();
        AQuestionnaire questionnaire_test  = new AQuestionnaire((long)1, user, cur_info, cur_info_search, black_list_other, fav_list_other);

        AQuestionnaire questionnaire_test_2  = new AQuestionnaire((long)2, user_other, info_2, info_search_2, black_list_2, fav_list_2);

        assertEquals(req_list.size(), 2);
        assertEquals(req_list.getFirst(), questionnaire_test);
        assertEquals(req_list.getLast(), questionnaire_test_2);
    }
}


