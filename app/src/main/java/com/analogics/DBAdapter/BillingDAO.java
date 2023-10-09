package com.analogics.DBAdapter;

import android.app.Activity;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class BillingDAO extends Activity {
	
	public List<String> update_autocomplete(Integer type ,String districtName){
		List<String> recordlist=new ArrayList<String>();
		DBAdapter dbAdapter=null;
		try{
			dbAdapter = DBAdapter.getDBAdapterInstance(this);
			dbAdapter.openDataBase();
			String getAccountidQuery;
			if(type==1)
			{
				getAccountidQuery = "select Service_Number from consumer_data where Dist_Zone_Name='"+districtName+"' ;";
			}
			else if(type==2)
			{
				getAccountidQuery = "select Usc_Code from consumer_data  where Dist_Zone_Name='"+districtName+"' ;";
			}else if(type==3)
			{
				getAccountidQuery = "select Consumer_Name from consumer_data  where Dist_Zone_Name='"+districtName+"' ;";
			}else if(type==4)
			{
				getAccountidQuery = "select Meter_Number from consumer_data  where Dist_Zone_Name='"+districtName+"' ;";
			}
			else
				getAccountidQuery="";
			
			Cursor cursor = dbAdapter.selectRecordsFromDB(getAccountidQuery,null);
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				recordlist.add(cursor.getString(0));
				while (cursor.moveToNext()) {
					recordlist.add(cursor.getString(0));
				}
			}
			dbAdapter.close();
		}
		catch(Exception e)
		{
			dbAdapter.close();
			e.printStackTrace();
		}
		return recordlist;
	}

	public List<String> update_DISTNameSpinner(){
		List<String> recordlist=new ArrayList<String>();
		DBAdapter dbAdapter=null;
		try{
			dbAdapter = DBAdapter.getDBAdapterInstance(this);
			dbAdapter.openDataBase();
			String getAccountidQuery;

			getAccountidQuery = "select distinct Dist_Zone_Name from consumer_data;";

			Cursor cursor = dbAdapter.selectRecordsFromDB(getAccountidQuery,
					null);
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				recordlist.add(cursor.getString(0));
				while (cursor.moveToNext()) {
					recordlist.add(cursor.getString(0));
				}
			}
			dbAdapter.close();
		}
		catch(Exception e)
		{
			dbAdapter.close();
			e.printStackTrace();
		}
		return recordlist;
	}
}
