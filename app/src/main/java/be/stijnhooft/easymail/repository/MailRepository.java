package be.stijnhooft.easymail.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import be.stijnhooft.easymail.dao.MailDao;
import be.stijnhooft.easymail.dao.MailDatabase;
import be.stijnhooft.easymail.model.Mail;
import be.stijnhooft.easymail.model.Person;
import be.stijnhooft.easymail.repository.task.FindMailByEmailTask;
import be.stijnhooft.easymail.repository.task.SaveMailTask;

public class MailRepository {

    private MailDao mailDao;

    public MailRepository(Application application) {
        MailDatabase db = MailDatabase.getDatabase(application);
        mailDao = db.mailDao();
    }

    public AsyncTask<String, Void, LiveData<List<Mail>>> findFor(Person person) {
        return new FindMailByEmailTask(mailDao).execute(person.getEmail());
    }

    public void save(Mail mail) {
        new SaveMailTask(mailDao).execute(mail);
    }
}
