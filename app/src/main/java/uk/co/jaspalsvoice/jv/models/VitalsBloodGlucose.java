package uk.co.jaspalsvoice.jv.models;

import android.content.ContentValues;

import uk.co.jaspalsvoice.jv.db.DbOpenHelper;

/**
 * Created by Srinivas Kalyani on 11 Jul 16.
 */
public class VitalsBloodGlucose {

    public String bloodGlucose;
    public String date;
    public String uuid;
    public int id;

    public void setBloodGlucose(String bloodGlucose){
        this.bloodGlucose = bloodGlucose;
    }

    public void setDate (String date){
        this.date = date;
    }

    public String getBloodGlucose(){
        return bloodGlucose;
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
       /* cv.put(DbOpenHelper.COLUMN_B_UUID, getUuid());
        cv.put(DbOpenHelper.COLUMN_B_ID, getId());*/
        cv.put(DbOpenHelper.COLUMN_BG_BLOODGLUCOSE, getBloodGlucose());
        cv.put(DbOpenHelper.COLUMN_BG_DATE, getDate());
        return cv;
    }
}
