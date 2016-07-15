package uk.co.jaspalsvoice.jv.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import uk.co.jaspalsvoice.jv.models.Diagnosis;
import uk.co.jaspalsvoice.jv.models.Doctor;
import uk.co.jaspalsvoice.jv.models.EnvironmentalAllergies;
import uk.co.jaspalsvoice.jv.models.FoodAllergies;
import uk.co.jaspalsvoice.jv.models.MedicalAllergies;
import uk.co.jaspalsvoice.jv.models.Medicine;
import uk.co.jaspalsvoice.jv.models.SurgicalHistory;
import uk.co.jaspalsvoice.jv.models.VitalsBloodGlucose;
import uk.co.jaspalsvoice.jv.models.VitalsBloodPressure;
import uk.co.jaspalsvoice.jv.models.VitalsHeight;
import uk.co.jaspalsvoice.jv.models.VitalsWeight;


/**
 * Created by Ana on 3/21/2016.
 */
public class DbHelper {
    private final static String TAG = DbHelper.class.getSimpleName();

    private ExecutorService executor = Executors.newFixedThreadPool(1);
    private final SQLiteDatabase sqlite;

    private static final String[] MEDICAL_TEAM_COLUMN_NAMES = new String[]{
            DbOpenHelper.COLUMN_MT_UUID,
            DbOpenHelper.COLUMN_MT_ID,
            DbOpenHelper.COLUMN_MT_DOCTOR_TYPE,
            DbOpenHelper.COLUMN_MT_NAME,
            DbOpenHelper.COLUMN_MT_HOSPITAL_NAME,
            DbOpenHelper.COLUMN_MT_HOSPITAL_ADDRESS,
            DbOpenHelper.COLUMN_MT_HOSPITAL_PHONE,
            DbOpenHelper.COLUMN_MT_ADDRESS,
            DbOpenHelper.COLUMN_MT_EMAIL,
            DbOpenHelper.COLUMN_MT_PHONE};

    private static final String[] MEDICAL_TEAM_MEMBER_COLUMN_NAMES = new String[]{
            DbOpenHelper.COLUMN_ME_UUID,
            DbOpenHelper.COLUMN_ME_ID,
            DbOpenHelper.COLUMN_ME_DOCTOR_TYPE,
            DbOpenHelper.COLUMN_ME_NAME,
            DbOpenHelper.COLUMN_ME_ADDRESS,
            DbOpenHelper.COLUMN_ME_EMAIL,
            DbOpenHelper.COLUMN_ME_PHONE};

    private static final String[] MEDICINES_COLUMN_NAMES = new String[]{
            DbOpenHelper.COLUMN_M_UUID,
            DbOpenHelper.COLUMN_M_ID,
            DbOpenHelper.COLUMN_M_DOSAGE,
            DbOpenHelper.COLUMN_M_NAME,
            DbOpenHelper.COLUMN_M_REASON,
            DbOpenHelper.COLUMN_M_FREQUENCY,
            DbOpenHelper.COLUMN_M_RENAL,
            DbOpenHelper.COLUMN_M_HEPATIC

    };

    private static final String[] BLOOD_PRESSURE_COLUMN_NAMES = new String[]{
            DbOpenHelper.COLUMN_B_UUID,
            DbOpenHelper.COLUMN_B_ID,
            DbOpenHelper.COLUMN_B_BLOODPRESSURE,
            DbOpenHelper.COLUMN_B_DATE,
    };

    private static final String[] BLOOD_GLUCOSE_COLUMN_NAMES = new String[]{
            DbOpenHelper.COLUMN_BG_UUID,
            DbOpenHelper.COLUMN_BG_ID,
            DbOpenHelper.COLUMN_BG_BLOODGLUCOSE,
            DbOpenHelper.COLUMN_BG_DATE,
    };

    private static final String[] HEIGHT_COLUMN_NAMES = new String[]{
            DbOpenHelper.COLUMN_H_UUID,
            DbOpenHelper.COLUMN_H_ID,
            DbOpenHelper.COLUMN_H_HEIGHT,
            DbOpenHelper.COLUMN_H_DATE,
    };

    private static final String[] WEIGHT_COLUMN_NAMES = new String[]{
            DbOpenHelper.COLUMN_W_UUID,
            DbOpenHelper.COLUMN_W_ID,
            DbOpenHelper.COLUMN_W_WEIGHT,
            DbOpenHelper.COLUMN_W_DATE,
    };

    private static final String[] MEDICAL_ALLERGIES_COLUMN_NAMES = new String[]{
            DbOpenHelper.COLUMN_MA_UUID,
            DbOpenHelper.COLUMN_MA_ID,
            DbOpenHelper.COLUMN_MA_ALLERGY,
            DbOpenHelper.COLUMN_MA_TYPE,
    };

    private static final String[] FOOD_ALLERGIES_COLUMN_NAMES = new String[]{
            DbOpenHelper.COLUMN_FA_UUID,
            DbOpenHelper.COLUMN_FA_ID,
            DbOpenHelper.COLUMN_FA_ALLERGY,
            DbOpenHelper.COLUMN_FA_TYPE,
    };

    private static final String[] ENVIRONMENTAL_ALLERGIES_COLUMN_NAMES = new String[]{
            DbOpenHelper.COLUMN_EA_UUID,
            DbOpenHelper.COLUMN_EA_ID,
            DbOpenHelper.COLUMN_EA_ALLERGY,
            DbOpenHelper.COLUMN_EA_TYPE,
    };

    private static final String[] DIAGNOSIS_COLUMN_NAMES = new String[]{
            DbOpenHelper.COLUMN_D_UUID,
            DbOpenHelper.COLUMN_D_ID,
            DbOpenHelper.COLUMN_D_DIAGNOSIS,
            DbOpenHelper.COLUMN_D_DATE,
    };


    private static final String[] SURGICAL_HISTORY_COLUMN_NAMES = new String[]{
            DbOpenHelper.COLUMN_SH_UUID,
            DbOpenHelper.COLUMN_SH_ID,
            DbOpenHelper.COLUMN_SH_HISTORY,
            DbOpenHelper.COLUMN_SH_DATE,
    };
    public DbHelper(DbOpenHelper DbOpenHelper) {
        sqlite = DbOpenHelper.getWritableDatabase();
    }

    /**
     * Inserts medical team in the db.
     */
    public Future<Long> insertOrReplaceDoctor(final List<Doctor> doctors) {
        return executor.submit(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                long insertedRows = 0;
                try {
                    sqlite.beginTransaction();
                    for (Doctor doctor : doctors) {
                        if (sqlite.insertWithOnConflict(DbOpenHelper.TABLE_MEDICAL_TEAM, null, doctor.toContentValues(), SQLiteDatabase.CONFLICT_REPLACE) >= 0) {
                            insertedRows++;
                        }
                    }
                    Log.d(TAG, "Insert succeeded, inserted rows:" + insertedRows);
                    sqlite.setTransactionSuccessful();
                    return insertedRows;
                } finally {
                    sqlite.endTransaction();
                }
            }
        });
    }

    /**
     * Method to insert a team member
     *
     * @param doctors List of doctors to be inserted in the db
     * @return the number of rows inserted
     */
    public Future<Long> insertOrReplaceTeamMember(final List<Doctor> doctors) {
        return executor.submit(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                long insertedRows = 0;
                try {
                    sqlite.beginTransaction();
                    for (Doctor doctor : doctors) {
                        if (sqlite.insertWithOnConflict(DbOpenHelper.TABLE_MEDICAL_TEAM_MEMBER, null, doctor.toContentValues(), SQLiteDatabase.CONFLICT_REPLACE) >= 0) {
                            insertedRows++;
                        }
                    }
                    Log.d(TAG, "Insert succeeded, inserted rows:" + insertedRows);
                    sqlite.setTransactionSuccessful();
                    return insertedRows;
                } finally {
                    sqlite.endTransaction();
                }
            }
        });
    }

    /**
     * Inserts medicines in the db.
     */
    public Future<Long> insertOrReplaceMedicine(final List<Medicine> medicines, final boolean isUpdate, final int id) {
        return executor.submit(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                long insertedRows = 0;
                try {
                    sqlite.beginTransaction();
                    for (Medicine medicine : medicines) {
                        if (!isUpdate) {
                            if (sqlite.insert(DbOpenHelper.TABLE_MEDICINES, null,
                                    medicine.toContentValues()) >= 0) {
                                insertedRows++;
                                Log.d(TAG, "Insert succeeded, inserted rows:" + insertedRows);
                            }
                        } else {
                            String where = "id=?";
                            String[] whereArgs = new String[]{String.valueOf(id)};
                            if (sqlite.update(DbOpenHelper.TABLE_MEDICINES,
                                    medicine.toContentValues(), where, whereArgs) >= 0) {
                                insertedRows++;
                                Log.d(TAG, "Update succeeded, updated rows:" + insertedRows + id);
                            }
                        }
                    }
                    sqlite.setTransactionSuccessful();
                    return insertedRows;
                } finally {
                    sqlite.endTransaction();
                }
            }
        });
    }

    /**
     * Inserts blood pressures in the db.
     */
    public Future<Long> insertOrReplaceBloodPressure(final List<VitalsBloodPressure> bloodPressures,
                                                     final boolean isUpdate, final int id) {
        return executor.submit(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                long insertedRows = 0;
                try {
                    sqlite.beginTransaction();
                    for (VitalsBloodPressure bloodPressure: bloodPressures) {
                        if (!isUpdate) {
                            if (sqlite.insert(DbOpenHelper.TABLE_BLOOD_PRESSURE, null,
                                    bloodPressure.toContentValues()) >= 0) {
                                insertedRows++;
                                Log.d(TAG, "Insert succeeded, inserted rows:" + insertedRows);
                            }
                        } else {
                            String where = "id=?";
                            String[] whereArgs = new String[]{String.valueOf(id)};
                            if (sqlite.update(DbOpenHelper.TABLE_BLOOD_PRESSURE,
                                    bloodPressure.toContentValues(), where, whereArgs) >= 0) {
                                insertedRows++;
                                Log.d(TAG, "Update succeeded, updated rows:" + insertedRows + id);
                            }
                        }
                    }
                    sqlite.setTransactionSuccessful();
                    return insertedRows;
                } finally {
                    sqlite.endTransaction();
                }
            }
        });
    }

    /**
     * Inserts blood glucoses in the db.
     */
    public Future<Long> insertOrReplaceBloodGlucose(final List<VitalsBloodGlucose> bloodGlucoses,
                                                     final boolean isUpdate, final int id) {
        return executor.submit(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                long insertedRows = 0;
                try {
                    sqlite.beginTransaction();
                    for (VitalsBloodGlucose bloodGlucose: bloodGlucoses) {
                        if (!isUpdate) {
                            if (sqlite.insert(DbOpenHelper.TABLE_BLOOD_GLUCOSE, null,
                                    bloodGlucose.toContentValues()) >= 0) {
                                insertedRows++;
                                Log.d(TAG, "Insert succeeded, inserted rows:" + insertedRows);
                            }
                        } else {
                            String where = "id=?";
                            String[] whereArgs = new String[]{String.valueOf(id)};
                            if (sqlite.update(DbOpenHelper.TABLE_BLOOD_GLUCOSE,
                                    bloodGlucose.toContentValues(), where, whereArgs) >= 0) {
                                insertedRows++;
                                Log.d(TAG, "Update succeeded, updated rows:" + insertedRows + id);
                            }
                        }
                    }
                    sqlite.setTransactionSuccessful();
                    return insertedRows;
                } finally {
                    sqlite.endTransaction();
                }
            }
        });
    }

    /**
     * Inserts heights in the db.
     */
    public Future<Long> insertOrReplaceHeight(final List<VitalsHeight> heights,
                                                    final boolean isUpdate, final int id) {
        return executor.submit(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                long insertedRows = 0;
                try {
                    sqlite.beginTransaction();
                    for (VitalsHeight height: heights) {
                        if (!isUpdate) {
                            if (sqlite.insert(DbOpenHelper.TABLE_HEIGHT, null,
                                    height.toContentValues()) >= 0) {
                                insertedRows++;
                                Log.d(TAG, "Insert succeeded, inserted rows:" + insertedRows);
                            }
                        } else {
                            String where = "id=?";
                            String[] whereArgs = new String[]{String.valueOf(id)};
                            if (sqlite.update(DbOpenHelper.TABLE_HEIGHT,
                                    height.toContentValues(), where, whereArgs) >= 0) {
                                insertedRows++;
                                Log.d(TAG, "Update succeeded, updated rows:" + insertedRows + id);
                            }
                        }
                    }
                    sqlite.setTransactionSuccessful();
                    return insertedRows;
                } finally {
                    sqlite.endTransaction();
                }
            }
        });
    }

    /**
     * Inserts weights in the db.
     */
    public Future<Long> insertOrReplaceWeight(final List<VitalsWeight> weights,
                                              final boolean isUpdate, final int id) {
        return executor.submit(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                long insertedRows = 0;
                try {
                    sqlite.beginTransaction();
                    for (VitalsWeight weight: weights) {
                        if (!isUpdate) {
                            if (sqlite.insert(DbOpenHelper.TABLE_WEIGHT, null,
                                    weight.toContentValues()) >= 0) {
                                insertedRows++;
                                Log.d(TAG, "Insert succeeded, inserted rows:" + insertedRows);
                            }
                        } else {
                            String where = "id=?";
                            String[] whereArgs = new String[]{String.valueOf(id)};
                            if (sqlite.update(DbOpenHelper.TABLE_WEIGHT,
                                    weight.toContentValues(), where, whereArgs) >= 0) {
                                insertedRows++;
                                Log.d(TAG, "Update succeeded, updated rows:" + insertedRows + id);
                            }
                        }
                    }
                    sqlite.setTransactionSuccessful();
                    return insertedRows;
                } finally {
                    sqlite.endTransaction();
                }
            }
        });
    }


    /**
     * Inserts diagnoses in the db.
     */
    public Future<Long> insertOrReplaceDiagnosis(final List<Diagnosis> diagnoses,
                                              final boolean isUpdate, final int id) {
        return executor.submit(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                long insertedRows = 0;
                try {
                    sqlite.beginTransaction();
                    for (Diagnosis weight: diagnoses) {
                        if (!isUpdate) {
                            if (sqlite.insert(DbOpenHelper.TABLE_DIAGNOSIS, null,
                                    weight.toContentValues()) >= 0) {
                                insertedRows++;
                                Log.d(TAG, "Insert succeeded, inserted rows:" + insertedRows);
                            }
                        } else {
                            String where = "id=?";
                            String[] whereArgs = new String[]{String.valueOf(id)};
                            if (sqlite.update(DbOpenHelper.TABLE_DIAGNOSIS,
                                    weight.toContentValues(), where, whereArgs) >= 0) {
                                insertedRows++;
                                Log.d(TAG, "Update succeeded, updated rows:" + insertedRows + id);
                            }
                        }
                    }
                    sqlite.setTransactionSuccessful();
                    return insertedRows;
                } finally {
                    sqlite.endTransaction();
                }
            }
        });
    }

    /**
     * Inserts surgical history in the db.
     */
    public Future<Long> insertOrReplaceSurgicalHistory(final List<SurgicalHistory> histories,
                                                 final boolean isUpdate, final int id) {
        return executor.submit(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                long insertedRows = 0;
                try {
                    sqlite.beginTransaction();
                    for (SurgicalHistory history: histories) {
                        if (!isUpdate) {
                            if (sqlite.insert(DbOpenHelper.TABLE_SURGICAL_HISTORY, null,
                                    history.toContentValues()) >= 0) {
                                insertedRows++;
                                Log.d(TAG, "Insert succeeded, inserted rows:" + insertedRows);
                            }
                        } else {
                            String where = "id=?";
                            String[] whereArgs = new String[]{String.valueOf(id)};
                            if (sqlite.update(DbOpenHelper.TABLE_SURGICAL_HISTORY,
                                    history.toContentValues(), where, whereArgs) >= 0) {
                                insertedRows++;
                                Log.d(TAG, "Update succeeded, updated rows:" + insertedRows + id);
                            }
                        }
                    }
                    sqlite.setTransactionSuccessful();
                    return insertedRows;
                } finally {
                    sqlite.endTransaction();
                }
            }
        });
    }

    /**
     * Inserts medical allergies in the db.
     */
    public Future<Long> insertOrReplaceMedicalAllergies(final List<MedicalAllergies> allergies,
                                              final boolean isUpdate, final int id) {
        return executor.submit(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                long insertedRows = 0;
                try {
                    sqlite.beginTransaction();
                    for (MedicalAllergies allergy: allergies) {
                        if (!isUpdate) {
                            if (sqlite.insert(DbOpenHelper.TABLE_MEDICAL_ALLERGIES, null,
                                    allergy.toContentValues()) >= 0) {
                                insertedRows++;
                                Log.d(TAG, "Insert succeeded, inserted rows:" + insertedRows);
                            }
                        } else {
                            String where = "id=?";
                            String[] whereArgs = new String[]{String.valueOf(id)};
                            if (sqlite.update(DbOpenHelper.TABLE_MEDICAL_ALLERGIES,
                                    allergy.toContentValues(), where, whereArgs) >= 0) {
                                insertedRows++;
                                Log.d(TAG, "Update succeeded, updated rows:" + insertedRows + id);
                            }
                        }
                    }
                    sqlite.setTransactionSuccessful();
                    return insertedRows;
                } finally {
                    sqlite.endTransaction();
                }
            }
        });
    }

    /**
     * Inserts food allergies in the db.
     */
    public Future<Long> insertOrReplaceFoodAllergies(final List<FoodAllergies> allergies,
                                                        final boolean isUpdate, final int id) {
        return executor.submit(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                long insertedRows = 0;
                try {
                    sqlite.beginTransaction();
                    for (FoodAllergies allergy: allergies) {
                        if (!isUpdate) {
                            if (sqlite.insert(DbOpenHelper.TABLE_FOOD_ALLERGIES, null,
                                    allergy.toContentValues()) >= 0) {
                                insertedRows++;
                                Log.d(TAG, "Insert succeeded, inserted rows:" + insertedRows);
                            }
                        } else {
                            String where = "id=?";
                            String[] whereArgs = new String[]{String.valueOf(id)};
                            if (sqlite.update(DbOpenHelper.TABLE_FOOD_ALLERGIES,
                                    allergy.toContentValues(), where, whereArgs) >= 0) {
                                insertedRows++;
                                Log.d(TAG, "Update succeeded, updated rows:" + insertedRows + id);
                            }
                        }
                    }
                    sqlite.setTransactionSuccessful();
                    return insertedRows;
                } finally {
                    sqlite.endTransaction();
                }
            }
        });
    }

    /**
     * Inserts environmental allergies in the db.
     */
    public Future<Long> insertOrReplaceEnvironmentalAllergies(final List<EnvironmentalAllergies> allergies,
                                                     final boolean isUpdate, final int id) {
        return executor.submit(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                long insertedRows = 0;
                try {
                    sqlite.beginTransaction();
                    for (EnvironmentalAllergies allergy: allergies) {
                        if (!isUpdate) {
                            if (sqlite.insert(DbOpenHelper.TABLE_ENVIRONMENTAL_ALLERGIES, null,
                                    allergy.toContentValues()) >= 0) {
                                insertedRows++;
                                Log.d(TAG, "Insert succeeded, inserted rows:" + insertedRows);
                            }
                        } else {
                            String where = "id=?";
                            String[] whereArgs = new String[]{String.valueOf(id)};
                            if (sqlite.update(DbOpenHelper.TABLE_ENVIRONMENTAL_ALLERGIES,
                                    allergy.toContentValues(), where, whereArgs) >= 0) {
                                insertedRows++;
                                Log.d(TAG, "Update succeeded, updated rows:" + insertedRows + id);
                            }
                        }
                    }
                    sqlite.setTransactionSuccessful();
                    return insertedRows;
                } finally {
                    sqlite.endTransaction();
                }
            }
        });
    }



    /**
     * Gets from db a list containing all doctors.
     */
    public Map<String, Doctor> readAllDoctors() {
        Map<String, Doctor> doctors = new HashMap<>();
        Cursor allDoctors = null;
        try {
            allDoctors = readAllFuture(DbOpenHelper.TABLE_MEDICAL_TEAM, MEDICAL_TEAM_COLUMN_NAMES, DbOpenHelper.COLUMN_MT_ID).get();
            if (allDoctors != null) {
                if (allDoctors.moveToFirst()) {
                    while (!allDoctors.isAfterLast()) {
                        Doctor doctor = new Doctor();
                        doctor.setName(allDoctors.getString(allDoctors.getColumnIndex(DbOpenHelper.COLUMN_MT_NAME)));
                        doctor.setHospitalName(allDoctors.getString(allDoctors.getColumnIndex(DbOpenHelper.COLUMN_MT_HOSPITAL_NAME)));
                        doctor.setHospitalAddress(allDoctors.getString(allDoctors.getColumnIndex(DbOpenHelper.COLUMN_MT_HOSPITAL_ADDRESS)));
                        doctor.setHospitalPhone(allDoctors.getString(allDoctors.getColumnIndex(DbOpenHelper.COLUMN_MT_HOSPITAL_PHONE)));
                        doctor.setType(allDoctors.getString(allDoctors.getColumnIndex(DbOpenHelper.COLUMN_MT_DOCTOR_TYPE)));
                        doctor.setAddress(allDoctors.getString(allDoctors.getColumnIndex(DbOpenHelper.COLUMN_MT_ADDRESS)));
                        doctor.setPhone(allDoctors.getString(allDoctors.getColumnIndex(DbOpenHelper.COLUMN_MT_PHONE)));
                        doctor.setEmail(allDoctors.getString(allDoctors.getColumnIndex(DbOpenHelper.COLUMN_MT_EMAIL)));
                        doctors.put(doctor.getType(), doctor);
                        allDoctors.moveToNext();
                    }
                }
            }
        } catch (InterruptedException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            if (allDoctors != null) {
                allDoctors.close();
            }
        }
        return doctors;
    }

    /**
     * Gets from db a list containing all doctors.
     */
    public List<Doctor> readAllTeamMembers() {
        List<Doctor> doctors = new ArrayList<>();
        Cursor allDoctors = null;
        try {
            allDoctors = readAllFuture(DbOpenHelper.TABLE_MEDICAL_TEAM_MEMBER, MEDICAL_TEAM_MEMBER_COLUMN_NAMES, DbOpenHelper.COLUMN_ME_ID).get();
            if (allDoctors != null) {
                if (allDoctors.moveToFirst()) {
                    while (!allDoctors.isAfterLast()) {
                        Doctor doctor = new Doctor();
                        doctor.setName(allDoctors.getString(allDoctors.getColumnIndex(DbOpenHelper.COLUMN_ME_NAME)));
                        doctor.setType(allDoctors.getString(allDoctors.getColumnIndex(DbOpenHelper.COLUMN_ME_DOCTOR_TYPE)));
                        doctor.setAddress(allDoctors.getString(allDoctors.getColumnIndex(DbOpenHelper.COLUMN_ME_ADDRESS)));
                        doctor.setPhone(allDoctors.getString(allDoctors.getColumnIndex(DbOpenHelper.COLUMN_ME_PHONE)));
                        doctor.setEmail(allDoctors.getString(allDoctors.getColumnIndex(DbOpenHelper.COLUMN_ME_EMAIL)));
                        doctors.add(doctor);
                        allDoctors.moveToNext();
                    }
                }
            }
        } catch (InterruptedException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            if (allDoctors != null) {
                allDoctors.close();
            }
        }
        return doctors;
    }

    /**
     * Gets from db a list containing all blood pressures.
     */
    public List<VitalsBloodPressure> readAllBloodPressures() {
        List<VitalsBloodPressure> bloodPressures = new ArrayList<>();
        Cursor allBloodPressures = null;
        try {
           allBloodPressures = readAllFuture(DbOpenHelper.TABLE_BLOOD_PRESSURE,
                    BLOOD_PRESSURE_COLUMN_NAMES, DbOpenHelper.COLUMN_B_ID).get();
            if (allBloodPressures != null) {
                if (allBloodPressures.moveToFirst()) {
                    while (!allBloodPressures.isAfterLast()) {
                        VitalsBloodPressure bloodPressure = new VitalsBloodPressure();
                        bloodPressure.setId(allBloodPressures.getInt(allBloodPressures.getColumnIndex(DbOpenHelper.COLUMN_B_ID)));
                        bloodPressure .setBloodPressure(allBloodPressures.getString(allBloodPressures.getColumnIndex(DbOpenHelper.COLUMN_B_BLOODPRESSURE)));
                        bloodPressure .setDate(allBloodPressures.getString(allBloodPressures.getColumnIndex(DbOpenHelper.COLUMN_B_DATE)));
                        bloodPressures.add(bloodPressure);
                        allBloodPressures.moveToNext();
                    }
                }
            }
        } catch (InterruptedException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            if (allBloodPressures != null) {
                allBloodPressures.close();
            }
        }
        return bloodPressures;
    }

    /**
     * Gets from db a list containing all blood glucoses.
     */
    public List<VitalsBloodGlucose> readAllBloodGlucoses() {
        List<VitalsBloodGlucose> bloodGlucoses = new ArrayList<>();
        Cursor allBloodGlucoses = null;
        try {
            allBloodGlucoses = readAllFuture(DbOpenHelper.TABLE_BLOOD_GLUCOSE,
                    BLOOD_GLUCOSE_COLUMN_NAMES, DbOpenHelper.COLUMN_BG_ID).get();
            if (allBloodGlucoses != null) {
                if (allBloodGlucoses.moveToFirst()) {
                    while (!allBloodGlucoses.isAfterLast()) {
                        VitalsBloodGlucose bloodGlucose = new VitalsBloodGlucose();
                        bloodGlucose.setId(allBloodGlucoses.getInt(allBloodGlucoses.getColumnIndex(DbOpenHelper.COLUMN_BG_ID)));
                        bloodGlucose .setBloodGlucose(allBloodGlucoses.getString(allBloodGlucoses.getColumnIndex(DbOpenHelper.COLUMN_BG_BLOODGLUCOSE)));
                        bloodGlucose .setDate(allBloodGlucoses.getString(allBloodGlucoses.getColumnIndex(DbOpenHelper.COLUMN_BG_DATE)));
                        bloodGlucoses.add(bloodGlucose);
                        allBloodGlucoses.moveToNext();
                    }
                }
            }
        } catch (InterruptedException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            if (allBloodGlucoses != null) {
                allBloodGlucoses.close();
            }
        }
        return bloodGlucoses;
    }

    /**
     * Gets from db a list containing all heights.
     */
    public List<VitalsHeight> readAllHeights() {
        List<VitalsHeight> heights = new ArrayList<>();
        Cursor allHeights= null;
        try {
            allHeights = readAllFuture(DbOpenHelper.TABLE_HEIGHT,
                    HEIGHT_COLUMN_NAMES, DbOpenHelper.COLUMN_H_ID).get();
            if (allHeights != null) {
                if (allHeights.moveToFirst()) {
                    while (!allHeights.isAfterLast()) {
                        VitalsHeight height = new VitalsHeight();
                        height.setId(allHeights.getInt(allHeights.getColumnIndex(DbOpenHelper.COLUMN_H_ID)));
                        height .setHeight(allHeights.getString(allHeights.getColumnIndex(DbOpenHelper.COLUMN_H_HEIGHT)));
                        height .setDate(allHeights.getString(allHeights.getColumnIndex(DbOpenHelper.COLUMN_H_DATE)));
                        heights.add(height);
                        allHeights.moveToNext();
                    }
                }
            }
        } catch (InterruptedException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            if (allHeights != null) {
                allHeights.close();
            }
        }
        return heights;
    }


    /**
     * Gets from db a list containing all weights.
     */
    public List<VitalsWeight> readAllWeights() {
        List<VitalsWeight> weights = new ArrayList<>();
        Cursor allWeights= null;
        try {
            allWeights = readAllFuture(DbOpenHelper.TABLE_WEIGHT,
                    WEIGHT_COLUMN_NAMES, DbOpenHelper.COLUMN_W_ID).get();
            if (allWeights != null) {
                if (allWeights.moveToFirst()) {
                    while (!allWeights.isAfterLast()) {
                        VitalsWeight weight = new VitalsWeight();
                        weight.setId(allWeights.getInt(allWeights.getColumnIndex(DbOpenHelper.COLUMN_W_ID)));
                        weight .setWeight(allWeights.getString(allWeights.getColumnIndex(DbOpenHelper.COLUMN_W_WEIGHT)));
                        weight .setDate(allWeights.getString(allWeights.getColumnIndex(DbOpenHelper.COLUMN_W_DATE)));
                        weights.add(weight);
                        allWeights.moveToNext();
                    }
                }
            }
        } catch (InterruptedException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            if (allWeights != null) {
                allWeights.close();
            }
        }
        return weights;
    }

    /**
     * Gets from db a list containing all medical allergies.
     */
    public List<MedicalAllergies> readAllMedicalAllergies() {
        List<MedicalAllergies> allergies = new ArrayList<>();
        Cursor allMedicalAllergies= null;
        try {
            allMedicalAllergies = readAllFuture(DbOpenHelper.TABLE_MEDICAL_ALLERGIES,
                    MEDICAL_ALLERGIES_COLUMN_NAMES, DbOpenHelper.COLUMN_MA_ID).get();
            if (allMedicalAllergies != null) {
                if (allMedicalAllergies.moveToFirst()) {
                    while (!allMedicalAllergies.isAfterLast()) {
                        MedicalAllergies allergy = new MedicalAllergies();
                        allergy.setId(allMedicalAllergies.getInt(allMedicalAllergies.getColumnIndex(DbOpenHelper.COLUMN_MA_ID)));
                        allergy .setMedicalAllergies(allMedicalAllergies.getString(allMedicalAllergies.getColumnIndex(DbOpenHelper.COLUMN_MA_ALLERGY)));
                        allergy .setType(allMedicalAllergies.getString(allMedicalAllergies.getColumnIndex(DbOpenHelper.COLUMN_MA_TYPE)));
                        allergies.add(allergy);
                        allMedicalAllergies.moveToNext();
                    }
                }
            }
        } catch (InterruptedException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            if (allMedicalAllergies != null) {
                allMedicalAllergies.close();
            }
        }
        return allergies;
    }

    /**
     * Gets from db a list containing all food allergies.
     */
    public List<FoodAllergies> readAllFoodAllergies() {
        List<FoodAllergies> allergies = new ArrayList<>();
        Cursor allFoodAllergies= null;
        try {
            allFoodAllergies = readAllFuture(DbOpenHelper.TABLE_FOOD_ALLERGIES,
                    FOOD_ALLERGIES_COLUMN_NAMES, DbOpenHelper.COLUMN_MA_ID).get();
            if (allFoodAllergies != null) {
                if (allFoodAllergies.moveToFirst()) {
                    while (!allFoodAllergies.isAfterLast()) {
                        FoodAllergies allergy = new FoodAllergies();
                        allergy.setId(allFoodAllergies.getInt(allFoodAllergies.getColumnIndex(DbOpenHelper.COLUMN_FA_ID)));
                        allergy .setFoodAllergies(allFoodAllergies.getString(allFoodAllergies.getColumnIndex(DbOpenHelper.COLUMN_FA_ALLERGY)));
                        allergy .setType(allFoodAllergies.getString(allFoodAllergies.getColumnIndex(DbOpenHelper.COLUMN_FA_TYPE)));
                        allergies.add(allergy);
                        allFoodAllergies.moveToNext();
                    }
                }
            }
        } catch (InterruptedException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            if (allFoodAllergies != null) {
                allFoodAllergies.close();
            }
        }
        return allergies;
    }

    /**
     * Gets from db a list containing all environmental allergies.
     */
    public List<EnvironmentalAllergies> readAllEnvironmentalAllergies() {
        List<EnvironmentalAllergies> allergies = new ArrayList<>();
        Cursor allEnvironmentalAllergies= null;
        try {
            allEnvironmentalAllergies = readAllFuture(DbOpenHelper.TABLE_ENVIRONMENTAL_ALLERGIES,
                    ENVIRONMENTAL_ALLERGIES_COLUMN_NAMES, DbOpenHelper.COLUMN_MA_ID).get();
            if (allEnvironmentalAllergies != null) {
                if (allEnvironmentalAllergies.moveToFirst()) {
                    while (!allEnvironmentalAllergies.isAfterLast()) {
                        EnvironmentalAllergies allergy = new EnvironmentalAllergies();
                        allergy.setId(allEnvironmentalAllergies.getInt(allEnvironmentalAllergies.getColumnIndex(DbOpenHelper.COLUMN_FA_ID)));
                        allergy .setEnvironmentalAllergies(allEnvironmentalAllergies.getString(allEnvironmentalAllergies.getColumnIndex(DbOpenHelper.COLUMN_FA_ALLERGY)));
                        allergy .setType(allEnvironmentalAllergies.getString(allEnvironmentalAllergies.getColumnIndex(DbOpenHelper.COLUMN_FA_TYPE)));
                        allergies.add(allergy);
                        allEnvironmentalAllergies.moveToNext();
                    }
                }
            }
        } catch (InterruptedException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            if (allEnvironmentalAllergies != null) {
                allEnvironmentalAllergies.close();
            }
        }
        return allergies;
    }

    /**
     * Gets from db a list containing all environmental allergies.
     */
    public List<Diagnosis> readAllDiagnosis() {
        List<Diagnosis> diagnoses = new ArrayList<>();
        Cursor allDiagnoses= null;
        try {
            allDiagnoses= readAllFuture(DbOpenHelper.TABLE_DIAGNOSIS,
                    DIAGNOSIS_COLUMN_NAMES, DbOpenHelper.COLUMN_D_ID).get();
            if (allDiagnoses != null) {
                if (allDiagnoses.moveToFirst()) {
                    while (!allDiagnoses.isAfterLast()) {
                        Diagnosis diagnosis = new Diagnosis();
                        diagnosis.setId(allDiagnoses.getInt(allDiagnoses.getColumnIndex(DbOpenHelper.COLUMN_D_ID)));
                        diagnosis .setDiagnosis(allDiagnoses.getString(allDiagnoses.getColumnIndex(DbOpenHelper.COLUMN_D_DIAGNOSIS)));
                        diagnosis .setDate(allDiagnoses.getString(allDiagnoses.getColumnIndex(DbOpenHelper.COLUMN_D_DATE)));
                        diagnoses.add(diagnosis);
                        allDiagnoses.moveToNext();
                    }
                }
            }
        } catch (InterruptedException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            if (allDiagnoses != null) {
                allDiagnoses.close();
            }
        }
        return diagnoses;
    }

    /**
     * Gets from db a list containing surgical history.
     */
    public List<SurgicalHistory> readAllSurgicalHistory() {
        List<SurgicalHistory> histories = new ArrayList<>();
        Cursor allHistories= null;
        try {
            allHistories= readAllFuture(DbOpenHelper.TABLE_SURGICAL_HISTORY,
                    SURGICAL_HISTORY_COLUMN_NAMES, DbOpenHelper.COLUMN_SH_ID).get();
            if (allHistories != null) {
                if (allHistories.moveToFirst()) {
                    while (!allHistories.isAfterLast()) {
                        SurgicalHistory history = new SurgicalHistory();
                        history.setId(allHistories.getInt(allHistories.getColumnIndex(DbOpenHelper.COLUMN_SH_ID)));
                        history .setSurgicalHistory(allHistories.getString(allHistories.getColumnIndex(DbOpenHelper.COLUMN_SH_HISTORY)));
                        history .setDate(allHistories.getString(allHistories.getColumnIndex(DbOpenHelper.COLUMN_SH_DATE)));
                        histories.add(history);
                        allHistories.moveToNext();
                    }
                }
            }
        } catch (InterruptedException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            if (allHistories != null) {
                allHistories.close();
            }
        }
        return histories;
    }





    /**
     * Gets from db a list containing all medicines.
     */
    public List<Medicine> readAllMedicines() {
        List<Medicine> medicines = new ArrayList<>();
        Cursor allMedicines = null;
        try {
            allMedicines = readAllFuture(DbOpenHelper.TABLE_MEDICINES, MEDICINES_COLUMN_NAMES, DbOpenHelper.COLUMN_M_ID).get();
            if (allMedicines != null) {
                if (allMedicines.moveToFirst()) {
                    while (!allMedicines.isAfterLast()) {
                        Medicine medicine = new Medicine();
                        medicine.setId(allMedicines.getInt(allMedicines.getColumnIndex(DbOpenHelper.COLUMN_M_ID)));
                        medicine.setName(allMedicines.getString(allMedicines.getColumnIndex(DbOpenHelper.COLUMN_M_NAME)));
                        medicine.setReason(allMedicines.getString(allMedicines.getColumnIndex(DbOpenHelper.COLUMN_M_REASON)));
                        medicine.setDosage(allMedicines.getString(allMedicines.getColumnIndex(DbOpenHelper.COLUMN_M_DOSAGE)));
                        medicine.setFrequency(allMedicines.getString(allMedicines.getColumnIndex(DbOpenHelper.COLUMN_M_FREQUENCY)));
                        medicine.setRenalDosage(allMedicines.getInt(allMedicines.getColumnIndex(DbOpenHelper.COLUMN_M_RENAL)));
                        medicine.setHepaticDosage(allMedicines.getInt(allMedicines.getColumnIndex(DbOpenHelper.COLUMN_M_HEPATIC)));
                        medicines.add(medicine);
                        allMedicines.moveToNext();
                    }
                }
            }
        } catch (InterruptedException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            if (allMedicines != null) {
                allMedicines.close();
            }
        }
        return medicines;
    }

    /**
     * Reads from db.
     */
    private Future<Cursor> readAllFuture(final String table, final String[] columns, final String order) {
        return executor.submit(new Callable<Cursor>() {
            @Override
            public Cursor call() throws Exception {
                return sqlite.query(table, columns,
                        null,
                        null,
                        null,
                        null,
                        order + " ASC"
                );
            }
        });
    }
}
