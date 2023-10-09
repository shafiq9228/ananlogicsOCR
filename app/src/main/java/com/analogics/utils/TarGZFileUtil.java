package com.analogics.utils;

import android.os.Environment;

import org.rauschig.jarchivelib.ArchiveFormat;
import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.ArchiverFactory;
import org.rauschig.jarchivelib.CompressionType;

import java.io.File;
import java.io.IOException;

public class TarGZFileUtil {
	static String sdcardPath= Environment.getExternalStorageDirectory()+"/Android/";
	String _TEMP_FILEPATH = sdcardPath+"TSSPDCL_TEMP/";
	String _OUT_FILEPATH = sdcardPath+"TSSPDCL_OUT/";
	String _METER_IMAGE_FILEPATH = sdcardPath+"TSSPDCL_IMAGES";


	public String tarGZ(String archiveName){

		String sdcardPath= Environment.getExternalStorageDirectory()+"";

		File destination = new File(_TEMP_FILEPATH);
		File source = new File(_OUT_FILEPATH);

		Archiver archiver = ArchiverFactory.createArchiver(ArchiveFormat.TAR, CompressionType.GZIP);
		try {
			@SuppressWarnings("unused")
            File archive = archiver.create(archiveName, destination, source);
		} catch (IOException e) {

			e.printStackTrace();


		}

		EncodeBased64Binary encodeBased64Binary=new EncodeBased64Binary();

		String base64Str = null;
		try {
			base64Str = encodeBased64Binary.encodeFileToBase64Binary(_TEMP_FILEPATH+archiveName+".tar.gz");
		} catch (IOException e) {

			e.printStackTrace();
		}

		System.out.println("base64 data::" + base64Str);
		return base64Str;
	}




    public String tarGZ_BillingSingleFile(String archiveName, String FilePath){
        File destination = new File(_TEMP_FILEPATH);
        File source = new File(FilePath);
        if(source.isFile()){
            Archiver archiver = ArchiverFactory.createArchiver(ArchiveFormat.TAR, CompressionType.GZIP);
            try {
                @SuppressWarnings("unused")
                File archive = archiver.create(archiveName, destination, source);
            } catch (IOException e) {

                e.printStackTrace();
            }

            EncodeBased64Binary encodeBased64Binary=new EncodeBased64Binary();
            String base64Str = null;
            try {
                base64Str = encodeBased64Binary.encodeFileToBase64Binary(_TEMP_FILEPATH+archiveName+".tar.gz");
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("base64 data::" + base64Str);
            return base64Str;
        }else {
            return "FAIL";
        }
    }

    public String tarGZBMC_billingSinglefile(String archiveName, String filepath){
        String sdcardPath= Environment.getExternalStorageDirectory()+"";
        File destination = new File(_TEMP_FILEPATH);
		File source = new File(_OUT_FILEPATH+archiveName);
		if(source.isFile()){
			Archiver archiver = ArchiverFactory.createArchiver(ArchiveFormat.TAR, CompressionType.GZIP);
			try {
				@SuppressWarnings("unused")
                File archive = archiver.create(archiveName, destination, source);
			} catch (IOException e) {
				e.printStackTrace();
			}
			EncodeBased64Binary encodeBased64Binary=new EncodeBased64Binary();
			String base64Str = "";
			try {
				base64Str = encodeBased64Binary.encodeFileToBase64Binary(_TEMP_FILEPATH+archiveName+".tar.gz");
			} catch (IOException e) {

				e.printStackTrace();
			}
			System.out.println("base64 data::" + base64Str);
			return base64Str;
		}else {
			return "FAIL";
		}
	}



	public String tarGZ_SingleFile(String archiveName, String FilePath){
		String _IN_FILEPATH_1 = sdcardPath+"TSSPDCL/server/in/";
		String _OUT_FILEPATH_1 = sdcardPath+"TSSPDCL/server/out/";
		String _TEMP_FILEPATH_1 = sdcardPath+"TSSPDCL/server/temp/";
		File destination = new File(_TEMP_FILEPATH_1);
		File source = new File(FilePath);
		if(source.isFile()){
			Archiver archiver = ArchiverFactory.createArchiver(ArchiveFormat.TAR, CompressionType.GZIP);
			try {
				@SuppressWarnings("unused")
                File archive = archiver.create(archiveName, destination, source);
			} catch (IOException e) {
				e.printStackTrace();
			}

			EncodeBased64Binary encodeBased64Binary=new EncodeBased64Binary();

			String base64Str = null;
			try {
				base64Str = encodeBased64Binary.encodeFileToBase64Binary(_TEMP_FILEPATH_1+archiveName+".tar.gz");
			} catch (IOException e) {

				e.printStackTrace();
			}

			System.out.println("base64 data::" + base64Str);
			return base64Str;
		}else {

			return "FAIL";
		}
	}


	public String tarGZForofflineupload(String archiveName){

		String sdcardPath = Environment.getExternalStorageDirectory()+"";



		File destination = new File(sdcardPath+"/tmp/");
		//File source = new File(sdcardPath+"/home/admin/indus/tmp/out");
		File source = new File(sdcardPath+"/temp/");

		Archiver archiver = ArchiverFactory.createArchiver(ArchiveFormat.TAR, CompressionType.GZIP);
		try {
			@SuppressWarnings("unused")
            File archive = archiver.create(archiveName, destination, source);
		} catch (IOException e) {

			e.printStackTrace();

		}


		EncodeBased64Binary encodeBased64Binary=new EncodeBased64Binary();

		String base64Str = null;
		try {
			base64Str = encodeBased64Binary.encodeFileToBase64Binary(sdcardPath+"/tmp/"+archiveName+".tar.gz");
		} catch (IOException e) {

			e.printStackTrace();
		}

		System.out.println("base64 data::" + base64Str);
		return base64Str;
	}
public void unTar(){

	Archiver archiver = ArchiverFactory.createArchiver("tar", "gz");
	//archiver.extract(archiveFile, destDir);
}

}
