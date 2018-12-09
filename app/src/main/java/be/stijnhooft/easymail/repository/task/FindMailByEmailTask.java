package be.stijnhooft.easymail.repository.task;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import be.stijnhooft.easymail.dao.MailDao;
import be.stijnhooft.easymail.model.Mail;

public class FindMailByEmailTask extends AsyncTask<String, Void, LiveData<List<Mail>>> {

    private MailDao mailDao;

    public FindMailByEmailTask(MailDao dao) {
        mailDao = dao;
    }

    @Override
    protected LiveData<List<Mail>> doInBackground(String... strings) {
        return mailDao.findBySender(strings[0]);
    }
}
