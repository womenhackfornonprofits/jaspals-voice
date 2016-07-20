package uk.co.jaspalsvoice.jv.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;
import java.util.Map;

import uk.co.jaspalsvoice.jv.JvApplication;
import uk.co.jaspalsvoice.jv.R;
import uk.co.jaspalsvoice.jv.models.Doctor;
import uk.co.jaspalsvoice.jv.models.DoctorType;
import uk.co.jaspalsvoice.jv.views.custom.ConsultantCardView;
import uk.co.jaspalsvoice.jv.views.custom.MedicalContactCardView;
import uk.co.jaspalsvoice.jv.views.custom.TeamMemberCardView;

/**
 * Created by Ana on 2/8/2016.
 */
public class GpActivity extends BaseActivity {

    private LinearLayout containerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        initViews();

        setContentView(R.layout.activity_gp);

        new MedicalTeam().execute();
        new MedicalTeamMember().execute();
    }

    private void initViews() {
        containerLayout = (LinearLayout) findViewById(R.id.containerLayout);
    }

    private class MedicalTeam extends AsyncTask<Void, Void, Map<String, Doctor>> {
        @Override
        protected Map<String, Doctor> doInBackground(Void... params) {
            return ((JvApplication) getApplication()).getDbHelper().readAllDoctors();
        }

        @Override
        protected void onPostExecute(Map<String, Doctor> doctors) {
            setupUI(doctors);
        }
    }

    private class MedicalTeamMember extends AsyncTask<Void, Void, List<Doctor>> {
        @Override
        protected List<Doctor> doInBackground(Void... params) {
            return ((JvApplication) getApplication()).getDbHelper().readAllTeamMembers();
        }

        @Override
        protected void onPostExecute(List<Doctor> doctors) {
            setupTeamMemberUI(doctors);
        }
    }


    private void setupUI(Map<String, Doctor> doctors) {
        initViews();
        MedicalContactCardView gp = (MedicalContactCardView) findViewById(R.id.gp_name);
        gp.setTitle(getString(R.string.gp_title));
        gp.setLabel1View(getString(R.string.gp_medical_team_name));
        gp.setLabel2View(getString(R.string.gp_medical_team_address));
        gp.setLabel3View(getString(R.string.gp_medical_team_phone));
        gp.setLabel4View(getString(R.string.gp_medical_team_email));
        gp.setLabelFView(getString(R.string.fax_label));
        setupDoctor(doctors, DoctorType.GP.name(), gp);

        ConsultantCardView mndContact = (ConsultantCardView) findViewById(R.id.mnd_contact);
        mndContact.setTitle(getString(R.string.mnd_contact_title));
        mndContact.setLabel1View(getString(R.string.gp_medical_team_name));
       /* mndContact.setLabel2View(getString(R.string.gp_consultant_address));
        mndContact.setLabel3View(getString(R.string.gp_consultant_phone));

*/
        mndContact.setLabel4View(getString(R.string.gp_consultant_email));
        mndContact.setLabel2hView(getString(R.string.gp_hospital_name));
        mndContact.setLabel3hView(getString(R.string.gp_hospital_address));
        mndContact.setLabel4hView(getString(R.string.gp_hospital_phone));
        setupConsultant(doctors, DoctorType.MND_CONTACT.name(), mndContact);

        MedicalContactCardView physiotherapist = (MedicalContactCardView) findViewById(R.id.physiotherapist);
        physiotherapist.setTitle(getString(R.string.physiotherapist_title));
        physiotherapist.setLabel1View(getString(R.string.gp_medical_team_name));
        physiotherapist.setLabel2View(getString(R.string.gp_medical_team_address));
        physiotherapist.setLabel3View(getString(R.string.gp_medical_team_phone));
        physiotherapist.setLabelFView(getString(R.string.fax_label));
        physiotherapist.setLabel4View(getString(R.string.gp_medical_team_email));
        setupDoctor(doctors, DoctorType.PHYSIOTHERAPIST.name(), physiotherapist);

        MedicalContactCardView therapist = (MedicalContactCardView) findViewById(R.id.therapist);
        therapist.setTitle(getString(R.string.therapist_title));
        therapist.setLabel1View(getString(R.string.gp_medical_team_name));
        therapist.setLabel2View(getString(R.string.gp_medical_team_address));
        therapist.setLabel3View(getString(R.string.gp_medical_team_phone));
        therapist.setLabelFView(getString(R.string.fax_label));
        therapist.setLabel4View(getString(R.string.gp_medical_team_email));
        setupDoctor(doctors, DoctorType.SPEECH_AND_LANGUAGE_THERAPIST.name(), therapist);

        MedicalContactCardView carer = (MedicalContactCardView) findViewById(R.id.carer);
        carer.setTitle(getString(R.string.carer_title));
        carer.setLabel1View(getString(R.string.gp_medical_team_name));
        carer.setLabel2View(getString(R.string.gp_medical_team_address));
        carer.setLabel3View(getString(R.string.gp_medical_team_phone));
        carer.setLabelFView(getString(R.string.fax_label));

        carer.setLabel4View(getString(R.string.gp_medical_team_email));
        setupDoctor(doctors, DoctorType.CARER.name(), carer);

        MedicalContactCardView other = (MedicalContactCardView) findViewById(R.id.other_medical);
        other.setTitle(getString(R.string.other_medical_title));
        other.setLabel1View(getString(R.string.gp_medical_team_name));
        other.setLabel2View(getString(R.string.gp_medical_team_address));
        other.setLabel3View(getString(R.string.gp_medical_team_phone));
        other.setLabel4View(getString(R.string.gp_medical_team_email));
        other.setLabelFView(getString(R.string.fax_label));
        setupDoctor(doctors, DoctorType.OTHER.name(), other);

        /*if (doctors.size() > 6){
            for (int position = 6; position < doctors.size(); position++){
                MedicalContactCardView sampleCard = new MedicalContactCardView(this);
                sampleCard.setPadding((int) getResources().getDimension(R.dimen.margin_small),
                        (int) getResources().getDimension(R.dimen.margin_small),
                        (int) getResources().getDimension(R.dimen.margin_small),
                        (int) getResources().getDimension(R.dimen.margin_small));
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) sampleCard.getLayoutParams();
                layoutParams.setMargins((int) getResources().getDimension(R.dimen.activity_horizontal_margin),
                        (int) getResources().getDimension(R.dimen.activity_vertical_margin),
                        (int) getResources().getDimension(R.dimen.activity_horizontal_margin),
                        (int) getResources().getDimension(R.dimen.activity_vertical_margin));
                sampleCard.setTitle(getString(doctors.geti));
                sampleCard.setLabel1View(getString(R.string.gp_medical_team_name));
                sampleCard.setLabel2View(getString(R.string.gp_medical_team_contact_details));
                setupDoctor(doctors, DoctorType.OTHER.name(), other);
                containerLayout.addView(sampleCard);
            }
        }*/

       /* MedicalContactCardView sampleCard = new MedicalContactCardView(this);
        sampleCard.setPadding((int)getResources().getDimension(R.dimen.margin_small),
                (int)getResources().getDimension(R.dimen.margin_small),
                (int)getResources().getDimension(R.dimen.margin_small),
                (int)getResources().getDimension(R.dimen.margin_small));
       *//* ViewGroup.MarginLayoutParams layoutParams  = (ViewGroup.MarginLayoutParams) sampleCard.getLayoutParams();
        layoutParams.setMargins((int)getResources().getDimension(R.dimen.activity_horizontal_margin),
                (int)getResources().getDimension(R.dimen.activity_vertical_margin),
                (int)getResources().getDimension(R.dimen.activity_horizontal_margin),
                (int)getResources().getDimension(R.dimen.activity_vertical_margin));*//*
        sampleCard.setTitle(getString(R.string.other_medical_title));
        sampleCard.setLabel1View(getString(R.string.gp_medical_team_name));
        sampleCard.setLabel2View(getString(R.string.gp_medical_team_contact_details));
        setupDoctor(doctors, DoctorType.OTHER.name(), other);
        containerLayout.addView(sampleCard);*/

    }

    private void setupTeamMemberUI(List<Doctor> doctors) {
        initViews();
        for (Doctor doctor : doctors){
            TeamMemberCardView sampleCard = new TeamMemberCardView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            int margin = (int)getResources().getDimension(R.dimen.activity_horizontal_margin);
            params.setMargins(0, 0, 0, margin);
            sampleCard.setLayoutParams(params);
            sampleCard.setMaxCardElevation(getResources().getDimension(R.dimen.elevation));

           /* sampleCard.setPadding((int) getResources().getDimension(R.dimen.margin_small),
                    (int) getResources().getDimension(R.dimen.margin_small),
                    (int) getResources().getDimension(R.dimen.margin_small),
                    (int) getResources().getDimension(R.dimen.margin_small));*/
            sampleCard.setTitle(doctor.getType());
            sampleCard.setLabel1View(getString(R.string.gp_medical_team_name));
            sampleCard.setLabel2View(getString(R.string.gp_medical_team_address));
            sampleCard.setLabel3View(getString(R.string.gp_medical_team_phone));
            sampleCard.setLabel4View(getString(R.string.gp_medical_team_email));
            setupTeamMember(doctor, sampleCard);
            containerLayout.addView(sampleCard);
        }
    }

    private void setupConsultant(Map<String, Doctor> doctors, String type, ConsultantCardView view) {
        Doctor doctor = doctors.get(type);
        if (doctor != null) {
            view.setText1(doctor.getName());
            view.setText2(doctor.getAddress());
            view.setText3(doctor.getPhone());
            view.setText4(doctor.getEmail());

            view.setTexth2(doctor.getHospitalName());
            view.setTexth3(doctor.getHospitalAddress());
            view.setTexth4(doctor.getHospitalPhone());
            view.setTextf(doctor.getFax());

        }
        view.setDoctorType(type);
    }


    private void setupDoctor(Map<String, Doctor> doctors, String type, MedicalContactCardView view) {
        Doctor doctor = doctors.get(type);
        if (doctor != null) {
            view.setText1(doctor.getName());
            view.setText2(doctor.getAddress());
            view.setText3(doctor.getPhone());
            view.setText4(doctor.getEmail());
            view.setTextf(doctor.getFax());
        }
        view.setDoctorType(type);
    }

    private void setupTeamMember(Doctor doctor, TeamMemberCardView view) {
        if (doctor != null) {
            view.setText1(doctor.getName());
            view.setText2(doctor.getAddress());
            view.setText3(doctor.getPhone());
            view.setText4(doctor.getEmail());
            view.setDoctorType(doctor.getType());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.medicine_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.addMedicine:
                addTeamMember();
                break;
        }
//        return true;
        return super.onOptionsItemSelected(item);
    }

    private void addTeamMember() {
        Intent intent = new Intent(this, AddTeamMemberActivity.class);
        startActivity(intent);
    }
}
