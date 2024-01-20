package com.analogics.ocr;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.analogics.R;

import java.io.File;
import java.util.ArrayList;

public class OfflineFolderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_folder);

        String sdcardPath = Environment.getExternalStorageDirectory() + "/Android/";
        String folderPath = sdcardPath + "TSSPDCL_OFFLINE_IMAGES";
        ArrayList<String> list = getFoldersList(folderPath);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        ListView listView = findViewById(R.id.folderListView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = (String) parent.getItemAtPosition(position);
            Intent i = new Intent(this, OfflineImagesActivity.class);
            i.putExtra("folderName", selectedItem);
            startActivity(i);
        });

    }


    public static ArrayList<String> getFoldersList(String path) {
        ArrayList<String> folderList = new ArrayList<>();

        File directory = new File(path);

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        folderList.add(file.getName());
                    }
                }
            }
        }

        return folderList;
    }
}