package ru.bmstu.iu7;

import ru.bmstu.iu7.API.IML_port;
import ru.bmstu.iu7.API.model.ATag;
import ru.bmstu.iu7.src.controllers.QuestionnaireController;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ML_port implements IML_port {
    private  String olamaHost;
    private static Logger log = Logger.getLogger(ML_port.class.getName());


    public ML_port(String host) {
        this.olamaHost = host;
        log.info("OLAMA HOST is" + olamaHost);
    }

    private static Map<String, String> parseJson(String jsonStr) {
        Map<String, String> result = new LinkedHashMap<>();

        jsonStr = jsonStr.trim();
        if (jsonStr.startsWith("{") && jsonStr.endsWith("}")) {
            jsonStr = jsonStr.substring(1, jsonStr.length() - 1).trim();
        } else {
            throw new IllegalArgumentException("Invalid JSON object");
        }

        boolean inQuotes = false;
        StringBuilder keyOrValue = new StringBuilder();
        List<String> tokens = new ArrayList<>();
        for (int i = 0; i < jsonStr.length(); i++) {
            char c = jsonStr.charAt(i);
            if (c == '\"') {
                inQuotes = !inQuotes;
                keyOrValue.append(c);
            } else if (c == ',' && !inQuotes) {
                tokens.add(keyOrValue.toString().trim());
                keyOrValue.setLength(0);
            } else {
                keyOrValue.append(c);
            }
        }
        if (!keyOrValue.isEmpty()) {
            tokens.add(keyOrValue.toString().trim());
        }

        for (String token : tokens) {
            int colonIndex = -1;
            boolean quoteFlag = false;
            for (int i = 0; i < token.length(); i++) {
                if (token.charAt(i) == '"') {
                    quoteFlag = !quoteFlag;
                }
                if (token.charAt(i) == ':' && !quoteFlag) {
                    colonIndex = i;
                    break;
                }
            }

            if (colonIndex == -1) continue;

            String key = token.substring(0, colonIndex).trim();
            String value = token.substring(colonIndex + 1).trim();

            key = stripQuotes(key);
            value = stripQuotes(value);

            result.put(key, value);
        }

        return result;
    }


    private static String stripQuotes(String str) {
        if (str.startsWith("\"") && str.endsWith("\"")) {
            return str.substring(1, str.length() - 1);
        }
        return str;
    }
    @Override
    public List<ATag> get_tags_names(String question, String answer, List<ATag> tags) throws IOException, InterruptedException {
        StringBuilder tags_name_list = new StringBuilder();
        for (ATag tag : tags) {
            tags_name_list.append(tag.getName());
            tags_name_list.append(",");
        }
        String requestBody = "{\"stream\": false, \"model\": \"gemma3:4b-it-qat\", \"prompt\": \"" +
                "Given the question: " +
                question +
                "Here is the answer:" +
                answer +
                "Available tags:" +
                tags_name_list +
                "Extract at least two tags from the answer and respond with nothing but the tags, separated by commas.\"}";


        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .uri(URI.create(olamaHost))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        Map<String, String> parsed = parseJson(response.body());

        List<String> tags_names = new ArrayList<>(List.of((parsed.get("response")).split(",")));
        int lastIndex = tags_names.size() - 1;
        String last = tags_names.get(lastIndex);
        if (last.contains("\\n")) {
            last = last.substring(0, last.length() - 2);
        }
        tags_names.set(lastIndex, last);
        List<ATag> answer_tags = new ArrayList<>();
        for (String tag_name : tags_names) {
             for (ATag tag : tags){
                if ((tag.getName()).equals(tag_name)) {
                    answer_tags.add(tag);
                }
            }
        }
        return answer_tags;
    }
}
