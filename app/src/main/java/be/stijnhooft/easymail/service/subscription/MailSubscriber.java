package be.stijnhooft.easymail.service.subscription;


import java.util.List;

import be.stijnhooft.easymail.model.Mail;

public interface MailSubscriber {

    void onReceiveMail(List<Mail> mails);

}
