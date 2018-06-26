package com.octrinsic.medicalapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Nicholas on 6/27/2016.
 */
public class ReferralsAdapter extends CursorAdapter{
    LayoutInflater inflater;
    DatabaseHelper dbHelper;
    public ReferralsAdapter(Context context, Cursor c) {
        super(context, c, false);
        dbHelper = new DatabaseHelper(context);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.patient_view_referrals, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvFacility = (TextView) view.findViewById(R.id.tvFacility);
        TextView tvDoctor = (TextView) view.findViewById(R.id.tvDoctor);
        TextView tvDate = (TextView) view.findViewById(R.id.tvDate);

        String  facility = getFacility(cursor);
        String doctorTo = getDoctorTo(cursor);

        tvFacility.setText(facility);
        tvDoctor.setText(doctorTo);
        tvDate.setText(cursor.getString(cursor.getColumnIndex("date")));

    }

    public String getFacility(Cursor cursor){
        Cursor  facilityCursor = dbHelper.getFacilityName(cursor.getString(1));
        if(facilityCursor != null){
            facilityCursor.moveToFirst();
            return facilityCursor.getString(0).toString();
        }
        return "";
    }

    public String getDoctorTo(Cursor cursor){
        Cursor doctorTo = dbHelper.getDoctorName(cursor.getInt(2));
        if(doctorTo != null){
            doctorTo.moveToFirst();
            return doctorTo.getString(1).toString();
        }
        return "";
    }
}
