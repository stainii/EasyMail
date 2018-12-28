package be.stijnhooft.easymail.backend.repository.task;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import be.stijnhooft.easymail.backend.dao.PersonDao;
import be.stijnhooft.easymail.backend.model.Person;

public class FindPersonByEmailTask extends AsyncTask<String, Void, LiveData<Person>> {

    private PersonDao personDao;

    public FindPersonByEmailTask(PersonDao dao) {
        personDao = dao;
    }

    @Override
    protected LiveData<Person> doInBackground(String... strings) {
        return personDao.findByEmail(strings[0]);
    }
}
