package ru.bmstu.iu7.API.model;

import java.util.List;
import java.util.Objects;

public class AQuestionnaire {

    Long id;
    AUser user;
    List<AQuestionnaire> black_list;
    List<AQuestionnaire> fav_list;
    AInformation information;
    AInformation search_information;
    boolean censored = Boolean.FALSE;

    public AQuestionnaire(Long id, AUser user, AInformation information,
                          AInformation search_information, List<AQuestionnaire> black_list,
                          List<AQuestionnaire> fav_list) {
        this.id = id;
        this.user = user;
        this.information = information;
        this.search_information = search_information;
        this.black_list = black_list;
        this.fav_list = fav_list;
    }

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) { this.id = id; }
    public AUser getUser() {
        return this.user;
    }
    public void setUser(AUser user) {
        this.user = user;
    }
    public AInformation getInformation() {
        return this.information;
    }
    public void setInformation(AInformation information) {
        this.information = information;
    }
    public AInformation getSearchInformation() {
        return this.search_information;
    }
    public boolean isCensored() {
        return this.censored;
    }
    public void setCensored(boolean censored) {
        this.censored = censored;
    }
    public List<AQuestionnaire> getBlackList() {
        return this.black_list;
    }
    public List<AQuestionnaire> getFavList() {
        return this.fav_list;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AQuestionnaire that = (AQuestionnaire) o;
        return Objects.equals(id, that.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    public String toString() {
        return information.toString();
    }
    public void addBlack(AQuestionnaire black) {
        this.black_list.add(black);
    }
    public void addFav(AQuestionnaire fav) {
        this.fav_list.add(fav);
    }
}
