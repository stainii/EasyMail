package be.stijnhooft.easymail.backend.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import be.stijnhooft.easymail.backend.dao.PersonDao;
import be.stijnhooft.easymail.backend.db.EasyMailDatabase;
import be.stijnhooft.easymail.backend.model.Person;
import be.stijnhooft.easymail.backend.repository.task.FindAllPersonsTask;
import be.stijnhooft.easymail.backend.repository.task.FindPersonByEmailTask;
import be.stijnhooft.easymail.backend.repository.task.UpdatePersonTask;

public class PersonRepository {

    private final PersonDao personDao;

    public PersonRepository(Application application) {
        EasyMailDatabase db = EasyMailDatabase.getDatabase(application);
        personDao = db.personDao();
    }

    public AsyncTask<String, Void, LiveData<Person>> findByEmail(String email) {
        return new FindPersonByEmailTask(personDao).execute(email);
    }

    public AsyncTask<Void, Void, LiveData<List<Person>>> findAll() {
        return new FindAllPersonsTask(personDao).execute();
    }

    public void update(Person person) {
        new UpdatePersonTask(personDao).execute(person);
    }

}
