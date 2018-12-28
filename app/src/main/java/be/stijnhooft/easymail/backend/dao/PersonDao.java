package be.stijnhooft.easymail.backend.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import be.stijnhooft.easymail.backend.model.Person;

@Dao
public interface PersonDao {

    @Insert
    void save(Person person);

    @Insert
    void save(List<Person> persons);

    @Query("DELETE FROM Person")
    void deleteAll();

    @Update
    void update(Person person);

    @Query("SELECT * FROM Person WHERE email = :email")
    LiveData<Person> findByEmail(String email);

    @Query("SELECT * FROM Person")
    LiveData<List<Person>> findAll();

}
