package be.stijnhooft.easymail.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import be.stijnhooft.easymail.model.Mail;

@Dao
public interface MailDao {

    @Insert
    void save(Mail mail);

    @Query("SELECT * from Mail WHERE email = :email")
    LiveData<List<Mail>> findBySender(String email);

}
