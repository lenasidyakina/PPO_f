package ru.bmstu.iu7.API.model;
import java.util.Objects;
import ru.bmstu.iu7.API.IML_port;
import ru.bmstu.iu7.API.IQuestionnaireRepository;

public class Tag {
    int id;
    String name;

    public Tag(int id, String name)
    {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        if (id != tag.id) return false;
        if (name != null ? !name.equals(tag.name) : tag.name != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name); // имя тега, по которому сравниваются
    }

}
