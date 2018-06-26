package com.octrinsic.medicalapp;

import android.database.Cursor;
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

public class EditDemographics extends AppCompatActivity {
    DatabaseHelper dbHelper;
    Cursor biodataCursor;
    EditText etAge;
    Spinner spGender;
    EditText etWeight;
    EditText etHeight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_demographics);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbHelper = new DatabaseHelper(this);


        etAge = (EditText)findViewById(R.id.etAge);
        spGender = (Spinner) findViewById(R.id.spGender);
        etWeight = (EditText) findViewById(R.id.etWeight);
        etHeight = (EditText) findViewById(R.id.etHeigt);

        biodataCursor = dbHelper.getDemographics(Patient.nationalpassportid);

        if(biodataCursor != null) {
            initializeWidgets();
        }
    }

    private void initializeWidgets() {
        if(biodataCursor.moveToFirst()){
           etAge.setText(new Integer(biodataCursor.getInt(biodataCursor.getColumnIndex("age"))).toString());
            initializeGender();
            etWeight.setText(new Double(biodataCursor.getDouble(biodataCursor.getColumnIndex("weight"))).toString());
            etHeight.setText(new Double(biodataCursor.getDouble(biodataCursor.getColumnIndex("height"))).toString());
        }
    }

    private void initializeGender() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Male");
        list.add("Female");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGender.setAdapter(adapter);
    }




        public void onEditDemographics(View view){
            try {

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
                long result = dbHelper.editDemographics(age_d, gender, weight_d, height_d, Patient.nationalpassportid);
                if(result > 0){
                    Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
                }else{
                    alertUser("Not saved");
                }
            }catch(NumberFormatException e){
                alertUser("Invalid input");
            }
        }

    public void alertUser(String msg){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Error");
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton("Ok", null);
        alertDialog.show();
    }

}
