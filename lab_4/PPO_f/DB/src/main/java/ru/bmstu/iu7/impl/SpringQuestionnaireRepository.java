package ru.bmstu.iu7.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bmstu.iu7.impl.model.Question;
import ru.bmstu.iu7.impl.model.Questionnaire;
import ru.bmstu.iu7.impl.model.Tag;
import ru.bmstu.iu7.impl.QuestionTagDto;
import java.util.List;



@Repository
public interface SpringQuestionnaireRepository extends JpaRepository<Questionnaire, Long>{
    List<Questionnaire> findByUserId(Long userId);
    void deleteByUserId(Long userId);
    @Query(value = "SELECT * FROM recommend_questionnaires(:questionnaireId)", nativeQuery = true)
    List<Long> findRecommendedQuestionnaireIds(@Param("questionnaireId") Long questionnaireId);
    @Query(value = "SELECT " +
            "question_id as questionId, " +
            "question_text as questionText, " +
            "tag_id as tagId, " +
            "tag_name as tagName " +
            "FROM get_top_question_tags_for_information()", nativeQuery = true)
    List<QuestionTagDto> findTopQuestionTags();
}
