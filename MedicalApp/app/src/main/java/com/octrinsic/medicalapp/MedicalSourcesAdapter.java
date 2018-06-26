package com.octrinsic.medicalapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Nicholas on 6/20/2016.
 */
public class MedicalSourcesAdapter extends CursorAdapter {

    private LayoutInflater cInflater;
    public MedicalSourcesAdapter(Context context, Cursor c) {
        super(context, c, false);
        cInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return cInflater.inflate(R.layout.medical_sources, parent, false);
    }
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String title = cursor.getString(cursor.getColumnIndex("title"));
        String link = cursor.getString(cursor.getColumnIndex("link"));
        TextView TvTitle = (TextView)view.findViewById(R.id.TvTitle);
        TextView TvLink = (TextView)view.findViewById(R.id.TvLink);
        TvTitle.setText(title);
        TvLink.setText(link);

    }
}
