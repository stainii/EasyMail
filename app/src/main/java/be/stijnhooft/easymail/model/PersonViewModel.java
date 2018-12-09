package be.stijnhooft.easymail.model;

import android.app.Application;
import android.content.Context;

import java.io.Serializable;
import java.util.List;

import be.stijnhooft.easymail.repository.PersonRepository;

public class PersonViewModel implements Serializable {

    private final transient Context context;
    private final transient PersonRepository personRepository;
    private Person selectedPerson;

    public PersonViewModel(Application application) {
        this.context = application;
        this.personRepository = new PersonRepository(application);
    }
    public Person getSelectedPerson() {
        return selectedPerson;
    }

    public void setSelectedPerson(Person selectedPerson) {
        this.selectedPerson = selectedPerson;
    }

    public List<Person> getPersons() {
        return personRepository.findAll();
    }

}
