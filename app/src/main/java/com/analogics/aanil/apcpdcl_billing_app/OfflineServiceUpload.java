package com.analogics.aanil.apcpdcl_billing_app;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import com.analogics.DBAdapter.DBAdapter;
import com.analogics.pojo.Upload_dataVO;
import com.analogics.utils.FileOperations;
import com.analogics.utils.GPRSTest;
import com.analogics.utils.GetIMEI_Number;
import com.analogics.utils.TarGZFileUtil;
import com.analogics.webservice.APIClient;
import com.analogics.webservice.APIInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * *
 * check the any offline records available if records available get the record details and update to the server.
 */
public class OfflineServiceUpload extends Service {

    APIInterface apiInterface;
    Timer myTimer;
    MyTimerTask myTask;

    Upload_dataVO upload_dataVO = new Upload_dataVO();

    JSONObject jsonObj = null;
    String simnum = null;


    static String sdcardPath = Environment.getExternalStorageDirectory() + "/Android/";

    String _OUT_FILEPATH = sdcardPath + "TSSPDCL_OUT/";
    String _TEMP_FILEPATH = sdcardPath + "TSSPDCL_TEMP/";

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @SuppressLint("NewApi")
    public void onCreate() {
        super.onCreate();

    }

    public int onStartCommand(Intent intent, int flags, int startId) {

    Log.d("FazilApp", "HELLO IM RUNNING");
        try {
            myTimer = new Timer();
            myTask = new MyTimerTask();

            myTimer.schedule(myTask, 2000, 20000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return START_STICKY;
    }

    public void onStop() {

    }

    public void onPause() {

    }

    public void onDestroy() {

    }

    class MyTimerTask extends TimerTask {

        @SuppressLint("NewApi")
        public void run() {

            try {

                GPRSTest gPRSTest = new GPRSTest(OfflineServiceUpload.this);
                DBAdapter dbAdapterBilling = DBAdapter.getDBAdapterInstance(OfflineServiceUpload.this);

                Log.d("myMeterApp", gPRSTest.gprsTest() + "");
                if (gPRSTest.gprsTest()) {

                    //	if (dbAdapterBilling.checkdbopen())
                    {
						/*dbAdapterBilling.openDataBase();
						String getCustomerIDQuery = "select * from upload_data where UploadFlag ='N' limit 1 ;";
						Cursor cursor = dbAdapterBilling.selectRecordsFromDB(getCustomerIDQuery,null);
						  if (cursor.getCount() > 0) {
						if (cursor.moveToFirst()) {
							upload_dataVO = new Upload_dataVO();

							upload_dataVO.setUsc_code(cursor.getString(1));
							String prepareUploadData =
									cursor.getString(0) + "|" +
											cursor.getString(1) + "|" +
											cursor.getString(2) + "|" +
											cursor.getString(3) + "|" +
											cursor.getString(4) + "|" +
											cursor.getString(5) + "|" +
											cursor.getString(6) + "|" +
											cursor.getString(7) + "|" +
											cursor.getString(8) + "|" +
											cursor.getString(9) + "|" +
											cursor.getString(10) + "|" +
											cursor.getString(11) + "|" +
											cursor.getString(12) + "|" +
											cursor.getString(13) + "|" +
											cursor.getString(14) + "|" +
											cursor.getString(15) + "|" +
											cursor.getString(16) + "|" +
											cursor.getString(17) + "|" +
											cursor.getString(18) + "|" +
											cursor.getString(19) + "|" +
											cursor.getString(20) + "|" +
											cursor.getString(21) + "|" +
											cursor.getString(22) + "|" +
											cursor.getString(23) + "|" +
											cursor.getString(24) + "|" +
											cursor.getString(25) + "|" +
											cursor.getString(26) + "|" +
											cursor.getString(27) + "|" +
											cursor.getString(28) + "|" +
											cursor.getString(29) + "|" +
											cursor.getString(30) + "|" +
											cursor.getString(31) + "|" +
											cursor.getString(32) + "|" +
											cursor.getString(33) + "|" +
											cursor.getString(34) + "|" +
											cursor.getString(35) + "|" +
											cursor.getString(36) + "|" +
											cursor.getString(37) + "|" +
											cursor.getString(38) + "|" +
											cursor.getString(39) + "|" +
											cursor.getString(40) + "|" +
											cursor.getString(41) + "|" +
											cursor.getString(42) + "|" +
											cursor.getString(43) + "|" +
											cursor.getString(44) + "|" +
											cursor.getString(45) + "|" +
											cursor.getString(46) + "|" +
											cursor.getString(47) + "|" +
											cursor.getString(48) + "|"+
											cursor.getString(49) + "|"+
											cursor.getString(50) + "|" +
											cursor.getString(51) + "|" +
											cursor.getString(52) + "|" +
											cursor.getString(53) + "|" +
											cursor.getString(54) + "|" +
											cursor.getString(55) + "|" +
											cursor.getString(56) + "|" +
											cursor.getString(57) + "|" +
											cursor.getString(58) + "|" +
											cursor.getString(59) + "|" +
											cursor.getString(60) + "|" +
											cursor.getString(61) + "|" +
											cursor.getString(62) + "|" +
											cursor.getString(63) + "|" +
											cursor.getString(64) + "|" +
											cursor.getString(65) + "|" +
											cursor.getString(66) + "|" +
											cursor.getString(67) + "|" +
											cursor.getString(68) + "|" +
											cursor.getString(69) + "|" +
											cursor.getString(70) + "|" +
											cursor.getString(71) + "|" +
											cursor.getString(72) + "|" +
											cursor.getString(73) + "|" +
											cursor.getString(74) + "|" +
											cursor.getString(75) + "|" +
											cursor.getString(76) + "|" +
											cursor.getString(77) + "|" +
											cursor.getString(78) + "|" +
											cursor.getString(79) + "|" +
											cursor.getString(80) + "|" +
											cursor.getString(81) + "|" +
											cursor.getString(82) + "|" +
											cursor.getString(83) + "|" +
											cursor.getString(84) + "|" +
											cursor.getString(85) + "|" +
											cursor.getString(86) + "|" +
											cursor.getString(87) + "|" +
											cursor.getString(88) + "|" +
											cursor.getString(89) + "|"+
											cursor.getString(90) + "|"+
							                cursor.getString(91) + "|";

							Date date = new Date();
						//	SimpleDateFormat dateformatter = new SimpleDateFormat("yyyyMMddHHmmss");
							//SimpleDateFormat dateformatter = new SimpleDateFormat("ddMMyyyyHHmmss");
							SimpleDateFormat dateformatter = new SimpleDateFormat("yyyyMMdd");
							String strDate = dateformatter.format(date);
							new FileOperations().checkDirectroies();
							new FileOperations().writeToFile(prepareUploadData, "APSPDCL_" + cursor.getString(90)+"_"+cursor.getString(91) + "_" + cursor.getString(2) +  "_" + strDate + ".txt", OfflineServiceUpload.this);
							dbAdapterBilling.close();
							upload( cursor.getString(90), cursor.getString(91));

						}else{
						    dbAdapterBilling.close();
						}}else{dbAdapterBilling.close();}*/

                        upload("12345", "9865");
                    }
                    //  else{dbAdapterBilling.close();}
                }
            } catch (Exception e) {
                Log.d("myMeterApp", e.toString());
                e.printStackTrace();
            }


        }
    }


    public String getfile(File dir) {
        File listFile[] = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            for (int i = 0; i < listFile.length; i++) {
                {
                    return listFile[0].getAbsoluteFile().getName();
                }
            }
        }
        return "NO FILES";
    }


    public void upload(String eroCode, String sectionId) {
        try {
            FileOperations FO = new FileOperations();
            FO.checkDirectroies();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date curDate = new Date();

            String currentdateTime = sdf.format(curDate);
            File source = new File(_OUT_FILEPATH);
            String filePath = getfile(source);
            System.out.println(" Timer FILE>>" + _OUT_FILEPATH + filePath);

            TarGZFileUtil tar = new TarGZFileUtil();
            String out_sourceFileName = _OUT_FILEPATH + filePath;
            String encodedTardata = tar.tarGZ_BillingSingleFile("TSSPDCL_BILLING_" + currentdateTime, out_sourceFileName);
            System.out.println(" Tar FILE>>" + encodedTardata);

            // *******************************************************

            String response = "";
            if (!encodedTardata.equalsIgnoreCase("FAIL")) {
                //  response = new WebServiceUtil().uploadBillDetails_WebseviceClient( encodedTardata);
                System.out.println(" Tar Success>>" + out_sourceFileName);
                UploadBillingData(eroCode, sectionId, encodedTardata, out_sourceFileName);

            } else {
                System.out.println(" FAIL FILE>>" + _OUT_FILEPATH + filePath);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    public void UploadBillingData(String eroCode, String sectionId, String encodedTardata, String out_sourceFileName) {

        try {
            if (true) return;
            Log.d("myMeterApp", encodedTardata);
            apiInterface = APIClient.getClient().create(APIInterface.class);
            //JsonObject convertedObject = new Gson().fromJson(prepareJSonStringToUpload(eroCode,sectionId,encodedTardata), JsonObject.class);
            String requestStr = prepareXMLStringToUpload(new GetIMEI_Number().getUniqueIMEIId(getApplicationContext()), encodedTardata);
            Call<ResponseBody> call4 = apiInterface.uploadXMLdata(requestStr);

	/*		final ProgressDialog progressDoalog;
			progressDoalog = new ProgressDialog(OfflineServiceUpload.this);
			progressDoalog.setTitle("Uploading Billing data to the server");
			progressDoalog.setMessage("Please wait....");
			progressDoalog.setCancelable(false);
			progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

			// show it
			progressDoalog.show();*/
            //
            //  progressDoalog.dismiss();

            call4.enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.isSuccessful()) {


                        String responseStr = "";
                        try {

                            responseStr = response.body().string();
                            System.out.println("responseStr>>>" + responseStr);


                            if (response.body() != null) {
                                System.out.println(">>>" + responseStr);
                                if (responseStr.contains("@ACK@")) {
                                    File outfile = new File(out_sourceFileName);
                                    outfile.delete();

                                    File tempfile = new File(_TEMP_FILEPATH);
                                    String[] tempFiles;

                                    tempFiles = tempfile.list();
                                    for (int i = 0; i < tempFiles.length; i++) {
                                        File myFile = new File(tempfile, tempFiles[i]);
                                        myFile.delete();
                                    }
                                    return;
                                }
                                Log.v("onSuccess", responseStr);
                                //  Toast.makeText(getApplicationContext(), (response.message())+"\n"+new Gson().toJson(response.body()), Toast.LENGTH_SHORT).show();
                                //   Log.v("TAG", "response 33: " + new Gson().toJson(responseMessage));
                                try {
                                    //       JSONObject jsonRootObject = new JSONObject(new Gson().toJson(response.body()) + "");
                                    JSONObject jsonRootObject = new JSONObject(responseStr);
                                    String status = jsonRootObject.optString("response");

                                    if (status.equalsIgnoreCase("@ACK@")) {

                                        DBAdapter dbAdapter;
                                        dbAdapter = DBAdapter.getDBAdapterInstance(OfflineServiceUpload.this);


                                        dbAdapter.openDataBase();
                                        ContentValues initialValues = new ContentValues();

                                        initialValues.put("UploadFlag", "Y");

                                        String[] strArray = {upload_dataVO.getUsc_code()};
                                        long n = dbAdapter.updateRecordsInDB("upload_data", initialValues, "usc_code=?", strArray);

                                        dbAdapter.close();
/*
									File outfile = new File(out_sourceFileName);
									outfile.delete();

									File tempfile = new File(_TEMP_FILEPATH);
											String[] tempFiles;

											tempFiles = tempfile.list();
											for (int i = 0; i < tempFiles.length; i++) {
												File myFile = new File(tempfile, tempFiles[i]);
												myFile.delete();
											}
*/


                                        upload_dataVO = new Upload_dataVO();

                                    } else {
                                        //	Toast.makeText(Online_CommunicationMenuActivity.this, "Masterdownload failed", Toast.LENGTH_LONG).show();
                                    }
                                    //progressDoalog.dismiss();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    //	progressDoalog.dismiss();
                                }
                                //progressDoalog.dismiss();
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }


                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    call.cancel();
                    //progressDoalog.dismiss();
                    System.out.println("onfailure");
                    //Toast.makeText(Online_CommunicationMenuActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
                    //new AlertMessage().alertMessage(OfflineServiceUpload.this,"Alert","Server down please check with the Administrator.");
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String prepareXMLStringToUpload(String machineID, String data) {
        String Request = "";
        try {
            Request = "<machineId>" + machineID + "</machineId>\n" + "<command>UPLOAD</command><data>" + data + "</data>";
//			System.out.println("Download Request>>>"+jsonObj.toString());
            return Request;
        } catch (Exception ex) {
            ex.printStackTrace();
            return Request;
        }
        // return jsonObj.toString();
    }

    public String prepareJSonStringToUpload(String eroCodeValue, String sectionIdValue, String data) {
        JSONObject jsonObj = null;
        try {

            jsonObj = new JSONObject();

//			REQ ::{"command":"upload","data":"H4sIAAAAAAAAA+2TQU7DQAxFc5R/AaL/Z8ZJw66CJaBKHCAC0XWl0iIq+fAsMmmbFioEVbvJW4wdy7Hj+c509jy7v3toRbaiWpIhqmVDBaqRaWKxXH2uir9DklXFgh0FGWKS9c9KZKGYZDGpohWUVVUqwH/0/DXr99XLEiiWi8XJGecf8+XmdfG2ucRHXY6nKQC4WQiR7NT3zgAAHIDqFErSAQDgzy7l/eLkcF/ihO2MU96tg9NP9TiDe3R2reUkE13Hr95Ulm9ArPu7iLarvPVCOo6d1ds/AaVU9Ql1k0pypwBddqtJHkV1CjtR6JSrYQzsMw4ayRkdgOWw5aCcOS/R6cyqZfZk75X9bpThBgwZZnT7uY1vSxw4j05HLNV4pFlQjGQKwwKHBQEA1/7/RkZGRq7FF0sqyJMACgAA","ero":"100","sectionId":"2222"}
//			RES :: {"response":"@ACK@","dataStr":"H4sIAAAAAAAAAO19eYwk13lfH"}
            jsonObj.put("command", "upload");
            jsonObj.put("data", data);
            jsonObj.put("ero", eroCodeValue);
            jsonObj.put("sectionId", sectionIdValue);


            System.out.println("Download Request>>>" + jsonObj.toString());
            return jsonObj.toString();

        } catch (JSONException ex) {

            ex.printStackTrace();
            return jsonObj.toString();

        }

        // return jsonObj.toString();
    }
}
