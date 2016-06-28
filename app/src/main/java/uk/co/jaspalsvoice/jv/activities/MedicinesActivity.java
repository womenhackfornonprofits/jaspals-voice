package uk.co.jaspalsvoice.jv.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import uk.co.jaspalsvoice.jv.JvApplication;
import uk.co.jaspalsvoice.jv.R;
import uk.co.jaspalsvoice.jv.models.Doctor;
import uk.co.jaspalsvoice.jv.models.Medicine;
import uk.co.jaspalsvoice.jv.views.custom.medicine.MedicineListAdapter;

/**
 * Created by Ana on 2/8/2016.
 */
public class MedicinesActivity extends BaseActivity {

    private JvApplication application;
    private List<Medicine> medicineList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_medicines);

        initValues();

        RecyclerView medicines = (RecyclerView) findViewById(R.id.medicines);
        medicines.setAdapter(new MedicineListAdapter(this, medicineList));
        medicines.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initValues() {
        application = (JvApplication) getApplicationContext();
        medicineList = application.getDbHelper().readAllMedicines();
    }

    private List<Medicine> getTestValues() {
        List<Medicine> list = new ArrayList<>();
        Medicine m1 = new Medicine();
        m1.setName("Medicine Name 1");
        m1.setDosage("Dosage 1");
        m1.setReason("Reason 1");
        m1.setFrequency("Frequency 1");
        list.add(m1);

        Medicine m2 = new Medicine();
        m2.setName("Medicine Name 2");
        m2.setDosage("Dosage 2");
        m2.setReason("Reason 2");
        m2.setFrequency("Frequency 2");
        list.add(m2);

        Medicine m3 = new Medicine();
        m3.setName("Medicine Name 3");
        m3.setDosage("Dosage 3");
        m3.setReason("Reason 3");
        m3.setFrequency("Frequency 3");
        list.add(m3);

        Medicine m4 = new Medicine();
        list.add(m4);

        return list;
    }

    public void saveMedicineData(String... medicineData){
        new Save().execute(medicineData);
    }

    private class Save extends AsyncTask<String, Void, uk.co.jaspalsvoice.jv.models.Medicine> {
        @Override
        protected uk.co.jaspalsvoice.jv.models.Medicine doInBackground(String... params) {
            List<uk.co.jaspalsvoice.jv.models.Medicine> medicines= new ArrayList<>();
            uk.co.jaspalsvoice.jv.models.Medicine medicine = new uk.co.jaspalsvoice.jv.models.Medicine();
            medicine.setName(params[0]);
            medicine.setDosage(params[1]);
            medicine.setReason(params[2]);
            medicine.setFrequency(params[3]);
            medicines.add(medicine);
            application.getDbHelper().insertOrReplaceMedicine(medicines);
            return medicine;
        }

        @Override
        protected void onPostExecute(uk.co.jaspalsvoice.jv.models.Medicine medicine) {
            if (medicine != null){
                showToast(getString(R.string.medicine_add_success_message), Toast.LENGTH_SHORT);
            }
        }
    }
}
