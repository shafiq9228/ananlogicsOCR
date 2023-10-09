package com.analogics.ui.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.analogics.R;


public class PC_Comm_MenuActivity extends AppCompatActivity {

    Button Btn_PC_To_SBM;
    Button Btn_SBM_To_PC;
    Button Btn_Image_Upload;
    Button Btn_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_c__comm__menu);

        ui_init();
        clickListener();
    }

    public void ui_init(){

        Btn_PC_To_SBM=findViewById(R.id.Btn_PC_To_SBM);
        Btn_SBM_To_PC=findViewById(R.id.Btn_SBM_To_PC);
        Btn_Image_Upload=findViewById(R.id.Btn_Image_Upload);
        Btn_home=findViewById(R.id.Btn_home);

    }

    public void clickListener() {
        Btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), MainMenuActivity.class);
                startActivity(i);
                finish();
            }
        });
        Btn_PC_To_SBM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), MainMenuActivity.class);
                startActivity(i);
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // If want to block just return false
            return false;
        }
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            // If want to block just return false
            return false;
        }
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            // If want to block just return false
            return false;
        }
        if (keyCode == KeyEvent.KEYCODE_SEARCH) {
            // If want to block just return false
            return false;
        }
        if (keyCode == KeyEvent.KEYCODE_SETTINGS) {
            // If want to block just return false
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}