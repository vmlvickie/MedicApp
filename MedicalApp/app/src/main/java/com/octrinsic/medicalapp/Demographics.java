package com.octrinsic.medicalapp;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class Demographics extends AppCompatActivity {
   EditText etAge;
    Spinner spGender;
    EditText etWeight;
    EditText etHeight;
    DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demographics);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbHelper = new DatabaseHelper(this);

       etAge = (EditText) findViewById(R.id.etAge);
        spGender = (Spinner) findViewById(R.id.spGender);
        etWeight = (EditText) findViewById(R.id.etWeight);
        etHeight = (EditText) findViewById(R.id.etHeigt);
        initializeGender();
        //initializeAge();
    }

    public void onSubmitDemographics(View view){
        try {
            boolean dataExists = dbHelper.chechIfDataExists(Patient.nationalpassportid);
            if(!dataExists) {
                String age = etAge.getText().toString();//float
                String gender = spGender.getSelectedItem().toString();
                String weight = etWeight.getText().toString();//float
                String height = etHeight.getText().toString();//float

                if ("".equals(age)) {
                    alertUser("You have to enter your age");
                    return;
                }
                if ("".equals(weight)) {
                    alertUser("You have to enter weight");
                    return;
                }
                if ("".equals(height)) {
                    alertUser("Please enter weight");
                    return;
                }
                int age_d = Integer.parseInt(age);
                double weight_d = Double.parseDouble(weight);
                double height_d = Double.parseDouble(height);
                long result = dbHelper.enterDemographics(age_d, gender, weight_d, height_d, Patient.nationalpassportid);
                if (result > 0) {
                    Toast.makeText(this, "Demographics captured", Toast.LENGTH_SHORT).show();
                    clearWidgets();
                } else {
                    alertUser("Demographics not captured");
                }
            }else{
                alertUser("Your data already exists");
                return;
            }

        }catch(NumberFormatException e){
            alertUser("Invalid input");
        }
    }
    public void clearWidgets(){
        etAge.setText("");
        etWeight.setText("");
        etHeight.setText("");
    }
    public void alertUser(String msg){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Input error");
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton("Ok", null);
        alertDialog.show();
    }

    private void initializeGender() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Male");
        list.add("Female");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGender.setAdapter(adapter);
    }

}
