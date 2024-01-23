package com.analogics.irda;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.analogics.DBAdapter.DBAdapter;
import com.analogics.R;
import com.analogics.pojo.IrdaVO;
import com.analogics.ui.billing.Billing_SearchBy_Activity;
import com.analogics.ui.billing.Search_By_NameActivity;
import com.analogics.ui.menu.BillingMenuActivity;
import com.analogics.ui.settings.UsbService;
import com.analogics.utils.HexSupport;
import com.analogics.utils.PlayAudio;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.Set;


public class USB_Activity extends Activity {
    int numberOfTries =0;

    UsbService usbService = new UsbService();
    private MyHandler mHandler;

    DBAdapter dbAdapter = null;
    Cursor readrecord = null;

    IrdaVO irdaVO = new IrdaVO();
    Button Btn_home, Btn_nextIR_IRDA;
    Button Btn_IR_3Phase_Send, Btn_IR_1Phase_Send;
    Button Btn_IRDA_3Phase_Send, Btn_IRDA_1Phase_Send;
    Button Btn_3PH_IRDA_Send;
    Button Btn_IRDA_1Phase_Delay_Send;

    String meterData = "";
    HexSupport hex = new HexSupport();
    String meterNumberIrDa = "";
    private TextView display;
    boolean isIr1P = false, isIr3P = false;
    boolean isIrda1P = false, isIrda3P = false, isIrda1PDelay = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ir);
        mHandler = new MyHandler(USB_Activity.this);

        Btn_home = (Button) findViewById(R.id.Btn_home);
        Btn_nextIR_IRDA = findViewById(R.id.BTN_nextIR_IRDA);
        display = (TextView) findViewById(R.id.display);

        Btn_IR_3Phase_Send = (Button) findViewById(R.id.Btn_IR_3Phase_Send);
        Btn_IR_1Phase_Send = (Button) findViewById(R.id.Btn_IR_1Phase_Send);

        Btn_IRDA_3Phase_Send = (Button) findViewById(R.id.Btn_IRDA_3Phase_Send);
        Btn_IRDA_1Phase_Send = (Button) findViewById(R.id.Btn_IRDA_1Phase_Send);
        Btn_IRDA_1Phase_Delay_Send = (Button) findViewById(R.id.Btn_IRDA_1Phase_Delay_Send);
        Btn_3PH_IRDA_Send = (Button) findViewById(R.id.Btn_3PH_IRDA_Send);

        Btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(USB_Activity.this, BillingMenuActivity.class); //MainMenuActivity
                startActivity(i);
                finish();
            }
        });

        Btn_IR_3Phase_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isIr3P = true;
                isIr1P = false;
                isIrda1P = false;
                isIrda3P = false;

                display.setText("IR Three Phase selected");
                new AnalogicsUtil().setIRBaudRate3P(usbService);
                GetMeterDataAsyncTask w = new GetMeterDataAsyncTask();
                w.execute();
            }
        });

        Btn_IR_1Phase_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isIr3P = false;
                isIr1P = true;
                isIrda1P = false;
                isIrda3P = false;

                display.setText("IR Single Phase selected");
                new AnalogicsUtil().setIRBaudRate1P(usbService);
                GetMeterDataAsyncTask w = new GetMeterDataAsyncTask();
                w.execute();
            }
        });

        Btn_IRDA_3Phase_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isIr3P = false;
                isIr1P = false;
                isIrda1P = false;
                isIrda3P = true;

                display.setText("IRDA Three Phase selected");
                display.setText("\n");
                display.setText("Meter being read, please wait");
                display.setText("\n");

                AnalogicsUtil util = new AnalogicsUtil();
                util.setIRDABaudRate3P(usbService);
                usbService.sb = new StringBuilder();

                try{
                    byte[] buffer = new byte[11];
                    // 95 95 FF FF FF 0B 96 31 11 05 00
                    buffer[0] = (byte) 0x95;
                    buffer[1] = (byte) 0x95;
                    buffer[2] = (byte) 0xFF;
                    buffer[3] = (byte) 0xFF;
                    buffer[4] = (byte) 0xFF;
                    buffer[5] = 0x0B;
                    buffer[6] = (byte) 0x96;
                    buffer[7] = 0x31;
                    buffer[8] = 0x11;
                    buffer[9] = 0x05;
                    buffer[10] = 0x00;
                    try{
                        if (usbService != null) { // if UsbService was correctly binded, Send data
                            usbService.write(buffer);
                            Thread.sleep(1000);
                        }
                    }catch(Exception ex){

                    }
                    String outputBuff = usbService.sb.toString();

                    if (outputBuff.length() > 42) {
                        outputBuff = outputBuff.substring(22);

                        String hexMeterNumber = outputBuff.substring(18, 20) + outputBuff.substring(16, 18) + outputBuff.substring(14, 16);
                        String meterNumber = Integer.parseInt(hexMeterNumber, 16) + "";

                        HexSupport hex = new HexSupport();
                        usbService.sb = new StringBuilder();
                        byte[] buffer1 = new byte[3];
                        buffer1[0] = (byte) util.hexStringToByteArray(outputBuff.substring(14, 16))[0];
                        buffer1[1] = (byte) util.hexStringToByteArray(outputBuff.substring(16, 18))[0];
                        buffer1[2] = (byte) util.hexStringToByteArray(outputBuff.substring(18, 20))[0];

                        byte[] buffer2 = new byte[11];
                        // 95 95 8A 94 32 0B 00 31 11 05 00  kwh/kvah/rmd
                        buffer2[0] = (byte) 0x95;
                        buffer2[1] = (byte) 0x95;
                        buffer2[2] = buffer1[0];
                        buffer2[3] = buffer1[1];
                        buffer2[4] = buffer1[2];
                        buffer2[5] = 0x0B;//
                        buffer2[6] = 0x00;// 0x01 for solar kwh
                        buffer2[7] = 0x31;
                        buffer2[8] = 0x11;
                        buffer2[9] = 0x05;
                        buffer2[10] = 0x00;

                        try{
                            if (usbService != null) { // if UsbService was correctly binded, Send data
                                usbService.write(buffer2);
                                Thread.sleep(1000);
                            }
                        }catch(Exception ex){

                        }

                        String outputBuff1 = usbService.sb.toString();
                        if (outputBuff1.length() > 42) {
                            outputBuff1 = outputBuff1.substring(22);
                            String VR = outputBuff1.substring(34, 36) + outputBuff1.substring(32, 34);
                            String voltage_R_VR = (Integer.parseInt(VR, 16) / 10.00) + "";

                            String VY = outputBuff1.substring(38, 40) + outputBuff1.substring(36, 38);
                            String voltage_Y_VY = (Integer.parseInt(VY, 16) / 10.00) + "";

                            String VB = outputBuff1.substring(42, 44) + outputBuff1.substring(40, 42);
                            String voltage_B_VB = (Integer.parseInt(VB, 16) / 10.00) + "";

                            String IR = outputBuff1.substring(46, 48) + outputBuff1.substring(44, 46);
                            String current_R_IR = (Integer.parseInt(IR, 16) / 100.00) + "";

                            String IY = outputBuff1.substring(50, 52) + outputBuff1.substring(48, 50);
                            String current_Y_IY = (Integer.parseInt(IY, 16) / 100.00) + "";

                            String IB = outputBuff1.substring(54, 56) + outputBuff1.substring(52, 54);
                            String current_B_IB = (Integer.parseInt(IB, 16) / 100.00) + "";

                            String CUMULATIVE_KWH = outputBuff1.substring(70, 72) +
                                    outputBuff1.substring(68, 70) + outputBuff1.substring(66, 68) +
                                    outputBuff1.substring(64, 66);
                            String meter_units = (Integer.parseInt(CUMULATIVE_KWH, 16) / 100.00) + "";

                            String CUMULATIVE_KVAH = outputBuff1.substring(94, 96) +
                                    outputBuff1.substring(92, 94) + outputBuff1.substring(90, 92) +
                                    outputBuff1.substring(88, 90);
                            String ir_curkvah = (Integer.parseInt(CUMULATIVE_KVAH, 16) / 100.00) + "";

                            String hv_rmd = outputBuff1.substring(98, 100) + outputBuff1.substring(96, 98);
                            String rmd = (Integer.parseInt(hv_rmd, 16) / 100.00) + "";

                            boolean isValid = true;
                            try{
                                double rmdDouble = Double.parseDouble(rmd);
                                if(rmdDouble > 50)
                                    isValid = false;
                            }catch(Exception ex){

                            }

                            String meterMake = "";
                            try{
                                String meterMakeHex = meterData.substring(114, 116) + meterData.substring(112, 114) + meterData.substring(110, 112);
                                meterMake = new com.analogics.utils.HexStringConverter().hexToString(meterMakeHex);

                            }catch(Exception ex){

                            }
                            display.setText("\n------------------------\n         IRDA 3 Phase " +
                                    "\nMeterNo   :  " + meterNumber);
                            display.append("\nKWH       :  " + meter_units +
                                    "\nKVAH      :  " + ir_curkvah +
                                    "\nhv_rmd    : " + rmd +
                                    // "\n***********************************" +
                                    // "\nVoltage_R_VR  : " + voltage_R_VR +
                                    // "\nVoltages_Y_VY : " + voltage_Y_VY +
                                    //"\nVoltage_B_VB   : " + voltage_B_VB +
                                    //"\nCurrent_R_IR  : " + current_R_IR +
                                    // "\nCurrent_Y_IY  : " + current_Y_IY +
                                    //"\nCurrent_B_IB  : " + current_B_IB +
                                    "\n------------------------\n");

                            if(isValid){
                                IrdaVO irdaVO = new IrdaVO();
                                irdaVO.setMeterNumber(meterNumber);
                                irdaVO.setVoltage_R_VR(Double.parseDouble(voltage_R_VR));
                                irdaVO.setVoltage_Y_VY(Double.parseDouble(voltage_Y_VY));
                                irdaVO.setVoltage_B_VB(Double.parseDouble(voltage_B_VB));
                                irdaVO.setCurrent_R_IR(Double.parseDouble(current_R_IR));
                                irdaVO.setCurrent_Y_IY(Double.parseDouble(current_Y_IY));
                                irdaVO.setCurrent_B_IB(Double.parseDouble(current_B_IB));
                                irdaVO.setMeterMake(meterMake);

                                irdaVO.setKWH(Double.parseDouble(meter_units));
                                irdaVO.setKVAH(Double.parseDouble(ir_curkvah));
                                irdaVO.setHv_rmd(Double.parseDouble(rmd));
                                irdaVO.setIrdaReadingFlag(1);
                                new   PlayAudio().playSuccess(getApplicationContext());

                                if(irdaVO != null){
                                    dbAdapter = DBAdapter.getDBAdapterInstance(USB_Activity.this);
                                    dbAdapter.openDataBase();
                                    String query = "select * from INPUT_MASTER where trim(printf('%08d',meter_num)) like '%" + irdaVO.getMeterNumber() + "" + "%';";
                                    if(irdaVO.getMeterNumber().length() > 8){
                                        query = "select * from INPUT_MASTER where trim(printf('%s',meter_num)) like '%" + irdaVO.getMeterNumber() + "" + "%';";
                                    }
                                    readrecord = dbAdapter.selectRecordsFromDB(query, null);

                                    if (readrecord.moveToFirst() && !irdaVO.getMeterNumber().trim().isEmpty()) {
                                        Btn_nextIR_IRDA.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(USB_Activity.this, Search_By_NameActivity.class);
                                                intent.putExtra("SearchType", "Automatic");
                                                intent.putExtra("IRDAVO", (Serializable) irdaVO);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });

                                    } else {
                                        new PlayAudio().playFail(getApplicationContext());
                                        display.setText("\n IRDA Three Phase \n MeterNo not found " + irdaVO.getMeterNumber() + "\n");
                                        display.append("KWH             : " + meter_units +
                                                "\nKVAH                 : " + ir_curkvah +
                                                "\nhv_rmd               : " + rmd );
                                        Toast.makeText(USB_Activity.this, "Try Again. ", Toast.LENGTH_LONG).show();
//                                        alertDialog(irdaVO, "Meter Not found."+irdaVO.getMeterNumber()+"/n"+meter_units+"/n"+ir_curkvah+"/n"
//                                                +rmd, BillingMenuActivity.class);
                                        Btn_nextIR_IRDA.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent i = new Intent(getBaseContext(), Billing_SearchBy_Activity.class);
                                                i.putExtra("SearchType", "Automatic");
                                                i.putExtra("IRDAVO", (Serializable) irdaVO);
                                                startActivity(i);
                                                finish();
                                            }
                                        });

                                    }
                                }else{
                                    new PlayAudio().playFail(getApplicationContext());
                                    display.setText("\n------------------------\n IRDA Three Phase \n Reading issue, try Again.\n");
                                    Toast.makeText(USB_Activity.this, "Try Again. ", Toast.LENGTH_LONG).show();

                                    numberOfTries++;
                                    switch (numberOfTries){
                                        case 1:
                                            Toast.makeText(USB_Activity.this, "3 Tries left", Toast.LENGTH_SHORT).show();
                                            break;
                                        case 2:
                                            Toast.makeText(USB_Activity.this, "2 Tries left", Toast.LENGTH_SHORT).show();
                                            break;
                                        case 3:
                                            Toast.makeText(USB_Activity.this, "1 try left ", Toast.LENGTH_SHORT).show();
                                            break;
                                        case 4:
                                            Toast.makeText(USB_Activity.this, "you have tried too many times", Toast.LENGTH_LONG).show();
                                            AlertDialog.Builder builder1 = new AlertDialog.Builder(USB_Activity.this);
                                            builder1.setTitle("Alert");
                                            builder1.setMessage("Do you want Proceed to Billing with ImageBilling");
                                            builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent(USB_Activity.this, Billing_SearchBy_Activity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            });
                                            builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    numberOfTries=0;
                                                }
                                            });
                                            AlertDialog alert = builder1.create();
                                            alert.show();

                                            if (!isFinishing()){
                                                alert.show();
                                            }
                                            break;
                                        default:
                                            numberOfTries = 0;
                                    }
                                }
                            }else{
                                new PlayAudio().playFail(getApplicationContext());
                                display.setText("\n------------------------\n IRDA Three Phase \n Abnormal Readings received, try Again.\n");
                                Toast.makeText(USB_Activity.this, "Try Again. ", Toast.LENGTH_LONG).show();

                                numberOfTries++;
                                switch (numberOfTries){
                                    case 1:
                                        Toast.makeText(USB_Activity.this, "3 Tries left", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 2:
                                        Toast.makeText(USB_Activity.this, "2 Tries left", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 3:
                                        Toast.makeText(USB_Activity.this, "1 try left ", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 4:
                                        Toast.makeText(USB_Activity.this, "you have tried too many times", Toast.LENGTH_LONG).show();
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(USB_Activity.this);
                                        builder1.setTitle("Alert");
                                        builder1.setMessage("Do you want Proceed to Billing with ImageBilling");
                                        builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(USB_Activity.this, Billing_SearchBy_Activity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                        builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                numberOfTries=0;
                                            }
                                        });
                                        AlertDialog alert = builder1.create();
                                        alert.show();

                                        if (!isFinishing()){
                                            alert.show();
                                        }
                                        break;
                                    default:
                                        numberOfTries = 0;
                                }
                            }
                        }
                    }else{
                        new PlayAudio().playFail(getApplicationContext());
                        display.setText("\n------------------------\n IRDA Three Phase \n MeterNo invalid, try Again.\n");
                        Toast.makeText(USB_Activity.this, "Try Again. ", Toast.LENGTH_LONG).show();
                        numberOfTries++;
                        switch (numberOfTries){
                            case 1:
                                Toast.makeText(USB_Activity.this, "3 Tries left", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                Toast.makeText(USB_Activity.this, "2 Tries left", Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                Toast.makeText(USB_Activity.this, "1 try left ", Toast.LENGTH_SHORT).show();
                                break;
                            case 4:
                                Toast.makeText(USB_Activity.this, "you have tried too many times", Toast.LENGTH_LONG).show();
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(USB_Activity.this);
                                builder1.setTitle("Alert");
                                builder1.setMessage("Do you want Proceed to Billing with ImageBilling");
                                builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(USB_Activity.this, Billing_SearchBy_Activity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                                builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        numberOfTries=0;
                                    }
                                });
                                AlertDialog alert = builder1.create();
                                alert.show();

                                if (!isFinishing()){
                                    alert.show();
                                }
                                break;
                            default:
                                numberOfTries = 0;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(USB_Activity.this, "Try Again. ", Toast.LENGTH_LONG).show();
                    display.setText("Please place the device correctly in front of meter");
                    new PlayAudio().playFail(getApplicationContext());
                    numberOfTries++;
                    switch (numberOfTries){
                        case 1:
                            Toast.makeText(USB_Activity.this, "3 Tries left", Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            Toast.makeText(USB_Activity.this, "2 Tries left", Toast.LENGTH_SHORT).show();
                            break;
                        case 3:
                            Toast.makeText(USB_Activity.this, "1 try left ", Toast.LENGTH_SHORT).show();
                            break;
                        case 4:
                            Toast.makeText(USB_Activity.this, "you have tried too many times", Toast.LENGTH_LONG).show();
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(USB_Activity.this);
                            builder1.setTitle("Alert");
                            builder1.setMessage("Do you want Proceed to Billing with ImageBilling");
                            builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(USB_Activity.this, Billing_SearchBy_Activity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                            builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    numberOfTries=0;
                                }
                            });
                            AlertDialog alert = builder1.create();
                            alert.show();

                            if (!isFinishing()){
                                alert.show();
                            }
                            break;
                        default:
                            numberOfTries = 0;
                    }
                }
            }
        });

        //New 3PH IRDA 2500millisec.
        Btn_3PH_IRDA_Send.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {

                isIr3P = false;
                isIr1P = false;
                isIrda1P = false;
                isIrda3P = true;

                display.setText("IRDA Three Phase selected");
                display.setText("\n");
                display.setText("Meter being read, please wait");
                display.setText("\n");

                AnalogicsUtil util = new AnalogicsUtil();
                util.setIRDABaudRate3P(usbService);
                usbService.sb = new StringBuilder();

                try{
                    byte[] buffer = new byte[11];
                    // 95 95 FF FF FF 0B 96 31 11 05 00
                    buffer[0] = (byte) 0x95;
                    buffer[1] = (byte) 0x95;
                    buffer[2] = (byte) 0xFF;
                    buffer[3] = (byte) 0xFF;
                    buffer[4] = (byte) 0xFF;
                    buffer[5] = 0x0B;
                    buffer[6] = (byte) 0x96;
                    buffer[7] = 0x31;
                    buffer[8] = 0x11;
                    buffer[9] = 0x05;
                    buffer[10] = 0x00;
                    try{
                        if (usbService != null) { // if UsbService was correctly binded, Send data
                            usbService.write(buffer);
                            Thread.sleep(2500);
                        }
                    }catch(Exception ex){

                    }
                    String outputBuff = usbService.sb.toString();

                    if (outputBuff.length() > 42) {
                        outputBuff = outputBuff.substring(22);

                        String hexMeterNumber = outputBuff.substring(18, 20) + outputBuff.substring(16, 18) + outputBuff.substring(14, 16);
                        String meterNumber = Integer.parseInt(hexMeterNumber, 16) + "";

                        HexSupport hex = new HexSupport();
                        usbService.sb = new StringBuilder();
                        byte[] buffer1 = new byte[3];
                        buffer1[0] = (byte) util.hexStringToByteArray(outputBuff.substring(14, 16))[0];
                        buffer1[1] = (byte) util.hexStringToByteArray(outputBuff.substring(16, 18))[0];
                        buffer1[2] = (byte) util.hexStringToByteArray(outputBuff.substring(18, 20))[0];

                        byte[] buffer2 = new byte[11];
                        // 95 95 8A 94 32 0B 00 31 11 05 00  kwh/kvah/rmd
                        buffer2[0] = (byte) 0x95;
                        buffer2[1] = (byte) 0x95;
                        buffer2[2] = buffer1[0];
                        buffer2[3] = buffer1[1];
                        buffer2[4] = buffer1[2];
                        buffer2[5] = 0x0B;//
                        buffer2[6] = 0x00;// 0x01 for solar kwh
                        buffer2[7] = 0x31;
                        buffer2[8] = 0x11;
                        buffer2[9] = 0x05;
                        buffer2[10] = 0x00;

                        try{
                            if (usbService != null) { // if UsbService was correctly binded, Send data
                                usbService.write(buffer2);
                                Thread.sleep(2500);
                            }
                        }catch(Exception ex){

                        }

                        String outputBuff1 = usbService.sb.toString();
                        if (outputBuff1.length() > 42) {
                            outputBuff1 = outputBuff1.substring(22);
                            String VR = outputBuff1.substring(34, 36) + outputBuff1.substring(32, 34);
                            String voltage_R_VR = (Integer.parseInt(VR, 16) / 10.00) + "";

                            String VY = outputBuff1.substring(38, 40) + outputBuff1.substring(36, 38);
                            String voltage_Y_VY = (Integer.parseInt(VY, 16) / 10.00) + "";

                            String VB = outputBuff1.substring(42, 44) + outputBuff1.substring(40, 42);
                            String voltage_B_VB = (Integer.parseInt(VB, 16) / 10.00) + "";

                            String IR = outputBuff1.substring(46, 48) + outputBuff1.substring(44, 46);
                            String current_R_IR = (Integer.parseInt(IR, 16) / 100.00) + "";

                            String IY = outputBuff1.substring(50, 52) + outputBuff1.substring(48, 50);
                            String current_Y_IY = (Integer.parseInt(IY, 16) / 100.00) + "";

                            String IB = outputBuff1.substring(54, 56) + outputBuff1.substring(52, 54);
                            String current_B_IB = (Integer.parseInt(IB, 16) / 100.00) + "";

                            String CUMULATIVE_KWH = outputBuff1.substring(70, 72) +
                                    outputBuff1.substring(68, 70) + outputBuff1.substring(66, 68) +
                                    outputBuff1.substring(64, 66);
                            String meter_units = (Integer.parseInt(CUMULATIVE_KWH, 16) / 100.00) + "";

                            String CUMULATIVE_KVAH = outputBuff1.substring(94, 96) +
                                    outputBuff1.substring(92, 94) + outputBuff1.substring(90, 92) +
                                    outputBuff1.substring(88, 90);
                            String ir_curkvah = (Integer.parseInt(CUMULATIVE_KVAH, 16) / 100.00) + "";

                            String hv_rmd = outputBuff1.substring(98, 100) + outputBuff1.substring(96, 98);
                            String rmd = (Integer.parseInt(hv_rmd, 16) / 100.00) + "";

                            boolean isValid = true;
                            try{
                                double rmdDouble = Double.parseDouble(rmd);
                                if(rmdDouble > 50)
                                    isValid = false;
                            }catch(Exception ex){

                            }

                            String meterMake = "";
                            try{
                                String meterMakeHex = meterData.substring(114, 116) + meterData.substring(112, 114) + meterData.substring(110, 112);
                                meterMake = new com.analogics.utils.HexStringConverter().hexToString(meterMakeHex);

                            }catch(Exception ex){

                            }
                            display.setText("\n------------------------\n         IRDA 3 Phase " +
                                    "\nMeterNo   :  " + meterNumber);
                            display.append("\nKWH       :  " + meter_units +
                                    "\nKVAH      :  " + ir_curkvah +
                                    "\nhv_rmd    : " + rmd +
//                                    "\n***********************************" +
//                                    "\nVoltage_R_VR  : " + voltage_R_VR +
//                                    "\nVoltages_Y_VY : " + voltage_Y_VY +
//                                    "\nVoltage_B_VB   : " + voltage_B_VB +
//                                    "\nCurrent_R_IR  : " + current_R_IR +
//                                    "\nCurrent_Y_IY  : " + current_Y_IY +
//                                    "\nCurrent_B_IB  : " + current_B_IB +
                                    "\n------------------------\n");

                            if(isValid){
                                IrdaVO irdaVO = new IrdaVO();
                                irdaVO.setMeterNumber(meterNumber);
                                irdaVO.setVoltage_R_VR(Double.parseDouble(voltage_R_VR));
                                irdaVO.setVoltage_Y_VY(Double.parseDouble(voltage_Y_VY));
                                irdaVO.setVoltage_B_VB(Double.parseDouble(voltage_B_VB));
                                irdaVO.setCurrent_R_IR(Double.parseDouble(current_R_IR));
                                irdaVO.setCurrent_Y_IY(Double.parseDouble(current_Y_IY));
                                irdaVO.setCurrent_B_IB(Double.parseDouble(current_B_IB));
                                irdaVO.setMeterMake(meterMake);

                                irdaVO.setKWH(Double.parseDouble(meter_units));
                                irdaVO.setKVAH(Double.parseDouble(ir_curkvah));
                                irdaVO.setHv_rmd(Double.parseDouble(rmd));
                                irdaVO.setIrdaReadingFlag(1);
                                new   PlayAudio().playSuccess(getApplicationContext());
//                            Thread.sleep(10);
                                if(irdaVO != null){
                                    dbAdapter = DBAdapter.getDBAdapterInstance(USB_Activity.this);
                                    dbAdapter.openDataBase();
                                    String query = "select * from INPUT_MASTER where trim(printf('%08d',meter_num)) like '%" + irdaVO.getMeterNumber() + "" + "%';";
                                    if(irdaVO.getMeterNumber().length() > 8){
                                        query = "select * from INPUT_MASTER where trim(printf('%s',meter_num)) like '%" + irdaVO.getMeterNumber() + "" + "%';";
                                    }
                                    readrecord = dbAdapter.selectRecordsFromDB(query, null);

                                    if (readrecord.moveToFirst() && !irdaVO.getMeterNumber().trim().isEmpty()) {
                                        Btn_nextIR_IRDA.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(USB_Activity.this, Search_By_NameActivity.class);
                                                intent.putExtra("SearchType", "Automatic");
                                                intent.putExtra("IRDAVO", (Serializable) irdaVO);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });

                                    } else {
                                        new PlayAudio().playFail(getApplicationContext());
                                        display.setText("\n IRDA Three Phase \n MeterNo not found " + irdaVO.getMeterNumber() + "\n");
                                        display.append("KWH             : " + meter_units +
                                                "\nKVAH                 : " + ir_curkvah +
                                                "\nhv_rmd               : " + rmd );
                                        Toast.makeText(USB_Activity.this, "Try Again. ", Toast.LENGTH_LONG).show();
//                                    alertDialog(irdaVO, "Meter Not found.", BillingMenuActivity.class);
                                        Btn_nextIR_IRDA.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent i = new Intent(getBaseContext(), Billing_SearchBy_Activity.class);
                                                i.putExtra("SearchType", "Automatic");
                                                i.putExtra("IRDAVO", (Serializable) irdaVO);
                                                startActivity(i);
                                                finish();
                                            }
                                        });

                                    }
                                }else{
                                    new PlayAudio().playFail(getApplicationContext());
                                    display.setText("\n------------------------\n IRDA Three Phase \n Reading issue, try Again.\n");
                                    Toast.makeText(USB_Activity.this, "Try Again. ", Toast.LENGTH_LONG).show();
                                }
                            }else{
                                new PlayAudio().playFail(getApplicationContext());
                                display.setText("\n------------------------\n IRDA Three Phase \n Abnormal Readings received, try Again.\n");
                                Toast.makeText(USB_Activity.this, "Try Again. ", Toast.LENGTH_LONG).show();
                            }
                        }
                    }else{
                        new PlayAudio().playFail(getApplicationContext());
                        display.setText("\n------------------------\n IRDA Three Phase \n MeterNo invalid, try Again.\n");
                        Toast.makeText(USB_Activity.this, "Try Again. ", Toast.LENGTH_LONG).show();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(USB_Activity.this, "Try Again. ", Toast.LENGTH_LONG).show();
                    display.setText("Please place the device correctly in front of meter");
                    new PlayAudio().playFail(getApplicationContext());
                }
            }
        });

        Btn_IRDA_1Phase_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isIr3P = false;
                isIr1P = false;
                isIrda1P = true;
                isIrda3P = false;

                display.setText("IRDA Single Phase selected");
                new AnalogicsUtil().setIRDABaudRate1P(usbService);
                GetMeterDataAsyncTask w = new GetMeterDataAsyncTask();
                w.execute();
            }
        });


        Btn_IRDA_1Phase_Delay_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isIr3P = false;
                isIr1P = false;
                isIrda1P = false;
                isIrda3P = false;
                isIrda1PDelay = true;

                display.setText("IRDA Single Phase selected");
                new AnalogicsUtil().setIRDABaudRate1P(usbService);
                GetMeterDataAsyncTask w = new GetMeterDataAsyncTask();
                w.execute();
            }
        });
    }

    private class GetMeterDataAsyncTask extends AsyncTask<String, String, String> {
        ProgressDialog p;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(USB_Activity.this);
            p.setMessage("Please wait...Getting Meter Data");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            meterData = "";
            if (usbService != null)
                usbService.finalData = "";
            try {
                if (isIrda1P) {
                    usbService.sb = new StringBuilder();
                    byte[] buffer1 = new byte[11];
                    //  3A 30 30 34 31 33 42 43 34 0D 0A  //single phase meternumber
                    buffer1[0] = 0x3A;
                    buffer1[1] = 0x30;
                    buffer1[2] = 0x30;
                    buffer1[3] = 0x34;
                    buffer1[4] = 0x31;
                    buffer1[5] = 0x33;
                    buffer1[6] = 0x42;
                    buffer1[7] = 0x43;
                    buffer1[8] = 0x34;
                    buffer1[9] = 0x0D;
                    buffer1[10] = 0x0A;

                    try{
                        if (usbService != null) { // if UsbService was correctly binded, Send data
                            usbService.write(buffer1);
                            Thread.sleep(500);
                        }
                    }catch(Exception ex){

                    }

                    buffer1 = new byte[11];
                    buffer1[0] = 0x3A;
                    buffer1[1] = 0x30;
                    buffer1[2] = 0x30;
                    buffer1[3] = 0x34;
                    buffer1[4] = 0x33;
                    buffer1[5] = 0x33;
                    buffer1[6] = 0x39;
                    buffer1[7] = 0x43;
                    buffer1[8] = 0x36;
                    buffer1[9] = 0x0D;
                    buffer1[10] = 0x0A;

                    try{
                        if (usbService != null) { // if UsbService was correctly binded, Send data
                            usbService.write(buffer1);
                            Thread.sleep(500);
                        }
                    }catch(Exception ex){

                    }

                    buffer1[0] = 0x3A;
                    buffer1[1] = 0x30;
                    buffer1[2] = 0x30;
                    buffer1[3] = 0x34;
                    buffer1[4] = 0x36;
                    buffer1[5] = 0x33;
                    buffer1[6] = 0x36;
                    buffer1[7] = 0x43;
                    buffer1[8] = 0x39;
                    buffer1[9] = 0x0D;
                    buffer1[10] = 0x0A;

                    try{
                        if (usbService != null) { // if UsbService was correctly binded, Send data
                            usbService.write(buffer1);
                            Thread.sleep(500);
                        }
                    }catch(Exception ex){

                    }
                    buffer1[0] = 0x3A;
                    buffer1[1] = 0x30;
                    buffer1[2] = 0x30;
                    buffer1[3] = 0x34;
                    buffer1[4] = 0x32;
                    buffer1[5] = 0x33;
                    buffer1[6] = 0x41;
                    buffer1[7] = 0x43;
                    buffer1[8] = 0x35;
                    buffer1[9] = 0x0D;
                    buffer1[10] = 0x0A;

                    try{
                        if (usbService != null) { // if UsbService was correctly binded, Send data
                            usbService.write(buffer1);
                            Thread.sleep(500);
                        }
                    }catch(Exception ex){

                    }
                }else if (isIrda1PDelay) {
                    usbService.sb = new StringBuilder();
                    byte[] buffer1 = new byte[11];
                    //  3A 30 30 34 31 33 42 43 34 0D 0A  //single phase meternumber
                    buffer1[0] = 0x3A;
                    buffer1[1] = 0x30;
                    buffer1[2] = 0x30;
                    buffer1[3] = 0x34;
                    buffer1[4] = 0x31;
                    buffer1[5] = 0x33;
                    buffer1[6] = 0x42;
                    buffer1[7] = 0x43;
                    buffer1[8] = 0x34;
                    buffer1[9] = 0x0D;
                    buffer1[10] = 0x0A;

                    try{
                        if (usbService != null) { // if UsbService was correctly binded, Send data
                            usbService.write(buffer1);
                            Thread.sleep(1000);
                        }
                    }catch(Exception ex){

                    }

                    buffer1 = new byte[11];
                    buffer1[0] = 0x3A;
                    buffer1[1] = 0x30;
                    buffer1[2] = 0x30;
                    buffer1[3] = 0x34;
                    buffer1[4] = 0x33;
                    buffer1[5] = 0x33;
                    buffer1[6] = 0x39;
                    buffer1[7] = 0x43;
                    buffer1[8] = 0x36;
                    buffer1[9] = 0x0D;
                    buffer1[10] = 0x0A;

                    try{
                        if (usbService != null) { // if UsbService was correctly binded, Send data
                            usbService.write(buffer1);
                            Thread.sleep(1000);
                        }
                    }catch(Exception ex){

                    }

                    buffer1[0] = 0x3A;
                    buffer1[1] = 0x30;
                    buffer1[2] = 0x30;
                    buffer1[3] = 0x34;
                    buffer1[4] = 0x36;
                    buffer1[5] = 0x33;
                    buffer1[6] = 0x36;
                    buffer1[7] = 0x43;
                    buffer1[8] = 0x39;
                    buffer1[9] = 0x0D;
                    buffer1[10] = 0x0A;

                    try{
                        if (usbService != null) { // if UsbService was correctly binded, Send data
                            usbService.write(buffer1);
                            Thread.sleep(1000);
                        }
                    }catch(Exception ex){

                    }
                    buffer1[0] = 0x3A;
                    buffer1[1] = 0x30;
                    buffer1[2] = 0x30;
                    buffer1[3] = 0x34;
                    buffer1[4] = 0x32;
                    buffer1[5] = 0x33;
                    buffer1[6] = 0x41;
                    buffer1[7] = 0x43;
                    buffer1[8] = 0x35;
                    buffer1[9] = 0x0D;
                    buffer1[10] = 0x0A;

                    try{
                        if (usbService != null) { // if UsbService was correctly binded, Send data
                            usbService.write(buffer1);
                            Thread.sleep(1000);
                        }
                    }catch(Exception ex){

                    }
                }else if (isIr1P) {
                    usbService.sb = new StringBuilder();
                    byte[] buffer1 = new byte[11];
                    //  3A 30 30 34 31 33 42 43 34 0D 0A  //single phase meternumber
                    buffer1[0] = 0x3A;
                    buffer1[1] = 0x30;
                    buffer1[2] = 0x30;
                    buffer1[3] = 0x34;
                    buffer1[4] = 0x31;
                    buffer1[5] = 0x33;
                    buffer1[6] = 0x42;
                    buffer1[7] = 0x43;
                    buffer1[8] = 0x34;
                    buffer1[9] = 0x0D;
                    buffer1[10] = 0x0A;

                    try{
                        if (usbService != null) { // if UsbService was correctly binded, Send data
                            usbService.write(buffer1);
                            Thread.sleep(200);
                        }
                    }catch(Exception ex){

                    }

                    buffer1 = new byte[11];
                    buffer1[0] = 0x3A;
                    buffer1[1] = 0x30;
                    buffer1[2] = 0x30;
                    buffer1[3] = 0x34;
                    buffer1[4] = 0x33;
                    buffer1[5] = 0x33;
                    buffer1[6] = 0x39;
                    buffer1[7] = 0x43;
                    buffer1[8] = 0x36;
                    buffer1[9] = 0x0D;
                    buffer1[10] = 0x0A;

                    try{
                        if (usbService != null) { // if UsbService was correctly binded, Send data
                            usbService.write(buffer1);
                            Thread.sleep(200);
                        }
                    }catch(Exception ex){

                    }

                    buffer1[0] = 0x3A;
                    buffer1[1] = 0x30;
                    buffer1[2] = 0x30;
                    buffer1[3] = 0x34;
                    buffer1[4] = 0x36;
                    buffer1[5] = 0x33;
                    buffer1[6] = 0x36;
                    buffer1[7] = 0x43;
                    buffer1[8] = 0x39;
                    buffer1[9] = 0x0D;
                    buffer1[10] = 0x0A;

                    try{
                        if (usbService != null) { // if UsbService was correctly binded, Send data
                            usbService.write(buffer1);
                            Thread.sleep(200);
                        }
                    }catch(Exception ex){

                    }
                    buffer1[0] = 0x3A;
                    buffer1[1] = 0x30;
                    buffer1[2] = 0x30;
                    buffer1[3] = 0x34;
                    buffer1[4] = 0x32;
                    buffer1[5] = 0x33;
                    buffer1[6] = 0x41;
                    buffer1[7] = 0x43;
                    buffer1[8] = 0x35;
                    buffer1[9] = 0x0D;
                    buffer1[10] = 0x0A;

                    try{
                        if (usbService != null) { // if UsbService was correctly binded, Send data
                            usbService.write(buffer1);
                            Thread.sleep(200);
                        }
                    }catch(Exception ex){

                    }
                }else if(isIr3P){
                    usbService.sb = new StringBuilder();

                    byte[] buffer1 = new byte[5];
                    buffer1[0] = (byte) 0XB9;
                    buffer1[1] = (byte) 0X9E;
                    buffer1[2] = (byte) 0X8E;
                    buffer1[3] = 0X7E;
                    buffer1[4] = 0X1E;
                    try{
                        if (usbService != null) { // if UsbService was correctly binded, Send data
                            usbService.write(buffer1);
                            Thread.sleep(100);
                        }
                    }catch(Exception ex){

                    }
                    usbService.sb = new StringBuilder();
                    buffer1[0] = (byte) 0XB9;
                    buffer1[1] = (byte) 0X9E;
                    buffer1[2] = (byte) 0X8E;
                    buffer1[3] = 0X7E;
                    buffer1[4] = 0X1E;
                    try{
                        if (usbService != null) { // if UsbService was correctly binded, Send data
                            usbService.write(buffer1);
                            Thread.sleep(1000);
                        }
                    }catch(Exception ex){

                    }
                }else if(isIrda3P){

                    usbService.sb = new StringBuilder();
                    byte[] buffer = new byte[11];
                    // 95 95 FF FF FF 0B 96 31 11 05 00
                    buffer[0] = (byte) 0x95;
                    buffer[1] = (byte) 0x95;
                    buffer[2] = (byte) 0xFF;
                    buffer[3] = (byte) 0xFF;
                    buffer[4] = (byte) 0xFF;
                    buffer[5] = 0x0B;
                    buffer[6] = (byte) 0x96;
                    buffer[7] = 0x31;
                    buffer[8] = 0x11;
                    buffer[9] = 0x05;
                    buffer[10] = 0x00;
                    try{
                        if (usbService != null) { // if UsbService was correctly binded, Send data
                            usbService.write(buffer);
                            Thread.sleep(3000);
                        }
                    }catch(Exception ex){

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (usbService != null) {
                meterData = usbService.sb.toString();
            }
            return meterData;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String bitmap) {

            if (!isFinishing())
                p.dismiss();
            Log.e("TAG", "onPostExecute: " + bitmap);

            try {
                if (meterData.length() > 0) {
                    IrdaVO irdaVO = null;
                    if (isIr1P || isIrda1P || isIrda1PDelay) {
                        String[] data;
                        if(StringUtils.contains(meterData, "0D0A")){
                            data = meterData.split("0D0A");
                        }else{
                            data = meterData.split("0d0a");
                        }

                        if (meterData.length() >= 32) {
                            if (data.length >= 1) {
                                boolean isValid = true;
                                HexSupport hex = new HexSupport();

                                String MeterNumber = "";
                                String kwh = "0", kvah = "0", rmd = "0", meterMake = "";
                                if(isIr1P)
                                    MeterNumber = (hex.hexToAscii(data[1].substring(10, 26))) + "";
                                else if(isIrda1P)
                                    MeterNumber = hex.hexToAscii(data[1].substring(10, data[1].length() - 8));
                                else if(isIrda1PDelay)
                                    MeterNumber = hex.hexToAscii(data[1].substring(10, data[1].length() - 8));

                                try{
                                    kwh=(hex.hexToAscii(data[3].substring(10, 28)))+ "";
                                }catch(Exception ex){

                                }
                                try{
                                    kvah = (hex.hexToAscii(data[3].substring(10, 28)))+ "";
                                }catch(Exception ex){

                                }
                                try{
                                    rmd = (hex.hexToAscii(data[5].substring(10, 20))) + "";
                                    double rmdDbl = Double.parseDouble(rmd);
                                    if(rmdDbl > 50)
                                        isValid = false;
                                }catch(Exception ex){

                                }

                                try{
                                    meterMake = (hex.hexToAscii(data[7].substring(10, 42))) + "";
                                }catch(Exception ex){

                                }

                                if(isIr1P){
                                    display.setText("IR - Single Phase Data \n" +
                                            "Meter No     :  " + MeterNumber + "\n" +
                                            "kwh          :  " + kwh + "\n" +
                                            //"kvah         :  " + kvah + "\n" +
                                            "rmd          :  " + rmd + "\n"
                                    );
                                }else{
                                    display.setText("IRDA - Single Phase Data \n" +
                                            "Meter No     :  " + MeterNumber + "\n" +
                                            "kwh          :  " + kwh + "\n" +
                                            //"kvah         :  " + kvah + "\n" +
                                            "rmd          :  " + rmd + "\n"
                                    );
                                }

                                if(isValid){
                                    irdaVO = new IrdaVO();
                                    irdaVO.setMeterNumber(MeterNumber);
                                    irdaVO.setKWH(Double.parseDouble(kwh));
                                    irdaVO.setKVAH(Double.parseDouble(kvah));
                                    irdaVO.setHv_rmd(Double.parseDouble(rmd));
                                    irdaVO.setMeterMake(meterMake);
                                    irdaVO.setIrdaReadingFlag(1);
                                    new   PlayAudio().playSuccess(getApplicationContext());
//                                    Thread.sleep(10);
                                }else{
                                    new PlayAudio().playFail(getApplicationContext());
                                    if(isIr1P)
                                        display.setText("\n------------------------\n IR Single Phase \n Invalid readings received, try Again.\n");
                                    else
                                        display.setText("\n------------------------\n IRDA Single Phase \n Invalid readings received, try Again.\n");
                                    Toast.makeText(USB_Activity.this, "Try Again. ", Toast.LENGTH_LONG).show();
                                }


                            } else {
                                new PlayAudio().playFail(getApplicationContext());
                                if(isIr1P)
                                    display.setText("\n------------------------\n IR Single Phase \n MeterNo invalid, try Again.\n");
                                else
                                    display.setText("\n------------------------\n IRDA Single Phase \n MeterNo invalid, try Again.\n");
                                Toast.makeText(USB_Activity.this, "Try Again. ", Toast.LENGTH_LONG).show();

                                numberOfTries++;
                                switch (numberOfTries){
                                    case 1:
                                        Toast.makeText(USB_Activity.this, "3 Tries left", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 2:
                                        Toast.makeText(USB_Activity.this, "2 Tries left", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 3:
                                        Toast.makeText(USB_Activity.this, "1 try left ", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 4:
                                        Toast.makeText(USB_Activity.this, "you have tried too many times", Toast.LENGTH_LONG).show();
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(USB_Activity.this);
                                        builder1.setTitle("Alert");
                                        builder1.setMessage("Do you want Proceed to Billing with ImageBilling");
                                        builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(USB_Activity.this, Billing_SearchBy_Activity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                        builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                numberOfTries=0;
                                            }
                                        });
                                        AlertDialog alert = builder1.create();
                                        alert.show();

                                        if (!isFinishing()){
                                            alert.show();
                                        }
                                        break;
                                    default:
                                        numberOfTries = 0;
                                }
                            }
                        } else {
                            new PlayAudio().playFail(getApplicationContext());
                            if(isIr1P)
                                display.setText("\n------------------------\n IR Single Phase \n MeterNo invalid, try Again.\n");
                            else
                                display.setText("\n------------------------\n IRDA Single Phase \n MeterNo invalid, try Again.\n");

                            Toast.makeText(USB_Activity.this, "Try Again. ", Toast.LENGTH_LONG).show();
                            numberOfTries++;
                            switch (numberOfTries){
                                case 1:
                                    Toast.makeText(USB_Activity.this, "3 Tries left", Toast.LENGTH_SHORT).show();
                                    break;
                                case 2:
                                    Toast.makeText(USB_Activity.this, "2 Tries left", Toast.LENGTH_SHORT).show();
                                    break;
                                case 3:
                                    Toast.makeText(USB_Activity.this, "1 try left ", Toast.LENGTH_SHORT).show();
                                    break;
                                case 4:
                                    Toast.makeText(USB_Activity.this, "you have tried too many times", Toast.LENGTH_LONG).show();
                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(USB_Activity.this);
                                    builder1.setTitle("Alert");
                                    builder1.setMessage("Do you want Proceed to Billing with ImageBilling");
                                    builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(USB_Activity.this, Billing_SearchBy_Activity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                    builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            numberOfTries=0;
                                        }
                                    });
                                    AlertDialog alert = builder1.create();
                                    alert.show();

                                    if (!isFinishing()){
                                        alert.show();
                                    }
                                    break;
                                default:
                                    numberOfTries = 0;
                            }
                        }
                    }else if(isIr3P){
                        String outputBuff = meterData;
                        boolean isValid = true;
                        String MeterNumber = "";
                        try{
                            MeterNumber = Integer.parseInt(outputBuff.substring(12, 20), 16) + "";
                        }catch(Exception ex){

                        }

                        String KWH = "";
                        try{
                            KWH = new DecimalFormat("##.##").format((double) (Integer.parseInt(outputBuff.substring(30, 38), 16)) / 1000) + "";
                        }catch(Exception ex){

                        }
                        String KvaH = "";
                        try{
                            KvaH = new DecimalFormat("##.##").format((double) (Integer.parseInt(outputBuff.substring(54, 62), 16)) / 1000) + "";
                        }catch(Exception ex){

                        }

                        String rmd = "";
                        try{
                            rmd = new DecimalFormat("##.##").format((double) (Integer.parseInt(outputBuff.substring(64, 68), 16)) / 1000) + "";
                            double rmdDouble = Double.parseDouble(rmd);
                            if(rmdDouble > 50)
                                isValid = false;
                        }catch(Exception ex){

                        }
                        display.setText("IR Three Phase Data \n" +
                                "Meter No     :  " + MeterNumber + "\n" +
                                "kwh          :  " + KWH + "\n" +
                                "kvah         :  " + KvaH + "\n" +
                                "rmd          :  " + rmd + "\n"
                        );

                        if(isValid){
                            irdaVO = new IrdaVO();
                            irdaVO.setMeterNumber(MeterNumber);
                            irdaVO.setKWH(Double.parseDouble(KWH));
                            irdaVO.setKVAH(Double.parseDouble(KvaH));
                            irdaVO.setHv_rmd(Double.parseDouble(rmd));
                            irdaVO.setMeterMake("");
                            irdaVO.setIrdaReadingFlag(1);
                            new   PlayAudio().playSuccess(getApplicationContext());
//                            Thread.sleep(10);
                        }else{
                            new PlayAudio().playFail(getApplicationContext());
                            display.setText("\n------------------------\n IR Three Phase \n Invalid Readings received, try Again.\n");
                            Toast.makeText(USB_Activity.this, "Try Again. ", Toast.LENGTH_LONG).show();
                        }

                    }else if(isIrda3P){
                        AnalogicsUtil util = new AnalogicsUtil();
                        display.setText("       3 Phase Data >>> \n" + meterData + "\n");
                        meterData = meterData.substring(22);
                        String hexMeterNumber = meterData.substring(18, 20) + meterData.substring(16, 18) + meterData.substring(14, 16);
                        String meterNumber = Integer.parseInt(hexMeterNumber, 16) + "";
                        display.setText("       3 Phase \nMeterNo >>> " + meterNumber + "\n");
                        byte[] buffer1 = new byte[3];
                        buffer1[0] = (byte) util.hexStringToByteArray(meterData.substring(14, 16))[0];
                        buffer1[1] = (byte) util.hexStringToByteArray(meterData.substring(16, 18))[0];
                        buffer1[2] = (byte) util.hexStringToByteArray(meterData.substring(18, 20))[0];
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        USB_Activity.Get_3PhaseMeterDataAsyncTask w = new USB_Activity.Get_3PhaseMeterDataAsyncTask();
                        w.execute(buffer1);
                    }

                    if(irdaVO != null){
                        dbAdapter = DBAdapter.getDBAdapterInstance(USB_Activity.this);
                        dbAdapter.openDataBase();
                        String query = "select * from INPUT_MASTER where trim(printf('%08d',meter_num)) like '%" + irdaVO.getMeterNumber() + "" + "%';";
                        if(irdaVO.getMeterNumber().length() > 8){
                            query = "select * from INPUT_MASTER where trim(printf('%s',meter_num)) like '%" + irdaVO.getMeterNumber() + "" + "%';";
                        }
                        readrecord = dbAdapter.selectRecordsFromDB(query, null);

                        if (readrecord.moveToFirst() && !irdaVO.getMeterNumber().trim().isEmpty()) {
                            IrdaVO finalIrdaVO = irdaVO;//srinu
                            Btn_nextIR_IRDA.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(USB_Activity.this, Search_By_NameActivity.class);
                                    intent.putExtra("SearchType", "Automatic");
                                    intent.putExtra("IRDAVO", (Serializable) finalIrdaVO);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                        } else {
                            new PlayAudio().playFail(getApplicationContext());
                            if(isIr1P)
                                display.setText("\n------------------------\n IR Single Phase \n Meter Number Not Found " + irdaVO.getMeterNumber() + "\n"
                                        + "KWH : " + irdaVO.getKWH() + "\n" + "RMD : " + irdaVO.getHv_rmd() + "\n");
                            else if(isIr3P)
                                display.setText("\n------------------------\n IR Three Phase \n Meter Number Not Found " + irdaVO.getMeterNumber() + "\n"
                                        + "KWH : " + irdaVO.getKWH() + "\n" + "RMD : " + irdaVO.getHv_rmd() + "\n" + "KVAH : " + irdaVO.getKVAH() + "\n");
                            else if(isIrda1P)
                                display.setText("\n------------------------\n IRDA Single Phase \n Meter Number Not Found " + irdaVO.getMeterNumber() + "\n"
                                        + "KWH : " + irdaVO.getKWH() + "\n" + "RMD : " + irdaVO.getHv_rmd() + "\n");
                            else if(isIrda1PDelay)
                                display.setText("\n------------------------\n IRDA Single Phase \n Meter Number Not Found " + irdaVO.getMeterNumber() + "\n"
                                        + "KWH : " + irdaVO.getKWH() + "\n" + "RMD : " + irdaVO.getHv_rmd() + "\n");

                            Toast.makeText(USB_Activity.this, "Try Again. ", Toast.LENGTH_LONG).show();
//                            alertDialog(irdaVO, "Meter Number Not Found.", BillingMenuActivity.class);
                            IrdaVO finalIrdaVO1 = irdaVO;
                            Btn_nextIR_IRDA.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent(getBaseContext(), Billing_SearchBy_Activity.class);
                                    i.putExtra("SearchType", "Automatic");
                                    i.putExtra("IRDAVO", (Serializable) finalIrdaVO1);
                                    startActivity(i);
                                    finish();
                                }
                            });


                        }
                    }else{
                        new PlayAudio().playFail(getApplicationContext());
                        if(isIr1P)
                            display.setText("\n------------------------\n IR Single Phase \n Reading issue, try Again.\n");
                        else if(isIr3P)
                            display.setText("\n------------------------\n IR Three Phase \n Reading issue, try Again.\n");
                        else if(isIrda3P)
                            display.setText("\n------------------------\n IRDA Three Phase \n Reading issue, try Again.\n");
                        else if(isIrda1P)
                            display.setText("\n------------------------\n IRDA Single Phase \n Reading issue, try Again.\n");
                        else if(isIrda1PDelay)
                            display.setText("\n------------------------\n IRDA Single Phase \n Reading issue, try Again.\n");
                        Toast.makeText(USB_Activity.this, "Try Again. ", Toast.LENGTH_LONG).show();
                        numberOfTries++;
                        switch (numberOfTries){
                            case 1:
                                Toast.makeText(USB_Activity.this, "3 Tries left", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                Toast.makeText(USB_Activity.this, "2 Tries left", Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                Toast.makeText(USB_Activity.this, "1 try left ", Toast.LENGTH_SHORT).show();
                                break;
                            case 4:
                                Toast.makeText(USB_Activity.this, "you have tried too many times", Toast.LENGTH_LONG).show();
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(USB_Activity.this);
                                builder1.setTitle("Alert");
                                builder1.setMessage("Do you want Proceed to Billing with ImageBilling");
                                builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(USB_Activity.this, Billing_SearchBy_Activity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                                builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        numberOfTries=0;
                                    }
                                });
                                AlertDialog alert = builder1.create();
                                alert.show();

                                if (!isFinishing()){
                                    alert.show();
                                }
                                break;
                            default:
                                numberOfTries = 0;
                        }
                    }
                } else {
                    new PlayAudio().playFail(getApplicationContext());
                    if(isIr1P)
                        display.setText("\n------------------------\n IR Single Phase \n Reading issue, try Again.\n");
                    else if(isIr3P)
                        display.setText("\n------------------------\n IR Three Phase \n Reading issue, try Again.\n");
                    else if(isIrda3P)
                        display.setText("\n------------------------\n IRDA Three Phase \n Reading issue, try Again.\n");
                    else if(isIrda1P)
                        display.setText("\n------------------------\n IRDA Single Phase \n Reading issue, try Again.\n");
                    else if(isIrda1PDelay)
                        display.setText("\n------------------------\n IRDA Single Phase \n Reading issue, try Again.\n");
                    Toast.makeText(USB_Activity.this, "Try Again. ", Toast.LENGTH_LONG).show();
                    numberOfTries++;
                    switch (numberOfTries){
                        case 1:
                            Toast.makeText(USB_Activity.this, "3 Tries left", Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            Toast.makeText(USB_Activity.this, "2 Tries left", Toast.LENGTH_SHORT).show();
                            break;
                        case 3:
                            Toast.makeText(USB_Activity.this, "1 try left ", Toast.LENGTH_SHORT).show();
                            break;
                        case 4:
                            Toast.makeText(USB_Activity.this, "you have tried too many times", Toast.LENGTH_LONG).show();
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(USB_Activity.this);
                            builder1.setTitle("Alert");
                            builder1.setMessage("Do you want Proceed to Billing with ImageBilling");
                            builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(USB_Activity.this, Billing_SearchBy_Activity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                            builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    numberOfTries=0;
                                }
                            });
                            AlertDialog alert = builder1.create();
                            alert.show();

                            if (!isFinishing()){
                                alert.show();
                            }
                            break;
                        default:
                            numberOfTries = 0;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(USB_Activity.this, "Try Again. ", Toast.LENGTH_LONG).show();
                display.setText("Please place the device correctly in front of meter");
                new PlayAudio().playFail(getApplicationContext());

                numberOfTries++;
                switch (numberOfTries){
                    case 1:
                        Toast.makeText(USB_Activity.this, "3 Tries left", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(USB_Activity.this, "2 Tries left", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(USB_Activity.this, "1 try left ", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(USB_Activity.this, "you have tried too many times", Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(USB_Activity.this);
                        builder1.setTitle("Alert");
                        builder1.setMessage("Do you want Proceed to Billing with ImageBilling");
                        builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(USB_Activity.this, Billing_SearchBy_Activity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                        builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                numberOfTries=0;
                            }
                        });
                        AlertDialog alert = builder1.create();
                        alert.show();

                        if (!isFinishing()){
                            alert.show();
                        }
                        break;
                    default:
                        numberOfTries = 0;
                }
            }
        }
    }



    private void alertDialog(IrdaVO irdaVO, String msg, Class<?> navigatedClass) {
        AlertDialog.Builder builder = new AlertDialog.Builder(USB_Activity.this);
        builder.setTitle("Alert");
        builder.setMessage(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(USB_Activity.this, navigatedClass);
                intent.putExtra("SearchType", "Automatic");
                intent.putExtra("IRDAVO", (Serializable) irdaVO);
                startActivity(intent);
                finish();
            }
        });

        AlertDialog alertDialog = builder.create();
        if (!isFinishing()) {
            alertDialog.show();
        }

    }

    private class Get_3PhaseMeterDataAsyncTask extends AsyncTask<byte[], String, String> {
        String phase = "";
        ProgressDialog p;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(USB_Activity.this);
            p.setMessage("Please wait...Getting Meter Data");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(byte[]... bytes) {
            if (usbService != null) { // if UsbService was correctly binded, Send data
                meterData = "";
                usbService.finalData = "";
                try {
                    byte[] buffer1 = new byte[11];
                    // 95 95 8A 94 32 0B 00 31 11 05 00  kwh/kvah/rmd
                    buffer1[0] = (byte) 0x95;
                    buffer1[1] = (byte) 0x95;
                    buffer1[2] = bytes[0][0];
                    buffer1[3] = bytes[0][1];
                    buffer1[4] = bytes[0][2];
                    buffer1[5] = 0x0B;//
                    buffer1[6] = 0x00;// 0x01 for solar kwh
                    buffer1[7] = 0x31;
                    buffer1[8] = 0x11;
                    buffer1[9] = 0x05;
                    buffer1[10] = 0x00;

                    if (usbService != null) { // if UsbService was correctly binded, Send data
                        try {
                            usbService.write(buffer1);
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (usbService != null) {
                meterData = usbService.sb.toString();
            }
            return meterData;
        }

        @Override
        protected void onPostExecute(String bitmap) {
            super.onPostExecute(bitmap);
            p.dismiss();

            try {
                if (meterData.length() > 100) {
                    boolean isValid = true;
                    System.out.println("Received meterData2  ->meterData:" + meterData);
                    //   Toast.makeText(MainActivity.this,meterData, Toast.LENGTH_LONG).show();
                    meterData = meterData.substring(22);
                    String VR = meterData.substring(34, 36) + meterData.substring(32, 34);
                    String voltage_R_VR = (Integer.parseInt(VR, 16) / 10.00) + "";

                    String VY = meterData.substring(38, 40) + meterData.substring(36, 38);
                    String voltage_Y_VY = (Integer.parseInt(VY, 16) / 10.00) + "";

                    String VB = meterData.substring(42, 44) + meterData.substring(40, 42);
                    String voltage_B_VB = (Integer.parseInt(VB, 16) / 10.00) + "";

                    String IR = meterData.substring(46, 48) + meterData.substring(44, 46);
                    String current_R_IR = (Integer.parseInt(IR, 16) / 100.00) + "";

                    String IY = meterData.substring(50, 52) + meterData.substring(48, 50);
                    String current_Y_IY = (Integer.parseInt(IY, 16) / 100.00) + "";

                    String IB = meterData.substring(54, 56) + meterData.substring(52, 54);
                    String current_B_IB = (Integer.parseInt(IB, 16) / 100.00) + "";

                    String CUMULATIVE_KWH = meterData.substring(70, 72) + meterData.substring(68, 70) + meterData.substring(66, 68) + meterData.substring(64, 66);
                    String meter_units = (Integer.parseInt(CUMULATIVE_KWH, 16) / 100.00) + "";

                    String CUMULATIVE_KVAH = meterData.substring(94, 96) + meterData.substring(92, 94) + meterData.substring(90, 92) + meterData.substring(88, 90);
                    String ir_curkvah = (Integer.parseInt(CUMULATIVE_KVAH, 16) / 100.00) + "";

                    String hv_rmd = meterData.substring(98, 100) + meterData.substring(96, 98);
                    String rmd = (Integer.parseInt(hv_rmd, 16) / 100.00) + "";

                    try{
                        double rmdDouble = Double.parseDouble(rmd);
                        if(rmdDouble > 50){
                            isValid = false;
                        }
                    }catch(Exception ex){

                    }
                    //57 56 55
                    String hexMeterNumber = meterData.substring(18, 20) + meterData.substring(16, 18) + meterData.substring(14, 16);
                    String meterNumber = Integer.parseInt(hexMeterNumber, 16) + "";
                    String meterMake = "";
                    try{
                        String meterMakeHex = meterData.substring(114, 116) + meterData.substring(112, 114) + meterData.substring(110, 112);
                        meterMake = new com.analogics.utils.HexStringConverter().hexToString(meterMakeHex);

                    }catch(Exception ex){

                    }
                    //changed 26072023 //nrb
                    display.setText("\n------------------------\n         IRDA 3 Phase " +
                            "\nMeterNo   :  " + meterNumber);
                    display.append("\nKWH       :  " + meter_units +
                            "\nKVAH      :  " + ir_curkvah +
                            "\nhv_rmd    : " + rmd +
                            "\n***********************************" +
                            "\nVoltage_R_VR  : " + voltage_R_VR +
                            "\nVoltages_Y_VY : " + voltage_Y_VY +
                            "\nVoltage_B_VB   : " + voltage_B_VB +
                            "\nCurrent_R_IR  : " + current_R_IR +
                            "\nCurrent_Y_IY  : " + current_Y_IY +
                            "\nCurrent_B_IB  : " + current_B_IB + "\n------------------------\n");


                    if(isValid){
                        IrdaVO irdaVO = new IrdaVO();
                        irdaVO.setMeterNumber(meterNumber);
                        irdaVO.setVoltage_R_VR(Double.parseDouble(voltage_R_VR));
                        irdaVO.setVoltage_Y_VY(Double.parseDouble(voltage_Y_VY));
                        irdaVO.setVoltage_B_VB(Double.parseDouble(voltage_B_VB));
                        irdaVO.setCurrent_R_IR(Double.parseDouble(current_R_IR));
                        irdaVO.setCurrent_Y_IY(Double.parseDouble(current_Y_IY));
                        irdaVO.setCurrent_B_IB(Double.parseDouble(current_B_IB));
                        irdaVO.setMeterMake(meterMake);

                        irdaVO.setKWH(Double.parseDouble(meter_units));
                        irdaVO.setKVAH(Double.parseDouble(ir_curkvah));
                        irdaVO.setHv_rmd(Double.parseDouble(rmd));
                        irdaVO.setIrdaReadingFlag(1);
                        new PlayAudio().playSuccess(getApplicationContext());
//                        Thread.sleep(10);

                        dbAdapter = DBAdapter.getDBAdapterInstance(USB_Activity.this);
                        dbAdapter.openDataBase();
                        String query = "select * from INPUT_MASTER where trim(printf('%08d',meter_num)) like '%" + irdaVO.getMeterNumber() + "" + "%';";
                        readrecord = dbAdapter.selectRecordsFromDB(query, null);

                        if (readrecord.moveToFirst() && !irdaVO.getMeterNumber().trim().isEmpty()) {
                            Btn_nextIR_IRDA.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(USB_Activity.this, Search_By_NameActivity.class); //Search_By_NameActivity
                                    intent.putExtra("IRDAVO", (Serializable) irdaVO);
                                    intent.putExtra("SearchType", "Automatic");
                                    startActivity(intent);
                                    finish();
                                }
                            });

                        } else {
//                            alertDialog(irdaVO, "Meter Not found(1).", BillingMenuActivity.class);
                            Btn_nextIR_IRDA.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent(getBaseContext(), Billing_SearchBy_Activity.class);
                                    i.putExtra("SearchType", "Automatic");
                                    i.putExtra("IRDAVO", (Serializable) irdaVO);
                                    startActivity(i);
                                    finish();
                                }
                            });

                        }
                    }else{
                        display.setText("\n IRDA Three Phase \n Abnormal RMD received " + irdaVO.getHv_rmd() + "\n");
                        Toast.makeText(USB_Activity.this, "Try Again. ", Toast.LENGTH_LONG).show();
//                        alertDialog(irdaVO, "\n IRDA Three Phase \n Abnormal RMD received", BillingMenuActivity.class);
                    }
                } else {
                    display.setText("\n IRDA Three Phase \n MeterNo not found " + irdaVO.getMeterNumber() + "\n");
                    Toast.makeText(USB_Activity.this, "Try Again. ", Toast.LENGTH_LONG).show();
//                       alertDialog(irdaVO, "Meter Number Not Found.", BillingMenuActivity.class);

                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(USB_Activity.this, "Try Again. ", Toast.LENGTH_LONG).show();
                display.setText("Please place the device correctly in front of meter");
                new PlayAudio().playFail(getApplicationContext());
            }
        }
    }

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case UsbService.ACTION_USB_PERMISSION_GRANTED: // USB PERMISSION GRANTED
                    Toast.makeText(context, "USB Ready", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_PERMISSION_NOT_GRANTED: // USB PERMISSION NOT GRANTED
                    Toast.makeText(context, "USB Permission not granted", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_NO_USB: // NO USB CONNECTED
                    Toast.makeText(context, "No USB connected", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_DISCONNECTED: // USB DISCONNECTED
                    Toast.makeText(context, "USB disconnected", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_NOT_SUPPORTED: // USB NOT SUPPORTED
                    Toast.makeText(context, "USB device not supported", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    @Override
    public void onResume() {
        super.onResume();
        setFilters();  // Start listening notifications from UsbService
        startService(UsbService.class, usbConnection, null); // Start UsbService(if it was not started before) and Bind it
        String powerOnCommand = new AnalogicsUtil().getPowerOnCommand();
        if(!StringUtils.containsIgnoreCase(powerOnCommand, "usbidseton")){
            new AnalogicsUtil().fingerPrintPowerOn();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(mUsbReceiver);
        unbindService(usbConnection);
    }

    @Override
    protected void onStop() {
//        new AnalogicsUtil().fingerPrintPowerOff();
//        unregisterReceiver(mUsbReceiver);
//        unbindService(usbConnection);
        super.onStop();
    }

    @Override
    public void onDestroy() {
//        new AnalogicsUtil().fingerPrintPowerOff();
//        unregisterReceiver(mUsbReceiver);
//        unbindService(usbConnection);
        super.onDestroy();
    }


    private void startService(Class<?> service, ServiceConnection serviceConnection, Bundle extras) {
        if (!UsbService.SERVICE_CONNECTED) {
            Intent startService = new Intent(this, service);
            if (extras != null && !extras.isEmpty()) {
                Set<String> keys = extras.keySet();
                for (String key : keys) {
                    String extra = extras.getString(key);
                    startService.putExtra(key, extra);
                }
            }
            startService(startService);
        }
        Intent bindingIntent = new Intent(this, service);
        bindService(bindingIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void setFilters() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbService.ACTION_USB_PERMISSION_GRANTED);
        filter.addAction(UsbService.ACTION_NO_USB);
        filter.addAction(UsbService.ACTION_USB_DISCONNECTED);
        filter.addAction(UsbService.ACTION_USB_NOT_SUPPORTED);
        filter.addAction(UsbService.ACTION_USB_PERMISSION_NOT_GRANTED);
        registerReceiver(mUsbReceiver, filter);
    }

    /*
     * This handler will be passed to UsbService. Data received from serial port is displayed through this handler
     */
    private static class MyHandler extends Handler {
        private final WeakReference<USB_Activity> mActivity;

        public MyHandler(USB_Activity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UsbService.MESSAGE_FROM_SERIAL_PORT:
                    String data = (String) msg.obj;
                    System.out.println("Response>>>" + data);
                    break;
                case UsbService.CTS_CHANGE:
                    Toast.makeText(mActivity.get(), "CTS_CHANGE", Toast.LENGTH_LONG).show();
                    break;
                case UsbService.DSR_CHANGE:
                    Toast.makeText(mActivity.get(), "DSR_CHANGE", Toast.LENGTH_LONG).show();
                    break;
                case UsbService.SYNC_READ:
                    String buffer = (String) msg.obj;
                    break;
            }
        }
    }

    private final ServiceConnection usbConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            usbService = ((UsbService.UsbBinder) arg1).getService();
            usbService.setHandler(mHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            usbService = null;
        }
    };
}