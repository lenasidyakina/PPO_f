package ru.bmstu.iu7.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bmstu.iu7.impl.model.Questionnaire;
import ru.bmstu.iu7.impl.model.ReqCache;

import java.util.List;

@Repository
public interface SpringReqCacheRepository extends JpaRepository<ReqCache, Long>{
    void deleteAllByQuestionnaire1(Questionnaire questionnaire);
    List<ReqCache> findAllByQuestionnaire1_Id(Long questionnaire1Id);

    boolean existsByQuestionnaire1_Id_AndQuestionnaire2_Id(
            Long questionnaire1Id, Long questionnaire2Id);
}
