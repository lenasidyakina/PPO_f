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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RecManagerTest2 {

    @Mock
    IML_port iml_port;
    @Mock
    IQuestionnaireRepository questionnaire_repository;
    @Mock
    IReqCacheRepository req_cache_repository;

    @Test
    void information_comparison() throws Exception {
        Information info1 = new Information(0,
                new ArrayList<VariantAnswer>(Arrays.asList(
                        new VariantAnswer(2, new Tag(0, "walking"),
                                new Question(0, "Do you love walking or swimming?", new ArrayList<Tag>(
                                        Arrays.asList(new Tag(0, "walking"), new Tag(1, "swimming"))))))),
                new ArrayList<ExtendedAnswer>(Arrays.asList(
                        new ExtendedAnswer(new Question(1, "How do you like to spend your time? ",
                                new ArrayList<Tag>(
                                        Arrays.asList(new Tag(2, "walking"), new Tag(3, "sleeping")))) ,2, "I like walking my dog.",
                                new ArrayList<Tag>(Arrays.asList(new Tag(2, "walking")))))));

        Information info2 = new Information(0,
                new ArrayList<VariantAnswer>(Arrays.asList(
                        new VariantAnswer(3, new Tag(0, "swimming"),
                                new Question(0, "Do you love walking or swimming?", new ArrayList<Tag>(
                                        Arrays.asList(new Tag(0, "walking"), new Tag(1, "swimming"))))))),
                new ArrayList<ExtendedAnswer>(Arrays.asList(
                        new ExtendedAnswer(new Question(1, "How do you like to spend your time? ",
                                new ArrayList<Tag>(
                                        Arrays.asList(new Tag(2, "walking"), new Tag(3, "sleeping")))) ,2, "I like walking my dog and sleeping",
                                new ArrayList<Tag>(Arrays.asList(new Tag(2, "walking"), new Tag(3, "sleeping")))))));

        RecManager rec_manager = new RecManager(new QuestionnaireController(iml_port, questionnaire_repository, req_cache_repository));
        int coeff = rec_manager.information_comparison(info1, info2);
        assertEquals(coeff, 2);
    }

    @Test
    void information_comparison_2() throws Exception {
        Information info1 = new Information(0,
                new ArrayList<VariantAnswer>(Arrays.asList(
                        new VariantAnswer(2, new Tag(0, "walking"),
                                new Question(0, "Do you love walking or swimming?", new ArrayList<Tag>(
                                        Arrays.asList(new Tag(0, "walking"), new Tag(1, "swimming"))))))),
                new ArrayList<ExtendedAnswer>(Arrays.asList(
                        new ExtendedAnswer(new Question(1, "How do you like to spend your time? ",
                                new ArrayList<Tag>(
                                        Arrays.asList(new Tag(2, "walking"), new Tag(3, "sleeping")))) ,2, "I like walking my dog.",
                                new ArrayList<Tag>(Arrays.asList(new Tag(2, "walking")))))));

        Information info2 = new Information(0,
                new ArrayList<VariantAnswer>(Arrays.asList(
                        new VariantAnswer(3, new Tag(0, "swimming"),
                                new Question(0, "Do you love walking or swimming?", new ArrayList<Tag>(
                                        Arrays.asList(new Tag(0, "walking"), new Tag(1, "swimming"))))))),
                new ArrayList<ExtendedAnswer>(Arrays.asList(
                        new ExtendedAnswer(new Question(1, "How do you like to spend your time? ",
                                new ArrayList<Tag>(
                                        Arrays.asList(new Tag(2, "walking"), new Tag(3, "sleeping")))) ,2, "I like walking my dog and sleeping",
                                new ArrayList<Tag>(Arrays.asList(new Tag(3, "sleeping")))))));

        RecManager rec_manager = new RecManager(new QuestionnaireController(iml_port, questionnaire_repository, req_cache_repository));
        int coeff = rec_manager.information_comparison(info1, info2);
        assertEquals(coeff, 0);
    }

    @Test
    void information_comparison_3() throws Exception {
        Information info1 = new Information(0,
                new ArrayList<VariantAnswer>(Arrays.asList(
                        new VariantAnswer(3, new Tag(0, "swimming"),
                                new Question(0, "Do you love walking or swimming?", new ArrayList<Tag>(
                                        Arrays.asList(new Tag(0, "walking"), new Tag(1, "swimming"))))))),
                new ArrayList<ExtendedAnswer>(Arrays.asList(
                        new ExtendedAnswer(new Question(1, "How do you like to spend your time? ",
                                new ArrayList<Tag>(
                                        Arrays.asList(new Tag(2, "walking"), new Tag(3, "sleeping")))), 2, "I like walking my dog and sleeping",
                                new ArrayList<Tag>(Arrays.asList(new Tag(3, "sleeping")))))));

        Information info2 = new Information(2,
                new ArrayList<VariantAnswer>(Arrays.asList(
                        new VariantAnswer(3, new Tag(0, "swimming"),
                                new Question(0, "Do you love walking or swimming?", new ArrayList<Tag>(
                                        Arrays.asList(new Tag(0, "walking"), new Tag(1, "swimming"))))))),
                new ArrayList<ExtendedAnswer>(Arrays.asList(
                        new ExtendedAnswer(new Question(1, "How do you like to spend your time? ",
                                new ArrayList<Tag>(
                                        Arrays.asList(new Tag(2, "walking"), new Tag(3, "sleeping")))), 2, "I like walking my dog and sleeping",
                                new ArrayList<Tag>(Arrays.asList(new Tag(3, "sleeping")))))));

        RecManager rec_manager = new RecManager(new QuestionnaireController(iml_port, questionnaire_repository, req_cache_repository));
        int coeff = rec_manager.information_comparison(info1, info2);
        assertEquals(coeff, 5);
    }

    @Test
    void get_friends() throws Exception {
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

        Information info = new Information(2,
                new ArrayList<VariantAnswer>(Arrays.asList(
                        new VariantAnswer(2, new Tag(0, "walking"),
                                new Question(0, "Do you love walking or swimming?", new ArrayList<Tag>(
                                        Arrays.asList(new Tag(0, "walking"), new Tag(1, "swimming"))))))),
                new ArrayList<ExtendedAnswer>(Arrays.asList(
                        new ExtendedAnswer(new Question(1, "How do you like to spend your time? ",
                                new ArrayList<Tag>(
                                        Arrays.asList(new Tag(2, "walking"), new Tag(3, "sleeping")))) ,2, "I like walking my dog.",
                                new ArrayList<Tag>(Arrays.asList(new Tag(2, "walking")))))));

        Information info_search = new Information(3,
                new ArrayList<VariantAnswer>(Arrays.asList(
                        new VariantAnswer(3, new Tag(0, "swimming"),
                                new Question(0, "Do you love walking or swimming?", new ArrayList<Tag>(
                                        Arrays.asList(new Tag(0, "walking"), new Tag(1, "swimming"))))))),
                new ArrayList<ExtendedAnswer>(Arrays.asList(
                        new ExtendedAnswer(new Question(1, "How do you like to spend your time? ",
                                new ArrayList<Tag>(
                                        Arrays.asList(new Tag(2, "walking"), new Tag(3, "sleeping")))) ,2, "I like walking my dog and sleeping",
                                new ArrayList<Tag>(Arrays.asList(new Tag(3, "sleeping")))))));
        Questionnaire questionnaire = new Questionnaire(1, info, info_search);

        RecManager rec_manager = new RecManager(new QuestionnaireController(iml_port, questionnaire_repository, req_cache_repository));
        Mockito.lenient().doReturn(List.of(questionnaire)).when(questionnaire_repository).find_interval(0, 1000);
        Mockito.lenient().doReturn(List.of()).when(questionnaire_repository).find_interval(1000, 2000);;
        rec_manager.get_friends();
        Mockito.verify(rec_manager).recommended_questionnaires(cur_questionnaire, List.of(questionnaire));
    }
}
