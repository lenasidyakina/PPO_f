package ru.bmstu.iu7.src.managers;

import ru.bmstu.iu7.API.model.*;
import ru.bmstu.iu7.src.controllers.QuestionnaireController;

import java.util.List;

public class QuestionnaireManager {
    private final QuestionnaireController m_questionnaireController;
    public QuestionnaireManager(QuestionnaireController controller) {
        m_questionnaireController = controller;
    }
    public void set_active_questionnaire(AQuestionnaire quest ) {
        m_questionnaireController.set_active_questionnaire(quest);
    }

    public AQuestionnaire create(AUser user, AInformation information,
                                 AInformation search_information) throws Exception {
        return m_questionnaireController.create_questionnaire(user, information, search_information);
    }
    public void add_fav(AQuestionnaire questionnaire) throws Exception {
        m_questionnaireController.add_fav(questionnaire);
    }

    public void add_black(AQuestionnaire questionnaire) throws Exception {
        m_questionnaireController.add_black(questionnaire);
    }

    public void delete(AQuestionnaire questionnaire) throws Exception {
        m_questionnaireController.delete(questionnaire);
    }

    public void censor(AQuestionnaire questionnaire) throws Exception {
        m_questionnaireController.censor(questionnaire);
    }

    public List<AQuestion> get_all_questions() throws Exception {
        return m_questionnaireController.get_all_questions();
    }

    public void delete_user_questionnaires(Long userId) throws Exception {
        m_questionnaireController.delete_user_questionnaires(userId);
    }

    public List<AQuestionnaire> get_user_questionnaies(Long id) throws Exception {
        return m_questionnaireController.get_user_questionnaies(id);
    }

    public AQuestion append_question(boolean kind, String text, List<ATag> tags) throws Exception {
        return m_questionnaireController.append_question(kind, text, tags);
    }

    public ATag append_tag(String name) throws Exception {
        return m_questionnaireController.append_tag(name);
    }

    public ATag find_tag(String name) throws Exception {
        return m_questionnaireController.find_tag(name);
    }

    public void clear_req_cache() throws Exception {
        m_questionnaireController.clear_req_cache();
    }
    public List<String> findTopQuestionTags(){
        return m_questionnaireController.findTopQuestionTags();
    }
    public AQuestionnaire findQuestionnaire(Long id){
        return m_questionnaireController.findQuestionnaire(id);
    }
}
