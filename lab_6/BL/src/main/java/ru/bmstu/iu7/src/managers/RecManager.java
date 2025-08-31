package ru.bmstu.iu7.src.managers;

import ru.bmstu.iu7.API.AppLogger;
import ru.bmstu.iu7.API.model.*;
import ru.bmstu.iu7.src.controllers.QuestionnaireController;
import ru.bmstu.iu7.src.controllers.ReqCacheController;

import java.util.*;

public class RecManager {
    private final QuestionnaireController m_controller;
    private static boolean is_update_running = false;
    private final Object sync = new Object();
    private final AppLogger logger;

    Timer timer = new Timer();

    public RecManager(QuestionnaireController controller) {
        this(controller, null);
    }

    public RecManager(QuestionnaireController controller, AppLogger applogger) {
        this.m_controller = controller;
        this.logger = applogger;
        if (logger != null) logger.info("RecManager initialized");
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (m_controller.get_active_questionnaire() != null) {
                    doGetFriends();
                    ReqCacheController.running = true;
                }
            }
        };
        timer.scheduleAtFixedRate(task, 60*1000, 60*1000);
    }

/*
    public void scheduleRun()
    {
        new ScheduledTask().run();
    }

 */

    public void doGetFriends()
    {
        if (logger != null) {
            logger.info("Get Friends");
            logger.info("ScheduledTask started for updating recommendations");
        }
        try {
            List<AQuestionnaire> all;
            int page = 0;
            AQuestionnaire current = m_controller.get_active_questionnaire();
            List<AQuestionnaire> black_list = current.getBlackList();
            List<AQuestionnaire> fav_list = current.getFavList();
            int pageSize = 1000;
            m_controller.clear_req_cache_active_quest();
            while (true) {
                all = (List<AQuestionnaire>) m_controller.get_questionnairies(page, pageSize, current.getUser().getId());
                Iterator<AQuestionnaire> it = all.iterator();
                List<AQuestionnaire> list = new ArrayList<>();
                while (it.hasNext()) {
                    AQuestionnaire q = it.next();
                    if (!black_list.contains(q) && !fav_list.contains(q)) {
                       list.add(q);
                    }
                }
                if (list.isEmpty()) break;
                recommended_questionnaires(current, list);
                if (logger != null) logger.info("Processed page {} for recommendations", page);
                page += 1;
            }
            m_controller.update_req_cache();
            if (logger != null) logger.info("Request cache updated successfully");
        } catch (Exception e) {
            if (logger != null) logger.info("ScheduledTask finished");
        }
    }

/*
    public class ScheduledTask extends TimerTask {
        public void run() {
            doGetFriends();

            if (logger != null) logger.info("ScheduledTask started for updating recommendations");
            try {
                List<AQuestionnaire> all;
                int page = 0;
                AQuestionnaire current = m_controller.get_active_questionnaire();
                List<AQuestionnaire> black_list = current.getBlackList();
                List<AQuestionnaire> fav_list = current.getFavList();
                int pageSize = 1000;
                m_controller.clear_req_cache_active_quest();
                while (true) {
                    all = (List<AQuestionnaire>) m_controller.get_questionnairies(page, pageSize, current.getUser().getId());
                    Iterator<AQuestionnaire> it = all.iterator();
                    while (it.hasNext()) {
                        AQuestionnaire q = it.next();
                        if (black_list.contains(q) || fav_list.contains(q)) {
                            it.remove();
                        }
                    }

                    if (all.isEmpty()) break;
                    recommended_questionnaires(current, all);
                    if (logger != null) logger.info("Processed page {} for recommendations", page);
                    page += 1;
                }

                m_controller.update_req_cache();
                if (logger != null) logger.info("Request cache updated successfully");
            } catch (Exception e) {
                if (logger != null) logger.error("Error during recommendation task", e);
            } finally {
                is_update_running = false;
                synchronized (sync) {
                    sync.notifyAll();
                }
                if (logger != null) logger.info("ScheduledTask finished");
            }
        }
    }
*/

    public List<AQuestionnaire> get_friends() {
        try {
            if (m_controller.get_active_questionnaire() == null) {
                if (logger != null) logger.warn("No active questionnaire found when getting friends");
                return Collections.emptyList();
            } else {
                if (!ReqCacheController.running) { //.isEmpty()) {
                    ReqCacheController.running = true;
                    doGetFriends();
                    //scheduleRun();
                }
                /*
                synchronized (sync) {
                    if (!is_update_running){
                        is_update_running = true;

                        // Сразу первый запуск
                        new ScheduledTask().run();

                        // Запускаем обновления каждые 30 секунд
                        Timer time = new Timer(true); // daemon
                        ScheduledTask st = new ScheduledTask();
                        time.schedule(st, 0, 1000 * 60 * 30);

                        if (logger != null) logger.info("Recommendation update scheduled every 30 sec");
                    }
                } */
            }
            if (logger != null) logger.info("Returning cached friends list");
            return m_controller.get_quest_in_cache();
        } catch (Exception e) {
            if (logger != null) logger.error("Failed to get friends", e);
            return Collections.emptyList();
        }
    }


//    public List<AQuestionnaire> get_friends() {
//        try {
//            if (m_controller.get_active_questionnaire() == null) {
//                if (logger != null) logger.warn("No active questionnaire found when getting friends");
//                return Collections.emptyList();
//            } else {
//                if (!is_update_running) {
//                    synchronized (sync) {
//                        is_update_running = true;
//                        Timer time = new Timer();
//                        ScheduledTask st = new ScheduledTask();
//                        time.schedule(st, 0, 1000 * 60 * 30);
//                        if (logger != null) logger.info("Recommendation update scheduled");
//                        sync.wait();
//                    }
//                }
//            }
//            if (logger != null) logger.info("Returning cached friends list");
//            return m_controller.get_quest_in_cache();
//        } catch (Exception e) {
//            if (logger != null) logger.error("Failed to get friends", e);
//            return Collections.emptyList();
//        }
//    }

    public int information_comparison(AInformation self, AInformation other) {
        try {
            if (logger != null) logger.info("Comparing information between two questionnaires");
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
            for (int i = 0; i < 1; i++) {
                ATag tag1 = var_answer_1.get(i).getTag();
                ATag tag2 = var_answer_2.get(i).getTag();
                if (tag1 != null && tag1.equals(tag2)) {
                    similarity_var_answer += var_answer_1.get(i).getWeight();
                }
            }

            int total_similarity = similarity_ex_answer + similarity_var_answer;
            if (logger != null) logger.info("Information comparison result: {}", total_similarity);
            return total_similarity;
        } catch (Exception e) {
            if (logger != null) logger.error("Error during information comparison", e);
            return 0;
        }
    }

    int count = 0;

    public void recommended_questionnaires(AQuestionnaire current, List<AQuestionnaire> all) {
        try {
            if (logger != null) logger.info("Generating recommended questionnaires");
            double harmonic_average;
            double max_harmonic_average;
            double harmonic_average_norm;
            int count_extnd_tags = 1;
            int count_var_tags = 1;

            count = 0;

            logger.info("Number of questionnaires: " + all.size());

            for (AQuestionnaire questionnaire : all) {
                if (Objects.equals(current.getUser().getId(), questionnaire.getUser().getId())) {
                    logger.info("Same id: " + questionnaire.getUser().getId());
                    continue;
                }
                logger.info("Number of questionnaires at pass " + count + " = " + all.size());


                int first_similarity_coeff = information_comparison(current.getInformation(), questionnaire.getSearchInformation());
                int second_similarity_coeff = information_comparison(current.getSearchInformation(), questionnaire.getInformation());

                if (first_similarity_coeff + second_similarity_coeff == 0) {
                    harmonic_average_norm = 0;
                } else {
                    harmonic_average = (double) (2 * first_similarity_coeff * second_similarity_coeff) /
                            (first_similarity_coeff + second_similarity_coeff);
                    max_harmonic_average = 10 * (count_extnd_tags + count_var_tags);
                    harmonic_average_norm = harmonic_average / max_harmonic_average;
                }

                if ((harmonic_average_norm >= 0) && (!m_controller.is_in_black(questionnaire) &&
                        (!m_controller.is_in_fav(questionnaire)))) {
                    logger.info("count = " + (count++));
                    m_controller.add_in_cache_list(harmonic_average_norm, questionnaire);
                    if (logger != null) logger.info("Added questionnaire {} to recommendation cache with score {}", questionnaire.getId(), harmonic_average_norm);
                }
            }
        } catch (Exception e) {
            if (logger != null) logger.error("Error generating recommended questionnaires", e);
        }
    }
}
