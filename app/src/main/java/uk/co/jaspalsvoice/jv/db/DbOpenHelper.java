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

    // Table, columns
    public static final String TABLE_BLOOD_PRESSURE = "bloodpressuretable";
    public static final String COLUMN_B_UUID = "uuid";
    public static final String COLUMN_B_ID = "id";
    public static final String COLUMN_B_BLOODPRESSURE = "bloodpressure";
    public static final String COLUMN_B_DATE = "date";

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
            + COLUMN_M_FREQUENCY + " TEXT);";

    // Creation statement for TABLE_BLOODPRESSURE
    private static final String CREATE_TABLE_BLOODPRESSURE = "CREATE TABLE "
            + TABLE_BLOOD_PRESSURE + "("
            + COLUMN_B_UUID + " TEXT, "
            + COLUMN_B_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_B_BLOODPRESSURE + " TEXT, "
            + COLUMN_B_DATE + " TEXT);";

    public DbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MEDICAL_TEAM);
        db.execSQL(CREATE_TABLE_MEDICINES);
        db.execSQL(CREATE_TABLE_MEDICAL_TEAM_MEMBER);
        db.execSQL(CREATE_TABLE_BLOODPRESSURE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}