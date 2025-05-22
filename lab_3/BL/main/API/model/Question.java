package ru.bmstu.iu7.API.model;

import java.util.List;

public class Question {
    int id;
    String question;
    List<Tag> tags;

    public Question(int id, String question, List<Tag> tags) {
        this.id = id;
        this.question = question;
        this.tags = tags;
    }
    public List<Tag> getTags() {
        return tags;
    }

    public String getQuestion() {
        return question;
    }
}
