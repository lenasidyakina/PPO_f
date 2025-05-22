package ru.bmstu.iu7.src;

import ru.bmstu.iu7.API.IML_port;
import ru.bmstu.iu7.API.IQuestionnaireRepository;
import ru.bmstu.iu7.API.IReqCacheRepository;
import ru.bmstu.iu7.API.IUserRepository;
import ru.bmstu.iu7.API.model.User;
import ru.bmstu.iu7.src.controllers.QuestionnaireController;
import ru.bmstu.iu7.src.managers.QuestionnaireManager;
import ru.bmstu.iu7.src.managers.RecManager;
import ru.bmstu.iu7.src.managers.UserManager;

public class MainManager {
    public MainManager(IML_port imlPort, IUserRepository user_repo, IQuestionnaireRepository questionnaire_repo, IReqCacheRepository reqCacheRepo) throws Exception
    {
        QuestionnaireController controller = new QuestionnaireController(imlPort, questionnaire_repo, reqCacheRepo);
        m_user_manager = new UserManager(user_repo);
        m_que_manager = new QuestionnaireManager(controller);
        m_rec_manager = new RecManager(controller);
    }

    public UserManager getM_user_manager() {
        return m_user_manager;
    }

    public QuestionnaireManager getM_que_manager() {
        return m_que_manager;
    }

    public RecManager getM_rec_manager() {
        return m_rec_manager;
    }

    private final UserManager m_user_manager;
    private QuestionnaireManager m_que_manager;
    private RecManager m_rec_manager;

}
