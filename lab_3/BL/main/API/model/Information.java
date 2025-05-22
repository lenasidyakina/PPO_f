package ru.bmstu.iu7.API.model;
import java.lang.ref.SoftReference;
import java.util.List;

public class Information {
    int id;
    List<VariantAnswer> variant_answers;
    List<ExtendedAnswer> extended_answers;

    public Information(int id, List<VariantAnswer> variant_answers, List<ExtendedAnswer> extended_answers) {
        this.id = id;
        this.variant_answers = variant_answers;
        this.extended_answers = extended_answers;
    }

    public List<ExtendedAnswer> getExtended_answers() {
        return extended_answers;
    }
    public void setExtended_answers(List<ExtendedAnswer> extended_answers) {
        this.extended_answers = extended_answers;
    }
    public List<VariantAnswer> getVariant_answers() {
        return variant_answers;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
