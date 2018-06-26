package com.octrinsic.medicalapp;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DoctorAppointmentsAdapter extends CursorAdapter{

    private LayoutInflater cursorInflater;

    public DoctorAppointmentsAdapter(Context context, Cursor c) {
        super(context, c, false);
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return cursorInflater.inflate(R.layout.appointments_doctor, parent, false);

    }



    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Find fields to populate in inflated template
        TextView tvName = (TextView) view.findViewById(R.id.tvDoctorName);
        TextView tvDate = (TextView) view.findViewById(R.id.tvDoctorDate);
        TextView tvFacility = (TextView) view.findViewById(R.id.tvDoctorFacility);
        ImageView ivStatus = (ImageView) view.findViewById(R.id.ivStatus);
        TextView tvStatusText = (TextView) view.findViewById(R.id.tvStatusText);

        int approved = getApprovedStatus(cursor);

        switch (approved){
            case 1:
                //view.setBackgroundColor(Color.GREEN);
                ivStatus.setImageResource(R.drawable.approved);
                tvStatusText.setText("Approved");
                break;
            case 2:
                //view.setBackgroundColor(Color.RED);
                ivStatus.setImageResource(R.drawable.rejected);
                tvStatusText.setText("Canceled");
                break;
            default:
                //view.setBackgroundColor(Color.YELLOW);
                ivStatus.setImageResource(R.drawable.waiting);
                tvStatusText.setText("Pending");
        }



        //doctorsid
        String fname = cursor.getString(cursor.getColumnIndexOrThrow("patientFname"));
        String lname = cursor.getString(cursor.getColumnIndexOrThrow("patientLname"));
        String date = cursor.getString(cursor.getColumnIndexOrThrow("dateOfAppointment"));
        String facility = cursor.getString(cursor.getColumnIndexOrThrow("facility"));
        // Populate fields with extracted properties
        String conct = fname + "\t" + lname;
        tvName.setText(conct);
        tvDate.setText(date);
        tvFacility.setText(facility);
    }

    private int getApprovedStatus(Cursor cursor) {
        return cursor.getInt(cursor.getColumnIndex("approved"));
    }

}

