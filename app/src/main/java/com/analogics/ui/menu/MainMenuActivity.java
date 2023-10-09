package com.analogics.ui.menu;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.gsm.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.content.FileProvider;

import com.analogics.BuildConfig;
import com.analogics.DBAdapter.DBAdapter;
import com.analogics.R;
import com.analogics.aanil.apcpdcl_billing_app.OfflineServiceUpload;
import com.analogics.appUtils.Config_SharedPreferances;
import com.analogics.bluetoothprinter.DeviceScanActivity;
import com.analogics.thermalAPI.RP_Printer_2inch_prof_ThermalAPI;
import com.analogics.thermalprinter.AnalogicsThermalPrinter;
import com.analogics.utils.CommonFunctions;
import com.analogics.utils.DownloadData;
import com.analogics.utils.FileOperations;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;



public class MainMenuActivity extends Activity {
    // PrinterLib printerAPI=   new PrinterLib();
    // String portNo= "/dev/ttyHS0";
    DBAdapter dbAdapter;
    Button Btn_Bill;
    Button Btn_Reports;
    Button Btn_Settings;
    Button Btn_Paper_Feed;
    Button Btn_PC_Comm;
    Button Btn_DownloadData;
    Button Btn_PrinterType;
    TextView TV_B_TOTAL;
    TextView TV_PENDING;
    TextView TV_UPLOAD;
    Button Btn_logout;
    EditText ET_AeMobileNo;
    Button Btn_SMS, btnAddBTPrinter, btnDownloadNewApk;
    //Configuration variables
    String eroCodeValue = "";
    String sectionIdValue = "";
    String bluetoothAddressValue = "";
    //Configuration variablessss
    public static ProgressDialog progressDialog;
    //AnalogicsThermalPrinter conn=new AnalogicsThermalPrinter(MainMenuActivity.this);
    private Config_SharedPreferances configSharedPreferances;
    private ProgressDialog pDialog;
    int progressData = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        ui_init();
        setClickListener();
        new FileOperations().checkDirectroies();
        boolean dbStatus = checkDBAvailability();
        //new AlertMessage().alertMessage(MainMenuActivity.this,"DB Info",dbStatus+"");
        new Config_SharedPreferances().check_GetConfigPreferanceValues(MainMenuActivity.this);
        eroCodeValue = new Config_SharedPreferances().getEroCodeValueConfigPreferanceValues(MainMenuActivity.this);

        sectionIdValue = new Config_SharedPreferances().getSectionIdValueConfigPreferanceValues(MainMenuActivity.this);

        String ptrType = new Config_SharedPreferances().getPrinterTypeValues(MainMenuActivity.this);
        Btn_PrinterType.setText("PrinterType:" + ptrType);

        String aePhoneNo = new Config_SharedPreferances().getAeMobileValues(MainMenuActivity.this);
        ET_AeMobileNo.setFocusable(false);
        ET_AeMobileNo.setText(aePhoneNo);

        ET_AeMobileNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 10) {
                    SharedPreferences configSharedpreferences;
                    String config_Preferences = "config_Preferences";
                    configSharedpreferences = MainMenuActivity.this.getSharedPreferences(config_Preferences, MainMenuActivity.this.MODE_PRIVATE);
                    String AeMobileNoKey = "AeMobileNoKey";
                    SharedPreferences.Editor editor = configSharedpreferences.edit();
                    editor.putString(AeMobileNoKey, s.toString());
                    editor.commit();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }
        });

        if (checkService()) {
            Toast.makeText(getBaseContext(), "Service is already running", Toast.LENGTH_SHORT).show();
        } else {
//          Toast.makeText(getBaseContext(), "There is no service running, starting service..", Toast.LENGTH_SHORT).show();
            try {
                Intent service = new Intent(MainMenuActivity.this, OfflineServiceUpload.class);
                this.startService(service);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean checkDBAvailability() {
        boolean dbExist = false;
        dbAdapter = DBAdapter.getDBAdapterInstance(this);
        try {
            dbExist = dbAdapter.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dbExist;
    }

    public void ui_init() {
        configSharedPreferances = new Config_SharedPreferances();
        btnDownloadNewApk = findViewById(R.id.btn_downloadNewApk);
        Btn_Bill = findViewById(R.id.Btn_Bill);
        Btn_Reports = findViewById(R.id.Btn_Reports);
        Btn_Settings = findViewById(R.id.Btn_Settings);
        Btn_Paper_Feed = findViewById(R.id.Btn_Paper_Feed);
        Btn_PC_Comm = findViewById(R.id.Btn_PC_Comm);
        Btn_DownloadData = findViewById(R.id.Btn_DownloadData);
        Btn_PrinterType = findViewById(R.id.Btn_PrinterType);
        Btn_logout = findViewById(R.id.Btn_logout);
        TV_B_TOTAL = findViewById(R.id.TV_B_TOTAL);
        TV_PENDING = findViewById(R.id.TV_PENDING);
        TV_UPLOAD = findViewById(R.id.TV_UPLOAD);
        Btn_PC_Comm.setVisibility(View.GONE);
        ET_AeMobileNo = findViewById(R.id.ET_AeMobileNo);
        Btn_SMS = findViewById(R.id.Btn_SMS);
        btnAddBTPrinter = findViewById(R.id.btn_add_bluetooth_printer);
        btnAddBTPrinter.setVisibility(View.GONE);
        if (CommonFunctions.isMachineOrMobileDevice().equals("mobile")) {
            btnAddBTPrinter.setVisibility(View.VISIBLE);
        }
    }

    public void setClickListener() {
        Btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutApplication();
            }
        });

        btnDownloadNewApk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkApkUpdatedVersion();
            }
        });

        Btn_PrinterType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ptrType = new Config_SharedPreferances().getPrinterTypeValues(MainMenuActivity.this);
                SharedPreferences configSharedpreferences;
                String config_Preferences = "config_Preferences";
                configSharedpreferences = getSharedPreferences(config_Preferences, Context.MODE_PRIVATE);
                System.out.println("Btn_PrinterType:" + Btn_PrinterType.getText());
                if (ptrType.equals("2T")) {
                    System.out.println("In Btn_PrinterType:" + Btn_PrinterType.getText());
                    SharedPreferences.Editor editor = configSharedpreferences.edit();
                    editor.putString("PrinterType_Key", "2I");
                    editor.commit();
                    editor.clear();
                    Btn_PrinterType.setText("PrinterType:2I");
                } else {
                    System.out.println("Else Btn_PrinterType:" + Btn_PrinterType.getText());
                    SharedPreferences.Editor editor = configSharedpreferences.edit();
                    editor.putString("PrinterType_Key", "2T");
                    editor.commit();
                    editor.clear();
                    Btn_PrinterType.setText("PrinterType:2T");
                }
            }
        });

        Btn_Bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), BillingMenuActivity.class);
                startActivity(i);
                finish();
            }
        });
        Btn_Reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), ReportsMenuActivity.class);
                startActivity(i);
                finish();
            }
        });
        Btn_PC_Comm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), PC_Comm_MenuActivity.class);
                startActivity(i);
                finish();
            }
        });
        Btn_DownloadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                downloadMasterData();

//                Intent i = new Intent(getBaseContext(), Online_CommunicationMenuActivity.class);
//                startActivity(i);
//                finish();
            }
        });


        Btn_Paper_Feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });


        btnAddBTPrinter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, DeviceScanActivity.class);
                startActivity(intent);
            }
        });

        Btn_SMS.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    final String phoneNo = ET_AeMobileNo.getText().toString();
                    if (phoneNo.length() >= 10) {
                        final String message = "Hi Naresh need help on Faulty Meter";

                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(
                                MainMenuActivity.this);
                        alertDialogBuilder
                                .setMessage("Do you want to send the SMS ?");

                        alertDialogBuilder.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface arg0,
                                                        int arg1) {

                                        sendSMS(phoneNo, message);

                                    }
                                });

                        alertDialogBuilder.setNegativeButton("No",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                    }
                                });

                        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.setCancelable(false);
                        alertDialog.show();

                    } else {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(
                                MainMenuActivity.this);
                        alertDialogBuilder
                                .setMessage("Please enter phone number .");

                        alertDialogBuilder.setNeutralButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0,
                                                        int arg1) {

                                    }
                                });

                        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.setCancelable(false);
                        alertDialog.show();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void checkApkUpdatedVersion() {
        try {
            String apkUrl = "http://14.99.140.194:9094/TsspdclAuthWs/analogics/versionWS/version";

            pDialog = new ProgressDialog(this);
            pDialog.setMessage("Downloading file. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setMax(100);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setCancelable(false);
            pDialog.show();

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, apkUrl, null, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {

                    try {
                        if (jsonObject != null && jsonObject.has("versionNo")) {
                            String apkVersion = jsonObject.getString("versionNo");
                            float serverApkVersion = Float.parseFloat(apkVersion);
                            float currentAppVersion = BuildConfig.VERSION_CODE;
                            if(currentAppVersion < serverApkVersion){
                                String apkUrl = jsonObject.getString("versionPath");
                                downloadUpdatedApk(apkUrl);
                            }else{
                                pDialog.dismiss();
                                //Toast.makeText(MainMenuActivity.this, "Apk Version Not Updated", Toast.LENGTH_SHORT).show();
                                Toast.makeText(MainMenuActivity.this, "Latest Version Available", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception ex) {
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(MainMenuActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                    20000,
                    2,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonRequest);

        } catch (
                Exception e) {
            e.printStackTrace();
        }

    }

    private void downloadUpdatedApk(String apkUrl) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    int count;
                    URL url = new URL(apkUrl);
                    URLConnection conection = url.openConnection();
                    conection.connect();

                    int lenghtOfFile = conection.getContentLength();

                    InputStream input = new BufferedInputStream(url.openStream(), 8192);
                    File DownDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/");
                    String Path = DownDirectory.getAbsolutePath().toString() + "/";

                    OutputStream output = new FileOutputStream(Path + "analogics_billing.apk");
                    byte data[] = new byte[1024];
                    long total = 0;
                    while ((count = input.read(data)) != -1) {
                        total += count;

                        long finalTotal = total;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressData = (int) ((finalTotal * 100) / lenghtOfFile);
                                pDialog.setProgress(progressData);
                                if (progressData == 100) {
                                    pDialog.dismiss();
                                    installApp();
                                }
                            }
                        });

                        output.write(data, 0, count);
                    }
                    output.flush();
                    output.close();
                    input.close();

                } catch (Exception e) {
//                Log.e("Error: ", e.getMessage());
                }
            }
        }).start();

        Log.e("TAG", "downloadUpdatedApk: " + "finished Background work");

    }

    private void installApp() {
        try {
            File file = new File(Environment.getExternalStorageDirectory() + "/" + "analogics_billing.apk");
            Uri uri = FileProvider.getUriForFile(MainMenuActivity.this, BuildConfig.APPLICATION_ID + ".provider", file);
            Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        } catch (Exception ex) {
            Log.e("LAG", "installApp:Exp:  " + ex.toString());
        }


    }

    private void logoutApplication() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MainMenuActivity.this, R.style.AlertMessageTheme));
        builder.setTitle("Info");
        builder.setMessage("Do you want to Exit .");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//                Intent i = new Intent(getBaseContext(), LoginMaterialDesignActivity.class);
//                startActivity(i);
                finishAffinity();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // If want to block just return false
            logoutApplication();
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            // If want to block just return false
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            // If want to block just return false
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_SEARCH) {
            // If want to block just return false
            return false;
        }
        if (keyCode == KeyEvent.KEYCODE_SETTINGS) {
            // If want to block just return false
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean checkService() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.analogics.aanil.apcpdcl_billing_app.OfflineServiceUpload".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    private void sendSMS(String phoneNumber, String message) {
        try {
            String SENT = "SMS_SENT";
            String DELIVERED = "SMS_DELIVERED";

            PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(
                    SENT), 0);

            PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                    new Intent(DELIVERED), 0);

            // ---when the SMS has been sent---
            registerReceiver(new BroadcastReceiver() {

                @Override
                public void onReceive(Context arg0, Intent arg1) {
                    switch (getResultCode()) {
                        case Activity.RESULT_OK:
                            Toast.makeText(getBaseContext(), "SMS sent",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                            Toast.makeText(getBaseContext(), "Generic failure",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_NO_SERVICE:
                            Toast.makeText(getBaseContext(), "No service",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_NULL_PDU:
                            Toast.makeText(getBaseContext(), "Null PDU",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                            Toast.makeText(getBaseContext(), "Radio off",
                                    Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }, new IntentFilter(SENT));

            // ---when the SMS has been delivered---
            registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context arg0, Intent arg1) {
                    switch (getResultCode()) {
                        case Activity.RESULT_OK:
                            Toast.makeText(getBaseContext(), "SMS delivered",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case Activity.RESULT_CANCELED:
                            Toast.makeText(getBaseContext(), "SMS not delivered",
                                    Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }, new IntentFilter(DELIVERED));

            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void downloadMasterData() {
        progressDialog = new ProgressDialog(MainMenuActivity.this);

        try {

//            String url = "http://172.16.0.207:9094/TsspdclAuthWs/analogics/dataWS/download/";
            String url = "http://14.99.140.194:9094/TsspdclAuthWs/analogics/dataWS/download/";
            progressDialog.setTitle("Downloading master data from the server");
            progressDialog.setMessage("Please wait....");
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            progressDialog.show();

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, prepareJSonStringTomasterDownload(), new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        if (jsonObject != null && jsonObject.has("status")) {
                            String status = jsonObject.getString("status");

                            Log.e("TAG", "onResponse:12222:  " + jsonObject);
                            String msg = jsonObject.getString("response");
                            if (status.equalsIgnoreCase("ACK")) {
                                new DownloadData(MainMenuActivity.this, jsonObject, false).start();
                            } else {
                                Toast.makeText(MainMenuActivity.this, msg, Toast.LENGTH_LONG).show();
                            }
                        }

                    } catch (Exception ex) {
//                        progressDialog.dismiss();
                    }


                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(MainMenuActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
                }
            });
            jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                    20000,
                    3,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonRequest);

        } catch (Exception e) {
//            progressDialog.dismiss();
            e.printStackTrace();
        }
    }


    public JSONObject prepareJSonStringTomasterDownload() {
        JSONObject requestJson = new JSONObject();
        try {
            String deviceId = configSharedPreferances.getUserCredentials(this, Config_SharedPreferances.deviceId);
            String phoneNumber = configSharedPreferances.getUserCredentials(this, Config_SharedPreferances.phoneNumber);
            String longitude = configSharedPreferances.getUserCredentials(this, Config_SharedPreferances.longitude);
            String latitude = configSharedPreferances.getUserCredentials(this, Config_SharedPreferances.latitude);


            JSONObject jsonObject = new JSONObject();

            jsonObject.put("longitude", longitude);
            jsonObject.put("latitude", latitude);
            jsonObject.put("deviceId", deviceId);
            jsonObject.put("mobileNo", phoneNumber);
            requestJson.put("downloadData", jsonObject);


            return requestJson;

        } catch (JSONException ex) {

            ex.printStackTrace();
        }
        return requestJson;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}