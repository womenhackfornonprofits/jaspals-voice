package uk.co.jaspalsvoice.jv.models;

import android.content.ContentValues;

import uk.co.jaspalsvoice.jv.db.DbOpenHelper;

/**
 * Created by Srinivas Kalyani on 11 Jul 16.
 */
public class VitalsHeight {

    public String height;
    public String date;
    public String uuid;
    public int id;

    public void setHeight(String height){
        this.height = height;
    }

    public void setDate (String date){
        this.date = date;
    }

    public String getHeight(){
        return height;
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
        cv.put(DbOpenHelper.COLUMN_H_HEIGHT, getHeight());
        cv.put(DbOpenHelper.COLUMN_H_DATE, getDate());
        return cv;
    }
}
