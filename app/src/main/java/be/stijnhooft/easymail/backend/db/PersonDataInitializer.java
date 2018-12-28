package be.stijnhooft.easymail.backend.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import be.stijnhooft.easymail.EasyMailApplication;
import be.stijnhooft.easymail.backend.dao.PersonDao;
import be.stijnhooft.easymail.backend.model.Person;
import be.stijnhooft.easymail.backend.repository.SettingRepository;

class PersonDataInitializer {

    private static final SettingRepository settingRepository = new SettingRepository(EasyMailApplication.getInstance());

    @NonNull
    static RoomDatabase.Callback initPersonData(Context context) {
        return new RoomDatabase.Callback() {
            @Override
            public void onOpen(@NonNull SupportSQLiteDatabase db) {
                Executors.newSingleThreadScheduledExecutor().execute(() -> {
                    final PersonDao personDao = EasyMailDatabase.getDatabase(context).personDao();
                    personDao.deleteAll();
                    personDao.save(loadPersonsFromSettings());
                });
            }
        };
    }

    private static List<Person> loadPersonsFromSettings() {
        final EasyMailApplication context = EasyMailApplication.getInstance();

        List<Person> persons = new ArrayList<>();
        JSONObject settings = settingRepository.getSettings();
        try {
            JSONArray contacts = settings.getJSONArray("contacts");
            for (int i = 0; i < contacts.length(); i++) {
                JSONObject contact = contacts.getJSONObject(i);
                String name = contact.getString("name");
                String email = contact.getString("email");
                String image = contact.getString("image");
                int resource = context.getResources().getIdentifier(image, "drawable", context.getPackageName());
                persons.add(new Person(name, email, resource));
            }
        } catch (JSONException e) {
            throw new RuntimeException("Could not use settings", e);
        }
        return persons;
    }
}
