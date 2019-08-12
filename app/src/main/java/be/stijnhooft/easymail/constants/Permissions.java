package be.stijnhooft.easymail.constants;

import android.Manifest;

public interface Permissions {

    String[] ALL = new String[] { Manifest.permission.INTERNET,
            Manifest.permission.RECEIVE_BOOT_COMPLETED,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.FOREGROUND_SERVICE
    };

    int REQUEST_CODE = 999;

}
