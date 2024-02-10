package com.analogics.ocr;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.analogics.R;
import com.analogics.ui.billing.Billing_SearchBy_Activity;

import java.io.File;
import java.util.ArrayList;

public class OfflineImagesActivity extends AppCompatActivity implements ClickInterFace {


    TextView titleView;
    Button processBtn;
    ArrayList<String> photoList;
    String folderPath;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_images);

        titleView = findViewById(R.id.titleView);

        String meterNumberFolderName = getIntent().getStringExtra("folderName");

        String sdcardPath = Environment.getExternalStorageDirectory() + "/Android/";
        folderPath = sdcardPath + "TSSPDCL_OFFLINE_IMAGES/" + meterNumberFolderName;

        photoList = getImageNamesFromFolder(folderPath);

        RecyclerView recyclerView = findViewById(R.id.imagesRv);

        recyclerView.setAdapter(new ImageItem(photoList, OfflineImagesActivity.this, folderPath, this));
        titleView.setText(String.format("S.No:  " + meterNumberFolderName));
        processBtn = findViewById(R.id.processBtn);

        processBtn.setOnClickListener(view -> {
            Intent i = new Intent(OfflineImagesActivity.this, Billing_SearchBy_Activity.class);
            i.putExtra("serviceNo", meterNumberFolderName);
            startActivity(i);
        });
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

    @Override
    public void onItemClick(@NonNull View view, int position) {
        String imagePath = folderPath + File.separator + photoList.get(position);
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        ImageSheet imageSheet = new ImageSheet(bitmap);
        FragmentManager fragmentManager = getSupportFragmentManager();
        imageSheet.show(fragmentManager, "");
    }

    @Override
    public void onValueReceived(String value) {

    }
}