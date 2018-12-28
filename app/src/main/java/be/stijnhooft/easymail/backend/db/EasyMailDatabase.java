package be.stijnhooft.easymail.backend.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import be.stijnhooft.easymail.backend.dao.MailDao;
import be.stijnhooft.easymail.backend.dao.PersonDao;
import be.stijnhooft.easymail.backend.model.Mail;
import be.stijnhooft.easymail.backend.model.Person;

@Database(
        entities = {
            Mail.class,
            Person.class
        }, version = 1,
        exportSchema = false
)
public abstract class EasyMailDatabase extends RoomDatabase {
    public abstract MailDao mailDao();
    public abstract PersonDao personDao();

    private static volatile EasyMailDatabase INSTANCE;

    public static EasyMailDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (EasyMailDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room
                            .databaseBuilder(context.getApplicationContext(), EasyMailDatabase.class, "mail_db")
                            .addCallback(PersonDataInitializer.initPersonData(context))
                            .build();
                }
            }
        }
        return INSTANCE;
    }


}
