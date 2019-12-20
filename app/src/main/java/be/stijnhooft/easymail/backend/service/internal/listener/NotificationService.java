package be.stijnhooft.easymail.backend.service.internal.listener;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import org.json.JSONException;

import java.util.Random;

import be.stijnhooft.easymail.EasyMailApplication;
import be.stijnhooft.easymail.backend.model.Mail;
import be.stijnhooft.easymail.backend.model.Person;
import be.stijnhooft.easymail.backend.repository.SettingRepository;
import be.stijnhooft.easymail.constants.Notifications;
import be.stijnhooft.easymail.frontend.activity.MainActivity;

public class NotificationService implements MailListener {

    private boolean soundEnabled;

    public NotificationService() {
        this(new SettingRepository(EasyMailApplication.getInstance()));
    }

    public NotificationService(SettingRepository settingRepository) {
        try {
            this.soundEnabled = settingRepository.getSettings()
                    .getJSONObject("notifications")
                    .getJSONObject("sound")
                    .getBoolean("enabled");
        } catch (JSONException e) {
            throw new RuntimeException("Could not read notification settings.", e);
        }
    }

    @Override
    public void onNewMail(Mail mail, Person sender) {
        final EasyMailApplication context = EasyMailApplication.getInstance();
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Notifications.CHANNEL_ID)
                .setSmallIcon(sender.getImage())
                .setContentTitle(sender.getName())
                .setContentText(mail.getMessage())
                .setAutoCancel(true);

        if (soundEnabled) {
            builder = builder.setSound(alarmSound);
        }

        Intent targetIntent = new Intent(context, MainActivity.class);
        targetIntent.putExtra(MainActivity.PERSON_TO_SELECT, sender.getEmail());

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder = builder.setContentIntent(contentIntent);

        NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.createNotificationChannel(new NotificationChannel(
                Notifications.CHANNEL_ID, Notifications.CHANNEL_NAME, Notifications.CHANNEL_IMPORTANCE));
        nManager.notify(new Random().nextInt(Integer.MAX_VALUE), builder.build());
    }
}
