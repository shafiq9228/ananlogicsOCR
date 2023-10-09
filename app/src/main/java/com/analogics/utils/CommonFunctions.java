package com.analogics.utils;

import android.os.Build;

public class CommonFunctions {

    public static String isMachineOrMobileDevice() {
        if (isAndroidVersion10Machine()) {
            return "android10Machine";
        } else if (isAndroidVersion7Machine()) {
            return "android7Machine";
        } else if (isTianyuDevice()){
            return "tianyu";
        }else
            return "mobile";
    }

    private static boolean isAndroidVersion10Machine() {
        return Build.HOST.equals("android10");
    }

    private static boolean isTianyuDevice() {
        return Build.MANUFACTURER.equals("tianyu");
    }
    private static boolean isAndroidVersion7Machine() {
        String result = Build.HOST;
        return result.equalsIgnoreCase("analogics") || result.equalsIgnoreCase("atilhydpun236");
    }
}
