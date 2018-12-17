package be.stijnhooft.easymail.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import be.stijnhooft.easymail.EasyMailApplication;
import be.stijnhooft.easymail.MainActivity;
import be.stijnhooft.easymail.R;
import be.stijnhooft.easymail.service.internal.receiver.MailReceiverWorkManagerFactory;

public class CheckMailService extends Service {

    private final MailReceiverWorkManagerFactory mailReceiverWorkManagerFactory;

    public CheckMailService() {
        EasyMailApplication application = EasyMailApplication.getInstance();
        this.mailReceiverWorkManagerFactory = new MailReceiverWorkManagerFactory(application);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        mailReceiverWorkManagerFactory.scheduleRecurring();
        showPersistentNotification();
        return START_STICKY;
    }

    private void showPersistentNotification() {
        //TODO: delete this code
        Notification.Builder builder =
                new Notification.Builder(this)
                        .setSmallIcon(R.drawable.ann_thumb)
                        .setContentTitle("De nodige services draaien")
                        .setContentText("Iedere 15 minuten wordt gecontroleerd op een nieuw bericht")
                        .setPriority(Notification.PRIORITY_MAX)
                        .setContentIntent(PendingIntent.getActivity(this, 0,
                            new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT));
        Notification persistentNotification = builder.build();
        startForeground(1, persistentNotification);
    }


    @Override
    public void onDestroy() {
        stopForeground(true);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

