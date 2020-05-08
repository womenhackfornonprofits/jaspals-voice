package uk.co.jaspalsvoice.jv.models;

import android.content.ContentValues;

import uk.co.jaspalsvoice.jv.db.DbOpenHelper;

/**
 * Created by Srinivas Kalyani on 11 Jul 16.
 */
public class FoodAllergies {

    public String foodAllergies;
    public String type;
    public String uuid;
    public int id;

    public void setFoodAllergies(String mfoodAllergies){
        this.foodAllergies = mfoodAllergies;
    }

    public void setType (String type){
        this.type = type;
    }

    public String getFoodAllergies(){
        return foodAllergies;
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
        cv.put(DbOpenHelper.COLUMN_FA_ALLERGY, getFoodAllergies());
        cv.put(DbOpenHelper.COLUMN_FA_TYPE, getType());
        return cv;
    }
}
