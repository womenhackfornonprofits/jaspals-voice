package uk.co.jaspalsvoice.jv.models;

import android.content.ContentValues;

import uk.co.jaspalsvoice.jv.db.DbOpenHelper;

/**
 * Created by Srinivas Kalyani on 11 Jul 16.
 */
public class Diagnosis {

   private String diagnosis;
   private String date;
   private String uuid;
   private int id;

    public void setDiagnosis(String diagnosis){
        this.diagnosis = diagnosis;
    }

    public void setDate (String date){
        this.date = date;
    }

    public String getDiagnosis(){
        return diagnosis;
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
        cv.put(DbOpenHelper.COLUMN_D_DIAGNOSIS, getDiagnosis());
        cv.put(DbOpenHelper.COLUMN_D_DATE, getDate());
        return cv;
    }
}
