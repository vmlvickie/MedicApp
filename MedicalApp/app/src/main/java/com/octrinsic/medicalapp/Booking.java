package com.octrinsic.medicalapp;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Booking extends AppCompatActivity  implements AdapterView.OnItemSelectedListener, RadioButton.OnCheckedChangeListener {

    Spinner facilities = null;
    Spinner spDoctorsName;
    Button btnDate;
    EditText etDateOfAppointment;
    EditText etFname;
    EditText etLname;
    EditText etNationalPassportNumber;
    Button btnSubmit;
    DatabaseHelper dbHelper;
    TextView tvTimes;

    //other widgets
    LinearLayout other;
    RadioButton selfBtn;
    RadioButton otherBtn;
    EditText firstnameTv;
    EditText lastnameTv;
    Spinner spinnerRealtionship;
    EditText TvAge;
    EditText TvMarital;
    RadioButton maleBtn;
    RadioButton femaleBtn;

    Spinner spDoctorType;


    static int patientNationalPassportId = 0;

    private int mYear,mMonth,mDay;
    String msg = "";
    private int bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle bundle = getIntent().getExtras();
        int id = bundle.getInt("nationalpassportid");
        patientNationalPassportId = id;  
        dbHelper = new DatabaseHelper(this);



        //initialize widgets - user
        facilities = (Spinner) findViewById(R.id.spFacilities);
        facilities.setOnItemSelectedListener(this);
        tvTimes = (TextView) findViewById(R.id.tvTimes);
        spDoctorsName = (Spinner) findViewById(R.id.spDoctorsName);

        spDoctorType = (Spinner)findViewById(R.id.spDoctorType);
        assert spDoctorType != null;
        spDoctorType.setOnItemSelectedListener(this);

        spDoctorsName.setOnItemSelectedListener(this);
        btnDate = (Button) findViewById(R.id.btnDate);
        etDateOfAppointment = (EditText) findViewById(R.id.etDateOfAppointment);
        etFname = (EditText) findViewById(R.id.etFname);
        etLname = (EditText) findViewById(R.id.etLname);
        btnSubmit = (Button) findViewById(R.id.btn_submit);

        //initialize widgets other
        other = (LinearLayout) findViewById(R.id.other);
        other.setVisibility(other.INVISIBLE);
        selfBtn = (RadioButton) findViewById(R.id.selfBtn);
        selfBtn.setOnCheckedChangeListener(this);
        otherBtn = (RadioButton) findViewById(R.id.otherBtn);
        otherBtn.setOnCheckedChangeListener(this);
        firstnameTv = (EditText) findViewById(R.id.firstnameTv);
        lastnameTv = (EditText) findViewById(R.id.lastnameTv);
        spinnerRealtionship = (Spinner) findViewById(R.id.spinnerRealtionship);
        TvAge = (EditText) findViewById(R.id.TvAge);
        TvMarital = (EditText) findViewById(R.id.TvMarital);
        maleBtn = (RadioButton) findViewById(R.id.maleBtn);
        femaleBtn = (RadioButton) findViewById(R.id.femaleBtn);

        hideOtherWidgets();
        populatePatientsName(patientNationalPassportId);
        instantiateFacilities();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.spFacilities) {
            String item = parent.getItemAtPosition(position).toString();
            Cursor cursorDoctorType = dbHelper.getDoctorType(item);
                populateDoctorType(cursorDoctorType);

        } else if (parent.getId() == R.id.spDoctorType) {
            String speciality = spDoctorType.getSelectedItem().toString();
            String facility = facilities.getSelectedItem().toString();
            Cursor doctorsCursor = dbHelper.getDoctorsSpectiality(facility, speciality);
            populateDoctors(doctorsCursor);

        } else if (parent.getId() == R.id.spDoctorsName) {
            Cursor availabilityCusrsor = dbHelper.getTimeAvailability(getDoctorsId());
            populateAvailability(availabilityCusrsor);
        }


    }

    private void populateDoctorType(Cursor cursorDcotorType) {
        ArrayList<String> doctorTypeArray = new ArrayList<>();
        while (cursorDcotorType.moveToNext()){
            doctorTypeArray.add(cursorDcotorType.getString(0));
        }
        ArrayAdapter doctorType = new ArrayAdapter(this, android.R.layout.simple_spinner_item, doctorTypeArray);
        doctorType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDoctorType.setAdapter(doctorType);
    }

    private void populateAvailability(Cursor availabilityCusrsor) {
        if(availabilityCusrsor != null){
            if(availabilityCusrsor.moveToFirst()) {
                String availableDayFrom = availabilityCusrsor.getString(0);
                String availableDayTo = availabilityCusrsor.getString(1);
                String availableTimeFrom = availabilityCusrsor.getString(2);
                String availableTimeTo = availabilityCusrsor.getString(3);
                String concat = "From: " + availableDayFrom + " To " + availableDayTo + "\n\t From" + availableTimeFrom + " To: " + availableTimeTo + "\n";
                tvTimes.setText(concat);
            }
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
    private void populateDoctors(Cursor doctorsCursor) {
        ArrayList<String> doctorsArray = new ArrayList<>();
        while (doctorsCursor.moveToNext()) {
            doctorsArray.add(doctorsCursor.getString(0) + " " + doctorsCursor.getString(1));
        }
        ArrayAdapter doctorsNames = new ArrayAdapter(this, android.R.layout.simple_spinner_item, doctorsArray);
        doctorsNames.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDoctorsName.setAdapter(doctorsNames);
    }

//Check out for this build version
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onChooseDate(View v) {
        final Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        //launch date picker dialog
        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        etDateOfAppointment.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }

                }, mYear, mMonth, mDay);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            dpd.getDatePicker().setMinDate(new Date().getTime());
        }
        dpd.show();
    }
    //Book appointment
    public void goSubmit(View v) {
        try {
            String facility = facilities.getSelectedItem().toString();
            if (facility.equals(null)) {
                msg = "Please select facility";
                alertUser(msg);
                return;
            }
            if(spDoctorType.getSelectedItem() == null){
                alertUser("Select doctor type");
                return;
            }
            String speciality = spDoctorType.getSelectedItem().toString();
            if (speciality.equals(null)){
                msg = "Please select speciality";
                alertUser(msg);
                return;
            }

            if (spDoctorsName.getSelectedItem() == null) {
                alertUser("Select doctor");
                return;
            }
            String doctor = spDoctorsName.getSelectedItem().toString();
            if (doctor.equals(null)) {
                msg = "Please select doctor";
                alertUser(msg);
                return;
            }
            String dateOfAppointment = etDateOfAppointment.getText().toString();
            if (dateOfAppointment.equals("")) {
                msg = "Choose date of appointment";
                alertUser(msg);
                return;
            }
            String patientFname = etFname.getText().toString();
            if (patientFname.equals("")) {
                msg = "Please your first name";
                alertUser(msg);
                return;
            }
            String patientLname = etLname.getText().toString();
            if (patientLname.equals("")) {
                msg = "Please your last name";
                alertUser(msg);
                return;
            }
            if (getDoctorsId() == 0) {
                alertUser("Please select doctor");
                return;
            }
            String patientType = "";
            if (otherBtn.isChecked()) {
                patientType = getPatientType();
            } else if (selfBtn.isChecked()) {
                patientType = getPatientType();
            }

            if (otherBtn.isChecked()) {
                String firstnameTvstr = firstnameTv.getText().toString();
                if ("".equals(firstnameTvstr)) {
                    alertUser("Enter patient first name");
                    return;
                }
                String lastnameTvstr = lastnameTv.getText().toString();
                if ("".equals(lastnameTvstr)) {
                    alertUser("Enter patient last name");
                    return;
                }
                String spinnerRealtionshipstr = spinnerRealtionship.getSelectedItem().toString();
                String TvAgestr = TvAge.getText().toString();
                if ("".equals(TvAgestr)) {
                    alertUser("Enter age");
                    return;
                }
                int age = new Integer(TvAgestr);
                String TvMaritalstr = TvMarital.getText().toString();
                if ("".equals(TvMaritalstr)) {
                    alertUser("Enter marital status");
                    return;
                }
                String doctorName[] = doctor.split(" ");
                int doctorId = dbHelper.getDoctorsNationalId(doctorName[0], doctorName[1]);
                String facilityId = dbHelper.getFacilityId(facility);
                int book_id = dbHelper.getBookId();
                String gender = getGender();

                long r = dbHelper.bookAppointmentForOther(firstnameTvstr, lastnameTvstr,
                        spinnerRealtionshipstr, gender, age, TvMaritalstr, patientNationalPassportId, book_id, doctorId, facilityId);
                if (r <= 0) {
                    alertUser("Couldn't book appointment");
                    return;
                }
            }
            //book appointment
            long result = dbHelper.bookAppointment(facility, doctor, dateOfAppointment,
                    patientFname, patientLname, patientNationalPassportId, getDoctorsId(), patientType, speciality);
            if (result > 0) {
                msg = "Appointment booked successful";
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, Patient.class);
                startActivity(i);
                finish();
            }
        } catch (NumberFormatException e) {
            alertUser("Enter a valid passport or National ID");
        }
    }

    public void instantiateFacilities(){
        Cursor facilitiesCursor = dbHelper.getFacilities();
        ArrayList<String> facilitiesArray = new ArrayList<>();
        while(facilitiesCursor.moveToNext()){
            facilitiesArray.add(facilitiesCursor.getString(0));
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, facilitiesArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        facilities.setAdapter(adapter);
    }

    public void alertUser(String msg){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Appointments");
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton("OK", null);
        alertDialog.setCancelable(true);
        alertDialog.show();
    }
    public int getDoctorsId(){
        if(spDoctorsName.getSelectedItem() != null){
        String name = spDoctorsName.getSelectedItem().toString();
        String names[] = name.split(" ");
        int id = dbHelper.getDoctorsNationalId(names[0], names[1]);
        return id;
        }
        return 0;
    }

    public void populatePatientsName(int idNumber){
        Cursor patientCursor = dbHelper.getPatientsName(idNumber);
        if(patientCursor != null){
            patientCursor.moveToFirst();
            String fname = patientCursor.getString(0).toString();
            String lname = patientCursor.getString(1).toString();
            etFname.setText(fname);
            etLname.setText(lname);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView == selfBtn && isChecked){
            hideOtherWidgets();
        }else if(buttonView == otherBtn && isChecked){
            initWidgets();
            showOtherWidgets();
        }
    }

    private void initWidgets() {
        ArrayList<String> relation = new ArrayList<>();
        String[] relt = {"Child", "Mother", "Father", "Spouse", "Brother", "Sister", "Grand Father", "Grand Mother"};
        for(int i = 0; i< relt.length; i++){
            relation.add(relt[i]);
        }
        ArrayAdapter realtionAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, relation);
        realtionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRealtionship.setAdapter(realtionAdapter);
    }

    public void hideOtherWidgets(){
        other.setVisibility(other.GONE);
    }
    public void showOtherWidgets(){
        other.setVisibility(View.VISIBLE);
    }

    public String getGender(){
        if (maleBtn.isChecked()){
            return "Male";
        }else if(femaleBtn.isChecked()){
            return "Female";
        }
        return "";
    }
    public String getPatientType(){
        if(selfBtn.isChecked()){
            return "Self";
        }else if(otherBtn.isChecked()){
            return "Other";
        }
        return "";
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
