package be.stijnhooft.easymail.backend.service.internal.listener;

import be.stijnhooft.easymail.backend.model.Mail;
import be.stijnhooft.easymail.backend.model.Person;

public interface MailListener {

    void onNewMail(Mail mail, Person sender);

}
