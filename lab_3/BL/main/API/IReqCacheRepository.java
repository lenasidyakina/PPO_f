package ru.bmstu.iu7.API;

import ru.bmstu.iu7.API.model.Questionnaire;
import ru.bmstu.iu7.API.model.ReqCache;

import java.util.List;
import java.util.Map;

public interface IReqCacheRepository {
    ReqCache insert(int id_quest, double harmonic_average_norm) throws Exception;
    void delete(int id_quest) throws Exception;
    List<Map.Entry<Integer, Double>> findAll(int id_cur_quest) throws Exception;
}
