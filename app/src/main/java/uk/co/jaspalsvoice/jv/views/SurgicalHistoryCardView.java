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
import uk.co.jaspalsvoice.jv.adapters.SurgicalHistoryAdapter;
import uk.co.jaspalsvoice.jv.models.SurgicalHistory;

/**
 * Created by Srinivas Klayani on 11 Jul 2016.
 */
public class SurgicalHistoryCardView extends CardView {

    private RecyclerView surgicalHistoryRecyclerView;
    private SurgicalHistoryAdapter adapter;
    private ImageView addSurgicalHistoryButton;
    private ArrayList<SurgicalHistory> surgicalHistory;
    private LinearLayout addNewSurgicalHistoryView;
    private LinearLayout saveLayout;
    private ImageView saveButton;
    private ImageView cancelButton;
    private ImageView editButton;
    private EditText surgicalHistoryEdittext;
    private EditText dateEdittext;
    private JvApplication application;
    private TextView titleView;
    private TextView noContentView;


    public SurgicalHistoryCardView(Context context) {
        super(context);
        if (!isInEditMode())
            init(context);
    }

    public SurgicalHistoryCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            init(context);
    }

    public SurgicalHistoryCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode())
            init(context);
    }

    private void init(Context context) {
        initViews(context);
        initValues(context);
        attachListeners();
        setTitleViews();
        retrieveSurgicalHistoryData();
    }

    private void setTitleViews() {

    }

    private void attachListeners() {
        addSurgicalHistoryButton.setOnClickListener(mOnClickListener);
        saveButton.setOnClickListener(mOnClickListener);
        cancelButton.setOnClickListener(mOnClickListener);
        dateEdittext.setOnClickListener(mOnClickListener);
    }

    private void initValues(Context context) {
        application = (JvApplication) context.getApplicationContext();
        surgicalHistory = new ArrayList<>();
        adapter = new SurgicalHistoryAdapter(context, surgicalHistory);
        surgicalHistoryRecyclerView.setAdapter(adapter);
    }

    private void initViews(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View root = inflater.inflate(R.layout.surgical_history_card_view, this);
        surgicalHistoryRecyclerView = (RecyclerView) root.findViewById(R.id.surgicalHistoryRecyclerView);
        surgicalHistoryRecyclerView.setLayoutManager
                (new LinearLayoutManager(surgicalHistoryRecyclerView.getContext()));
        addSurgicalHistoryButton = (ImageView) root.findViewById(R.id.addSurgicalHistoryButton);
        addNewSurgicalHistoryView = (LinearLayout) root.findViewById(R.id.addNewSurgicalHistory);
        addNewSurgicalHistoryView.setVisibility(GONE);
        saveLayout = (LinearLayout) addNewSurgicalHistoryView.findViewById(R.id.saveLayout);
        saveButton = (ImageView) saveLayout.findViewById(R.id.saveButton);
        cancelButton = (ImageView) saveLayout.findViewById(R.id.cancelButton);
        surgicalHistoryEdittext = (EditText) addNewSurgicalHistoryView.findViewById(R.id.editFirstField);
        dateEdittext = (EditText) addNewSurgicalHistoryView.findViewById(R.id.editSecondField);
        dateEdittext.setFocusable(false);
        LinearLayout bpTitleLayout = (LinearLayout) root.findViewById(R.id.bpTitleLayout);
        TextView blodPressureSubtitle = (TextView) bpTitleLayout.findViewById(R.id.headingFirstField);
        TextView dateSubtitle = (TextView) root.findViewById(R.id.headingSecondField);
        titleView = (TextView) root.findViewById(R.id.title);


        titleView.setText(getResources().getString(R.string.surgical_history_title));
        blodPressureSubtitle.setText(getResources().getString(R.string.surgical_history_subtitle));
        dateSubtitle.setText(getResources().getString(R.string.date_title));
        noContentView = (TextView) findViewById(R.id.noContentView);
    }

    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.addSurgicalHistoryButton:
                    surgicalHistoryEdittext.setText("");
                    dateEdittext.setText("");
                    hideNoContentView();
                    hideBPList();
                    showAddBPView();
                    break;

                case R.id.saveButton:
                    saveSurgicalHistoryData(surgicalHistoryEdittext.getText().toString(),
                            dateEdittext.getText().toString());
                    hideAddBPView();
                    showBPList();
                    break;

                case R.id.cancelButton:
                    hideAddBPView();
                    showBPList();
                    break;

                case R.id.editSecondField:
                    DateUtils.showDatePicker(dateEdittext, ((DiagnosesActivity) getContext()));
                    break;
            }

        }
    };

    private void saveSurgicalHistoryData(String surgicalHistory, String date) {
        new Save().execute(surgicalHistory, date);
    }

    private void showBPList() {
        surgicalHistoryRecyclerView.setVisibility(VISIBLE);
    }

    private void hideAddBPView() {
        addNewSurgicalHistoryView.setVisibility(GONE);
    }

    private void showAddBPView() {
        addNewSurgicalHistoryView.setVisibility(VISIBLE);
        saveLayout.setVisibility(VISIBLE);
    }

    private void hideBPList() {
        surgicalHistoryRecyclerView.setVisibility(GONE);
    }

    private void retrieveSurgicalHistoryData() {
        new SurgicalHistoryRetrieve().execute();
    }

    public void displayRecords(ArrayList<SurgicalHistory> surgicalHistory) {
        this.surgicalHistory.clear();
        this.surgicalHistory.addAll(surgicalHistory);
        adapter = new SurgicalHistoryAdapter(getContext(), this.surgicalHistory);
        surgicalHistoryRecyclerView.setAdapter(adapter);
        showBPList();
        adapter.notifyDataSetChanged();
        if (surgicalHistory.size() == 0) {
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

    private class Save extends AsyncTask<String, Void, SurgicalHistory> {
        @Override
        protected SurgicalHistory doInBackground(String... params) {
            List<SurgicalHistory> diagnoses = new ArrayList<>();
            SurgicalHistory surgicalHistory = new SurgicalHistory();
            surgicalHistory.setSurgicalHistory(params[0]);
            surgicalHistory.setDate(params[1]);
            diagnoses.add(surgicalHistory);
            application.getDbHelper().insertOrReplaceSurgicalHistory(diagnoses, false, 0);
            return surgicalHistory;
        }

        @Override
        protected void onPostExecute(SurgicalHistory surgicalHistory) {
            retrieveSurgicalHistoryData();
            hideAddBPView();
            showBPList();
        }
    }

    private class SurgicalHistoryRetrieve extends AsyncTask<Void, Void, List<SurgicalHistory>> {
        @Override
        protected List<SurgicalHistory> doInBackground(Void... params) {
            return application.getDbHelper().readAllSurgicalHistory();
        }

        @Override
        protected void onPostExecute(List<SurgicalHistory> surgicalHistory) {
            displayRecords(new ArrayList<SurgicalHistory>(surgicalHistory));
        }
    }

}
