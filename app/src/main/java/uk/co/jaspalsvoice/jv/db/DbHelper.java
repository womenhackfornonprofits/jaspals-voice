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

import uk.co.jaspalsvoice.jv.models.Doctor;
import uk.co.jaspalsvoice.jv.models.Medicine;
import uk.co.jaspalsvoice.jv.models.VitalsBloodPressure;


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
            DbOpenHelper.COLUMN_M_FREQUENCY};

    private static final String[] BLOOD_PRESSURE_COLUMN_NAMES = new String[]{
            DbOpenHelper.COLUMN_B_UUID,
            DbOpenHelper.COLUMN_B_ID,
            DbOpenHelper.COLUMN_B_BLOODPRESSURE,
            DbOpenHelper.COLUMN_B_DATE,
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
     * Method to insert a blood pressure entry
     *
     * @param bloodPressures List of doctors to be inserted in the db
     * @return the number of rows inserted
     */
    public Future<Long> insertOrReplaceBloodPressure(final List<VitalsBloodPressure> bloodPressures) {
        return executor.submit(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                long insertedRows = 0;
                try {
                    sqlite.beginTransaction();
                    for (VitalsBloodPressure bloodPressure : bloodPressures) {
                        if (sqlite.insertWithOnConflict(DbOpenHelper.TABLE_BLOOD_PRESSURE, null, bloodPressure.toContentValues(),
                                SQLiteDatabase.CONFLICT_REPLACE) >= 0) {
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
     * Gets from db a list containing all doctors.
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
