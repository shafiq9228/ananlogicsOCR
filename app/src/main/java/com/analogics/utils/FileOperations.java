package com.analogics.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class FileOperations {
	static String sdcardPath= Environment.getExternalStorageDirectory()+"/Android/";

	static String imagesDir = sdcardPath+"TSSPDCL/";
	String _IN_FILEPATH = sdcardPath+"TSSPDCL_IN/";
	String _OUT_FILEPATH = sdcardPath+"TSSPDCL_OUT/";
	String _TEMP_FILEPATH = sdcardPath+"TSSPDCL_TEMP/";
 	String _METER_IMAGES = sdcardPath+"TSSPDCL_METER_IMAGES";
	String _IN_FILEPATH_1 = sdcardPath+"TSSPDCL/server/in/";
	String _OUT_FILEPATH_1 = sdcardPath+"TSSPDCL/server/out/";
	String _TEMP_FILEPATH_1 = sdcardPath+"TSSPDCL/server/temp/";
	String _OFFLINE_PATH = sdcardPath+"TSSPDCL_OFFLINE_IMAGES";
	String DB_PATH = sdcardPath;

	
	public void checkDirectroies(){
		File img_dir = new File(imagesDir);
		if(!(img_dir.exists())) {
			img_dir.mkdir();
		}else{
			for(File file: img_dir.listFiles())
				if (!file.isDirectory())
					file.delete();
		}
		File IMAGES_dir = new File(_METER_IMAGES);
		if (!IMAGES_dir.exists()){
			IMAGES_dir.mkdir();
		}
		File DB_dir = new File(DB_PATH);
		if(!(DB_dir.exists())) {
			DB_dir.mkdir();
		}
		File DB_dir_OFFLINE = new File(_OFFLINE_PATH);
		if(!(DB_dir_OFFLINE.exists())) {
			DB_dir_OFFLINE.mkdir();
		}
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
		File in_dir1 = new File(_IN_FILEPATH_1);
		if(!(in_dir1.exists())) {
			in_dir1.mkdir();
		}
		File out_dir1 = new File(_OUT_FILEPATH_1);
		if(!(out_dir1.exists())) {
			out_dir1.mkdir();
		}
		File temp_dir1 = new File(_TEMP_FILEPATH_1);
		if(!(temp_dir1.exists())) {
			temp_dir1.mkdir();
		}

	}

	public void writeToFile(String billNo, String data, String fileName, Context context) {
		try {

			File File=new File(_OUT_FILEPATH,fileName);
			if(!File.exists()){
				File.createNewFile();
			}

			FileUtils.writeStringToFile(File, data, true);
			FileUtils.writeStringToFile(File, "\n", true);

			String writtenString = FileUtils.readFileToString(File);
			if(StringUtils.isBlank(writtenString)){
				File.delete();
				File inFile = new File(_IN_FILEPATH, fileName);
				FileUtils.writeStringToFile(inFile, billNo, true);
			}
		}
		catch (IOException e) {
			Log.e("Exception", "File write failed: " + e.toString());
		}
	}

	public int fileCountOUT(){
		File folder=new File(_OUT_FILEPATH);
		if(!folder.exists()){
			folder.mkdirs();
		}
		File[] listOfFiles = folder.listFiles();
		System.out.println("File copied successfully!!");
		System.out.println("FILES  COUNT>>>"+listOfFiles.length);
		return listOfFiles.length;
	}



}
