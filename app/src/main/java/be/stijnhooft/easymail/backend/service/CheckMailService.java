package be.stijnhooft.easymail.backend.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import be.stijnhooft.easymail.EasyMailApplication;
import be.stijnhooft.easymail.backend.service.internal.receiver.MailReceiverWorkManagerFactory;

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
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

