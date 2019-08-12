package be.stijnhooft.easymail.backend.service.internal.receiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;

import be.stijnhooft.easymail.EasyMailApplication;
import be.stijnhooft.easymail.backend.model.Mail;
import be.stijnhooft.easymail.backend.model.Person;
import be.stijnhooft.easymail.backend.repository.MailRepository;
import be.stijnhooft.easymail.backend.repository.PersonRepository;
import be.stijnhooft.easymail.constants.Notifications;
import be.stijnhooft.easymail.frontend.activity.MainActivity;

public class MailReceiver extends Worker implements LifecycleOwner {

    public static final String TAG = "MailReceiver";
    private final MailMapper mailMapper;
    private final MailRepository mailRepository;
    private final PersonRepository personRepository;
    private final LifecycleRegistry lifecycleRegistry;


    public MailReceiver(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        this.mailMapper = new MailMapper(EasyMailApplication.getInstance());
        this.mailRepository = new MailRepository(EasyMailApplication.getInstance());
        this.personRepository = new PersonRepository(EasyMailApplication.getInstance());
        this.lifecycleRegistry = new LifecycleRegistry(this);
        lifecycleRegistry.markState(Lifecycle.State.STARTED);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i(MailReceiver.class.getSimpleName(), "Checking for new messages");
        checkForNewMessages(mails -> {
            try {
                for (Mail mail : mails) {
                    final LiveData<Person> findByEmail = personRepository.findByEmail(mail.getEmail()).get();
                    findByEmail.observe(this, sender -> {
                        if (sender != null) {
                            insertIntoDatabase(mail);
                            showNotification(mail, sender);
                            markThatPersonHasUnreadMessages(sender);
                            findByEmail.removeObservers(this);
                        }
                    });
                }
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("Cannot check for new mails.", e);
            }
        });
        return Result.success();
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return lifecycleRegistry;
    }

    @Override
    public void onStopped() {
        lifecycleRegistry.markState(Lifecycle.State.DESTROYED);
    }

    private void checkForNewMessages(OnReceiveMail callback) {
        Properties properties = new Properties();
        properties.put("mail.store.protocol", getInputData().getString("protocol"));
        properties.put("mail.imaps.host", getInputData().getString("host"));
        properties.put("mail.imaps.port", getInputData().getInt("port", 0));

        Session session = Session.getDefaultInstance(properties, null);
        try (Store store = session.getStore(getInputData().getString("protocol"))) {
            store.connect(getInputData().getString("emailAddress"), getInputData().getString("password"));

            try (Folder inbox = store.getFolder("inbox")) {
                inbox.open(Folder.READ_WRITE);
                Message[] messages = fetchUnseenMessages(inbox);
                notify(messages, callback);
                markMessagesAsRead(inbox, messages);
            }
        } catch (MessagingException e) {
            Log.d("CheckNewMail", "Could not setup connection", e);
        }
    }

    private Message[] fetchUnseenMessages(Folder inbox) throws MessagingException {
        return inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
    }

    private void notify(Message[] messages, OnReceiveMail callback) {
        List<Mail> mails = new ArrayList<>();
        for (Message message : messages) {
            Mail mail = mailMapper.mapIncomingMail(message);
            if (mail != null) {
                mails.add(mail);
            }
        }
        if (!mails.isEmpty()) {
            callback.onReceiveMail(mails);
        }
    }

    private void markMessagesAsRead(Folder inbox, Message[] messages) {
        for (Message message : messages) {
            try {
                inbox.setFlags(new Message[]{message}, new Flags(Flags.Flag.SEEN), true);
            } catch (MessagingException e) {
                Log.e(this.getClass().getSimpleName(), "An error occurred when marking mails as read", e);
            }
        }
    }

    private void markThatPersonHasUnreadMessages(Person sender) {
        sender.setNewMessages(true);
        personRepository.update(sender);
    }

    private void insertIntoDatabase(Mail mail) {
        mailRepository.save(mail);
    }

    private void showNotification(Mail mail, Person sender) {
        final EasyMailApplication context = EasyMailApplication.getInstance();
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Notifications.CHANNEL_ID)
                .setSmallIcon(sender.getImage())
                .setContentTitle(sender.getName())
                .setContentText(mail.getMessage())
                .setSound(alarmSound)
                .setAutoCancel(true);

        Intent targetIntent = new Intent(context, MainActivity.class);
        targetIntent.putExtra(MainActivity.PERSON_TO_SELECT, sender.getEmail());
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.createNotificationChannel(new NotificationChannel(
                Notifications.CHANNEL_ID, Notifications.CHANNEL_NAME, Notifications.CHANNEL_IMPORTANCE));
        nManager.notify(new Random().nextInt(Integer.MAX_VALUE), builder.build());
    }
}
