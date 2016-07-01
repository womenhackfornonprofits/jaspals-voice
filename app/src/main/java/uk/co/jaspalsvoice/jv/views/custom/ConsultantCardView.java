package uk.co.jaspalsvoice.jv.views.custom;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import uk.co.jaspalsvoice.jv.JvApplication;
import uk.co.jaspalsvoice.jv.R;
import uk.co.jaspalsvoice.jv.models.Doctor;

/**
 * Created by Ana on 2/21/2016.
 */
public class ConsultantCardView extends CardView {
    private String title;
    private String text1;
    private String text2;
    private String text3;
    private String text4;

    private String texth2;
    private String texth3;
    private String texth4;    
    
    private String doctorType;
    private boolean editMode;

    private TextView titleView;

    private TextView label1View;
    private TextView text1View;
    private EditText edit1View;

    private TextView label2View;
    private TextView text2View;
    private EditText edit2View;

    private TextView label3View;
    private TextView text3View;
    private EditText edit3View;

    private TextView label4View;
    private TextView text4View;
    private EditText edit4View;

    private TextView label2hView;
    private TextView text2hView;
    private EditText edit2hView;

    private TextView label3hView;
    private TextView text3hView;
    private EditText edit3hView;

    private TextView label4hView;
    private TextView text4hView;
    private EditText edit4hView;

    private ViewGroup buttonsView;
    private Button cancelBtn;
    private Button saveBtn;

    private JvApplication application;

    public ConsultantCardView(Context context) {
        super(context);
        init(context);
    }

    public ConsultantCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ConsultantCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        application = (JvApplication) context.getApplicationContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View root = inflater.inflate(R.layout.consultant_contact_card_view, this);

        titleView = (TextView) root.findViewById(R.id.title);

        label1View = (TextView) root.findViewById(R.id.label1);
        text1View = (TextView) root.findViewById(R.id.text1);
        edit1View = (EditText) root.findViewById(R.id.edit1);

        label2View = (TextView) root.findViewById(R.id.label2);
        text2View = (TextView) root.findViewById(R.id.text2);
        edit2View = (EditText) root.findViewById(R.id.edit2);

        label3View = (TextView) root.findViewById(R.id.label3);
        text3View = (TextView) root.findViewById(R.id.text3);
        edit3View = (EditText) root.findViewById(R.id.edit3);

        label4View = (TextView) root.findViewById(R.id.label4);
        text4View = (TextView) root.findViewById(R.id.text4);
        edit4View = (EditText) root.findViewById(R.id.edit4);

        label2hView = (TextView) root.findViewById(R.id.labelh2);
        text2hView = (TextView) root.findViewById(R.id.texth2);
        edit2hView = (EditText) root.findViewById(R.id.edith2);

        label3hView = (TextView) root.findViewById(R.id.labelh3);
        text3hView = (TextView) root.findViewById(R.id.texth3);
        edit3hView = (EditText) root.findViewById(R.id.edith3);

        label4hView = (TextView) root.findViewById(R.id.labelh4);
        text4hView = (TextView) root.findViewById(R.id.texth4);
        edit4hView = (EditText) root.findViewById(R.id.edith4);

        buttonsView = (ViewGroup) root.findViewById(R.id.buttons);
        cancelBtn = (Button) root.findViewById(R.id.cancel);
        saveBtn = (Button) root.findViewById(R.id.save);

        showDefaultText();

        titleView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editMode = !editMode;

                if (editMode) {
                    showEditMode();
                } else {
                    showNonEditMode();
                }
            }
        });

        cancelBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editMode = !editMode;

                setEdit(edit1View, text1);
                setEdit(edit2View, text2);

                setEdit(edit3View, text1);
                setEdit(edit4View, text2);
                showNonEditMode();
            }
        });

        saveBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editMode = !editMode;

                new Save().execute(edit1View.getText().toString(),edit2hView.getText().toString(),
                        edit3hView.getText().toString(),edit4hView.getText().toString(),
                        edit2View.getText().toString(),
                        edit3View.getText().toString(),edit4View.getText().toString());
            }
        });
    }

    public void setTitle(String title) {
        this.title = title;
        titleView.setText(title);
    }

    public String getTitle() {
        return title;
    }

    public void setText1(String text) {
        this.text1 = text;
        text1View.setText(text);
    }

    public String getText2() {
        return text2;
    }

    public void setText2(String text) {
        this.text2 = text;
        text2View.setText(text);
    }

    public void setText3(String text) {
        this.text3 = text;
        text3View.setText(text);
    }

    public void setText4(String text) {
        this.text4 = text;
        text4View.setText(text);
    }

    public void setTexth2(String text) {
        this.texth2 = text;
        text2hView.setText(text);
    }

    public void setTexth3(String text) {
        this.texth3 = text;
        text3hView.setText(text);
    }

    public void setTexth4(String text) {
        this.texth4 = text;
        text4hView.setText(text);
    }
    
    public String getText1() {
        return text1;
    }

    public String getDoctorType() {
        return doctorType;
    }

    public void setDoctorType(String doctorType) {
        this.doctorType = doctorType;
    }

    public void setEdit(EditText editView, String text) {
        editView.setText(text);
    }

    public void setLabel1View(String text) {
        label1View.setText(text);
    }

    public void setLabel2View(String text) {
        label2View.setText(text);
    }

    public void setLabel3View(String text) {
        label3View.setText(text);
    }

    public void setLabel4View(String text) {
        label4View.setText(text);
    }
    
    public void setLabel2hView(String text) {
        label2hView.setText(text);
    }

    public void setLabel3hView(String text) {
        label3hView.setText(text);
    }

    public void setLabel4hView(String text) {
        label4hView.setText(text);
    }


    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public boolean isEditMode() {
        return editMode;
    }

    private void showNonEditMode() {
        edit1View.setVisibility(GONE);
        edit2View.setVisibility(GONE);
        text1View.setVisibility(VISIBLE);
        text2View.setVisibility(VISIBLE);
        edit3View.setVisibility(GONE);
        edit4View.setVisibility(GONE);
        text3View.setVisibility(VISIBLE);
        text4View.setVisibility(VISIBLE);
        edit2hView.setVisibility(GONE);
        text2hView.setVisibility(VISIBLE);
        edit3hView.setVisibility(GONE);
        edit4hView.setVisibility(GONE);
        text3hView.setVisibility(VISIBLE);
        text4hView.setVisibility(VISIBLE);
        buttonsView.setVisibility(GONE);
        showDefaultText();
        titleView.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_action_edit), null);
    }

    private void showEditMode() {
        titleView.setCompoundDrawables(null, null, null, null);
        text1View.setVisibility(GONE);
        text2View.setVisibility(GONE);
        edit1View.setText(text1View.getText());
        edit1View.setVisibility(VISIBLE);
        edit2View.setText(text2View.getText());
        edit2View.setVisibility(VISIBLE);
        text3View.setVisibility(GONE);
        text4View.setVisibility(GONE);
        edit3View.setText(text3View.getText());
        edit3View.setVisibility(VISIBLE);
        edit4View.setText(text4View.getText());
        edit4View.setVisibility(VISIBLE);

        edit2hView.setText(text2hView.getText());
        edit2hView.setVisibility(VISIBLE);
        edit3hView.setText(text3hView.getText());
        edit3hView.setVisibility(VISIBLE);
        edit4hView.setText(text4hView.getText());
        edit4hView.setVisibility(VISIBLE);
        text2hView.setVisibility(GONE);
        text3hView.setVisibility(GONE);
        text4hView.setVisibility(GONE);

        buttonsView.setVisibility(VISIBLE);
    }

    private void showDefaultText() {
        if (TextUtils.isEmpty(text1)) {
            text1View.setText(R.string.default_text_when_not_specified);
        }
        if (TextUtils.isEmpty(text2)) {
            text2View.setText(R.string.default_text_when_not_specified);
        }
        if (TextUtils.isEmpty(text3)) {
            text3View.setText(R.string.default_text_when_not_specified);
        }
        if (TextUtils.isEmpty(text4)) {
            text4View.setText(R.string.default_text_when_not_specified);
        }
        if (TextUtils.isEmpty(texth2)) {
            text2hView.setText(R.string.default_text_when_not_specified);
        }
        if (TextUtils.isEmpty(texth3)) {
            text3hView.setText(R.string.default_text_when_not_specified);
        }
        if (TextUtils.isEmpty(texth4)) {
            text4hView.setText(R.string.default_text_when_not_specified);
        }
    }

    private class Save extends AsyncTask<String, Void, Doctor> {
        @Override
        protected Doctor doInBackground(String... params) {
            List<Doctor> doctors = new ArrayList<>();
            Doctor doctor = new Doctor();
            doctor.setName(params[0]);
            doctor.setHospitalName(params[1]);
            doctor.setHospitalAddress(params[2]);
            doctor.setHospitalPhone(params[3]);
            doctor.setAddress(params[4]);
            doctor.setPhone(params[5]);
            doctor.setEmail(params[6]);
            doctor.setType(doctorType);
            doctors.add(doctor);
            application.getDbHelper().insertOrReplaceDoctor(doctors);
            return doctor;
        }

        @Override
        protected void onPostExecute(Doctor doctor) {
            setText1(doctor.getName());
            setText2(doctor.getAddress());
            setText3(doctor.getPhone());
            setText4(doctor.getEmail());
            showNonEditMode();
        }
    }
}
