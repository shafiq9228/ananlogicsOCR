package com.analogics.ui.menu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.analogics.R;
import com.analogics.aanil.apcpdcl_billing_app.MainSplashScreen;
import com.analogics.appUtils.Config_SharedPreferances;
import com.analogics.utils.CommonFunctions;
import com.analogics.utils.GPSCoordinates;
import com.analogics.utils.GetIMEI_Number;
import com.analogics.webservice.APIClient;
import com.analogics.webservice.APIInterface;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;


import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class RegistrationActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 104;
    private Button btnRegistration;
    private TextInputEditText etPhoneNumber;
    private TextInputLayout tnlPhoneNumber;
    private TextView tvDeviceId;
    private String deviceId;
    private APIInterface apiInterface;
    private String phoneNumber;
    private Config_SharedPreferances configSharedPreferances;
    private GPSCoordinates gpsCoordinates;
    int num = 0;

    String[] permissionsRequired = new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.RECEIVE_BOOT_COMPLETED, Manifest.permission.CHANGE_NETWORK_STATE, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN};


    private BluetoothAdapter bluetoothAdapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        try {
            if (Build.VERSION.SDK_INT >= 30) {
                if (!Environment.isExternalStorageManager()) {
                    Intent getpermission = new Intent();
                    getpermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    startActivity(getpermission);
                }
            }
        } catch (Exception ex) {
            Log.e("TAG", "onCreate: " + ex);
        }


        if (CommonFunctions.isMachineOrMobileDevice().equals("mobile")) {
            initBluetoothActions();
            checkBluetoothState();
            //  init();
        }

        checkAllPermission();
        init();
//        if (checkAllPermission()) {
//             init();
//        }
    }

    private void checkBluetoothState() {
        if (bluetoothAdapter == null) {
            showToastMsg("Bluetooth NOT support");
        } else {
            if (bluetoothAdapter.isEnabled()) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                if (bluetoothAdapter.isDiscovering()) {
                    showToastMsg("Bluetooth is currently in device discovery process.");
                }
            } else {
                showToastMsg("Bluetooth is NOT Enabled!");
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    private void initBluetoothActions() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available.", Toast.LENGTH_LONG).show();
            return;
        }
        if (!bluetoothAdapter.isEnabled()) {
            Toast.makeText(this, "Please enable your BT and re-run this program.", Toast.LENGTH_LONG).show();
            return;
        }
    }

    private boolean checkAllPermission() {

        if (ActivityCompat.checkSelfPermission(this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED ||
//                ActivityCompat.checkSelfPermission(this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED || // was denied
//                ActivityCompat.checkSelfPermission(this, permissionsRequired[3]) != PackageManager.PERMISSION_GRANTED || // was denied
                ActivityCompat.checkSelfPermission(this, permissionsRequired[4]) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, permissionsRequired[5]) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, permissionsRequired[6]) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, permissionsRequired[7]) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, permissionsRequired[8]) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, permissionsRequired[9]) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, permissionsRequired[10]) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, permissionsRequired[11]) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, permissionsRequired[12]) != PackageManager.PERMISSION_GRANTED) {
            //     Toast.makeText(getApplicationContext(), "Im in if", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, permissionsRequired, 1);
            return false;
        } else {
            //      Toast.makeText(getApplicationContext(), "Im in else", Toast.LENGTH_SHORT).show();
            return true;
        }

    }

    private void init() {
        initElements();
        initActions();
        initListeners();

        getLocationsCoordinates();


    }

    private void getLocationsCoordinates() {


    }

    private void initActions() {
        setDeviceId();
//        try {
//            fetchMobileNumber();
//        } catch (Exception ex) {
//            Log.e("TAG", "initActions: " + ex);
//        }

    }

    private void initElements() {
        tvDeviceId = findViewById(R.id.tv_device_id);
        etPhoneNumber = findViewById(R.id.et_phone_number);
        tnlPhoneNumber = findViewById(R.id.tnl_phone_number);
        btnRegistration = findViewById(R.id.btn_registration);
        configSharedPreferances = new Config_SharedPreferances();
        gpsCoordinates = new GPSCoordinates(getApplicationContext());

//        Executors.callable(() -> {
//           // gpsCoordinates = new GPSCoordinates(getApplicationContext());
//        });
//        new Thread(() -> {
//            //run code on background thread
//            gpsCoordinates = new GPSCoordinates(getApplicationContext());
//            runOnUiThread(() -> {
//                Toast.makeText(getApplicationContext(), "Loc: " + gpsCoordinates.getLatitude() + " " + gpsCoordinates.getLongitude(), Toast.LENGTH_SHORT).show();
//            });
//        }).start();


    }

    private void initListeners() {
        // Toast.makeText(getApplicationContext(), "button is added", Toast.LENGTH_SHORT).show();
        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_SHORT).show();
                    tnlPhoneNumber.setErrorEnabled(false);
                    tnlPhoneNumber.setError(null);
                    String getPhoneNumber = etPhoneNumber.getText().toString().trim();
                    if (getPhoneNumber.length() == 10) {
                        registrationOnServer(getPhoneNumber);
                    } else {
                        tnlPhoneNumber.setErrorEnabled(true);
                        tnlPhoneNumber.setError("Check Phone Number");
                        Toast.makeText(RegistrationActivity.this, "Check Phone Number", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    tnlPhoneNumber.setErrorEnabled(true);
                    tnlPhoneNumber.setError("Check Phone Number");
                    Toast.makeText(RegistrationActivity.this, "Check Phone Number", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }


    @SuppressLint("SetTextI18n")
    private void setDeviceId() {
        if (CommonFunctions.isMachineOrMobileDevice().equals("tainyu")) {
            deviceId = (GetIMEI_Number.getUniqueIMEIId(RegistrationActivity.this)).toUpperCase();
        } else {
            deviceId = "5FC9024647003238";
        }
        tvDeviceId.setText("Device Id: " + deviceId);
    }

    private void fetchMobileNumber() throws Exception {

        HintRequest hintRequest = new HintRequest.Builder().setPhoneNumberIdentifierSupported(true).build();

        PendingIntent intent = Credentials.getClient(RegistrationActivity.this).getHintPickerIntent(hintRequest);
        try {
            startIntentSenderForResult(intent.getIntentSender(), 1008, null, 0, 0, 0, null);
        } catch (IntentSender.SendIntentException e) {
            throw e;
//            Log.e("", "Could not start hint picker Intent", e);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1008:
                if (resultCode == RESULT_OK) {
                    try {
                        Credential cred = data.getParcelableExtra(Credential.EXTRA_KEY);
                        phoneNumber = (cred.getId()).substring(3);
                        etPhoneNumber.setText(phoneNumber);
                        etPhoneNumber.setSelection(phoneNumber.length());
                    } catch (Exception ex) {
                        etPhoneNumber.setText("");
                    }

                } else {
                    etPhoneNumber.setText("");
                    return;
                }
            case REQUEST_ENABLE_BT:
                // todo
                break;
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        num = num + 1;
        Log.d("ocrApp", num + " - "+ grantResults.length);
        // Toast.makeText(getApplicationContext(), "number "+num, Toast.LENGTH_SHORT).show();
        if (requestCode == 1 && grantResults.length > 1) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    if (i == grantResults.length - 1) {
                        // init();
                    }
                } else {
                    // ActivityCompat.requestPermissions(this, permissionsRequired, 1);
                    break;
                }
            }


        } else {
           // ActivityCompat.requestPermissions(this, permissionsRequired, 1);
        }
    }

    public void registrationOnServer(String phoneNumber) {


        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(RegistrationActivity.this);
        progressDialog.setTitle("User Authentication is in progress");
        progressDialog.setMessage("Please wait....");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();


        try {
            String latitude = String.valueOf(gpsCoordinates.getLatitude());
            String longitude = String.valueOf(gpsCoordinates.getLongitude());


//            String url = "http://172.16.0.207:9094/TsspdclAuthWs/analogics/authWS/register/";
            String url = "http://14.99.140.194:9094/TsspdclAuthWs/analogics/authWS/register/";
            JSONObject requestJson = new JSONObject();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("longitude", longitude);
            jsonObject.put("latitude", latitude);
            jsonObject.put("deviceId", deviceId);
            jsonObject.put("mobileNo", phoneNumber);
            requestJson.put("registerData", jsonObject);

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, requestJson, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        if (jsonObject != null) {
                            String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("reason");
                            progressDialog.dismiss();
                            if (status.equalsIgnoreCase("ACK")) {
                                setIntoSharedPreferences("valid", phoneNumber, deviceId, longitude, latitude);
                                showToastMsg(msg);
                                relaunchApplication();
                            } else {
                                showToastMsg(msg);
                            }
                        }
                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(), "Err " + ex, Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    progressDialog.dismiss();
                    setIntoSharedPreferences("", "", "", "", "");

                }
            });
            jsonRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonRequest);


        } catch (Exception e) {
            progressDialog.dismiss();
            setIntoSharedPreferences("", "", "", "", "");
            e.printStackTrace();
        }
    }

    private void relaunchApplication() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(RegistrationActivity.this, MainSplashScreen.class);
                startActivity(intent);
                finishAffinity();
            }
        }, 1000);

    }

    private void setIntoSharedPreferences(String isValid, String phoneNumber, String deviceId, String longitude, String latitude) {
        configSharedPreferances.setUserCredentials(RegistrationActivity.this, isValid, Config_SharedPreferances.isValidUser);
        configSharedPreferances.setUserCredentials(RegistrationActivity.this, phoneNumber, Config_SharedPreferances.phoneNumber);
        configSharedPreferances.setUserCredentials(RegistrationActivity.this, deviceId, Config_SharedPreferances.deviceId);
        configSharedPreferances.setUserCredentials(RegistrationActivity.this, longitude, Config_SharedPreferances.latitude);
        configSharedPreferances.setUserCredentials(RegistrationActivity.this, latitude, Config_SharedPreferances.longitude);
    }

    private void showToastMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

//    @Override
//    public void onBackPressed() {
//        //  super.onBackPressed();
//    }
}