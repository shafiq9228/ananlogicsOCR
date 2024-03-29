package com.analogics.ocr;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Pair;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.analogics.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class OfflineOcrInsertActivity extends AppCompatActivity {


    Button kwhImageBtn, mdImageBtn, fullImageBtn, saveBtn;
    ImageView kwhImageView, mdImageView, fullImageView;
    ImageView selectedImageView;
    Intent intent;
    String serviceNumber = "";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_ocr_insert);

        intent = new Intent(OfflineOcrInsertActivity.this, CameraActivity.class);
        serviceNumber = getIntent().getExtras().getString("serviceNumber", "");
        kwhImageBtn = findViewById(R.id.kwhImageBtn);
        mdImageBtn = findViewById(R.id.mdImageBtn);
        fullImageBtn = findViewById(R.id.fullImageBtn);
        saveBtn = findViewById(R.id.saveBtn);

        kwhImageView = findViewById(R.id.kwhImageView);
        mdImageView = findViewById(R.id.mdImageView);
        fullImageView = findViewById(R.id.fullImageView);


        kwhImageBtn.setOnClickListener(view -> {
            selectedImageView = kwhImageView;
            intent.putExtra("disableOcr", false);
            openOcrCamera(MeterType.kwh);
        });
        mdImageBtn.setOnClickListener(view -> {
            selectedImageView = mdImageView;
            intent.putExtra("disableOcr", false);
            openOcrCamera(MeterType.Rmd);
        });
        fullImageBtn.setOnClickListener(view -> {
            selectedImageView = fullImageView;
            intent.putExtra("disableOcr", true);
            openOcrCamera(MeterType.FullPhoto);
        });

        saveBtn.setOnClickListener(view -> {
            try {
                ArrayList<Pair<String, String>> list = new ArrayList<>();
                list.add(new Pair<>("kwhImage", convertUriToBase64(getApplicationContext(), kwhImageView.getTag().toString())));
                list.add(new Pair<>("fullImage", convertUriToBase64(getApplicationContext(), fullImageView.getTag().toString())));
                list.add(new Pair<>("mdImage", convertUriToBase64(getApplicationContext(), mdImageView.getTag().toString())));
                MeterDetails.saveOfflineImages(getApplicationContext(), serviceNumber + "", list);
                finish();
                Toast.makeText(this, "Photos Added Offline", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Please Take All Photos", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static String convertUriToBase64(Context context, String imageUri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(Uri.parse(imageUri));
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            if (inputStream != null) {
                inputStream.close();
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            return Base64.encodeToString(byteArray, Base64.DEFAULT);

        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception accordingly
            return null;
        }
    }

    private void openOcrCamera(MeterType ocrType) {
        intent.putExtra("isFromOfflineMode", true);
        intent.putExtra("type", ocrType.toString());
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityForResult(intent, 111);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (kwhDialogEditText != null){
//            kwhDialogEditText.clearFocus();
//        }
        if (data == null) return;
        String imageUri = data.getStringExtra("imageUri");
        selectedImageView.setImageURI(Uri.parse(imageUri));
        selectedImageView.setTag(imageUri);
    }
}