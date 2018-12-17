package be.stijnhooft.easymail.service;

import org.junit.Test;

import java.util.Date;

import be.stijnhooft.easymail.service.internal.sender.MailSender;
import be.stijnhooft.easymail.service.internal.sender.MailSenderFactory;

public class RepeatSendingsMailsTest {

    @Test
    public void repeatSendingMails() throws InterruptedException {
        while(true) {
            MailSender mailSender = MailSenderFactory.configure()
                    .emailAddress("stijnhooft@hotmail.com")
                    .host("smtp-mail.outlook.com")
                    .port(587)
                    .password("fbn44f9")
                    .build();
            mailSender.sendMail(new Date().toString(), "stijnhooft@gmail.com");
            Thread.sleep(60000);
        }
    }
}
