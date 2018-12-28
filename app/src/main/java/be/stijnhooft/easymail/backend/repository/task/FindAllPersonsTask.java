package be.stijnhooft.easymail.backend.repository.task;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import be.stijnhooft.easymail.backend.dao.PersonDao;
import be.stijnhooft.easymail.backend.model.Person;

public class FindAllPersonsTask extends AsyncTask<Void, Void, LiveData<List<Person>>> {

    private PersonDao personDao;

    public FindAllPersonsTask(PersonDao dao) {
        personDao = dao;
    }

    @Override
    protected LiveData<List<Person>> doInBackground(Void... something) {
        return personDao.findAll();
    }
}
