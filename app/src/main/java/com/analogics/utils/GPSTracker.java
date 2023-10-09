package com.analogics.utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GPSTracker extends Service implements LocationListener {

	private final Context mContext;

	// flag for GPS status
	boolean isGPSEnabled = false;

	// flag for network status
	boolean isNetworkEnabled = false;

	// flag for GPS status
	boolean canGetLocation = false;

	Location location; // location
	double latitude; // latitude
	double longitude; // longitude

	// The minimum distance to change Updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters

	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1000 * 5 * 1; // 1 minute

	// Declaring a Location Manager
	protected LocationManager locationManager;

	public GPSTracker(Context context) {
		this.mContext = context;
		getLocation();
	}

	@SuppressLint("MissingPermission")
	public Location getLocation() {
		try {
			locationManager = (LocationManager) mContext
					.getSystemService(LOCATION_SERVICE);

			// getting GPS status
			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
				// no network provider is enabled
			} else {
				this.canGetLocation = true;
				if (isNetworkEnabled) {
					locationManager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER,
							MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					Log.d("Network", "Network");
					if (locationManager != null) {
						location = locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();
						}
					}
				}
				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {
					if (location == null) {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER,
								MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						Log.d("GPS Enabled", "GPS Enabled");
						if (locationManager != null) {
							location = locationManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return location;
	}
	
	/**
	 * Stop using GPS listener
	 * Calling this function will stop using GPS in your app
	 * */
	public void stopUsingGPS(){
		if(locationManager != null){
			locationManager.removeUpdates(GPSTracker.this);
		}		
	}
	
	/**
	 * Function to get latitude
	 * */
	public double getLatitude(){
		if(location != null){
			latitude = location.getLatitude();
		}
		
		// return latitude
		return latitude;
	}
	
	/**
	 * Function to get longitude
	 * */
	public double getLongitude(){
		if(location != null){
			longitude = location.getLongitude();
		}
		
		// return longitude
		return longitude;
	}
	
	/**
	 * Function to check GPS/wifi enabled
	 * @return boolean
	 * */
	public boolean canGetLocation() {
		return this.canGetLocation;
	}
	
	/**
	 * Function to show bluetooth_settings alert dialog
	 * On pressing Settings button will lauch Settings Options
	 * */
	public void showSettingsAlert(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
   	 
        // Setting Dialog Title
        alertDialog.setTitle("GPS is bluetooth_settings");
 
        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to bluetooth_settings menu?");
 
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            	mContext.startActivity(intent);
            }
        });
 
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });
 
        // Showing Alert Message
        alertDialog.show();
	}

	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

    public static class FileOperations {

         String _IN_FILEPATH = Environment.getExternalStorageDirectory()+ "/BMC_IN/";
         String _OUT_FILEPATH = Environment.getExternalStorageDirectory()+ "/BMC_OUT/";
         String _TEMP_FILEPATH = Environment.getExternalStorageDirectory()+ "/BMC_TEMP/";



        public void checkDirectroies(){

            File in_dir = new File(_IN_FILEPATH);
            if(!(in_dir.exists())) {
                in_dir.mkdir();
            }
            File out_dir = new File(_OUT_FILEPATH);
            if(!(out_dir.exists())) {
                out_dir.mkdir();
            }
            File temp_dir = new File(_TEMP_FILEPATH);
            if(!(temp_dir.exists())) {
                temp_dir.mkdir();
            }
        }



        public void writeToFile(String data, String fileName, Context context) {
            try {
                String sdcardPath= Environment.getExternalStorageDirectory()+"";

                File File=new File(sdcardPath+"/BMC_OUT/",fileName);
                if(!File.exists()){
                    File.createNewFile();
                }

                OutputStreamWriter writer = new OutputStreamWriter(
                        new FileOutputStream(sdcardPath+"/BMC_OUT/"
                                + fileName, true), "UTF-8");
                BufferedWriter fbw = new BufferedWriter(writer);
                fbw.write(data);
                fbw.newLine();
                fbw.close();
            }
            catch (IOException e) {
                Log.e("Exception", "File write failed: " + e.toString());
            }
        }
        public void write_GPSData_to_IN(String _IN_FILEPATH, String KSRTC_GPS_filename, String gpsdata){

            try {


                File folder=new File(_IN_FILEPATH);
                if(!folder.exists()){
                    folder.mkdirs();
                }

                File gpsFile=new File(folder,KSRTC_GPS_filename);
                if(!gpsFile.exists()){
                    gpsFile.createNewFile();
                }


                OutputStreamWriter writer = new OutputStreamWriter(
                        new FileOutputStream(_IN_FILEPATH
                                + KSRTC_GPS_filename, true), "UTF-8");
                BufferedWriter fbw = new BufferedWriter(writer);
                fbw.write(gpsdata);
                fbw.newLine();
                fbw.close();

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }


        }

        public void moveGPSData_to_OUT(){



            FileInputStream instream = null;
        FileOutputStream outstream = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date();

        String currentdateTime=sdf.format(curDate);


            File folder=new File(_IN_FILEPATH);
            if(!folder.exists()){
                folder.mkdirs();
            }

            File gpsFile;
            gpsFile = new File(folder,"KSRTC_GPS.txt");
            if(!gpsFile.exists()){
                try {
                    gpsFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try{
                File infile =new File(_IN_FILEPATH+"KSRTC_GPS.txt");
                File outfile =new File(_OUT_FILEPATH+"KSRTCGPS_"+currentdateTime+".txt");

                instream = new FileInputStream(infile);
                outstream = new FileOutputStream(outfile,true);

                byte[] buffer = new byte[1024];

                int length;
                /*copying the contents from input stream to
                 * output stream using read and write methods
                 */
                while ((length = instream.read(buffer)) > 0){
                    outstream.write(buffer, 0, length);
                }

                //Closing the input/output file streams
                instream.close();
                outstream.close();

                System.out.println("File copied successfully!!");
                infile.delete();
            }catch(IOException ioe){
                ioe.printStackTrace();
             }

        }







    }
}
