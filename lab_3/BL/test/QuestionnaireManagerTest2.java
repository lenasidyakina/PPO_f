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

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class QuestionnaireManagerTest2 {

    @Mock
    IML_port iml_port;
    @Mock
    IQuestionnaireRepository questionnaire_repository;
    @Mock
    IReqCacheRepository req_cache_repository;

    @Test
    void create() throws Exception {
        Information cur_info = new Information(0,
                new ArrayList<VariantAnswer>(Arrays.asList(
                        new VariantAnswer(2, new Tag(0, "walking"),
                                new Question(0, "Do you love walking or swimming?", new ArrayList<Tag>(
                                        Arrays.asList(new Tag(0, "walking"), new Tag(1, "swimming"))))))),
                new ArrayList<ExtendedAnswer>(Arrays.asList(
                        new ExtendedAnswer(new Question(1, "How do you like to spend your time? ",
                                new ArrayList<Tag>(
                                        Arrays.asList(new Tag(2, "walking"), new Tag(3, "sleeping")))) ,2, "I like walking my dog.",
                                new ArrayList<Tag>(Arrays.asList())))));

        Information cur_info_search = new Information(1,
                new ArrayList<VariantAnswer>(Arrays.asList(
                        new VariantAnswer(3, new Tag(0, "swimming"),
                                new Question(0, "Do you love walking or swimming?", new ArrayList<Tag>(
                                        Arrays.asList(new Tag(0, "walking"), new Tag(1, "swimming"))))))),
                new ArrayList<ExtendedAnswer>(Arrays.asList(
                        new ExtendedAnswer(new Question(1, "How do you like to spend your time? ",
                                new ArrayList<Tag>(
                                        Arrays.asList(new Tag(2, "walking"), new Tag(3, "sleeping")))) ,2, "I like walking my dog and sleeping",
                                new ArrayList<Tag>(Arrays.asList())))));
        //Questionnaire cur_questionnaire = new Questionnaire(0, cur_info, cur_info_search);

        QuestionnaireController questionnaire_controller = new QuestionnaireController(iml_port, questionnaire_repository, req_cache_repository);
        QuestionnaireManager questionnaireManager = new QuestionnaireManager(questionnaire_controller);
        questionnaireManager.create(cur_info, cur_info_search);
        Mockito.verify(questionnaire_repository).createQuestionnaire(cur_info, cur_info_search);
    }

    @Test
    void censor() throws Exception {
        Information cur_info = new Information(0,
                new ArrayList<VariantAnswer>(Arrays.asList(
                        new VariantAnswer(2, new Tag(0, "walking"),
                                new Question(0, "Do you love walking or swimming?", new ArrayList<Tag>(
                                        Arrays.asList(new Tag(0, "walking"), new Tag(1, "swimming"))))))),
                new ArrayList<ExtendedAnswer>(Arrays.asList(
                        new ExtendedAnswer(new Question(1, "How do you like to spend your time? ",
                                new ArrayList<Tag>(
                                        Arrays.asList(new Tag(2, "walking"), new Tag(3, "sleeping")))) ,2, "I like walking my dog.",
                                new ArrayList<Tag>(Arrays.asList(new Tag(2, "walking")))))));

        Information cur_info_search = new Information(1,
                new ArrayList<VariantAnswer>(Arrays.asList(
                        new VariantAnswer(3, new Tag(0, "swimming"),
                                new Question(0, "Do you love walking or swimming?", new ArrayList<Tag>(
                                        Arrays.asList(new Tag(0, "walking"), new Tag(1, "swimming"))))))),
                new ArrayList<ExtendedAnswer>(Arrays.asList(
                        new ExtendedAnswer(new Question(1, "How do you like to spend your time? ",
                                new ArrayList<Tag>(
                                        Arrays.asList(new Tag(2, "walking"), new Tag(3, "sleeping")))) ,2, "I like walking my dog and sleeping",
                                new ArrayList<Tag>(Arrays.asList(new Tag(3, "sleeping")))))));
        Questionnaire cur_questionnaire = new Questionnaire(0, cur_info, cur_info_search);

        QuestionnaireController questionnaire_controller = new QuestionnaireController(iml_port, questionnaire_repository, req_cache_repository);
        questionnaire_controller.set_active_questionnaire(cur_questionnaire);
        QuestionnaireManager questionnaireManager = new QuestionnaireManager(questionnaire_controller);
        questionnaireManager.censor(cur_questionnaire);
        assertEquals(cur_questionnaire.is_censored(), true);
    }
}