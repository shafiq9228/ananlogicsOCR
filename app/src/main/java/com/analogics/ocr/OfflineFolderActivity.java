package com.analogics.ocr;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Pair;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.analogics.R;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;


public class OfflineFolderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_folder);

        String sdcardPath = Environment.getExternalStorageDirectory() + "/Android/";
        String folderPath = sdcardPath + "TSSPDCL_OFFLINE_IMAGES";
        ArrayList<Pair<String ,String>> list = getFoldersList(folderPath);
        CustomAdapter adapter = new CustomAdapter(this, list);

        // new ArrayAdapter<>(this, android.R.layout.simple_list_item_2, list);
        ListView listView = findViewById(R.id.folderListView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Pair<String,String> selectedItem = (Pair<String ,String>) parent.getItemAtPosition(position);
            Intent i = new Intent(this, OfflineImagesActivity.class);
            i.putExtra("folderName", selectedItem.first.replace("S.No: ", ""));
            startActivity(i);
        });

    }


    public static ArrayList<Pair<String, String>> getFoldersList(String path) {
        ArrayList<Pair<String, String>> folderList = new ArrayList<>();

        File directory = new File(path);

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        String createdDate = getCreationDate(file);
                        folderList.add(new Pair("S.No: " + file.getName(),createdDate));

                    }
                }
            }
        }

        return folderList;
    }

    private static String getCreationDate(File file) {
        try{
            if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)) {
                return "";
            }
            Path path;

            path = FileSystems.getDefault().getPath(file.getAbsolutePath());

            try {
                BasicFileAttributes attributes = null;
                attributes = Files.readAttributes(path, BasicFileAttributes.class);
                return String.valueOf(attributes.creationTime().toString().split("T")[0]);
            } catch (Exception e) {
                e.printStackTrace();
                return "N/A";
            }
        }catch (Exception e){
            return "";
        }

    }
}
//
//import android.content.Context;
//        import android.view.LayoutInflater;
//        import android.view.View;
//        import android.view.ViewGroup;
//        import android.widget.ArrayAdapter;
//        import android.widget.TextView;
//
//        import androidx.annotation.NonNull;
//        import androidx.annotation.Nullable;
//
//        import java.util.List;

