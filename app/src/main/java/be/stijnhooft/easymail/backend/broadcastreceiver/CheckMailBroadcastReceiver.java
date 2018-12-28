package be.stijnhooft.easymail.backend.broadcastreceiver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import be.stijnhooft.easymail.backend.service.CheckMailService;

// https://fabcirablog.weebly.com/blog/creating-a-never-ending-background-service-in-android
public class CheckMailBroadcastReceiver extends android.content.BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(CheckMailBroadcastReceiver.class.getSimpleName(), "Making sure that the CheckMailService does not stop");
        Intent service = new Intent(context, CheckMailService.class);
        context.startService(service);
    }
}
