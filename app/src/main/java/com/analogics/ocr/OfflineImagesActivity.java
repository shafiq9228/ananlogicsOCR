package com.analogics.ocr;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.analogics.R;

import java.io.File;
import java.util.ArrayList;

public class OfflineImagesActivity extends AppCompatActivity {


    TextView titleView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_images);

        titleView = findViewById(R.id.titleView);

        String meterNumberFolderName = getIntent().getStringExtra("folderName");

        String sdcardPath = Environment.getExternalStorageDirectory() + "/Android/";
        String folderPath = sdcardPath + "TSSPDCL_OFFLINE_IMAGES/"+meterNumberFolderName;

        ArrayList<String> list = getImageNamesFromFolder(folderPath);

        RecyclerView recyclerView = findViewById(R.id.imagesRv);
        recyclerView.setAdapter(new ImageItem(list, OfflineImagesActivity.this, folderPath));
        titleView.setText(String.format("S.No:  "+meterNumberFolderName));
    }

    public static ArrayList<String> getFoldersList(String path) {
        ArrayList<String> folderList = new ArrayList<>();

        File directory = new File(path);

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                        folderList.add(file.getName());

                }
            }
        }

        return folderList;
    }


    private ArrayList<String> getImageNamesFromFolder(String folderPath) {
        ArrayList<String> imageNamesList = new ArrayList<>();

        File folder = new File(folderPath);

        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();


            if (files != null) {
                for (File file : files) {
                    if (isImageFile(file)) {
                        imageNamesList.add(file.getName());
                    }
                }
            }
        }

        return imageNamesList;
    }

    private boolean isImageFile(File file) {
        String name = file.getName();
        return name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png") || name.endsWith(".gif");
    }
}