package be.stijnhooft.easymail.backend.repository.task;

import android.os.AsyncTask;

import java.util.Arrays;

import be.stijnhooft.easymail.backend.dao.MailDao;
import be.stijnhooft.easymail.backend.model.Mail;

public class MarkMailsAsReadTask extends AsyncTask<Mail, Void, Void> {

    private final MailDao mailDao;

    public MarkMailsAsReadTask(MailDao mailDao)  {
        this.mailDao = mailDao;
    }

    @Override
    protected Void doInBackground(Mail... mails) {
        for (Mail mail : mails) {
            mail.setRead(true);
        }
        this.mailDao.update(Arrays.asList(mails));
        return null;
    }
}
