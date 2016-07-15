package uk.co.jaspalsvoice.jv.activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import uk.co.jaspalsvoice.jv.JvApplication;
import uk.co.jaspalsvoice.jv.R;
import uk.co.jaspalsvoice.jv.models.Diagnosis;
import uk.co.jaspalsvoice.jv.models.SurgicalHistory;
import uk.co.jaspalsvoice.jv.views.DiagnosisCardView;
import uk.co.jaspalsvoice.jv.views.SurgicalHistoryCardView;

public class DiagnosesActivity extends BaseActivity {

    private DiagnosisCardView diagnosisCardView;
    private int diagnosisId;

    private SurgicalHistoryCardView surgicalHistoryCardView;
    private int surgicalHistoryId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnoses);
        loadDiagnosisView();
        loadSurgicalHistoryView();
    }

    private void loadDiagnosisView() {
        diagnosisCardView = (DiagnosisCardView) findViewById(R.id.diagnosisCardView);
        new DiagnosisRetrieve().execute();
    }

    private void loadSurgicalHistoryView() {
        surgicalHistoryCardView = (SurgicalHistoryCardView)
                findViewById(R.id.surgicalHistoryCardView);
        new SurgicalHistoryRetrieve().execute();
    }

    public int getDiagnosisId() {
        return diagnosisId;
    }

    public void setDiagnosisId(int diagnosisId) {
        this.diagnosisId = diagnosisId;
    }

    public int getSurgicalHistoryId() {
        return surgicalHistoryId;
    }

    public void setSurgicalHistoryId(int surgicalHistoryId) {
        this.surgicalHistoryId = surgicalHistoryId;
    }

    private class DiagnosisRetrieve extends AsyncTask<Void, Void, List<Diagnosis>> {
        @Override
        protected List<Diagnosis> doInBackground(Void... params) {
            return ((JvApplication) getApplication()).getDbHelper().readAllDiagnosis();
        }

        @Override
        protected void onPostExecute(List<Diagnosis> diagnoses) {
            diagnosisCardView.displayRecords(new ArrayList<Diagnosis>(diagnoses));
        }
    }

    private class SurgicalHistoryRetrieve extends AsyncTask<Void, Void, List<SurgicalHistory>> {
        @Override
        protected List<SurgicalHistory> doInBackground(Void... params) {
            return ((JvApplication) getApplication()).getDbHelper().readAllSurgicalHistory();
        }

        @Override
        protected void onPostExecute(List<SurgicalHistory> diagnoses) {
            surgicalHistoryCardView.displayRecords(new ArrayList<SurgicalHistory>(diagnoses));
        }
    }

}
