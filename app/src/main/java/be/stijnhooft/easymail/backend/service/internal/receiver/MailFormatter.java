package be.stijnhooft.easymail.backend.service.internal.receiver;

import android.app.Application;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import be.stijnhooft.easymail.backend.repository.SettingRepository;

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

        if (lines.length > 0) {
            StringBuilder result = new StringBuilder();

            boolean referenceToPreviousMailFound = false;
            int i = 0;
            do {
                if (thereIsAReferenceToPreviousMailOnThisLine(lines[i])) {
                    result.append(System.lineSeparator());
                    result.append(stripAwayReferenceToPreviousMailOnThisLine(lines[i]));
                    referenceToPreviousMailFound = true;
                } else {
                    result.append(System.lineSeparator());
                    result.append(lines[i]);
                    i++;
                }
            } while (!referenceToPreviousMailFound && lines.length > i);

            return result.toString().trim();
        } else {
            return "";
        }
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

    private String stripAwayReferenceToPreviousMailOnThisLine(String line) {
        for (Pattern pattern : responsePatterns) {
            final Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                return line.substring(0, matcher.start());
            }
        }
        return line;
    }
}
