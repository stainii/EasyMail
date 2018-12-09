package be.stijnhooft.easymail.service.subscription;

import android.app.Application;

import java.io.IOException;

import javax.mail.Message;
import javax.mail.MessagingException;

import be.stijnhooft.easymail.model.Mail;
import be.stijnhooft.easymail.model.Person;
import be.stijnhooft.easymail.repository.PersonRepository;

public class MailMapper {
    private final MailTextFetcher mailTextFetcher = new MailTextFetcher();
    private final MailFormatter mailFormatter = new MailFormatter();
    private final Application context;

    public MailMapper(Application application) {
        this.context = application;
    }

    public Mail mapIncomingMailIfSenderIsKnown(Message message) {
        try {
            // find sender
            Person sender = findPersonOrNull(message);
            if (sender == null) {
                return null;
            }

            // get and format contents of mail
            String content = mailTextFetcher.getTextFromMessage(message);
            content = mailFormatter.stripAwayPreviousMessages(content);

            // create mail
            return new Mail(sender, message.getReceivedDate().toString(), content, true);
        } catch (MessagingException | IOException e) {
            throw new MailSubscriptionException("An error occurred when receiving mails", e);
        }
    }


    private Person findPersonOrNull(Message message) throws MessagingException {
        String from = message.getFrom()[0].toString();
        String email = from.substring(from.indexOf("<") + 1, from.indexOf(">"));
        return new PersonRepository(context).findByEmail(email);
    }
}
