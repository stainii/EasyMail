package be.stijnhooft.easymail.service.internal.receiver;


import java.util.List;

import be.stijnhooft.easymail.model.Mail;

public interface OnReceiveMail {

    void onReceiveMail(List<Mail> mails);

}
