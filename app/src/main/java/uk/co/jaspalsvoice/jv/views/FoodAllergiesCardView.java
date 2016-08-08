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
import uk.co.jaspalsvoice.jv.adapters.FoodAllergiesAdapter;
import uk.co.jaspalsvoice.jv.models.FoodAllergies;

/**
 * Created by Srinivas Kalyani on 11 Jul 2016.
 */
public class FoodAllergiesCardView extends CardView {

    private RecyclerView foodAllergiesRecyclerView;
    private FoodAllergiesAdapter adapter;
    private ImageView addFoodAllergiesButton;
    private ArrayList<FoodAllergies> foodAllergies;
    private LinearLayout addNewFoodAllergiesView;
    private LinearLayout saveLayout;
    private ImageView saveButton;
    private ImageView cancelButton;
    private ImageView editButton;
    private EditText foodAllergiesEdittext;
    private EditText dateEdittext;
    private JvApplication application;
    private TextView titleView;
    private TextView noContentView;


    public FoodAllergiesCardView(Context context) {
        super(context);
        if (!isInEditMode())
            init(context);
    }

    public FoodAllergiesCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            init(context);
    }

    public FoodAllergiesCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode())
            init(context);
    }

    private void init(Context context) {
        initViews(context);
        initValues(context);
        attachListeners();
        setTitleViews();
        retrieveFoodAllergiesData();
    }

    private void setTitleViews() {

    }

    private void attachListeners() {
        addFoodAllergiesButton.setOnClickListener(mOnClickListener);
        saveButton.setOnClickListener(mOnClickListener);
        cancelButton.setOnClickListener(mOnClickListener);
    }

    private void initValues(Context context) {
        application = (JvApplication) context.getApplicationContext();
        foodAllergies = new ArrayList<>();
        adapter = new FoodAllergiesAdapter(context, foodAllergies);
        foodAllergiesRecyclerView.setAdapter(adapter);
    }

    private void initViews(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View root = inflater.inflate(R.layout.food_allergies_card_view, this);
        foodAllergiesRecyclerView = (RecyclerView) root.findViewById(R.id.foodAllergiesRecyclerView);
        foodAllergiesRecyclerView.setLayoutManager
                (new LinearLayoutManager(foodAllergiesRecyclerView.getContext()));
        addFoodAllergiesButton = (ImageView) root.findViewById(R.id.addFoodAllergiesButton);
        addNewFoodAllergiesView = (LinearLayout) root.findViewById(R.id.addNewFoodAllergies);
        addNewFoodAllergiesView.setVisibility(GONE);
        saveLayout = (LinearLayout) addNewFoodAllergiesView.findViewById(R.id.saveLayout);
        saveButton = (ImageView) saveLayout.findViewById(R.id.saveButton);
        cancelButton = (ImageView) saveLayout.findViewById(R.id.cancelButton);
        foodAllergiesEdittext = (EditText) addNewFoodAllergiesView.
                findViewById(R.id.editFirstField);
        dateEdittext = (EditText) addNewFoodAllergiesView.findViewById(R.id.editSecondField);
        LinearLayout bpTitleLayout = (LinearLayout) root.findViewById(R.id.bpTitleLayout);
        TextView blodPressureSubtitle = (TextView)bpTitleLayout.findViewById(R.id.headingFirstField);
        TextView dateSubtitle = (TextView)root.findViewById(R.id.headingSecondField);
        titleView = (TextView)root.findViewById(R.id.title);
        titleView.setText(getResources().getString(R.string.food_allergies_title));
        blodPressureSubtitle.setText(getResources().getString(R.string.food_allergies_subtitle));
        dateSubtitle.setText(getResources().getString(R.string.type_title));
        noContentView = (TextView) findViewById(R.id.noContentView);
    }

    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.addFoodAllergiesButton:
                    foodAllergiesEdittext.setText("");
                    dateEdittext.setText("");
                    hideNoContentView();
                    hideBPList();
                    showAddBPView();
                    break;

                case R.id.saveButton:
                    saveFoodAllergiesData(foodAllergiesEdittext.getText().toString(),
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

    private void saveFoodAllergiesData(String foodAllergies, String date) {
        new Save().execute(foodAllergies, date);
    }

    private void showBPList() {
        foodAllergiesRecyclerView.setVisibility(VISIBLE);
    }

    private void hideAddBPView() {
        addNewFoodAllergiesView.setVisibility(GONE);
    }

    private void showAddBPView() {
        addNewFoodAllergiesView.setVisibility(VISIBLE);
        saveLayout.setVisibility(VISIBLE);
    }

    private void hideBPList() {
        foodAllergiesRecyclerView.setVisibility(GONE);
    }

    private void retrieveFoodAllergiesData() {
        new FoodAllergiesRetrieve().execute();
    }

    public void displayRecords(ArrayList<FoodAllergies> foodAllergies) {
        this.foodAllergies.clear();
        this.foodAllergies.addAll(foodAllergies);
        adapter = new FoodAllergiesAdapter(getContext(), this.foodAllergies);
        foodAllergiesRecyclerView.setAdapter(adapter);
        showBPList();
        adapter.notifyDataSetChanged();
        if (foodAllergies.size() == 0){
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

    private class Save extends AsyncTask<String, Void, FoodAllergies> {
        @Override
        protected FoodAllergies doInBackground(String... params) {
            List<FoodAllergies> foodAllergies = new ArrayList<>();
            FoodAllergies foodAllergy= new FoodAllergies();
            foodAllergy.setFoodAllergies(params[0]);
            foodAllergy.setType(params[1]);
            foodAllergies.add(foodAllergy);
            application.getDbHelper().insertOrReplaceFoodAllergies(foodAllergies, false, 0);
            return foodAllergy;
        }

        @Override
        protected void onPostExecute(FoodAllergies foodAllergies) {
            retrieveFoodAllergiesData();
            hideAddBPView();
            showBPList();
        }
    }

    private class FoodAllergiesRetrieve extends AsyncTask<Void, Void, List<FoodAllergies>> {
        @Override
        protected List<FoodAllergies> doInBackground(Void... params) {
            return application.getDbHelper().readAllFoodAllergies();
        }

        @Override
        protected void onPostExecute(List<FoodAllergies> foodAllergies) {
            displayRecords(new ArrayList<FoodAllergies>(foodAllergies));
        }
    }

}
