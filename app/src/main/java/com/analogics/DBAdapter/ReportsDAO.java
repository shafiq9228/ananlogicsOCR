package com.analogics.DBAdapter;

import android.app.Activity;
import android.database.Cursor;

import com.analogics.pojo.ReportsVO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ReportsDAO extends Activity {
    ReportsVO reportsVO=new ReportsVO();
    String totalServiceCount="0";
    String totalBilledServiceCount="0";
    String totalUnBilledServiceCount="0";
    String totalBilledAmount="0";
    String totalBilledUnits="0";
    String todaysTotalBilledCount="0";
    String todaysTotalBilledUnits="0";
    String todaysTotalBilledAmount="0";


    public String getTotalService(){
        totalServiceCount="0";
        DBAdapter dbAdapter=null;
        try{
            dbAdapter = DBAdapter.getDBAdapterInstance(this);
            dbAdapter.openDataBase();
            String getAccountidQuery;

            getAccountidQuery = "select count(*) from consumer_data;";

            Cursor cursor = dbAdapter.selectRecordsFromDB(getAccountidQuery,
                    null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                totalServiceCount=cursor.getString(0);

            }
            dbAdapter.close();
        }
        catch(Exception e)
        {
            dbAdapter.close();
            e.printStackTrace();
        }
        return totalServiceCount;
    }

    public int Get_Upload_Record_Count(){
        DBAdapter dbAdapter=null;
        int retVal=0;
        try{
            dbAdapter = DBAdapter.getDBAdapterInstance(this);
            dbAdapter.openDataBase();
            String countQuery;
            Cursor cursor;
            countQuery = "SELECT count(*) FROM (select distinct(consumer_num) from OUTPUT_MASTER2)";
            cursor = dbAdapter.selectRecordsFromDB(countQuery, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                retVal=cursor.getInt(0);
                dbAdapter.close();
            }
            }
            catch(Exception e)
            {
                dbAdapter.close();
                e.printStackTrace();
            }
        return retVal;
    }
    public int Get_Total_Records_Count(){
        DBAdapter dbAdapter=null;
        int retVal=0;
        try{
            dbAdapter = DBAdapter.getDBAdapterInstance(this);
            dbAdapter.openDataBase();
            String countQuery;
            Cursor cursor;
            countQuery = "select count(*) from INPUT_MASTER";
            cursor = dbAdapter.selectRecordsFromDB(countQuery, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                retVal=cursor.getInt(0);
                dbAdapter.close();
            }
        }
        catch(Exception e)
        {
            dbAdapter.close();
            e.printStackTrace();
        }
        return retVal;
    }

    public ReportsVO getCatWiseListInput(){
        DBAdapter dbAdapter=null;
        try {
            dbAdapter = DBAdapter.getDBAdapterInstance(this);
            dbAdapter.openDataBase();
            String query = "select distinct(category),count(*) from INPUT_MASTER group by category";
            Cursor cursor = dbAdapter.selectRecordsFromDB(query,
                    null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                reportsVO.totalCatagoryTypeList.add(cursor.getString(0));
                reportsVO.totalCatagoryCountList.add(cursor.getString(1));
                while (cursor.moveToNext()) {
                    reportsVO.totalCatagoryTypeList.add(cursor.getString(0));
                    reportsVO.totalCatagoryCountList.add(cursor.getString(1));
                }
            }
            dbAdapter.close();
        }
        catch(Exception e)
        {
            dbAdapter.close();
            e.printStackTrace();
        }
        return reportsVO;
    }

    public ReportsVO getUnbilledServiceNos(){
        DBAdapter dbAdapter=null;
        try {
            dbAdapter = DBAdapter.getDBAdapterInstance(this);
            dbAdapter.openDataBase();
            String query = "select trim(consumer_num) from INPUT_MASTER where trim(INPUT_MASTER.consumer_num) NOT IN(select trim(OUTPUT_MASTER2.consumer_num) FROM OUTPUT_MASTER2)";
            Cursor cursor = dbAdapter.selectRecordsFromDB(query,
                    null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                reportsVO.totalCatagoryTypeList.add(cursor.getString(0));
                while (cursor.moveToNext()) {
                    reportsVO.totalCatagoryTypeList.add(cursor.getString(0));
                }
            }
            dbAdapter.close();
        }
        catch(Exception e)
        {
            dbAdapter.close();
            e.printStackTrace();
        }
        return reportsVO;
    }

    public int getCatWiseInput(int tempCat){
        DBAdapter dbAdapter=null;
        int retVal=0;
        try {
            dbAdapter = DBAdapter.getDBAdapterInstance(this);
            dbAdapter.openDataBase();
            String query = "select count(*) from INPUT_MASTER where category="+tempCat;
            Cursor cursor = dbAdapter.selectRecordsFromDB(query,
                    null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                retVal=cursor.getInt(0);
            }
            dbAdapter.close();
        }
        catch(Exception e)
        {
            dbAdapter.close();
            e.printStackTrace();
        }
        return retVal;
    }

    public ReportsVO getCatWiseListOuptut(){
        DBAdapter dbAdapter=null;
        try {
            dbAdapter = DBAdapter.getDBAdapterInstance(this);
            dbAdapter.openDataBase();
            String query = "select distinct(cat_subcat),count(*) from OUTPUT_MASTER2 group by distinct(cat_subcat)";
            Cursor cursor = dbAdapter.selectRecordsFromDB(query,
                    null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                reportsVO.totalBilledCatagoryTypeList.add(cursor.getString(0));
                reportsVO.totalBilledCatagoryCountList.add(cursor.getString(1));
                while (cursor.moveToNext()) {
                    reportsVO.totalBilledCatagoryTypeList.add(cursor.getString(0));
                    reportsVO.totalBilledCatagoryCountList.add(cursor.getString(1));
                }
            }
            dbAdapter.close();
        }
        catch(Exception e)
        {
            dbAdapter.close();
            e.printStackTrace();
        }
        return reportsVO;
    }

    public int getCatWiseOutput(int tempCat){
        DBAdapter dbAdapter=null;
        int retVal=0;
        try {
            dbAdapter = DBAdapter.getDBAdapterInstance(this);
            dbAdapter.openDataBase();
            String query = "select count(*) from (SELECT distinct(consumer_num) FROM OUTPUT_MASTER2 where substr(cat_subcat,1,1)='"+tempCat+"')";
            Cursor cursor = dbAdapter.selectRecordsFromDB(query,
                    null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                retVal=cursor.getInt(0);
            }
            dbAdapter.close();
        }
        catch(Exception e)
        {
            dbAdapter.close();
            e.printStackTrace();
        }
        return retVal;
    }

    public ReportsVO getTotalCatagoryWiseList(){
        DBAdapter dbAdapter=null;
        try{
            dbAdapter = DBAdapter.getDBAdapterInstance(this);
            dbAdapter.openDataBase();
            String query="select substr(cat_subcat,1,1),count(*) from OUTPUT_MASTER2 group by substr(cat_subcat,1,1) order by substr(cat_subcat,1,1)";
            Cursor cursor = dbAdapter.selectRecordsFromDB(query,
                    null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                reportsVO.totalCatagoryTypeList.add(cursor.getString(0));
                reportsVO.totalCatagoryCountList.add(cursor.getString(1));
                while (cursor.moveToNext()) {
                    reportsVO.totalCatagoryTypeList.add(cursor.getString(0));
                    reportsVO.totalCatagoryCountList.add(cursor.getString(1));
                }
            }
            dbAdapter.close();
        }
        catch(Exception e)
        {
            dbAdapter.close();
            e.printStackTrace();
        }
        return reportsVO;
    }



    public String getTotalBilledService(){
        totalBilledServiceCount="0";
        DBAdapter dbAdapter=null;
        try{
            dbAdapter = DBAdapter.getDBAdapterInstance(this);
            dbAdapter.openDataBase();
            String getAccountidQuery;

            getAccountidQuery = "select count(*) from OUTPUT_MASTER2";

            Cursor cursor = dbAdapter.selectRecordsFromDB(getAccountidQuery,
                    null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                totalBilledServiceCount=cursor.getString(0);

            }
            dbAdapter.close();
        }
        catch(Exception e)
        {
            dbAdapter.close();
            e.printStackTrace();
        }
        return totalBilledServiceCount;
    }

    public ReportsVO getTotalBilledCatagoryWiseList(){

        DBAdapter dbAdapter=null;
        try{
            dbAdapter = DBAdapter.getDBAdapterInstance(this);
            dbAdapter.openDataBase();
            //String query="select distinct category,cnt from (select * from  consumer_data a left join  (select category,count(*)cnt from   upload_data group by category)b on a.category=b.category)c";
            String query="select distinct cat,cnt from (select * from INPUT_MASTER a left join (select substr(cat_subcat,1,1) cat,count(*) cnt from OUTPUT_MASTER2 group by substr(cat_subcat,1,1)) b on substr(a.category,1,1)=substr(b.cat,1,1))";
            Cursor cursor = dbAdapter.selectRecordsFromDB(query,
                    null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                reportsVO.totalBilledCatagoryTypeList.add(cursor.getString(0));
                reportsVO.totalBilledCatagoryCountList.add(cursor.getString(1));
                while (cursor.moveToNext()) {
                    reportsVO.totalBilledCatagoryTypeList.add(cursor.getString(0));
                    reportsVO.totalBilledCatagoryCountList.add(cursor.getString(1));
                }
            }
            dbAdapter.close();
        }
        catch(Exception e)
        {
            dbAdapter.close();
            e.printStackTrace();
        }
        return reportsVO;
    }


    public String getTotalUnBilledService(){
        totalUnBilledServiceCount="0";
        DBAdapter dbAdapter=null;
        try{
            dbAdapter = DBAdapter.getDBAdapterInstance(this);
            dbAdapter.openDataBase();
            String getAccountidQuery;
            getAccountidQuery = "SELECT (SELECT count(*) FROM INPUT_MASTER)-(SELECT count(*) FROM OUTPUT_MASTER2);";
            Cursor cursor = dbAdapter.selectRecordsFromDB(getAccountidQuery,
                    null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                totalUnBilledServiceCount=cursor.getString(0);

            }
            dbAdapter.close();
        }
        catch(Exception e)
        {
            dbAdapter.close();
            e.printStackTrace();
        }
        return totalUnBilledServiceCount;
    }

    public ReportsVO getTotalUnBilledCatagoryWiseList(){
        DBAdapter dbAdapter=null;
        try{
            dbAdapter = DBAdapter.getDBAdapterInstance(this);
            dbAdapter.openDataBase();
            String query="select distinct category,cnt from (select * from consumer_data a left join (SELECT c.category, COUNT(c.Service_Number)- COUNT(u.scno)cnt  FROM consumer_data c Left JOIN upload_data u ON u.scno=c.Service_Number GROUP BY c.category ORDER BY c.category)b on a.category=b.category)c;";
            Cursor cursor = dbAdapter.selectRecordsFromDB(query,
                    null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                reportsVO.totalUnBilledCatagoryTypeList.add(cursor.getString(0));
                reportsVO.totalUnBilledCatagoryCountList.add(cursor.getString(1));
                while (cursor.moveToNext()) {
                    reportsVO.totalUnBilledCatagoryTypeList.add(cursor.getString(0));
                    reportsVO.totalUnBilledCatagoryCountList.add(cursor.getString(1));
                }
            }
            dbAdapter.close();
        }
        catch(Exception e)
        {
            dbAdapter.close();
            e.printStackTrace();
        }
        return reportsVO;
    }

    public String getTotalBilledAmountService(){
        totalBilledAmount="0";
        DBAdapter dbAdapter=null;
        try{
            dbAdapter = DBAdapter.getDBAdapterInstance(this);
            dbAdapter.openDataBase();
            String query;
            query = "select sum(Bill_Amount_excl_arrearars) from upload_data;";
            Cursor cursor = dbAdapter.selectRecordsFromDB(query,
                    null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                totalBilledAmount=cursor.getString(0);
            }
            dbAdapter.close();
        }
        catch(Exception e)
        {
            dbAdapter.close();
            e.printStackTrace();
        }
        return totalBilledAmount;
    }

    public ReportsVO getTotalBilledAmountCatagoryWiseList(){
        DBAdapter dbAdapter=null;
        try{
            dbAdapter = DBAdapter.getDBAdapterInstance(this);
            dbAdapter.openDataBase();
          // TOTAL AMOUNT with arrears LIST    >>>> select distinct category,cnt from (select * from consumer_data a left join (SELECT c.category, sum(u.Bill_Amount_excl_arrearars)cnt  FROM consumer_data c Left JOIN upload_data u ON u.scno=c.Service_Number GROUP BY c.category ORDER BY c.category)b on a.category=b.category)c
             String query="select distinct category,cnt from (select * from consumer_data a left join (SELECT c.category, sum(u.Bill_Amount_excl_arrearars)cnt  FROM consumer_data c Left JOIN upload_data u ON u.scno=c.Service_Number GROUP BY c.category ORDER BY c.category)b on a.category=b.category)c";
          // String query="select distinct category,cnt from (select * from consumer_data a left join (SELECT c.category, sum(u.Total_Amont_incl_Arrs)cnt  FROM consumer_data c Left JOIN upload_data u ON u.scno=c.Service_Number GROUP BY c.category ORDER BY c.category)b on a.category=b.category)c";
          // TOTAL AMOUNT without arrears LIST >>>> select distinct category,cnt from (select * from consumer_data a left join (SELECT c.category, sum(u.Total_Amont_incl_Arrs)cnt  FROM consumer_data c Left JOIN upload_data u ON u.scno=c.Service_Number GROUP BY c.category ORDER BY c.category)b on a.category=b.category)c
            Cursor cursor = dbAdapter.selectRecordsFromDB(query, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                reportsVO.totalBilledAmountCatagoryTypeList.add(cursor.getString(0));
                reportsVO.totalBilledAmountCatagoryCountList.add(cursor.getString(1));
                while (cursor.moveToNext()) {
                    reportsVO.totalBilledAmountCatagoryTypeList.add(cursor.getString(0));
                    reportsVO.totalBilledAmountCatagoryCountList.add(cursor.getString(1));
                }
            }
            dbAdapter.close();
        }
        catch(Exception e)
        {
            dbAdapter.close();
            e.printStackTrace();
        }
        return reportsVO;
    }

    public String getTotalBilledUnitsService(){
        totalBilledUnits="0";
        DBAdapter dbAdapter=null;
        try{
            dbAdapter = DBAdapter.getDBAdapterInstance(this);
            dbAdapter.openDataBase();
            String getAccountidQuery;
            getAccountidQuery = "select sum(Billed_units) from upload_data;";
            Cursor cursor = dbAdapter.selectRecordsFromDB(getAccountidQuery,
                    null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                totalBilledUnits=cursor.getString(0);
            }
            dbAdapter.close();
        }
        catch(Exception e)
        {
            dbAdapter.close();
            e.printStackTrace();
        }
        return totalBilledUnits;
    }

    public ReportsVO getTotalBilledUnitsCatagoryWiseList(){
        DBAdapter dbAdapter=null;
        try{
            dbAdapter = DBAdapter.getDBAdapterInstance(this);
            dbAdapter.openDataBase();
            //TOTAL UNITS LIST >>>> SELECT c.category, sum(u.Billed_units)  FROM consumer_data c Left JOIN upload_data u ON u.scno=c.Service_Number GROUP BY c.category ORDER BY c.category
            String query="SELECT c.category, sum(u.Billed_units)  FROM consumer_data c Left JOIN upload_data u ON u.scno=c.Service_Number GROUP BY c.category ORDER BY c.category";
            Cursor cursor = dbAdapter.selectRecordsFromDB(query, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                reportsVO.totalBilledUnitsCatagoryTypeList.add(cursor.getString(0));
                reportsVO.totalBilledUnitsCatagoryCountList.add(cursor.getString(1));
                while (cursor.moveToNext()) {
                    reportsVO.totalBilledUnitsCatagoryTypeList.add(cursor.getString(0));
                    reportsVO.totalBilledUnitsCatagoryCountList.add(cursor.getString(1));
                }
            }
            dbAdapter.close();
        }
        catch(Exception e)
        {
            dbAdapter.close();
            e.printStackTrace();
        }
        return reportsVO;
    }

    public String getTodaysTotalBilledCountService(String todaysDate){
        todaysTotalBilledCount="0";
        DBAdapter dbAdapter=null;
        try{
            dbAdapter = DBAdapter.getDBAdapterInstance(this);
            dbAdapter.openDataBase();
            String getAccountidQuery;
            getAccountidQuery = "select count(*) from upload_data where Prdg_Date='"+todaysDate+"';";
            Cursor cursor = dbAdapter.selectRecordsFromDB(getAccountidQuery,
                    null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                todaysTotalBilledCount=cursor.getString(0);
            }
            dbAdapter.close();
        }
        catch(Exception e)
        {
            dbAdapter.close();
            e.printStackTrace();
        }
        return todaysTotalBilledCount;
    }

    public String getTodaysTotalBilledUnitsService(String todaysDate){
        todaysTotalBilledUnits="0";
        String todaysTotalBilledAmount="0";
        DBAdapter dbAdapter=null;
        try{
            dbAdapter = DBAdapter.getDBAdapterInstance(this);
            dbAdapter.openDataBase();
            String getAccountidQuery;
            getAccountidQuery = "select sum(Billed_units) from upload_data where Prdg_Date='"+todaysDate+"';";
            Cursor cursor = dbAdapter.selectRecordsFromDB(getAccountidQuery,
                    null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                todaysTotalBilledUnits=cursor.getString(0);
            }
            dbAdapter.close();
        }
        catch(Exception e)
        {
            dbAdapter.close();
            e.printStackTrace();
        }
        return todaysTotalBilledUnits;
    }

    public String getTodaysTotalBilledAmountService(String todaysDate){
        todaysTotalBilledAmount="0";
        DBAdapter dbAdapter=null;
        try{
            dbAdapter = DBAdapter.getDBAdapterInstance(this);
            dbAdapter.openDataBase();
            String getAccountidQuery;
            getAccountidQuery = " select sum(Total_Amont_incl_Arrs) from upload_data where Prdg_Date='"+todaysDate+"';";
            Cursor cursor = dbAdapter.selectRecordsFromDB(getAccountidQuery, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                todaysTotalBilledAmount=cursor.getString(0);
            }
            dbAdapter.close();
        }
        catch(Exception e)
        {
            dbAdapter.close();
            e.printStackTrace();
        }
        return todaysTotalBilledAmount;
    }

    public ReportsVO getDemandReportList(String fromDate, String toDate){
        DBAdapter dbAdapter=null;
        try{
            dbAdapter = DBAdapter.getDBAdapterInstance(this);
            dbAdapter.openDataBase();
            String query="select c.Dist_Zone_Name,c.Transformer_Code,c.Service_Number,u.Old_Arrears,u.New_Arrears,u.Bill_Amount_excl_arrearars,u.Total_Amont_incl_Arrs from consumer_data c Left JOIN upload_data u ON u.scno=c.Service_Number where c.Service_Number IN (select scno from upload_data where Prdg_Date BETWEEN '"+fromDate+"' AND '"+toDate+"')";
            Cursor cursor = dbAdapter.selectRecordsFromDB(query,
                    null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                reportsVO.demand_DistName_List.add(cursor.getString(0));
                reportsVO.demand_TRANSNO_List.add(cursor.getString(1));
                reportsVO.demand_SERVICE_NO_List.add(cursor.getString(2));
                reportsVO.demand_ARREARS_List.add(cursor.getString(4));
                reportsVO.demand_CMD_List.add(cursor.getString(5));
                reportsVO.demand_TOTAL_List.add(cursor.getString(6));
                while (cursor.moveToNext()) {
                    reportsVO.demand_DistName_List.add(cursor.getString(0));
                    reportsVO.demand_TRANSNO_List.add(cursor.getString(1));
                    reportsVO.demand_SERVICE_NO_List.add(cursor.getString(2));
                    reportsVO.demand_ARREARS_List.add(cursor.getString(4));
                    reportsVO.demand_CMD_List.add(cursor.getString(5));
                    reportsVO.demand_TOTAL_List.add(cursor.getString(6));
                }
            }
            dbAdapter.close();
        }
        catch(Exception e)
        {
            dbAdapter.close();
            e.printStackTrace();
        }
        return reportsVO;
    }


    public ReportsVO getStatusWiseReportList(String fromDate, String toDate, String pres_mtr_Staus){
        DBAdapter dbAdapter=null;
        try{
            dbAdapter = DBAdapter.getDBAdapterInstance(this);
            dbAdapter.openDataBase();
            String query="select c.Dist_Zone_Name,c.Transformer_Code,c.Service_Number,u.pmtr_rdg_KWH,u.Billed_units from consumer_data c Left JOIN upload_data u ON u.scno=c.Service_Number where c.Service_Number IN (select scno from upload_data where (Prdg_Date BETWEEN '"+fromDate+"' AND '"+toDate+"') AND pres_mtr_Staus = '"+pres_mtr_Staus+"')";
            Cursor cursor = dbAdapter.selectRecordsFromDB(query, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                reportsVO.status_DistName_List.add(cursor.getString(0));
                reportsVO.status_TRANSNO_List.add(cursor.getString(1));
                reportsVO.status_SERVICE_NO_List.add(cursor.getString(2));
                reportsVO.status_pmtr_rdg_KWH_List.add(cursor.getString(3));
                reportsVO.status_Billed_units_List.add(cursor.getString(4));
                while (cursor.moveToNext()) {
                    reportsVO.status_DistName_List.add(cursor.getString(0));
                    reportsVO.status_TRANSNO_List.add(cursor.getString(1));
                    reportsVO.status_SERVICE_NO_List.add(cursor.getString(2));
                    reportsVO.status_pmtr_rdg_KWH_List.add(cursor.getString(3));
                    reportsVO.status_Billed_units_List.add(cursor.getString(4));
                }
            }
            dbAdapter.close();
        }
        catch(Exception e)
        {
            dbAdapter.close();
            e.printStackTrace();
        }
        return reportsVO;
    }
    public String getOutputRecordsBackup(){
        String data = "";
        DBAdapter dbAdapter=null;
        int retVal=0;
        try{
            StringBuilder sb = new StringBuilder();
            dbAdapter = DBAdapter.getDBAdapterInstance(this);
            dbAdapter.openDataBase();
            String countQuery;
            Cursor cursor;
            countQuery = "select * from OUTPUT_MASTER2";
            cursor = dbAdapter.selectRecordsFromDB(countQuery, null);
            int columnCount = cursor.getColumnCount();

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                sb.append(prepareColumnData(cursor));
                sb.append("\n");
                while (cursor.moveToNext()) {
                    sb.append(prepareColumnData(cursor));
                    sb.append("\n");
                }
            }
            data = sb.toString();
            dbAdapter.close();

        }
        catch(Exception e)
        {
            dbAdapter.close();
            e.printStackTrace();
        }
        return data;
    }
    private String prepareColumnData(Cursor cursor){
        String output = "";
        try{
            StringBuilder sb = new StringBuilder();
            int columnCount = cursor.getColumnCount();
            for(int i = 0;i<columnCount;i++){
                sb.append(cursor.getString(i));
                sb.append("|");
            }
            output = sb.toString();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return output;
    }

    public static void zipFile(String folderPath) throws IOException {

        String zipPath = folderPath+ File.separator+"Zip_Out.zip";
        FileOutputStream fileOutputStream = new FileOutputStream(zipPath);
        ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
        File directoryToZip = new File(folderPath);
        zipFormat(directoryToZip, directoryToZip.getName(),zipOutputStream );
        zipOutputStream.close();
        fileOutputStream.close();
    }
    private static void zipFormat(File directoryToZip, String parentFolder, ZipOutputStream zipOutputStream) throws IOException, FileNotFoundException {
        for (String fileName: directoryToZip.list()){
            File file = new File(directoryToZip,fileName);
            if (file.isDirectory()){
                zipFormat(file,parentFolder+ File.separator+file.getName(),zipOutputStream);
                continue;
            }
            FileInputStream fileInputStream = new FileInputStream(file);
            ZipEntry zipEntry = new ZipEntry(parentFolder+File.separator+file.getName());
            zipOutputStream.putNextEntry(zipEntry);
            byte[] bytes= new byte[1024];
            int length;
            while((length=fileInputStream.read(bytes))>=0){
                zipOutputStream.write(bytes,0,length);
            }
            fileInputStream.close();
        }
    }
}
