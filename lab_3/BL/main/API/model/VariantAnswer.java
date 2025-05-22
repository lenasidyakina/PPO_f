package ru.bmstu.iu7.API.model;

public class VariantAnswer
{
    int weight;
    Tag tag;
    Question question;

    public VariantAnswer(int weight, Tag tag, Question question){
        this.weight = weight;
        this.tag = tag;
        this.question = question;
    }
    public int getWeight() {
        return weight;
    }
    public Tag getTag() {
        return tag;
    }
}
