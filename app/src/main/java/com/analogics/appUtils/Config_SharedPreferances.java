package com.analogics.appUtils;

import android.content.Context;
import android.content.SharedPreferences;

import com.analogics.utils.CommonFunctions;

import org.apache.commons.lang3.StringUtils;

public class Config_SharedPreferances {
    SharedPreferences configSharedpreferences;
    String config_Preferences = "config_Preferences";
    String eroCodeKey = "eroCodeKey";
    String sectionIdKey = "sectionIdKey";
    String bluetoothAddressKey = "bluetoothAddressKey";
    String bluetoothDeviceNameKey = "bluetoothDeviceNameKey";
    String PrinterType_Key = "PrinterType_Key";
    String AeMobileNoKey = "AeMobileNoKey";
    String eroCodeValue = "";
    String sectionIdValue = "";
    String bluetoothAddressValue = "";
    String bluetoothDeviceNameValue = "";
    String PrinterTypeValue = "2T";
    public static String userCredentials = "credentials";
    public static String  isValidUser =  "isValid";
    public static String phoneNumber =  "phoneNumber";
    public static String deviceId =  "deviceId";
    public static String longitude =  "longitude";
    public static String latitude =  "latitude";




    public void check_GetConfigPreferanceValues(Context mContext) {

        configSharedpreferences = mContext.getSharedPreferences(config_Preferences, mContext.MODE_PRIVATE);

        if (configSharedpreferences.contains(eroCodeKey)) { //How can I ask here?
            eroCodeValue = configSharedpreferences.getString(eroCodeKey, "defaultValue");
        } else {
            //Set the shared preferences
            SharedPreferences.Editor editor = configSharedpreferences.edit();
            editor.putString(eroCodeKey, "");
            editor.commit();
        }
        if (configSharedpreferences.contains(sectionIdKey)) { //How can I ask here?
            sectionIdValue = configSharedpreferences.getString(sectionIdKey, "defaultValue");
        } else {
            //Set the shared preferences
            SharedPreferences.Editor editor = configSharedpreferences.edit();
            editor.putString(sectionIdKey, "");
            editor.commit();
        }
        if (configSharedpreferences.contains(bluetoothAddressKey)) { //How can I ask here?
            bluetoothAddressValue = configSharedpreferences.getString(bluetoothAddressKey, "defaultValue");
        } else {
            //Set the shared preferences
            SharedPreferences.Editor editor = configSharedpreferences.edit();
            editor.putString(bluetoothAddressKey, "");
            editor.commit();
        }

        if (configSharedpreferences.contains(bluetoothDeviceNameKey)) { //How can I ask here?
            bluetoothDeviceNameValue = configSharedpreferences.getString(bluetoothDeviceNameKey, "defaultValue");
        } else {
            //Set the shared preferences
            SharedPreferences.Editor editor = configSharedpreferences.edit();
            editor.putString(bluetoothDeviceNameKey, "");
            editor.commit();
        }


    }

    public String getEroCodeValueConfigPreferanceValues(Context mContext) {

        configSharedpreferences = mContext.getSharedPreferences(config_Preferences, mContext.MODE_PRIVATE);

        if (configSharedpreferences.contains(eroCodeKey)) { //How can I ask here?
            eroCodeValue = configSharedpreferences.getString(eroCodeKey, "defaultValue");
        } else {
            //Set the shared preferences
            SharedPreferences.Editor editor = configSharedpreferences.edit();
            editor.putString(eroCodeKey, "");
            editor.commit();
        }
        return eroCodeValue;
    }

    public String getSectionIdValueConfigPreferanceValues(Context mContext) {

        configSharedpreferences = mContext.getSharedPreferences(config_Preferences, mContext.MODE_PRIVATE);

        if (configSharedpreferences.contains(sectionIdKey)) { //How can I ask here?
            sectionIdValue = configSharedpreferences.getString(sectionIdKey, "defaultValue");
        } else {
            //Set the shared preferences
            SharedPreferences.Editor editor = configSharedpreferences.edit();
            editor.putString(sectionIdKey, "");
            editor.commit();
        }
        return sectionIdValue;
    }

    public String getBtNameAndAddress(Context mContext) {

        configSharedpreferences = mContext.getSharedPreferences(config_Preferences, mContext.MODE_PRIVATE);

        if (configSharedpreferences.contains(bluetoothAddressKey)) { //How can I ask here?
            bluetoothAddressValue = configSharedpreferences.getString(bluetoothAddressKey, "defaultValue");
        } else {
            //Set the shared preferences
            SharedPreferences.Editor editor = configSharedpreferences.edit();
            editor.putString(bluetoothAddressKey, "");
            editor.commit();
        }
        return bluetoothAddressValue;
    }

    public String getBluetoothDeviceNameValueConfigPreferanceValues(Context mContext) {
        configSharedpreferences = mContext.getSharedPreferences(config_Preferences, mContext.MODE_PRIVATE);
        if (configSharedpreferences.contains(bluetoothDeviceNameKey)) { //How can I ask here?
            bluetoothDeviceNameValue = configSharedpreferences.getString(bluetoothDeviceNameKey, "defaultValue");
        } else {
            //Set the shared preferences
            SharedPreferences.Editor editor = configSharedpreferences.edit();
            editor.putString(bluetoothDeviceNameKey, "");
            editor.commit();
        }
        return bluetoothDeviceNameValue;
    }


    public String getPrinterTypeValues(Context mContext) {
        String machineType = CommonFunctions.isMachineOrMobileDevice();
        configSharedpreferences = mContext.getSharedPreferences(config_Preferences, mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = configSharedpreferences.edit();
        if(StringUtils.equalsIgnoreCase(machineType, "tianyu")){
            editor.putString(PrinterType_Key, "tianyu");
            PrinterTypeValue = "tianyu";
        }
        else
            editor.putString(PrinterType_Key, "2T");
        editor.commit();

//        configSharedpreferences = mContext.getSharedPreferences(config_Preferences, mContext.MODE_PRIVATE);
//        if (configSharedpreferences.contains(PrinterType_Key)) { //How can I ask here?
//            PrinterTypeValue = configSharedpreferences.getString(PrinterType_Key, "defaultValue");
//        } else {
//            SharedPreferences.Editor editor = configSharedpreferences.edit();
//            if(StringUtils.equalsIgnoreCase(machineType, "tianyu"))
//                editor.putString(PrinterType_Key, "tianyu");
//            else
//                editor.putString(PrinterType_Key, "2T");
//            editor.commit();
//        }
        return PrinterTypeValue;
    }

    public String getSequenceValues(Context mContext) {

        configSharedpreferences = mContext.getSharedPreferences(config_Preferences, mContext.MODE_PRIVATE);

        if (configSharedpreferences.contains(PrinterType_Key)) { //How can I ask here?
            PrinterTypeValue = configSharedpreferences.getString(PrinterType_Key, "defaultValue");
        } else {
            //Set the shared preferences
            SharedPreferences.Editor editor = configSharedpreferences.edit();
            editor.putString(PrinterType_Key, "2T");
            editor.commit();
        }
        return PrinterTypeValue;
    }

    public String getAeMobileValues(Context mContext) {

        configSharedpreferences = mContext.getSharedPreferences(config_Preferences, mContext.MODE_PRIVATE);

        if (configSharedpreferences.contains(AeMobileNoKey)) { //How can I ask here?
            PrinterTypeValue = configSharedpreferences.getString(AeMobileNoKey, "defaultValue");
        } else {
            //Set the shared preferences
            SharedPreferences.Editor editor = configSharedpreferences.edit();
            editor.putString(AeMobileNoKey, "");
            editor.commit();
        }
        return PrinterTypeValue;
    }


    public void setUserCredentials(Context mContext, String data, String key) {
        configSharedpreferences = mContext.getSharedPreferences(userCredentials, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = configSharedpreferences.edit();
        editor.putString(key, data);
        editor.commit();
        editor.apply();

    }

    public String getUserCredentials(Context mContext, String key) {
        String result = "";
        configSharedpreferences = mContext.getSharedPreferences(userCredentials, Context.MODE_PRIVATE);

        if (configSharedpreferences.contains(key)) {
            result = configSharedpreferences.getString(key,"");
        }
        return result;
    }

}