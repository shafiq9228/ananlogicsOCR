package com.analogics.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import androidx.core.app.ActivityCompat;

public class SimSignal_StausUtil {
    static Context mContext;

    public SimSignal_StausUtil(Context context) {
        mContext = context;
    }


    public boolean gprsStatus() {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {

                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null)
                    for (int i = 0; i < info.length; i++)
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }


    public boolean isSimCardAvailable() {

        TelephonyManager tm = (TelephonyManager)
                mContext.getSystemService(Context.TELEPHONY_SERVICE);
        String IMEI = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            }
            IMEI = tm.getImei();
            if (IMEI != null) {
                return true;
            } else {
                return false;
            }
        }else{
            String number = tm.getSimSerialNumber();
            if (number != null) {
                return true;
            } else {
                return false;
            }


        }


    }





}
