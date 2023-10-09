package com.analogics.ui.billing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.analogics.DBAdapter.DBAdapter;
import com.analogics.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;


public class DeleteData extends AppCompatActivity {

    private TextInputLayout tilServiceNo;
    private TextInputEditText etServiceNo;
    private AppCompatButton btnDeleteData;
    private CheckBox cbDeleteAllData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_data);
        init();
    }

    private void init(){
        initElements();
        initListeners();
    }

    private void initElements() {

        tilServiceNo = findViewById(R.id.til_service_no);
        etServiceNo = findViewById(R.id.et_service_no);
        btnDeleteData = findViewById(R.id.btn_delete_master_data);
        cbDeleteAllData = findViewById(R.id.cb_delete_all_data);
        cbDeleteAllData.setChecked(false);

    }

    private void initListeners() {

        btnDeleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(cbDeleteAllData.isChecked()){
                    deleteData(null);
                }else{
                String serviceNo = Objects.requireNonNull(etServiceNo.getText()).toString().trim();
                if (!serviceNo.trim().isEmpty()) {
                    tilServiceNo.setError(null);
                    tilServiceNo.setErrorEnabled(false);
                    deleteData(serviceNo);
                } else {
                    tilServiceNo.setErrorEnabled(true);
                    tilServiceNo.setError("Please Enter Service No.");

                    Toast.makeText(DeleteData.this, "Please Enter Service No.", Toast.LENGTH_LONG).show();
                }
                }

            }
        });

    }

    private void deleteData(String serviceNo) {

        if(cbDeleteAllData.isChecked()){
            deleteDataFromOutputMaster();
        }else{
            deleteDataUsingServiceNum(serviceNo);
        }

    }

    private void deleteDataUsingServiceNum(String serviceNo) {
        String deleteDataUsingServiceNo = "DELETE FROM OUTPUT_MASTER2 WHERE consumer_num = '"+serviceNo+"'";
        DBAdapter dbAdapter = DBAdapter.getDBAdapterInstance(this);
        dbAdapter.openDataBase();
        boolean result = DBAdapter.ExecuteQry(deleteDataUsingServiceNo);
        if(result){
            Toast.makeText(this, "Bill Deleted Successfully", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }


    private void deleteDataFromOutputMaster() {
        DBAdapter dbAdapter = DBAdapter.getDBAdapterInstance(this);
        dbAdapter.openDataBase();

        String dltOutputMasterDataQry = "DELETE FROM OUTPUT_MASTER2";
        boolean result = DBAdapter.ExecuteQry(dltOutputMasterDataQry);

        String duplicateQry = "DELETE FROM duplicateBillPurpose";
        result = DBAdapter.ExecuteQry(duplicateQry);

        if(result){
            Toast.makeText(this, "Bill Deleted Successfully", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }
}