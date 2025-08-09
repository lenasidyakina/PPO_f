package ru.bmstu.iu7;

import ru.bmstu.iu7.API.IQuestionRepository;
import ru.bmstu.iu7.API.model.AQuestion;
import ru.bmstu.iu7.API.model.ATag;

import java.util.List;

public class QuestionRepository implements IQuestionRepository {
    @Override
    public List<AQuestion> get_questions() throws Exception {
        return null;
    }

    @Override
    public List<ATag> get_question_tags(int id) throws Exception {
        return null;
    }
}
