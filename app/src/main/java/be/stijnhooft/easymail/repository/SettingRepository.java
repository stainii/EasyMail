package be.stijnhooft.easymail.repository;

import android.app.Application;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class SettingRepository {

    private final Application context;

    public SettingRepository(Application application) {
        this.context = application;
    }

    public JSONObject getSettings() {
        try(InputStream is = context.getAssets().open("settings.json")) {
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            return new JSONObject(new String(buffer, "UTF-8"));
        } catch (IOException | JSONException ex) {
            throw new RuntimeException("Cannot load settings", ex);
        }
    }

}
