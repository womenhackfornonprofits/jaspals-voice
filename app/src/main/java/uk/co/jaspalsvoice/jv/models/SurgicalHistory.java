package uk.co.jaspalsvoice.jv.models;

import android.content.ContentValues;

import uk.co.jaspalsvoice.jv.db.DbOpenHelper;

/**
 * Created by Srinivas Kalyani on 11 Jul 16.
 */
public class SurgicalHistory {

   private String surgicalHistory;
   private String date;
   private String uuid;
   private int id;

    public void setSurgicalHistory(String surgicalHistory){
        this.surgicalHistory = surgicalHistory;
    }

    public void setDate (String date){
        this.date = date;
    }

    public String getSurgicalHistory(){
        return surgicalHistory;
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
        cv.put(DbOpenHelper.COLUMN_SH_HISTORY, getSurgicalHistory());
        cv.put(DbOpenHelper.COLUMN_SH_DATE, getDate());
        return cv;
    }
}
