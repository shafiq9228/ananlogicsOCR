package com.analogics.ui.billing;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.analogics.DBAdapter.BillingDAO;
import com.analogics.DBAdapter.DBAdapter;
import com.analogics.R;
import com.analogics.appUtils.Config_SharedPreferances;
import com.analogics.pojo.ConsumerDataVO;
import com.analogics.pojo.IrdaVO;
import com.analogics.pojo.inputDataVO;
import com.analogics.thermalprinter.AnalogicsThermalPrinter;
import com.analogics.ui.menu.BillingMenuActivity;
import com.analogics.utils.AlertMessage;
import com.analogics.utils.CommonFunctions;
import com.analogics.utils.PublicVariables;
import com.whty.smartpos.tysmartposapi.ITYSmartPosApi;
import com.whty.smartpos.tysmartposapi.modules.printer.PrintElement;
import com.whty.smartpos.tysmartposapi.modules.printer.PrinterConstant;
import com.whty.smartpos.tysmartposapi.modules.printer.PrinterInitListener;
import com.whty.smartpos.tysmartposapi.modules.printer.PrinterListener;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Billing_SearchBy_Activity extends Activity {

    String portNo1 = "/dev/ttyHS0";
    ConsumerDataVO consumerDataVO = new ConsumerDataVO();
    inputDataVO inputDataVO = new inputDataVO();
    IrdaVO irdaVO = new IrdaVO();
    //String[] sel_searchBy = { "Meter Number","USC Code","Consumer Name", "Service Number"};
    String[] sel_searchBy = {"NORMAL", "A4 SRV6", "A4 SRV5", "A5 SRV5", "SRVNO13", "CITY", "5SP5", "A4 2 S5", "A5 SRV"};
    Spinner SP_SearchBy;
    //AutoCompleteTextView ACT_searchby;
    Button Btn_GetDetails;
    EditText ET_ServiceNo;
    EditText ET_AreaCode;
    EditText ET_SrvEntered;
    EditText ET_Name;
    EditText ET_Address;
    EditText ET_Address1;
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

    Button Btn_Back;
    Button Btn_Ok;
    Button Btn_Next;
    Button Btn_home;
    int sequence_count = 1;
    ArrayList<String> routeSequence = new ArrayList<String>();
    DBAdapter dbAdapter = null;
    Cursor readrecord = null;
    String searchby_type = "Service Number";
    String type;
    AnalogicsThermalPrinter conn;
    private boolean isAnalogicsPrinter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.newbilling_searchby);

        Intent intent = getIntent();
        type = intent.getStringExtra("SearchType") + "";
        if (type.equalsIgnoreCase("Automatic")) {
            irdaVO = (IrdaVO) intent.getSerializableExtra("IRDAVO");
        }
        SP_SearchBy = findViewById(R.id.SP_SearchBy);
        Btn_GetDetails = findViewById(R.id.Btn_GetDetails);
        ET_AreaCode = findViewById(R.id.ET_AreaCode);
        ET_SrvEntered = findViewById(R.id.ET_SrvEntered);

        ET_ServiceNo = findViewById(R.id.ET_ServiceNo);
        ET_Name = findViewById(R.id.ET_Name);
        ET_Address = findViewById(R.id.ET_Address);
        ET_Address1 = findViewById(R.id.ET_Address1);
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

        //Btn_Back = findViewById(R.id.Btn_Back);
        Btn_Ok = findViewById(R.id.Btn_Ok);
        //Btn_Next = findViewById(R.id.Btn_Next);
        Btn_home = findViewById(R.id.Btn_home);
        openBlueToothPrinterConnection();
        goneVisibilityForSomeWidgets();

        Btn_home.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Billing_SearchBy_Activity.this, BillingMenuActivity.class); //changed 070923
                startActivity(i);
                finish();
            }
        });
        dbAdapter = DBAdapter.getDBAdapterInstance(this);
        SP_SearchBy.getBackground().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
        ArrayAdapter<String> adapter_SearchBy = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sel_searchBy);
        adapter_SearchBy.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SP_SearchBy.setAdapter(adapter_SearchBy);
        SP_SearchBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                searchby_type = SP_SearchBy.getSelectedItem().toString();
                //ACT_searchby.setHint(searchby_type+"");

                ET_ServiceNo.setText("");
                ET_Name.setText("");
                ET_Address.setText("");
                ET_Address1.setText("");
                ET_MeterNo.setText("");
                ET_USCNo.setText("");
                ET_Catagory.setText("");
                ET_SubCatagory.setText("");
                ET_Phase.setText("");
                ET_ContractedLoad.setText("");
                ET_ConnectedLoad.setText("");
                ET_MeterTC_Seal.setText("");
                ET_MeterMake.setText("");
                ET_MeterCapacity.setText("");
                //ACT_searchby.setText("");
                ET_PreviousMeterStatus.setText("");
                ET_Old_Average_Units.setText("");
                ET_PreviousReadingKWH.setText("");
                ET_PreviousReadingKVah.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Btn_Ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                int billed_count = 0;
                dbAdapter = DBAdapter.getDBAdapterInstance(Billing_SearchBy_Activity.this);
                dbAdapter.openDataBase();
                String already_billed = "select count(*) from output_master2 where consumer_num='" + ET_ServiceNo.getText().toString() + "';";
                Cursor cursor = dbAdapter.selectRecordsFromDB(already_billed, null);
                if (cursor.moveToFirst()) {
                    billed_count = cursor.getInt(0);
                }
                dbAdapter.close();
                if (billed_count > 0) {//ALREADY BILLED\nDUPLICATE BILL(Y/N)
                    AlertDialog.Builder builder = new AlertDialog.Builder(Billing_SearchBy_Activity.this);
                    builder.setTitle("Alert");
                    builder.setMessage("Already Billed. Duplicate Bill(Y/N)");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing but close the dialog
                            PublicVariables.isDuplicateSlip = true;
                            dialog.dismiss();
                            dbAdapter.openDataBase();
                            String already_billed = "select duplicateBillData,QrInformation from duplicateBillPurpose where consumer_num='" + ET_ServiceNo.getText().toString().trim() + "';";
                            Cursor cursor = dbAdapter.selectRecordsFromDB(already_billed, null);
                            if (cursor.moveToFirst()) {
                                inputDataVO.setDuplicatePrintDT(cursor.getString(0));
                                inputDataVO.setQTPrintData(cursor.getString(1));
                            }
                            PublicVariables.isDuplicateSlip = true;
                            dbAdapter.close();
                            printBill w = new printBill();
                            w.execute();
                            return;
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
                } else {

                    dbAdapter = DBAdapter.getDBAdapterInstance(Billing_SearchBy_Activity.this);
                    dbAdapter.openDataBase();
                    String query = "select * from INPUT_MASTER where consumer_num like '%" + ET_ServiceNo.getText().toString() + "" + "%';";
                    readrecord = dbAdapter.selectRecordsFromDB(query, null);
                    if (readrecord.moveToFirst()) {
                        if (type.equalsIgnoreCase("Automatic")) {
                            Intent intent = new Intent(Billing_SearchBy_Activity.this, Search_By_NameActivity.class);//Search_By_NameActivity.class
                            intent.putExtra("SearchType", "Automatic");
                            intent.putExtra("ServiceNoToSearch", ET_ServiceNo.getText().toString());
                            intent.putExtra("IRDAVO", (Serializable) irdaVO);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(Billing_SearchBy_Activity.this, Search_By_NameActivity.class);
                            intent.putExtra("SearchType", "ManualSearch");
                            intent.putExtra("ServiceNoToSearch", ET_ServiceNo.getText().toString());
                            startActivity(intent);
                            finish();
                        }
                    } else
                        new AlertMessage().alertMessage(Billing_SearchBy_Activity.this, "Alert", "Meter Not found.");


                }
            }
        });

	/*	Btn_Ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				int billed_count=0;
				dbAdapter.openDataBase();
				String already_billed = "select count(*) from upload_data where cast(Usc_Code as int)='"
						+ ET_USCNo.getText().toString() + "';";
				Cursor cursor = dbAdapter.selectRecordsFromDB(
						already_billed, null);
				if (cursor.moveToFirst()) {
					billed_count = cursor.getInt(0);
				}
				dbAdapter.close();
				if(billed_count>0){
					AlertDialog alert = new AlertDialog.Builder(
							Billing_SearchBy_Activity.this).create();
					alert.setTitle("VALIDATION");
					// alert.setIcon(R.drawable.warning);
					alert.setMessage("Already Billed ");
					alert.setButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
                                                    int which) {
									
								}
							});
					alert.show();
					return;
				}
				else{
				//AANIL
				//	Intent intent=new Intent(Billing_Sequence_Activity.this,Billing_seqselect_Activity.class);
				//	intent.putExtra("accountid", ET_AccountId.getText().toString());
				//	startActivity(intent);
				//	finish();
					Toast.makeText(Billing_SearchBy_Activity.this,"Not billed",Toast.LENGTH_LONG).show();

				}
			}
		});
*/


        Btn_GetDetails.setOnClickListener(arg0 -> {
            hideKeyBoard(Billing_SearchBy_Activity.this);
            dbAdapter.openDataBase();

            try {
                String query = "";
                int searchType = 0;
                if (SP_SearchBy.getSelectedItem().toString() == "A4 SRV6")
                    query = "select * from INPUT_MASTER where consumer_num like '" + StringUtils.rightPad(ET_AreaCode.getText().toString(), 4) + StringUtils.leftPad(ET_SrvEntered.getText().toString(), 6, "0") + "%'";
                else if (SP_SearchBy.getSelectedItem().toString() == "A4 SRV5")
                    query = "select * from INPUT_MASTER where consumer_num like '" + StringUtils.rightPad(ET_AreaCode.getText().toString(), 4) + " " + StringUtils.leftPad(ET_SrvEntered.getText().toString(), 5, "0") + "%'";
                else if (SP_SearchBy.getSelectedItem().toString() == "A5 SRV5")
                    query = "select * from INPUT_MASTER where consumer_num like '" + StringUtils.rightPad(ET_AreaCode.getText().toString(), 5) + StringUtils.leftPad(ET_SrvEntered.getText().toString(), 5, "0") + "%'";
                else
                    query = "select * from INPUT_MASTER where consumer_num like '" + ET_AreaCode.getText() + "%" + ET_SrvEntered.getText() + "%'";

                System.out.println("Query>>" + query);
                readrecord = dbAdapter.selectRecordsFromDB(query, null);
                if (readrecord.moveToFirst()) {
                    ET_ServiceNo.setText(readrecord.getString(0).trim());
                    ET_Name.setText(readrecord.getString(3).trim());
                    ET_Address.setText(readrecord.getString(4).trim());
                    ET_Address1.setText(readrecord.getString(5).trim());
                    ET_MeterNo.setText(readrecord.getString(1).trim());
                    ET_USCNo.setText(readrecord.getString(2).trim());
                    ET_Catagory.setText(readrecord.getString(10).trim());
                    ET_SubCatagory.setText(readrecord.getString(11).trim());
                    ET_Phase.setText(readrecord.getString(12).trim());

                    inputDataVO.setLoc(readrecord.getString(0).substring(0, 5));
                    inputDataVO.setLoc1(readrecord.getString(0).substring(5, 8));
                    inputDataVO.setSerno(readrecord.getString(0).substring(8, 13));
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
                    inputDataVO.setOmtrsts(readrecord.getInt(16));
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
                        //  Toast.makeText(this, "Error "+ex, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    consumerDataVO = new ConsumerDataVO();
                    ET_ServiceNo.setText("");
                    ET_Name.setText("");
                    ET_Address.setText("");
                    ET_Address1.setText("");
                    ET_MeterNo.setText("");
                    ET_USCNo.setText("");
                    ET_Catagory.setText("");
                    ET_SubCatagory.setText("");
                    ET_Phase.setText("");
                    ET_ContractedLoad.setText("");
                    ET_ConnectedLoad.setText("");
                    ET_MeterTC_Seal.setText("");
                    ET_MeterMake.setText("");
                    ET_MeterCapacity.setText("");
                    //ACT_searchby.setText("");
                    ET_PreviousMeterStatus.setText("");
                    ET_Old_Average_Units.setText("");
                    ET_PreviousReadingKWH.setText("");
                    ET_PreviousReadingKVah.setText("");
                }
                readrecord.close();
                dbAdapter.close();

            } catch (Exception ex) {
                // Toast.makeText(Billing_SearchBy_Activity.this, "Error "+ex, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openBlueToothPrinterConnection() {
        if (CommonFunctions.isMachineOrMobileDevice().equals("mobile")) {

            try {
                String printerType = new Config_SharedPreferances().getBtNameAndAddress(Billing_SearchBy_Activity.this);

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
            } catch (Exception ignored) {
            }
        }
    }

    private void goneVisibilityForSomeWidgets() {

    }

    public static void hideKeyBoard(Context mContext) {
        if (((Activity) mContext).getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) (mContext).getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(((Activity) mContext).getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void update_autocomplete(int type, String districtName) {
        // TODO Auto-generated method stub
        List<String> resultlist = new BillingDAO().update_autocomplete(type, districtName);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Billing_SearchBy_Activity.this, android.R.layout.simple_dropdown_item_1line, resultlist);

        //ACT_searchby.setAdapter(adapter);
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

    private class printBill extends AsyncTask<byte[], String, String> {
        String phase = "";
        ProgressDialog p;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(Billing_SearchBy_Activity.this);
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

                    if(PublicVariables.isDuplicateSlip){
                        tyApi.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        tyApi.setLineSpace(1);
                        String duplicatePrint = inputDataVO.getDuplicatePrintDT();
                        String[] boldText = duplicatePrint.split("\n");
                        tyApi.appendPrintElement(new PrintElement("DUPLICATE BILL", PrinterConstant.Align.ALIGN_CENTER));
                        for(int i=0;i<boldText.length;i++){
                            if(StringUtils.containsIgnoreCase(boldText[i], "SC.NO:") ||
                                    StringUtils.containsIgnoreCase(boldText[i], "USC:") ||
                                    StringUtils.containsIgnoreCase(boldText[i], "TOTAL AMOUNT")){
                                tyApi.appendPrintElement(new PrintElement(boldText[i], PrinterConstant.Align.ALIGN_LEFT, PrinterConstant.FontSize.FONT_SIZE_LARGE));
                            }else{
                                tyApi.appendPrintElement(new PrintElement(boldText[i], PrinterConstant.Align.ALIGN_CENTER));
                            }
                        }
//                        tyApi.printText(inputDataVO.getDuplicatePrintDT() + "\n");
                        tyApi.appendPrintElement(new PrintElement("\n", PrinterConstant.Align.ALIGN_CENTER));
                        tyApi.appendPrintElement(new PrintElement("\n", PrinterConstant.Align.ALIGN_CENTER));
                        tyApi.startPrintElement();

                    }else{
//                        Typeface font = Typeface.createFromAsset(getAssets(),"fonts/OpenSans_SemiCondensed-Bold.ttf");
//
//                        tyApi.setTypeface(Typeface.create(font, Typeface.BOLD));
//                        Bundle bundle = new Bundle();
//                        bundle.putInt(PrinterConfig.ALIGN, PrinterConstant.Align.ALIGN_CENTER);
//                        bundle.putInt(PrinterConfig.EN_FONT, font.getStyle());
//                        bundle.putInt(PrinterConfig.FONT_SIZE,5);
//                        tyApi.setPrinterParameters(bundle);
//
//                        tyApi.setLineSpace(1);
//                        tyApi.setTypeface(Typeface.create(font, Typeface.BOLD));
//
//                        String printData = inputDataVO.getDuplicatePrintDT();
//                        String[] data=printData.split("\n");
//
//                        for(int i=0;i<data.length;i++){
//                            tyApi.appendPrintElement(new PrintElement(data[i], PrinterConstant.Align.ALIGN_CENTER));
//                        }


//                        tyApi.setLineSpace(5);
//                        tyApi.printText(inputDataVO.getPrintHead() + "\n");
//                        tyApi.appendPrintElement(new PrintElement(inputDataVO.getPrintBoldText1(), PrinterConstant.Align.ALIGN_LEFT, PrinterConstant.FontSize.FONT_SIZE_MIDDLE_WIDTH,true));
//                        tyApi.printText(inputDataVO.getPrintBody() + "\n");
//                        tyApi.appendPrintElement(new PrintElement(inputDataVO.getPrintBoldText2(), PrinterConstant.Align.ALIGN_LEFT, PrinterConstant.FontSize.FONT_SIZE_LARGE,true));
//                        tyApi.printText(inputDataVO.getPrintFoot() + "\n");


                        tyApi.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

                        tyApi.setLineSpace(1);
                        String[] headerData=inputDataVO.getPrintHead().split("\n");

                        for(int i=0;i<headerData.length;i++){
                            tyApi.appendPrintElement(new PrintElement(headerData[i], PrinterConstant.Align.ALIGN_CENTER));

                        }
                        String[] boldText1=inputDataVO.getPrintBoldText1().split("\n");

                        for(int i=0;i<boldText1.length;i++){
                            //tyApi.appendPrintElement(new PrintElement(boldText1[i], PrinterConstant.Align.ALIGN_LEFT));
                            tyApi.appendPrintElement(new PrintElement(boldText1[i], PrinterConstant.Align.ALIGN_LEFT, PrinterConstant.FontSize.FONT_SIZE_LARGE));

                        }

                        String[] body=inputDataVO.getPrintBody().split("\n");

                        for(int i=0;i<body.length;i++){
                            tyApi.appendPrintElement(new PrintElement(body[i], PrinterConstant.Align.ALIGN_CENTER));

                        }

                        String[] boldText2=inputDataVO.getPrintBoldText2().split("\n");

                        for(int i=0;i<boldText2.length;i++){
                            //tyApi.appendPrintElement(new PrintElement(boldText2[i], PrinterConstant.Align.ALIGN_LEFT));
                            tyApi.appendPrintElement(new PrintElement(inputDataVO.getPrintBoldText2(), PrinterConstant.Align.ALIGN_LEFT, PrinterConstant.FontSize.FONT_SIZE_LARGE));

                        }
                        String[] foot=inputDataVO.getPrintFoot().split("\n");

                        for(int i=0;i<foot.length;i++){
                            tyApi.appendPrintElement(new PrintElement(foot[i], PrinterConstant.Align.ALIGN_CENTER));
                        }
                        tyApi.appendPrintElement(new PrintElement("\n", PrinterConstant.Align.ALIGN_CENTER));
                        tyApi.appendPrintElement(new PrintElement("\n", PrinterConstant.Align.ALIGN_CENTER));
                        tyApi.startPrintElement();
                    }
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
}
