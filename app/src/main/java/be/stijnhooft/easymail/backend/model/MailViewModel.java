package be.stijnhooft.easymail.backend.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutionException;

import be.stijnhooft.easymail.backend.repository.MailRepository;

public class MailViewModel extends AndroidViewModel {

    private transient MailRepository mailRepository;

    public MailViewModel(@NonNull Application application) {
        super(application);
        mailRepository = new MailRepository(application);
    }

    public LiveData<List<Mail>> getMessagesFor(Person person) {
        try {
            return mailRepository.findFor(person).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Exception while fetching messages", e);
        }
    }

    public void markAsRead(List<Mail> mails) {
        try {
            mailRepository.markAsRead(mails).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Exception while marking messages as read", e);
        }
    }

}
