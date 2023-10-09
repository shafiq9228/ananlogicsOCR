package com.analogics.aanil.apcpdcl_billing_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.analogics.R;
import com.analogics.appUtils.Config_SharedPreferances;
import com.analogics.ui.billing.Billing_Sequence_Activity;
import com.analogics.ui.menu.MainMenuActivity;
import com.analogics.ui.menu.RegistrationActivity;
import com.analogics.utils.CommonFunctions;
import com.analogics.utils.GetIMEI_Number;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.whty.smartpos.tysmartposapi.modules.printer.PrinterConstant;

import org.json.JSONObject;


public class MainSplashScreen extends Activity {

    private Config_SharedPreferances configSharedPreferances;
    private TextView tvDeviceId, tvMobNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_splash_screen);
        configSharedPreferances = new Config_SharedPreferances();

        tvDeviceId = findViewById(R.id.tv_device_id);
        tvMobNumber = findViewById(R.id.tv_mobile_num);


        setDeviceId();
        checkUserValidation();
    }

    private void setDeviceId() {
        try {
            String machineId = GetIMEI_Number.getUniqueIMEIId(MainSplashScreen.this);
            tvDeviceId.setText(machineId.toUpperCase());
            String phoneNumber = configSharedPreferances.getUserCredentials(this, Config_SharedPreferances.phoneNumber);
            tvMobNumber.setText("Mob:" + phoneNumber);
        } catch (Exception ex) {

        }
    }

    private void checkUserValidation() {

        try {
            String deviceId = configSharedPreferances.getUserCredentials(this, Config_SharedPreferances.deviceId);
            String phoneNumber = configSharedPreferances.getUserCredentials(this, Config_SharedPreferances.phoneNumber);
            String longitude = configSharedPreferances.getUserCredentials(this, Config_SharedPreferances.longitude);
            String latitude = configSharedPreferances.getUserCredentials(this, Config_SharedPreferances.latitude);


            if (deviceId.isEmpty() || phoneNumber.isEmpty() || longitude.isEmpty() || latitude.isEmpty()) {
                navigateToOtherActivity(RegistrationActivity.class);
            }

//          String url = "http://172.16.0.207:9094/TsspdclAuthWs/analogics/authWS/validate";
            String url = "http://14.99.140.194:9094/TsspdclAuthWs/analogics/authWS/validate";
            JSONObject requestJson = new JSONObject();
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("longitude", longitude);
            jsonObject.put("latitude", latitude);
            jsonObject.put("deviceId", deviceId);
            jsonObject.put("mobileNo", phoneNumber);
            requestJson.put("validateData", jsonObject);

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, requestJson, jsonObject1 -> {

                try {
                    if (jsonObject1 != null && jsonObject1.has("status")) {
                        String status = jsonObject1.getString("status");
                        String msg = jsonObject1.getString("reason");
                        if (status.equalsIgnoreCase("ACK")) {
                            navigateToOtherActivity(MainMenuActivity.class);
                        } else {
                            Toast.makeText(MainSplashScreen.this, msg, Toast.LENGTH_LONG).show();
                        }
                    }

                } catch (Exception ex) {
                    Toast.makeText(MainSplashScreen.this, ex.toString(), Toast.LENGTH_LONG).show();
                }


            }, volleyError -> Toast.makeText(MainSplashScreen.this, "Please Check Your Internet Connection.", Toast.LENGTH_SHORT).show());
            jsonRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void navigateToOtherActivity(Class<?> navigatedClass) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getBaseContext(), navigatedClass);
                startActivity(i);
                finish();
            }
        }, 1500);
    }


    @Override
    protected void onResume() {
        super.onResume();
       // checkUserValidation();

    }

    @Override
    protected void onPause() {

        super.onPause();

    }
}
