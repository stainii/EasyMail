package be.stijnhooft.easymail.backend.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;
import java.util.concurrent.ExecutionException;

import be.stijnhooft.easymail.backend.repository.MailRepository;

public class MailViewModel extends AndroidViewModel {

    private transient MailRepository mailRepository;

    public MailViewModel(@NonNull Application application) {
        super(application);
        mailRepository = new MailRepository(application);
    }

    public LiveData<List<Mail>> getMailFor(Person person) {
        try {
            return mailRepository.findFor(person).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Exception while fetching mail", e);
        }
    }


    public boolean hasEverythingBeenRead() {
        try {
            return mailRepository.findUnread().get().isEmpty();
        } catch (ExecutionException | InterruptedException e) {
            Log.e("MailRepository", "Cannot query for unread mail", e);
            return true;
        }
    }

    public boolean isThereAnyUnreadMail() {
        return !hasEverythingBeenRead();
    }

    public void markAsRead(List<Mail> mails) {
        try {
            mailRepository.markAsRead(mails).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Exception while marking messages as read", e);
        }
    }

}
