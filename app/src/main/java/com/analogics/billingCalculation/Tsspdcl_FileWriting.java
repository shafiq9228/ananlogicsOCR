package com.analogics.billingCalculation;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;

import com.analogics.DBAdapter.DBAdapter;
import com.analogics.appUtils.Config_SharedPreferances;
import com.analogics.pojo.inputDataVO;
import com.analogics.utils.DateUtil;
import com.analogics.utils.GPSCoordinates;

public class Tsspdcl_FileWriting extends Activity {

    GPSCoordinates gpsCoordinates;
    private static String phoneNumber;
    private static String deviceId;
    Config_SharedPreferances configSharedPreferances = new Config_SharedPreferances();

    public Tsspdcl_FileWriting(Context mContext) {
        gpsCoordinates = new GPSCoordinates(mContext);
        phoneNumber = configSharedPreferances.getUserCredentials(mContext, Config_SharedPreferances.phoneNumber);
        deviceId = configSharedPreferances.getUserCredentials(mContext, Config_SharedPreferances.deviceId);
    }

    public String getMonthName(int temp_mm) {
        String montharray = "";
        if (temp_mm == 1)
            montharray = "Jan";
        else if (temp_mm == 2)
            montharray = "Feb";
        else if (temp_mm == 3)
            montharray = "Mar";
        else if (temp_mm == 4)
            montharray = "Apr";
        else if (temp_mm == 5)
            montharray = "May";
        else if (temp_mm == 6)
            montharray = "Jun";
        else if (temp_mm == 7)
            montharray = "Jul";
        else if (temp_mm == 8)
            montharray = "Aug";
        else if (temp_mm == 9)
            montharray = "Sep";
        else if (temp_mm == 10)
            montharray = "Oct";
        else if (temp_mm == 11)
            montharray = "Nov";
        else if (temp_mm == 12)
            montharray = "Dec";
        return montharray;
    }

    public int Write_Record(inputDataVO inputDataVO, String machineId) {

        int retVal = -1;
        float rebateorSubsidybillamnt = 0.00F;
        DBAdapter dbAdapter;
        dbAdapter = DBAdapter.getDBAdapterInstance(this);
        dbAdapter.openDataBase();
        ContentValues initialValues = new ContentValues();
        initialValues.put("consumer_num", inputDataVO.getService_number().trim());
        initialValues.put("ero_code", inputDataVO.getErocode());
        initialValues.put("cat_subcat", (inputDataVO.getCat() + String.format("%02d", inputDataVO.getSub_cat())));
        initialValues.put("bill_date", new DateUtil().getDate());
        initialValues.put("bill_time", new DateUtil().getTime());
        initialValues.put("discntdate", inputDataVO.getDisdd() + "-" + inputDataVO.getDismm() + "-" + inputDataVO.getDisyy());
        initialValues.put("duedate", inputDataVO.getDuedd() + "-" + inputDataVO.getDuemm() + "-" + inputDataVO.getDueyy());
        initialValues.put("bill_num", inputDataVO.getBill_count());
        initialValues.put("prev_kwh", inputDataVO.getPreviousKwh());
        initialValues.put("presnt_kwh", inputDataVO.getPresentKwh());
        initialValues.put("prs_exp_read", inputDataVO.getPresentExportReading());
        initialValues.put("pmtrs_sts", inputDataVO.getPmtrsts());
        initialValues.put("record_units", inputDataVO.getTemp_float());
        initialValues.put("ecchrg", inputDataVO.getEngchg());
        initialValues.put("custchrg", inputDataVO.getCuschg());
        initialValues.put("ed", inputDataVO.getEdchg());
        initialValues.put("fixedchrg", inputDataVO.getFixchg());
        initialValues.put("demand_chrg", inputDataVO.getDemandCharges());
        initialValues.put("cap_chrg", inputDataVO.getCapchg());
        initialValues.put("acd", inputDataVO.getAcd());
        initialValues.put("cus_clmn1", inputDataVO.getCustomcolumn1());
        initialValues.put("pr_amt", inputDataVO.getPamount());
        initialValues.put("pr_ed", inputDataVO.getPedchg());
        initialValues.put("edint", inputDataVO.getIntrestOnED());
        initialValues.put("adjust_cc", inputDataVO.getAdjusted_cc());
        initialValues.put("adjust_ed", inputDataVO.getAdjusted_ed());
        initialValues.put("intst_acd", inputDataVO.getIntrestOnACD());
        initialValues.put("arrear_bfr", inputDataVO.getArrearsBefore());
        initialValues.put("arrear_aftr", inputDataVO.getArrearsAfter());
        initialValues.put("additional_chrg", inputDataVO.getAdditionalCharges());
        initialValues.put("cus_clmn2", inputDataVO.getCustomcolumn2());
        initialValues.put("subsidy", rebateorSubsidybillamnt);
        initialValues.put("lld_surchg", inputDataVO.getLLDSurchage());
        initialValues.put("recrd_enchg", "" + inputDataVO.getHt_billed_flag());
        float bamount = 0.00F;
        initialValues.put("bamount", bamount);
        initialValues.put("tot_amount", inputDataVO.getBillAmount());
        initialValues.put("net_amount", inputDataVO.getTotalAmount());
        initialValues.put("toatl_due", inputDataVO.getTotalDue());
        initialValues.put("los_gain", inputDataVO.getLoss_gain());
        initialValues.put("lld_units", inputDataVO.getLLDUnits());
        initialValues.put("avg_units", inputDataVO.getPresent_average_units());
        initialValues.put("abormal_flag", " ");
        initialValues.put("tc_sealflag", "" + inputDataVO.getTcSealFlag());
        initialValues.put("struct_code", inputDataVO.getStructurecode());
        initialValues.put("discn_kwh", inputDataVO.getKwhfinalreading4());
        initialValues.put("discn_kvah", inputDataVO.getKvahfinalreading4());
        initialValues.put("discn_solar_kwh", inputDataVO.getDisconnectionSolarKWH());
        initialValues.put("new_mtrno", inputDataVO.getNewMeterNumber());
        initialValues.put("new_mtr_mf", inputDataVO.getNewmf());
        initialValues.put("new_mtr_md", inputDataVO.getNewmetermd());
        initialValues.put("new_mtr_acc", "" + inputDataVO.getNewmeteraccuracy());
        initialValues.put("old_md", "" + inputDataVO.getOldmetermd());
        initialValues.put("old_mtr_pf", inputDataVO.getOldmeterpf());
        initialValues.put("prev_kvah", inputDataVO.getPreviousKvah());
        initialValues.put("pres_kvah", inputDataVO.getPresentKvah());
        initialValues.put("pmtr_sts", inputDataVO.getPresentKvah());
        initialValues.put("avg_kvah", inputDataVO.getAvgkvah());
        initialValues.put("expot_kvah", inputDataVO.getExportKVAH());
        initialValues.put("cary_fwd_units", inputDataVO.getFinalCarryForwardExportUnits());
        initialValues.put("cat3_billunits", inputDataVO.getCat3BilledUnits());
        initialValues.put("recmnd_unit", inputDataVO.getRecmendedunits());
        initialValues.put("pf", inputDataVO.getPf());
        initialValues.put("rpf", inputDataVO.getRecpf());
        initialValues.put("recmnd_md", inputDataVO.getRecommendedMD());
        initialValues.put("rmd", inputDataVO.getRecordedMD());
        initialValues.put("billedrmd", inputDataVO.getBilledRecordedMD());
        initialValues.put("billed_demand", inputDataVO.getBilledDemand());
        initialValues.put("mtr_class", inputDataVO.getMeter_class());
        initialValues.put("mtr_class", inputDataVO.getMeter_class());
        initialValues.put("v_r", inputDataVO.getVoltage_R_VR());
        initialValues.put("v_y", inputDataVO.getVoltage_Y_VY());
        initialValues.put("v_b", inputDataVO.getVoltage_B_VB());
        initialValues.put("c_r", inputDataVO.getCurrent_R_IR());
        initialValues.put("c_y", inputDataVO.getCurrent_Y_IY());
        initialValues.put("c_b", inputDataVO.getCurrent_B_IB());
        initialValues.put("pol_no", inputDataVO.getPoleno());
        initialValues.put("ir_mode", "" + inputDataVO.getMeter_reading_mode());
        initialValues.put("ph", inputDataVO.getPh());
        initialValues.put("ele_neflag", "" + inputDataVO.getMtr_type());
        initialValues.put("mtr_pos_flag", "" + inputDataVO.getMeterPosition());
        initialValues.put("billed_kwh", inputDataVO.getBilledKwhUnits());
        initialValues.put("expt_units", (inputDataVO.getExportUnits() - inputDataVO.getCarryForwardUnits()));
        initialValues.put("cat2_htflag", inputDataVO.getCat2HTFlag());
        initialValues.put("md_date", "");
        if (inputDataVO.getAdharcardid() == null)
            initialValues.put("aadharid_text", inputDataVO.getUscNo());
        else
            initialValues.put("aadharid_text", inputDataVO.getAdharcardid());
        initialValues.put("mb_no", inputDataVO.getPhoneno());
        initialValues.put("mtr_make", inputDataVO.getMtrmk());
        initialValues.put("sbm_ver", inputDataVO.getVer_ser());
        //initialValues.put("sbm_id",new GetIMEI_Number().getUniqueIMEIId(getApplicationContext()));
        initialValues.put("sbm_id", phoneNumber);
        initialValues.put("sbm_memry", "P");
        initialValues.put("cmpnyflag", "P");
        initialValues.put("agencyflag", "A");
        initialValues.put("lati", gpsCoordinates.getLatitude());
        initialValues.put("langi", gpsCoordinates.getLongitude());
        long n = dbAdapter.insertRecordsInDB("OUTPUT_MASTER2", null, initialValues);
        dbAdapter.close();
        return 1;
    }

    public String Write_Into_File(inputDataVO inputDataVO, String machineId) {
        char data_type;
        String str = null;
        String str_date = new DateUtil().getDate();
        String str_time = new DateUtil().getTime();

        /*inputDataVO.setErocode("316");
        inputDataVO.setEro_code(316);
        inputDataVO.setEroName("QUTBULLAHPUR");
        inputDataVO.setSecName("GAJULARAMARAM");*/
        String disconnectDate, dueDate;
        disconnectDate = inputDataVO.getDisdd() + "-" + getMonthName(inputDataVO.getDismm()) + "-" + yearsInFormat(inputDataVO.getDisyy());
        dueDate = inputDataVO.getDuedd() + "-" + getMonthName(inputDataVO.getDuemm()) + "-" + yearsInFormat(inputDataVO.getDueyy());
		/*rt.pmtrdd = str_date.left(2).toInt();
		rt.pmtrmm = str_date.mid(3,2).toInt();
		getMonthName(rt.pmtrmm);
		rt.pmtryy = str_date.mid(8,2).toInt();*/
        String cat_subcat;
        cat_subcat = inputDataVO.getCat() + "";
        cat_subcat += inputDataVO.getSubcat();
        try {
            str = inputDataVO.getService_number() + "|";
            str += inputDataVO.getErocode();
            str += "|";
            str += cat_subcat;
            str += "|";
            str += String.format("%02d", inputDataVO.getPmtrdd()) + "-" + getMonthName(inputDataVO.getPmtrmm()) + "-" + yearsInFormat(inputDataVO.getPmtryy()) + "|"
                    + str_time + "|";
            str += disconnectDate + "|";
            str += dueDate + "|";
            str += String.format("%04d", inputDataVO.getBill_count() + 1) + "|";
            str += inputDataVO.getPreviousKwh() + "|";
            str += inputDataVO.getPresentKwh() + "|";
            str += inputDataVO.getPresentExportReading() + "|";
            str += inputDataVO.getPmtrsts() + "|";
            //str+=inputDataVO.getPresentExportReading()+"|";
            str += inputDataVO.getUnits() + "|";
            str += decimalTwoPlaceFormat(inputDataVO.getEngchg()) + "|";
            str += decimalTwoPlaceFormat(inputDataVO.getCuschg()) + "|";
            str += decimalTwoPlaceFormat(inputDataVO.getEdchg()) + "|";
            str += decimalTwoPlaceFormat(inputDataVO.getFixchg()) + "|";
            str += inputDataVO.getDemandCharges() + "|";
            str += decimalTwoPlaceFormat(inputDataVO.getCapchg()) + "|";
            str += inputDataVO.getAcd() + "|";
            str += inputDataVO.getCustomcolumn1() + "|";
            //str+=inputDataVO.getCustomcolumn2()+"|";
            str += decimalTwoPlaceFormat(inputDataVO.getPamount()) + "|";
            str += decimalTwoPlaceFormat(inputDataVO.getPedchg()) + "|";
            str += inputDataVO.getIntrestOnED() + "|";
            str += inputDataVO.getAdjusted_cc() + "|";
            str += inputDataVO.getAdjusted_ed() + "|";
            str += inputDataVO.getIntrestOnACD() + "|";
            str += inputDataVO.getArrearsBefore() + "|";
            str += inputDataVO.getArrearsAfter() + "|";
            str += inputDataVO.getAdditionalCharges() + "|";
            str += inputDataVO.getCustomcolumn2() + "|";
            if (inputDataVO.getScstflag() == 2) {
                str += inputDataVO.getSubsidybillamnt() + "|";
            } else {
                str += inputDataVO.getRebate() + "|";
            }
            str += inputDataVO.getLLDSurchage() + "|";
            str += inputDataVO.getHtBillTotal() + "|";
            str += inputDataVO.getTemp_float() + "|";
            str += decimalTwoPlaceFormat(inputDataVO.getBillAmount()) + "|";
            str += decimalTwoPlaceFormat(inputDataVO.getTotalAmount()) + "|";
            str += decimalTwoPlaceFormat(inputDataVO.getTotalDue()) + "|";
            str += decimalTwoPlaceFormat(inputDataVO.getLoss_gain()) + "|";
            str += inputDataVO.getLLDUnits() + "|";
            str += inputDataVO.getPresent_average_units() + "|";
            str += " |";
            str += new StringBuilder(inputDataVO.getTcSealFlag()) + "|";
            str += inputDataVO.getStructurecode() + "|";
            str += inputDataVO.getKwhfinalreading4() + "|";
            str += inputDataVO.getKvahfinalreading4() + "|";
            str += inputDataVO.getDisconnectionSolarKWH() + "|";
            str += inputDataVO.getNewMeterNumber() + "|";
            str += inputDataVO.getNewmf() + "|";
            str += inputDataVO.getNewmetermd() + "|";
            str += new StringBuilder(inputDataVO.getNewmeteraccuracy()) + "|";
            str += inputDataVO.getOldmetermd() + "|";
            str += inputDataVO.getOldmeterpf() + "|";
            str += inputDataVO.getPreviousKvah() + "|";
            str += inputDataVO.getPresentKvah() + "|";
            str += inputDataVO.getKvahMeterStatus() + "|";
            str += inputDataVO.getAvgkvah() + "|";
            str += inputDataVO.getExportKVAH() + "|";
            str += inputDataVO.getFinalCarryForwardExportUnits() + "|";
            str += inputDataVO.getCat3BilledUnits() + "|";
            str += inputDataVO.getRecmendedunits() + "|";
            str += inputDataVO.getPf() + "|";
            str += inputDataVO.getRecpf() + "|";
            str += decimalTwoPlaceFormat(inputDataVO.getRecommendedMD()) + "|";
            str += decimalTwoPlaceFormat(inputDataVO.getRecordedMD()) + "|";
            str += decimalTwoPlaceFormat(inputDataVO.getBilledRecordedMD()) + "|";
            str += decimalTwoPlaceFormat(inputDataVO.getBilledDemand()) + "|";
            str += inputDataVO.getMeter_class() + "|";
            str += decimalTwoPlaceFormat(inputDataVO.getVoltage_R_VR()) + "|";
            str += decimalTwoPlaceFormat(inputDataVO.getVoltage_Y_VY()) + "|";
            str += decimalTwoPlaceFormat(inputDataVO.getVoltage_B_VB()) + "|";
            str += decimalTwoPlaceFormat(inputDataVO.getCurrent_R_IR()) + "|";
            str += decimalTwoPlaceFormat(inputDataVO.getCurrent_Y_IY()) + "|";
            str += decimalTwoPlaceFormat(inputDataVO.getCurrent_B_IB()) + "|";
            str += inputDataVO.getPoleno() + "|";
            str += new StringBuilder(inputDataVO.getMeter_reading_mode()) + "|";
            str += inputDataVO.getPh() + "|";
            str += new StringBuilder(inputDataVO.getMtr_type()) + "|";
            str += new StringBuilder(inputDataVO.getMeterPosition()) + "|";
            str += inputDataVO.getBilledKwhUnits() + "|";
            str += (inputDataVO.getExportUnits() - inputDataVO.getCarryForwardUnits()) + "|";
            str += inputDataVO.getCat2HTFlag() + "|";
            //str+=str_date+" "+str_time+"|";
            str += str_date + "|";
            if (inputDataVO.getAdharcardid() == null)
                str += inputDataVO.getUscNo() + "|";
            else
                str += inputDataVO.getAdharcardid() + "|";
            str += inputDataVO.getPhoneno() + "|";
            str += inputDataVO.getMtrmk() + "|";
            str += inputDataVO.getVersion() + "|";
            str += phoneNumber + "|";//machine id
            str += "P|";//sbm_Memory
            str += "P|";
            str += "A|";
            str += gpsCoordinates.getLatitude() + "|";
            str += gpsCoordinates.getLongitude() + "|";
            str += "" + "|";
            str += ""+ "|";
            System.out.println("str:" + str);
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            return str;
        }
    }

    private String yearsInFormat(int months) {
        String result = "";
        String tempMonths = "";
        try {
            tempMonths = String.valueOf(months);
        } catch (Exception ignore) {
        }

        result = tempMonths.trim();
        if (result.length() == 4) {
            result = result.substring(2, tempMonths.length());
            return result;
        }
        return result;
    }


    private String decimalTwoPlaceFormat(double x) {
        String result = "";
        try {
            result = String.format("%.2f", x);
        } catch (Exception ignore) {
            result = String.valueOf(x);
        }

        return result;
    }

}
