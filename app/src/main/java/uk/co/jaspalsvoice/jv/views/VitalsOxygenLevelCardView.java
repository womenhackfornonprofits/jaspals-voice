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
import uk.co.jaspalsvoice.jv.adapters.VitalsOxygenLevelAdapter;
import uk.co.jaspalsvoice.jv.models.VitalsOxygenLevel;

public class VitalsOxygenLevelCardView extends CardView {

    private RecyclerView oxygenRecyclerView;
    private VitalsOxygenLevelAdapter adapter;
    private ImageView addOxygenButton;
    private ArrayList<VitalsOxygenLevel> oxygenLevels;
    private LinearLayout addNewOxygenView;
    private LinearLayout saveLayout;
    private ImageView saveButton;
    private ImageView cancelButton;
    private ImageView editButton;
    private EditText oxygenEdittext;
    private EditText dateEdittext;
    private JvApplication application;
    private TextView titleView;
    private TextView noContentView;


    public VitalsOxygenLevelCardView(Context context) {
        super(context);
        if (!isInEditMode())
            init(context);
    }

    public VitalsOxygenLevelCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            init(context);
    }

    public VitalsOxygenLevelCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode())
            init(context);
    }

    private void init(Context context) {
        initViews(context);
        initValues(context);
        attachListeners();
        setTitleViews();
        retrieveOxygenLevelData();
    }

    private void setTitleViews() {

    }

    private void attachListeners() {
        addOxygenButton.setOnClickListener(mOnClickListener);
        saveButton.setOnClickListener(mOnClickListener);
        cancelButton.setOnClickListener(mOnClickListener);
        dateEdittext.setOnClickListener(mOnClickListener);
    }

    private void initValues(Context context) {
        application = (JvApplication) context.getApplicationContext();
        oxygenLevels = new ArrayList<>();
        oxygenRecyclerView.setAdapter(adapter);
    }

    private void initViews(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View root = inflater.inflate(R.layout.vitals_oxygen_card_view, this);
        oxygenRecyclerView = (RecyclerView) root.findViewById(R.id.oxygenRecyclerView);
        oxygenRecyclerView.setLayoutManager
                (new LinearLayoutManager(oxygenRecyclerView.getContext()));
        addOxygenButton = (ImageView) root.findViewById(R.id.addOxygenButton);
        addNewOxygenView = (LinearLayout) root.findViewById(R.id.addNewOxygen);
        addNewOxygenView.setVisibility(GONE);
        saveLayout = (LinearLayout) addNewOxygenView.findViewById(R.id.saveLayout);
        saveButton = (ImageView) saveLayout.findViewById(R.id.saveButton);
        cancelButton = (ImageView) saveLayout.findViewById(R.id.cancelButton);
        oxygenEdittext = (EditText) addNewOxygenView.
                findViewById(R.id.editFirstField);
        dateEdittext = (EditText) addNewOxygenView.findViewById(R.id.editSecondField);
        dateEdittext.setFocusable(false);
        LinearLayout olTitleLayout = (LinearLayout) root.findViewById(R.id.olTitleLayout);
        TextView blodPressureSubtitle = (TextView)olTitleLayout.findViewById(R.id.headingFirstField);
        TextView dateSubtitle = (TextView)root.findViewById(R.id.headingSecondField);
        titleView = (TextView)root.findViewById(R.id.title);


        titleView.setText(getResources().getString(R.string.vitals_oxygen_level_title));
        blodPressureSubtitle.setText(getResources().getString(R.string.vitals_oxygen_level_subtitle));
        dateSubtitle.setText(getResources().getString(R.string.date_title));
        noContentView = (TextView) findViewById(R.id.noContentView);
    }

    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.addOxygenButton:
                    oxygenEdittext.setText("");
                    dateEdittext.setText("");
                    hideNoContentView();
                    hideOLList();
                    showAddOLView();
                    break;

                case R.id.saveButton:
                    saveOxygenLevelData(oxygenEdittext.getText().toString(),
                            dateEdittext.getText().toString());
                    hideAddOLView();
                    showOLList();
                    break;

                case R.id.cancelButton:
                    hideAddOLView();
                    showOLList();
                    break;

                case R.id.editSecondField:
                    DateUtils.showDatePicker(dateEdittext, ((VitalsActivity)getContext()));
                    break;
            }

        }
    };

    private void saveOxygenLevelData(String oxygenLevel, String date) {
        new Save().execute(oxygenLevel, date);
    }

    private void showOLList() {
        oxygenRecyclerView.setVisibility(VISIBLE);
    }

    private void hideAddOLView() {
        addNewOxygenView.setVisibility(GONE);
    }

    private void showAddOLView() {
        addNewOxygenView.setVisibility(VISIBLE);
        saveLayout.setVisibility(VISIBLE);
    }

    private void hideOLList() {
        oxygenRecyclerView.setVisibility(GONE);
    }

    private void retrieveOxygenLevelData() {
        new OxygenLevel().execute();
    }

    public void displayRecords(ArrayList<VitalsOxygenLevel> oxygenLevels) {
        this.oxygenLevels.clear();
        this.oxygenLevels.addAll(oxygenLevels);
        adapter = new VitalsOxygenLevelAdapter(getContext(), this.oxygenLevels);
        oxygenRecyclerView.setAdapter(adapter);
        showOLList();
        adapter.notifyDataSetChanged();
        if (oxygenLevels.size() == 0){
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

    private class Save extends AsyncTask<String, Void, VitalsOxygenLevel> {
        @Override
        protected VitalsOxygenLevel doInBackground(String... params) {
            List<VitalsOxygenLevel> oxygenLevels = new ArrayList<>();
            VitalsOxygenLevel oxygenLevel = new VitalsOxygenLevel();
            oxygenLevel.setOxygenLevel(params[0]);
            oxygenLevel.setDate(params[1]);
            oxygenLevels.add(oxygenLevel);
            application.getDbHelper().insertOrReplaceOxygenLevel(oxygenLevels, false, 0);
            return oxygenLevel;
        }

        @Override
        protected void onPostExecute(VitalsOxygenLevel oxygenLevel) {
            retrieveOxygenLevelData();
            hideAddOLView();
            showOLList();
        }
    }

    private class OxygenLevel extends AsyncTask<Void, Void, List<VitalsOxygenLevel>> {
        @Override
        protected List<VitalsOxygenLevel> doInBackground(Void... params) {
            return application.getDbHelper().readAllOxygenLevels();
        }

        @Override
        protected void onPostExecute(List<VitalsOxygenLevel> oxygenLevels) {
            displayRecords(new ArrayList<VitalsOxygenLevel>(oxygenLevels));
        }
    }

}
