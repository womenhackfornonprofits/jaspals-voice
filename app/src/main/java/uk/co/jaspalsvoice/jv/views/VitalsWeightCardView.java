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
import uk.co.jaspalsvoice.jv.adapters.VitalsWeightAdapter;
import uk.co.jaspalsvoice.jv.models.VitalsWeight;

/**
 * Created by Srinivas Klayani on 11 Jul 2016.
 */
public class VitalsWeightCardView extends CardView {

    private RecyclerView weightRecyclerView;
    private VitalsWeightAdapter adapter;
    private ImageView addWeightButton;
    private ArrayList<VitalsWeight> weights;
    private LinearLayout addNewWeightView;
    private LinearLayout saveLayout;
    private ImageView saveButton;
    private ImageView cancelButton;
    private ImageView editButton;
    private EditText weightEdittext;
    private EditText dateEdittext;
    private JvApplication application;
    private TextView titleView;
    private TextView blodPressureSubtitle, dateSubtitle;
    private TextView noContentView;


    public VitalsWeightCardView(Context context) {
        super(context);
        if (!isInEditMode())
            init(context);
    }

    public VitalsWeightCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            init(context);
    }

    public VitalsWeightCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode())
            init(context);
    }

    private void init(Context context) {
        initViews(context);
        initValues(context);
        attachListeners();
        setTitleViews();
        retrieveWeightData();
    }

    private void setTitleViews() {

    }

    private void attachListeners() {
        addWeightButton.setOnClickListener(mOnClickListener);
        saveButton.setOnClickListener(mOnClickListener);
        cancelButton.setOnClickListener(mOnClickListener);
    }

    private void initValues(Context context) {
        application = (JvApplication) context.getApplicationContext();
        weights = new ArrayList<>();
        adapter = new VitalsWeightAdapter(context, weights);
        weightRecyclerView.setAdapter(adapter);
    }

    private void initViews(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View root = inflater.inflate(R.layout.vitals_weight_card_view, this);
        weightRecyclerView = (RecyclerView) root.findViewById(R.id.weightRecyclerView);
        weightRecyclerView.setLayoutManager
                (new LinearLayoutManager(weightRecyclerView.getContext()));
        addWeightButton = (ImageView) root.findViewById(R.id.addWeightButton);
        addNewWeightView = (LinearLayout) root.findViewById(R.id.addNewWeight);
        saveLayout = (LinearLayout) addNewWeightView.findViewById(R.id.saveLayout);
        saveButton = (ImageView) saveLayout.findViewById(R.id.saveButton);
        cancelButton = (ImageView) saveLayout.findViewById(R.id.cancelButton);
        weightEdittext = (EditText) addNewWeightView.
                findViewById(R.id.weightEdittext);
        dateEdittext = (EditText) addNewWeightView.findViewById(R.id.dateEditText);
        blodPressureSubtitle = (TextView)root.findViewById(R.id.weightTitle);
        dateSubtitle = (TextView)root.findViewById(R.id.bpDateTitle);
        titleView = (TextView)root.findViewById(R.id.title);


        titleView.setText(getResources().getString(R.string.vitals_weight_title));
        blodPressureSubtitle.setText(getResources().getString(R.string.vitals_weight_subtitle));
        dateSubtitle.setText(getResources().getString(R.string.date_title));
        noContentView = (TextView) findViewById(R.id.noContentView);
    }

    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.addWeightButton:
                    hideBPList();
                    showAddBPView();
                    break;

                case R.id.saveButton:
                    saveWeightData(weightEdittext.getText().toString(),
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

    private void saveWeightData(String weight, String date) {
        new Save().execute(weight, date);
    }

    private void showBPList() {
        weightRecyclerView.setVisibility(VISIBLE);
    }

    private void hideAddBPView() {
        addNewWeightView.setVisibility(GONE);
    }

    private void showAddBPView() {
        addNewWeightView.setVisibility(VISIBLE);
        saveLayout.setVisibility(VISIBLE);
    }

    private void hideBPList() {
        weightRecyclerView.setVisibility(GONE);
    }

    private void retrieveWeightData() {
        new Weight().execute();
    }

    public void displayRecords(ArrayList<VitalsWeight> weights) {
        this.weights.clear();
        this.weights.addAll(weights);
        adapter = new VitalsWeightAdapter(getContext(), this.weights);
        weightRecyclerView.setAdapter(adapter);
        showBPList();
        adapter.notifyDataSetChanged();
        if (weights.size() == 0){
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

    private class Save extends AsyncTask<String, Void, VitalsWeight> {
        @Override
        protected VitalsWeight doInBackground(String... params) {
            List<VitalsWeight> weights = new ArrayList<>();
            VitalsWeight weight = new VitalsWeight();
            weight.setWeight(params[0]);
            weight.setDate(params[1]);
            weights.add(weight);
            application.getDbHelper().insertOrReplaceWeight(weights, false, 0);
            return weight;
        }

        @Override
        protected void onPostExecute(VitalsWeight weight) {
            retrieveWeightData();
            hideAddBPView();
            showBPList();
        }
    }

    private class Weight extends AsyncTask<Void, Void, List<VitalsWeight>> {
        @Override
        protected List<VitalsWeight> doInBackground(Void... params) {
            return application.getDbHelper().readAllWeights();
        }

        @Override
        protected void onPostExecute(List<VitalsWeight> weights) {
            displayRecords(new ArrayList<VitalsWeight>(weights));
        }
    }

}
