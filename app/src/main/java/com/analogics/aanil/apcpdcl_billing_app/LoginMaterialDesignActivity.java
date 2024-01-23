package com.analogics.aanil.apcpdcl_billing_app;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.CellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.analogics.R;
import com.analogics.appUtils.Config_SharedPreferances;
import com.analogics.ui.menu.MainMenuActivity;
import com.analogics.utils.AlertMessage;
import com.analogics.utils.CommonFunctions;
import com.analogics.utils.GPRSTest;
import com.analogics.utils.GetIMEI_Number;
import com.analogics.utils.SignalStrengthUtil;
import com.analogics.utils.SimSignal_StausUtil;
import com.analogics.webservice.APIClient;
import com.analogics.webservice.APIInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginMaterialDesignActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 104;
    APIInterface apiInterface;
    TelephonyManager telManager;

    TextView TV_signalstrength;
    private EditText ET_Userid;
    private EditText ET_Password;
    private Button Btn_login;
    private Button Btn_logout;
    ImageView IV_DownloadMaster;
    private CardView cv;

    Button Btn_SimStatusCheck;

    boolean availableFlag = false;

    SharedPreferences sharedpreferences;
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;
    ProgressDialog dialog;

    String[] permissionsRequired =
            new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.RECEIVE_BOOT_COMPLETED,
                    Manifest.permission.CHANGE_NETWORK_STATE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN
            };


    private BluetoothAdapter bluetoothAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login_material_design);

        int currentapiVersion = Build.VERSION.SDK_INT;
        try {
            if (currentapiVersion >= 30) {
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
            initActions();
        }


        if (currentapiVersion >= 23) {
            // Do something for 23 and above versions

            permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);

            if (ActivityCompat.checkSelfPermission(LoginMaterialDesignActivity.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(LoginMaterialDesignActivity.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(LoginMaterialDesignActivity.this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(LoginMaterialDesignActivity.this, permissionsRequired[3]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(LoginMaterialDesignActivity.this, permissionsRequired[4]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(LoginMaterialDesignActivity.this, permissionsRequired[5]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(LoginMaterialDesignActivity.this, permissionsRequired[6]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(LoginMaterialDesignActivity.this, permissionsRequired[7]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(LoginMaterialDesignActivity.this, permissionsRequired[8]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(LoginMaterialDesignActivity.this, permissionsRequired[9]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(LoginMaterialDesignActivity.this, permissionsRequired[10]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(LoginMaterialDesignActivity.this, permissionsRequired[11]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(LoginMaterialDesignActivity.this, permissionsRequired[12]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(LoginMaterialDesignActivity.this, permissionsRequired[13]) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(LoginMaterialDesignActivity.this, permissionsRequired[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(LoginMaterialDesignActivity.this, permissionsRequired[1])
                        || ActivityCompat.shouldShowRequestPermissionRationale(LoginMaterialDesignActivity.this, permissionsRequired[2])
                        || ActivityCompat.shouldShowRequestPermissionRationale(LoginMaterialDesignActivity.this, permissionsRequired[3])
                        || ActivityCompat.shouldShowRequestPermissionRationale(LoginMaterialDesignActivity.this, permissionsRequired[4])
                        || ActivityCompat.shouldShowRequestPermissionRationale(LoginMaterialDesignActivity.this, permissionsRequired[5])
                        || ActivityCompat.shouldShowRequestPermissionRationale(LoginMaterialDesignActivity.this, permissionsRequired[6])
                        || ActivityCompat.shouldShowRequestPermissionRationale(LoginMaterialDesignActivity.this, permissionsRequired[7])
                        || ActivityCompat.shouldShowRequestPermissionRationale(LoginMaterialDesignActivity.this, permissionsRequired[8])
                        || ActivityCompat.shouldShowRequestPermissionRationale(LoginMaterialDesignActivity.this, permissionsRequired[9])
                        || ActivityCompat.shouldShowRequestPermissionRationale(LoginMaterialDesignActivity.this, permissionsRequired[10])
                        || ActivityCompat.shouldShowRequestPermissionRationale(LoginMaterialDesignActivity.this, permissionsRequired[11])

                        || ActivityCompat.shouldShowRequestPermissionRationale(LoginMaterialDesignActivity.this, permissionsRequired[12])
                        || ActivityCompat.shouldShowRequestPermissionRationale(LoginMaterialDesignActivity.this, permissionsRequired[13])


                ) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginMaterialDesignActivity.this);
                    builder.setTitle("Need Multiple Permissions");
                    builder.setMessage("This app needs permissions.");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            ActivityCompat.requestPermissions(LoginMaterialDesignActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
                    //Previously Permission Request was cancelled with 'Dont Ask Again',
                    // Redirect to Settings after showing Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginMaterialDesignActivity.this);
                    builder.setTitle("Need Multiple Permissions");
                    builder.setMessage("This app needs Camera and Location permissions .");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            sentToSettings = true;
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                            Toast.makeText(getBaseContext(), "Go to Permissions to Grant  Camera and Location", Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                    sentToSettings = true;
                /*Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
*/
                } else {
                    //just request the permission
                    ActivityCompat.requestPermissions(LoginMaterialDesignActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                }
                SharedPreferences.Editor editor = permissionStatus.edit();
                editor.putBoolean(permissionsRequired[0], true);
                editor.commit();
            } else {
                //You already have the permission, just go ahead.
                proceedAfterPermission();
            }
        } else {
            proceedAfterPermission();
        }
        setDeviceId();
    }

    private void setDeviceId() {
        String deviceId = new GetIMEI_Number().getUniqueIMEIId(LoginMaterialDesignActivity.this);
        ((TextView) findViewById(R.id.tv_device_id)).setText(deviceId);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
                proceedAfterPermission();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(LoginMaterialDesignActivity.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(LoginMaterialDesignActivity.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(LoginMaterialDesignActivity.this, permissionsRequired[2])
                    || ActivityCompat.shouldShowRequestPermissionRationale(LoginMaterialDesignActivity.this, permissionsRequired[3])
                    || ActivityCompat.shouldShowRequestPermissionRationale(LoginMaterialDesignActivity.this, permissionsRequired[4])
                    || ActivityCompat.shouldShowRequestPermissionRationale(LoginMaterialDesignActivity.this, permissionsRequired[5])
                    || ActivityCompat.shouldShowRequestPermissionRationale(LoginMaterialDesignActivity.this, permissionsRequired[6])
                    || ActivityCompat.shouldShowRequestPermissionRationale(LoginMaterialDesignActivity.this, permissionsRequired[7])
                    || ActivityCompat.shouldShowRequestPermissionRationale(LoginMaterialDesignActivity.this, permissionsRequired[8])
                    || ActivityCompat.shouldShowRequestPermissionRationale(LoginMaterialDesignActivity.this, permissionsRequired[9])
                    || ActivityCompat.shouldShowRequestPermissionRationale(LoginMaterialDesignActivity.this, permissionsRequired[10])
                    || ActivityCompat.shouldShowRequestPermissionRationale(LoginMaterialDesignActivity.this, permissionsRequired[11])
                    || ActivityCompat.shouldShowRequestPermissionRationale(LoginMaterialDesignActivity.this, permissionsRequired[12])
                    || ActivityCompat.shouldShowRequestPermissionRationale(LoginMaterialDesignActivity.this, permissionsRequired[13])

            ) {

                AlertDialog.Builder builder = new AlertDialog.Builder(LoginMaterialDesignActivity.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(LoginMaterialDesignActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

            } else {
                Toast.makeText(getBaseContext(), "Unable to get Permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(LoginMaterialDesignActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    private void proceedAfterPermission() {
        Toast.makeText(getBaseContext(), "We got All Permissions", Toast.LENGTH_LONG).show();
        //new PlayAudio().playSound(LoginMaterialDesignActivity.this,"beep_success.mp3");

        MediaPlayer mp = new MediaPlayer();
        mp = MediaPlayer.create(this, R.raw.beep_success);
        mp.start();

      /*    ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        if (is3g) {

            SignalStrengthUtil.MyPhoneStateListener myListener = new SignalStrengthUtil.MyPhoneStateListener();
            //   MyPhoneSignalStateListener   myListener = new MyPhoneSignalStateListener();
            telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            telManager.listen(myListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        }
*/
        MyPhoneStateListener myListener = new MyPhoneStateListener();
        //   MyPhoneSignalStateListener   myListener = new MyPhoneSignalStateListener();
        telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telManager.listen(myListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

        //     @SuppressLint({"NewApi", "LocalSuppress"}) String signalStr=new SignalStrengthUtil().getSignalStrength(LoginMaterialDesignActivity.this);
        //     Toast.makeText(getBaseContext(), "signalStr Sss >>> "+signalStr, Toast.LENGTH_LONG).show();

        sharedpreferences = getSharedPreferences("UseridPref_Key", Context.MODE_PRIVATE);
        String userId = sharedpreferences.getString("userId_Key", null);
        SharedPreferences configSharedpreferences;
        String config_Preferences = "config_Preferences";

        configSharedpreferences = getSharedPreferences(config_Preferences, Context.MODE_PRIVATE);

        if (userId == null) {
            // the key does not exist
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("userId_Key", "");
            editor.commit();
            editor.clear();
        } else {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("userId_Key", "");
            editor.commit();
            editor.clear();
        }

        String PrinterType = sharedpreferences.getString("PrinterType_Key", null);
        if (PrinterType == null) {
            // the key does not exist
            SharedPreferences.Editor editor = configSharedpreferences.edit();
            editor.putString("PrinterType_Key", "2T");
            editor.commit();
            editor.clear();
        }
        String printerType = new Config_SharedPreferances().getPrinterTypeValues(LoginMaterialDesignActivity.this);
        ;
        Toast.makeText(LoginMaterialDesignActivity.this, ">>>" + printerType, Toast.LENGTH_LONG).show();
// for example value of first element
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }

        initView();
        setListener();


    }

    private void initView() {

        ET_Userid = findViewById(R.id.ET_Userid);
        ET_Password = findViewById(R.id.ET_Password);
        Btn_login = findViewById(R.id.Btn_login);
        Btn_logout = findViewById(R.id.Btn_logout);
        cv = findViewById(R.id.cv);
        IV_DownloadMaster = findViewById(R.id.IV_DownloadMaster);
        IV_DownloadMaster.setVisibility(View.GONE);
        Btn_SimStatusCheck = findViewById(R.id.Btn_SimStatusCheck);
        Btn_SimStatusCheck.setVisibility(View.GONE);
        TV_signalstrength = findViewById(R.id.TV_signalstrength);
        TV_signalstrength.setVisibility(View.GONE);


    }

    private void setListener() {


      /*  // Start Service On Boot Start Up
        try {

            Intent service = new Intent(getApplicationContext(),
                    OfflineServiceUpload.class);
            this.startService(service);

        } catch (Exception e) {
            e.printStackTrace();
        }
*/


        Btn_SimStatusCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimSignal_StausUtil util = new SimSignal_StausUtil(LoginMaterialDesignActivity.this);

                boolean gprsStatus = util.gprsStatus();
                boolean simStatus = util.isSimCardAvailable();

                Toast.makeText(LoginMaterialDesignActivity.this, "Sim Status  >> " + simStatus + "\nGPRS Status >> " + gprsStatus, Toast.LENGTH_LONG).show();
            }
        });


        Btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(LoginMaterialDesignActivity.this);
                builder.setTitle("Info");
                builder.setMessage("Do you want to Exit .");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        //   Intent i = new Intent(getBaseContext(), LoginMaterialDesignActivity.class);
                        //  startActivity(i);
                        finish();
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
        });


        IV_DownloadMaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(LoginMaterialDesignActivity.this);
                builder.setTitle("Info");
                builder.setMessage("Do you want to download the Masterdata.");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                        GPRSTest gPRSTest = new GPRSTest(LoginMaterialDesignActivity.this);

                        if (gPRSTest.gprsTest()) {

                            // downloadMasterData();

                        } else {
                            new AlertMessage().alertMessage(LoginMaterialDesignActivity.this,
                                    "Please Check                                      ",
                                    "DATA CONNECTION NOT AVAILABLE");
                        }

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
        });

        // logout imageview onclick start
        Btn_login.setOnClickListener(new View.OnClickListener() {

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            public void onClick(View v) {

                //                Explode explode = new Explode();
                //              explode.setDuration(500);
                //            getWindow().setExitTransition(explode);
                //          getWindow().setEnterTransition(explode);

                Intent i = new Intent(getBaseContext(), MainMenuActivity.class);
                startActivity(i);
                finish();


                if ((ET_Userid.getText().toString().length() > 0)
                        && (ET_Password.getText().toString().length() > 0)) {

                    GPRSTest gPRSTest = new GPRSTest(LoginMaterialDesignActivity.this);

                    if (gPRSTest.gprsTest()) {
                        //AANIL

                        mobileLoginUser();

                    } else {
                        new AlertMessage().alertMessage(LoginMaterialDesignActivity.this,
                                "Please Check                                      ",
                                "DATA CONNECTION NOT AVAILABLE");
                    }
                } else {

                    new AlertMessage()
                            .alertMessage(
                                    LoginMaterialDesignActivity.this,
                                    "Validation                                      ",
                                    "Please Check the Userid, Password and Try again. ");
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        initView();
        setListener();

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_download_masters) {

            GPRSTest gPRSTest = new GPRSTest(LoginMaterialDesignActivity.this);

            if (gPRSTest.gprsTest()) {

                // downloadMasterData();
            } else {
                new AlertMessage().alertMessage(LoginMaterialDesignActivity.this,
                        "Please Check                                      ",
                        "DATA CONNECTION NOT AVAILABLE");
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private class myPhoneStateListener extends PhoneStateListener {
        public int signalStrengthValue = 0;

        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            if (signalStrength.isGsm()) {
                if (signalStrength.getGsmSignalStrength() != 99)
                    signalStrengthValue = signalStrength.getGsmSignalStrength() * 2 - 113;
                else
                    signalStrengthValue = signalStrength.getGsmSignalStrength();
            } else {
                signalStrengthValue = signalStrength.getCdmaDbm();
            }
            TV_signalstrength.setText("Signal Strength : " + signalStrengthValue);
        }
    }


    class MyPhoneSignalStateListener extends PhoneStateListener {
        int signal;
        String cellList;

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        @SuppressLint("MissingPermission")
        @Override
        public void onCellInfoChanged(List<CellInfo> cellInfo) {
            super.onCellInfoChanged(cellInfo);

            if (ActivityCompat.checkSelfPermission(LoginMaterialDesignActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            cellInfo = telManager.getAllCellInfo();
            if (cellInfo != null)
                cellList = cellInfo.toString();
            else cellList = "No Signal";
            TV_signalstrength.append("" + String.valueOf(cellList));
        }

        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            signal = signalStrength.getGsmSignalStrength() * 2 - 113;
            TV_signalstrength.append("GSM signal strength is " + String.valueOf(this.signal));
        }
    }


    public class MyPhoneStateListener extends PhoneStateListener {
        public int singalStenths = 0;

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);


            String signalStr = new SignalStrengthUtil(LoginMaterialDesignActivity.this).getSignalStrength_DbM();
            //      Toast.makeText(getBaseContext(), "signalStr >>> "+signalStr, Toast.LENGTH_LONG).show();
            //TV_signalstrength.setText("Signal strength is " +   signalStr +" dBm");


            int signal = Integer.parseInt(signalStr);


            //   TV_signalStrength.setText(signal+" " );
            if (signal > (-70)) {
                //   Toast.makeText(getBaseContext(), "signall >>> "+signal, Toast.LENGTH_LONG).show();
                TV_signalstrength.setTextColor(Color.GREEN);
                TV_signalstrength.setTextSize(20);
                TV_signalstrength.setText("Signal Strength : " + signal + " dBm");

            } else if ((signal > (-85)) && (signal < (-70))) {
                // Toast.makeText(getBaseContext(), "signall >>> "+signal, Toast.LENGTH_LONG).show();
                TV_signalstrength.setTextColor(Color.YELLOW);
                TV_signalstrength.setTextSize(20);
                TV_signalstrength.setText("Signal Strength : " + signal + " dBm");

            } else if ((signal > (-100)) && (signal < (-85))) {
                //Toast.makeText(getBaseContext(), "signall >>> "+signal, Toast.LENGTH_LONG).show();
                TV_signalstrength.setTextColor(Color.YELLOW);
                TV_signalstrength.setTextSize(20);
                TV_signalstrength.setText("Signal Strength : " + signal + " dBm");

            } else if ((signal <= (-100))) {
                //Toast.makeText(getBaseContext(), "signall >>> "+signal, Toast.LENGTH_LONG).show();
                TV_signalstrength.setTextSize(20);
                TV_signalstrength.setTextColor(Color.RED);
                TV_signalstrength.setText("Signal Strength : " + "No Signal");

            }
        }


    }


    public void mobileLoginUser() {

        try {
            apiInterface = APIClient.getClient().create(APIInterface.class);

            String requestStr = "<imeiNo>" + new GetIMEI_Number().getUniqueIMEIId(LoginMaterialDesignActivity.this) + "</imeiNo><userName>" + ET_Userid.getText().toString() + "</userName><password>" + ET_Password.getText().toString() + "</password>";

            Call<ResponseBody> call4 = apiInterface.mobileLoginUser(requestStr);

            final ProgressDialog progressDoalog;
            progressDoalog = new ProgressDialog(LoginMaterialDesignActivity.this);
            progressDoalog.setTitle("User Authentication is in progress");
            progressDoalog.setMessage("Please wait....");
            progressDoalog.setCancelable(false);
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.show();
            progressDoalog.dismiss();

            call4.enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                    if (response.isSuccessful()) {
                        String responseStr = "";
                        try {
                            responseStr = response.body().string();
                            System.out.println("responseStr>>>" + responseStr);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (response.body() != null) {

                            System.out.println(">>>" + responseStr);

                            Log.v("onSuccess", responseStr);
                            //  Toast.makeText(getApplicationContext(), (response.message())+"\n"+new Gson().toJson(response.body()), Toast.LENGTH_SHORT).show();
                            //   Log.v("TAG", "response 33: " + new Gson().toJson(responseMessage));
                            progressDoalog.dismiss();
                            //    String responseStr = new Gson().toJson(response.body()) + "";
                            try {
                                JSONObject jsonRootObject = new JSONObject(responseStr);

                                String status = jsonRootObject.optString("response");

                                if (status.equalsIgnoreCase("@ACK@")) {

                                    Intent i = new Intent(getBaseContext(), MainMenuActivity.class);
                                    startActivity(i);
                                    finish();

                                    Toast.makeText(LoginMaterialDesignActivity.this, responseStr + " Success", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(LoginMaterialDesignActivity.this, responseStr + " failed", Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }


                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    call.cancel();
                    progressDoalog.dismiss();
                    System.out.println("onfailure");
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // If want to block just return false
            return false;
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


    private void initActions() {

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


    private void showToastMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


}