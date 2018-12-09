package be.stijnhooft.easymail.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import be.stijnhooft.easymail.model.Mail;

@Database(
        entities = {
            Mail.class
        }, version = 1,
        exportSchema = false
)
public abstract class MailDatabase extends RoomDatabase {
    public abstract MailDao mailDao();

    private static volatile MailDatabase INSTANCE;

    public static MailDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MailDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), MailDatabase.class, "mail_db")
                                    .build();
                }
            }
        }
        return INSTANCE;
    }
}
