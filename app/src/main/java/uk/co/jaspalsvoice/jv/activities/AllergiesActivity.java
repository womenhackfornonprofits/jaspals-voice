package uk.co.jaspalsvoice.jv.activities;

import android.os.AsyncTask;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import uk.co.jaspalsvoice.jv.JvApplication;
import uk.co.jaspalsvoice.jv.R;
import uk.co.jaspalsvoice.jv.models.FoodAllergies;
import uk.co.jaspalsvoice.jv.models.MedicalAllergies;
import uk.co.jaspalsvoice.jv.models.EnvironmentalAllergies;
import uk.co.jaspalsvoice.jv.views.FoodAllergiesCardView;
import uk.co.jaspalsvoice.jv.views.MedicalAllergiesCardView;
import uk.co.jaspalsvoice.jv.views.EnvironmentalAllergiesCardView;

public class AllergiesActivity extends BaseActivity {

    private MedicalAllergiesCardView medicalAllergiesCardView;
    private int medicalAllergiesId;

    private FoodAllergiesCardView foodAllergiesCardView;
    private int foodAllergiesId;

    private EnvironmentalAllergiesCardView environmentalAllergiesCardView;
    private int environmentalAllergiesId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allergies);
        loadMedicalAllergiesView();
        loadFoodAllergiesView();
        loadEnvironmentalAllergiesView();
    }

    private void loadMedicalAllergiesView() {
        medicalAllergiesCardView = (MedicalAllergiesCardView)
                findViewById(R.id.medicalAllergiesCardView);
        new MedicalAllergiesRetrieve().execute();
    }

    private void loadFoodAllergiesView() {
        foodAllergiesCardView = (FoodAllergiesCardView)
                findViewById(R.id.foodAllergiesCardView);
        new FoodAllergiesRetrieve().execute();
    }

    private void loadEnvironmentalAllergiesView() {
        environmentalAllergiesCardView = (EnvironmentalAllergiesCardView)
                findViewById(R.id.environmentalAllergiesCardView);
        new EnvironmentalAllergiesRetrieve().execute();
    }






    public int getMedicalAllergiesId() {
        return medicalAllergiesId;
    }

    public void setMedicalAllergiesId(int medicalAllergiesId) {
        this.medicalAllergiesId= medicalAllergiesId;
    }

    public int getFoodAllergiesId() {
        return foodAllergiesId;
    }

    public void setFoodAllergiesId(int foodAllergiesId) {
        this.foodAllergiesId = foodAllergiesId;
    }

    public int getEnvironmentalAllergiesId() {
        return environmentalAllergiesId;
    }

    public void setEnvironmentalAllergiesId(int environmentalAllergiesId) {
        this.environmentalAllergiesId = environmentalAllergiesId;
    }


    private class MedicalAllergiesRetrieve extends AsyncTask<Void, Void, List<MedicalAllergies>> {
        @Override
        protected List<MedicalAllergies> doInBackground(Void... params) {
            return ((JvApplication) getApplication()).getDbHelper().readAllMedicalAllergies();
        }

        @Override
        protected void onPostExecute(List<MedicalAllergies> medicalAllergiess) {
            medicalAllergiesCardView.displayRecords(new ArrayList<MedicalAllergies>(medicalAllergiess));
        }
    }

    private class FoodAllergiesRetrieve extends AsyncTask<Void, Void, List<FoodAllergies>> {
        @Override
        protected List<FoodAllergies> doInBackground(Void... params) {
            return ((JvApplication) getApplication()).getDbHelper().readAllFoodAllergies();
        }

        @Override
        protected void onPostExecute(List<FoodAllergies> foodAllergiess) {
            foodAllergiesCardView.displayRecords(new ArrayList<FoodAllergies>(foodAllergiess));
        }
    }

    private class EnvironmentalAllergiesRetrieve extends AsyncTask<Void, Void, List<EnvironmentalAllergies>> {
        @Override
        protected List<EnvironmentalAllergies> doInBackground(Void... params) {
            return ((JvApplication) getApplication()).getDbHelper().readAllEnvironmentalAllergies();
        }

        @Override
        protected void onPostExecute(List<EnvironmentalAllergies> heights) {
            environmentalAllergiesCardView.displayRecords(new ArrayList<EnvironmentalAllergies>(heights));
        }
    }


}
