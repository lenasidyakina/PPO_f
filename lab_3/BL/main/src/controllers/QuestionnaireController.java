package ru.bmstu.iu7.src.controllers;

import ru.bmstu.iu7.API.IML_port;
import ru.bmstu.iu7.API.IQuestionnaireRepository;
import ru.bmstu.iu7.API.IReqCacheRepository;
import ru.bmstu.iu7.API.model.ExtendedAnswer;
import ru.bmstu.iu7.API.model.Information;
import ru.bmstu.iu7.API.model.Questionnaire;
import ru.bmstu.iu7.API.model.ReqCache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QuestionnaireController
{
    private IML_port m_ml;
    private Questionnaire m_active_questionnaire;
    private ReqCache m_req_cache;
    private IQuestionnaireRepository m_questionnaireRepository;
    private IReqCacheRepository m_req_cache_repository;


    public QuestionnaireController(IML_port imlPort, IQuestionnaireRepository questionnaireRepo, IReqCacheRepository reqCacheRepo)
    {
        this.m_questionnaireRepository = questionnaireRepo;
        this.m_ml = imlPort;
        this.m_req_cache_repository = reqCacheRepo;
    }

    public List<Questionnaire> get_user_questionnaies(int id) throws Exception {
        return m_questionnaireRepository.find_user_questionnaire(id);
    }

    public void set_active_questionnaire(Questionnaire questionnaire) {
        this.m_active_questionnaire = questionnaire;
    }

    public Questionnaire get_active_questionnaire() {
        return this.m_active_questionnaire;
    }


    public Questionnaire create_questionnaire(Information information, Information search_information) throws Exception {
        List<ExtendedAnswer> extended_answers = information.getExtended_answers();
        for (ExtendedAnswer extendedAnswer : extended_answers){
            extendedAnswer.setTags(m_ml.get_tags_names((extendedAnswer.getQuestion()).getQuestion(), extendedAnswer.getAnswer(), extendedAnswer.getTags()));

        }
        information.setExtended_answers(extended_answers);
        return m_questionnaireRepository.createQuestionnaire(information, search_information);
    }

    public void add_fav(Questionnaire questionnaire) throws Exception {
        m_active_questionnaire.add_fav(questionnaire);
        m_req_cache_repository.delete(questionnaire.get_id());
        m_req_cache.delete_from_cache(questionnaire);
        m_questionnaireRepository.update(m_active_questionnaire);
    }

    public void add_black(Questionnaire questionnaire) throws Exception {
        m_active_questionnaire.add_black(questionnaire);
        m_req_cache_repository.delete(questionnaire.get_id());
        m_req_cache.delete_from_cache(questionnaire);
        m_questionnaireRepository.update(m_active_questionnaire);
    }

    public boolean is_in_black(Questionnaire questionnaire) {
        for (Questionnaire quest : questionnaire.get_black_list()){
            if (quest.get_id() == questionnaire.get_id()){
                return true;
            }
        }
        return false;
    }

    public boolean is_in_fav(Questionnaire questionnaire) {
        for (Questionnaire quest : questionnaire.get_fav_list()){
            if (quest.get_id() == questionnaire.get_id()){
                return true;
            }
        }
        return false;
    }

    public void delete(Questionnaire questionnaire) throws Exception {
        m_questionnaireRepository.delete(questionnaire);
    }

    public void censor(Questionnaire questionnaire) throws Exception {
        m_active_questionnaire.set_censored(true);
        m_questionnaireRepository.update(questionnaire);
    }

    public List<Questionnaire> get_questionnairies(int lower, int upper) throws Exception {
        return m_questionnaireRepository.find_interval(lower, upper);
    }

    public void add_in_cache_list(double harmonic_average_norm, int id_quest) throws Exception {
        m_req_cache_repository.insert(id_quest, harmonic_average_norm);
    }

    public void update_req_cache() throws Exception {
        List<Map.Entry<Integer, Double>> req_cache = m_req_cache_repository.findAll(m_active_questionnaire.get_id());
        List<Questionnaire> req_questionnaires = new ArrayList<>();
        req_cache.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));
        int limit = Math.min(500, req_cache.size());

        List<Integer> rec_quest_id = req_cache.stream()
                .limit(500)
                .map(Map.Entry::getKey)
                .toList();

        for (int id : rec_quest_id){
            req_questionnaires.add(m_questionnaireRepository.findQuestionnaire(id));
        }
        m_req_cache.setM_req_cache(req_questionnaires.subList(0,limit));
    }

    public List<Questionnaire> get_quest_in_cache() throws Exception {
        return m_req_cache.get_req_cache().subList(0, 1);
    }
}
