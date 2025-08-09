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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class QuestionnaireManagerTest {

    @Mock
    IML_port iml_port;
    @Mock
    IQuestionnaireRepository quest_repository;
    @Mock
    IReqCacheRepository req_cache_repository;

    @Test
    void create() throws Exception {


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

        QuestionnaireController questionnaire_controller = new QuestionnaireController(iml_port, quest_repository, req_cache_repository);
        QuestionnaireManager questionnaireManager = new QuestionnaireManager(questionnaire_controller);
        questionnaireManager.create(user,info1, info2);
        Mockito.verify(quest_repository).createQuestionnaire(user,info1, info2);

    }

    @Test
    void censor() throws Exception {
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

        AUser user = new AUser(0L,"Lena", "Lena12345");
        List<AQuestionnaire> black_list = new ArrayList<>();
        List<AQuestionnaire> fav_list = new ArrayList<>();
        QuestionnaireController qc = new QuestionnaireController(iml_port, quest_repository, req_cache_repository);
        AQuestionnaire quest  = new AQuestionnaire((long)0, user, info1, info2, black_list, fav_list);
        qc.set_active_questionnaire(quest);
        QuestionnaireManager questionnaireManager = new QuestionnaireManager(qc);

        questionnaireManager.censor(quest);
        assertEquals(quest.isCensored(), true);

    }
}