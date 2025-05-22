package ru.bmstu.iu7.API.model;

import java.util.List;

public class Questionnaire {
    public int user_id;
    int id;
    List<Question> questions;
    List<Questionnaire> black_list;
    List<Questionnaire> fav_list;
    Information information;
    Information search_information;
    boolean censored = Boolean.FALSE;

    public Questionnaire(int id, Information information, Information searchInformation)
    {
        this.id = id;
        this.information = information;
        this.search_information = searchInformation;
    }

    public void add_fav(Questionnaire questionnaire)
    {
        fav_list.add(questionnaire);
    }
    public List<Questionnaire> get_black_list(){
        return black_list;
    }
    public List<Questionnaire> get_fav_list(){
        return fav_list;
    }
    public void add_black(Questionnaire questionnaire)
    {
        black_list.add(questionnaire);
    }
    public int get_id() {
        return id;
    }
    public void set_censored(boolean censored){
        this.censored = censored;
    }
    public Information get_information() {
        return information;
    }
    public Information get_search_information() {
        return search_information;
    }

    public boolean is_censored() {
        return censored;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Questionnaire that = (Questionnaire) o;
        return id == that.id;
    }

    @Override
    public String toString() {
        return information.toString();
    }
}
