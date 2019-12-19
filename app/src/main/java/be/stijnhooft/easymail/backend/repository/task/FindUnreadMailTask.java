package be.stijnhooft.easymail.backend.repository.task;

import android.os.AsyncTask;

import java.util.List;

import be.stijnhooft.easymail.backend.dao.MailDao;
import be.stijnhooft.easymail.backend.model.Mail;

public class FindUnreadMailTask extends AsyncTask<Void, Void, List<Mail>> {

    private MailDao mailDao;

    public FindUnreadMailTask(MailDao dao) {
        mailDao = dao;
    }

    @Override
    protected List<Mail> doInBackground(Void... something) {
        return mailDao.findUnread();
    }
}
