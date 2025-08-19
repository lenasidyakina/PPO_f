package ru.bmstu.iu7;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bmstu.iu7.API.model.*;

import java.util.*;

@RestController
@RequestMapping("api/v1")
@CrossOrigin(origins="http://localhost:3000")
public class Controller {

    @GetMapping("/users")
    public List<AUser> getUsers() {
        List<AUser> users = LabService.userRepository.findAll();
        return users;
    }

    @GetMapping("/questions")
    public List<AQuestion> getQuestions() throws Exception {
        List<AQuestion> qs = LabService.mainManager.getM_que_manager().get_all_questions();
        return qs;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody Map<String, String> crs) {
        try {
            String name = crs.get("name");
            String password = crs.get("password");
            AUser user = LabService.mainManager.getM_user_manager().register(name, password);
            if (user == null)
                throw new Exception();
            LabService.currentUser = user;
            return new ResponseEntity<Object>(user, HttpStatus.OK);
        }
        catch (Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", "registration is  failed");
            return ResponseEntity.ok(map);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestBody Map<String, String> crs) {
        try {
            String name = crs.get("name");
            String password = crs.get("password");
            AUser user = LabService.mainManager.getM_user_manager().authorize(name, password);
            if ((user == null) || !password.equals(user.getPassword()))
                throw new Exception();
            LabService.currentUser = user;
            return new ResponseEntity<Object>(user, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/user/delete")
    public ResponseEntity<Object> deleteUser(long id) {
        try {
            LabService.mainManager.getM_user_manager().delete(id);
        }
        catch (Exception e) {

        }
        return ResponseEntity.ok("");
    }

    @GetMapping("/logout")
    public ResponseEntity<Object> logoutUser() {
        LabService.currentUser = null;
        return ResponseEntity.ok("");
    }

    @PostMapping("/deleteuserquests")
    public ResponseEntity<Object> deleteAllUserQuests(long userId) {
        try {
            LabService.mainManager.getM_que_manager().delete_user_questionnaires(userId);
        }
        catch (Exception e) {

        }
        return ResponseEntity.ok("");
    }

    @PostMapping("/createquest")
    public ResponseEntity<Object> createQuest(long userId) {
        try {
            LabService.mainManager.getM_que_manager().delete_user_questionnaires(userId);
        }
        catch (Exception e) {

        }
        return ResponseEntity.ok("");
    }

    @GetMapping("/userquests")
    public List<AQuestionnaire> getUserQuests() throws Exception {
        if (LabService.currentUser == null)
            return new ArrayList<>();
        List<AQuestionnaire> list = LabService.mainManager.getM_que_manager().
                get_user_questionnaies(LabService.currentUser.getId());
        return list;
    }

    @GetMapping("/getfriends/{id}")
    public List<AUser> getFriends(@PathVariable int id) throws Exception {
        if (LabService.currentUser == null)
            return new ArrayList<>();
        List<AQuestionnaire> list = LabService.mainManager.getM_que_manager().
                get_user_questionnaies(LabService.currentUser.getId());

        AQuestionnaire activeQuest = list.get(id);
        LabService.mainManager.getM_que_manager().set_active_questionnaire(activeQuest);
        List<AQuestionnaire>  qs = LabService.mainManager.getM_rec_manager().get_friends();

        List<AUser> users = new ArrayList<>();
        for (AQuestionnaire q : qs) {
            AUser user = q.getUser();
            if (!user.equals(LabService.currentUser)) {
                if (!users.contains(user) && !activeQuest.getBlackList().contains(user) &&
                        !activeQuest.getFavList().contains(user)) {
                    users.add(user);
                }
            }
        }
        return users;
    }

    @PostMapping("/submitquest")
    public ResponseEntity<Object> submitQuest(@RequestBody ArrayList<ArrayList<String>> data) throws Exception {
        // answer type EXT or VAR, answer, weight, otherAnswer, otherWeight

        if (LabService.currentUser == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        List<AQuestion> qs = LabService.mainManager.getM_que_manager().get_all_questions();

        List<AVariantAnswer> variantAnswers = new ArrayList<>();
        List<AExtendedAnswer> extendedAnswers = new ArrayList<>();
        List<AVariantAnswer> otherVariantAnswers = new ArrayList<>();
        List<AExtendedAnswer> otherExtendedAnswers = new ArrayList<>();

        int cnt = 0;
        for (AQuestion q : qs) {
            ArrayList<String> v = data.get(cnt++);
            if (v.get(0).equals("EXT")) {
                String answer = v.get(1);
                int weight = Integer.parseInt(v.get(2));
                List<ATag> tags = new ArrayList<>();
                AExtendedAnswer an = new AExtendedAnswer(0L, q, weight, answer, tags);
                extendedAnswers.add(an);

                String otherAnswer = v.get(3);
                int otherWeight = Integer.parseInt(v.get(4));
                List<ATag> otherTags = new ArrayList<>();
                AExtendedAnswer oan = new AExtendedAnswer(0L, q, otherWeight, otherAnswer, otherTags);
                otherExtendedAnswers.add(oan);
            }
            else {
                String answer = v.get(1);
                int weight = Integer.parseInt(v.get(2));
                ATag tag = LabService.mainManager.getM_que_manager().find_tag(answer);
                AVariantAnswer an = new AVariantAnswer(0L,weight, tag, q);
                variantAnswers.add(an);

                String otherAnswer = v.get(3);
                int otherWeight = Integer.parseInt(v.get(4));
                ATag otherTag = LabService.mainManager.getM_que_manager().find_tag(otherAnswer);
                AVariantAnswer oan = new AVariantAnswer(0L,otherWeight, otherTag, q);
                otherVariantAnswers.add(oan);
            }
        }
        AInformation inf = new AInformation(0L, variantAnswers, extendedAnswers);
        AInformation otherInf = new AInformation(0L, otherVariantAnswers, otherExtendedAnswers);
        AQuestionnaire questionnaire = LabService.mainManager.getM_que_manager().create(
                LabService.currentUser, inf, otherInf);
        if (questionnaire == null)
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(HttpStatus.OK);
    }






}
