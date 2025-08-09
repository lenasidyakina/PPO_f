package ru.bmstu.iu7.src.controllers;

import ru.bmstu.iu7.API.model.AQuestionnaire;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ReqCacheController {
    List<AQuestionnaire> m_list = new ArrayList<>();

    private static Logger log = Logger.getLogger(QuestionnaireController.class.getName());


    public ReqCacheController() {}

    public void delete_from_cache(AQuestionnaire q)
    {
        m_list.remove(q);
    }

    public List<AQuestionnaire> get_req_cache() {
        return m_list;
    }

    public void setM_req_cache(List<AQuestionnaire> aQuestionnaires) {
        m_list = aQuestionnaires;
    }

    public void clearAll() {
        m_list.clear();
    }

}
