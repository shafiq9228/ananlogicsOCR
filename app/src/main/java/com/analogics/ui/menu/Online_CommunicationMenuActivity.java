package com.analogics.ui.menu;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;

import com.analogics.DBAdapter.DBAdapter;


import com.analogics.R;
import com.analogics.aanil.apcpdcl_billing_app.MainSplashScreen;
import com.analogics.appUtils.Config_SharedPreferances;
import com.analogics.utils.AlertMessage;
import com.analogics.utils.DownloadData;
import com.analogics.webservice.APIClient;
import com.analogics.webservice.APIInterface;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.ArchiverFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.PUT;

public class Online_CommunicationMenuActivity extends AppCompatActivity {

    APIInterface apiInterface;

    SharedPreferences configSharedpreferences;
    String config_Preferences = "config_Preferences";
    String eroCodeKey = "eroCodeKey";
    String sectionIdKey = "sectionIdKey";

    String eroCodeValue = "";
    String sectionIdValue = "";

    Button Btn_ERO_Code;
    Button Btn_SectionId;
    Button Btn_DownloadData;
    Button Btn_PatchUpdate;
    Button Btn_Image_Upload;
    Button Btn_BulkUpload;
    Button Btn_home;
    public static ProgressDialog progressDialog;
    private Config_SharedPreferances configSharedPreferances;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online__communication_menu);
        configSharedPreferances = new Config_SharedPreferances();


        ui_init();
        clickListener();

        new Config_SharedPreferances().check_GetConfigPreferanceValues(Online_CommunicationMenuActivity.this);
        eroCodeValue = new Config_SharedPreferances().getEroCodeValueConfigPreferanceValues(Online_CommunicationMenuActivity.this);
        ;
        sectionIdValue = new Config_SharedPreferances().getSectionIdValueConfigPreferanceValues(Online_CommunicationMenuActivity.this);
        ;
        configSharedpreferences = getSharedPreferences(config_Preferences, MODE_PRIVATE);

    }

    public void ui_init() {

        Btn_ERO_Code = findViewById(R.id.Btn_ERO_Code);
        Btn_SectionId = findViewById(R.id.Btn_SectionId);
        Btn_DownloadData = findViewById(R.id.Btn_DownloadData);
        Btn_PatchUpdate = findViewById(R.id.Btn_PatchUpdate);
        Btn_Image_Upload = findViewById(R.id.Btn_Image_Upload);
        Btn_BulkUpload = findViewById(R.id.Btn_BulkUpload);
        Btn_home = findViewById(R.id.Btn_home);
    }

    public void clickListener() {

        Btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), MainMenuActivity.class);
                startActivity(i);
                finish();
            }
        });


        Btn_ERO_Code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.appcompat.app.AlertDialog.Builder alertDialog = new androidx.appcompat.app.AlertDialog.Builder(new ContextThemeWrapper(Online_CommunicationMenuActivity.this, R.style.AlertMessageTheme));

                //    AlertDialog.Builder alertDialog = new AlertDialog.Builder(Online_CommunicationMenuActivity.this, R.style.AlertDialogTheme);
                alertDialog.setTitle("ERO CODE Configuration");
                alertDialog.setMessage("Enter ERO CODE.");
                final EditText input = new EditText(Online_CommunicationMenuActivity.this);
                input.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                alertDialog.setView(input);
                input.setBackgroundResource(R.color.blue_gray);
                input.setTextSize(20);
                input.setTextColor(Color.WHITE);
                input.setText(eroCodeValue + "");
                alertDialog.setIcon(R.drawable.ero_icon_75x75);

                alertDialog.setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                if (input.getText().toString().length() > 0) {

                                    SharedPreferences.Editor editor = configSharedpreferences.edit();
                                    editor.putString(eroCodeKey, input.getText().toString() + "");
                                    editor.commit();
                                    Intent i = new Intent(getBaseContext(), Online_CommunicationMenuActivity.class);
                                    startActivity(i);
                                    finish();
                                } else {
                                    //Toast.makeText(getApplicationContext(), "Password Not Matched", Toast.LENGTH_SHORT).show();
                                    new AlertMessage().alertMessage(Online_CommunicationMenuActivity.this, "Info", "Please enter the ERO Code.");
                                }
                            }
                        });

                alertDialog.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog
                                dialog.cancel();
                            }
                        });
                final androidx.appcompat.app.AlertDialog dialog = alertDialog.create();

                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(final DialogInterface dialog) {
                        Button negativeButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                        Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);

                        // this not working because multiplying white background (e.g. Holo Light) has no effect
                        //negativeButton.getBackground().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);

                        final Drawable negativeButtonDrawable = getResources().getDrawable(R.drawable.bt_shape_orange);
                        final Drawable positiveButtonDrawable = getResources().getDrawable(R.drawable.bt_shape);
                        if (Build.VERSION.SDK_INT >= 16) {
                            negativeButton.setBackground(negativeButtonDrawable);
                            positiveButton.setBackground(positiveButtonDrawable);
                        } else {
                            negativeButton.setBackgroundDrawable(negativeButtonDrawable);
                            positiveButton.setBackgroundDrawable(positiveButtonDrawable);
                        }

                        negativeButton.invalidate();
                        positiveButton.invalidate();
                    }
                });
                alertDialog.show();

            }
        });


        Btn_SectionId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.appcompat.app.AlertDialog.Builder alertDialog = new androidx.appcompat.app.AlertDialog.Builder(new ContextThemeWrapper(Online_CommunicationMenuActivity.this, R.style.AlertMessageTheme));
                //AlertDialog.Builder alertDialog = new AlertDialog.Builder(Online_CommunicationMenuActivity.this, R.style.AlertDialogTheme);
                alertDialog.setTitle("Section Id Configuration");
                alertDialog.setMessage("Enter Section Id.");
                final EditText input = new EditText(Online_CommunicationMenuActivity.this);
                input.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                alertDialog.setView(input);
                input.setBackgroundResource(R.color.blue_gray);
                input.setTextSize(20);
                input.setTextColor(Color.WHITE);
                input.setText(sectionIdValue);
                alertDialog.setIcon(R.drawable.section_id_icon_75x75);

                alertDialog.setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                if (input.getText().toString().length() > 0) {

                                    SharedPreferences.Editor editor = configSharedpreferences.edit();
                                    editor.putString(sectionIdKey, input.getText().toString() + "");
                                    editor.commit();
                                    Intent i = new Intent(getBaseContext(), Online_CommunicationMenuActivity.class);
                                    startActivity(i);
                                    finish();

                                } else {
                                    //Toast.makeText(getApplicationContext(), "Password Not Matched", Toast.LENGTH_SHORT).show();
                                    new AlertMessage().alertMessage(Online_CommunicationMenuActivity.this, "Info", "Please enter the Section Code.");
                                }
                            }
                        });

                alertDialog.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.cancel();
                            }
                        });
                final androidx.appcompat.app.AlertDialog dialog = alertDialog.create();

                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(final DialogInterface dialog) {
                        Button negativeButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                        Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);

                        // this not working because multiplying white background (e.g. Holo Light) has no effect
                        //negativeButton.getBackground().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);

                        final Drawable negativeButtonDrawable = getResources().getDrawable(R.drawable.bt_shape_orange);
                        final Drawable positiveButtonDrawable = getResources().getDrawable(R.drawable.bt_shape);
                        if (Build.VERSION.SDK_INT >= 16) {
                            negativeButton.setBackground(negativeButtonDrawable);
                            positiveButton.setBackground(positiveButtonDrawable);
                        } else {
                            negativeButton.setBackgroundDrawable(negativeButtonDrawable);
                            positiveButton.setBackgroundDrawable(positiveButtonDrawable);
                        }

                        negativeButton.invalidate();
                        positiveButton.invalidate();
                    }
                });

                alertDialog.show();

            }
        });

        Btn_DownloadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                downloadMasterData();
            }
        });

    }


    public void downloadMasterData() {
         progressDialog = new ProgressDialog(Online_CommunicationMenuActivity.this);

        try {

//            apiInterface = APIClient.getDownloadClient().create(APIInterface.class);
//            JsonObject convertedObject = new Gson().fromJson(prepareJSonStringTomasterDownload(), JsonObject.class);
//            Call<ResponseBody> call4 = apiInterface.downloaddata(convertedObject);

            String url = "http://172.16.0.207:9094/TsspdclAuthWs/analogics/dataWS/download/";
            progressDialog.setTitle("Downloading master data from the server");
            progressDialog.setMessage("Please wait....");
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            progressDialog.show();

//            call4.enqueue(new Callback<ResponseBody>() {
//
//                @Override
//                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
//
//                    if (response.isSuccessful() && response.body() != null) {
//                        String responseStr = "";
//                        try {
//                            responseStr = response.body().string();
//                            JSONObject jsonRootObject = new JSONObject(responseStr);
//                            String status = jsonRootObject.getString("status");
//
//                            if (status.equalsIgnoreCase("ACK")) {
//
//                                JSONArray responseArrayList = jsonRootObject.getJSONArray("inputList");
//                                new DownloadData(Online_CommunicationMenuActivity.this,responseArrayList).start();
//
//                            }
//
////                            String status = jsonRootObject.optString("response");
////
////                            if (status.equalsIgnoreCase("@ACK@")) {
////                                String inputString = jsonRootObject.optString("inputString");
////                                byte[] bb = Base64.decode(inputString, 0);
////
////                                try {
////                                    FileUtils.writeByteArrayToFile(new File(Environment.getExternalStorageDirectory() + "/APSPDCLDATA.tar"),
////                                            bb);
////
////                                    Archiver archiver = ArchiverFactory.createArchiver("tar", "gz");
////                                    archiver.extract(new File(Environment.getExternalStorageDirectory() + "/APSPDCLDATA.tar"),
////                                                     new File(Environment.getExternalStorageDirectory() + "/Android/"));
////
////                                    File sdcard = Environment.getExternalStorageDirectory();
////                                    //Get the text file
////                                    File file = new File(sdcard, "/Android/" + eroCodeValue + "_" + sectionIdValue + "_input.txt");
////                                    //Read text from file
////                                    //StringBuilder text = new StringBuilder();
////
////                                    BufferedReader br = new BufferedReader(new FileReader(file));
////                                    String line;
////                                    DBAdapter dbAdapter = DBAdapter.getDBAdapterInstance(Online_CommunicationMenuActivity.this);
////                                    dbAdapter.openDataBase();
////                                    dbAdapter.executeRawQuery("delete from consumer_data ;");
////                                    int recordsCount = 0;
////                                    while ((line = br.readLine()) != null) {
////                                        recordsCount++;
////
////                                        String[] record = line.split("\\|");
////                                        //String insertQuery="insert into consumer_data values("+record[0]+","+record[1]+","+record[2]+","+record[3]+","+record[4]+","+record[5]+","+record[6]+","+record[7]+","+record[8]+","+record[9]+","+record[10]+","+record[11]+","+record[12]+","+record[13]+","+record[14]+","+record[15]+","+record[16]+","+record[17]+","+record[18]+","+record[19]+","+record[20]+","+record[21]+","+record[22]+","+record[23]+","+record[24]+","+record[25]+","+record[26]+","+record[27]+","+record[28]+","+record[29]+","+record[30]+","+record[31]+","+record[32]+","+record[33]+","+record[34]+","+record[35]+","+record[36]+","+record[37]+","+record[38]+","+record[39]+","+record[40]+","+record[41]+","+record[42]+","+record[43]+","+record[44]+","+record[45]+","+record[46]+","+record[47]+","+record[48]+","+record[49]+","+record[50]+","+record[51]+","+record[52]+","+record[53]+","+record[54]+","+record[55]+","+record[56]+","+record[57]+","+record[58]+","+record[59]+","+record[60]+","+record[61]+","+record[62]+","+record[63]+","+record[64]+","+record[65]+","+record[66]+","+record[67]+","+record[68]+","+record[69]+","+record[70]+","+record[71]+","+record[72]+","+record[73]+","+record[74]+","+record[75]+","+record[76]+","+record[77]+","+record[78]+");";
////
////                                        String insertQuery = "insert into consumer_data values('"
////                                                + record[0] + "','" + record[1] + "','" + record[2]
////                                                + "','" + record[3] + "','" + record[4] + "','" + record[5] + "','" + record[6]
////                                                + "','" + record[7] + "','" + record[8] + "','" + record[9] + "','" + record[10]
////                                                + "','" + record[11] + "','" + record[12] + "','" + record[13] + "','" + record[14]
////                                                + "','" + record[15] + "','" + record[16] + "','" + record[17] + "','" + record[18]
////                                                + "','" + record[19] + "','" + record[20] + "','" + record[21] + "','" + record[22]
////                                                + "','" + record[23] + "','" + record[24] + "','" + record[25] + "','" + record[26]
////                                                + "','" + record[27] + "','" + record[28] + "','" + record[29] + "','" + record[30]
////                                                + "','" + record[31] + "','" + record[32] + "','" + record[33] + "','" + record[34]
////                                                + "','" + record[35] + "','" + record[36] + "','" + record[37] + "','" + record[38]
////                                                + "','" + record[39] + "','" + record[40] + "','" + record[41] + "','" + record[42]
////                                                + "','" + record[43] + "','" + record[44] + "','" + record[45] + "','" + record[46]
////                                                + "','" + record[47] + "','" + record[48] + "','" + record[49] + "','" + record[50]
////                                                + "','" + record[51] + "','" + record[52] + "','" + record[53] + "','" + record[54]
////                                                + "','" + record[55] + "','" + record[56] + "','" + record[57] + "','" + record[58]
////                                                + "','" + record[59] + "','" + record[60] + "','" + record[61] + "','" + record[62]
////                                                + "','" + record[63] + "','" + record[64] + "','" + record[65] + "','" + record[66]
////                                                + "','" + record[67] + "','" + record[68] + "','" + record[69] + "','" + record[70]
////                                                + "','" + record[71] + "','" + record[72] + "','" + record[73] + "','" + record[74]
////                                                + "','" + record[75] + "','" + record[76] + "','" + record[77] + "','" + record[78]
////                                                + "','" + eroCodeValue + "','" + sectionIdValue + "');";
////
////                                        dbAdapter.executeRawQuery(insertQuery);
////                                    }
////                                    dbAdapter.close();
////                                    br.close();
////                                    Toast.makeText(Online_CommunicationMenuActivity.this, recordsCount + " Records downloaded Successfully.", Toast.LENGTH_LONG).show();
////
////                                } catch (IOException e) {
////                                    e.printStackTrace();
////                                }
////
////                            } else {
////                                Toast.makeText(Online_CommunicationMenuActivity.this, "Master download failed", Toast.LENGTH_LONG).show();
////                            }
//                            progressDialog.dismiss();
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            progressDialog.dismiss();
//                        }
//                    }
//                }
//
//                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
//                    call.cancel();
//                    progressDialog.dismiss();
//
//                    new AlertMessage().alertMessage(Online_CommunicationMenuActivity.this, "Alert", "Server down please check with the Administrator.");
//                }
//
//            });

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, prepareJSonStringTomasterDownload(), new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        if (jsonObject != null && jsonObject.has("status")) {
                            String status = jsonObject.getString("status");

                            Log.e("TAG", "onResponse:12222:  "+ jsonObject);
                            String msg = "jsonObject.getString(reason)";
                       if (status.equalsIgnoreCase("ACK")) {
                                new DownloadData(Online_CommunicationMenuActivity.this, jsonObject, false).start();
                            } else {
                                Toast.makeText(Online_CommunicationMenuActivity.this, msg, Toast.LENGTH_LONG).show();
                            }
                        }

                    } catch (Exception ex) {
//                        progressDialog.dismiss();
                    }


                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(Online_CommunicationMenuActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
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


            Log.e("TAG", "prepareJSonStringTomasterDownload: " + requestJson);

            return requestJson;

        } catch (JSONException ex) {

            ex.printStackTrace();
        }
        return requestJson;

    }

    // return jsonObj.toString();


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // If want to block just return false
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            // If want to block just return false
            return false;
        }
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            // If want to block just return false
            return false;
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
}