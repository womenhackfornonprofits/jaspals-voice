package uk.co.jaspalsvoice.jv.models;

import android.content.ContentValues;

import uk.co.jaspalsvoice.jv.db.DbOpenHelper;

/**
 * Created by Srinivas Kalyani on 11 Jul 16.
 */
public class VitalsBloodPressure {

    protected String bloodPressure;
    protected String date;
    protected String uuid;
    protected String id;

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

    public void setId(String id){
        this.id= id;
    }

    public String getId(){
        return id;
    }

    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(DbOpenHelper.COLUMN_B_UUID, getUuid());
        cv.put(DbOpenHelper.COLUMN_B_ID, getId());
        cv.put(DbOpenHelper.COLUMN_B_BLOODPRESSURE, getBloodPressure());
        cv.put(DbOpenHelper.COLUMN_B_DATE, getDate());
        return cv;
    }
}
