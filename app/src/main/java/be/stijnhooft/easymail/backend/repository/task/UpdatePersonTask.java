package be.stijnhooft.easymail.backend.repository.task;

import android.os.AsyncTask;

import be.stijnhooft.easymail.backend.dao.PersonDao;
import be.stijnhooft.easymail.backend.model.Person;

public class UpdatePersonTask extends AsyncTask<Person, Void, Void> {

    private final PersonDao personDao;

    public UpdatePersonTask(PersonDao personDao)  {
        this.personDao = personDao;
    }

    @Override
    protected Void doInBackground(Person... persons) {
        this.personDao.update(persons[0]);
        return null;
    }
}
