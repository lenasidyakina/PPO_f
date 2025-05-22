package ru.bmstu.iu7.src.managers;

import ru.bmstu.iu7.API.model.Information;
import ru.bmstu.iu7.API.model.Questionnaire;
import ru.bmstu.iu7.src.controllers.QuestionnaireController;

public class QuestionnaireManager {
    private QuestionnaireController m_questionnaireController;
    public QuestionnaireManager(QuestionnaireController controller) {
        m_questionnaireController = controller;
    }
    public Questionnaire get_active_questionnaire() {
        return m_questionnaireController.get_active_questionnaire();
    }
    public Questionnaire create(Information information, Information search_information) throws Exception {
        return m_questionnaireController.create_questionnaire(information, search_information);
    }
    public void add_fav(Questionnaire questionnaire) throws Exception {
        m_questionnaireController.add_fav(questionnaire);
    }

    public void add_black(Questionnaire questionnaire) throws Exception {
        m_questionnaireController.add_black(questionnaire);
    }

    public void delete(Questionnaire questionnaire) throws Exception {
        m_questionnaireController.delete(questionnaire);
    }

    public void censor(Questionnaire questionnaire) throws Exception {
        m_questionnaireController.censor(questionnaire);
    }
}
