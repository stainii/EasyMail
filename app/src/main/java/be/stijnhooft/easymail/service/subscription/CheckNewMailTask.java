package be.stijnhooft.easymail.service.subscription;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;

import be.stijnhooft.easymail.EasyMailApplication;
import be.stijnhooft.easymail.model.Mail;

public class CheckNewMailTask implements Runnable {

    private final MailSubscriptionConfiguration configuration;
    private final Collection<MailSubscriber> subscribers;
    private final MailMapper mailMapper;

    public CheckNewMailTask(MailSubscriptionConfiguration configuration, Collection<MailSubscriber> subscribers) {
        this.configuration = configuration;
        this.subscribers = subscribers;
        this.mailMapper = new MailMapper(EasyMailApplication.getInstance());
    }

    @Override
    public void run() {
        Properties properties = new Properties();
        properties.put("mail.store.protocol", configuration.getProtocol().name().toLowerCase());
        properties.put("mail.imaps.host", configuration.getHost());
        properties.put("mail.imaps.port", configuration.getPort());

        Session session = Session.getDefaultInstance(properties, null);
        try (Store store = session.getStore(configuration.getProtocol().name().toLowerCase())) {
            store.connect(configuration.getEmailAddress(), configuration.getPassword());

            try (Folder inbox = store.getFolder("inbox")) {
                inbox.open(Folder.READ_WRITE);
                Message[] messages = fetchUnseenMessages(inbox);
                notifySubscribers(messages);
                markMessagesAsRead(inbox, messages);
            }
        } catch (MessagingException e) {
            Log.d("CheckNewMail", "Could not setup connection", e);
        }
    }

    private Message[] fetchUnseenMessages(Folder inbox) throws MessagingException {
        return inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
    }

    private void notifySubscribers(Message[] messages) {
        List<Mail> mails = new ArrayList<>();
        for (Message message : messages) {
            Mail mail = mailMapper.mapIncomingMailIfSenderIsKnown(message);
            if (mail != null) {
                mails.add(mail);
            }
        }
        if (!mails.isEmpty()) {
            this.emit(mails);
        }
    }

    private void markMessagesAsRead(Folder inbox, Message[] messages) {
        for (Message message : messages) {
            try {
                inbox.setFlags(new Message[]{message}, new Flags(Flags.Flag.SEEN), true);
            } catch (MessagingException e) {
                System.err.println("An error occurred when marking mails as read");
                System.err.println(e);
            }
        }
    }

    private void emit(List<Mail> mails) {
        for (MailSubscriber subscriber : subscribers) {
            subscriber.onReceiveMail(mails);
        }
    }
}
