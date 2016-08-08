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
import uk.co.jaspalsvoice.jv.adapters.EnvironmentalAllergiesAdapter;
import uk.co.jaspalsvoice.jv.models.EnvironmentalAllergies;

/**
 * Created by Srinivas Kalyani on 11 Jul 2016.
 */
public class EnvironmentalAllergiesCardView extends CardView {

    private RecyclerView environmentalAllergiesRecyclerView;
    private EnvironmentalAllergiesAdapter adapter;
    private ImageView addEnvironmentalAllergiesButton;
    private ArrayList<EnvironmentalAllergies> environmentalAllergies;
    private LinearLayout addNewEnvironmentalAllergiesView;
    private LinearLayout saveLayout;
    private ImageView saveButton;
    private ImageView cancelButton;
    private ImageView editButton;
    private EditText environmentalAllergiesEdittext;
    private EditText dateEdittext;
    private JvApplication application;
    private TextView titleView;
    private TextView noContentView;


    public EnvironmentalAllergiesCardView(Context context) {
        super(context);
        if (!isInEditMode())
            init(context);
    }

    public EnvironmentalAllergiesCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            init(context);
    }

    public EnvironmentalAllergiesCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode())
            init(context);
    }

    private void init(Context context) {
        initViews(context);
        initValues(context);
        attachListeners();
        setTitleViews();
        retrieveEnvironmentalAllergiesData();
    }

    private void setTitleViews() {

    }

    private void attachListeners() {
        addEnvironmentalAllergiesButton.setOnClickListener(mOnClickListener);
        saveButton.setOnClickListener(mOnClickListener);
        cancelButton.setOnClickListener(mOnClickListener);
    }

    private void initValues(Context context) {
        application = (JvApplication) context.getApplicationContext();
        environmentalAllergies = new ArrayList<>();
        adapter = new EnvironmentalAllergiesAdapter(context, environmentalAllergies);
        environmentalAllergiesRecyclerView.setAdapter(adapter);
    }

    private void initViews(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View root = inflater.inflate(R.layout.environmental_allergies_card_view, this);
        environmentalAllergiesRecyclerView = (RecyclerView) root.findViewById(R.id.environmentalAllergiesRecyclerView);
        environmentalAllergiesRecyclerView.setLayoutManager
                (new LinearLayoutManager(environmentalAllergiesRecyclerView.getContext()));
        addEnvironmentalAllergiesButton = (ImageView) root.findViewById(R.id.addEnvironmentalAllergiesButton);
        addNewEnvironmentalAllergiesView = (LinearLayout) root.findViewById(R.id.addNewEnvironmentalAllergies);
        addNewEnvironmentalAllergiesView.setVisibility(GONE);
        saveLayout = (LinearLayout) addNewEnvironmentalAllergiesView.findViewById(R.id.saveLayout);
        saveButton = (ImageView) saveLayout.findViewById(R.id.saveButton);
        cancelButton = (ImageView) saveLayout.findViewById(R.id.cancelButton);
        environmentalAllergiesEdittext = (EditText) addNewEnvironmentalAllergiesView.findViewById(R.id.editFirstField);
        dateEdittext = (EditText) addNewEnvironmentalAllergiesView.findViewById(R.id.editSecondField);
        LinearLayout bpTitleLayout = (LinearLayout) root.findViewById(R.id.bpTitleLayout);
        TextView blodPressureSubtitle = (TextView)bpTitleLayout.findViewById(R.id.headingFirstField);
        TextView dateSubtitle = (TextView)root.findViewById(R.id.headingSecondField);
        titleView = (TextView)root.findViewById(R.id.title);


        titleView.setText(getResources().getString(R.string.environmental_allergies_title));
        blodPressureSubtitle.setText(getResources().getString(R.string.environmental_allergies_subtitle));
        dateSubtitle.setText(getResources().getString(R.string.type_title));
        noContentView = (TextView) findViewById(R.id.noContentView);
    }

    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.addEnvironmentalAllergiesButton:
                    environmentalAllergiesEdittext.setText("");
                    dateEdittext.setText("");
                    hideNoContentView();
                    hideBPList();
                    showAddBPView();
                    break;

                case R.id.saveButton:
                    saveEnvironmentalAllergiesData(environmentalAllergiesEdittext.getText().toString(),
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

    private void saveEnvironmentalAllergiesData(String environmentalAllergies, String date) {
        new Save().execute(environmentalAllergies, date);
    }

    private void showBPList() {
        environmentalAllergiesRecyclerView.setVisibility(VISIBLE);
    }

    private void hideAddBPView() {
        addNewEnvironmentalAllergiesView.setVisibility(GONE);
    }

    private void showAddBPView() {
        addNewEnvironmentalAllergiesView.setVisibility(VISIBLE);
        saveLayout.setVisibility(VISIBLE);
    }

    private void hideBPList() {
        environmentalAllergiesRecyclerView.setVisibility(GONE);
    }

    private void retrieveEnvironmentalAllergiesData() {
        new EnvironmentalAllergiesRetrieve().execute();
    }

    public void displayRecords(ArrayList<EnvironmentalAllergies> environmentalAllergies) {
        this.environmentalAllergies.clear();
        this.environmentalAllergies.addAll(environmentalAllergies);
        adapter = new EnvironmentalAllergiesAdapter(getContext(), this.environmentalAllergies);
        environmentalAllergiesRecyclerView.setAdapter(adapter);
        showBPList();
        adapter.notifyDataSetChanged();
        if (environmentalAllergies.size() == 0){
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

    private class Save extends AsyncTask<String, Void, EnvironmentalAllergies> {
        @Override
        protected EnvironmentalAllergies doInBackground(String... params) {
            List<EnvironmentalAllergies> environmentalAllergies = new ArrayList<>();
            EnvironmentalAllergies environmentalAllergy= new EnvironmentalAllergies();
            environmentalAllergy.setEnvironmentalAllergies(params[0]);
            environmentalAllergy.setType(params[1]);
            environmentalAllergies.add(environmentalAllergy);
            application.getDbHelper().insertOrReplaceEnvironmentalAllergies(environmentalAllergies, false, 0);
            return environmentalAllergy;
        }

        @Override
        protected void onPostExecute(EnvironmentalAllergies environmentalAllergies) {
            retrieveEnvironmentalAllergiesData();
            hideAddBPView();
            showBPList();
        }
    }

    private class EnvironmentalAllergiesRetrieve extends AsyncTask<Void, Void, List<EnvironmentalAllergies>> {
        @Override
        protected List<EnvironmentalAllergies> doInBackground(Void... params) {
            return application.getDbHelper().readAllEnvironmentalAllergies();
        }

        @Override
        protected void onPostExecute(List<EnvironmentalAllergies> environmentalAllergies) {
            displayRecords(new ArrayList<EnvironmentalAllergies>(environmentalAllergies));
        }
    }

}
