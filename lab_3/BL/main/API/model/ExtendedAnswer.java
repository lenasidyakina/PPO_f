package ru.bmstu.iu7.API.model;


import java.util.List;

public class ExtendedAnswer {
    Question question;
    int weight;
    String answer;
    List<Tag> tags;

    public ExtendedAnswer(Question question, int weight, String answer, List<Tag> tags) {
        this.question = question;
        this.weight = weight;
        this.answer = answer;
        this.tags = tags;
    }
    public int getWeight() {
        return weight;
    }
    public void setWeight(int weight) {
        this.weight = weight;
    }
    public String getAnswer() {
        return answer;
    }
    public void setAnswer(String answer) {
        this.answer = answer;
    }
    public List<Tag> getTags() {
        return tags;
    }
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
    public Question getQuestion() {
        return question;
    }
}
