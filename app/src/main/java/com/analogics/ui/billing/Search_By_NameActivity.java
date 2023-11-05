package com.analogics.ui.billing;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.gsm.SmsManager;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;

import com.analogics.DBAdapter.DBAdapter;
import com.analogics.Printer.PrinterLib;
import com.analogics.R;
import com.analogics.appUtils.Config_SharedPreferances;
import com.analogics.billingCalculation.Tsspdcl_FileWriting;
import com.analogics.ocr.CameraActivity;
import com.analogics.ocr.ImageSheet;
import com.analogics.ocr.MeterDetails;
import com.analogics.ocr.MeterType;
import com.analogics.pojo.ConsumerDataVO;
import com.analogics.pojo.IrdaVO;
import com.analogics.pojo.inputDataVO;
import com.analogics.thermalAPI.RP_Printer_2inch_prof_ThermalAPI;
import com.analogics.thermalprinter.AnalogicsThermalPrinter;
import com.analogics.ui.menu.BillingMenuActivity;
import com.analogics.ui.menu.MainMenuActivity;
import com.analogics.utils.AlertMessage;
import com.analogics.utils.CommonFunctions;
import com.analogics.utils.DateUtil;
import com.analogics.utils.FileOperations;
import com.analogics.utils.GetIMEI_Number;
import com.analogics.utils.PublicVariables;
import com.whty.smartpos.tysmartposapi.ITYSmartPosApi;
import com.whty.smartpos.tysmartposapi.modules.printer.PrinterConfig;
import com.whty.smartpos.tysmartposapi.modules.printer.PrinterConstant;
import com.whty.smartpos.tysmartposapi.modules.printer.PrinterInitListener;
import com.whty.smartpos.tysmartposapi.modules.printer.PrinterListener;

import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.util.ArrayList;


public class Search_By_NameActivity extends AppCompatActivity {
    String portNo1 = "/dev/ttyHS0";
    Tsspdcl_FileWriting uploadFile;
    EditText ET_ServiceNo;
    EditText ET_Name;
    EditText ET_Address;
    EditText ET_MeterNo;
    EditText ET_USCNo;
    EditText ET_Catagory;
    EditText ET_SubCatagory;
    EditText ET_Phase;
    EditText ET_ContractedLoad;
    EditText ET_ConnectedLoad;
    EditText ET_MeterTC_Seal;
    EditText ET_MeterMake;
    EditText ET_MeterCapacity;
    EditText ET_PreviousMeterStatus;
    EditText ET_Old_Average_Units;
    EditText ET_PreviousReadingKWH;
    EditText ET_PreviousReadingKVah;
    Button Btn_Ok; // this ok button
    Button Btn_home;
    byte[] days_in_mth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    int sequence_count = 0;
    int ReturnRensponse = 0;
    String duplicateBillInformation;
    String tariffRates;
    String debug_buffer;
    String buffer;
    String ttemp;
    String currentDate;
    int status_punched_flag = 0;
    int ret = 0;
    int mhexcur = 0;
    int dhexcur = 0;
    int mhexold = 0;
    int bflag = 0, lastmonth = 0, presmonth = 0, group1 = 0;
    char NewService = '0';
    int tempfloat = 0, tempch = 0;
    float rate = 0.00F, paise = 0.00F, slab1 = 0.00F;
    float red = 0.00F;
    float ptflag = 0.00F;
    int newdays = 0;
    int olddays = 0;
    int totmths = 0;
    int totmths1 = 0;
    int utemp = 0;
    int mmin = 0;
    int etemp = 0;
    long[] navg = new long[3];
    float sub_engchg = 0.00F, tmpkvah = 0.00F;
    long kwh_consumption = 0, percentConsumption = 0;
    long kvah_consumption = 0, tavg = 0;
    int avgflag = 0;
    int xunits = 0;
    long aunits = 0;
    float avgut = 0.00F;
    float oldUnitsHT = 0.00F, newUnitsHT = 0.00F;
    float oldLLDUnits = 0.00F, newLLDUnits = 0.00F, cat1_rebate = 0.00F;
    float tmpflt = 0.00F;
    int oldmonth = 0, middlemonth = 0, monl = 0, monp = 0, newmonth = 0;
    float munits = 0.00F;
    float oldSlbMF = 0.00F, totMF = 0.00F, ecold = 0.00F, ecnew = 0.00F, newSlbMF = 0.00F, slabMF = 0.00F, floatx = 0.00F, otherChargesMF = 0.00F, temp_mf = 0.00F, C_new = 0.00F, C_old = 0.00F, C_dummy = 0.00F;
    int calender_days = 0, calender_days_old = 0, calender_days2 = 0;
    int noofdays = 0, odays = 0, ndays = 0;
    long days = 0;
    byte[] fromdt = new byte[7];
    byte[] todt = new byte[7];
    long previous_days = 0, current_days = 0, Tariff_days = 0;
    int demand_based_Flag = 0, excess_flag = 0;
    float CMD = 0.00F;
    String SearchType = "";

    String meterPhaseType = "";
    String meterSerialNumber = "";

    ArrayList<String> routeSequence = new ArrayList<>();
    DBAdapter dbAdapter = null;
    Cursor readrecord = null;
    IrdaVO irdaVO;

    ConsumerDataVO consumerDataVO = new ConsumerDataVO();
    inputDataVO inputDataVO = new inputDataVO();
    AnalogicsThermalPrinter conn;
    private boolean isAnalogicsPrinter;
    private String ptrType;
    private RP_Printer_2inch_prof_ThermalAPI printApi;

    int numberOfTries = 0;
    TextView kwhDialogEditText;
    ImageView kwhDialogImageView;
    Button kwhPhotoBtn;
    Button kwhDialogFullImageBtn;
    TextView kwhDialogAttemptTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search_by_name);

        conn = new AnalogicsThermalPrinter(Search_By_NameActivity.this);
        uploadFile = new Tsspdcl_FileWriting(this);
        Intent intent = getIntent();
        SearchType = intent.getStringExtra("SearchType") + "";

        if (!SearchType.equalsIgnoreCase("ManualSearch"))
            irdaVO = (IrdaVO) intent.getSerializableExtra("IRDAVO");
        ET_ServiceNo = findViewById(R.id.ET_ServiceNo);
        ET_Name = findViewById(R.id.ET_Name);
        ET_Address = findViewById(R.id.ET_Address);
        ET_MeterNo = findViewById(R.id.ET_MeterNo);
        ET_USCNo = findViewById(R.id.ET_USCNo);
        ET_Catagory = findViewById(R.id.ET_Catagory);
        ET_SubCatagory = findViewById(R.id.ET_SubCatagory);
        ET_Phase = findViewById(R.id.ET_Phase);
        ET_ContractedLoad = findViewById(R.id.ET_ContractedLoad);
        ET_ConnectedLoad = findViewById(R.id.ET_ConnectedLoad);
        ET_MeterTC_Seal = findViewById(R.id.ET_MeterTC_Seal);
        ET_MeterMake = findViewById(R.id.ET_MeterMake);
        ET_MeterCapacity = findViewById(R.id.ET_MeterCapacity);
        ET_PreviousMeterStatus = findViewById(R.id.ET_PreviousMeterStatus);
        ET_Old_Average_Units = findViewById(R.id.ET_Old_Average_Units);
        ET_PreviousReadingKWH = findViewById(R.id.ET_PreviousReadingKWH);
        ET_PreviousReadingKVah = findViewById(R.id.ET_PreviousReadingKVah);
        Btn_Ok = findViewById(R.id.Btn_Ok);
        Btn_home = findViewById(R.id.Btn_home);

        if (irdaVO == null) {
            irdaVO = new IrdaVO();
        }


        Btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), MainMenuActivity.class);
                startActivity(i);
                finish();
            }
        });


        if (CommonFunctions.isMachineOrMobileDevice().equals("tainyu")) {
            openBlueToothPrinterConnection();
            openDevicePrinter();
        }


        try {
            dbAdapter = DBAdapter.getDBAdapterInstance(this);
            dbAdapter.openDataBase();
            String query;
            if (SearchType.equalsIgnoreCase("ManualSearch")) {
                String ServiceNoToSearch = intent.getStringExtra("ServiceNoToSearch") + "";
                query = "select * from INPUT_MASTER where consumer_num like '%" + ServiceNoToSearch + "" + "%';";
            } else {
                String ServiceNoToSearch = intent.getStringExtra("ServiceNoToSearch") + "";
                if (!ServiceNoToSearch.equals("null"))
                    query = "select * from INPUT_MASTER where consumer_num like '%" + ServiceNoToSearch + "" + "%';";
                else {
                    query = "select * from INPUT_MASTER where trim(printf('%08d',meter_num)) like '%" + irdaVO.getMeterNumber() + "" + "%';";
                    if (irdaVO.getMeterNumber().length() > 8) {
                        query = "select * from INPUT_MASTER where trim(printf('%s',meter_num)) like '%" + irdaVO.getMeterNumber() + "" + "%';";
                    }
                }


            }
            readrecord = dbAdapter.selectRecordsFromDB(query, null);
            if (readrecord.moveToFirst()) {
                //ET_ERO_Name.setText(readrecord.getString(2).trim());
                //ET_ServNo.setText(readrecord.getString(0).trim());
                ET_ServiceNo.setText(readrecord.getString(0).trim());
                ET_Name.setText(readrecord.getString(3).trim());
                ET_Address.setText(readrecord.getString(4).trim());
                ET_MeterNo.setText(readrecord.getString(1).trim());
                ET_USCNo.setText(readrecord.getString(2).trim());
                ET_Catagory.setText(readrecord.getString(10).trim());
                ET_SubCatagory.setText(readrecord.getString(11).trim());
                ET_Phase.setText(readrecord.getString(12).trim());


//                inputDataVO.setLoc(readrecord.getString(0).substring(0, 5));
//                inputDataVO.setLoc1(readrecord.getString(0).substring(5, 8));
//                inputDataVO.setSerno(readrecord.getString(0).substring(8, 10));

                String consumerData = readrecord.getString(0).trim();
                if (consumerData.contains(" ")) {
                    String[] splitData = consumerData.split(" ");
                    String loc = splitData[0].trim();
                    String serNo = splitData[1].trim();
                    inputDataVO.setLoc(loc);
                    inputDataVO.setLoc1(" ");
                    inputDataVO.setSerno(serNo);
                } else {
                    inputDataVO.setLoc(" ");
                    inputDataVO.setLoc1(" ");
                    inputDataVO.setSerno(consumerData);
                }

                inputDataVO.setService_number(readrecord.getString(0));
                inputDataVO.setMtrno(readrecord.getString(1));
                inputDataVO.setUscNo(readrecord.getString(2));
                inputDataVO.setName(readrecord.getString(3));
                inputDataVO.setAdd1(readrecord.getString(4));
                inputDataVO.setAdd2(readrecord.getString(5));
                inputDataVO.setAdd3(readrecord.getString(6));
                inputDataVO.setAcode(readrecord.getString(7));
                inputDataVO.setStructurecode(readrecord.getString(8));
                inputDataVO.setGroup(readrecord.getString(9));
                inputDataVO.setCat(readrecord.getInt(10));
                inputDataVO.setSub_cat(readrecord.getInt(11));
                inputDataVO.setSubcat(readrecord.getString(11));
                inputDataVO.setPh(readrecord.getInt(12));
                inputDataVO.setMf(readrecord.getFloat(13));
                inputDataVO.setOmtrred(readrecord.getLong(14));
                inputDataVO.setPreviousKwh(readrecord.getLong(14));
                inputDataVO.setPreviousExportReading(readrecord.getLong(15));
                inputDataVO.setOmtrsts(Integer.parseInt(readrecord.getString(16)));
                inputDataVO.setOmtrdd(Integer.parseInt(readrecord.getString(17).substring(0, 2)));
                inputDataVO.setOmtrmm(Integer.parseInt(readrecord.getString(17).substring(3, 5)));
                inputDataVO.setOmtryy(Integer.parseInt(readrecord.getString(17).substring(6, 10)));
                inputDataVO.setFrezdred(readrecord.getLong(18));
                inputDataVO.setPhoneno(readrecord.getString(18));
                inputDataVO.setLpdt(readrecord.getString(19));
                inputDataVO.setArrearsBefore(readrecord.getFloat(20));
                inputDataVO.setArrearsAfter(readrecord.getFloat(21));
                inputDataVO.setAdditionalCharges(readrecord.getFloat(22));
                inputDataVO.setIntrestOnACD(readrecord.getFloat(23));
                inputDataVO.setPamount(readrecord.getFloat(24));
                inputDataVO.setPedchg(readrecord.getFloat(25));
                inputDataVO.setIntrestOnED(readrecord.getFloat(26));
                inputDataVO.setDiffintariff(readrecord.getFloat(27));
                inputDataVO.setCustomcolumn1(readrecord.getFloat(28));
                inputDataVO.setCustomcolumn2(readrecord.getFloat(29));
                inputDataVO.setAcd(readrecord.getFloat(30));
                inputDataVO.setOldavg(readrecord.getLong(31));
                inputDataVO.setAvgpf(readrecord.getFloat(32));
                inputDataVO.setAvgmaxdemand(readrecord.getFloat(33));
                inputDataVO.setSflag(readrecord.getInt(34));
                inputDataVO.setCapflag(readrecord.getInt(35));
                inputDataVO.setCat2HTFlag(readrecord.getInt(36));
                inputDataVO.setTriVectorFlag(readrecord.getInt(37));
                inputDataVO.setLvsideflag(readrecord.getInt(38));
                inputDataVO.setItsectorflag(readrecord.getInt(39));
                inputDataVO.setEdexflag(readrecord.getInt(40));
                inputDataVO.setScstflag(readrecord.getInt(41));
                inputDataVO.setCheckdsnrflag(readrecord.getInt(42));
                inputDataVO.setElcnonelecflag(readrecord.getInt(43));
                inputDataVO.setNetMeteringFlag(readrecord.getInt(44));
                inputDataVO.setMeterChangeFlag(readrecord.getInt(45));
                inputDataVO.setMeter_class(readrecord.getInt(46));
                inputDataVO.setIrFlag(readrecord.getInt(47));
                inputDataVO.setOccupancy_mode(readrecord.getInt(48));
                inputDataVO.setContractedLoad(readrecord.getFloat(49));
                inputDataVO.setConnectedMD(readrecord.getFloat(50));
                inputDataVO.setPreviousKvah(readrecord.getLong(51));
                inputDataVO.setPreviousSolarReadingKvah(readrecord.getFloat(52));
                inputDataVO.setKwhfinalreading4(readrecord.getLong(53));
                inputDataVO.setKvahfinalreading4(readrecord.getLong(54));
                inputDataVO.setDisconnectionSolarKWH(readrecord.getLong(55));
                inputDataVO.setPts(readrecord.getInt(56));
                inputDataVO.setCarryForwardUnits(readrecord.getLong(57));
                inputDataVO.setClubbingid(readrecord.getLong(58));
                inputDataVO.setClubmainserviceflag(readrecord.getInt(59));
                inputDataVO.setPoleno(readrecord.getInt(60));
                inputDataVO.setAgserno1(readrecord.getString(61));
                inputDataVO.setAgserno2(readrecord.getString(62));
                inputDataVO.setAgserno3(readrecord.getString(63));
                inputDataVO.setAgarrears1(readrecord.getFloat(64));
                inputDataVO.setAgarrears2(readrecord.getFloat(65));
                inputDataVO.setAgarrears3(readrecord.getFloat(66));
                inputDataVO.setCases_amt(readrecord.getLong(67));
                inputDataVO.setDc_caseamt(readrecord.getLong(68));
                inputDataVO.setVersion(readrecord.getString(69));

                try {
                    inputDataVO.setEroName(readrecord.getString(70));
                    inputDataVO.setSecName(readrecord.getString(71));
                } catch (Exception ex) {

                }
            } else {
                new AlertMessage().alertMessage(Search_By_NameActivity.this, "Alert", "Meter Number Not Found.");
            }
            readrecord.close();
            dbAdapter.close();
        } catch (Exception ex) {
            Log.e("TAG", "onCreate: " + ex);
        }

        Btn_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                inputDataVO.setBill_flag(0);
                meterSerialNumber = ET_ServiceNo.getText().toString();
                meterPhaseType = ET_Phase.getText().toString();
                dbAdapter.openDataBase();
                String already_billed = "select count(*) from OUTPUT_MASTER2 where consumer_num='" + ET_ServiceNo.getText().toString().trim() + "';";
                Cursor cursor = dbAdapter.selectRecordsFromDB(already_billed, null);
                if (cursor.moveToFirst()) {
                    inputDataVO.setBill_flag(cursor.getInt(0));
                }
                dbAdapter.close();
                if (inputDataVO.getBill_flag() != 0) {
                    //ALREADY BILLED\nDUPLICATE BILL(Y/N)
                    AlertDialog.Builder builder = new AlertDialog.Builder(Search_By_NameActivity.this);
                    builder.setTitle("Alert");
                    builder.setMessage("Already Billed. Duplicate Bill(Y/N)");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing but close the dialog
                            try {
                                PublicVariables.isDuplicateSlip = true;
                                dialog.dismiss();
                                dbAdapter.openDataBase();
                                String already_billed = "select duplicateBillData, QrInformation from duplicateBillPurpose where consumer_num='" + ET_ServiceNo.getText().toString().trim() + "';";
                                Cursor cursor = dbAdapter.selectRecordsFromDB(already_billed, null);
                                if (cursor.moveToFirst()) {
                                    inputDataVO.setDuplicatePrintDT(cursor.getString(0));
                                    inputDataVO.setQTPrintData(cursor.getString(1));
                                }
                                PublicVariables.isDuplicateSlip = true;
                                dbAdapter.close();
                                printBill w = new printBill();
                                w.execute();
                            } catch (Exception ex) {
                                PublicVariables.isDuplicateSlip = false;
                                Log.e("TAG", "onClick: " + ex);
                            }
                            return;
                        }
                    });

                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing
                            PublicVariables.isDuplicateSlip = false;
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    dataentry();
                }
            }
        });
    }

    public void dataentry() {
        PublicVariables.isDuplicateSlip = false;
        if (!SearchType.equalsIgnoreCase("ManualSearch")) inputDataVO.setMeter_reading_mode('1');
        else inputDataVO.setMeter_reading_mode('0');
        if ((inputDataVO.getMeter_reading_mode() == '1') || (inputDataVO.getMeter_reading_mode() == '2')) {
            inputDataVO.setNewMeterNumber(irdaVO.getMeterNumber());
            if (StringUtils.isNotBlank(irdaVO.getMeterMake()))
                inputDataVO.setMtrmk(irdaVO.getMeterMake());
            else inputDataVO.setMtrmk("HPL");
            inputDataVO.setPmtrred((long) irdaVO.getKWH());
            inputDataVO.setRecordedMD((float) irdaVO.getHv_rmd());
            inputDataVO.setPresentKvah((long) irdaVO.getKVAH());
            inputDataVO.setPresentKwh((long) irdaVO.getKWH());
            //inputDataVO.setPf(irdaVO.get);
            inputDataVO.setVoltage_B_VB((float) irdaVO.getVoltage_B_VB());
            inputDataVO.setVoltage_R_VR((float) irdaVO.getVoltage_R_VR());
            inputDataVO.setVoltage_Y_VY((float) irdaVO.getVoltage_Y_VY());
            inputDataVO.setCurrent_B_IB((float) irdaVO.getCurrent_B_IB());
            inputDataVO.setCurrent_R_IR((float) irdaVO.getCurrent_R_IR());
            inputDataVO.setCurrent_Y_IY((float) irdaVO.getCurrent_Y_IY());
            inputDataVO.setBilledRecordedMD(inputDataVO.getRecordedMD() * inputDataVO.getMf());
            inputDataVO.setPresentExportReading((long) irdaVO.getExport_KWH());
            System.out.println("IRDA_obj.Ir_IrDA_mtrno:" + inputDataVO.getMeter_reading_mode());
        } else {
            //IR & IRDA Init
            inputDataVO.setMeter_reading_mode('0');
            inputDataVO.setNewMeterNumber("");
            inputDataVO.setMtrmk("");
        }
        if (inputDataVO.getOmtrsts() == 14) {
            Toast.makeText(Search_By_NameActivity.this, "DISMANTLED.", Toast.LENGTH_LONG).show();
            return;
        }
        if (inputDataVO.getOmtrsts() > 19) {
            Toast.makeText(Search_By_NameActivity.this, "ISSUE BILL FROM ERO.", Toast.LENGTH_LONG).show();
            return;
        }
        //Need To Write sequence Number
        if (inputDataVO.getPh() == 0) {
            androidx.appcompat.app.AlertDialog.Builder alertDialog = new androidx.appcompat.app.AlertDialog.Builder(new ContextThemeWrapper(Search_By_NameActivity.this, R.style.AlertDialogTheme));
            alertDialog.setTitle("Data Entry");
            alertDialog.setMessage("Enter Phase");
            final EditText input = new EditText(Search_By_NameActivity.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            alertDialog.setView(input);

            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    inputDataVO.setPf(Float.parseFloat(input.getText().toString()));
                    Toast.makeText(Search_By_NameActivity.this, input.getText().toString(), Toast.LENGTH_LONG).show();
                    checkbox();
                }
            });
            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();
        } else checkbox();
    }

    public int checkbox() {
        try {
            System.out.println("In Checkbox");
            inputDataVO.setPmtrsts(1);
            inputDataVO.setKvahMeterStatus(1);
            inputDataVO.setMTRaccuracy(0.00F);
            status_punched_flag = 0;
            System.out.println("Category:" + inputDataVO.getCat());
            if ((inputDataVO.getCat() != 1) && (inputDataVO.getCat() != 2) && (inputDataVO.getCat() != 3) && (inputDataVO.getCat() != 4) && (inputDataVO.getCat() != 5) && (inputDataVO.getCat() != 6) && (inputDataVO.getCat() != 7) && (inputDataVO.getCat() != 8) && (inputDataVO.getCat() != 9)) {
                Toast.makeText(Search_By_NameActivity.this, "INVALID CATEGORY.", Toast.LENGTH_LONG).show();
                return 0;
            }
            if ((inputDataVO.getPh() != 1) && (inputDataVO.getPh() != 3)) {
                Toast.makeText(Search_By_NameActivity.this, "INVALID PHASE.", Toast.LENGTH_LONG).show();
                return 0;
            }
            //Testing Purpose
            inputDataVO.setIrFlag(0); //nrb
            //inputDataVO.setMeter_reading_mode('1');
            System.out.println("inputDataVO.getIrFlag():" + inputDataVO.getIrFlag());
            System.out.println("inputDataVO.getMeter_reading_mode():" + inputDataVO.getMeter_reading_mode());
            automaticStatusEntryFunction();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    private void automaticStatusEntry(String Msg, int maxLen) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View alertLayout = layoutInflater.inflate(R.layout.status_dialog, null);
        TextView textView = alertLayout.findViewById(R.id.textView);
        textView.setText(Msg);
        EditText editText = alertLayout.findViewById(R.id.Status);
        editText.requestFocus();
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLen)});
        Button Btn_next = (Button) alertLayout.findViewById(R.id.Btn_next);
        Button Btn_back = (Button) alertLayout.findViewById(R.id.Btn_back);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Search_By_NameActivity.this);
        alertDialog.setCancelable(false);
        alertDialog.setView(alertLayout);
        //editText.setText("1");
        AlertDialog dialog = alertDialog.create();
        editText.setFocusable(true);
        editText.requestFocus();
        textView.setTextSize(20);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        Btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (editText.getText().length() > 0)
                    ReturnRensponse = Integer.parseInt(editText.getText().toString());
                else ReturnRensponse = 1;
                System.out.println("automatic_status_entry ret:" + ReturnRensponse);
                if (ReturnRensponse == 0) inputDataVO.setPmtrsts(1);
                else inputDataVO.setPmtrsts(ReturnRensponse);
                System.out.println("Pmtrsts:" + inputDataVO.getPmtrsts());
                /*if ((inputDataVO.getPmtrsts() == 4) || (inputDataVO.getPmtrsts() == 7) || (inputDataVO.getPmtrsts() == 1)) {
                    Toast.makeText(Search_By_NameActivity.this, "\"FOR IR/IRDA CONSUMER\\nMANUAL READINGS NOT \\nALLOWED FOR REGULAR\\nCONSMPT.CASES:1,4,7 \\nPLZ IR/IRDA RDG ONLY\".", Toast.LENGTH_LONG).show();
                    automaticStatusEntryFunction();
                    return;
                } else if ((inputDataVO.getPmtrsts() == 5) && (inputDataVO.getTriVectorFlag() == 1)) {
                    Toast.makeText(Search_By_NameActivity.this, "\"INVALID COMBINATION\\nDOOR LOCK STATUS\\nCANNOT BE APPLIED TO\\nTRIVECTOR SERVICES.", Toast.LENGTH_LONG).show();
                    automaticStatusEntryFunction();
                    return;
                }*/
                if (((((inputDataVO.getPmtrsts() == 4) || (inputDataVO.getPmtrsts() == 7) || (inputDataVO.getPmtrsts() == 1) || (inputDataVO.getPmtrsts() == 9) || (inputDataVO.getPmtrsts() == 3) || (inputDataVO.getPmtrsts() == 2) || (inputDataVO.getPmtrsts() == 11) || (inputDataVO.getPmtrsts() == 5) || (inputDataVO.getPmtrsts() == 6) || (inputDataVO.getPmtrsts() == 8) || (inputDataVO.getPmtrsts() == 12))) && (inputDataVO.getTriVectorFlag() != 1)) || (((inputDataVO.getPmtrsts() == 4) || (inputDataVO.getPmtrsts() == 7) || (inputDataVO.getPmtrsts() == 1) || (inputDataVO.getPmtrsts() == 5)) && (inputDataVO.getTriVectorFlag() == 1)) || (((inputDataVO.getPmtrsts() == 4) || (inputDataVO.getPmtrsts() == 7) || (inputDataVO.getPmtrsts() == 1) || (inputDataVO.getPmtrsts() == 5)) && (inputDataVO.getNetMeteringFlag() == 1))) {
                    Toast.makeText(Search_By_NameActivity.this, "\"FOR IR/IRDA CONSUMER\\nMANUAL READINGS NOT \\nALLOWED FOR REGULAR\\nCONSMPT.CASES:1,4,7 \\nPLZ IR/IRDA RDG ONLY\".", Toast.LENGTH_LONG).show();
                    automaticStatusEntryFunction();
                    return;
                }
                status_punched_flag = 1;
                kwhEntryFunction();
            }
        });
        Btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(Search_By_NameActivity.this,"back",Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public int automaticStatusEntryFunction() {
        if ((inputDataVO.getIrFlag() == 1) && (inputDataVO.getMeter_reading_mode() == '0')) {
            automaticStatusEntry("IR/IRDA METER CONS\nPRES STATUS:", 2);
        } else {
            if ((inputDataVO.getIrFlag() == 1) || (inputDataVO.getIrFlag() == 0)) {
                if ((inputDataVO.getMeter_reading_mode() == '1') || (inputDataVO.getMeter_reading_mode() == '2')) {
                    System.out.println("inputDataVO.getMeterChangeFlag():" + inputDataVO.getMeterChangeFlag());
                    if (inputDataVO.getMeterChangeFlag() != 1) {
                        presentStatusEntry("PRES.STATUS:", 2);
                    } else {
                        Toast.makeText(Search_By_NameActivity.this, "PRES STATUS : 04", Toast.LENGTH_LONG).show();
                        inputDataVO.setPmtrsts(14);
                        inputDataVO.setKvahMeterStatus(14);
                        exportKWHreadingEntryFunction();
                    }
                } else kwhEntryFunction();
            }
        }
        return 0;
    }

    public int kwhEntryFunction() {
        if (inputDataVO.getMeter_reading_mode() == '0') {
            showkwhAlertDialog("PRV KWH:" + inputDataVO.getPreviousKwh() + "\nPRS KWH:", 11);
            return 0;
        }
        kvahEntryFunction();
        return 0;
    }

    public void kvahEntryFunction() {
        String presentKvah = "\nPRS KVAH " + inputDataVO.getPresentKvah();
        if (inputDataVO.getPmtrsts() != 4) {
            if (inputDataVO.getMeter_class() == 1) {
                PFEntry("PRS KWH:" + inputDataVO.getPresentKwh() + "\nPRS STS:" + inputDataVO.getPmtrsts() + "\nPF:", 6);
            } else if ((inputDataVO.getMeter_class() == 2) || ((inputDataVO.getTriVectorFlag() == 1) && (inputDataVO.getMeter_class() == 0))) {
                PrsKvahEntry("PRS KWH:" + inputDataVO.getPresentKwh() + "\nPRS STS:" + inputDataVO.getPmtrsts() + "\nPRV KVAH:" + inputDataVO.getPreviousKvah() + "\nPRS KVAH:", 11);
            } else {
                if ((inputDataVO.getCat() == 2) || (inputDataVO.getCat() == 3) || (inputDataVO.getCat() == 6) || (inputDataVO.getCat() == 7) || (inputDataVO.getCat() == 8) || (inputDataVO.getCat() == 1) || (inputDataVO.getCat() == 4) || (inputDataVO.getCat() == 5) || (inputDataVO.getCat() == 9)) {
                    RMDEntry("PRS KWH:" + inputDataVO.getPresentKwh() + "\nPRS STS:" + inputDataVO.getPmtrsts() + presentKvah + "\nRMD:", 6);
                } else exportKWHreadingEntryFunction();
            }
        } else exportKWHreadingEntryFunction();
    }

    public void rmdEntryFunction() {
        String presentKvah = "\nPRS KVAH " + inputDataVO.getPresentKvah();
        if (inputDataVO.getTriVectorFlag() == 1) {
            if (inputDataVO.getMeter_class() == 1) {
                RMDEntry1("PRS KWH:" + inputDataVO.getPresentKwh() + "\nPRS STS:" + inputDataVO.getPmtrsts() + "\nPF:" + inputDataVO.getPf() + presentKvah + "\nRMD:", 6);
            } else if (inputDataVO.getMeter_class() == 2) {
                RMDEntry1("PRS KWH:" + inputDataVO.getPresentKwh() + "\nPRS STS:" + inputDataVO.getPmtrsts() + "\nPRS KVAH:" + inputDataVO.getPresentKvah() + "\nRMD:", 6);
            } else
                RMDEntry1("PRS KWH:" + inputDataVO.getPresentKwh() + "\nPRS STS:" + inputDataVO.getPmtrsts() + presentKvah + "\nRMD:", 6);
        } else if ((inputDataVO.getMeter_class() == 2) || ((inputDataVO.getTriVectorFlag() == 1) && (inputDataVO.getMeter_class() == 0))) {
            RMDEntry1("PRS KWH:" + inputDataVO.getPresentKwh() + "\nPRS STS:" + inputDataVO.getPmtrsts() + "\nPRS KVAH:" + inputDataVO.getPresentKvah() + "\nRMD:", 6);
        } else exportKWHreadingEntryFunction();
    }

    public void RMDEntry1(String Msg, int maxLen) {
        System.out.println("RMDEntry1");
        LayoutInflater layoutInflater = getLayoutInflater();
        View alertLayout = layoutInflater.inflate(R.layout.kwh_dialog, null);
        TextView textView = alertLayout.findViewById(R.id.textView);
        textView.setText(Msg);
        EditText editText = alertLayout.findViewById(R.id.Status);
        editText.requestFocus();
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLen)});
        Button Btn_next = alertLayout.findViewById(R.id.Btn_next);
        Button Btn_back = alertLayout.findViewById(R.id.Btn_back);
        Button Btn_photo = alertLayout.findViewById(R.id.Btn_photo);


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Search_By_NameActivity.this);
        alertDialog.setCancelable(false);
        alertDialog.setView(alertLayout);
        AlertDialog dialog = alertDialog.create();
        editText.setFocusable(true);
        editText.requestFocus();
        textView.setTextSize(20);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);


        kwhDialogImageView = alertLayout.findViewById(R.id.Meter_Image_View);
        kwhPhotoBtn = Btn_photo;
        kwhDialogAttemptTv = alertLayout.findViewById(R.id.attemptTv);
        kwhDialogFullImageBtn = alertLayout.findViewById(R.id.imageSheetBtn);
        kwhDialogEditText = editText;
        numberOfTries = 0;
        Btn_photo.setOnClickListener(view -> {
            openOcrCamera(false, MeterType.Rmd);

        });


        Btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                editText.clearFocus();
                if (editText.getText().length() == 0) {
                    rmdEntryFunction();

                } else {
                    MeterDetails.RmdData.setManual(!MeterDetails.RmdData.getValue().equals(editText.getText().toString()));
                    inputDataVO.setRecordedMD(Float.parseFloat(editText.getText().toString()));
                    inputDataVO.setBilledRecordedMD(inputDataVO.getRecordedMD() * inputDataVO.getMf());
                    if (inputDataVO.getRecordedMD() > 500.00F) {
                        rmdEntryFunction();
                    } else {
                        export_KWHreading_entry();
                    }
                }
            }
        });

//        Btn_photo.setOnClickListener(v -> {
//            dialog.dismiss();
//            Intent intent = new Intent(getBaseContext(), ScanAutoAnalogDigitalMeterActivity.class);
//            intent.putExtra("inputDataVO", (Serializable) inputDataVO);
//            intent.putExtra("BillType", "PHOTO");
//            intent.putExtra("unitType", "RMD");
//            startActivity(intent);
//        });

        Btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                kwhEntryFunction();
            }
        });
        dialog.show();
    }

    public void RMDEntry(String Msg, int maxLen) {
        System.out.println("RMDEntry");
        LayoutInflater layoutInflater = getLayoutInflater();
        View alertLayout = layoutInflater.inflate(R.layout.kwh_dialog, null);


        TextView textView = alertLayout.findViewById(R.id.textView);
        textView.setText(Msg);
        EditText editText = alertLayout.findViewById(R.id.Status);
        editText.requestFocus();
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLen)});
        Button Btn_next = (Button) alertLayout.findViewById(R.id.Btn_next);
        Button Btn_back = (Button) alertLayout.findViewById(R.id.Btn_back);
        Button Btn_photo = (Button) alertLayout.findViewById(R.id.Btn_photo);

        kwhDialogImageView = alertLayout.findViewById(R.id.Meter_Image_View);
        kwhPhotoBtn = Btn_photo;
        kwhDialogEditText = editText;
        kwhDialogAttemptTv = alertLayout.findViewById(R.id.attemptTv);
        kwhDialogFullImageBtn = alertLayout.findViewById(R.id.imageSheetBtn);

        numberOfTries = 0;
        Btn_photo.setOnClickListener(view -> {
            openOcrCamera(false, MeterType.Rmd);
        });


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Search_By_NameActivity.this);
        alertDialog.setCancelable(false);
        alertDialog.setView(alertLayout);
        AlertDialog dialog = alertDialog.create();
        editText.setFocusable(true);
        editText.requestFocus();
        textView.setTextSize(20);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        Btn_next.setOnClickListener(v -> {
            MeterDetails.RmdData.setManual(!MeterDetails.RmdData.getValue().equals(editText.getText().toString()));
            editText.clearFocus();
            dialog.dismiss();
            if (editText.getText().length() == 0) {
                rmdEntryFunction();
                return;
            } else {
                inputDataVO.setRecordedMD(Float.parseFloat(editText.getText().toString()));
                inputDataVO.setBilledRecordedMD(inputDataVO.getRecordedMD() * inputDataVO.getMf());
                if (inputDataVO.getRecordedMD() > 500.00F) return;
                else {
                    rmdEntryFunction();
                    return;
                }
            }
        });


//        Btn_photo.setOnClickListener(v -> {
//            dialog.dismiss();
//            Intent intent = new Intent(getBaseContext(), ScanAutoAnalogDigitalMeterActivity.class);
//            intent.putExtra("inputDataVO", (Serializable) inputDataVO);
//            intent.putExtra("BillType", "PHOTO");
//            intent.putExtra("unitType", "RMD");
//            startActivity(intent);
//        });

        Btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void checkEntryFunction() {
        if (((inputDataVO.getCat() == 3) && (inputDataVO.getContractedLoad() > 20.00)) || ((inputDataVO.getCat() == 3) && (inputDataVO.getContractedLoad() > 10.00)) || ((inputDataVO.getCat() == 7) && (inputDataVO.getContractedLoad() > 10.00)) || ((inputDataVO.getCat() == 8) && (inputDataVO.getContractedLoad() > 10.00)) || ((inputDataVO.getCat() == 6) && (inputDataVO.getSub_cat() == 5 || inputDataVO.getSub_cat() == 6 || inputDataVO.getSub_cat() == 9 || inputDataVO.getSub_cat() == 12) && (inputDataVO.getContractedLoad() > 20.00))) {
            //PRS KVAH
        } else {
            exportKWHreadingEntryFunction();
        }
    }

    public int validation() {
//        Toast.makeText(Search_By_NameActivity.this, "IN VALIDATION", Toast.LENGTH_LONG).show();
        try {
            if ((inputDataVO.getPmtrsts() == 1) || (inputDataVO.getPmtrsts() == 2) || (inputDataVO.getPmtrsts() == 3) || (inputDataVO.getPmtrsts() == 4) || (inputDataVO.getPmtrsts() == 5) || (inputDataVO.getPmtrsts() == 6) || (inputDataVO.getPmtrsts() == 7) || (inputDataVO.getPmtrsts() == 9) || (inputDataVO.getPmtrsts() == 10) || (inputDataVO.getPmtrsts() == 11) || (inputDataVO.getPmtrsts() == 12) || (inputDataVO.getPmtrsts() == 13) || (inputDataVO.getPmtrsts() == 14)) {
                // Toast.makeText(Search_By_NameActivity.this, "IN VALIDATION", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(Search_By_NameActivity.this, "INVALID METER STATUS\nENTERED!..PLZ ENTER\nONLY VALID STATUS!..\n1,2,3,4,5,7,9\n10,11,12&13", Toast.LENGTH_LONG).show();
                return 0;
            }
            if (inputDataVO.getOmtrsts() == 13) {
                if (inputDataVO.getPmtrsts() != 13) {
                    Toast.makeText(Search_By_NameActivity.this, "INVALID COMBINATION\nPREVIOUS STATUS(13)\n&PRESENT STATUS(13)\nENTER ONLY STATUS13", Toast.LENGTH_LONG).show();
                    return 0;
                }
            }
            if ((inputDataVO.getPmtrsts() == 10) && (inputDataVO.getCat() != 6)) {
                if (inputDataVO.getTriVectorFlag() != 1) {
                    Toast.makeText(Search_By_NameActivity.this, "INVALID COMBINATION\\nPRESENT STATUS - 10\\nSHOULD BE APPLIED TO\\nNON TRIVECTOR AND \\nCAT-6 SERVICES", Toast.LENGTH_LONG).show();
                    return 0;
                }
            }
            if (inputDataVO.getOmtrsts() != 13) {
                if (inputDataVO.getPmtrsts() == 13) {
                    Toast.makeText(Search_By_NameActivity.this, "INVALID COMBINATION\nPRESENT STATUS(13)\nAND PREVIOUS STATUS\nSHOULD BE SAME(13)", Toast.LENGTH_LONG).show();
                    return 0;
                }
            }
            if (inputDataVO.getOmtrsts() == 3) {
                if (inputDataVO.getPmtrsts() == 5) {
                    Toast.makeText(Search_By_NameActivity.this, "INVALID COMBINATION\nDOOR LOCK STATUS(05)\nCANNOT BE APPLIED TO\nPREVIOUS STATUS(03)\n03-METER DISCONN STS", Toast.LENGTH_LONG).show();
                    return 0;
                }
            }
            if ((inputDataVO.getPmtrsts() == 0) || (inputDataVO.getPmtrsts() == 8) || (inputDataVO.getPmtrsts() > 14)) {
                Toast.makeText(Search_By_NameActivity.this, "INVALID COMBINATION\nPRESENT STATUS\nCANNOT BE 0,8 (OR)\nGREATER THAN 14.", Toast.LENGTH_LONG).show();
                return 0;
            }
            if ((inputDataVO.getPmtrsts() == 1) && (inputDataVO.getOmtrsts() == 5)) {
                if (inputDataVO.getPmtrred() <= inputDataVO.getOmtrred()) {
                    Toast.makeText(Search_By_NameActivity.this, "CURR KWH RDG IS LESS.", Toast.LENGTH_LONG).show();
                    return 0;
                }
            }
            if (inputDataVO.getOmtrsts() == 3) {
                if ((inputDataVO.getPmtrsts() != 1) && (inputDataVO.getPmtrsts() != 9)) {
                    Toast.makeText(Search_By_NameActivity.this, "INVALID COMBINATION\nPREVIOUS STATUS(3)\n&PRESENT STATUS(3)", Toast.LENGTH_LONG).show();
                    return 0;
                }
            }

            MeterDetails.fullImageBitmap = null;
            // Inflate the custom layout for the dialog
            LayoutInflater layoutInflater = getLayoutInflater();
            View alertLayout = layoutInflater.inflate(R.layout.meter_image_dialog, null);
            Button backBtn = alertLayout.findViewById(R.id.Btn_back);
            Button nextBtn = alertLayout.findViewById(R.id.Btn_next);
            Button photoBtn = alertLayout.findViewById(R.id.Btn_photo);

            EditText editText = alertLayout.findViewById(R.id.Status);
            editText.setFocusable(false);
            editText.clearFocus();
            kwhDialogImageView = alertLayout.findViewById(R.id.Meter_Image_View);
            kwhDialogFullImageBtn = alertLayout.findViewById(R.id.imageSheetBtn);
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(Search_By_NameActivity.this);
            alertDialog.setCancelable(false);
            alertDialog.setView(alertLayout);
            AlertDialog dialog = alertDialog.create();
            photoBtn.setOnClickListener(view -> openOcrCamera(true, MeterType.FullPhoto));
            nextBtn.setOnClickListener(v -> {
                if (MeterDetails.fullImageBitmap != null) {
                    dialog.dismiss();
                    MeterDetails.saveBase64ToImageFile(getApplicationContext(), ET_ServiceNo.getText().toString());
                    confirmationDialog();
                } else {
                    Toast.makeText(getApplicationContext(), "Please Capture Full Photo", Toast.LENGTH_SHORT).show();
                }
            });
            backBtn.setOnClickListener(view -> {
                dialog.dismiss();
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    private void confirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm");
        builder.setMessage("ENTER TO CONFIRM");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                dialog.dismiss();
                if ((inputDataVO.getPmtrsts() != 4) && (inputDataVO.getPmtrsts() != 7) && (inputDataVO.getPmtrsts() != 14)) {
                    if (inputDataVO.getPmtrred() < inputDataVO.getOmtrred()) {
                        Toast.makeText(Search_By_NameActivity.this, "CURR KWH RDG IS LESS.", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                if (((inputDataVO.getPmtrsts() == 4) && (inputDataVO.getOmtrsts() == 5)) || ((inputDataVO.getPmtrsts() == 6) && (inputDataVO.getOmtrsts() == 6)) || ((inputDataVO.getPmtrsts() == 7) && (inputDataVO.getOmtrsts() == 4)) || ((inputDataVO.getPmtrsts() == 7) && (inputDataVO.getOmtrsts() == 7))) {
                    Toast.makeText(Search_By_NameActivity.this, "ISSUE BILL FROM ERO.", Toast.LENGTH_LONG).show();
                    return;
                }
                if ((inputDataVO.getPmtrsts() == 1) && (inputDataVO.getPmtrred() < inputDataVO.getOmtrred())) {
                    Toast.makeText(Search_By_NameActivity.this, "ISSUE BILL FROM ERO.", Toast.LENGTH_LONG).show();
                    return;
                }

                if ((inputDataVO.getOmtrsts() == 3) && (inputDataVO.getPmtrsts() == 7)) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(Search_By_NameActivity.this);

                    builder1.setTitle("Confirm");
                    builder1.setMessage("Are you sure?");

                    builder1.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing but close the dialog
                            dialog.dismiss();
                            beforeCalc();
                        }
                    });

                    builder1.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing
                            Toast.makeText(Search_By_NameActivity.this, "ISSUE BILL FROM ERO.", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    });
                } else beforeCalc();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void ApproxUnits(inputDataVO inputDataVO) {
        System.out.println("ApproxUnits");
        inputDataVO.setAunits(0);
        LayoutInflater layoutInflater = getLayoutInflater();
        View alertLayout = layoutInflater.inflate(R.layout.kwh_dialog, null);
        TextView textView = alertLayout.findViewById(R.id.textView);
        textView.setText("APPROX. UNITS:");
        EditText editText = alertLayout.findViewById(R.id.Status);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
        Button Btn_next = (Button) alertLayout.findViewById(R.id.Btn_next);
        Button Btn_back = (Button) alertLayout.findViewById(R.id.Btn_back);
        Button Btn_photo = (Button) alertLayout.findViewById(R.id.Btn_photo);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Search_By_NameActivity.this);
        alertDialog.setCancelable(false);
        alertDialog.setView(alertLayout);
        AlertDialog dialog = alertDialog.create();
        editText.setFocusable(true);
        editText.requestFocus();
        textView.setTextSize(20);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        Btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (editText.getText().length() == 0) {
                    //inputDataVO.setAunits(Integer.parseInt(editText.getText().toString()));
                    inputDataVO.setAunits(0);
                } else {
                    inputDataVO.setAunits(Integer.parseInt(editText.getText().toString()));
                }
                //ret = globalVbls.calculate(inputDataVO);
                validation();
                /*if (ret == 2) {
                    showkwhAlertDialog("PRV KWH:" + inputDataVO.getPreviousKwh() + "\nPRS KWH:", 11);
                }*/
            }
        });
        Btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public int beforeCalc() {
        String currentDate = new DateUtil().getCurrentdate();
        inputDataVO.setPmtrdd(Integer.parseInt(currentDate.substring(0, 2)));
        inputDataVO.setPmtrmm(Integer.parseInt(currentDate.substring(2, 4)));
        inputDataVO.setPmtryy(Integer.parseInt(currentDate.substring(4, 8)));
        if (inputDataVO.getPmtryy() < 2022) {
            Toast.makeText(Search_By_NameActivity.this, "INVALID DATE AND TIME", Toast.LENGTH_LONG).show();
            return 0;
        }
        dhexcur = inputDataVO.getPmtrdd();
        mhexcur = inputDataVO.getPmtrmm();
        if ((dhexcur > 31) || (mhexcur > 12)) {
            Toast.makeText(Search_By_NameActivity.this, "INVALID DATE AND TIME2\nBILLING NOT ALLOWED", Toast.LENGTH_LONG).show();
            return 0;
        }
        if ((inputDataVO.getOmtrmm() <= 0) || (inputDataVO.getOmtrmm() > 12) || (inputDataVO.getOmtrdd() <= 0) || (inputDataVO.getOmtrdd() > 31)) {
            Toast.makeText(Search_By_NameActivity.this, "INVALID DATE AND TIME3\nBILLING NOT ALLOWED", Toast.LENGTH_LONG).show();
            return 0;
        }
        if ((inputDataVO.getPmtrmm() <= 0) || (inputDataVO.getPmtrmm() > 12) || (inputDataVO.getPmtrdd() <= 0) || (inputDataVO.getPmtrdd() > 31)) {
            Toast.makeText(Search_By_NameActivity.this, "INVALID DATE AND TIME4\nBILLING NOT ALLOWED", Toast.LENGTH_LONG).show();
            return 0;
        }

        inputDataVO.setDuedd(Integer.parseInt(currentDate.substring(0, 2).toString()));
        inputDataVO.setDuemm(Integer.parseInt(currentDate.substring(2, 4).toString()));
        inputDataVO.setDueyy(Integer.parseInt(currentDate.substring(4, 8).toString()));
        future_date();

        if (inputDataVO.getArrearsBefore() > 10 || inputDataVO.getArrearsAfter() > 10) {
            inputDataVO.setDisdd(inputDataVO.getOmtrdd());
            inputDataVO.setDismm(inputDataVO.getOmtrmm());
            inputDataVO.setDisyy(inputDataVO.getOmtryy());
        } else {
            inputDataVO.setDisdd(Integer.parseInt(currentDate.substring(0, 2).toString()));
            inputDataVO.setDismm(Integer.parseInt(currentDate.substring(2, 4).toString()));
            inputDataVO.setDisyy(Integer.parseInt(currentDate.substring(4, 8).toString()));
        }
        disconnected_date();
        //nminflag=0;
        ret = 0;
        if ((inputDataVO.getPmtrsts() != 6) || ((inputDataVO.getPmtrsts() == 6) && (inputDataVO.getOmtrsts() != 6))) {
            ret = 0;
            //ret=calculate();

            /*if((inputDataVO.getOmtrsts()==2)||(inputDataVO.getOmtrsts()==11)||(inputDataVO.getOmtrsts()==12)
                    ||(inputDataVO.getPmtrsts()==2)||(inputDataVO.getPmtrsts()==11)||(inputDataVO.getPmtrsts()==12))
            {
                //Approx Units
                ApproxUnits(inputDataVO);
            }
            else*/
            {
                if ((inputDataVO.getPmtrsts() == 1) && (inputDataVO.getPresentKwh() == inputDataVO.getPreviousKwh()))
                    inputDataVO.setPmtrsts(9);
                if (ret == 2) {
//                    showkwhAlertDialog("PRV KWH:" + inputDataVO.getPreviousKwh() + "\nPRS KWH:", 11);
                    Toast.makeText(Search_By_NameActivity.this, "KVAH CONSUMPTION\nIS LESSER THAN\nKWH CONSUMPTION", Toast.LENGTH_SHORT).show();
                    //goto kwh_entry;
                    return 0;
                }
            }
        }
        if ((inputDataVO.getPmtrsts() == 6) && (inputDataVO.getOmtrsts() == 6)) {
            inputDataVO.setPmtrred(inputDataVO.getOmtrred());
            inputDataVO.setPresentKwh(inputDataVO.getOmtrred());
            inputDataVO.setUnits(0.00F);
            inputDataVO.setEngchg(0.00F);
            inputDataVO.setCuschg(0.00F);
            inputDataVO.setEdchg(0.00F);
            inputDataVO.setAdditionalCharges(0.00F);
            inputDataVO.setArrearsBefore(0.00F);
            inputDataVO.setBillAmount(0.00F);
            inputDataVO.setPamount(0.00F);
            inputDataVO.setPedchg(0.00F);
            inputDataVO.setTotalAmount(0.00F);
            inputDataVO.setTotalDue(0.00F);
        }
        if ((inputDataVO.getOmtrsts() == 13) || (inputDataVO.getPmtrsts() == 13)) {
            inputDataVO.setEngchg(0.00F);
            inputDataVO.setCuschg(0.00F);
            inputDataVO.setEdchg(0.00F);
            inputDataVO.setFixchg(0.00F);
            inputDataVO.setAdditionalCharges(0.00F);
            inputDataVO.setIntrestOnED(0.00F);
            inputDataVO.setIntrestOnACD(0.00F);
            inputDataVO.setBillAmount(0.00F);
            inputDataVO.setNetAmount(0.00F);
            inputDataVO.setTotalAmount(0.00F);
            inputDataVO.setTotalDue(0.00F);
            inputDataVO.setTotalDue(inputDataVO.getArrearsBefore() + inputDataVO.getArrearsAfter());
            inputDataVO.setPamount(0.00F);
            inputDataVO.setPedchg(0.00F);
        }
		/*if((inputDataVO.getBillAmount()==nan)||(inputDataVO.getBillAmount()==plusINF)||(inputDataVO.getBillAmount()==minusINF))
		{
			Toast.makeText(Search_By_NameActivity.this, "INVALID BILL AMOUNT.", Toast.LENGTH_LONG).show();
			return 0;
		}*/
        if (inputDataVO.getScstflag() == 2) {
            //displayMessagebox("SUB UNITS : "+QString::number(rt.subsidyunits)+"\nSUB AMT : "+QString::number(round(rt.Subsidybillamnt))+"\nENTER TO NEXT");
            Toast.makeText(Search_By_NameActivity.this, "SUB UNITS : " + inputDataVO.getSubsidyunits() + "\nSUB AMT : " + inputDataVO.getSubsidybillamnt() + "\nENTER TO NEXT", Toast.LENGTH_LONG).show();
        }
		/*reply = displayMessageboxQuestion("UNITS : "+QString::number(rt.units)+"\nBILL AMT : "+QString::number(rt.BillAmount)+"\nTOTAL DUE : "+QString::number(rt.TotalDue)+"\n---------------------------\nISSUE BILL(Y/N)");
		qDebug()<<"reply:"<<reply;
		if(reply==false)
		{
			return 0;
		}
		saveDuplicatePrintData();
		globalVbls.saveDuplicatePrintData(inputDataVO);
		*/
        //Toast.makeText(Search_By_NameActivity.this, "PortNo:SC1", Toast.LENGTH_LONG).show();
        //Write_Into_File();

        SharedPreferences sharedpreferences;
        sharedpreferences = getSharedPreferences("billnoPref_Key", Context.MODE_PRIVATE);
        String billno = sharedpreferences.getString("billno_Key", null);

        if (billno == null) {
            // the key does not exist
            SharedPreferences.Editor editor = sharedpreferences.edit();
            inputDataVO.setBill_count(1);
            editor.putString("billno_Key", "00001");
            editor.commit();
            editor.clear();
        } else {
            inputDataVO.setBill_count(Integer.parseInt(billno));
			/*inputDataVO.setBill_count(inputDataVO.getBill_count()+1);
			SharedPreferences.Editor editor = sharedpreferences.edit();
			editor.putString("billno_Key", inputDataVO.getBill_count()+"");
			editor.commit();
			editor.clear();*/
        }

        /* todo made some changes by @Mk*/


        int retValue = 0;
        String machineId = new GetIMEI_Number().getUniqueIMEIId(Search_By_NameActivity.this);
        transactionInfo(machineId, sharedpreferences, billno);


        return 1;
    }

    private void transactionInfo(String machineId, SharedPreferences sharedpreferences, String billno) {


        String message = "";
        if (!StringUtils.equalsIgnoreCase(inputDataVO.getMtrno(), inputDataVO.getNewMeterNumber())) {
            message = "INPUT METER NO : " + inputDataVO.getMtrno() + "\n" + "SCANNED METER NO : " + inputDataVO.getNewMeterNumber() + "\n" + "KWH : " + inputDataVO.getPresentKwh() + "\n" + "KVAH : " + inputDataVO.getPresentKvah() + "\n" + "RMD : " + inputDataVO.getBilledRecordedMD() + "\n" + "MAKE : " + inputDataVO.getMtrmk() + "\n" + "  --------------------------- \n" + "UNITS : " + inputDataVO.getUnits() + "\n" + "BILL AMT : " + inputDataVO.getBillAmount() + "\n" + "TOTAL DUE : " + inputDataVO.getTotalDue() + "\n" + "CONTINUE  : " + "(YES/NO)" + "\n";
        } else {


            message = "METER NO : " + inputDataVO.getMtrno() + "\n" + "KWH : " + inputDataVO.getPresentKwh() + "\n" + "KVAH : " + inputDataVO.getPresentKvah() + "\n" + "RMD : " + inputDataVO.getBilledRecordedMD() + "\n" + "MAKE : " + inputDataVO.getMtrmk() + "\n" + "  --------------------------- \n" + "UNITS : " + inputDataVO.getUnits() + "\n" + "BILL AMT : " + inputDataVO.getBillAmount() + "\n" + "TOTAL DUE : " + inputDataVO.getTotalDue() + "\n" + "CONTINUE  : " + "(YES/NO)" + "\n";

        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        builder.setTitle("Bill Details");
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                saveAndPrintTransaction(machineId, sharedpreferences, billno);

//                if(inputDataVO.getUnits() <= 7 * inputDataVO.getOldavg()){
//                    saveAndPrintTransaction(machineId, sharedpreferences, billno);
//                }else{
//                    Toast.makeText(Search_By_NameActivity.this, "Please check UNITS", Toast.LENGTH_SHORT).show();
//                    return;
//                }
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //  Action for 'NO' Button
                dialog.dismiss();
                dialog.cancel();
                return;
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void saveAndPrintTransaction(String machineId, SharedPreferences sharedpreferences, String billno) {
        SharedPreferences configSharedpreferences;
        String config_Preferences = "config_Preferences";
        configSharedpreferences = getSharedPreferences(config_Preferences, Context.MODE_PRIVATE);

        int retValue = 0;

        String responseStr = uploadFile.Write_Into_File(inputDataVO, machineId);
        if (responseStr == null) return;

        //Write to DB
        retValue = uploadFile.Write_Record(inputDataVO, machineId);

        //Write to file
        new FileOperations().writeToFile(billno, responseStr, "TSSPDCL_" + machineId + "_" + new DateUtil().getDatetimeStamp() + ".txt", Search_By_NameActivity.this);
        System.out.println("Data inserted successfully");

        sharedpreferences = getSharedPreferences("billnoPref_Key", getApplicationContext().MODE_PRIVATE);
        billno = sharedpreferences.getString("billno_Key", null);
        configSharedpreferences = getSharedPreferences(config_Preferences, getApplicationContext().MODE_PRIVATE);
        if (billno == null) {
            // the key does not exist
            SharedPreferences.Editor editor = sharedpreferences.edit();
            inputDataVO.setBill_count(1);
            editor.putString("billno_Key", "00001");
            editor.commit();
            editor.clear();
        } else {
            inputDataVO.setBill_count(Integer.parseInt(billno));
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("billno_Key", (inputDataVO.getBill_count() + 1) + "");
            editor.commit();
            editor.clear();
        }

        String aePhoneNo = new Config_SharedPreferances().getAeMobileValues(getApplicationContext());
        if (aePhoneNo.length() == 10) {
            if (inputDataVO.getPmtrsts() == 2)
                sendSMS(aePhoneNo, inputDataVO.getService_number() + " ,METER STRUCKUP");
            if (inputDataVO.getPmtrsts() == 11)
                sendSMS(aePhoneNo, inputDataVO.getService_number() + " ,METER BURNT");
        }

        printBill w = new printBill();
        w.execute();

    }


    private void openOcrCamera(boolean disableOcr, MeterType ocrType) {
        Intent intent = new Intent(Search_By_NameActivity.this, CameraActivity.class);
        intent.putExtra("disableOcr", disableOcr);
        intent.putExtra("type", ocrType.toString());
        intent.putExtra("phase", meterPhaseType);
        intent.putExtra("serviceNumber", meterSerialNumber);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityForResult(intent, 111);
    }

    private class printBill extends AsyncTask<byte[], String, String> {
        String phase = "";
        ProgressDialog p;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(Search_By_NameActivity.this);
            p.setMessage("Please wait...printing Bill");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(byte[]... bytes) {
            try {
                String ptrType = new Config_SharedPreferances().getPrinterTypeValues(getApplicationContext());
//                Thread.sleep(1000);
                /*Bitmap mBitmap =null;
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.tsspdcl_logo_print);
                mBitmap=printApi.prepareReciptImageDataToPrint_VIP(bmp);
                printApi.printRecipt( mBitmap);
                Thread.sleep(500);*/

                String receiptData = inputDataVO.getDuplicatePrintDT();

                if (PublicVariables.isDuplicateSlip) {
                    String result = "";
                    String[] stringSplits = receiptData.split("\n");
                    for (int i = 0; i < stringSplits.length; i++) {
                        if (i == 3) {
                            result += "DUPLICATE BILL";
//                            result += "-------------------------";
                            result += "\n";
                            result += "\n";
                        }
                        result += stringSplits[i] + "\n";
                    }
                    receiptData = result;
                }

                if (ptrType.equals("tianyu")) {

                    ITYSmartPosApi tyApi = ITYSmartPosApi.get(getApplicationContext());

                    tyApi.initPrinter(new PrinterInitListener() {
                        @Override
                        public void onPrinterInit(boolean b) {

                        }
                    });
//
//                    /*Bundle bundle = new Bundle();
//                    bundle.putInt(PrinterConfig.FONT_SIZE, 4);
//                    bundle.putInt(PrinterConfig.ALIGN, PrinterConstant.Align.ALIGN_CENTER);
//                    bundle.putInt(PrinterConfig.CN_FONT, PrinterConstant.Typeface.SONGTI_BOLD);
//                    bundle.putInt(PrinterConfig.EN_FONT, PrinterConstant.Typeface.SONGTI_BOLD);
//                    tyApi.setPrinterParameters(bundle);
//               */
//                    tyApi.setLineSpace(1);
//                   tyApi.setTypeface(Typeface.DEFAULT);
//                   if (PublicVariables.isDuplicateSlip) {
//                       String[] stringSplits = receiptData.split("\n");
//                       for (int i = 0; i < stringSplits.length; i++) {
//                           //tyApi.appendPrintElement(new PrintElement(stringSplits[i] + "\n", PrinterConstant.Align.ALIGN_CENTER, PrinterConstant.FontSize.FONT_SIZE_LARGE));
//                           if(i==11||i==12||i==44) {
//
//                               tyApi.appendPrintElement(new PrintElement(stringSplits[i] + "", PrinterConstant.Align.ALIGN_CENTER, PrinterConstant.FontSize.FONT_SIZE_LARGE,true));
//                               System.out.println(">>>" + i + " >>>>" + stringSplits[i]);
//                           }else{
//                               //tyApi.appendPrintElement(new PrintElement(stringSplits[i] + "", PrinterConstant.Align.ALIGN_CENTER,PrinterConstant.FontSize.FONT_SIZE_MIDDLE,true));
//                               tyApi.appendPrintElement(new PrintElement(stringSplits[i] + "", PrinterConstant.Align.ALIGN_CENTER,4));
//                               System.out.println(">>>" + i + " >>>>" + stringSplits[i]);
//                           }}
//                   }else {
//                       String[] stringSplits = receiptData.split("\n");
//                       for (int i = 0; i < stringSplits.length; i++) {
//                           //tyApi.appendPrintElement(new PrintElement(stringSplits[i] + "\n", PrinterConstant.Align.ALIGN_CENTER, PrinterConstant.FontSize.FONT_SIZE_LARGE));
//                           if(i==9||i==10||i==42) {
//
//                               tyApi.appendPrintElement(new PrintElement(stringSplits[i] + "", PrinterConstant.Align.ALIGN_LEFT, PrinterConstant.FontSize.FONT_SIZE_LARGE,true));
//                               System.out.println(">>>" + i + " >>>>" + stringSplits[i]);
//                           }else{
//                               tyApi.appendPrintElement(new PrintElement(stringSplits[i] + "", PrinterConstant.Align.ALIGN_CENTER,4));
//                               //tyApi.appendPrintElement(new PrintElement(stringSplits[i] + "", PrinterConstant.Align.ALIGN_CENTER,PrinterConstant.FontSize.FONT_SIZE_MIDDLE,true));
//                               System.out.println(">>>" + i + " >>>>" + stringSplits[i]);
//                           }}
//                   }

                    Bundle bundle = new Bundle();
                    bundle.putInt(PrinterConfig.FONT_SIZE, 4);
                    bundle.putInt(PrinterConfig.ALIGN, PrinterConstant.Align.ALIGN_CENTER);
                    bundle.putInt(PrinterConfig.CN_FONT, PrinterConstant.Typeface.SONGTI_BOLD);
                    bundle.putInt(PrinterConfig.EN_FONT, PrinterConstant.Typeface.SONGTI_BOLD);
                    tyApi.setPrinterParameters(bundle);
                    tyApi.setLineSpace(5);
                    int ret = tyApi.printText(receiptData);

                    ret = tyApi.startPrintElement();

                    AssetManager assetManager = getAssets();
                    InputStream is = assetManager.open("vote_3.bmp");
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    ret = tyApi.printBitmap(bitmap);


                    //ret = tyApi.startPrintElement();
                    // int ret = tyApi.printText(receiptData);

                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String bitmap) {
            super.onPostExecute(bitmap);
            p.dismiss();
            navigateBillingMenu();
        }
    }

    @Override
    protected void onDestroy() {
        try {
            conn.closeBT();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    private void navigateBillingMenu() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Search_By_NameActivity.this, BillingMenuActivity.class); //BillingMenuActivity
                startActivity(intent);
                finish();
            }
        }, 2500);
    }

    public void future_date() {
        int DUEDAYS = 14;
        inputDataVO.setDuedd(inputDataVO.getDuedd() + DUEDAYS);
        if (inputDataVO.getDuedd() > days_in_mth[(inputDataVO.getDuemm() - 1)]) {
            inputDataVO.setDuedd(inputDataVO.getDuedd() - days_in_mth[(inputDataVO.getDuemm() - 1)]);
            inputDataVO.setDuemm(inputDataVO.getDuemm() + 1);
            if ((inputDataVO.getDuemm() - 1) == 12) {
                inputDataVO.setDuemm(1);
                inputDataVO.setDueyy(inputDataVO.getDueyy() + 1);
            }
        }
        return;
    }

    public void disconnected_date() {
        int DISDAYS = 29;

        if (inputDataVO.getArrearsBefore() > 10 || inputDataVO.getArrearsAfter() > 10) {
            DISDAYS = 29;
        } else {
            DISDAYS = 28;
        }

        inputDataVO.setDisdd(inputDataVO.getDisdd() + DISDAYS);
        if (inputDataVO.getDisdd() > days_in_mth[(inputDataVO.getDuemm() - 1)]) {
            inputDataVO.setDisdd(inputDataVO.getDisdd() - days_in_mth[(inputDataVO.getDismm() - 1)]);
            inputDataVO.setDismm(inputDataVO.getDismm() + 1);
            if ((inputDataVO.getDismm() - 1) == 12) {
                inputDataVO.setDismm(1);
                inputDataVO.setDisyy(inputDataVO.getDisyy() + 1);
            }
        }
        return;
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

    private void kvah_entry(String Msg, int maxLen) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View alertLayout = layoutInflater.inflate(R.layout.status_dialog, null);
        TextView textView = alertLayout.findViewById(R.id.textView);
        textView.setText(Msg);
        EditText editText = alertLayout.findViewById(R.id.Status);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLen)});
        Button Btn_next = (Button) alertLayout.findViewById(R.id.Btn_next);
        Button Btn_back = (Button) alertLayout.findViewById(R.id.Btn_back);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Search_By_NameActivity.this);
        alertDialog.setCancelable(false);
        alertDialog.setView(alertLayout);
        //editText.setText("1");
        AlertDialog dialog = alertDialog.create();
        editText.setFocusable(true);
        editText.requestFocus();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        Btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                if (editText.getText().length() > 0)
                    ReturnRensponse = Integer.parseInt(editText.getText().toString());
                else ReturnRensponse = 1;
                System.out.println("status_entry ret:" + ReturnRensponse);
                if (ReturnRensponse == 0) {
                    inputDataVO.setPmtrsts(1);
                    inputDataVO.setKvahMeterStatus(inputDataVO.getPmtrsts());
                    System.out.println("Pmtrsts:" + inputDataVO.getPmtrsts());
                    //kvah_entry();
                } else {
                    inputDataVO.setPmtrsts(ReturnRensponse);
                    inputDataVO.setKvahMeterStatus(inputDataVO.getPmtrsts());
                    System.out.println("Pmtrsts:" + inputDataVO.getPmtrsts());
                    if ((inputDataVO.getPmtrsts() == 5) && (inputDataVO.getTriVectorFlag() == 1)) {
                        Toast.makeText(Search_By_NameActivity.this, "INVALID COMBINATION\nDOOR LOCK STATUS\nCANNOT BE APPLIED TO\nTRIVECTOR SERVICES.", Toast.LENGTH_LONG).show();
                        status_entry("PRV KWH:" + inputDataVO.getPreviousKwh() + "\nPRS KWH:" + inputDataVO.getPresentKwh() + "\nPRES STATUS:", 2);
                    } else if ((inputDataVO.getPmtrsts() == 1) || (inputDataVO.getPmtrsts() == 2) || (inputDataVO.getPmtrsts() == 3) || (inputDataVO.getPmtrsts() == 5) || (inputDataVO.getPmtrsts() == 6) || (inputDataVO.getPmtrsts() == 7) || (inputDataVO.getPmtrsts() == 9) || (inputDataVO.getPmtrsts() == 10) || (inputDataVO.getPmtrsts() == 11) || (inputDataVO.getPmtrsts() == 12) || (inputDataVO.getPmtrsts() == 13)) {
                        //goto kvah_entry;
                    } else if (inputDataVO.getPmtrsts() == 4) {
                        Toast.makeText(Search_By_NameActivity.this, "INVALID METER STATUS\nENTERED!..PLZ ENTER\nONLY VALID STATUS!..\n4\n", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Search_By_NameActivity.this, "INVALID METER STATUS\nENTERED!..PLZ ENTER\nONLY VALID STATUS!..\n1,2,3,4,5,7,9\n10,11,12 & 13", Toast.LENGTH_LONG).show();
                        status_entry("PRV KWH:" + inputDataVO.getPreviousKwh() + "\nPRS KWH:" + inputDataVO.getPresentKwh() + "\nPRES STATUS:", 2);
                    }
                }
            }
        });
        Btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(Search_By_NameActivity.this,"back",Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /*
        private void kvah_entry(String Msg,int maxLen){
            LayoutInflater layoutInflater = getLayoutInflater();
            View alertLayout = layoutInflater.inflate(R.layout.status_dialog, null);
            TextView textView = alertLayout.findViewById(R.id.textView);
            textView.setText(Msg);
            EditText editText = alertLayout.findViewById(R.id.Status);
            editText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLen)});
            Button Btn_next= (Button) alertLayout.findViewById(R.id.Btn_next);
            Button Btn_back= (Button) alertLayout.findViewById(R.id.Btn_back);
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(Search_By_NameActivity.this);
            alertDialog.setCancelable(false);
            alertDialog.setView(alertLayout);
            editText.setText("1");
            AlertDialog dialog = alertDialog.create();
            editText.setFocusable(true);
            editText.requestFocus();
            Btn_next.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if(editText.getText().length()>0)
                        ReturnRensponse= Integer.parseInt(editText.getText().toString());
                    else
                        ReturnRensponse=1;
                    System.out.println("status_entry ret:" + ReturnRensponse);
                    if (ReturnRensponse == 0) {
                        inputDataVO.setPmtrsts(1);
                        inputDataVO.setKvahMeterStatus(inputDataVO.getPmtrsts());
                        System.out.println("Pmtrsts:" + inputDataVO.getPmtrsts());
                        //kvah_entry();
                    }
                    else {
                        inputDataVO.setPmtrsts(ReturnRensponse);
                        inputDataVO.setKvahMeterStatus(inputDataVO.getPmtrsts());
                        System.out.println("Pmtrsts:" + inputDataVO.getPmtrsts());
                        if((inputDataVO.getPmtrsts()==5)&&(inputDataVO.getTriVectorFlag()==1))
                        {
                            Toast.makeText(Search_By_NameActivity.this,"INVALID COMBINATION\nDOOR LOCK STATUS\nCANNOT BE APPLIED TO\nTRIVECTOR SERVICES.",Toast.LENGTH_LONG).show();
                            status_entry("PRV KWH:"+inputDataVO.getPreviousKwh()+"\nPRS KWH:"+inputDataVO.getPresentKwh()+"\nPRES STATUS:",2);
                        }
                        else if((inputDataVO.getPmtrsts()==1)||(inputDataVO.getPmtrsts()==2)||(inputDataVO.getPmtrsts()==3)||(inputDataVO.getPmtrsts()==4)||(inputDataVO.getPmtrsts()==5)||(inputDataVO.getPmtrsts()==6)||
                                (inputDataVO.getPmtrsts()==7)||(inputDataVO.getPmtrsts()==9)||(inputDataVO.getPmtrsts()==10)||(inputDataVO.getPmtrsts()==11)||(inputDataVO.getPmtrsts()==12)||(inputDataVO.getPmtrsts()==13))
                        {
                            //kvah_entry("Data Entry\nPRS KWH:"+inputDataVO.getPresentKwh()+"\nPRS STS:"+inputDataVO.getPmtrsts());
                        }
                        else
                        {
                            Toast.makeText(Search_By_NameActivity.this,"INVALID METER STATUS\nENTERED!..PLZ ENTER\nONLY VALID STATUS!..\n1,2,3,4,5,7,9\n10,11,12 & 13",Toast.LENGTH_LONG).show();
                            status_entry("PRV KWH:"+inputDataVO.getPreviousKwh()+"\nPRS KWH:"+inputDataVO.getPresentKwh()+"\nPRES STATUS:",2);
                        }
                    }
                }});
            Btn_back.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(Search_By_NameActivity.this,"back",Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }});
            dialog.show();
        }
    */
    private void status_entry(String Msg, int maxLen) {
        inputDataVO.setMeterChangeFlag(0);
        LayoutInflater layoutInflater = getLayoutInflater();
        View alertLayout = layoutInflater.inflate(R.layout.status_dialog, null);
        TextView textView = alertLayout.findViewById(R.id.textView);
        textView.setText(Msg);
        EditText editText = alertLayout.findViewById(R.id.Status);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLen)});
        Button Btn_next = (Button) alertLayout.findViewById(R.id.Btn_next);
        Button Btn_back = (Button) alertLayout.findViewById(R.id.Btn_back);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Search_By_NameActivity.this);
        alertDialog.setCancelable(false);
        alertDialog.setView(alertLayout);
        //editText.setText("1");
        AlertDialog dialog = alertDialog.create();
        editText.setFocusable(true);
        editText.requestFocus();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        Btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (editText.getText().length() > 0)
                    ReturnRensponse = Integer.parseInt(editText.getText().toString());
                else ReturnRensponse = 1;
                System.out.println("status_entry ret:" + ReturnRensponse);
                if (ReturnRensponse == 0) {
                    inputDataVO.setPmtrsts(1);
                    inputDataVO.setKvahMeterStatus(inputDataVO.getPmtrsts());
                    System.out.println("Pmtrsts:" + inputDataVO.getPmtrsts());
                    kvahEntryFunction();
                } else {
                    inputDataVO.setPmtrsts(ReturnRensponse);
                    inputDataVO.setKvahMeterStatus(inputDataVO.getPmtrsts());
                    System.out.println("Pmtrsts:" + inputDataVO.getPmtrsts());
                    if ((inputDataVO.getPmtrsts() == 5) && (inputDataVO.getTriVectorFlag() == 1)) {
                        Toast.makeText(Search_By_NameActivity.this, "INVALID COMBINATION\nDOOR LOCK STATUS\nCANNOT BE APPLIED TO\nTRIVECTOR SERVICES.", Toast.LENGTH_LONG).show();
                        status_entry("PRV KWH:" + inputDataVO.getPreviousKwh() + "\nPRS KWH:" + inputDataVO.getPresentKwh() + "\nPRES STATUS:", 2);
                    } else if ((inputDataVO.getPmtrsts() == 1) || (inputDataVO.getPmtrsts() == 2) || (inputDataVO.getPmtrsts() == 3) || (inputDataVO.getPmtrsts() == 5) || (inputDataVO.getPmtrsts() == 6) || (inputDataVO.getPmtrsts() == 7) || (inputDataVO.getPmtrsts() == 9) || (inputDataVO.getPmtrsts() == 10) || (inputDataVO.getPmtrsts() == 11) || (inputDataVO.getPmtrsts() == 12) || (inputDataVO.getPmtrsts() == 13)) {
                        kvahEntryFunction();
                    } else if (inputDataVO.getPmtrsts() == 4) {
                        Toast.makeText(Search_By_NameActivity.this, "INVALID METER STATUS\nENTERED!..PLZ ENTER\nONLY VALID STATUS!..\n4\n", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Search_By_NameActivity.this, "INVALID METER STATUS\nENTERED!..PLZ ENTER\nONLY VALID STATUS!..\n1,2,3,4,5,7,9\n10,11,12 & 13", Toast.LENGTH_LONG).show();
                        status_entry("PRV KWH:" + inputDataVO.getPreviousKwh() + "\nPRS KWH:" + inputDataVO.getPresentKwh() + "\nPRES STATUS:", 2);
                    }
                }
            }
        });
        Btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(Search_By_NameActivity.this,"back",Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void kvah_entry() {
        if (inputDataVO.getPmtrsts() != 4) {
            if (inputDataVO.getMeter_class() == 1) {

            }
        }
    }

    public void export_KWHreading_entry() {
        inputDataVO.setExportKVAH(0);
        System.out.println("NetMeteringFlag():" + inputDataVO.getNetMeteringFlag());
        if (inputDataVO.getNetMeteringFlag() == 1) {

        } else {
            if ((inputDataVO.getOmtrsts() == 2) || (inputDataVO.getOmtrsts() == 11) || (inputDataVO.getOmtrsts() == 12) || (inputDataVO.getPmtrsts() == 2) || (inputDataVO.getPmtrsts() == 11) || (inputDataVO.getPmtrsts() == 12)) {
                //Approx Units
                ApproxUnits(inputDataVO);
            } else validation();
        }
    }

    private void presentStatusEntry(String Msg, int maxLen) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View alertLayout = layoutInflater.inflate(R.layout.status_dialog, null);
        TextView textView = alertLayout.findViewById(R.id.textView);
        textView.setText(Msg);
        EditText editText = alertLayout.findViewById(R.id.Status);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLen)});
        Button Btn_next = (Button) alertLayout.findViewById(R.id.Btn_next);
        Button Btn_back = (Button) alertLayout.findViewById(R.id.Btn_back);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Search_By_NameActivity.this);
        alertDialog.setCancelable(false);
        alertDialog.setView(alertLayout);
        editText.setText("1");
        AlertDialog dialog = alertDialog.create();
        editText.setFocusable(true);
        editText.requestFocus();
        Btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (editText.getText().length() > 0)
                    ReturnRensponse = Integer.parseInt(editText.getText().toString());
                else ReturnRensponse = 1;
                System.out.println("presentStatusEntry ret:" + ReturnRensponse);
                if (ReturnRensponse == 0) inputDataVO.setPmtrsts(1);
                else inputDataVO.setPmtrsts(ReturnRensponse);
                System.out.println("Pmtrsts:" + inputDataVO.getPmtrsts());
                /*if ((inputDataVO.getPmtrsts() == 5) && (inputDataVO.getTriVectorFlag() == 1)) {
                    Toast.makeText(Search_By_NameActivity.this, "IR/IRDA CONSUMER\nREADINGS CAPTURED \nFROM IR/IRDA SENSORS\nDOOR LOCK STATUS - 5\nNIL CONSUMPTION -9", Toast.LENGTH_LONG).show();
                    return;
                } else if (inputDataVO.getPmtrsts() == 5) {
                    Toast.makeText(Search_By_NameActivity.this, "INVALID COMBINATION\nDOOR LOCK STATUS\nCANNOT BE APPLIED TO\nTRIVECTOR SERVICES", Toast.LENGTH_LONG).show();
                    automaticStatusEntryFunction();
                } else {
                    exportKWHreadingEntryFunction();
                }*/
                /*if (((((inputDataVO.getPmtrsts() == 3) || (inputDataVO.getPmtrsts() == 9) || (inputDataVO.getPmtrsts() == 4) || (inputDataVO.getPmtrsts() == 2) || (inputDataVO.getPmtrsts() == 11) || (inputDataVO.getPmtrsts() == 5)
                        || (inputDataVO.getPmtrsts() == 6) || (inputDataVO.getPmtrsts() == 8) || (inputDataVO.getPmtrsts() == 12))) && (inputDataVO.getTriVectorFlag() != 1))) {
                    Toast.makeText(Search_By_NameActivity.this, "INVALID STATUS", Toast.LENGTH_LONG).show();
                    return;
                } else if (((((inputDataVO.getPmtrsts() == 3) || (inputDataVO.getPmtrsts() == 9) || (inputDataVO.getPmtrsts() == 4) || (inputDataVO.getPmtrsts() == 2) || (inputDataVO.getPmtrsts() == 11) || (inputDataVO.getPmtrsts() == 5)
                        || (inputDataVO.getPmtrsts() == 6) || (inputDataVO.getPmtrsts() == 8) || (inputDataVO.getPmtrsts() == 12))) && (inputDataVO.getNetMeteringFlag() != 1))) {
                    Toast.makeText(Search_By_NameActivity.this, "INVALID STATUS", Toast.LENGTH_LONG).show();
                    return;
                }*/
                if (inputDataVO.getTriVectorFlag() == 1) {
                    if (((inputDataVO.getPmtrsts() == 4) || (inputDataVO.getPmtrsts() == 5) || (inputDataVO.getPmtrsts() == 6) || (inputDataVO.getPmtrsts() == 8) || (inputDataVO.getPmtrsts() == 12))) {
                        Toast.makeText(Search_By_NameActivity.this, "INVALID STATUS", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        exportKWHreadingEntryFunction();
                    }
                } else if (inputDataVO.getNetMeteringFlag() == 1) {
                    if (((inputDataVO.getPmtrsts() == 4) || (inputDataVO.getPmtrsts() == 5) || (inputDataVO.getPmtrsts() == 6) || (inputDataVO.getPmtrsts() == 8) || (inputDataVO.getPmtrsts() == 12))) {
                        Toast.makeText(Search_By_NameActivity.this, "INVALID STATUS", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        exportKWHreadingEntryFunction();
                    }
                } else if (inputDataVO.getClubbingid() != 0) {
                    if (((inputDataVO.getPmtrsts() == 4) || (inputDataVO.getPmtrsts() == 5) || (inputDataVO.getPmtrsts() == 6) || (inputDataVO.getPmtrsts() == 8) || (inputDataVO.getPmtrsts() == 12))) {
                        Toast.makeText(Search_By_NameActivity.this, "INVALID STATUS", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        exportKWHreadingEntryFunction();
                    }
                } else if (((inputDataVO.getPmtrsts() == 3) || (inputDataVO.getPmtrsts() == 9) || (inputDataVO.getPmtrsts() == 4) || (inputDataVO.getPmtrsts() == 2) || (inputDataVO.getPmtrsts() == 11) || (inputDataVO.getPmtrsts() == 5) || (inputDataVO.getPmtrsts() == 6) || (inputDataVO.getPmtrsts() == 8) || (inputDataVO.getPmtrsts() == 12))) {
                    Toast.makeText(Search_By_NameActivity.this, "INVALID STATUS", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    exportKWHreadingEntryFunction();
                }
            }
        });
        Btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(Search_By_NameActivity.this,"back",Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void exportKWHreadingEntryFunction() {
        inputDataVO.setExportKVAH(0);
        if (inputDataVO.getNetMeteringFlag() == 1) {
            if ((inputDataVO.getMeter_reading_mode() == '1') || (inputDataVO.getMeter_reading_mode() == '2')) {
                if (inputDataVO.getPresentExportReading() > inputDataVO.getPreviousExportReading()) {
                    inputDataVO.setExportUnits(inputDataVO.getPresentExportReading() - inputDataVO.getPreviousExportReading());
                    if (inputDataVO.getExportUnits() > (inputDataVO.getConnectedMD() * 150.00)) {
                        Toast.makeText(Search_By_NameActivity.this, "Check the Solar\nConsumption\nExport Units > 150.", Toast.LENGTH_LONG).show();
                        return;
                    } else exportKVAHreadingEntryFunction();
                } else {
                    if ((inputDataVO.getPmtrsts() != 4) && (inputDataVO.getPmtrsts() != 7) && (inputDataVO.getPmtrsts() != 14)) {
                        Toast.makeText(Search_By_NameActivity.this, "PRS SOLAR RDG LESS.", Toast.LENGTH_LONG).show();
                        return;
                    } else exportKVAHreadingEntryFunction();
                }
                return;
            } else {
                //Prs Export KWHreading entry
                PRSexportKWHreadingEntry("PRV KWH EXPO:" + inputDataVO.getPreviousExportReading() + "\nPRS KWH EXPO:", 14);
                return;
            }
        }
        if ((inputDataVO.getOmtrsts() == 2) || (inputDataVO.getOmtrsts() == 11) || (inputDataVO.getOmtrsts() == 12) || (inputDataVO.getPmtrsts() == 2) || (inputDataVO.getPmtrsts() == 11) || (inputDataVO.getPmtrsts() == 12)) {
            //Approx Units
            ApproxUnits(inputDataVO);
        } else validation();
        return;
    }

    public void PRSexportKWHreadingEntry(String Msg, int maxLen) {
        System.out.println("PRSexportKWHreadingEntry");
        LayoutInflater layoutInflater = getLayoutInflater();
        View alertLayout = layoutInflater.inflate(R.layout.kwh_dialog, null);
        TextView textView = alertLayout.findViewById(R.id.textView);
        textView.setText(Msg);
        EditText editText = alertLayout.findViewById(R.id.Status);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLen)});
        Button Btn_next = (Button) alertLayout.findViewById(R.id.Btn_next);
        Button Btn_back = (Button) alertLayout.findViewById(R.id.Btn_back);
        Button Btn_photo = (Button) alertLayout.findViewById(R.id.Btn_photo);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Search_By_NameActivity.this);
        alertDialog.setCancelable(false);
        alertDialog.setView(alertLayout);
        AlertDialog dialog = alertDialog.create();
        editText.setFocusable(true);
        editText.requestFocus();
        textView.setTextSize(20);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        Btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (editText.getText().length() == 0) {
                    return;
                }
                long ret_float = Long.parseLong(editText.getText().toString());
                System.out.println("ret_float:" + ret_float);
                inputDataVO.setPresentExportReading(ret_float);
                if (inputDataVO.getPresentExportReading() > inputDataVO.getPreviousExportReading()) {
                    inputDataVO.setExportUnits(inputDataVO.getPresentExportReading() - inputDataVO.getPreviousExportReading());
                    exportKVAHreadingEntryFunction();
                } else {
                    if ((inputDataVO.getPmtrsts() != 4) && (inputDataVO.getPmtrsts() != 7) && (inputDataVO.getPmtrsts() != 14)) {
                        Toast.makeText(Search_By_NameActivity.this, "PRS SOLAR RDG LESS.", Toast.LENGTH_LONG).show();
                        return;
                    } else exportKVAHreadingEntryFunction();
                }
            }
        });
        Btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void exportKVAHreadingEntryFunction() {
        if ((inputDataVO.getMeter_reading_mode() == '1') || (inputDataVO.getMeter_reading_mode() == '2')) {
            if (inputDataVO.getExportKVAH() < inputDataVO.getPreviousSolarReadingKvah()) {
                if ((inputDataVO.getPmtrsts() != 4) && (inputDataVO.getPmtrsts() != 7) && (inputDataVO.getPmtrsts() != 14)) {
                    Toast.makeText(Search_By_NameActivity.this, "PRS SOLAR KVAH LESS.", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    if ((inputDataVO.getOmtrsts() == 2) || (inputDataVO.getOmtrsts() == 11) || (inputDataVO.getOmtrsts() == 12) || (inputDataVO.getPmtrsts() == 2) || (inputDataVO.getPmtrsts() == 11) || (inputDataVO.getPmtrsts() == 12)) {
                        //Approx Units
                        ApproxUnits(inputDataVO);
                    } else validation();
                    return;
                }
            }
            if ((inputDataVO.getOmtrsts() == 2) || (inputDataVO.getOmtrsts() == 11) || (inputDataVO.getOmtrsts() == 12) || (inputDataVO.getPmtrsts() == 2) || (inputDataVO.getPmtrsts() == 11) || (inputDataVO.getPmtrsts() == 12)) {
                //Approx Units
                ApproxUnits(inputDataVO);
            } else validation();
        } else {
            //PRS export KVAH Reading
            PRSexportKVAHreadingEntry("PRV KVAH EXPO:" + inputDataVO.getPreviousSolarReadingKvah() + "\nPRS KVAH EXPO:", 15);
        }
        return;
    }

    public void PRSexportKVAHreadingEntry(String Msg, int maxLen) {
        System.out.println("PRSexportKVAHreadingEntry");
        LayoutInflater layoutInflater = getLayoutInflater();
        View alertLayout = layoutInflater.inflate(R.layout.kwh_dialog, null);
        TextView textView = alertLayout.findViewById(R.id.textView);
        textView.setText(Msg);
        EditText editText = alertLayout.findViewById(R.id.Status);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLen)});
        Button Btn_next = (Button) alertLayout.findViewById(R.id.Btn_next);
        Button Btn_back = (Button) alertLayout.findViewById(R.id.Btn_back);
        Button Btn_photo = (Button) alertLayout.findViewById(R.id.Btn_photo);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Search_By_NameActivity.this);
        alertDialog.setCancelable(false);
        alertDialog.setView(alertLayout);
        AlertDialog dialog = alertDialog.create();
        editText.setFocusable(true);
        editText.requestFocus();
        textView.setTextSize(20);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        Btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (editText.getText().length() == 0) {
                    return;
                }
                long ret_float = Long.parseLong(editText.getText().toString());
                System.out.println("ret_float:" + ret_float);
                inputDataVO.setExportKVAH(ret_float);
                if (inputDataVO.getExportKVAH() < inputDataVO.getPreviousSolarReadingKvah()) {
                    if ((inputDataVO.getPmtrsts() != 4) && (inputDataVO.getPmtrsts() != 7) && (inputDataVO.getPmtrsts() != 14)) {
                        Toast.makeText(Search_By_NameActivity.this, "PRS SOLAR KVAH LESS.", Toast.LENGTH_LONG).show();
                        exportKVAHreadingEntryFunction();
                    } else {
                        if ((inputDataVO.getOmtrsts() == 2) || (inputDataVO.getOmtrsts() == 11) || (inputDataVO.getOmtrsts() == 12) || (inputDataVO.getPmtrsts() == 2) || (inputDataVO.getPmtrsts() == 11) || (inputDataVO.getPmtrsts() == 12)) {
                            //Approx Units
                            ApproxUnits(inputDataVO);
                        } else validation();
                    }
                } else validation();


            }
        });
        Btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void PFEntry(String Msg, int maxLen) {
        System.out.println("PFEntry");
        LayoutInflater layoutInflater = getLayoutInflater();
        View alertLayout = layoutInflater.inflate(R.layout.kwh_dialog, null);
        TextView textView = alertLayout.findViewById(R.id.textView);
        textView.setText(Msg);
        EditText editText = alertLayout.findViewById(R.id.Status);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLen)});
        Button Btn_next = (Button) alertLayout.findViewById(R.id.Btn_next);
        Button Btn_back = (Button) alertLayout.findViewById(R.id.Btn_back);
        Button Btn_photo = (Button) alertLayout.findViewById(R.id.Btn_photo);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Search_By_NameActivity.this);
        alertDialog.setCancelable(false);
        alertDialog.setView(alertLayout);
        AlertDialog dialog = alertDialog.create();
        editText.setFocusable(true);
        editText.requestFocus();
        textView.setTextSize(20);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        Btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (editText.getText().length() == 0) {
                    return;
                }
                long ret_float = Long.parseLong(editText.getText().toString());
                System.out.println("ret_float:" + ret_float);
                inputDataVO.setPf(ret_float);
                inputDataVO.setMTRaccuracy(0.5F);
                if ((inputDataVO.getPf() <= 0.00) || (inputDataVO.getPf() <= 1.00)) {
                    inputDataVO.setPf(1.00F);
                    float kvah_consumption = ((inputDataVO.getPresentKwh() - inputDataVO.getPreviousKvah()) / inputDataVO.getPf());
                    System.out.println("kvah_consumption:" + kvah_consumption);
                    rmdEntryFunction();
                }
            }
        });
        Btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void PrsKvahEntry(String Msg, int maxLen) {
        System.out.println("PrsKvahEntry");
        LayoutInflater layoutInflater = getLayoutInflater();
        View alertLayout = layoutInflater.inflate(R.layout.kwh_dialog, null);
        TextView textView = alertLayout.findViewById(R.id.textView);
        textView.setText(Msg);
        EditText editText = alertLayout.findViewById(R.id.Status);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLen)});
        Button Btn_next = (Button) alertLayout.findViewById(R.id.Btn_next);
        Button Btn_back = (Button) alertLayout.findViewById(R.id.Btn_back);
        Button Btn_photo = (Button) alertLayout.findViewById(R.id.Btn_photo);
        kwhDialogImageView = alertLayout.findViewById(R.id.Meter_Image_View);


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Search_By_NameActivity.this);
        alertDialog.setCancelable(false);
        alertDialog.setView(alertLayout);
        AlertDialog dialog = alertDialog.create();
        editText.setFocusable(true);
        editText.requestFocus();
        textView.setTextSize(20);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        kwhDialogImageView = alertLayout.findViewById(R.id.Meter_Image_View);
        kwhPhotoBtn = Btn_photo;
        kwhDialogAttemptTv = alertLayout.findViewById(R.id.attemptTv);
        kwhDialogFullImageBtn = alertLayout.findViewById(R.id.imageSheetBtn);
        kwhDialogEditText = editText;
        numberOfTries = 0;
        Btn_photo.setOnClickListener(view -> {
            openOcrCamera(false, MeterType.Kvah);

        });


        Btn_next.setOnClickListener(v -> {
            dialog.dismiss();
            editText.clearFocus();
            if (editText.getText().length() == 0) {
                inputDataVO.setPresentKvah(0);
                rmdEntryFunction();
            } else {
                try {
                    MeterDetails.kvahData.setManual(!MeterDetails.kvahData.getValue().equals(editText.getText().toString()));
                    long ret_float;
                    if (editText.getText().toString().contains(".")) {
                        ret_float = Long.parseLong(editText.getText().toString().split("\\.")[0]);
                    } else {
                        ret_float = Long.parseLong(editText.getText().toString());
                    }
                    inputDataVO.setPresentKvah(ret_float);
                    inputDataVO.setMTRaccuracy(1.00F);
                    rmdEntryFunction();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_SHORT).show();
                }

            }
        });

//        Btn_photo.setOnClickListener(v -> {
//            dialog.dismiss();
//            Intent intent = new Intent(getBaseContext(), ScanAutoAnalogDigitalMeterActivity.class);
//            intent.putExtra("inputDataVO", (Serializable) inputDataVO);
//            intent.putExtra("BillType", "PHOTO");
//            intent.putExtra("unitType", "KVAH");
//            startActivity(intent);
//        });

        Btn_back.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void showkwhAlertDialog(String Msg, int maxLen) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View alertLayout = layoutInflater.inflate(R.layout.kwh_dialog, null);
        TextView textView = alertLayout.findViewById(R.id.textView);
        textView.setText(Msg);
        EditText editText = alertLayout.findViewById(R.id.Status);
        editText.requestFocus();
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLen)});
        Button Btn_next = (Button) alertLayout.findViewById(R.id.Btn_next);
        Button Btn_back = alertLayout.findViewById(R.id.Btn_back);
        Button Btn_photo = alertLayout.findViewById(R.id.Btn_photo);

        kwhDialogImageView = alertLayout.findViewById(R.id.Meter_Image_View);
        kwhPhotoBtn = Btn_photo;
        kwhDialogEditText = editText;
        kwhDialogAttemptTv = alertLayout.findViewById(R.id.attemptTv);
        kwhDialogFullImageBtn = alertLayout.findViewById(R.id.imageSheetBtn);

        numberOfTries = 0;
        Btn_photo.setOnClickListener(view -> {
            openOcrCamera(false, MeterType.kwh);

        });

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Search_By_NameActivity.this);
        alertDialog.setCancelable(false);
        alertDialog.setView(alertLayout);
        AlertDialog dialog = alertDialog.create();
        editText.setFocusable(true);
        editText.requestFocus();
        textView.setTextSize(20);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

//        Btn_photo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                Intent intent = new Intent(getBaseContext(), ScanAutoAnalogDigitalMeterActivity.class);
//                intent.putExtra("inputDataVO", (Serializable) inputDataVO);
//                intent.putExtra("BillType", "PHOTO");
//                intent.putExtra("unitType", "KWH");
//                startActivity(intent);
//                //finish();
//            }
//        });

        Btn_next.setOnClickListener(v -> {
            dialog.dismiss();
            if (editText.getText().length() == 0) {
                showkwhAlertDialog("PRV KWH:" + inputDataVO.getPreviousKwh() + "\nPRS KWH:", 11);
                return;
            }
            try {
                MeterDetails.kvahData.setManual(!MeterDetails.kvahData.getValue().equals(editText.getText().toString()));
                long value;
                if (editText.getText().toString().contains(".")) {
                    value = Long.parseLong(editText.getText().toString().split("\\.")[0]);
                } else {
                    value = Long.parseLong(editText.getText().toString());
                }
                inputDataVO.setPmtrred(value);
                irdaVO.setKWH(value);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }
            inputDataVO.setPresentKwh(inputDataVO.getPmtrred());
            System.out.println("Pmtrred:" + inputDataVO.getPmtrred() + " PresentKwh:" + inputDataVO.getPresentKwh());
            if (status_punched_flag == 0) {
                System.out.println("status_punched_flag:" + status_punched_flag + " meterChangeFlag:" + inputDataVO.getMeterChangeFlag());
                if (inputDataVO.getMeterChangeFlag() != 1) {
                    status_entry("PRV KWH:" + inputDataVO.getPreviousKwh() + "\nPRS KWH:" + inputDataVO.getPresentKwh() + "\nPRES STATUS:", 2);
                } else {
                    Toast.makeText(Search_By_NameActivity.this, "PRES STATUS : 04", Toast.LENGTH_LONG).show();
                    inputDataVO.setPmtrsts(14);
                    inputDataVO.setKvahMeterStatus(14);
                    export_KWHreading_entry();
                }
                return;
            }
            export_KWHreading_entry();
        });
        Btn_back.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void sendSMS(String phoneNumber, String message) {
        try {
            String SENT = "SMS_SENT";
            String DELIVERED = "SMS_DELIVERED";

            PendingIntent sentPI = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(SENT), 0);

            PendingIntent deliveredPI = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(DELIVERED), 0);

            // ---when the SMS has been sent---
            registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context arg0, Intent arg1) {
                    switch (getResultCode()) {
                        case Activity.RESULT_OK:
                            Toast.makeText(getBaseContext(), "SMS sent", Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                            Toast.makeText(getBaseContext(), "Generic failure", Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_NO_SERVICE:
                            Toast.makeText(getBaseContext(), "No service", Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_NULL_PDU:
                            Toast.makeText(getBaseContext(), "Null PDU", Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                            Toast.makeText(getBaseContext(), "Radio off", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(getBaseContext(), "SMS delivered", Toast.LENGTH_SHORT).show();
                            break;
                        case Activity.RESULT_CANCELED:
                            Toast.makeText(getBaseContext(), "SMS not delivered", Toast.LENGTH_SHORT).show();
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


    private void openBlueToothPrinterConnection() {
        if (CommonFunctions.isMachineOrMobileDevice().equals("mobile")) {

            String printerType = new Config_SharedPreferances().getBtNameAndAddress(Search_By_NameActivity.this);

            String[] str = printerType.split("@@");
            String btPrinterName = str[0];
            String btPrinterAddress = str[1];
            if ((btPrinterName.toUpperCase().contains("AT2T")) || (btPrinterName.toUpperCase().contains("2TV"))) {
                try {
                    conn.openBT(btPrinterAddress);
                    isAnalogicsPrinter = true;
                } catch (Exception ex) {
                    isAnalogicsPrinter = false;
                    ex.printStackTrace();
                }
            } else {
                isAnalogicsPrinter = false;
            }
        }
    }

    private void openDevicePrinter() {
        try {
            String ptrType = new Config_SharedPreferances().getPrinterTypeValues(getApplicationContext());

            if (ptrType.equals("2T")) {
                PrinterLib printerAPI = new PrinterLib();
                printApi = new RP_Printer_2inch_prof_ThermalAPI();
                ptrType = new Config_SharedPreferances().getPrinterTypeValues(getApplicationContext());
                printerAPI.PortOpen(portNo1.getBytes());
            }
        } catch (Exception ex) {

        }

    }

    class MyPrinterInitListener implements PrinterInitListener {

        @Override
        public void onPrinterInit(boolean isSuccess) {

        }
    }

    static class MyPrinterListener implements PrinterListener {

        @Override
        public void onPrinterOutOfPaper() {
            System.out.println("Out of paper");
        }

        @Override
        public void onPrinterStart(String templateName) {
            System.out.println("Print start");
        }

        @Override
        public void onPrinterEnd(String templateName) {
            System.out.println("Print end");
        }

        @Override
        public void onPrinterError(int errorCode, String errorMsg) {
            System.out.println("Print error");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;

        String meterNumber = data.getStringExtra("meterNumber");

        if (MeterDetails.outputBase64 != null && kwhDialogImageView != null) {
            byte[] decodedString = Base64.decode(MeterDetails.outputBase64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            kwhDialogImageView.setVisibility(View.VISIBLE);
            kwhDialogImageView.setImageBitmap(decodedByte);
            kwhDialogFullImageBtn.setOnClickListener(view -> {
                ImageSheet imageSheet = new ImageSheet(MeterDetails.fullImageBitmap);
                imageSheet.show(getSupportFragmentManager(), "");
            });
            kwhDialogFullImageBtn.setOnClickListener(view -> {
                ImageSheet imageSheet = new ImageSheet(MeterDetails.fullImageBitmap);
                imageSheet.show(getSupportFragmentManager(), "");
            });
        } else if (kwhDialogImageView != null) {
            kwhDialogImageView.setVisibility(View.GONE);
        }

        if (meterNumber != null && kwhDialogEditText != null) {
            numberOfTries = numberOfTries + 1;
            kwhDialogAttemptTv.setText("Attempts " + numberOfTries);
            if (numberOfTries == 3) {
                numberOfTries = 0;
                kwhPhotoBtn.setVisibility(View.GONE);

            } else {
                kwhPhotoBtn.setVisibility(View.VISIBLE);
            }
            kwhDialogEditText.setText(meterNumber);

        }
    }
}