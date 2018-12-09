package be.stijnhooft.easymail.service.subscription;

import java.util.regex.Pattern;

public class MailFormatter {

    private static final Pattern EMAIL = Pattern.compile("<(.+)@(.+)>");

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

    private boolean thereIsAReferenceToPreviousMailOnThisLine(String line) {
        return EMAIL.matcher(line).find() ||
                (line.contains("> Op ") && line.contains(" heeft ") && line.contains(" het volgende geschreven") ||
                line.contains("Verzonden vanaf mijn Samsung Galaxy-smartphone") ||
                line.contains("-------- Oorspronkelijk bericht"));
    }
}
