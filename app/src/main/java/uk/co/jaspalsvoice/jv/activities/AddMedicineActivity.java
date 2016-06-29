package uk.co.jaspalsvoice.jv.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import uk.co.jaspalsvoice.jv.JvApplication;
import uk.co.jaspalsvoice.jv.R;
import uk.co.jaspalsvoice.jv.models.Medicine;
import uk.co.jaspalsvoice.jv.views.custom.medicine.MedicineListAdapter;

/**
 * Created by Srinivas Kalyani on 29 Jun 2016.
 */
public class AddMedicineActivity extends BaseActivity {

    private JvApplication application;
    private List<Medicine> medicineList;
    private EditText medicineNameText;
    private EditText dosagetext;
    private EditText reasonText;
    private EditText frequencyText;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_medicine);

        initViews();
        initValues();
        attachListeners();
    }

    private void attachListeners() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMedicineData(medicineNameText.getText().toString(),
                        dosagetext.getText().toString(), reasonText.getText().toString(),
                        frequencyText.getText().toString());
            }
        });
    }

    private void initValues() {
        application = (JvApplication) getApplicationContext();
        medicineList = application.getDbHelper().readAllMedicines();
    }

    private void initViews() {
        medicineNameText = (EditText)findViewById(R.id.edit_value_name);
        dosagetext = (EditText)findViewById(R.id.edit_value_dosage);
        reasonText = (EditText)findViewById(R.id.edit_value_reason);
        frequencyText = (EditText)findViewById(R.id.edit_value_frequency);
        saveButton = (Button) findViewById(R.id.saveButton);
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
            application.getDbHelper().insertOrReplaceMedicine(medicines, false, 0);
            return medicine;
        }

        @Override
        protected void onPostExecute(uk.co.jaspalsvoice.jv.models.Medicine medicine) {
            if (medicine != null){
                showToast(getString(R.string.medicine_add_success_message), Toast.LENGTH_SHORT);
            }
        }
    }

    @Override
    public void onBackPressed() {
        launchMedicinesActivity();
    }

    private void launchMedicinesActivity() {
        Intent intent = new Intent(this, MedicinesActivity.class);
        startActivity(intent);
        finish();
    }
}
