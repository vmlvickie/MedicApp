package com.octrinsic.medicalapp;

/**
 * Created by Nicholas on 7/19/2016.
 */
public class CreateDatabase {
    String sqlUsers = "CREATE TABLE IF NOT EXISTS \"users\" (\n" +
            "\t`_id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`firstName`\tTEXT,\n" +
            "\t`lastName`\tTEXT,\n" +
            "\t`phone`\tTEXT,\n" +
            "\t`email`\tTEXT,\n" +
            "\t`password`\tTEXT,\n" +
            "\t`nationalPassportId`\tINTEGER,\n" +
            "\t`userLevel`\tINTEGER\n" +
            ")";
    String sqlUsersInsert = "INSERT INTO `users` (_id,firstName,lastName,phone,email,password,nationalPassportId,userLevel) VALUES (1,'Test','Test','+254725463245','test@yahoo.com','test',30067123,1),\n" +
            " (3,'admin','admin',NULL,'admin@yahoo.com','admin',30067124,0)";

    String sqlSuggestion = "CREATE TABLE IF NOT EXISTS \"time_suggestion_tbl\" (\n" +
            "\t`_id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`doctor_national_id`\tINTEGER,\n" +
            "\t`date`\tTEXT,\n" +
            "\t`time`\tTEXT,\n" +
            "\t`patient_national_id`\tINTEGER\n" +
            ")";

    String sqlSuggestionInsert = "INSERT INTO `time_suggestion_tbl` (_id,doctor_national_id,date,time,patient_national_id) VALUES (3,40098765,'18/5/2016','8:30 AM',30067123),\n" +
            " (4,40098765,'18/5/2016','8:30 AM',30067123)";

    String sqlReferrals = "CREATE TABLE  IF NOT EXISTS \"referrals\" (\n" +
            "\t`_id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`facilityId`\tTEXT,\n" +
            "\t`doctorToNationalId`\tINTEGER,\n" +
            "\t`patientNationalPassportId`\tINTEGER,\n" +
            "\t`dismiss`\tINTEGER DEFAULT 1,\n" +
            "\t`doctorFromNationalId`\tINTEGER,\n" +
            "\t`date`\tTEXT\n" +
            ")";
    String sqlReferralsInsert = "INSERT INTO `referrals` (_id,facilityId,doctorToNationalId," +
            "patientNationalPassportId,dismiss,doctorFromNationalId,date) VALUES (12,'003',40098765,30067123,1,40098765,'29/06/2016')";

    String sqlOtherAppointment = "CREATE TABLE IF NOT EXISTS \"others_appointments\" (\n" +
            "\t`id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`fname`\tTEXT,\n" +
            "\t`lname`\tTEXT,\n" +
            "\t`age`\tINTEGER,\n" +
            "\t`maritalStatus`\tTEXT,\n" +
            "\t`relationship`\tTEXT,\n" +
            "\t`booker_id`\tINTEGER,\n" +
            "\t`gender`\tTEXT,\n" +
            "\t`doctorId`\tINTEGER,\n" +
            "\t`facilityId`\tTEXT,\n" +
            "\t`book_id`\tINTEGER\n" +
            ")";
    String sqlOtherAppointmentInsert = "INSERT INTO `others_appointments` (id,fname,lname," +
            "age,maritalStatus,relationship,booker_id,gender,doctorId,facilityId,book_id)" +
            " VALUES (9,'ATest','Atest',21,'Single','Brother',40098765,'Male',40098765,'003',25)";
    String sqlMedicalSources = "\n" +
            "CREATE TABLE IF NOT EXISTS \"medical_sources\" (\n" +
            "\t`_id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`title`\tTEXT UNIQUE,\n" +
            "\t`link`\tTEXT UNIQUE\n" +
            ")";
    String sqlMedicalSourcesInsert = "INSERT INTO `medical_sources` (_id,title,link) VALUES (1,'Test site 1','www.testsite.co.ke'),\n" +
            " (2,'Test site 2','www.site2.com')";

    String sqlFacilities = "CREATE TABLE IF NOT EXISTS \"facilities\" (\n" +
            "\t`_id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`facilityName`\tTEXT,\n" +
            "\t`location`\tTEXT,\n" +
            "\t`facilityId`\tTEXT\n" +
            ")";
    String sqlFacilitiesInsert = "INSERT INTO `facilities` (_id,facilityName,location,facilityId) VALUES (1,'KNH','Nairobi','001'),\n" +
            " (2,'FacilityTest2','Kisii','003'),\n" +
            " (3,'FacilityTest1','Edoret','002')";

    String sqlDoctorsSchedule = "CREATE TABLE IF NOT EXISTS \"doctors_schedule\" (\n" +
            "\t`faciltyId`\tTEXT,\n" +
            "\t`availableTimeFrom`\tTEXT,\n" +
            "\t`availableTimeTo`\tTEXT,\n" +
            "\t`availableDayFrom`\tTEXT,\n" +
            "\t`availableDayTo`\tTEXT,\n" +
            "\t`nationalPassportId`\tINTEGER,\n" +
            "\t`_id`\tINTEGER PRIMARY KEY AUTOINCREMENT\n" +
            ")";
    String sqlDoctorsScheduleInsert = "INSERT INTO `doctors_schedule` (faciltyId,availableTimeFrom,availableTimeTo,availableDayFrom,availableDayTo,nationalPassportId,_id) VALUES ('001','8:00 am','1:00 pm','Monday','Wednesday',50075172,1),\n" +
            " ('003','10:00 am','2:00 pm','Thursday','Friday',40098765,2)";
    String sqlDoctors = "CREATE TABLE IF NOT EXISTS \"doctors\" (\n" +
            "\t`id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`firstName`\tTEXT,\n" +
            "\t`lastName`\tTEXT,\n" +
            "\t`nationalPassportId`\tINTEGER UNIQUE,\n" +
            "\t`facility`\tTEXT,\n" +
            "\t`phone`\tTEXT,\n" +
            "\t`email`\tTEXT UNIQUE,\n" +
            "\t`password`\tTEXT,\n" +
            "\t`facilityId`\tTEXT,\n" +
            "\t`doctorType`\tTEXT\n" +
            ")";
    String sqlDoctorsInsert = "INSERT INTO `doctors` (id,firstName,lastName,nationalPassportId,facility,phone,email,password,facilityId,doctorType) VALUES (1,'Victor','Lijoodi',30067128,'FacilityTest1','+254708976345','lijoodiv@gmail.com','vickie','002','Surgeon'),\n" +
            " (2,'TestFirstName','TestLastName',40098765,'FacilityTest2','+254723432453','doctor2@yahoo.com','doctor2','003','Dentist')";

    String sqlDemographics = "CREATE TABLE IF NOT EXISTS \"demographics\" (\n" +
            "\t`_id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`age`\tINTEGER,\n" +
            "\t`gender`\tTEXT,\n" +
            "\t`weight`\tREAL,\n" +
            "\t`height`\tREAL,\n" +
            "\t`national_passport_id`\tINTEGER\n" +
            ")";
    String sqlDemographicsInsert = "INSERT INTO `demographics` (_id,age,gender,weight,height," +
            "national_passport_id) VALUES (1,45,'Male',78.4,1.6,30067123);";

    String sqlAppointmentsNotifications = "CREATE TABLE IF NOT EXISTS \"appointments_notifications\" (\n" +
            "\t`_id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`appointmentId`\tINTEGER,\n" +
            "\t`patientNationalPassportId`\tINTEGER\n" +
            ")";

    String sqlAppointments = "CREATE TABLE IF NOT EXISTS \"appointments\" (\n" +
            "\t`_id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`facility`\tTEXT,\n" +
            "\t`doctor`\tTEXT,\n" +
            "\t`dateOfAppointment`\tTEXT,\n" +
            "\t`patientFname`\tTEXT,\n" +
            "\t`patientLname`\tTEXT,\n" +
            "\t`patientNationalId`\tINTEGER,\n" +
            "\t`doctorsId`\tINTEGER,\n" +
            "\t`approved`\tINTEGER,\n" +
            "\t`selfOther`\tTEXT,\n" +
            "\t`facilityId`\tTEXT,\n" +
            "\t`doctorType`\tTEXT\n" +
            ")";
    String sqlAppointmentsInsert = "INSERT INTO `appointments` (_id,facility,doctor,dateOfAppointment,patientFname,patientLname,patientNationalId,doctorsId,approved,selfOther,facilityId,doctorType) VALUES (11,'FacilityTest2','TestFirstName TestLastName','31-5-2016','Victor','Lijoodi',30067123,40098765,2,'self','003','Dentist'),\n" +
            " (25,'FacilityTest2','TestFirstName TestLastName','31-6-2016','Kelvin','Omondi',45673231,40098765,1,'other','003','Dentist')";

    public String getSqlUsers(){
        return sqlUsers;
    }
    public String getSqlUsersInsert(){
        return sqlUsersInsert;
    }
    public String getSqlSuggestion(){
        return sqlSuggestion;
    }

    public String getSqlSuggestionInsert() {
        return sqlSuggestionInsert;
    }



    public String getSqlAppointments() {
        return sqlAppointments;
    }

    public String getSqlAppointmentsInsert() {
        return sqlAppointmentsInsert;
    }

    public String getSqlAppointmentsNotifications() {
        return sqlAppointmentsNotifications;
    }

    public String getSqlDemographics() {
        return sqlDemographics;
    }

    public String getSqlDemographicsInsert() {
        return sqlDemographicsInsert;
    }

    public String getSqlDoctors() {
        return sqlDoctors;
    }

    public String getSqlDoctorsInsert() {
        return sqlDoctorsInsert;
    }

    public String getSqlDoctorsSchedule() {
        return sqlDoctorsSchedule;
    }

    public String getSqlDoctorsScheduleInsert() {
        return sqlDoctorsScheduleInsert;
    }

    public String getSqlFacilities() {
        return sqlFacilities;
    }

    public String getSqlFacilitiesInsert() {
        return sqlFacilitiesInsert;
    }

    public String getSqlMedicalSources() {
        return sqlMedicalSources;
    }

    public String getSqlMedicalSourcesInsert() {
        return sqlMedicalSourcesInsert;
    }

    public String getSqlOtherAppointment() {
        return sqlOtherAppointment;
    }

    public String getSqlOtherAppointmentInsert() {
        return sqlOtherAppointmentInsert;
    }

    public String getSqlReferrals() {
        return sqlReferrals;
    }

    public String getSqlReferralsInsert() {
        return sqlReferralsInsert;
    }
}

