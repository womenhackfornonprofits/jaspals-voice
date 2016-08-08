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
import uk.co.jaspalsvoice.jv.activities.VitalsActivity;
import uk.co.jaspalsvoice.jv.adapters.VitalsBloodGlucoseAdapter;
import uk.co.jaspalsvoice.jv.models.VitalsBloodGlucose;

/**
 * Created by Srinivas Klayani on 11 Jul 2016.
 */
public class VitalsBloodGlucoseCardView extends CardView {

    private RecyclerView bloodGlucoseRecyclerView;
    private VitalsBloodGlucoseAdapter adapter;
    private ImageView addBloodGlucoseButton;
    private ArrayList<VitalsBloodGlucose> bloodGlucoses;
    private LinearLayout addNewBloodGlucoseView;
    private LinearLayout saveLayout;
    private ImageView saveButton;
    private ImageView cancelButton;
    private ImageView editButton;
    private EditText bloodGlucoseEdittext;
    private EditText dateEdittext;
    private JvApplication application;
    private TextView titleView;
    private TextView noContentView;


    public VitalsBloodGlucoseCardView(Context context) {
        super(context);
        if (!isInEditMode())
            init(context);
    }

    public VitalsBloodGlucoseCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            init(context);
    }

    public VitalsBloodGlucoseCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode())
            init(context);
    }

    private void init(Context context) {
        initViews(context);
        initValues(context);
        attachListeners();
        setTitleViews();
        retrieveBloodGlucoseData();
    }

    private void setTitleViews() {

    }

    private void attachListeners() {
        addBloodGlucoseButton.setOnClickListener(mOnClickListener);
        saveButton.setOnClickListener(mOnClickListener);
        cancelButton.setOnClickListener(mOnClickListener);
        dateEdittext.setOnClickListener(mOnClickListener);
    }

    private void initValues(Context context) {
        application = (JvApplication) context.getApplicationContext();
        bloodGlucoses = new ArrayList<>();
        adapter = new VitalsBloodGlucoseAdapter(context, bloodGlucoses);
        bloodGlucoseRecyclerView.setAdapter(adapter);
    }

    private void initViews(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View root = inflater.inflate(R.layout.vitals_blood_glucose_card_view, this);
        bloodGlucoseRecyclerView = (RecyclerView) root.findViewById(R.id.bloodGlucoseRecyclerView);
        bloodGlucoseRecyclerView.setLayoutManager
                (new LinearLayoutManager(bloodGlucoseRecyclerView.getContext()));
        addBloodGlucoseButton = (ImageView) root.findViewById(R.id.addBloodGlucoseButton);
        addNewBloodGlucoseView = (LinearLayout) root.findViewById(R.id.addNewBloodGlucose);
        addNewBloodGlucoseView.setVisibility(GONE);
        saveLayout = (LinearLayout) addNewBloodGlucoseView.findViewById(R.id.saveLayout);
        saveButton = (ImageView) saveLayout.findViewById(R.id.saveButton);
        cancelButton = (ImageView) saveLayout.findViewById(R.id.cancelButton);
        bloodGlucoseEdittext = (EditText) addNewBloodGlucoseView.
                findViewById(R.id.editFirstField);
        dateEdittext = (EditText) addNewBloodGlucoseView.findViewById(R.id.editSecondField);
        dateEdittext.setFocusable(false);
        LinearLayout bpTitleLayout = (LinearLayout) root.findViewById(R.id.bpTitleLayout);
        TextView blodPressureSubtitle = (TextView)bpTitleLayout.findViewById(R.id.headingFirstField);
        TextView dateSubtitle = (TextView)root.findViewById(R.id.headingSecondField);
        titleView = (TextView)root.findViewById(R.id.title);


        titleView.setText(getResources().getString(R.string.vitals_blood_glucose_title));
        blodPressureSubtitle.setText(getResources().getString(R.string.vitals_blood_glucose_subtitle));
        dateSubtitle.setText(getResources().getString(R.string.date_title));
        noContentView = (TextView) findViewById(R.id.noContentView);
    }

    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.addBloodGlucoseButton:
                    bloodGlucoseEdittext.setText("");
                    dateEdittext.setText("");
                    hideNoContentView();
                    hideBPList();
                    showAddBPView();
                    break;

                case R.id.saveButton:
                    saveBloodGlucoseData(bloodGlucoseEdittext.getText().toString(),
                            dateEdittext.getText().toString());
                    hideAddBPView();
                    showBPList();
                    break;

                case R.id.cancelButton:
                    hideAddBPView();
                    showBPList();
                    break;

                case R.id.editSecondField:
                    DateUtils.showDatePicker(dateEdittext, ((VitalsActivity)getContext()));
                    break;
            }

        }
    };

    private void saveBloodGlucoseData(String bloodGlucose, String date) {
        new Save().execute(bloodGlucose, date);
    }

    private void showBPList() {
        bloodGlucoseRecyclerView.setVisibility(VISIBLE);
    }

    private void hideAddBPView() {
        addNewBloodGlucoseView.setVisibility(GONE);
    }

    private void showAddBPView() {
        addNewBloodGlucoseView.setVisibility(VISIBLE);
        saveLayout.setVisibility(VISIBLE);
    }

    private void hideBPList() {
        bloodGlucoseRecyclerView.setVisibility(GONE);
    }

    private void retrieveBloodGlucoseData() {
        new BloodGlucose().execute();
    }

    public void displayRecords(ArrayList<VitalsBloodGlucose> bloodGlucoses) {
        this.bloodGlucoses.clear();
        this.bloodGlucoses.addAll(bloodGlucoses);
        adapter = new VitalsBloodGlucoseAdapter(getContext(), this.bloodGlucoses);
        bloodGlucoseRecyclerView.setAdapter(adapter);
        showBPList();
        adapter.notifyDataSetChanged();
        if (bloodGlucoses.size() == 0){
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

    private class Save extends AsyncTask<String, Void, VitalsBloodGlucose> {
        @Override
        protected VitalsBloodGlucose doInBackground(String... params) {
            List<VitalsBloodGlucose> bloodGlucoses = new ArrayList<>();
            VitalsBloodGlucose bloodGlucose = new VitalsBloodGlucose();
            bloodGlucose.setBloodGlucose(params[0]);
            bloodGlucose.setDate(params[1]);
            bloodGlucoses.add(bloodGlucose);
            application.getDbHelper().insertOrReplaceBloodGlucose(bloodGlucoses, false, 0);
            return bloodGlucose;
        }

        @Override
        protected void onPostExecute(VitalsBloodGlucose bloodGlucose) {
            retrieveBloodGlucoseData();
            hideAddBPView();
            showBPList();
        }
    }

    private class BloodGlucose extends AsyncTask<Void, Void, List<VitalsBloodGlucose>> {
        @Override
        protected List<VitalsBloodGlucose> doInBackground(Void... params) {
            return application.getDbHelper().readAllBloodGlucoses();
        }

        @Override
        protected void onPostExecute(List<VitalsBloodGlucose> bloodGlucoses) {
            displayRecords(new ArrayList<VitalsBloodGlucose>(bloodGlucoses));
        }
    }

}
