package be.stijnhooft.easymail.backend.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import be.stijnhooft.easymail.backend.dao.MailDao;
import be.stijnhooft.easymail.backend.db.EasyMailDatabase;
import be.stijnhooft.easymail.backend.model.Mail;
import be.stijnhooft.easymail.backend.model.Person;
import be.stijnhooft.easymail.backend.repository.task.FindMailByEmailTask;
import be.stijnhooft.easymail.backend.repository.task.FindUnreadMailTask;
import be.stijnhooft.easymail.backend.repository.task.MarkMailsAsReadTask;
import be.stijnhooft.easymail.backend.repository.task.SaveMailTask;

public class MailRepository {

    private final MailDao mailDao;

    public MailRepository(Application application) {
        EasyMailDatabase db = EasyMailDatabase.getDatabase(application);
        mailDao = db.mailDao();
    }

    public AsyncTask<String, Void, LiveData<List<Mail>>> findFor(Person person) {
        return new FindMailByEmailTask(mailDao).execute(person.getEmail());
    }

    public AsyncTask<Mail, Void, Void> markAsRead(List<Mail> mails) {
        return new MarkMailsAsReadTask(mailDao).execute(mails.toArray(new Mail[]{}));
    }

    public void save(Mail mail) {
        new SaveMailTask(mailDao).execute(mail);
    }

    public AsyncTask<Void, Void, List<Mail>> findUnread() {
        return new FindUnreadMailTask(mailDao).execute();
    }
}
