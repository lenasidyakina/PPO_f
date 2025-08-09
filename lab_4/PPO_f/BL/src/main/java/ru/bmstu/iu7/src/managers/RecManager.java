package ru.bmstu.iu7.src.managers;

import ru.bmstu.iu7.API.model.*;
import ru.bmstu.iu7.src.controllers.QuestionnaireController;

import java.util.*;
import java.util.logging.Logger;


public class RecManager {
    private final QuestionnaireController m_controller;
    private boolean is_update_running = false;
    private final Object sync = new Object();
    public class ScheduledTask extends TimerTask {

        public void run(){
            List<AQuestionnaire> all;
            int page = 0;
            AQuestionnaire current = m_controller.get_active_questionnaire();
            int pageSize = 1000;
            while (true){
                try {
                    all = (List<AQuestionnaire>)m_controller.get_questionnairies(page, pageSize, current.getUser().getId());
                    if (all.isEmpty())
                        break;
                    recommended_questionnaires(current, all);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                page += 1;
            };
            try {
                m_controller.update_req_cache();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            is_update_running = false;
             synchronized (sync) {
                sync.notifyAll();
            }
        }
    }

    public RecManager(QuestionnaireController controller) {
        this.m_controller = controller;
    }

    private static Logger log = Logger.getLogger(RecManager.class.getName());


    public List<AQuestionnaire> get_friends() throws Exception {
        if (m_controller.get_active_questionnaire() == null) {
            throw new Exception();
        }
        else
        {
            if (!is_update_running)
            {
                synchronized (sync) {
                    is_update_running = true;
                    Timer time = new Timer();
                    ScheduledTask st = new ScheduledTask();
                    time.schedule(st, 0, 1000 * 60 * 30);
                    sync.wait();
                }
            }
        }
        return m_controller.get_quest_in_cache();
    }


    public int information_comparison(AInformation self, AInformation other) throws Exception {
        List<AExtendedAnswer> ex_answer_1 = self.getExtendedAnswers();
        List<AExtendedAnswer> ex_answer_2 = other.getExtendedAnswers();

        int similarity_ex_answer = 0;
        for (int i = 0; i < 1; i++) {
            int totalMatches = 0;
            Set<ATag> tagsSet = new HashSet<>(ex_answer_1.get(i).getTags());

            for (ATag tag : ex_answer_2.get(i).getTags()) {
                if (tagsSet.contains(tag)) {
                    totalMatches++;
                    tagsSet.remove(tag);
                }
            }
            similarity_ex_answer += totalMatches * ex_answer_1.get(i).getWeight();
        }

        List<AVariantAnswer> var_answer_1 = self.getVariantAnswers();
        List<AVariantAnswer> var_answer_2 = other.getVariantAnswers();

        int similarity_var_answer = 0;
        for (int i = 0; i < 1; i++){;
            ATag tag1 = var_answer_1.get(i).getTag();
            ATag tag2 = var_answer_2.get(i).getTag();
            if (tag1 != null && tag1.equals(tag2)) {
                similarity_var_answer += var_answer_1.get(i).getWeight();
            }
        }

        return similarity_ex_answer + similarity_var_answer;
    }



    public void recommended_questionnaires(AQuestionnaire current, List<AQuestionnaire> all) throws Exception {
        double harmonic_average;
        double max_harmonic_average;
        double harmonic_average_norm;
        int count_extnd_tags = 1;
        int count_var_tags = 1;
        for (AQuestionnaire questionnaire : all) {
            if (Objects.equals(current.getUser().getId(), questionnaire.getUser().getId())) {
                continue;
            }
            int first_similarity_coeff = information_comparison(current.getInformation(), questionnaire.getSearchInformation());
            int second_similarity_coeff = information_comparison(current.getSearchInformation(), questionnaire.getInformation());

            if (first_similarity_coeff + second_similarity_coeff == 0)
                harmonic_average_norm = 0;
            else {
                harmonic_average = (double) (2 * first_similarity_coeff * second_similarity_coeff) / (first_similarity_coeff + second_similarity_coeff);
                max_harmonic_average = 10 * (count_extnd_tags + count_var_tags);
                harmonic_average_norm = harmonic_average / max_harmonic_average;
            }
            if ((harmonic_average_norm > 0) && (!m_controller.is_in_black(questionnaire) && (!m_controller.is_in_fav(questionnaire))))
                m_controller.add_in_cache_list(harmonic_average_norm, questionnaire);
        }
    }


}
