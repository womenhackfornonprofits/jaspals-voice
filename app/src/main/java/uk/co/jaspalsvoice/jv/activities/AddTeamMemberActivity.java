package uk.co.jaspalsvoice.jv.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import uk.co.jaspalsvoice.jv.JvApplication;
import uk.co.jaspalsvoice.jv.R;
import uk.co.jaspalsvoice.jv.models.Doctor;

public class AddTeamMemberActivity extends BaseActivity {

    private JvApplication application;
    private List<Doctor> doctors;
    private EditText typeEdittext;
    private EditText nameEdittext;
    private EditText addressEdittext;
    private EditText phoneNumberEdittext;
    private EditText emailEdittext;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_team_member);

        initViews();
        initValues();
        attachListeners();
    }

    private void attachListeners() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                

                    
                    saveMedicineData(typeEdittext.getText().toString(),
                            nameEdittext.getText().toString(), addressEdittext.getText().toString(),
                            phoneNumberEdittext.getText().toString(), emailEdittext.getText().toString());


                    

            }
        });
    }

    private void showValidationToast() {
        Toast.makeText(this,getResources().getString(R.string.add_gp_validation), 
                Toast.LENGTH_LONG).show();
    }

    private void initValues() {
        application = (JvApplication) getApplicationContext();
    }

    private void initViews() {
        typeEdittext = (EditText) findViewById(R.id.typeEdittext);
        nameEdittext = (EditText) findViewById(R.id.nameEdittext);
        addressEdittext = (EditText) findViewById(R.id.addressEdittext);
        phoneNumberEdittext = (EditText) findViewById(R.id.phoneEdittext);
        emailEdittext = (EditText) findViewById(R.id.emailEdittext);
        saveButton = (Button) findViewById(R.id.saveButton);
    }

    public void saveMedicineData(String... doctorData) {
        if (!TextUtils.isEmpty(typeEdittext.getText().toString()) &&
                !TextUtils.isEmpty(nameEdittext.getText().toString()) &&
                !TextUtils.isEmpty(addressEdittext.getText().toString()) &&
                !TextUtils.isEmpty(phoneNumberEdittext.getText().toString()) &&
                !TextUtils.isEmpty(emailEdittext.getText().toString())) {
            new Save().execute(doctorData);
            typeEdittext.setText("");
            nameEdittext.setText("");
            addressEdittext.setText("");
            phoneNumberEdittext.setText("");
            emailEdittext.setText("");
        } else {
            showValidationToast();
        }
    }

    private class Save extends AsyncTask<String, Void, Doctor> {
        @Override
        protected Doctor doInBackground(String... params) {
            List<Doctor> doctors = new ArrayList<>();
            Doctor doctor = new Doctor();
            doctor.setType(params[0]);
            doctor.setName(params[1]);
            doctor.setAddress(params[2]);
            doctor.setPhone(params[3]);
            doctor.setEmail(params[4]);
            doctors.add(doctor);
            application.getDbHelper().insertOrReplaceTeamMember(doctors);
            return doctor;
        }

        @Override
        protected void onPostExecute(Doctor doctor) {
            showToast("Team member added", Toast.LENGTH_SHORT);
            launchGpActivity();
        }
    }

    @Override
    public void onBackPressed() {
        launchGpActivity();
    }

    private void launchGpActivity() {
        Intent intent = new Intent(this, GpActivity.class);
        startActivity(intent);
        finish();
    }
}
