package uk.co.jaspalsvoice.jv.models;

import android.content.ContentValues;

import uk.co.jaspalsvoice.jv.db.DbOpenHelper;

/**
 * Created by Ana on 3/21/2016.
 */
public class Medicine {

    private String uuid;
    private int id;
    private String name;
    private String dosage;
    private String reason;
    private String frequency;
    private int renalDosage;
    private int hepaticDosage;

    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(DbOpenHelper.COLUMN_M_NAME, getName());
        cv.put(DbOpenHelper.COLUMN_M_DOSAGE, getDosage());
        cv.put(DbOpenHelper.COLUMN_M_REASON, getReason());
        cv.put(DbOpenHelper.COLUMN_M_FREQUENCY, getFrequency());
        cv.put(DbOpenHelper.COLUMN_M_RENAL, getRenalDosage());
        cv.put(DbOpenHelper.COLUMN_M_HEPATIC, getHepaticDosage());
        return cv;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public int getRenalDosage() {
        return renalDosage;
    }

    public void setRenalDosage(int renalDosage) {
        this.renalDosage = renalDosage;
    }

    public int getHepaticDosage() {
        return hepaticDosage;
    }

    public void setHepaticDosage(int hepaticDosage) {
        this.hepaticDosage = hepaticDosage;
    }
}
