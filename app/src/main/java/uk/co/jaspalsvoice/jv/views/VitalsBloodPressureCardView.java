package uk.co.jaspalsvoice.jv.views;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import uk.co.jaspalsvoice.jv.JvApplication;
import uk.co.jaspalsvoice.jv.JvPreferences;
import uk.co.jaspalsvoice.jv.R;
import uk.co.jaspalsvoice.jv.adapters.VitalsBloodPressureAdapter;
import uk.co.jaspalsvoice.jv.models.VitalsBloodPressure;

/**
 * Created by Srinivas Klayani on 11 Jul 2016.
 */
public class VitalsBloodPressureCardView extends CardView {

    private RecyclerView bloodPressureRecyclerView;
    private VitalsBloodPressureAdapter adapter;
    private ImageView addButton;
    private ArrayList<VitalsBloodPressure> bloodPressures;

    public VitalsBloodPressureCardView(Context context) {
        super(context);
        init(context);
    }

    public VitalsBloodPressureCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VitalsBloodPressureCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        initViews(context);
        initValues(context);
    }

    private void initValues(Context context) {
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
    }



}
