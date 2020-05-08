package uk.co.jaspalsvoice.jv.models;

import android.content.ContentValues;

import uk.co.jaspalsvoice.jv.db.DbOpenHelper;

/**
 * Created by Srinivas Kalyani on 11 Jul 16.
 */
public class VitalsBloodPressure {

    public String bloodPressure;
    public String date;
    public String uuid;
    public int id;

    public void setBloodPressure(String bloodPressure){
        this.bloodPressure = bloodPressure;
    }

    public void setDate (String date){
        this.date = date;
    }

    public String getBloodPressure(){
        return bloodPressure;
    }

    public String getDate(){
        return date;
    }

    public void setUuid(String uuid){
        this.uuid= uuid;
    }

    public String getUuid(){
        return uuid;
    }

    public void setId(int id){
        this.id= id;
    }

    public int getId(){
        return id;
    }

    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(DbOpenHelper.COLUMN_B_BLOODPRESSURE, getBloodPressure());
        cv.put(DbOpenHelper.COLUMN_B_DATE, getDate());
        return cv;
    }
}
