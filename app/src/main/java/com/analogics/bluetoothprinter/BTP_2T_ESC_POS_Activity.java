package com.analogics.bluetoothprinter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.analogics.R;
import com.analogics.thermalAPI.ESC_POS_2T_BTPrinterAPI;
import com.analogics.thermalprinter.AnalogicsThermalPrinter;
import com.analogics.ui.menu.MainMenuActivity;

import java.io.IOException;


public class BTP_2T_ESC_POS_Activity extends Activity {

    protected static final int CAMERA_PIC_REQUEST = 1337;
    protected static final int CAMERA_IMAGE_CAPTUE_OK = -1;
    static String imagesDir = Environment.getExternalStorageDirectory() + "";
    String capturedImgSave = "";

    AnalogicsThermalPrinter printer= null;

    private final static String TAG = BTP_2T_ESC_POS_Activity.class.getSimpleName();

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    private static final int PERMISSION_REQUEST_CODE = 2296;
    private static final int REQUEST_ENABLE_BT = 1;

    TextView mConnectionState;
    private TextView mDataField;

    EditText ET_PrintData;
    Button btn_InitializePrinter;
    Button btn_fonts;
    Button btnLeft;
    Button btn_Center;
    Button btn_Right;
    Button btn_FontA;
    Button btn_FontB;
    Button btn_FontC;
    Button btn_EmphasizedON;
    Button btn_EmphasizedOFF;
    Button btn_UnderlineON;
    Button btn_UnderlineOFF;
    Button btn_IMagePrint;
    Button btn_doubleStrikeModeON;
    Button btn_doubleStrikeModeOFF;
    Button btn_QrcodePrint;
    Button btn_ReceiptPrint;
    Button Btn_BlackmarkSensor;
    Button  Btn_BillReceiptPrint;


    Button Btn_BTType;
    Button Btn_Connect;
    Button Btn_Disconnect;

    Handler handler;

    private String mDeviceName;
    public String mDeviceAddress;
    private boolean mConnected = false;

    private void clearUI() {
        //mGattServicesList.setAdapter((SimpleExpandableListAdapter) null);
        mDataField.setText(R.string.no_data);
    }

    @SuppressLint("MissingPermission")
    public int getBluetoothType(){
        int type=0;
        BluetoothManager mBluetoothManager = null;
        BluetoothAdapter mBluetoothAdapter;
        {
            // For API level 18 and above, get a reference to BluetoothAdapter through
            // BluetoothManager.

            if (mBluetoothManager == null) {
                mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
                if (mBluetoothManager == null) {
                    Log.e(TAG, "Unable to initialize BluetoothManager.");
                }
         }

            mBluetoothAdapter = mBluetoothManager.getAdapter();
            if (mBluetoothAdapter == null) {
                Log.e(TAG, "Unable to obtain a BluetoothAdapter.");

            }
        }
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(mDeviceAddress);
        device.getName();
        type=device.getType();
        //Toast.makeText(this, "initialize() >>  "+initialize(), Toast.LENGTH_SHORT).show();
       // Toast.makeText(DeviceControlActivity.this, "device.getName() >>  "+device.getName(), Toast.LENGTH_SHORT).show();
       // Toast.makeText(DeviceControlActivity.this, "device.getType() >>  "+device.getType(), Toast.LENGTH_SHORT).show();
     //   BluetoothClass bc=device.getBluetoothClass();
       // Toast.makeText(DeviceControlActivity.this, "bc.getDeviceClass() >>  "+bc.getDeviceClass(), Toast.LENGTH_SHORT).show();
        mBluetoothAdapter=null;
        mBluetoothManager=null;
        device=null;
      return type;

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.button_2t_esc_pos_control);

        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
        printer= new AnalogicsThermalPrinter(BTP_2T_ESC_POS_Activity.this);
        // Sets up UI references.
        /*
        ((TextView) findViewById(R.id.device_address)).setText(mDeviceAddress);
        mGattServicesList = (ExpandableListView) findViewById(R.id.gatt_services_list);
        mGattServicesList.setOnChildClickListener(servicesListClickListner);
    mConnectionState = (TextView) findViewById(R.id.connection_state);
    */
        mConnectionState = (TextView) findViewById(R.id.connection_state);
        mDataField = (TextView) findViewById(R.id.data_value);
        ET_PrintData = (EditText) findViewById(R.id.ET_PrintData);
        Btn_Connect=(Button) findViewById(R.id.Btn_Connect);
        Btn_Disconnect=(Button) findViewById(R.id.Btn_Disconnect);

         btn_InitializePrinter= findViewById(R.id.btn_InitializePrinter);

         btn_fonts = findViewById(R.id.btn_fonts);
         btnLeft= findViewById(R.id.btnLeft);
         btn_Center= findViewById(R.id.btn_Center);
         btn_Right= findViewById(R.id.btn_Right);
         btn_FontA= findViewById(R.id.btn_FontA);
         btn_FontB= findViewById(R.id.btn_FontB);
         btn_FontC= findViewById(R.id.btn_FontC);
         btn_EmphasizedON= findViewById(R.id.btn_EmphasizedON);
         btn_EmphasizedOFF= findViewById(R.id.btn_EmphasizedOFF);
         btn_UnderlineON= findViewById(R.id.btn_UnderlineON);
         btn_UnderlineOFF= findViewById(R.id.btn_UnderlineOFF);
         btn_IMagePrint= findViewById(R.id.btn_IMagePrint);
         btn_doubleStrikeModeON= findViewById(R.id.btn_doubleStrikeModeON);
         btn_doubleStrikeModeOFF= findViewById(R.id.btn_doubleStrikeModeOFF);
         btn_QrcodePrint= findViewById(R.id.btn_QrcodePrint);
         btn_ReceiptPrint= findViewById(R.id.btn_ReceiptPrint);
         Btn_BlackmarkSensor= findViewById(R.id.Btn_BlackmarkSensor);
         Btn_BillReceiptPrint= findViewById(R.id.Btn_BillReceiptPrint);
        
        
            handler = new Handler();

        Runnable r = new Runnable() {
            public void run() {

                Boolean status=printer.isConnected();

                if(status) {
                    Btn_Connect.setEnabled(false);
                    Btn_Disconnect.setEnabled(true);

                    Btn_Connect.setVisibility(View.GONE);
                    Btn_Disconnect.setVisibility(View.VISIBLE);
                }else{

                    Btn_Connect.setEnabled(true);
                    Btn_Disconnect.setEnabled(false);
                    Btn_Connect.setVisibility(View.VISIBLE);
                    Btn_Disconnect.setVisibility(View.GONE);

                }
                handler.postDelayed(this, 1000);
            }
        };

        handler.postDelayed(r, 1000);


        try {
            printer.openBT(mDeviceAddress);

        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(BTP_2T_ESC_POS_Activity.this,mDeviceAddress+">>"+ printer.isConnected(),Toast.LENGTH_SHORT).show();

        btn_InitializePrinter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                ESC_POS_2T_BTPrinterAPI btpAPI = new ESC_POS_2T_BTPrinterAPI();
            printer.printData(btpAPI.initializePrinter());


        } });

        btn_fonts.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {


                    String msg = ET_PrintData.getText().toString();
            if (msg.length() > 0) {

                ESC_POS_2T_BTPrinterAPI btpAPI = new ESC_POS_2T_BTPrinterAPI();

                printer.printData(btpAPI.initializePrinter());

                printer.printData(btpAPI.setFontSize(1));
                printer.printData(msg );
                printer.printData(btpAPI.setFontSize(2));
                printer.printData(msg );
                printer.printData(btpAPI.setFontSize(3));
                printer.printData(msg );
                printer.printData(btpAPI.setFontSize(4));
                printer.printData(msg );
                Toast.makeText(BTP_2T_ESC_POS_Activity.this,msg,Toast.LENGTH_LONG).show();
            }
        }});


        btnLeft.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


            String msg = ET_PrintData.getText().toString();
            if (msg.length() > 0) {



                ESC_POS_2T_BTPrinterAPI btpAPI = new ESC_POS_2T_BTPrinterAPI();

                printer.printData(btpAPI.alignLeft());
                printer.printData(msg );

            }
        }});


        btn_Right.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


            String msg = ET_PrintData.getText().toString();
            if (msg.length() > 0) {


                ESC_POS_2T_BTPrinterAPI btpAPI = new ESC_POS_2T_BTPrinterAPI();

                printer.printData(btpAPI.alignRight());
                printer.printData(msg );


            }
        } });


        btn_Center.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                    String msg = ET_PrintData.getText().toString();
                    if (msg.length() > 0) {


                        ESC_POS_2T_BTPrinterAPI btpAPI = new ESC_POS_2T_BTPrinterAPI();

                        printer.printData(btpAPI.alignCenter());
                        printer.printData(msg);


                    }
                }
            });

        btn_FontA.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

            String msg = ET_PrintData.getText().toString();
            if (msg.length() > 0) {



                ESC_POS_2T_BTPrinterAPI btpAPI = new ESC_POS_2T_BTPrinterAPI();

                printer.printData(btpAPI.characterFontA());
                printer.printData(msg );



            }
        }
                });

                btn_FontB.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

            String msg = ET_PrintData.getText().toString();
            if (msg.length() > 0) {



                ESC_POS_2T_BTPrinterAPI btpAPI = new ESC_POS_2T_BTPrinterAPI();

                printer.printData(btpAPI.characterFontB());
                printer.printData(msg );



            }
        } });

        btn_FontC.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {

            String msg = ET_PrintData.getText().toString();
            if (msg.length() > 0) {


                ESC_POS_2T_BTPrinterAPI btpAPI = new ESC_POS_2T_BTPrinterAPI();

                printer.printData(btpAPI.characterFontC());
                printer.printData(msg );



            }
        }});

        btn_EmphasizedON.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {

            String msg = ET_PrintData.getText().toString();
            if (msg.length() > 0) {

                ESC_POS_2T_BTPrinterAPI btpAPI = new ESC_POS_2T_BTPrinterAPI();
                printer.printData(btpAPI.emphasizedOn());
                printer.printData(msg);



            }
        }});

                        btn_EmphasizedOFF.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

            String msg = ET_PrintData.getText().toString();
            if (msg.length() > 0) {

                ESC_POS_2T_BTPrinterAPI btpAPI = new ESC_POS_2T_BTPrinterAPI();

                printer.printData(btpAPI.emphasizedOff());
                printer.printData(msg);


            }
        }});

        btn_UnderlineON.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {


            String msg = ET_PrintData.getText().toString();
            if (msg.length() > 0) {

                ESC_POS_2T_BTPrinterAPI btpAPI = new ESC_POS_2T_BTPrinterAPI();

                printer.printData(btpAPI.underlineOn());
                printer.printData(msg);


            }
        }});

                                btn_EmphasizedOFF.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v) {

            String msg = ET_PrintData.getText().toString();
            if (msg.length() > 0) {

                ESC_POS_2T_BTPrinterAPI btpAPI = new ESC_POS_2T_BTPrinterAPI();

                printer.printData(btpAPI.underlineOff());
                printer.printData(msg);


            }
        }});

        btn_doubleStrikeModeON.setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View v) {


            String msg = ET_PrintData.getText().toString();
            if (msg.length() > 0) {

                ESC_POS_2T_BTPrinterAPI btpAPI = new ESC_POS_2T_BTPrinterAPI();

                printer.printData(btpAPI.doubleStrikeModeON());
                printer.printData(msg);


            }
        }});

                                        btn_doubleStrikeModeOFF.setOnClickListener(new View.OnClickListener() {
                                            public void onClick(View v) {


            String msg = ET_PrintData.getText().toString();
            if (msg.length() > 0) {

                ESC_POS_2T_BTPrinterAPI btpAPI = new ESC_POS_2T_BTPrinterAPI();

                printer.printData(btpAPI.doubleStrikeModeOFF());
                printer.printData(msg);

            }
        }});

        btn_IMagePrint.setOnClickListener(new View.OnClickListener() {
               public void onClick(View v) {


            String msg = ET_PrintData.getText().toString();

            ESC_POS_2T_BTPrinterAPI btpAPI = new ESC_POS_2T_BTPrinterAPI();

            //  printer.printData(btpAPI.prepareImageData("/mnt/sdcard/2.png"));
            //  Bitmap btm = BitmapFactory.decodeFile("/mnt/sdcard/test.png");
            //  printer.printData(btpAPI.prepareImageData(btm));
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.apcpdcl_logo);
            printer.printImageData(btpAPI.prepareImageData(bmp));


        }});

          btn_QrcodePrint.setOnClickListener(new View.OnClickListener() {
                                                    public void onClick(View v) {


            String msg = ET_PrintData.getText().toString();

            ESC_POS_2T_BTPrinterAPI btpAPI = new ESC_POS_2T_BTPrinterAPI();

            Bitmap bmp=btpAPI._2DbarcodePrint(BTP_2T_ESC_POS_Activity.this, msg);
            printer.printImageData(btpAPI.prepareImageData(bmp));



        }});

        btn_ReceiptPrint.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            String msg = ET_PrintData.getText().toString();

            ESC_POS_2T_BTPrinterAPI btpAPI = new ESC_POS_2T_BTPrinterAPI();

            String printdata="The journey of Analogics Tech India Ltd. began in 1994, as the first electronic manufacturing company to supply Hand Held Terminals for Spot Billing and Collection solution to the Power Utility Sector in India. Since our inception, the focus has been on understanding customer needs and providing innovative technological products, thus making us a market leader in Hand Held portable devices. The integrated manufacturing approach – from design to delivery, is what our customers’ value the most and has been one of the drivers of our success.";
            String printdata1="We believe in being at the forefront of technological advancement and it is this belief that led us to constantly innovate and provide everything ranging from hardware and software to services. We have constantly adapted to the global trends changing needs of our customers by transitioning from a Hardware & IT product company to a company that can provide end-to-end solutions in various sectors";
            String printdata2="Our commitment towards meeting evolving customer needs and achieving utmost satisfaction, has led to the development of new products and solutions over the past two decades. Today, our portfolio of products and solutions cater to various needs in multiple sectors including Utilities, Transportation, Banking, Solar, Defence, Police and Security. We also provide products and solutions for Time & Attendance requirement, Spot Billing & Ticketing demands and Retail Billing applications.\n\n";

            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.apcpdcl_logo);
            printer.printImageData(btpAPI.prepareImageData(bmp));

            String barcodeData="Analogics Tech India Ltd. is the first electronic manufacturing company."; 	//printer.printData(btpAPI.alignLeft());
            printer.printData(btpAPI.initializePrinter());
            printer.printData(btpAPI.characterFontC());
            printer.printData(printdata );
            printer.printData(printdata1 );
            printer.printData(printdata2 );
            printer.printData(btpAPI.initializePrinter());

            bmp=btpAPI._2DbarcodePrint(BTP_2T_ESC_POS_Activity.this, barcodeData);
            printer.printImageData(btpAPI.prepareImageData(bmp));

        }
        });

         Btn_BillReceiptPrint.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

            ESC_POS_2T_BTPrinterAPI btpAPI = new ESC_POS_2T_BTPrinterAPI();
              int i=0;
              while(true){
                try {
                    printer.printData(("Count   "+i+"\n").getBytes());
                    printer.printData(btpAPI.initializePrinter());
                    printer.printData(btpAPI.alignCenter());
                    printer.printData("Firm Name\n".getBytes());
                    printer.printData("Address Line 1\n".getBytes());
                    printer.printData("Address Line 2\n".getBytes());
                    printer.printData("City, State,500086\n".getBytes());
                    printer.printData("Contact : 02323-124343343\n".getBytes());
                    printer.printData("TIN: ABCDEF123456\n".getBytes());
                    printer.printData("INVOICE\n".getBytes());
                    printer.printData(btpAPI.initializePrinter());
                    printer.printData("Bill No   :  1304318796 \n".getBytes());
                    printer.printData("Bill Date :  26-09-2020\n".getBytes());
                    printer.printData("Type      :  Sample\n".getBytes());
                    printer.printData("---------------------------------------\n".getBytes());

                    printer.printData("Client Name\n".getBytes());
                    printer.printData("Address Line 1\n".getBytes());
                    printer.printData("Address Line 2\n".getBytes());
                    printer.printData("City, State,500086\n".getBytes());
                    printer.printData("Contact : 02323-124343343\n".getBytes());
                    printer.printData("Email   : sample3gmail.com".getBytes());
                    printer.printData("TIN     : 123456\n".getBytes());
                    printer.printData("---------------------------------------\n".getBytes());
                    printer.printData("ITEM        QTY   RATE     TOTAL\n".getBytes());
                    printer.printData("---------------------------------------\n".getBytes());

                    printer.printData("Item1         1   120    	120\n".getBytes());
                    printer.printData("Item2         1   160    	160\n".getBytes());
                    printer.printData("Item3         5   178    	178\n".getBytes());
                    printer.printData("Item4         1   178    	178\n".getBytes());
                    printer.printData("Item5         1   234    	258\n".getBytes());
                    printer.printData("Item6         1   75      	75\n".getBytes());
                    printer.printData("---------------------------------------\n".getBytes());
                    printer.printData(" 		      9           	7335\n".getBytes());
                    printer.printData("---------------------------------------\n".getBytes());
                    printer.printData("Discount                    -57.00 \n".getBytes());
                    printer.printData("ServiceTax (5.00%)Exclusive  36.02 \n".getBytes());
                    printer.printData("VAT(5.80%)Exclusive         -57.56 \n".getBytes());
                    printer.printData("Shipping                         0 \n".getBytes());
                    printer.printData("Roundoff                         0 \n".getBytes());
                    printer.printData("---------------------------------------\n".getBytes());

                    printer.printData("Total                       755.00 \n".getBytes());
                    printer.printData("Total Paid                  500.00 \n".getBytes());
                    printer.printData("Balance                     255.00 \n".getBytes());
                    printer.printData("Due Date               13 May 2020 \n".getBytes());
                    printer.printData("---------------------------------------\n".getBytes());
                    printer.printData("Remarks\n".getBytes());
                    printer.printData("All items are tax inclusive   \n".getBytes());
                    printer.printData("Thank You, Visit Again..!!\n".getBytes());
                    printer.printData(btpAPI.alignCenter());
                    printer.printData(btpAPI.initializePrinter());
                    printer.printData(btpAPI.carriageReturn());
                    printer.printData(btpAPI.carriageReturn());
                    i++;

                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(i==10){
                    break;
                }
            }

        }});

       // getActionBar().setTitle(mDeviceName);
      //  getActionBar().setDisplayHomeAsUpEnabled(true);
 }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gatt_services, menu);
        if (mConnected) {
            menu.findItem(R.id.menu_connect).setVisible(false);
            menu.findItem(R.id.menu_disconnect).setVisible(true);
            Toast.makeText(BTP_2T_ESC_POS_Activity.this,"BT_Status  c:   "+mConnected+"",Toast.LENGTH_LONG).show();

        } else {
            menu.findItem(R.id.menu_connect).setVisible(true);
            menu.findItem(R.id.menu_disconnect).setVisible(false);
           Toast.makeText(BTP_2T_ESC_POS_Activity.this,"BT_Status d:   "+mConnected+"",Toast.LENGTH_LONG).show();

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_connect:
                try {
                       printer.openBT(mDeviceAddress);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                return true;
            case R.id.menu_disconnect:
                try {
                    printer.closeBT();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                //  mBluetoothLeService.disconnect();
                //Toast.makeText(DeviceControlActivity.this,mBluetoothLeService.disconnect()+"",Toast.LENGTH_LONG).show();
                return true;
            case android.R.id.home:
                onBackPressed();
                return mConnected;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateConnectionState(final int resourceId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mConnectionState.setText(resourceId);
            }
        });
    }

    private void displayData(String data) {
        if (data != null) {
            mDataField.setText(data);
        }
    }

    public void onClickConnect(View v){

         printer= new AnalogicsThermalPrinter(BTP_2T_ESC_POS_Activity.this);
        try {

            boolean status=printer.isConnected();
            if(!(status)) {
                printer.openBT(mDeviceAddress);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                status=printer.isConnected();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClickConnectionStatus(View v) {
        Toast.makeText(BTP_2T_ESC_POS_Activity.this, "Connection Status >> " + printer.isConnected(), Toast.LENGTH_LONG).show();

    }

    public void onClickDisconnect(View v){
    //    Toast.makeText(DeviceControlActivity.this,"Connection Status >> "+printer.isConnected(),Toast.LENGTH_LONG).show();
        try {
            printer.closeBT();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    class ClickEvent implements View.OnClickListener {
        public void onClick(View v) {


            


			
			/*else if (v == btn_InitializePrinter) {
				String msg = ET_PrintData.getText().toString();
				if (msg.length() > 0) {

					// printImage();

					ESC_POS_2T_BTPrinterAPI btpAPI = new ESC_POS_2T_BTPrinterAPI();
					printer.printData(btpAPI.prepareImageData("/mnt/sdcard/2.png"));
		
				}
			}*/ 
        }
    }
    
}
