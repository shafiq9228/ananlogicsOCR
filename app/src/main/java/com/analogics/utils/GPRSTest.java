package com.analogics.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

public class GPRSTest {
    private Context context;

    public GPRSTest(Context context2) {
        this.context = context2;
    }

    public boolean gprsTest() {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) this.context.getSystemService("connectivity");
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo state : info) {
                        if (state.getState() == State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
