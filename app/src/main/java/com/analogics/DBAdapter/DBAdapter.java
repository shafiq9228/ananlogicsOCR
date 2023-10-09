package com.analogics.DBAdapter;

/**
 * @author ANIL REDDY GANTLA
 */

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DBAdapter extends SQLiteOpenHelper {

    private static final String AndroidDB = "Billingdb.db";

    public static SQLiteDatabase myDataBase;
    private final Context myContext;
    public final String DB_PATH = Environment.getExternalStorageDirectory() + "/Android/";

    //public final String DB_PATH = "/sdcard/";
    @SuppressLint("SdCardPath")
    //public final String DB_PATH="/storage/sdcard1/";
    // String DB_PATH =null;
    private static DBAdapter mDBConnection;

    /**
     * Constructor Takes and keeps a reference of the passed context in order to
     * access to the application assets and resources.
     *
     * @param context
     */

    private DBAdapter(Context context) {
        super(context, AndroidDB, null, 1);
        this.myContext = context;
        // DB_PATH = "/data/data/" +
        // context.getApplicationContext().getPackageName()+ "/databases/";
        // The Android's default system path of your application database is
        // "/data/data/mypackagename/databases/"
    }

    /**
     * getting Instance
     *
     * @param context
     * @return DBAdapter
     */
    public static synchronized DBAdapter getDBAdapterInstance(Context context) {
        if (mDBConnection == null) {
            mDBConnection = new DBAdapter(context);
        }
        return mDBConnection;
    }

    /**
     * Creates an empty database on the system and rewrites it with your own
     * database.
     **/
    public boolean createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {
            // do nothing - database already exist
            return dbExist;
        } else {
            // By calling following method
            // 1) an empty database will be created into the default system path
            // of your application
            // 2) than we overwrite that database with our database.
//			mDBConnection.openDataBase();
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("TAG", "createDataBase: " + e.toString());
//				throw new Error("Error copying database");
            }
            return dbExist;
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each
     * time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {


        try {
            String myPath = DB_PATH + AndroidDB;
            File f = new File(myPath);
            if (f.exists()) {
                return true;
            } else {

                return false;

            }
        } catch (Exception e) {
            return false;
            // database does't exist yet.
        }


    }

    /**
     * Copies your database from your local assets-folder to the just created
     * empty database in the system folder, from where it can be accessed and
     * handled. This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException {

        InputStream myInput = null;
        OutputStream myOutput = null;
        String outFileName = DB_PATH + AndroidDB;
        File file = new File(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        if (!file.exists()) {
            file.createNewFile();
        }
        try {
            myInput = myContext.getAssets().open(AndroidDB);
            myOutput = new FileOutputStream(outFileName);
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            myOutput.flush();
            myOutput.close();
            myInput.close();

        }
    }

    /**
     * Open the database
     *
     * @throws SQLException
     */
    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + AndroidDB;
        //String myPath=DB_PATH;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);
    }


    /**
     * Open the database
     *
     * @throws SQLException
     */

    /**
     * Open the database
     *
     * @throws SQLException
     */


    /**
     * Close the database if exist
     */
    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }

    /**
     * Call on creating data base for example for creating tables at run time
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    /**
     * can used for drop tables then call onCreate(db) function to create tables
     * again - upgrade
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    // ----------------------- CRUD Functions ------------------------------

    /**
     * This function used to select the records from DB.
     *
     * @param tableName
     * @param tableColumns
     * @param whereClase
     * @param whereArgs
     * @param groupBy
     * @param having
     * @param orderBy
     * @return A Cursor object, which is positioned before the first entry.
     */
    public Cursor selectRecordsFromDB(String tableName, String[] tableColumns,
                                      String whereClase, String whereArgs[], String groupBy,
                                      String having, String orderBy)
    {
        return myDataBase.query(tableName, tableColumns, whereClase, whereArgs,
                groupBy, having, orderBy);
    }

    /**
     * select records from db and return in list
     *
     * @param tableName
     * @param tableColumns
     * @param whereClase
     * @param whereArgs
     * @param groupBy
     * @param having
     * @param orderBy
     * @return ArrayList<ArrayList < String>>
     */
    public ArrayList<ArrayList<String>> selectRecordsFromDBList(
            String tableName, String[] tableColumns, String whereClase,
            String whereArgs[], String groupBy, String having, String orderBy) {

        ArrayList<ArrayList<String>> retList = new ArrayList<ArrayList<String>>();
        ArrayList<String> list = new ArrayList<String>();
        Cursor cursor = myDataBase.query(tableName, tableColumns, whereClase,
                whereArgs, groupBy, having, orderBy);
        if (cursor.moveToFirst()) {
            do {
                list = new ArrayList<String>();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    list.add(cursor.getString(i));
                }
                retList.add(list);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return retList;

    }

    /**
     * This function used to insert the Record in DB.
     *
     * @param tableName
     * @param nullColumnHack
     * @param initialValues
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */
    public long insertRecordsInDB(String tableName, String nullColumnHack,
                                  ContentValues initialValues) {

        return myDataBase.insert(tableName, nullColumnHack, initialValues);
    }

    /**
     * This function used to update the Record in DB.
     *
     * @param tableName
     * @param initialValues
     * @param whereClause
     * @param whereArgs
     * @return true / false on updating one or more records
     */
    public boolean updateRecordInDB(String tableName,
                                    ContentValues initialValues, String whereClause, String whereArgs[]) {
        return myDataBase.update(tableName, initialValues, whereClause,
                whereArgs) > 0;
    }

    /**
     * This function used to update the Record in DB.
     *
     * @param tableName
     * @param initialValues
     * @param whereClause
     * @param whereArgs
     * @return 0 in case of failure otherwise return no of row(s) are updated
     */
    public int updateRecordsInDB(String tableName, ContentValues initialValues,
                                 String whereClause, String whereArgs[]) {
        return myDataBase.update(tableName, initialValues, whereClause,
                whereArgs);
    }

    /**
     * This function used to delete the Record in DB.
     *
     * @param tableName
     * @param whereClause
     * @param whereArgs
     * @return 0 in case of failure otherwise return no of row(s) are deleted.
     */
    public int deleteRecordInDB(String tableName, String whereClause,
                                String[] whereArgs) {
        return myDataBase.delete(tableName, whereClause, whereArgs);
    }

    // --------------------- Select Raw Query Functions ---------------------

    /**
     * apply raw Query
     *
     * @param query
     * @param selectionArgs
     * @return Cursor
     */
    public Cursor selectRecordsFromDB(String query, String[] selectionArgs) {
        return myDataBase.rawQuery(query, selectionArgs);
    }

    // Getting Translates Count
    public int getduplicateBillCount() {
        String countQuery;
        Cursor cursor;
        try {
            countQuery = "select count(*) from duplicateBillPurpose";
            cursor = selectRecordsFromDB(countQuery, null);
            cursor.close();
            int retCnt = cursor.getCount();
            if (retCnt == 0) {
                countQuery = "DROP TABLE duplicateBillPurpose";
                cursor = selectRecordsFromDB(countQuery, null);
                cursor.close();
                countQuery = "CREATE TABLE IF NOT EXISTS duplicateBillPurpose(consumer_num TEXT,duplicateBillData TEXT, QrInformation TEXT)";
                cursor = selectRecordsFromDB(countQuery, null);
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage().contains("no such table")) {
				/*countQuery = "DROP TABLE duplicateBillPurpose";
				db = this.getReadableDatabase();
				cursor = db.rawQuery(countQuery, null);
				cursor.close();*/
                countQuery = "CREATE TABLE IF NOT EXISTS duplicateBillPurpose(consumer_num TEXT,duplicateBillData TEXT, QrInformation TEXT)";
                cursor = selectRecordsFromDB(countQuery, null);
                cursor.close();
            }
        }
        return 0;
    }

    public void executeRawQuery(String query) {

        myDataBase.execSQL(query);

    }

    public boolean checkdbopen() {
        if (myDataBase.isOpen()) {
            return true;
        } else {
            return false;
        }

    }
	public static boolean ExecuteQry(String Qry) {
		try {
			myDataBase.execSQL(Qry);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

    public boolean checkLatestDb() {
        Cursor mCursor = null;
        try {
            mCursor = myDataBase.rawQuery("SELECT * FROM INPUT_MASTER LIMIT 0", null);
            if (mCursor.getColumnIndex("SECTIONNAME") != -1)
                return true;
            else
                return false;

        } catch (Exception Exp) {
            Log.d("... - existsColumnInTable", "When checking whether a column exists in the table, an error occurred: " + Exp.getMessage());
            return false;
        } finally {
            if (mCursor != null) mCursor.close();
        }
    }


    public void alterTable(){
        try{
            String alterTableQuery = "ALTER TABLE INPUT_MASTER ADD COLUMN ";
            List<String> newColumns = new ArrayList<String>();
            newColumns.add("ERONAME");
            newColumns.add("SECTIONNAME");
            myDataBase.beginTransaction();
            for (String column : newColumns){
                myDataBase.execSQL(alterTableQuery + column +  " VARCHAR");
            }
            myDataBase.setTransactionSuccessful();
            myDataBase.endTransaction();
        }catch(Exception ex){

        }
    }
}
