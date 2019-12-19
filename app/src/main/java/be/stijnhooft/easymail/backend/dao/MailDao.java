package be.stijnhooft.easymail.backend.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import be.stijnhooft.easymail.backend.model.Mail;

@Dao
public interface MailDao {

    @Insert
    void save(Mail mail);

    @Update
    void update(List<Mail> mail);

    @Query("SELECT * FROM Mail WHERE email = :email")
    LiveData<List<Mail>> findBySender(String email);

    @Query("SELECT * FROM Mail WHERE read = 0")
    List<Mail> findUnread();
}
