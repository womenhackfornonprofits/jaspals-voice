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
import uk.co.jaspalsvoice.jv.adapters.VitalsHeightAdapter;
import uk.co.jaspalsvoice.jv.models.VitalsHeight;

/**
 * Created by Srinivas Klayani on 11 Jul 2016.
 */
public class VitalsHeightCardView extends CardView {

    private RecyclerView heightRecyclerView;
    private VitalsHeightAdapter adapter;
    private ImageView addHeightButton;
    private ArrayList<VitalsHeight> heights;
    private LinearLayout addNewHeightView;
    private LinearLayout saveLayout;
    private ImageView saveButton;
    private ImageView cancelButton;
    private ImageView editButton;
    private EditText heightEdittext;
    private EditText dateEdittext;
    private JvApplication application;
    private TextView titleView;
    private TextView noContentView;


    public VitalsHeightCardView(Context context) {
        super(context);
        if (!isInEditMode())
            init(context);
    }

    public VitalsHeightCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            init(context);
    }

    public VitalsHeightCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode())
            init(context);
    }

    private void init(Context context) {
        initViews(context);
        initValues(context);
        attachListeners();
        setTitleViews();
        retrieveHeightData();
    }

    private void setTitleViews() {

    }

    private void attachListeners() {
        addHeightButton.setOnClickListener(mOnClickListener);
        saveButton.setOnClickListener(mOnClickListener);
        cancelButton.setOnClickListener(mOnClickListener);
        dateEdittext.setOnClickListener(mOnClickListener);
    }

    private void initValues(Context context) {
        application = (JvApplication) context.getApplicationContext();
        heights = new ArrayList<>();
        adapter = new VitalsHeightAdapter(context, heights);
        heightRecyclerView.setAdapter(adapter);
    }

    private void initViews(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View root = inflater.inflate(R.layout.vitals_height_card_view, this);
        heightRecyclerView = (RecyclerView) root.findViewById(R.id.heightRecyclerView);
        heightRecyclerView.setLayoutManager
                (new LinearLayoutManager(heightRecyclerView.getContext()));
        addHeightButton = (ImageView) root.findViewById(R.id.addHeightButton);
        addNewHeightView = (LinearLayout) root.findViewById(R.id.addNewHeight);
        saveLayout = (LinearLayout) addNewHeightView.findViewById(R.id.saveLayout);
        saveButton = (ImageView) saveLayout.findViewById(R.id.saveButton);
        cancelButton = (ImageView) saveLayout.findViewById(R.id.cancelButton);
        heightEdittext = (EditText) addNewHeightView.
                findViewById(R.id.heightEdittext);
        dateEdittext = (EditText) addNewHeightView.findViewById(R.id.dateEditText);
        LinearLayout bpTitleLayout = (LinearLayout) root.findViewById(R.id.bpTitleLayout);
        TextView blodPressureSubtitle = (TextView)bpTitleLayout.findViewById(R.id.headingFirstField);
        TextView dateSubtitle = (TextView)root.findViewById(R.id.headingSecondField);
        titleView = (TextView)root.findViewById(R.id.title);


        titleView.setText(getResources().getString(R.string.vitals_height_title));
        blodPressureSubtitle.setText(getResources().getString(R.string.vitals_height_subtitle));
        dateSubtitle.setText(getResources().getString(R.string.date_title));
        noContentView = (TextView) findViewById(R.id.noContentView);
    }

    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.addHeightButton:
                    hideBPList();
                    showAddBPView();
                    break;

                case R.id.saveButton:
                    saveHeightData(heightEdittext.getText().toString(),
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

    private void saveHeightData(String height, String date) {
        new Save().execute(height, date);
    }

    private void showBPList() {
        heightRecyclerView.setVisibility(VISIBLE);
    }

    private void hideAddBPView() {
        addNewHeightView.setVisibility(GONE);
    }

    private void showAddBPView() {
        addNewHeightView.setVisibility(VISIBLE);
        saveLayout.setVisibility(VISIBLE);
    }

    private void hideBPList() {
        heightRecyclerView.setVisibility(GONE);
    }

    private void retrieveHeightData() {
        new Height().execute();
    }

    public void displayRecords(ArrayList<VitalsHeight> heights) {
        this.heights.clear();
        this.heights.addAll(heights);
        adapter = new VitalsHeightAdapter(getContext(), this.heights);
        heightRecyclerView.setAdapter(adapter);
        showBPList();
        adapter.notifyDataSetChanged();
        if (heights.size() == 0){
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

    private class Save extends AsyncTask<String, Void, VitalsHeight> {
        @Override
        protected VitalsHeight doInBackground(String... params) {
            List<VitalsHeight> heights = new ArrayList<>();
            VitalsHeight height = new VitalsHeight();
            height.setHeight(params[0]);
            height.setDate(params[1]);
            heights.add(height);
            application.getDbHelper().insertOrReplaceHeight(heights, false, 0);
            return height;
        }

        @Override
        protected void onPostExecute(VitalsHeight height) {
            retrieveHeightData();
            hideAddBPView();
            showBPList();
        }
    }

    private class Height extends AsyncTask<Void, Void, List<VitalsHeight>> {
        @Override
        protected List<VitalsHeight> doInBackground(Void... params) {
            return application.getDbHelper().readAllHeights();
        }

        @Override
        protected void onPostExecute(List<VitalsHeight> heights) {
            displayRecords(new ArrayList<VitalsHeight>(heights));
        }
    }

}
