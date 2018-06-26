package com.octrinsic.medicalapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.TextView;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 14;
    String TABLE_NAME = "users";
    String ID = "_id";
    String FIRST_NAME = "firstName";
    String LAST_NAME = "lastName";
    String PHONE = "phone";
    String EMAIL = "email";
    String PASSWORD = "password";
    String NATIONAL_PASSPORT_ID = "nationalPassportId";
    CreateDatabase dbCreator;

    public DatabaseHelper(Context context) {
        super(context, "mims.db", null, DATABASE_VERSION);
        dbCreator = new CreateDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createDatabaseTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }
    public void createDatabaseTables(SQLiteDatabase db){
        String sqlUsers = dbCreator.sqlUsers;
        db.execSQL(sqlUsers);
        String sqlUsersInsert = dbCreator.getSqlUsersInsert();
        db.execSQL(sqlUsersInsert);
        String sqlSuggestion = dbCreator.getSqlSuggestion();
        db.execSQL(sqlSuggestion);
        String sqlSuggestionInsert = dbCreator.getSqlSuggestionInsert();
        db.execSQL(sqlSuggestionInsert);
        String sqlReferrals = dbCreator.getSqlReferrals();
        db.execSQL(sqlReferrals);
        String sqlReferralsInsert = dbCreator.getSqlReferralsInsert();
        db.execSQL(sqlReferralsInsert);
        String sqlOtherAppointment = dbCreator.getSqlOtherAppointment();
        db.execSQL(sqlOtherAppointment);
        String sqlOtherAppointmentInsert = dbCreator.getSqlOtherAppointmentInsert();
        db.execSQL(sqlOtherAppointmentInsert);
        String sqlMedicalSources = dbCreator.getSqlMedicalSources();
        db.execSQL(sqlMedicalSources);
        String sqlMedicalSourcesInsert = dbCreator.getSqlMedicalSourcesInsert();
        db.execSQL(sqlMedicalSourcesInsert);
        String sqlFacilities = dbCreator.getSqlFacilities();
        db.execSQL(sqlFacilities);
        String sqlFacilitiesInsert = dbCreator.getSqlFacilitiesInsert();
        db.execSQL(sqlFacilitiesInsert);
        String sqlDoctorsSchedule = dbCreator.getSqlDoctorsSchedule();
        db.execSQL(sqlDoctorsSchedule);
        String sqlDoctorsScheduleInsert = dbCreator.getSqlDoctorsScheduleInsert();
        db.execSQL(sqlDoctorsScheduleInsert);
        String sqlDoctors = dbCreator.getSqlDoctors();
        db.execSQL(sqlDoctors);
        String sqlDoctorsInsert = dbCreator.getSqlDoctorsInsert();
        db.execSQL(sqlDoctorsInsert);
        String sqlDemographics = dbCreator.getSqlDemographics();
        db.execSQL(sqlDemographics);
        String sqlDemographicsInsert = dbCreator.getSqlDemographicsInsert();
        db.execSQL(sqlDemographicsInsert);
        String sqlAppointmentsNotifications = dbCreator.getSqlAppointmentsNotifications();
        db.execSQL(sqlAppointmentsNotifications);
        String sqlAppointments = dbCreator.getSqlAppointments();
        db.execSQL(sqlAppointments);
        String sqlAppointmentsInsert = dbCreator.getSqlAppointmentsInsert();
        db.execSQL(sqlAppointmentsInsert);
    }

    //register patient
    public long registerUser(String firstNname, String lastName, String phoneNo, String emailAddr, int idNo, String confirmPass) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues userData = new ContentValues();
        userData.put(FIRST_NAME, firstNname);
        userData.put(LAST_NAME, lastName);
        userData.put(PHONE, phoneNo);
        userData.put(EMAIL, emailAddr);
        userData.put(PASSWORD, confirmPass);
        userData.put(NATIONAL_PASSPORT_ID, idNo);
        userData.put("userLevel", 1);

        return database.insert(TABLE_NAME, null, userData);


    }

    public Cursor validateUser(String email, String password) {
        String query = "SELECT " + NATIONAL_PASSPORT_ID + ", userLevel FROM " + TABLE_NAME + " WHERE "
                + EMAIL + " = '" + email + "' AND " + PASSWORD + " = '" + password + "'";

        SQLiteDatabase db = getReadableDatabase();
        Cursor rCursor = db.rawQuery(query, null);
        return rCursor;
    }

    public Cursor validateDoctor(String email, String password, String facilityId) {
        String query = "SELECT nationalPassportId FROM doctors WHERE email = '" + email + "'" +
                " AND password = '" + password + "' AND facilityId = '" + facilityId + "'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor resultCursor = db.rawQuery(query, null);
        return resultCursor;
    }


    public boolean userExists(int idNummber) {
        String sql = "SELECT " + ID + " FROM " + TABLE_NAME + " WHERE " + NATIONAL_PASSPORT_ID + " = '" + idNummber + "'";
        Cursor cursor = getReadableDatabase().rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            return true;
        }
        return false;
    }

    public boolean emailUserExists(String email) {
        String sql = "SELECT " + ID + " FROM " + TABLE_NAME + " WHERE " + EMAIL + " = '" + email + "'";
        Cursor cursor = getReadableDatabase().rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            return true;
        }
        return false;
    }


    //get facilities
    public Cursor getFacilities() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT facilityName FROM facilities";
        return db.rawQuery(sql, null);
    }

    public Cursor getDoctors(String facilityName) {
        String sql = "SELECT firstName, lastName FROM doctors WHERE facility = '" + facilityName + "'";

        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(sql, null);
    }

    public Cursor getTimeAvailability(int id) {
        SQLiteDatabase db = getReadableDatabase();
        int nationalPassportId = id;
        String availSql = "SELECT availableDayFrom, availableDayTo, availableTimeFrom," +
                " availableTimeTo FROM doctors_schedule WHERE nationalPassportId = '" + nationalPassportId + "'";
        return db.rawQuery(availSql, null);
    }

    public long bookAppointment(String facility, String doctor,
                                String dateOfAppointment, String patientFname,
                                String patientLname, int patientNationalPassportId, int doctorsId, String patientType, String speciality) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        String facilityId = getFacilityId(facility);//get the facility ID

        values.put("facility", facility);
        values.put("doctor", doctor);
        values.put("dateOfAppointment", dateOfAppointment);
        values.put("patientFname", patientFname);
        values.put("patientLname", patientLname);
        values.put("patientNationalId", patientNationalPassportId);
        values.put("doctorsId", doctorsId);
        values.put("approved", 0);
        values.put("selfOther", patientType);
        values.put("facilityId", facilityId);
        values.put("doctorType", speciality);
        return db.insert("appointments", null, values);
    }

    public long ReferAppointment(String facility, String doctor,
                                 TextView availableDay,
                                 TextView available, int patientNationalPassportId, int doctorsId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("facility", facility);
        values.put("doctor", doctor);
        //values.put("day Available", DateAvailable);
        values.put("availableDay", String.valueOf(availableDay));
        values.put("available", String.valueOf(available));
        values.put("patientNationalId", patientNationalPassportId);
        values.put("doctorsId", doctorsId);
        values.put("Refer ed", 0);

        return db.insert("appointments", null, values);
    }

    public int getDoctorsNationalId(String fname, String lname) {
        String sql = "SELECT nationalPassportId FROM doctors WHERE firstName = '" + fname + "' AND lastName = '" + lname + "'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            return c.getInt(0);
        }
        return 0;
    }

    public Cursor getPatientsName(int idNumber) {
        String sql = "SELECT firstName, lastName FROM users WHERE nationalPassportId = '" + idNumber + "'";
        SQLiteDatabase db = getWritableDatabase();
        return db.rawQuery(sql, null);
    }

    public Cursor getPatientAppointments(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM appointments WHERE patientNationalId = '" + id + "'";
        return db.rawQuery(sql, null);
    }

    public Cursor getDoctorAppointments(int nationalPassportId, String facilityId) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM appointments WHERE doctorsId = '" + nationalPassportId + "' AND facilityId = '" + facilityId + "' AND approved = '" + 0 + "'";
        return db.rawQuery(sql, null);
    }

    public Cursor getDoctorWaitingAppointments(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM appointments WHERE doctorsId = '" + id + "' AND approved = 0 ";
        return db.rawQuery(sql, null);
    }

    public Cursor getApprovedAppointments(int id, String facilityId) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM appointments WHERE doctorsId = '" + id + "' AND approved = 1 AND facilityId = '" + facilityId + "'";
        return db.rawQuery(sql, null);
    }

    public long bookAppointmentForOther(String firstnameTvstr, String lastName, String relation,
                                        String gender, int age, String madritalStatus, int patientNationalPassportId, int book_id, int doctorId, String facilityId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("fname", firstnameTvstr);
        values.put("lname", lastName);
        values.put("age", age);
        values.put("maritalStatus", madritalStatus);
        values.put("relationship", relation);
        values.put("booker_id", patientNationalPassportId);
        values.put("gender", gender);
        values.put("doctorId", doctorId);
        values.put("facilityId", facilityId);
        values.put("book_id", book_id);
        return db.insert("others_appointments", null, values);
    }

    public void setAppointmentStatus(int id, String status) {
        SQLiteDatabase db = getWritableDatabase();
        if (status.equals("approved")) {
            //set approve status
            String sql = "UPDATE appointments SET approved = 1 WHERE _id = '" + id + "'";
            db.execSQL(sql);

        } else if (status.equals("cancel")) {
            //set reject status
            String sql = "UPDATE appointments SET approved = 2 WHERE _id = '" + id + "'";
            db.execSQL(sql);
        }
    }

    public int getPatientID(int appointmentId) {
        String sql = "SELECT nationalPassportId FROM users where _id = '" + appointmentId + "' AND userLevel = 1";
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            return c.getInt(c.getColumnIndex("nationalPassportId"));
        }
        return 0;
    }
    //
    public Cursor getNotificationInfo(int appointmentId, int patientNationalId){
        String sql = "SELECT facility, doctor, dateOfAppointment, approved FROM appointments WHERE _id = '" + appointmentId + "' AND  patientNationalId = '" + patientNationalId + "'";
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(sql, null);
        if (c!= null){
            return c;
        }
        return null;
    }


//
    public long referPatient(String facility, int doctorToNationalId, int patientNationalPassportId, int doctorFromNationalId, String date) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("facilityId", getFacilityId(facility));
        values.put("doctorToNationalId", doctorToNationalId);
        values.put("patientNationalPassportId", patientNationalPassportId);
        values.put("dismiss", 1);
        values.put("doctorFromNationalId", doctorFromNationalId);
        values.put("date", date);
        return db.insert("referrals", null, values);
    }

    public long suggestTime(int doctor_national_id, String time, String date, int patient_national_id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("doctor_national_id", doctor_national_id);
        values.put("date", date);
        values.put("time", time);
        values.put("patient_national_id", patient_national_id);
        return db.insert("time_suggestion_tbl", null, values);
    }

    public String getFacilityId(String facilityName) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT facilityId FROM facilities WHERE facilityName = '" + facilityName + "'";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
            return cursor.getString(cursor.getColumnIndex("facilityId"));
        }
        return "";
    }

    public Cursor getFacilityName(String facilityId) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT facilityName FROM facilities WHERE facilityId = '" + facilityId + "'";
        return db.rawQuery(sql, null);

    }

    public Cursor getOtherAppointments(int id, int booker_id, String facilityId) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT fname, lname FROM others_appointments WHERE book_id = '" + id + "'" +
                " AND booker_id = '" + booker_id + "' AND facilityId = '" + facilityId + "'";
        return db.rawQuery(sql, null);
    }

    public int getBookId() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT _id FROM appointments";
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToLast()) {
            return c.getInt(c.getColumnIndex("_id"));
        }
        return 0;
    }

    public Cursor getMedicalInfoSources() {
        String sql = "SELECT * FROM medical_sources";
        SQLiteDatabase db = getWritableDatabase();
        return db.rawQuery(sql, null);
    }

    //capture demographics
    public long enterDemographics(int age_d, String gender, double weight_d, double height_d, int national_passport_id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("age", age_d);
        values.put("gender", gender);
        values.put("weight", weight_d);
        values.put("height", height_d);
        values.put("national_passport_id", national_passport_id);
        return db.insert("demographics", null, values);
    }

    public Cursor getDemographics(int nationalpassportid) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM demographics WHERE national_passport_id = '" + nationalpassportid + "'";
        return db.rawQuery(sql, null);
    }

    public long editDemographics(int age_d, String gender, double weight_d, double height_d, int nationalpassportid) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("age", age_d);
        values.put("gender", gender);
        values.put("weight", weight_d);
        values.put("height", height_d);
        return db.update("demographics", values, "national_passport_id = " + nationalpassportid, null);
    }

    public boolean checkPassword(String pass, int nationalpassportid) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT _id FROM users WHERE password = '" + pass + "' " +
                "AND nationalPassportId = '" + nationalpassportid + "'";
        Cursor c = db.rawQuery(sql, null);
        if (c.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public long editProfile(String firstNname, String lastName, String phoneNo, String emailAddr, int nationalpassportid, String newPass) {
        ContentValues values = new ContentValues();
        values.put("firstName", firstNname);
        values.put("lastName", lastName);
        values.put("phone", phoneNo);
        values.put("email", emailAddr);
        values.put("password", newPass);
        SQLiteDatabase db = getWritableDatabase();
        return db.update("users", values, "nationalPassportId = " + nationalpassportid, null);
    }

    public Cursor getUser(int nationalpassportid) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT firstName, lastName, phone, email, password FROM users WHERE" +
                "  nationalPassportId = '" + nationalpassportid + "'";
        return db.rawQuery(sql, null);
    }

    public Cursor getReferrals(int nationalPassportId) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM referrals WHERE doctorToNationalId = '" + nationalPassportId + "' AND dismiss = 1";
        return db.rawQuery(sql, null);
    }

    public Cursor getPatientReferrals(int nationalPassportId) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM referrals WHERE patientNationalPassportId = '" + nationalPassportId + "' AND dismiss = 1";
        return db.rawQuery(sql, null);
    }

    //:come back
    public Cursor getDoctorName(int doctorToNationalId) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT firstName, lastName FROM doctors WHERE nationalPassportId = '" + doctorToNationalId + "'";
        return db.rawQuery(sql, null);
    }

    public Cursor getDoctorType(String facilityName) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT DISTINCT doctorType FROM doctors WHERE facility = '" + facilityName + "'";
        return db.rawQuery(sql, null);
    }

    public Cursor getDoctorsSpectiality(String facility, String speciality) {
        String sql = "SELECT firstName, lastName FROM doctors WHERE facility = '" + facility + "'" +
                " AND doctorType = '" + speciality + "'";

        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(sql, null);
    }

    public long cancelAppointment(int appointmentId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("approved", 2);
         return db.update("appointments", cv, "_id = " + appointmentId, null);
    }

    public Cursor getDoctorEditDetails(int id){
        String sql = "SELECT firstName, lastName, phone, email FROM doctors WHERE nationalPassportId = '" + id + "'";
        SQLiteDatabase db = getWritableDatabase();
        return db.rawQuery(sql, null);
    }

    public boolean checkDoctorPassword(String pass, int nationalPassportId) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT id FROM doctors WHERE password = '" + pass + "' " +
                "AND nationalPassportId = '" + nationalPassportId + "'";
        Cursor c = db.rawQuery(sql, null);
        if (c.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public long editDoctorDetails(String firstName, String lastName, String phone, String email, String password, int nationalPassportId){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("firstName", firstName);
        cv.put("lastName", lastName);
        cv.put("phone", phone);
        cv.put("email", email);
        cv.put("password", password);
        return db.update("doctors", cv, "nationalPassportId = " + nationalPassportId, null);
    }

    public int getAppointmentNotificationId(int id){
        Cursor c = null;
        String query = "SELECT appointmentId FROM appointments_notifications WHERE patientNationalPassportId = '"+ id +"'";
        SQLiteDatabase db = getWritableDatabase();
         c =  db.rawQuery(query, null);
        if(c != null && c.getCount() > 0){
            c.moveToNext();
            return c.getInt(c.getColumnIndex("appointmentId"));
        }
        return 0;
    }

    public int getNotificationCount(int nationalpassportid) {
        String query = "SELECT _id FROM appointments_notifications WHERE patientNationalPassportId = '"+ nationalpassportid +"'";
        SQLiteDatabase db = getWritableDatabase();
        return db.rawQuery(query, null).getCount();
    }
    public boolean issueNotification(int appointmentId, int patientNationalPassportId){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("appointmentId", appointmentId);
        cv.put("patientNationalPassportId", patientNationalPassportId);
        if(db.insert("appointments_notifications", null, cv) > 0){
            return true;
        }
        return false;
    }

    public int dismissAppointment(int appointmentId) {
        String query = "DELETE FROM  appointments_notifications WHERE appointmentId = '"+ appointmentId +"'";
        SQLiteDatabase db = getWritableDatabase();
        return db.rawQuery(query, null).getCount();
    }

    public boolean checkForNotifications(int nationalpassportid) {
        String query = "SELECT _id FROM appointments_notifications WHERE patientNationalPassportId = '"+ nationalpassportid +"'";
        SQLiteDatabase db = getWritableDatabase();
        if(db.rawQuery(query, null).getCount() > 0){
            return true;
        }
        return false;
    }

    public boolean chechIfDataExists(int nationalpassportid) {
        String sql = "SELECT _id FROM demographics WHERE national_passport_id = '"+ nationalpassportid +"'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        if(c.getCount() > 0){
            return true;
        }
        return false;
    }
}
