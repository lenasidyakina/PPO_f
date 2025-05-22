package ru.bmstu.iu7.API;

import ru.bmstu.iu7.API.model.Tag;

import java.io.IOException;
import java.util.List;

public interface IML_port {
    List<Tag> get_tags_names(String question, String answer, List<Tag> tags) throws IOException, InterruptedException;
}
