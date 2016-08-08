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

import uk.co.jaspalsvoice.jv.DateUtils;
import uk.co.jaspalsvoice.jv.JvApplication;
import uk.co.jaspalsvoice.jv.R;
import uk.co.jaspalsvoice.jv.activities.DiagnosesActivity;
import uk.co.jaspalsvoice.jv.adapters.DiagnosisAdapter;
import uk.co.jaspalsvoice.jv.models.Diagnosis;

/**
 * Created by Srinivas Klayani on 11 Jul 2016.
 */
public class DiagnosisCardView extends CardView {

    private RecyclerView diagnosisRecyclerView;
    private DiagnosisAdapter adapter;
    private ImageView addDiagnosisButton;
    private ArrayList<Diagnosis> diagnosis;
    private LinearLayout addNewDiagnosisView;
    private LinearLayout saveLayout;
    private ImageView saveButton;
    private ImageView cancelButton;
    private ImageView editButton;
    private EditText diagnosisEdittext;
    private EditText dateEdittext;
    private JvApplication application;
    private TextView titleView;
    private TextView noContentView;


    public DiagnosisCardView(Context context) {
        super(context);
        if (!isInEditMode())
            init(context);
    }

    public DiagnosisCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            init(context);
    }

    public DiagnosisCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode())
            init(context);
    }

    private void init(Context context) {
        initViews(context);
        initValues(context);
        attachListeners();
        setTitleViews();
        retrieveDiagnosisData();
    }

    private void setTitleViews() {

    }

    private void attachListeners() {
        addDiagnosisButton.setOnClickListener(mOnClickListener);
        saveButton.setOnClickListener(mOnClickListener);
        cancelButton.setOnClickListener(mOnClickListener);
        dateEdittext.setOnClickListener(mOnClickListener);

    }

    private void initValues(Context context) {
        application = (JvApplication) context.getApplicationContext();
        diagnosis = new ArrayList<>();
        adapter = new DiagnosisAdapter(context, diagnosis);
        diagnosisRecyclerView.setAdapter(adapter);
    }

    private void initViews(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View root = inflater.inflate(R.layout.diagnosis_card_view, this);
        diagnosisRecyclerView = (RecyclerView) root.findViewById(R.id.diagnosisRecyclerView);
        diagnosisRecyclerView.setLayoutManager
                (new LinearLayoutManager(diagnosisRecyclerView.getContext()));
        addDiagnosisButton = (ImageView) root.findViewById(R.id.addDiagnosisButton);
        addNewDiagnosisView = (LinearLayout) root.findViewById(R.id.addNewDiagnosis);
        addNewDiagnosisView.setVisibility(GONE);
        saveLayout = (LinearLayout) addNewDiagnosisView.findViewById(R.id.saveLayout);
        saveButton = (ImageView) saveLayout.findViewById(R.id.saveButton);
        cancelButton = (ImageView) saveLayout.findViewById(R.id.cancelButton);

        diagnosisEdittext = (EditText) addNewDiagnosisView.findViewById(R.id.editFirstField);
        dateEdittext = (EditText) addNewDiagnosisView.findViewById(R.id.editSecondField);
        dateEdittext.setFocusable(false);
        LinearLayout bpTitleLayout = (LinearLayout) root.findViewById(R.id.bpTitleLayout);
        TextView blodPressureSubtitle = (TextView)bpTitleLayout.findViewById(R.id.headingFirstField);
        TextView dateSubtitle = (TextView)root.findViewById(R.id.headingSecondField);

        titleView = (TextView)root.findViewById(R.id.title);

        titleView.setText(getResources().getString(R.string.diagnosis_title));
        blodPressureSubtitle.setText(getResources().getString(R.string.diagnosis_title));
        dateSubtitle.setText(getResources().getString(R.string.date_title));
        noContentView = (TextView) findViewById(R.id.noContentView);
    }

    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.addDiagnosisButton:
                    diagnosisEdittext.setText("");
                    dateEdittext.setText("");
                    hideNoContentView();
                    hideBPList();
                    showAddBPView();
                    break;

                case R.id.saveButton:
                    saveDiagnosisData(diagnosisEdittext.getText().toString(),
                            dateEdittext.getText().toString());
                    hideAddBPView();
                    showBPList();
                    break;

                case R.id.cancelButton:
                    hideAddBPView();
                    showBPList();
                    break;

                case R.id.editSecondField:
                    DateUtils.showDatePicker(dateEdittext,((DiagnosesActivity)getContext()));
                    break;
            }

        }
    };

    private void saveDiagnosisData(String diagnosis, String date) {
        new Save().execute(diagnosis, date);
    }

    private void showBPList() {
        diagnosisRecyclerView.setVisibility(VISIBLE);
    }

    private void hideAddBPView() {
        addNewDiagnosisView.setVisibility(GONE);
    }

    private void showAddBPView() {
        addNewDiagnosisView.setVisibility(VISIBLE);
        saveLayout.setVisibility(VISIBLE);
    }

    private void hideBPList() {
        diagnosisRecyclerView.setVisibility(GONE);
    }

    private void retrieveDiagnosisData() {
        new DiagnosisRetrieve().execute();
    }

    public void displayRecords(ArrayList<Diagnosis> diagnosis) {
        this.diagnosis.clear();
        this.diagnosis.addAll(diagnosis);
        adapter = new DiagnosisAdapter(getContext(), this.diagnosis);
        diagnosisRecyclerView.setAdapter(adapter);
        showBPList();
        adapter.notifyDataSetChanged();
        if (diagnosis.size() == 0){
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

    private class Save extends AsyncTask<String, Void, Diagnosis> {
        @Override
        protected Diagnosis doInBackground(String... params) {
            List<Diagnosis> diagnoses = new ArrayList<>();
            Diagnosis diagnosis = new Diagnosis();
            diagnosis.setDiagnosis(params[0]);
            diagnosis.setDate(params[1]);
            diagnoses.add(diagnosis);
            application.getDbHelper().insertOrReplaceDiagnosis(diagnoses, false, 0);
            return diagnosis;
        }

        @Override
        protected void onPostExecute(Diagnosis diagnosis) {
            retrieveDiagnosisData();
            hideAddBPView();
            showBPList();
        }
    }

    private class DiagnosisRetrieve extends AsyncTask<Void, Void, List<Diagnosis>> {
        @Override
        protected List<Diagnosis> doInBackground(Void... params) {
            return application.getDbHelper().readAllDiagnosis();
        }

        @Override
        protected void onPostExecute(List<Diagnosis> diagnosis) {
            displayRecords(new ArrayList<Diagnosis>(diagnosis));
        }
    }

}
