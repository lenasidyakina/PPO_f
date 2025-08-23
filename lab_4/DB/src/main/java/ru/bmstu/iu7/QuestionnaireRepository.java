package ru.bmstu.iu7;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.bmstu.iu7.API.IQuestionnaireRepository;
import ru.bmstu.iu7.API.model.*;
import ru.bmstu.iu7.impl.*;
import ru.bmstu.iu7.impl.model.*;

import java.util.ArrayList;
import java.util.List;

public class QuestionnaireRepository implements IQuestionnaireRepository {

    DBAPI api;

    public QuestionnaireRepository(DBAPI api) {
        this.api = api;
    }

    @Override
    public List<AQuestionnaire> findUserQuestionnaires(Long id) {
        return ModelFactory.Questionnaires2AQuestionnaires(api.m_questionnaireRepository.findByUserId(id));
    }

    @Override
    public AQuestionnaire createQuestionnaire(AUser user,
                                              AInformation information,
                                              AInformation search_info) {

        AQuestionnaire questionnaire = new AQuestionnaire(0L, user, information, search_info,
                new ArrayList<AQuestionnaire>(), new ArrayList<AQuestionnaire>());
        Questionnaire q = ModelFactory.AQuestionnaire2QuestionnaireNew(questionnaire);
        q =  api.m_questionnaireRepository.save(q);
        return ModelFactory.Questionnaire2AQuestionnaire(q);
    }

    @Override
    public AQuestionnaire update(AQuestionnaire questionnaire) {
        return ModelFactory.Questionnaire2AQuestionnaire(
            api.m_questionnaireRepository.save(ModelFactory.AQuestionnaire2Questionnaire(questionnaire)));
    }

    @Override
    public void delete(AQuestionnaire questionnaire) {
        api.m_questionnaireRepository.delete(ModelFactory.AQuestionnaire2Questionnaire(questionnaire));
    }

    @Override
    public List<AQuestionnaire> get_page(int pageNumber, int pageSize, Long user_id) throws Exception {
        return ModelFactory.Questionnaires2AQuestionnaires(
                api.m_questionnaireRepository
                        .findAllByUserIdNot(user_id,
                                PageRequest.of(pageNumber, pageSize, Sort.by("id").descending()))
                        .toList()
        );
    }

    @Override
    public AQuestionnaire findQuestionnaire(Long id) {
        return ModelFactory.Questionnaire2AQuestionnaire(
                api.m_questionnaireRepository.findById(Long.valueOf(id)).orElse(null));
    }

    @Override
    public List<AQuestion> get_all_questions() {
        return ModelFactory.Questions2AQuestions(api.m_questionRepository.findAll());
    }

    @Override
    public void deleteUserQuestionnaires(Long userId) {
        api.m_questionnaireRepository.deleteByUserId(userId);
    }

    @Override
    public void reset() {

    }

    @Override
    public Long count_quest() {
        return api.m_questionRepository.count();
    }

    public List<AQuestion> findAllQuestions() {
        return ModelFactory.Questions2AQuestions(api.m_questionRepository.findAll());
    }

    @Override
    public AQuestion  appendQuestion(boolean kind, String text, List<ATag> tags) {
        Question question = api.m_questionRepository.findByQuestion(text);
        if (question == null) {
            question = api.m_questionRepository.save((Question)ModelFactory.makeQuestion(null, text,
                    ModelFactory.ATags2Tags(tags), kind));
        }
        return ModelFactory.Question2AQuestion(question);
    }

    public void clearQuestions() {
        api.m_questionRepository.deleteAll();
    }

    public AUser addUser(AUser user) {

        return ModelFactory.User2AUser(api.m_userRepository.save(ModelFactory.AUser2User(user)));
    }

    @Override
    public ATag appendTag(String name) {
        Tag t = api.m_tagRepository.findByName(name);
        if (t == null) {
            t = api.m_tagRepository.save(ModelFactory.makeTag(null, name));
        }
        return ModelFactory.Tag2ATag(t);
    }

    @Override
    public ATag findTag(String name) {

        return ModelFactory.Tag2ATag(api.m_tagRepository.findByName(name));
    }

    @Override
    public List<Long> findRecommendedQuestionnaireIds(Long id) {
        return api.m_questionnaireRepository.findRecommendedQuestionnaireIds(id);
    }

    @Override
    public List<String> findTopQuestionTags() {
        List<QuestionTagDto> req = api.m_questionnaireRepository.findTopQuestionTags();
        List<String> list = new ArrayList<>();
        for (QuestionTagDto qtp : req)
        {
            list.add(qtp.getTagName());
        }
        return list;
    }

    public void clearTags() {
        api.m_tagRepository.deleteAll();
    }

    public void clearQuestionnaires() {
        api.m_questionnaireRepository.deleteAll();
    }

    public void clearInformation() {
        api.m_informationRepository.deleteAll();
    }

    public void clearAnswers() {
        api.m_variantAnswerRepository.deleteAll();
        api.m_extendedAnswerRepository.deleteAll();
    }

    public void clearUsers()
    {
        api.m_userRepository.deleteAll();
    }

    public void clearAll()
    {
        clearQuestionnaires();
        clearInformation();
        clearAnswers();
        clearUsers();
        clearQuestions();
        clearTags();
    }

}
