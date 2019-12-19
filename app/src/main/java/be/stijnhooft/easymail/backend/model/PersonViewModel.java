package be.stijnhooft.easymail.backend.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutionException;

import be.stijnhooft.easymail.backend.repository.PersonRepository;

public class PersonViewModel extends AndroidViewModel {

    private final transient PersonRepository personRepository;
    private Person selectedPerson;

    public PersonViewModel(@NonNull Application application) {
        super(application);
        this.personRepository = new PersonRepository(application);
    }

    public Person getSelectedPerson() {
        return selectedPerson;
    }

    public void setSelectedPerson(Person selectedPerson) {
        this.selectedPerson = selectedPerson;
    }

    public LiveData<List<Person>> getPersons() {
        try {
            return personRepository.findAll().get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Exception while fetching messages", e);
        }
    }

    public void markMessagesAsRead(Person person) {
        person.setNewMessages(false);
        personRepository.update(person);
    }

}
