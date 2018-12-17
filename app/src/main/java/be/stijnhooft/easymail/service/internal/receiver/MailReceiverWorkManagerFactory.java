package be.stijnhooft.easymail.service.internal.receiver;

import android.app.Application;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import be.stijnhooft.easymail.repository.SettingRepository;

public class MailReceiverWorkManagerFactory {

    private final SettingRepository settingRepository;

    public MailReceiverWorkManagerFactory(Application application) {
        this(new SettingRepository(application));
    }

    public MailReceiverWorkManagerFactory(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    public void scheduleOneTime() {
        try {
            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();
            Data inputData = assembleInputData();
            OneTimeWorkRequest checkMails = new OneTimeWorkRequest.Builder(
                    MailReceiver.class)
                    .setConstraints(constraints)
                    .setInputData(inputData)
                    .build();
            WorkManager.getInstance()
                    .enqueue(checkMails);
        } catch (JSONException e) {
            throw new MailReceiverException("Could not use settings", e);
        }
    }

    public void scheduleRecurring() {
        try {
            Data inputData = assembleInputData();
            PeriodicWorkRequest checkMails = new PeriodicWorkRequest.Builder(
                    MailReceiver.class, 15, TimeUnit.MINUTES).addTag(MailReceiver.TAG)
                    .setInputData(inputData)
                    .build();
            WorkManager.getInstance()
                    .enqueueUniquePeriodicWork(MailReceiver.TAG, ExistingPeriodicWorkPolicy.KEEP, checkMails);

        } catch (JSONException e) {
            throw new MailReceiverException("Could not use settings", e);
        }
    }

    @NonNull
    private Data assembleInputData() throws JSONException {
        JSONObject settings = settingRepository.getSettings();
        return new Data.Builder()
                .putString("emailAddress", settings.getString("emailAddress"))
                .putString("host", settings.getJSONObject("receiver").getString("password"))
                .putString("password", settings.getJSONObject("receiver").getString("password"))
                .putString("protocol", settings.getJSONObject("receiver").getString("protocol"))
                .putInt("port", settings.getJSONObject("receiver").getInt("port"))
                .build();
    }
}