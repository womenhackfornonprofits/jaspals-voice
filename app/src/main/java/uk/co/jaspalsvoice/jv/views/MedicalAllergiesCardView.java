package uk.co.jaspalsvoice.jv.views;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import uk.co.jaspalsvoice.jv.JvApplication;
import uk.co.jaspalsvoice.jv.R;
import uk.co.jaspalsvoice.jv.adapters.MedicalAllergiesAdapter;
import uk.co.jaspalsvoice.jv.models.MedicalAllergies;

/**
 * Created by Srinivas Klayani on 11 Jul 2016.
 */
public class MedicalAllergiesCardView extends CardView {

    private RecyclerView medicalAllergiesRecyclerView;
    private MedicalAllergiesAdapter adapter;
    private ImageView addMedicalAllergiesButton;
    private ArrayList<MedicalAllergies> medicalAllergies;
    private LinearLayout addNewMedicalAllergiesView;
    private LinearLayout saveLayout;
    private ImageView saveButton;
    private ImageView cancelButton;
    private ImageView editButton;
    private EditText medicalAllergiesEdittext;
    private EditText dateEdittext;
    private JvApplication application;
    private TextView titleView;
    private TextView blodPressureSubtitle, dateSubtitle;
    private TextView noContentView;


    public MedicalAllergiesCardView(Context context) {
        super(context);
        if (!isInEditMode())
            init(context);
    }

    public MedicalAllergiesCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            init(context);
    }

    public MedicalAllergiesCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode())
            init(context);
    }

    private void init(Context context) {
        initViews(context);
        initValues(context);
        attachListeners();
        setTitleViews();
        retrieveMedicalAllergiesData();
    }

    private void setTitleViews() {

    }

    private void attachListeners() {
        addMedicalAllergiesButton.setOnClickListener(mOnClickListener);
        saveButton.setOnClickListener(mOnClickListener);
        cancelButton.setOnClickListener(mOnClickListener);
    }

    private void initValues(Context context) {
        application = (JvApplication) context.getApplicationContext();
        medicalAllergies = new ArrayList<>();
        adapter = new MedicalAllergiesAdapter(context, medicalAllergies);
        medicalAllergiesRecyclerView.setAdapter(adapter);
    }

    private void initViews(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View root = inflater.inflate(R.layout.medical_allergies_card_view, this);
        medicalAllergiesRecyclerView = (RecyclerView) root.findViewById(R.id.medicalAllergiesRecyclerView);
        medicalAllergiesRecyclerView.setLayoutManager
                (new LinearLayoutManager(medicalAllergiesRecyclerView.getContext()));
        addMedicalAllergiesButton = (ImageView) root.findViewById(R.id.addMedicalAllergiesButton);
        addNewMedicalAllergiesView = (LinearLayout) root.findViewById(R.id.addNewMedicalAllergies);
        saveLayout = (LinearLayout) addNewMedicalAllergiesView.findViewById(R.id.saveLayout);
        saveButton = (ImageView) saveLayout.findViewById(R.id.saveButton);
        cancelButton = (ImageView) saveLayout.findViewById(R.id.cancelButton);
        medicalAllergiesEdittext = (EditText) addNewMedicalAllergiesView.
                findViewById(R.id.medicalAllergiesEdittext);
        dateEdittext = (EditText) addNewMedicalAllergiesView.findViewById(R.id.typeEditText);
        blodPressureSubtitle = (TextView)root.findViewById(R.id.medicalAllergiesTitle);
        dateSubtitle = (TextView)root.findViewById(R.id.bpDateTitle);
        titleView = (TextView)root.findViewById(R.id.title);


        titleView.setText(getResources().getString(R.string.medical_allergies_title));
        blodPressureSubtitle.setText(getResources().getString(R.string.medical_allergies_subtitle));
        dateSubtitle.setText(getResources().getString(R.string.type_title));
        noContentView = (TextView) findViewById(R.id.noContentView);
    }

    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.addMedicalAllergiesButton:
                    hideBPList();
                    showAddBPView();
                    break;

                case R.id.saveButton:
                    saveMedicalAllergiesData(medicalAllergiesEdittext.getText().toString(),
                            dateEdittext.getText().toString());
                    hideAddBPView();
                    showBPList();
                    break;

                case R.id.cancelButton:
                    hideAddBPView();
                    showBPList();
                    break;
            }

        }
    };

    private void saveMedicalAllergiesData(String medicalAllergies, String date) {
        new Save().execute(medicalAllergies, date);
    }

    private void showBPList() {
        medicalAllergiesRecyclerView.setVisibility(VISIBLE);
    }

    private void hideAddBPView() {
        addNewMedicalAllergiesView.setVisibility(GONE);
    }

    private void showAddBPView() {
        addNewMedicalAllergiesView.setVisibility(VISIBLE);
        saveLayout.setVisibility(VISIBLE);
    }

    private void hideBPList() {
        medicalAllergiesRecyclerView.setVisibility(GONE);
    }

    private void retrieveMedicalAllergiesData() {
        new MedicalAllergiesRetrieve().execute();
    }

    public void displayRecords(ArrayList<MedicalAllergies> medicalAllergies) {
        this.medicalAllergies.clear();
        this.medicalAllergies.addAll(medicalAllergies);
        adapter = new MedicalAllergiesAdapter(getContext(), this.medicalAllergies);
        medicalAllergiesRecyclerView.setAdapter(adapter);
        showBPList();
        adapter.notifyDataSetChanged();
        if (medicalAllergies.size() == 0){
            showNoContentView();
        } else {
            hideNoContentView();
        }
    }

    private void hideNoContentView() {
        noContentView.setVisibility(GONE);
    }

    private void showNoContentView() {
        noContentView.setVisibility(VISIBLE);
    }

    private class Save extends AsyncTask<String, Void, MedicalAllergies> {
        @Override
        protected MedicalAllergies doInBackground(String... params) {
            List<MedicalAllergies> medicalAllergies = new ArrayList<>();
            MedicalAllergies medicalAllergy= new MedicalAllergies();
            medicalAllergy.setMedicalAllergies(params[0]);
            medicalAllergy.setType(params[1]);
            medicalAllergies.add(medicalAllergy);
            application.getDbHelper().insertOrReplaceMedicalAllergies(medicalAllergies, false, 0);
            return medicalAllergy;
        }

        @Override
        protected void onPostExecute(MedicalAllergies medicalAllergies) {
            retrieveMedicalAllergiesData();
            hideAddBPView();
            showBPList();
        }
    }

    private class MedicalAllergiesRetrieve extends AsyncTask<Void, Void, List<MedicalAllergies>> {
        @Override
        protected List<MedicalAllergies> doInBackground(Void... params) {
            return application.getDbHelper().readAllMedicalAllergies();
        }

        @Override
        protected void onPostExecute(List<MedicalAllergies> medicalAllergies) {
            displayRecords(new ArrayList<MedicalAllergies>(medicalAllergies));
        }
    }

}
