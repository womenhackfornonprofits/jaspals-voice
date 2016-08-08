package uk.co.jaspalsvoice.jv.views;

import android.app.DatePickerDialog;
import android.content.Context;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import uk.co.jaspalsvoice.jv.DateUtils;
import uk.co.jaspalsvoice.jv.JvApplication;
import uk.co.jaspalsvoice.jv.JvPreferences;
import uk.co.jaspalsvoice.jv.R;
import uk.co.jaspalsvoice.jv.activities.VitalsActivity;
import uk.co.jaspalsvoice.jv.adapters.VitalsBloodPressureAdapter;
import uk.co.jaspalsvoice.jv.models.Doctor;
import uk.co.jaspalsvoice.jv.models.VitalsBloodPressure;

/**
 * Created by Srinivas Klayani on 11 Jul 2016.
 */
public class VitalsBloodPressureCardView extends CardView {

    private RecyclerView bloodPressureRecyclerView;
    private VitalsBloodPressureAdapter adapter;
    private ImageView addBloodPressureButton;
    private ArrayList<VitalsBloodPressure> bloodPressures;
    private LinearLayout addNewBloodPressureView;
    private LinearLayout saveLayout;
    private ImageView saveButton;
    private ImageView cancelButton;
    private ImageView editButton;
    private EditText bloodPressureEdittext;
    private EditText dateEdittext;
    private JvApplication application;
    private TextView titleView;
    private TextView noContentView;


    public VitalsBloodPressureCardView(Context context) {
        super(context);
        if (!isInEditMode())
            init(context);
    }

    public VitalsBloodPressureCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            init(context);
    }

    public VitalsBloodPressureCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode())
            init(context);
    }

    private void init(Context context) {
        initViews(context);
        initValues(context);
        attachListeners();
        setTitleViews();
        retrieveBloodPressureData();
    }

    private void setTitleViews() {

    }

    private void attachListeners() {
        addBloodPressureButton.setOnClickListener(mOnClickListener);
        saveButton.setOnClickListener(mOnClickListener);
        cancelButton.setOnClickListener(mOnClickListener);
        dateEdittext.setOnClickListener(mOnClickListener);
    }

    private void initValues(Context context) {
        application = (JvApplication) context.getApplicationContext();
        bloodPressures = new ArrayList<>();
        adapter = new VitalsBloodPressureAdapter(context, bloodPressures);
        bloodPressureRecyclerView.setAdapter(adapter);
    }

    private void initViews(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View root = inflater.inflate(R.layout.vitals_blood_pressure_card_view, this);
        bloodPressureRecyclerView = (RecyclerView) root.findViewById(R.id.bloodPressureRecyclerView);
        bloodPressureRecyclerView.setLayoutManager
                (new LinearLayoutManager(bloodPressureRecyclerView.getContext()));
        addBloodPressureButton = (ImageView) root.findViewById(R.id.addBloodPressureButton);
        addNewBloodPressureView = (LinearLayout) root.findViewById(R.id.addNewBloodPressure);
        saveLayout = (LinearLayout) addNewBloodPressureView.findViewById(R.id.saveLayout);
        saveButton = (ImageView) saveLayout.findViewById(R.id.saveButton);
        cancelButton = (ImageView) saveLayout.findViewById(R.id.cancelButton);
        bloodPressureEdittext = (EditText) addNewBloodPressureView.
                findViewById(R.id.bloodPressureEdittext);
        dateEdittext = (EditText) addNewBloodPressureView.findViewById(R.id.dateEditText);
        LinearLayout bpTitleLayout = (LinearLayout) root.findViewById(R.id.bpTitleLayout);
        TextView blodPressureSubtitle = (TextView)bpTitleLayout.findViewById(R.id.headingFirstField);
        TextView dateSubtitle = (TextView)root.findViewById(R.id.headingSecondField);
        titleView = (TextView)root.findViewById(R.id.title);

        titleView.setText(getResources().getString(R.string.vitals_blood_pressure_title));
        blodPressureSubtitle.setText(getResources().getString(R.string.vitals_blood_pressure_subtitle));
        dateSubtitle.setText(getResources().getString(R.string.date_title));
        noContentView = (TextView) findViewById(R.id.noContentView);
    }

    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.addBloodPressureButton:
                    hideBPList();
                    showAddBPView();
                    break;

                case R.id.saveButton:
                    saveBloodPressureData(bloodPressureEdittext.getText().toString(),
                            dateEdittext.getText().toString());
                    hideAddBPView();
                    showBPList();
                    break;

                case R.id.cancelButton:
                    hideAddBPView();
                    showBPList();
                    break;

                case R.id.dateEditText:
                    DateUtils.showDatePicker(dateEdittext, ((VitalsActivity)getContext()));
                    break;
            }

        }
    };

    private void saveBloodPressureData(String bloodPressure, String date) {
        new Save().execute(bloodPressure, date);
    }

    private void showBPList() {
        bloodPressureRecyclerView.setVisibility(VISIBLE);
    }

    private void hideAddBPView() {
        addNewBloodPressureView.setVisibility(GONE);
    }

    private void showAddBPView() {
        addNewBloodPressureView.setVisibility(VISIBLE);
        saveLayout.setVisibility(VISIBLE);
    }

    private void hideBPList() {
        bloodPressureRecyclerView.setVisibility(GONE);
    }

    private void retrieveBloodPressureData() {
        new BloodPressure().execute();
    }

    public void displayRecords(ArrayList<VitalsBloodPressure> bloodPressures) {
        this.bloodPressures.clear();
        this.bloodPressures.addAll(bloodPressures);
        adapter = new VitalsBloodPressureAdapter(getContext(), this.bloodPressures);
        bloodPressureRecyclerView.setAdapter(adapter);
        showBPList();
        adapter.notifyDataSetChanged();
        if (bloodPressures.size() == 0){
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

    private class Save extends AsyncTask<String, Void, VitalsBloodPressure> {
        @Override
        protected VitalsBloodPressure doInBackground(String... params) {
            List<VitalsBloodPressure> bloodPressures = new ArrayList<>();
            VitalsBloodPressure bloodPressure = new VitalsBloodPressure();
            bloodPressure.setBloodPressure(params[0]);
            bloodPressure.setDate(params[1]);
            bloodPressures.add(bloodPressure);
            application.getDbHelper().insertOrReplaceBloodPressure(bloodPressures, false, 0);
            return bloodPressure;
        }

        @Override
        protected void onPostExecute(VitalsBloodPressure bloodPressure) {
            retrieveBloodPressureData();
            hideAddBPView();
            showBPList();
        }
    }

    private class BloodPressure extends AsyncTask<Void, Void, List<VitalsBloodPressure>> {
        @Override
        protected List<VitalsBloodPressure> doInBackground(Void... params) {
            return application.getDbHelper().readAllBloodPressures();
        }

        @Override
        protected void onPostExecute(List<VitalsBloodPressure> bloodPressures) {
            displayRecords(new ArrayList<VitalsBloodPressure>(bloodPressures));
        }
    }

}
