package be.stijnhooft.easymail.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import be.stijnhooft.easymail.EasyMailApplication;
import be.stijnhooft.easymail.MainActivity;
import be.stijnhooft.easymail.model.Mail;
import be.stijnhooft.easymail.model.Person;
import be.stijnhooft.easymail.model.Protocol;
import be.stijnhooft.easymail.repository.MailRepository;
import be.stijnhooft.easymail.repository.PersonRepository;
import be.stijnhooft.easymail.repository.SettingRepository;
import be.stijnhooft.easymail.service.subscription.MailSubscriber;
import be.stijnhooft.easymail.service.subscription.MailSubscription;
import be.stijnhooft.easymail.service.subscription.MailSubscriptionException;
import be.stijnhooft.easymail.service.subscription.MailSubscriptionFactory;

public class CheckMailService extends IntentService {

    private final MailRepository mailRepository;
    private PersonRepository personRepository;
    private final SettingRepository settingRepository;

    public CheckMailService() {
        super("CheckMailService");
        EasyMailApplication application = EasyMailApplication.getInstance();
        this.mailRepository = new MailRepository(application);
        this.personRepository = new PersonRepository(application);
        this.settingRepository = new SettingRepository(application);
    }

    public final MailSubscriber CHECK_MAIL_SERVICE = mails -> {
        for (Mail mail : mails) {
            insertIntoDatabase(mail);
            showNotification(mail);
            personRepository.findByEmail(mail.getEmail()).setNewMessages(true);
        }
    };

    private void insertIntoDatabase(Mail mail) {
        mailRepository.save(mail);
    }

    private void showNotification(Mail mail) {
        Person sender = personRepository.findByEmail(mail.getEmail());
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification.Builder builder =
                new Notification.Builder(this)
                        .setSmallIcon(sender.getImage())
                        .setContentTitle(sender.getName())
                        .setContentText(mail.getMessage())
                        .setPriority(Notification.PRIORITY_MAX)
                        .setSound(alarmSound)
                        .setAutoCancel(true);


        Intent targetIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(new Random().nextInt(Integer.MAX_VALUE), builder.build());
    }

    private MailSubscription mailSubscription;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        onHandleIntent(intent);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mailSubscription.subscribe(CHECK_MAIL_SERVICE);
        Toast.makeText(this, "CheckMailService started", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        JSONObject settings = settingRepository.getSettings();
        try {
            this.mailSubscription = MailSubscriptionFactory.configure()
                    .emailAddress(settings.getString("emailAddress"))
                    .host(settings.getJSONObject("subscription").getString("host"))
                    .password(settings.getJSONObject("subscription").getString("password"))
                    .protocol(Protocol.valueOf(settings.getJSONObject("subscription").getString("protocol")))
                    .port(settings.getJSONObject("subscription").getInt("port"))
                    .intervalInMilliseconds(settings.getJSONObject("subscription").getInt("intervalInMilliseconds"))
                    .build();
        } catch (JSONException e) {
            throw new MailSubscriptionException("Could not use settings", e);
        }
        Toast.makeText(this, "CheckMailService created", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        mailSubscription.unsubscribe(CHECK_MAIL_SERVICE);
        super.onDestroy();
    }

}

