package uk.co.jaspalsvoice.jv.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ana on 3/21/2016.
 */
public class DbOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "jv.db";
    private static final int DATABASE_VERSION = 1;

    // Table, columns
    public static final String TABLE_MEDICAL_TEAM = "medical_team";
    public static final String COLUMN_MT_UUID = "uuid";
    public static final String COLUMN_MT_ID = "id";
    public static final String COLUMN_MT_DOCTOR_TYPE = "type";
    public static final String COLUMN_MT_NAME = "name";
    public static final String COLUMN_MT_ADDRESS = "address";
    public static final String COLUMN_MT_PHONE = "phone";
    public static final String COLUMN_MT_EMAIL = "email";
    public static final String COLUMN_MT_HOSPITAL_NAME = "hostpitalname";
    public static final String COLUMN_MT_HOSPITAL_ADDRESS = "hospitaladdress";
    public static final String COLUMN_MT_HOSPITAL_PHONE = "hospitalphone";

    // Table, columns
    public static final String TABLE_MEDICAL_TEAM_MEMBER = "medical_team_member";
    public static final String COLUMN_ME_UUID = "uuid";
    public static final String COLUMN_ME_ID = "id";
    public static final String COLUMN_ME_DOCTOR_TYPE = "type";
    public static final String COLUMN_ME_NAME = "name";
    public static final String COLUMN_ME_ADDRESS = "address";
    public static final String COLUMN_ME_PHONE = "phone";
    public static final String COLUMN_ME_EMAIL = "email";
    public static final String COLUMN_ME_HOSPITAL_NAME = "hostpitalname";
    public static final String COLUMN_ME_HOSPITAL_ADDRESS = "hospitaladdress";
    public static final String COLUMN_ME_HOSPITAL_PHONE = "hospitalphone";


    // Table, columns
    public static final String TABLE_MEDICINES = "medicines";
    public static final String COLUMN_M_UUID = "uuid";
    public static final String COLUMN_M_ID = "id";
    public static final String COLUMN_M_NAME = "name";
    public static final String COLUMN_M_DOSAGE = "dosage";
    public static final String COLUMN_M_REASON = "reason";
    public static final String COLUMN_M_FREQUENCY = "frequency";
    public static final String COLUMN_M_RENAL= "renaldosage";
    public static final String COLUMN_M_HEPATIC= "hepaticdosage";

    // Table, columns
    public static final String TABLE_BLOOD_PRESSURE = "bloodpressuretable";
    public static final String COLUMN_B_UUID = "uuid";
    public static final String COLUMN_B_ID = "id";
    public static final String COLUMN_B_BLOODPRESSURE = "bloodpressure";
    public static final String COLUMN_B_DATE = "date";

    // Table, columns
    public static final String TABLE_BLOOD_GLUCOSE = "bloodglucosetable";
    public static final String COLUMN_BG_UUID = "uuid";
    public static final String COLUMN_BG_ID = "id";
    public static final String COLUMN_BG_BLOODGLUCOSE = "bloodglucose";
    public static final String COLUMN_BG_DATE = "date";

    // Table, columns
    public static final String TABLE_HEIGHT= "heighttable";
    public static final String COLUMN_H_UUID = "uuid";
    public static final String COLUMN_H_ID = "id";
    public static final String COLUMN_H_HEIGHT= "height";
    public static final String COLUMN_H_DATE = "date";

    // Table, columns
    public static final String TABLE_WEIGHT= "weighttable";
    public static final String COLUMN_W_UUID = "uuid";
    public static final String COLUMN_W_ID = "id";
    public static final String COLUMN_W_WEIGHT= "weight";
    public static final String COLUMN_W_DATE = "date";

    // Table, columns
    public static final String TABLE_MEDICAL_ALLERGIES= "medicalallergies";
    public static final String COLUMN_MA_UUID = "uuid";
    public static final String COLUMN_MA_ID = "id";
    public static final String COLUMN_MA_ALLERGY= "allergy";
    public static final String COLUMN_MA_TYPE = "type";


    // Table, columns
    public static final String TABLE_FOOD_ALLERGIES= "foodallergies";
    public static final String COLUMN_FA_UUID = "uuid";
    public static final String COLUMN_FA_ID = "id";
    public static final String COLUMN_FA_ALLERGY= "allergy";
    public static final String COLUMN_FA_TYPE = "type";

    // Table, columns
    public static final String TABLE_ENVIRONMENTAL_ALLERGIES= "environmentalallergies";
    public static final String COLUMN_EA_UUID = "uuid";
    public static final String COLUMN_EA_ID = "id";
    public static final String COLUMN_EA_ALLERGY= "allergy";
    public static final String COLUMN_EA_TYPE = "type";

    // Table, columns
    public static final String TABLE_DIAGNOSIS = "diagnosistable";
    public static final String COLUMN_D_UUID = "uuid";
    public static final String COLUMN_D_ID = "id";
    public static final String COLUMN_D_DIAGNOSIS= "diagnosis";
    public static final String COLUMN_D_DATE = "date";

    // Table, columns
    public static final String TABLE_SURGICAL_HISTORY= "surgicalhistory";
    public static final String COLUMN_SH_UUID = "uuid";
    public static final String COLUMN_SH_ID = "id";
    public static final String COLUMN_SH_HISTORY= "history";
    public static final String COLUMN_SH_DATE = "date";




    // Creation statement for TABLE_MEDICAL_TEAM
    private static final String CREATE_TABLE_MEDICAL_TEAM = "CREATE TABLE "
            + TABLE_MEDICAL_TEAM + "("
            + COLUMN_MT_UUID + " TEXT UNIQUE ON CONFLICT REPLACE, "
            + COLUMN_MT_ID + " TEXT, "
            + COLUMN_MT_DOCTOR_TYPE + " TEXT, "
            + COLUMN_MT_NAME + " TEXT, "
            + COLUMN_MT_HOSPITAL_NAME + " TEXT, "
            + COLUMN_MT_HOSPITAL_ADDRESS + " TEXT, "
            + COLUMN_MT_HOSPITAL_PHONE + " TEXT, "
            + COLUMN_MT_ADDRESS + " TEXT, "
            + COLUMN_MT_PHONE + " TEXT, "
            + COLUMN_MT_EMAIL + " TEXT);";

    // Creation statement for TABLE_MEDICAL_TEAM_MEMBER
    private static final String CREATE_TABLE_MEDICAL_TEAM_MEMBER = "CREATE TABLE "
            + TABLE_MEDICAL_TEAM_MEMBER + "("
            + COLUMN_ME_UUID + " TEXT UNIQUE ON CONFLICT REPLACE, "
            + COLUMN_ME_ID + " TEXT, "
            + COLUMN_ME_DOCTOR_TYPE + " TEXT UNIQUE ON CONFLICT REPLACE, "
            + COLUMN_ME_NAME + " TEXT, "
            + COLUMN_ME_ADDRESS + " TEXT, "
            + COLUMN_ME_HOSPITAL_NAME + " TEXT, "
            + COLUMN_ME_HOSPITAL_ADDRESS + " TEXT, "
            + COLUMN_ME_HOSPITAL_PHONE + " TEXT, "
            + COLUMN_ME_PHONE + " TEXT, "
            + COLUMN_ME_EMAIL + " TEXT);";

    // Creation statement for TABLE_MEDICINES
    private static final String CREATE_TABLE_MEDICINES = "CREATE TABLE "
            + TABLE_MEDICINES + "("
            + COLUMN_M_UUID + " TEXT, "
            + COLUMN_M_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_M_NAME + " TEXT, "
            + COLUMN_M_DOSAGE + " TEXT, "
            + COLUMN_M_REASON + " TEXT, "
            + COLUMN_M_FREQUENCY + " TEXT, "
            + COLUMN_M_RENAL + " INTEGER, "
            + COLUMN_M_HEPATIC + " INTEGER);";

    // Creation statement for TABLE_BLOODPRESSURE
    private static final String CREATE_TABLE_BLOODPRESSURE = "CREATE TABLE "
            + TABLE_BLOOD_PRESSURE + "("
            + COLUMN_B_UUID + " TEXT, "
            + COLUMN_B_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_B_BLOODPRESSURE + " TEXT, "
            + COLUMN_B_DATE + " TEXT);";

    // Creation statement for TABLE_BLOODGLUCOSE
    private static final String CREATE_TABLE_BLOODGLUCOSE= "CREATE TABLE "
            + TABLE_BLOOD_GLUCOSE+ "("
            + COLUMN_BG_UUID + " TEXT, "
            + COLUMN_BG_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_BG_BLOODGLUCOSE + " TEXT, "
            + COLUMN_BG_DATE + " TEXT);";

    // Creation statement for TABLE_HEIGHT
    private static final String CREATE_TABLE_HEIGHT= "CREATE TABLE "
            + TABLE_HEIGHT+ "("
            + COLUMN_H_UUID + " TEXT, "
            + COLUMN_H_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_H_HEIGHT+ " TEXT, "
            + COLUMN_H_DATE + " TEXT);";

    // Creation statement for TABLE_WEIGHT
    private static final String CREATE_TABLE_WEIGHT= "CREATE TABLE "
            + TABLE_WEIGHT+ "("
            + COLUMN_W_UUID + " TEXT, "
            + COLUMN_W_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_W_WEIGHT+ " TEXT, "
            + COLUMN_W_DATE + " TEXT);";

    // Creation statement for TABLE_MEDICAL_ALLERGIES
    private static final String CREATE_TABLE_MEDICAL_ALLERGIES= "CREATE TABLE "
            + TABLE_MEDICAL_ALLERGIES+ "("
            + COLUMN_MA_UUID + " TEXT, "
            + COLUMN_MA_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_MA_ALLERGY+ " TEXT, "
            + COLUMN_MA_TYPE + " TEXT);";

    // Creation statement for TABLE_FOOD_ALLERGIES
    private static final String CREATE_TABLE_FOOD_ALLERGIES= "CREATE TABLE "
            + TABLE_FOOD_ALLERGIES+ "("
            + COLUMN_FA_UUID + " TEXT, "
            + COLUMN_FA_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_FA_ALLERGY+ " TEXT, "
            + COLUMN_FA_TYPE + " TEXT);";

    // Creation statement for TABLE_ENVIRONMENTAL_ALLERGIES
    private static final String CREATE_TABLE_ENVIRONMENTAL_ALLERGIES= "CREATE TABLE "
            + TABLE_ENVIRONMENTAL_ALLERGIES+ "("
            + COLUMN_EA_UUID + " TEXT, "
            + COLUMN_EA_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_EA_ALLERGY+ " TEXT, "
            + COLUMN_EA_TYPE + " TEXT);";

    // Creation statement for TABLE_DIAGNOSIS
    private static final String CREATE_TABLE_DIAGNOSIS= "CREATE TABLE "
            + TABLE_DIAGNOSIS+ "("
            + COLUMN_D_UUID + " TEXT, "
            + COLUMN_D_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_D_DIAGNOSIS+ " TEXT, "
            + COLUMN_D_DATE+ " TEXT);";

    // Creation statement for TABLE_SURGICAL_HISTORY
    private static final String CREATE_TABLE_SURGICALHISTORY= "CREATE TABLE "
            + TABLE_SURGICAL_HISTORY+ "("
            + COLUMN_SH_UUID + " TEXT, "
            + COLUMN_SH_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_SH_HISTORY+ " TEXT, "
            + COLUMN_SH_DATE+ " TEXT);";


    public DbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MEDICAL_TEAM);
        db.execSQL(CREATE_TABLE_MEDICINES);
        db.execSQL(CREATE_TABLE_MEDICAL_TEAM_MEMBER);
        db.execSQL(CREATE_TABLE_BLOODPRESSURE);
        db.execSQL(CREATE_TABLE_BLOODGLUCOSE);
        db.execSQL(CREATE_TABLE_HEIGHT);
        db.execSQL(CREATE_TABLE_WEIGHT);
        db.execSQL(CREATE_TABLE_MEDICAL_ALLERGIES);
        db.execSQL(CREATE_TABLE_FOOD_ALLERGIES);
        db.execSQL(CREATE_TABLE_ENVIRONMENTAL_ALLERGIES);
        db.execSQL(CREATE_TABLE_DIAGNOSIS);
        db.execSQL(CREATE_TABLE_SURGICALHISTORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}