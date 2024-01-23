package com.analogics.ui.menu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.analogics.DBAdapter.ReportsDAO;
import com.analogics.R;
import com.analogics.appUtils.Config_SharedPreferances;
import com.analogics.pojo.ReportsVO;
import com.analogics.utils.AlertMessage;
import com.analogics.utils.DateUtil;
import com.analogics.utils.FileOperations;
import com.whty.smartpos.tysmartposapi.ITYSmartPosApi;
import com.whty.smartpos.tysmartposapi.modules.printer.PrinterConfig;
import com.whty.smartpos.tysmartposapi.modules.printer.PrinterConstant;
import com.whty.smartpos.tysmartposapi.modules.printer.PrinterInitListener;


public class ReportsMenuActivity extends AppCompatActivity {

    Button Btn_CatagoryReport;
    Button Btn_DemandReport;
    Button Btn_StatusReport;
    Button Btn_Bill_UnbilledReport;
    Button Btn_home;
    ITYSmartPosApi tyApi = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_menu);

        ui_init();
        clickListener();
        initializePrinter();
        new Config_SharedPreferances().check_GetConfigPreferanceValues(ReportsMenuActivity.this);
    }

    @Override
    protected void onStop() {
        int status = tyApi.getPrinterStatus();
        if(status == PrinterConstant.PrinterStatus.STATUS_NORMAL)
            tyApi.releasePrinter();
        super.onStop();
    }

    public void ui_init() {

        Btn_CatagoryReport = findViewById(R.id.Btn_CatagoryReport);
        Btn_DemandReport = findViewById(R.id.Btn_DemandReport);
        Btn_StatusReport = findViewById(R.id.Btn_StatusReport);
        Btn_Bill_UnbilledReport = findViewById(R.id.Btn_Bill_UnbilledReport);
        Btn_home = findViewById(R.id.Btn_home);
    }

    private void initializePrinter(){
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

    public void clickListener() {
        Btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToBackActivity();
            }
        });

        Btn_Bill_UnbilledReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                ReportsDAO reportsDAO = new ReportsDAO();
                int temp_total_inputRecords = 0;
                int temp_total_billedRecords = 0;
                temp_total_inputRecords = reportsDAO.Get_Total_Records_Count();
                temp_total_billedRecords = reportsDAO.Get_Upload_Record_Count();
                AlertDialog.Builder builder = new AlertDialog.Builder(ReportsMenuActivity.this);
                builder.setTitle("          DISPLAY REPORT");
                builder.setMessage("TOTAL RECORDS        : " + temp_total_inputRecords + "\nBILLED RECORDS       : " + temp_total_billedRecords + "\nPENDING RECORDS   : " + (temp_total_inputRecords - temp_total_billedRecords));
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        dialog.dismiss();
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
            }
        });


        Btn_CatagoryReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isPrintSuccess = false;

                ReportsDAO reportsDAO = new ReportsDAO();
                int temp_total_inputRecords = 0;
                int temp_total_billedRecords = 0;
                temp_total_inputRecords = reportsDAO.Get_Total_Records_Count();
                temp_total_billedRecords = reportsDAO.Get_Upload_Record_Count();
                int count1, count2, count3, count4, count5, count6, count7, count8, count9 = 0;
                int Ucount1, Ucount2, Ucount3, Ucount4, Ucount5, Ucount6, Ucount7, Ucount8, Ucount9 = 0;
                count1 = reportsDAO.getCatWiseInput(1);
                count2 = reportsDAO.getCatWiseInput(2);
                count3 = reportsDAO.getCatWiseInput(3);
                count4 = reportsDAO.getCatWiseInput(4);
                count5 = reportsDAO.getCatWiseInput(5);
                count6 = reportsDAO.getCatWiseInput(6);
                count7 = reportsDAO.getCatWiseInput(7);
                count8 = reportsDAO.getCatWiseInput(8);
                count9 = reportsDAO.getCatWiseInput(9);
                Ucount1 = reportsDAO.getCatWiseOutput(1);
                Ucount2 = reportsDAO.getCatWiseOutput(2);
                Ucount3 = reportsDAO.getCatWiseOutput(3);
                Ucount4 = reportsDAO.getCatWiseOutput(4);
                Ucount5 = reportsDAO.getCatWiseOutput(5);
                Ucount6 = reportsDAO.getCatWiseOutput(6);
                Ucount7 = reportsDAO.getCatWiseOutput(7);
                Ucount8 = reportsDAO.getCatWiseOutput(8);
                Ucount9 = reportsDAO.getCatWiseOutput(9);
                ReportsVO reportsVO = new ReportsVO();
                String catagoryReport = "";
                catagoryReport = "      CATAGORY REPORT   \n";
                catagoryReport += "---------------------------\n";
                catagoryReport += "D:" + new DateUtil().getBillDate() + "T:" + new DateUtil().getTime() + "\n";
                catagoryReport += "TOTAL SERVICES   : " + temp_total_inputRecords + "\n";
                /*reportsVO=  reportsDAO.getCatWiseListInput();
                for (int counter = 0; counter < reportsVO.totalCatagoryTypeList.size(); counter++)
                {
                    catagoryReport+="CATAGORY "+reportsVO.totalCatagoryTypeList.get(counter)   +" : "+reportsVO.totalCatagoryCountList.get(counter)+"\n";
                }*/
                catagoryReport += "CATAGORY I    : " + count1 + "\n";
                catagoryReport += "CATAGORY II   : " + count2 + "\n";
                catagoryReport += "CATAGORY III  : " + count3 + "\n";
                catagoryReport += "CATAGORY IV   : " + count4 + "\n";
                catagoryReport += "CATAGORY V    : " + count5 + "\n";
                catagoryReport += "CATAGORY VI   : " + count6 + "\n";
                catagoryReport += "CATAGORY VII  : " + count7 + "\n";
                catagoryReport += "CATAGORY VIII : " + count8 + "\n";
                catagoryReport += "CATAGORY XI   : " + count9 + "\n";

                catagoryReport += "BILLED SERVICES  : " + temp_total_billedRecords + "\n";
                /*reportsVO=   reportsDAO.getCatWiseListOuptut();
                for (int counter = 0; counter < reportsVO.totalBilledCatagoryTypeList.size(); counter++) {
                        catagoryReport+="CATAGORY "+reportsVO.totalBilledCatagoryTypeList.get(counter)   +" : "+reportsVO.totalBilledCatagoryCountList.get(counter)+"\n";
                }*/
                catagoryReport += "CATAGORY I    : " + Ucount1 + "\n";
                catagoryReport += "CATAGORY II   : " + Ucount2 + "\n";
                catagoryReport += "CATAGORY III  : " + Ucount3 + "\n";
                catagoryReport += "CATAGORY IV   : " + Ucount4 + "\n";
                catagoryReport += "CATAGORY V    : " + Ucount5 + "\n";
                catagoryReport += "CATAGORY VI   : " + Ucount6 + "\n";
                catagoryReport += "CATAGORY VII  : " + Ucount7 + "\n";
                catagoryReport += "CATAGORY VIII : " + Ucount8 + "\n";
                catagoryReport += "CATAGORY XI   : " + Ucount9 + "\n";

                catagoryReport += "UNBILLED SERVICES: " + (temp_total_inputRecords - temp_total_billedRecords) + "\n";
                catagoryReport += "CATAGORY I    : " + (count1 - Ucount1) + "\n";
                catagoryReport += "CATAGORY II   : " + (count2 - Ucount2) + "\n";
                catagoryReport += "CATAGORY III  : " + (count3 - Ucount3) + "\n";
                catagoryReport += "CATAGORY IV   : " + (count4 - Ucount4) + "\n";
                catagoryReport += "CATAGORY V    : " + (count5 - Ucount5) + "\n";
                catagoryReport += "CATAGORY VI   : " + (count6 - Ucount6) + "\n";
                catagoryReport += "CATAGORY VII  : " + (count7 - Ucount7) + "\n";
                catagoryReport += "CATAGORY VIII : " + (count8 - Ucount8) + "\n";
                catagoryReport += "CATAGORY XI   : " + (count9 - Ucount9) + "\n";
                catagoryReport += "---------------------------\n\n\n\n";

                try {
                    int status = tyApi.getPrinterStatus();
                    tyApi.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    if(status == PrinterConstant.PrinterStatus.STATUS_NORMAL){
                        int ret = tyApi.printText(catagoryReport);
                        if(ret == PrinterConstant.PrintResult.SUCCESS)
                            isPrintSuccess = true;
                    }

                    if(isPrintSuccess){
                        AlertDialog.Builder builder = new AlertDialog.Builder(ReportsMenuActivity.this);
                        builder.setTitle("         UNBILLED SERVICES");
                        builder.setMessage("\nPRINT-  YES/NO");
                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing but close the dialog
                                ReportsVO unbilledVO = new ReportsVO();
                                unbilledVO = reportsDAO.getUnbilledServiceNos();
                                String unbilledServiceReport = "";
                                unbilledServiceReport = "UNBILLED SERVICES:" + unbilledVO.totalCatagoryTypeList.size() + "\n";
                                for (int counter = 0; counter < unbilledVO.totalCatagoryTypeList.size(); counter++) {
                                    unbilledServiceReport += " " + String.format("%4d", counter) + "    " + unbilledVO.totalCatagoryTypeList.get(counter) + "\n";
                                }
                                unbilledServiceReport += "\n\n\n\n";

                                int status = tyApi.getPrinterStatus();
                                if(status == PrinterConstant.PrinterStatus.STATUS_NORMAL){
                                    int ret = tyApi.printText(unbilledServiceReport);
                                    System.out.println(ret);
                                }

                                dialog.dismiss();
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

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        Btn_DemandReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReportsDAO reportsDAO = new ReportsDAO();
                ReportsVO reportsVO = new ReportsVO();
                String demandReport = "";

                demandReport = "      DEMAND REPORT   \n";
                demandReport += "---------------------------\n";
                demandReport += "D:" + new DateUtil().getBillDate() + "T:" + new DateUtil().getTime() + "\n";
                reportsVO = reportsDAO.getDemandReportList("05/09/2018", "05/09/2018");

                for (int counter = 0; counter < reportsVO.demand_DistName_List.size(); counter++) {
                    demandReport += "DIST NAME  : " + reportsVO.demand_DistName_List.get(counter) + "\n" +
                            "TRANS NO   : " + reportsVO.demand_TRANSNO_List.get(counter) + "\n" +
                            "SERVICE NO : " + reportsVO.demand_SERVICE_NO_List.get(counter) + "\n" +
                            "ARREARS    : " + reportsVO.demand_ARREARS_List.get(counter) + "\n" +
                            "CMD        : " + reportsVO.demand_CMD_List.get(counter) + "\n" +
                            "TOTAL      : " + reportsVO.demand_TOTAL_List.get(counter) + "\n";
                }
                AlertMessage alertMessage = new AlertMessage();
                alertMessage.alertMessage(ReportsMenuActivity.this, "Demand Report", demandReport);
            }
        });


        Btn_StatusReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int Pending_count = 0;
                int Total_Records_Count = 0;
                int Total_Billed = 0;
                int upload_count = 0;
                ReportsDAO reportsDAO = new ReportsDAO();
                Pending_count = new FileOperations().fileCountOUT();
                Total_Records_Count = reportsDAO.Get_Total_Records_Count();
                Total_Billed = reportsDAO.Get_Upload_Record_Count();
                if (Total_Billed > Pending_count)
                    upload_count = Total_Billed - Pending_count;
                else
                    upload_count = 0;

                AlertDialog.Builder builder = new AlertDialog.Builder(ReportsMenuActivity.this);
                builder.setTitle("      ONLINE STATUS REPORT ");
                builder.setMessage("TOTAL RECORDS        : " + Total_Records_Count + "\nPENDING RECORDS   : " + Pending_count + "\nUPLOAD RECORDS     : " + upload_count + "\nTOTAL BILLED             : " + Total_Billed);
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        dialog.dismiss();
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
            }
        });

    }

    private void navigateToBackActivity() {
        Intent i = new Intent(getBaseContext(), MainMenuActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // If want to block just return false
            navigateToBackActivity();
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