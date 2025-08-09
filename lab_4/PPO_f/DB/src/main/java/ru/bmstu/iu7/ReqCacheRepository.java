package ru.bmstu.iu7;

import org.springframework.boot.Banner;
import ru.bmstu.iu7.API.IReqCacheRepository;
import ru.bmstu.iu7.API.model.AQuestionnaire;
import ru.bmstu.iu7.API.model.AReqCache;
import ru.bmstu.iu7.impl.ModelFactory;
import ru.bmstu.iu7.impl.SpringReqCacheRepository;
import ru.bmstu.iu7.impl.SpringUserRepository;
import ru.bmstu.iu7.impl.model.Questionnaire;
import ru.bmstu.iu7.impl.model.ReqCache;

import java.util.List;
import java.util.Map;

public class ReqCacheRepository implements IReqCacheRepository {

    private SpringReqCacheRepository m_springReqCacheRepository;

    public ReqCacheRepository(SpringReqCacheRepository springReqCacheRepository) {
        this.m_springReqCacheRepository = springReqCacheRepository;
    }

    @Override
    public AReqCache insert(double Factor,
                            AQuestionnaire questionnaire1, AQuestionnaire questionnaire2) throws Exception {
        ReqCache rc = new ReqCache();
        rc.setFactor(Factor);
        rc.setQuestionnaire1(ModelFactory.AQuestionnaire2Questionnaire(questionnaire1));
        rc.setQuestionnaire2(ModelFactory.AQuestionnaire2Questionnaire(questionnaire2));
        if (!m_springReqCacheRepository.existsByQuestionnaire1_Id_AndQuestionnaire2_Id(
                questionnaire1.getId(), questionnaire2.getId()))
        {
            return ModelFactory.ReqCache2AReqCache(m_springReqCacheRepository.save(rc));
        }
        return null;
    }

    @Override
    public void delete(AQuestionnaire questionnaire) throws Exception {
        m_springReqCacheRepository.deleteAllByQuestionnaire1(
                ModelFactory.AQuestionnaire2Questionnaire(questionnaire));
    }

    @Override
    public List<AReqCache> findAll(AQuestionnaire questionnaire) throws Exception{
        return ModelFactory.ReqCaches2AReqCaches(
                m_springReqCacheRepository.findAllByQuestionnaire1_Id(
                        questionnaire.getId()));
    }

    @Override
    public void ClearAll() {
        m_springReqCacheRepository.deleteAll();
    }
}
