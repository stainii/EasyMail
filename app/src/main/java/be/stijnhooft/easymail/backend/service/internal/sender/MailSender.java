package be.stijnhooft.easymail.backend.service.internal.sender;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender {

    private MailSenderConfiguration configuration;

    public MailSender(MailSenderConfiguration configuration) {
        this.configuration = configuration;
    }

    public void sendMail(String content, String to) {
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", configuration.getHost());
        properties.put("mail.smtp.port", configuration.getPort());
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Get the default Session object.
        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(configuration.getEmailAddress(), configuration.getPassword());
                    }
                });


        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(configuration.getEmailAddress()));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setText(content);

            if (content.length() > 30) {
                message.setSubject(content.substring(0,27) + "...");
            } else {
                message.setSubject(content);
            }

            Transport.send(message);
        } catch (MessagingException mex) {
            throw new MailSenderException("Could not send message", mex);
        }
    }
}
