package ru.bmstu.iu7.API.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReqCache {
    int id_questionnaire;
    private List<Questionnaire> m_req_cache = new ArrayList<>();

    public ReqCache(int id_questionnaire, List<Questionnaire> req_cache) {
        this.id_questionnaire = id_questionnaire;
        this.m_req_cache = req_cache;
    }
    public List<Questionnaire>get_req_cache() {
        return m_req_cache;
    }
    public void setM_req_cache(List<Questionnaire> req_cache) {
        this.m_req_cache = req_cache;
    }
    public void delete_from_cache(Questionnaire questionnaire) {
        m_req_cache.remove(questionnaire);
    }

}
