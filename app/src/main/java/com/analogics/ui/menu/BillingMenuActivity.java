package com.analogics.ui.menu;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.analogics.DBAdapter.ReportsDAO;
import com.analogics.R;
import com.analogics.appUtils.Config_SharedPreferances;
import com.analogics.irda.USB_Activity;
import com.analogics.ocr.OfflineFolderActivity;
import com.analogics.pojo.ConsumerDataVO;
import com.analogics.ui.billing.Billing_SearchBy_Activity;
import com.analogics.ui.billing.Billing_Sequence_Activity;
import com.analogics.ui.billing.DeleteData;
import com.analogics.ui.billing.Search_By_NameActivity;
import com.analogics.utils.CommonFunctions;
import com.analogics.utils.DateUtil;
import com.analogics.utils.DownloadData;
import com.analogics.utils.FileOperations;
import com.analogics.utils.GetIMEI_Number;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;


public class BillingMenuActivity extends AppCompatActivity {
    ConsumerDataVO consumerDataVO = new ConsumerDataVO();
    Button Btn_Search;
    Button Btn_Sequence;
    Button Btn_Search_By_Name;
    Button Btn_IR;
    Button Btn_IRDA;
    Button Btn_home;
    Button Btn_PhotoBilling;
    Button Btn_server_Push;
    private Config_SharedPreferances configSharedPreferances;
    Button Btn_SahasraBillPay, btnSasaBillPrint, btnDeleteBilledData, btnVerifyPass, btnBillBilledData;
    Button btn_append, btn_download;
    public static ProgressDialog progressDialog;
    private TextInputLayout tilPassword, tilDownload;
    private TextInputEditText etPassword, etDownload;
    private WindowManager.LayoutParams layoutParams;
    private Dialog passwordDialog, areaDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_menu);
        ui_init();
        clickListener();
    }

    public void ui_init() {
        Button offlineLayout = findViewById(R.id.offlineBtn);
        offlineLayout.setOnClickListener(view -> {
            startActivity(new Intent(BillingMenuActivity.this, OfflineFolderActivity.class));
        });
        configSharedPreferances = new Config_SharedPreferances();
        Btn_Search = findViewById(R.id.Btn_Search);
        Btn_Sequence = findViewById(R.id.Btn_Sequence);
        Btn_Search_By_Name = findViewById(R.id.Btn_Search_By_Name);
        Btn_PhotoBilling = findViewById(R.id.Btn_PhotoBilling);
        Btn_IR = findViewById(R.id.Btn_IR);
        Btn_IRDA = findViewById(R.id.Btn_IRDA);
        Btn_home = findViewById(R.id.Btn_home);
        btnSasaBillPrint = findViewById(R.id.btnSasaBillPrint);
        btnDeleteBilledData = findViewById(R.id.btn_erase_billed_data);
        btnBillBilledData = findViewById(R.id.btn_append_billed_data);
        Btn_server_Push = findViewById(R.id.btn_serverPush);
        passwordDialog = new Dialog(this);
        btn_append = findViewById(R.id.btn_append);
        btn_download = findViewById(R.id.btn_download);
        areaDialog = new Dialog(this);
    }

    public void clickListener() {

        btnSasaBillPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BillingMenuActivity.this, SasaBillPrintActivity.class);
                startActivity(intent);
            }
        });

        Btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToBackActivity();
            }
        });
        //photobilling
//        Btn_PhotoBilling.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getBaseContext(), ScanAutoAnalogDigitalMeterActivity.class);
//                intent.putExtra("ConsumerDataVO", (Serializable) consumerDataVO);
//                intent.putExtra("BillType", "PHOTO");
//                startActivity(intent);
//                finish();
//            }
//        });
        Btn_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getBaseContext(), Billing_SearchBy_Activity.class);
                i.putExtra("SearchType", "Search");
                startActivity(i);
                finish();
            }
        });
        Btn_Sequence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), Billing_Sequence_Activity.class);
                i.putExtra("BillType", "");
                startActivity(i);
                finish();
            }
        });
        Btn_Search_By_Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getBaseContext(), Search_By_NameActivity.class);
                startActivity(i);
                finish();
            }
        });

        Btn_IR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                if (CommonFunctions.isMachineOrMobileDevice().equals("tianyu")) {
                    intent = new Intent(getBaseContext(), USB_Activity.class);
                }
                startActivity(intent);
                finish();

            }
        });

        btnDeleteBilledData.setOnClickListener(v -> {
            showPasswordDialog();
        });

        btnBillBilledData.setOnClickListener(v -> {
            downloadDataDialog();
        });
        Btn_server_Push.setOnClickListener(v ->{
            serverPush();
        });
    }

    private void serverPush() {
        passwordDialog.setContentView(R.layout.custom_dialog_layout);
        passwordDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tilPassword = passwordDialog.findViewById(R.id.tnl_password);
        etPassword = passwordDialog.findViewById(R.id.et_password);
        btnVerifyPass = passwordDialog.findViewById(R.id.btn_verify_pass);
        if (!isFinishing()){
            passwordDialog.show();
            btnVerifyPass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String password1 = Objects.requireNonNull(etPassword.getText()).toString().trim();
                    if (password1.equals("tsspdcl")) {
                        tilPassword.setError(null);
                        tilPassword.setErrorEnabled(false);
                        passwordDialog.dismiss();
                        passwordDialog.cancel();
                        try {
                            String backupData = new ReportsDAO().getOutputRecordsBackup();
                            String machineId = GetIMEI_Number.getUniqueIMEIId(BillingMenuActivity.this);
                            new FileOperations().writeToFile("", backupData, "TSSPDCL_DUMP_" + machineId + "_" + new DateUtil().getDatetimeStamp() + ".txt", BillingMenuActivity.this);
//                             String path = "/Android/TSSPDCL_METER_IMAGES/F6006526/rmd___Manual.png";
//                             ReportsDAO.zipFile(path);
                            Toast.makeText(BillingMenuActivity.this, "Done", Toast.LENGTH_SHORT).show();
                        }catch (Exception ex){
                            Log.e("error",ex.getMessage());
                            ex.printStackTrace();
                            Toast.makeText(BillingMenuActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        tilPassword.setErrorEnabled(true);
                        tilPassword.setError("Wrong Password");
                        Toast.makeText(BillingMenuActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    private void downloadDataDialog() {

        areaDialog.setContentView(R.layout.download_data_layout);
        areaDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tilDownload = areaDialog.findViewById(R.id.tnl_download);
        etDownload = areaDialog.findViewById(R.id.et_download);
        btn_append = areaDialog.findViewById(R.id.btn_append);
        btn_download = areaDialog.findViewById(R.id.btn_download);

        if (!isFinishing()) {
            areaDialog.show();
            btn_append.setOnClickListener(v -> {
                String areaCode = Objects.requireNonNull(etDownload.getText()).toString().trim();
                tilDownload.setError(null);
                tilDownload.setErrorEnabled(false);
                areaDialog.dismiss();
                areaDialog.cancel();
                appendMasterData(areaCode, true);
            });

            btn_download.setOnClickListener(v -> {
                String areaCode = Objects.requireNonNull(etDownload.getText()).toString().trim();
                tilDownload.setError(null);
                tilDownload.setErrorEnabled(false);
                areaDialog.dismiss();
                areaDialog.cancel();
                appendMasterData(areaCode, false);
            });
        }
    }

    private void appendMasterData(String areaCode, boolean isAppend) {
        progressDialog = new ProgressDialog(BillingMenuActivity.this);
        try {
            String url = "http://14.99.140.194:9094/TsspdclAuthWs/analogics/dataWS/download/";
            progressDialog.setTitle("Downloading master data from the server");
            progressDialog.setMessage("Please wait....");
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            progressDialog.show();

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, prepareJSonStringTomasterDownload(areaCode), new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        if (jsonObject != null && jsonObject.has("status")) {
                            String status = jsonObject.getString("status");

                            //  Log.e("TAG", "onResponse:12222:  " + jsonObject);
                            String msg = jsonObject.getString("response");
                            if (status.equalsIgnoreCase("ACK")) {
                                new DownloadData(BillingMenuActivity.this, jsonObject, isAppend).start();
                            } else {
                                Toast.makeText(BillingMenuActivity.this, msg, Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }
                        }

                    } catch (Exception ex) {
//                        progressDialog.dismiss();
                    }


                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(BillingMenuActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
                }
            });
            jsonRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonRequest);

        } catch (Exception e) {
//            progressDialog.dismiss();
            e.printStackTrace();
        }
    }

    private JSONObject prepareJSonStringTomasterDownload(String areaCode) {
        JSONObject requestJson = new JSONObject();
        try {
            String deviceId = configSharedPreferances.getUserCredentials(this, Config_SharedPreferances.deviceId);
            String longitude = configSharedPreferances.getUserCredentials(this, Config_SharedPreferances.longitude);
            String latitude = configSharedPreferances.getUserCredentials(this, Config_SharedPreferances.latitude);
            String phoneNumber = configSharedPreferances.getUserCredentials(this, Config_SharedPreferances.phoneNumber);


            JSONObject jsonObject = new JSONObject();

            jsonObject.put("longitude", longitude);
            jsonObject.put("latitude", latitude);
            jsonObject.put("deviceId", deviceId);
            jsonObject.put("mobileNo", phoneNumber);
            jsonObject.put("areaCode", areaCode);
            requestJson.put("downloadData", jsonObject);


            return requestJson;

        } catch (JSONException ex) {

            ex.printStackTrace();
        }
        return requestJson;

    }

    private void showPasswordDialog() {

        passwordDialog.setContentView(R.layout.custom_dialog_layout);
        passwordDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tilPassword = passwordDialog.findViewById(R.id.tnl_password);
        etPassword = passwordDialog.findViewById(R.id.et_password);
        btnVerifyPass = passwordDialog.findViewById(R.id.btn_verify_pass);

        if (!isFinishing()) {
            passwordDialog.show();
            btnVerifyPass.setOnClickListener(v -> {
                String password = Objects.requireNonNull(etPassword.getText()).toString().trim();
                if (password.equals("tsspdcl")) {
                    tilPassword.setError(null);
                    tilPassword.setErrorEnabled(false);
                    passwordDialog.dismiss();
                    passwordDialog.cancel();
                    Intent intent = new Intent(BillingMenuActivity.this, DeleteData.class);
                    startActivity(intent);
                } else {
                    tilPassword.setErrorEnabled(true);
                    tilPassword.setError("Wrong Password");
                    Toast.makeText(this, "Wrong password..", Toast.LENGTH_LONG).show();
                }
            });
        }


    }

//    private void deleteDataFromOutputMaster(Dialog passwordDialog) {
//        String dltOutputMasterDataQry = "DELETE FROM OUTPUT_MASTER2";
//
//        DBAdapter dbAdapter = DBAdapter.getDBAdapterInstance(this);
//        dbAdapter.openDataBase();
//        boolean result = DBAdapter.ExecuteQry(dltOutputMasterDataQry);
//        if(result){
//            Toast.makeText(this, "Bill Deleted Successfully", Toast.LENGTH_LONG).show();
//            passwordDialog.dismiss();
//        }else{
//            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
//        }
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // If want to block just return false
            navigateToBackActivity();

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

    private void navigateToBackActivity() {
        Intent i = new Intent(getBaseContext(), MainMenuActivity.class);
        startActivity(i);
        finish();
    }
}