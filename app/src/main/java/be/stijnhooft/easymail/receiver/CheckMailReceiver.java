package be.stijnhooft.easymail.receiver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import be.stijnhooft.easymail.service.CheckMailService;

// https://fabcirablog.weebly.com/blog/creating-a-never-ending-background-service-in-android
public class CheckMailReceiver extends android.content.BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(CheckMailReceiver.class.getSimpleName(), "Making sure that the CheckMailService does not stop");
        context.startService(new Intent(context, CheckMailService.class));
        Toast.makeText(context, "EasyMail terug opgestart in de achtergrond...", Toast.LENGTH_LONG).show();
    }
}
