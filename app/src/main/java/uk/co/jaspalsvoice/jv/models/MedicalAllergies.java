package uk.co.jaspalsvoice.jv.models;

import android.content.ContentValues;

import uk.co.jaspalsvoice.jv.db.DbOpenHelper;

/**
 * Created by Srinivas Kalyani on 11 Jul 16.
 */
public class MedicalAllergies {

    public String medicalAllergies;
    public String type;
    public String uuid;
    public int id;

    public void setMedicalAllergies(String medicalAllergies){
        this.medicalAllergies = medicalAllergies;
    }

    public void setType (String type){
        this.type = type;
    }

    public String getMedicalAllergies(){
        return medicalAllergies;
    }

    public String getType(){
        return type;
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
        cv.put(DbOpenHelper.COLUMN_MA_ALLERGY, getMedicalAllergies());
        cv.put(DbOpenHelper.COLUMN_MA_TYPE, getType());
        return cv;
    }
}
