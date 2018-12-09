package be.stijnhooft.easymail.repository.task;

import android.os.AsyncTask;

import be.stijnhooft.easymail.dao.MailDao;
import be.stijnhooft.easymail.model.Mail;

public class SaveMailTask  extends AsyncTask<Mail, Void, Void> {

    private final MailDao mailDao;

    public SaveMailTask(MailDao mailDao)  {
        this.mailDao = mailDao;
    }

    @Override
    protected Void doInBackground(Mail... mails) {
        this.mailDao.save(mails[0]);
        return null;
    }
}
