package be.stijnhooft.easymail.service.subscription;

import android.app.Application;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import be.stijnhooft.easymail.repository.SettingRepository;

public class MailFormatter {

    private List<Pattern> responsePatterns = new ArrayList<>();
    private SettingRepository settingRepository;

    public MailFormatter(Application application) {
        this(new SettingRepository(application));
    }

    public MailFormatter(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
        readResponsePatterns();
    }


    public String stripAwayPreviousMessages(String mail) {
        String lines[] = mail.split("\\r?\\n");

        StringBuilder result = new StringBuilder(lines[0]);

        if (lines.length > 1) {
            boolean referenceToPreviousMailFound = false;
            int i = 1;
            do {
                if (thereIsAReferenceToPreviousMailOnThisLine(lines[i])) {
                    referenceToPreviousMailFound = true;
                } else {
                    result.append(System.lineSeparator());
                    result.append(lines[i]);
                    i++;
                }
            } while (!referenceToPreviousMailFound && lines.length > i);
        }

        return result.toString().trim();
    }

    private void readResponsePatterns() {
        try {
            JSONArray responsePatternsAsJson = settingRepository.getSettings().getJSONArray("responsePatterns");
            for (int i = 0; i < responsePatternsAsJson.length(); i++) {
                responsePatterns.add(Pattern.compile(responsePatternsAsJson.getString(i)));
            }
        } catch (JSONException e) {
            throw new RuntimeException("Could not read response patterns", e);
        }
    }

    private boolean thereIsAReferenceToPreviousMailOnThisLine(String line) {
        for (Pattern pattern : responsePatterns) {
            if (pattern.matcher(line).find()) {
                return true;
            }
        }
        return false;
    }
}
