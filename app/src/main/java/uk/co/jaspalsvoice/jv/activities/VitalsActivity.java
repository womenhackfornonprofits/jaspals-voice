package uk.co.jaspalsvoice.jv.activities;

import android.os.AsyncTask;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import uk.co.jaspalsvoice.jv.JvApplication;
import uk.co.jaspalsvoice.jv.R;
import uk.co.jaspalsvoice.jv.models.VitalsBloodGlucose;
import uk.co.jaspalsvoice.jv.models.VitalsBloodPressure;
import uk.co.jaspalsvoice.jv.models.VitalsHeight;
import uk.co.jaspalsvoice.jv.models.VitalsOxygenLevel;
import uk.co.jaspalsvoice.jv.models.VitalsWeight;
import uk.co.jaspalsvoice.jv.views.VitalsBloodGlucoseCardView;
import uk.co.jaspalsvoice.jv.views.VitalsBloodPressureCardView;
import uk.co.jaspalsvoice.jv.views.VitalsHeightCardView;
import uk.co.jaspalsvoice.jv.views.VitalsOxygenLevelCardView;
import uk.co.jaspalsvoice.jv.views.VitalsWeightCardView;

public class VitalsActivity extends BaseActivity {

    private VitalsBloodPressureCardView bloodPressureCardView;
    private int bloodPressureId;

    private VitalsBloodGlucoseCardView bloodGlucoseCardView;
    private int bloodGlucoseId;

    private VitalsOxygenLevelCardView oxygenLevelCardView;
    private int oxygenLevelId;

    private VitalsHeightCardView heightCardView;
    private int heightId;

    private VitalsWeightCardView weightCardView;
    private int weightId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vitals);
        loadBloodPressureView();
        loadBloodGlucoseView();
    }

    private void loadBloodPressureView() {
        bloodPressureCardView = (VitalsBloodPressureCardView)
                findViewById(R.id.bloodPressureCardView);
        new BloodPressure().execute();
    }

    private void loadBloodGlucoseView() {
        bloodGlucoseCardView = (VitalsBloodGlucoseCardView)
                findViewById(R.id.bloodGlucoseCardView);
        new BloodGlucose().execute();
    }

    private void loadOxygenLevelView() {
        oxygenLevelCardView = (VitalsOxygenLevelCardView)
                findViewById(R.id.oxygenLevelCardView);
        new OxygenLevel().execute();
    }

    private void loadHeightView() {
        heightCardView = (VitalsHeightCardView)
                findViewById(R.id.heightCardView);
        new Height().execute();
    }

    private void loadWeightView() {
        weightCardView = (VitalsWeightCardView)
                findViewById(R.id.weightCardView);
        new Weight().execute();
    }

    public int getBloodPressureId() {
        return bloodPressureId;
    }

    public void setBloodPressureId(int bloodPressureId) {
        this.bloodPressureId= bloodPressureId;
    }

    public int getBloodGlucoseId() {
        return bloodGlucoseId;
    }

    public void setBloodGlucoseId(int bloodGlucoseId) {
        this.bloodGlucoseId = bloodGlucoseId;
    }

    public int getOxygenLevelId() {
        return oxygenLevelId;
    }

    public void setOxygenLevelId(int oxygenLevelId) {
        this.oxygenLevelId = oxygenLevelId;
    }

    public int getHeightId() {
        return heightId;
    }

    public int getWeightId() {
        return weightId;
    }

    public void setHeightId(int heightId) {
        this.heightId = heightId;
    }

    public void setWeightId(int weightId) {
        this.weightId = weightId;
    }

    private class BloodPressure extends AsyncTask<Void, Void, List<VitalsBloodPressure>> {
        @Override
        protected List<VitalsBloodPressure> doInBackground(Void... params) {
            return ((JvApplication) getApplication()).getDbHelper().readAllBloodPressures();
        }

        @Override
        protected void onPostExecute(List<VitalsBloodPressure> bloodPressures) {
            bloodPressureCardView.displayRecords(new ArrayList<VitalsBloodPressure>(bloodPressures));
        }
    }

    private class BloodGlucose extends AsyncTask<Void, Void, List<VitalsBloodGlucose>> {
        @Override
        protected List<VitalsBloodGlucose> doInBackground(Void... params) {
            return ((JvApplication) getApplication()).getDbHelper().readAllBloodGlucoses();
        }

        @Override
        protected void onPostExecute(List<VitalsBloodGlucose> bloodGlucoses) {
            bloodGlucoseCardView.displayRecords(new ArrayList<VitalsBloodGlucose>(bloodGlucoses));
        }
    }

    private class OxygenLevel extends AsyncTask<Void, Void, List<VitalsOxygenLevel>> {
        @Override
        protected List<VitalsOxygenLevel> doInBackground(Void... params) {
            return ((JvApplication) getApplication()).getDbHelper().readAllOxygenLevels();
        }

        @Override
        protected void onPostExecute(List<VitalsOxygenLevel> oxygenLevels) {
            oxygenLevelCardView.displayRecords(new ArrayList<VitalsOxygenLevel>(oxygenLevels));
        }
    }

    private class Height extends AsyncTask<Void, Void, List<VitalsHeight>> {
        @Override
        protected List<VitalsHeight> doInBackground(Void... params) {
            return ((JvApplication) getApplication()).getDbHelper().readAllHeights();
        }

        @Override
        protected void onPostExecute(List<VitalsHeight> heights) {
            heightCardView.displayRecords(new ArrayList<VitalsHeight>(heights));
        }
    }

    private class Weight extends AsyncTask<Void, Void, List<VitalsWeight>> {
        @Override
        protected List<VitalsWeight> doInBackground(Void... params) {
            return ((JvApplication) getApplication()).getDbHelper().readAllWeights();
        }

        @Override
        protected void onPostExecute(List<VitalsWeight> weights) {
            weightCardView.displayRecords(new ArrayList<VitalsWeight>(weights));
        }
    }

}
