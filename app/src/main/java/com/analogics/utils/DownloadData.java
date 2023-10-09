package com.analogics.utils;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import android.widget.Toast;

import com.analogics.DBAdapter.DBAdapter;
import com.analogics.ui.menu.BillingMenuActivity;
import com.analogics.ui.menu.MainMenuActivity;
import com.analogics.ui.menu.Online_CommunicationMenuActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class DownloadData extends Thread {

    private JSONObject jsonObject = null;
    private DBAdapter dbAdapter = null;
    private Context mContext;
    private boolean isAppend = false;

    public DownloadData(Context mContext, JSONObject jsonObject, boolean isAppend) {
        this.jsonObject = jsonObject;
        this.mContext = mContext;
        this.isAppend = isAppend;

        dbAdapter = DBAdapter.getDBAdapterInstance(mContext);
        dbAdapter.openDataBase();

        boolean isUpdated = dbAdapter.checkLatestDb();
        if(!isUpdated){
            dbAdapter.alterTable();
        }
    }

    @Override
    public void run() {
        super.run();

        try {

            JSONObject jsonObjIntOnSd = jsonObject.getJSONObject("intOnSd");

            String serverIp = jsonObjIntOnSd.getString("serverIp");
            String data = jsonObjIntOnSd.getString("data");

            DBAdapter.ExecuteQry("Delete From INTONSD");

            String sql = " INSERT OR REPLACE INTO " +
                    " INTONSD (SERVERIP, DATA) " +
                    " VALUES (?, ?)";

            SQLiteStatement stmt = DBAdapter.myDataBase.compileStatement(sql);
            stmt.bindString(1, serverIp);
            stmt.bindString(2, data);

            stmt.executeInsert();
            stmt.clearBindings();


            JSONObject jsonObjHeader = jsonObject.getJSONObject("header");

            String eroCode = jsonObjHeader.getString("eroCode");
            String eroName = jsonObjHeader.getString("eroName");
            String sectionName = jsonObjHeader.getString("sectionName");
            String adeCellNo = jsonObjHeader.getString("adeCellNo");
            String aaoCellNo = jsonObjHeader.getString("aaoCellNo");

            DBAdapter.ExecuteQry("Delete From HEADER");

            sql = " INSERT OR REPLACE INTO " +
                    " HEADER (EROCODE, ERONAME, SECTIONNAME, AAOCELNUMBER, ADECELLNUMBER) " +
                    " VALUES (?, ?, ?, ?, ?)";

            stmt = DBAdapter.myDataBase.compileStatement(sql);
            stmt.bindString(1, eroCode);
            stmt.bindString(2, eroName);
            stmt.bindString(3, sectionName);
            stmt.bindString(4, adeCellNo);
            stmt.bindString(5, aaoCellNo);

            stmt.executeInsert();
            stmt.clearBindings();

            JSONArray jsonArray = jsonObject.getJSONArray("inputList");

            if (jsonArray.length() > 0) {
                if(!isAppend)
                    DBAdapter.ExecuteQry("Delete From INPUT_MASTER");

                sql = "INSERT OR REPLACE INTO " +
                        " INPUT_MASTER (CONSUMER_NUM, METER_NUM, USC_NUM, NAME, ADD1, ADD2, ADD3," +
                        " AREA_CODE, STRUCT_CODE, GROUP_TS, CATEGORY, SUBCAT, PHASE, MF, PREV_KWH," +
                        " PREV_EXPORT_READ, PREV_MTR_STS, PREV_DATE, FREEZED_READ, LAST_PAID_DATE," +
                        " ARREAR_BFR, ARREAR_AFTR, ADDITIONAL_CHRG, ACD_INTREST, PAMOUNT, P_ED," +
                        " EDINT, DIFF_TARIFF, CUS_CLMN1, CUSCLMN2, ACD, AVG_UNIT, AVG_PF, AVG_MD, " +
                        " SEASONALFLAG, CAPASITORFLAG, MTR_UNMTR_FLAG, TRIVECTORFLAG, LVSIDEFLAG, " +
                        " ITSECCTORFLAG, ED_EXEMPTIONFLAG, SCSTFLAG, FLAG, ELE_NOONELE_FLAG, NET_MTRFLAG," +
                        " MTR_CHNG_FLAG, MTR_CLASS, IRFLAG, OCUPFLAG, CONTRACTEDLOAD, CONNECTEDLOAD, " +
                        " PREV_KVAH, PREV_SOLAR_KVAH, STS4_KWH_READ,STS4_KVAH_READ, STS4_SOLAR_READ, CNCTED_POINT, " +
                        " CARY_FWD_UNITS, CLUBID, CLUB_FLAG, POLE_NUM, AGL_SER1, AGL_SER2, AGL_SER3, AGL_ARR1, " +
                        " AGL_ARR2, AGL_ARR3, CASE_AMT, DEV_CHRG, APP_VER, ERONAME, SECTIONNAME) " +
                        " VALUES (?, ?, ?,?,?,?, ?, ?,?,?, ?, ?,?,?,?, ?, ?,?,?, ?, ?,?,?,?, ?, ?,?,?, " +
                        " ?, ?,?,?,?, ?, ?,?,?, ?, ?,?,?,?, ?, ?,?,?, ?, ?,?,?,?, ?, ?,?,?," +
                        " ?,?,?,?, ?, ?,?,?, ?, ?,?,?,?, ?, ?, ?, ?)";

                stmt = DBAdapter.myDataBase.compileStatement(sql);

                int i = 0;
                for (i=0; i < jsonArray.length(); i++) {
                    JSONObject c = jsonArray.getJSONObject(i);
                    stmt.bindString(1, c.getString("consumerNo"));
                    stmt.bindString(2, c.getString("meterNo"));
                    stmt.bindString(3, c.getString("uscNo"));
                    stmt.bindString(4, c.getString("name"));
                    stmt.bindString(5, c.getString("add1"));
                    stmt.bindString(6, c.getString("add2"));
                    stmt.bindString(7, c.getString("add3"));
                    stmt.bindString(8, c.getString("areaCode"));
                    stmt.bindString(9, c.getString("structCode"));
                    stmt.bindString(10, c.getString("groupTs"));
                    stmt.bindString(11, c.getString("category"));
                    stmt.bindString(12, c.getString("subCat"));
                    stmt.bindString(13, c.getString("phase"));
                    stmt.bindString(14, c.getString("mf"));
                    stmt.bindString(15, c.getString("prevKwh"));
                    stmt.bindString(16, c.getString("prevExportRead"));
                    stmt.bindString(17, c.getString("prevMtrSts"));
                    stmt.bindString(18, c.getString("prevDate"));
                    stmt.bindString(19, c.getString("freezeRead"));
                    stmt.bindString(20, c.getString("lastPaidDate"));
                    stmt.bindString(21, c.getString("arrearBfr"));
                    stmt.bindString(22, c.getString("arrearAftr"));
                    stmt.bindString(23, c.getString("additionalChrg"));
                    stmt.bindString(24, c.getString("acdInterest"));
                    stmt.bindString(25, c.getString("pamount"));
                    stmt.bindString(26, c.getString("ped"));
                    stmt.bindString(27, c.getString("edInt"));
                    stmt.bindString(28, c.getString("diffTariff"));
                    stmt.bindString(29, c.getString("cusClmn1"));
                    stmt.bindString(30, c.getString("cusClmn2"));
                    stmt.bindString(31, c.getString("acd"));
                    stmt.bindString(32, c.getString("avgUnit"));
                    stmt.bindString(33, c.getString("avgPf"));
                    stmt.bindString(34, c.getString("avgMd"));
                    stmt.bindString(35, c.getString("seasonFlag"));
                    stmt.bindString(36, c.getString("capacitorFlag"));
                    stmt.bindString(37, c.getString("mtrUnmtrFlag"));
                    stmt.bindString(38, c.getString("trivectorFlag"));
                    stmt.bindString(39, c.getString("lvsideFlag"));
                    stmt.bindString(40, c.getString("itSectorFlag"));
                    stmt.bindString(41, c.getString("edExemptionFlag"));
                    stmt.bindString(42, c.getString("scstFlag"));
                    stmt.bindString(43, c.getString("flag"));
                    stmt.bindString(44, c.getString("eleNonEleFlag"));
                    stmt.bindString(45, c.getString("netMtrFlag"));
                    stmt.bindString(46, c.getString("mtrChngFlag"));
                    stmt.bindString(47, c.getString("mtrClass"));
                    stmt.bindString(48, c.getString("irflag"));
                    stmt.bindString(49, c.getString("occFlag"));
                    stmt.bindString(50, c.getString("contractLoad"));
                    stmt.bindString(51, c.getString("connectLoad"));
                    stmt.bindString(52, c.getString("prevKvah"));
                    stmt.bindString(53, c.getString("prevSolarKvah"));
                    stmt.bindString(54, c.getString("sts4KwhRead"));
                    stmt.bindString(55, c.getString("sts4KvahRead"));
                    stmt.bindString(56, c.getString("sts4SolarRead"));
                    stmt.bindString(57, c.getString("cnctedPt"));
                    stmt.bindString(58, c.getString("carryFwdUnits"));
                    stmt.bindString(59, c.getString("clubid"));
                    stmt.bindString(60, c.getString("clubFlag"));
                    stmt.bindString(61, c.getString("poleNo"));
                    stmt.bindString(62, c.getString("aglSer1"));
                    stmt.bindString(63, c.getString("aglSer2"));
                    stmt.bindString(64, c.getString("aglSer3"));
                    stmt.bindString(65, c.getString("aglArr1"));
                    stmt.bindString(66, c.getString("aglArr2"));
                    stmt.bindString(67, c.getString("aglArr3"));
                    stmt.bindString(68, c.getString("caseAmt"));
                    stmt.bindString(69, c.getString("devChrg"));
                    stmt.bindString(70, c.getString("appVer"));
                    stmt.bindString(71, eroName);
                    stmt.bindString(72, sectionName);

                    stmt.executeInsert();
                    stmt.clearBindings();
                }
                if(i == jsonArray.length()){
                    dismissDialog("Data Download Successfully");
                }

            } else {
                dismissDialog("Data doesn't exist");
            }
        } catch (Exception e) {
            dismissDialog(e.getMessage());
        }
    }

    private void dismissDialog(String msg){
        ((Activity)mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BillingMenuActivity.progressDialog.dismiss();
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();

            }
        });
    }

}
