package be.stijnhooft.easymail.repository;

import android.app.Application;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import be.stijnhooft.easymail.model.Person;

public class PersonRepository {

    private final Context context;
    private List<Person> persons;
    private final SettingRepository settingRepository;

    public PersonRepository(Application application) {
        this.context = application;
        settingRepository = new SettingRepository(application);
        loadPersonsFromSettings();
    }

    private void loadPersonsFromSettings() {
        persons = new ArrayList<>();
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
    }

    public Person findByEmail(String email) {
        for (Person person : persons) {
            if (person.getEmail().equalsIgnoreCase(email)) {
                return person;
            }
        }
        return null;
    }

    public List<Person> findAll() {
        return persons;
    }
}
