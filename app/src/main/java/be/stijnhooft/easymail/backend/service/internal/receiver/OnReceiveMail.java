package be.stijnhooft.easymail.backend.service.internal.receiver;


import java.util.List;

import be.stijnhooft.easymail.backend.model.Mail;

public interface OnReceiveMail {

    void onReceiveMail(List<Mail> mails);

}
