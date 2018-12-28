package be.stijnhooft.easymail.backend.service.internal.receiver;

import android.app.Application;

import java.io.IOException;

import javax.mail.Message;
import javax.mail.MessagingException;

import be.stijnhooft.easymail.backend.model.Mail;

public class MailMapper {
    private final MailTextFetcher mailTextFetcher;
    private final MailFormatter mailFormatter;
    private final Application context;

    public MailMapper(Application application) {
        this.context = application;
        mailFormatter = new MailFormatter(application);
        mailTextFetcher = new MailTextFetcher();
    }

    public Mail mapIncomingMail(Message message) {
        try {
            // find sender
            String email = findEmail(message);

            // get and format contents of mail
            String content = mailTextFetcher.getTextFromMessage(message);
            content = mailFormatter.stripAwayPreviousMessages(content);

            // create mail
            return new Mail(email, message.getReceivedDate().toString(), content, true, false);
        } catch (MessagingException | IOException e) {
            throw new MailReceiverException("An error occurred when receiving mails", e);
        }
    }


    private String findEmail(Message message) throws MessagingException {
        String from = message.getFrom()[0].toString();
        return from.substring(from.indexOf("<") + 1, from.indexOf(">"));
    }
}
