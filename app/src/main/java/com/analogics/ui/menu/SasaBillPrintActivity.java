package com.analogics.ui.menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.analogics.Printer.PrinterLib;
import com.analogics.R;
import com.analogics.thermalAPI.RP_Printer_2inch_prof_ThermalAPI;
import com.analogics.utils.DateUtil;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonParser;
import com.whty.smartpos.tysmartposapi.ITYSmartPosApi;
import com.whty.smartpos.tysmartposapi.modules.printer.PrinterConfig;
import com.whty.smartpos.tysmartposapi.modules.printer.PrinterConstant;
import com.whty.smartpos.tysmartposapi.modules.printer.PrinterInitListener;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Objects;


public class SasaBillPrintActivity extends AppCompatActivity {

    private TextInputEditText etEroCode, etServiceNo;
    private TextInputLayout etEroTil, etServiceNoTil;
    private AppCompatButton btnGenerateSasaPrint;
    private ProgressDialog progressDialog;
    ITYSmartPosApi tyApi = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sasa_bill_print);
        init();
    }

    private void init() {
        initElements();
        initListeners();
        initializePrinter();
    }

    private void initializePrinter() {
        tyApi = ITYSmartPosApi.get(getApplicationContext());
        tyApi.initPrinter(new PrinterInitListener() {
            @Override
            public void onPrinterInit(boolean b) {

            }
        });

        Bundle bundle = new Bundle();
        bundle.putInt(PrinterConfig.FONT_SIZE, 4);
        bundle.putInt(PrinterConfig.ALIGN, PrinterConstant.Align.ALIGN_CENTER);
        bundle.putInt(PrinterConfig.CN_FONT, PrinterConstant.Typeface.SONGTI_BOLD);
        bundle.putInt(PrinterConfig.EN_FONT, PrinterConstant.Typeface.SONGTI_BOLD);
        tyApi.setPrinterParameters(bundle);
        tyApi.setLineSpace(5);
    }

    private void initElements() {
        etEroCode = findViewById(R.id.et_ero_cd);
        etEroTil = findViewById(R.id.til_ero_cd);
        etServiceNo = findViewById(R.id.et_service_no);
        etServiceNoTil = findViewById(R.id.til_service_no);
        btnGenerateSasaPrint = findViewById(R.id.btn_generate_sasa_print);
        progressDialog = new ProgressDialog(SasaBillPrintActivity.this);

    }

    @Override
    protected void onStop() {
        /*int status = tyApi.getPrinterStatus();
        if (status == PrinterConstant.PrinterStatus.STATUS_NORMAL)
            tyApi.releasePrinter();*/
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void initListeners() {

        btnGenerateSasaPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });

    }


    private void checkValidation() {
        String eroCode = Objects.requireNonNull(etEroCode.getText()).toString().trim();
        String serviceNo = Objects.requireNonNull(etServiceNo.getText()).toString().trim();


        if (!eroCode.isEmpty()) {
            setTextInputLayoutError(etEroTil, null, false);
            if (!serviceNo.isEmpty()) {
                setTextInputLayoutError(etServiceNoTil, null, false);
                String areaCode = serviceNo.substring(0, 4);
                String service = serviceNo.substring(4);

                serviceNo = areaCode + service;
                getSasaData(eroCode, serviceNo);
            } else {
                setTextInputLayoutError(etServiceNoTil, "Enter Service Number.", true);
                showShortToast("Please Enter Service Number.");
            }
        } else {
            setTextInputLayoutError(etEroTil, "Enter ERO Code.", true);
            showShortToast("Please Enter ERO Code.");
        }
    }

    private void getSasaData(String eroCode, String serviceNo) {

        progressDialog.setTitle("Please wait...");
        setProgressMsg(progressDialog, "Data Downloading..");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        try {
            String url = "http://14.99.140.194:9094/TsspdclAuthWs/analogics/dataWS/downloadService";
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("eroCode", eroCode);
            jsonObject.put("serviceNo", serviceNo.toUpperCase());

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObjResp) {
                    try {
                        if (jsonObjResp != null) {
                            String errorMsg = jsonObjResp.getString("ERROR");
                            if (errorMsg.contains("Bill not generated")) {
                                showShortToast(errorMsg);
                                progressDialog.dismiss();
                            } else {
                                String printingData = getPrintData(jsonObjResp);
                                setProgressMsg(progressDialog, "Printing...");
                                printData(printingData);
                                progressDialog.dismiss();
                            }

                        }
                    } catch (Exception ignored) {
                        progressDialog.dismiss();

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    progressDialog.dismiss();

                }
            });
            jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                    20000,
                    5,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonRequest);


        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }


    }


    private void setProgressMsg(ProgressDialog progressDialog, String msg) {
        progressDialog.setMessage(msg);
    }

    private String getPrintData(JSONObject jsonObjResp) {
        String btkvahflag = "";
        String bttheft = "";
        String btDevlop = "";
        String Tarifdiff = "";
        String todunits = "";
        String pnlunits = "";
        String result = "";
        try {
            result = "      T S S P D C L\n";
            result += "       ELECTRICITY\n";
            result += "     BILL-CUM NOTICE\n";
            result += "-------------------------\n";
            String str_date = new DateUtil().getDate();
            String str_time = new DateUtil().getTime();
            btkvahflag = jsonObjResp.getString("TRIVFLAG").trim() + "";
            bttheft = jsonObjResp.getString("THEFT_AMT").trim() + "";
            btDevlop = jsonObjResp.getString("DEVCHG_AMT").trim() + "";
            Tarifdiff = jsonObjResp.getString("TARIFFDIFF").trim() + "";
            pnlunits = jsonObjResp.getString("BTBLKVA_PNL").trim() + "";
            todunits = jsonObjResp.getString("BTBLDUNITS_TOD").trim() + "";

            result += "DT:" + str_date + " TM:" + str_time + "\n";
            result += "ERO NO: " + jsonObjResp.getString("EROCD").trim() + "\n";
            result += "ERONAME: " + jsonObjResp.getString("ERONAME").trim() + "\n";
            result += "SECNAME: " + jsonObjResp.getString("CTSECNAME").trim() + "\n";
            result += "AREACD: " + jsonObjResp.getString("AREACD").trim() + "   GRP:" + jsonObjResp.getString("GRP").trim() + "\n";
            result += "-------------------------\n";
            result += "SC NO: " + jsonObjResp.getString("SCNO").trim() + "\n";
            result += "USCNO: " + jsonObjResp.getString("UKSCNO").trim() + "\n";
            result += "NAME: " + jsonObjResp.getString("NAME").trim() + "\n";
            result += "ADDR: " + jsonObjResp.getString("ADDRESS").trim() + "\n";
            if (StringUtils.isNotBlank(jsonObjResp.getString("CAT_DECODE")))
                result += "CAT: " + jsonObjResp.getString("CAT").trim() + " " + jsonObjResp.getString("CAT_DECODE").trim() + "\n";
            else
                result += "CAT: " + jsonObjResp.getString("CAT").trim() + " \n";
            result += "LOAD: " + jsonObjResp.getString("LOAD").trim() + "    PHASE: " + jsonObjResp.getString("PHASE") + "\n";
            if (btkvahflag.equalsIgnoreCase("1")) {
                result += "CONNECTED LOAD: " + jsonObjResp.getString("CONN_LD").trim() + "" + jsonObjResp.getString("LOAD_PAR").trim() + "\n";
            }
            result += "METERNO: " + jsonObjResp.getString("MTRNO").trim() + "  " + jsonObjResp.getString("BTBILL_TARIFF").trim() + "\n";
            result += "MF: " + jsonObjResp.getString("MF").trim() + "\n";
            result += "-------------------------\n";
            result += "      PRESENT    PREVIOUS \n";
            result += "KWH : " + jsonObjResp.getString("PRS_RDG").trim() + "   " + jsonObjResp.getString("PRV_RDG").trim() + "\n";
            if (btkvahflag.equalsIgnoreCase("1")) {
                result += "KVAH: " + jsonObjResp.getString("CLKVAH").trim() + "   " + jsonObjResp.getString("OPNKVAH").trim() + "\n";
            }
            result += "DATE: " + jsonObjResp.getString("PSR_RDGDT").trim() + "  " + jsonObjResp.getString("PPRV_RDGDT").trim() + "\n";
            result += "STS:     " + jsonObjResp.getString("PRS_RDGSTAT").trim() + "       " + jsonObjResp.getString("PRV_RDGSTAT").trim() + "\n";
            result += "UNITS:  " + jsonObjResp.getString("BLDUNITS").trim() + "   DAYS:" + jsonObjResp.getString("NOOFDAYS").trim() + "\n";
            if (btkvahflag.equalsIgnoreCase("1")) {
                result += "RMD: " + jsonObjResp.getString("BTRKVA_HT").trim() + "\n";
                result += "KVAH:" + jsonObjResp.getString("BTRKVAH_HT").trim() + " KWH:" + jsonObjResp.getString("BTRKWH_HT").trim() + "\n";
                result += "MINIMUM UNITS: " + jsonObjResp.getString("BTMIN_UNITS").trim() + "\n";
                result += "-------------------------\n";
                result += "VOL_R:" + jsonObjResp.getString("VOLTAGE_R").trim() + "  CUR_R:" + jsonObjResp.getString("CURRENT_R").trim() + "\n";
                result += "VOL_Y:" + jsonObjResp.getString("VOLTAGE_Y").trim() + "  CUR_Y:" + jsonObjResp.getString("CURRENT_Y").trim() + "\n";
                result += "VOL_B:" + jsonObjResp.getString("VOLTAGE_B").trim() + "  CUR_B:" + jsonObjResp.getString("CURRENT_B").trim() + "\n";
            }
            result += "-------------------------\n";
            result += "ENERGY CHARGES: " + jsonObjResp.getString("ENGCHG").trim() + "\n";
            if (btkvahflag.equalsIgnoreCase("1")) {
                if (Long.parseLong(todunits) > 0) {
                    result += "Rs." + jsonObjResp.getString("BTBLDUNITS_RT_NOR").trim() + " for " + jsonObjResp.getString("BTBLDUNITS_NOR").trim() + "\n";
                    result += "Rs." + jsonObjResp.getString("BTBLDUNITS_RT_TOD").trim() + " for " + jsonObjResp.getString("BTBLDUNITS_TOD").trim() + "\n";
                }
            }
            result += "FIXED CHARGES : " + jsonObjResp.getString("FIXCHG").trim() + "\n";
            if (btkvahflag.equalsIgnoreCase("1")) {
                if (Double.parseDouble(pnlunits) > 0) {
                    result += "Rs." + jsonObjResp.getString("BTBLKVA_RT_NOR").trim() + " for " + jsonObjResp.getString("BTBLKVA_NOR").trim() + "\n";
                    result += "Rs." + jsonObjResp.getString("BTBLKVA_RT_PNL").trim() + " for " + jsonObjResp.getString("BTBLKVA_PNL").trim() + "\n";
                }
            }

            result += "CUST  CHARGES : " + jsonObjResp.getString("CUSTCHG").trim() + "\n";
            result += "ELECTRICI DUTY: " + jsonObjResp.getString("ELDTY").trim() + "\n";
            //result += "ED INI        : " + jsonObjResp.getString("EDINT").trim() + "\n";
            result += "ADDL  CHARGES : " + jsonObjResp.getString("SURCHG").trim() + "\n";
            result += "ADDSURCHARGES : " + jsonObjResp.getString("ACDSURCHG").trim() + "\n";
            result += "ISD           : " + jsonObjResp.getString("ISD").trim() + "\n";
            result += "ADJ  AMOUNT   : " + jsonObjResp.getString("ADJAMT").trim() + "\n";
            result += "BILL AMOUNT   : " + jsonObjResp.getString("BILLAMT").trim() + "\n";
            result += "LOSS/GAIN     : " + jsonObjResp.getString("LOSS_GAIN").trim() + "\n";
            result += "NET  AMOUNT   : " + jsonObjResp.getString("NETAMT").trim() + "\n";
            result += "ARRERAS  -------------- \n";

            result += "AFTER 01-04-23: " + jsonObjResp.getString("CURRARR").trim() + "\n";
            result += "AS ON 31-03-23: " + jsonObjResp.getString("APRARR").trim() + "\n";

            result += "TOTAL AMOUNT  : " + jsonObjResp.getString("TOTALAMOUNT").trim() + "\n";
            result += "ACD DUC       : " + jsonObjResp.getString("ACDDUE").trim() + "\n";
            result += "TOTAL DUE     : " + jsonObjResp.getString("TOTAL_DUE").trim() + "\n";
            result += "-------------------------\n";
            result += "SUBSIDY UNIT  : " + jsonObjResp.getString("SUBSIDY_UNIT").trim() + "\n";
            result += "DUE DATE      : " + jsonObjResp.getString("DUEDT").trim() + "\n";
            result += "LAST PAID DATE: " + jsonObjResp.getString("LASTPDDT").trim() + "\n";
            result += "ADE NO        : " + jsonObjResp.getString("ADEPHNO").trim() + "\n";
            result += "AAO NO        : " + jsonObjResp.getString("AAOPHNO").trim() + "\n";
            result += "-------------------------\n";

            if (Float.parseFloat(bttheft) > 0) {
                result += "THEFT CHRG     : " + jsonObjResp.getString("THEFT_AMT").trim() + "\n";
            }
            if (Float.parseFloat(btDevlop) > 0) {
                result += "DEVLOPMENT CHRG: " + jsonObjResp.getString("DEVCHG_AMT").trim() + "\n";
            }
            if (Float.parseFloat(Tarifdiff) > 0) {
                result += "TARIFF DIFF    : " + jsonObjResp.getString("TARIFFDIFF").trim() + "\n";
            }

            result += "-------------------------\n";
            result += "\n\n\n";

        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
        return result;
    }

    private void printData(String printingData) {
        try {
            int status = tyApi.getPrinterStatus();
            if (status == PrinterConstant.PrinterStatus.STATUS_NORMAL) {
                int ret = tyApi.printText(printingData);
            }
        } catch (Exception ex) {

        }
    }

    private void showShortToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void setTextInputLayoutError(TextInputLayout inputLayout, String errorMsg, boolean isEnabled) {
        try {
            inputLayout.setError(errorMsg);
            inputLayout.setErrorEnabled(isEnabled);
        } catch (Exception ignore) {
        }
    }

    @Override
    protected void onDestroy() {

        int status = tyApi.getPrinterStatus();
        if (status == PrinterConstant.PrinterStatus.STATUS_NORMAL)
            tyApi.releasePrinter();
        super.onDestroy();
    }

    @Override
    protected void onResume() {

        super.onResume();
        int status = tyApi.getPrinterStatus();
        if (status != PrinterConstant.PrinterStatus.STATUS_NORMAL)
            init();

    }

    @Override
    protected void onPause() {

        super.onPause();

    }
}