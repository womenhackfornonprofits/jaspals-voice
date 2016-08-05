package uk.co.jaspalsvoice.jv.views.custom;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import uk.co.jaspalsvoice.jv.JvApplication;
import uk.co.jaspalsvoice.jv.R;
import uk.co.jaspalsvoice.jv.activities.GpActivity;
import uk.co.jaspalsvoice.jv.models.Doctor;

/**
 * Created by Ana on 2/21/2016.
 */
public class ConsultantCardView extends CardView {
    private String title;
    private String text1;
    private String text2;
    private String texth1;
    private String texth2;
    private String texth3;
    private String texth4;

    private String doctorType;
    private boolean editMode;

    private TextView titleView;
    private EditText edit1View;
    private EditText edit2View;
    private EditText edith1View;
    private EditText edith2View;
    private EditText edith3View;
    private EditText edith4View;

    private ViewGroup buttonsView;
    private Button editButton;
    private Context mContext;

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
        mContext = context;
        application = (JvApplication) context.getApplicationContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View root = inflater.inflate(R.layout.consultant_contact_card_view, this);

        titleView = (TextView) root.findViewById(R.id.title);
        edit1View = (EditText) root.findViewById(R.id.edit1);
        edit2View = (EditText) root.findViewById(R.id.edit2);
        edith1View = (EditText) root.findViewById(R.id.edith1);
        edith2View = (EditText) root.findViewById(R.id.edith2);
        edith3View = (EditText) root.findViewById(R.id.edith3);
        edith4View = (EditText) root.findViewById(R.id.edith4);

        buttonsView = (ViewGroup) root.findViewById(R.id.buttons);
        Button cancelBtn = (Button) buttonsView.findViewById(R.id.cancel);
        Button saveBtn = (Button) buttonsView.findViewById(R.id.save);
        editButton = (Button) root.findViewById(R.id.editButton);

        registerFocusListener();

        root.setOnClickListener(new OnClickListener() {
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

        editButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editMode = !editMode;

                if (editMode) {
                    edit1View.requestFocus();
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
                setEdit(edith1View, texth1);
                setEdit(edith2View, texth2);
                setEdit(edith3View, texth3);
                setEdit(edith4View, texth4);

                showNonEditMode();
            }
        });

        saveBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edit2View.getText()) || isValidEmail(edit2View.getText())) {
                    editMode = !editMode;
                    new Save().execute(
                            edit1View.getText().toString().trim(), edit2View.getText().toString().trim(),
                            edith1View.getText().toString().trim(), edith2View.getText().toString().trim(),
                            edith3View.getText().toString(), edith4View.getText().toString());
                } else {
                    showEmailToast();
                }
            }
        });
    }

    private void registerFocusListener() {
        edit1View.setOnFocusChangeListener(editTextFocusListener);
        edit2View.setOnFocusChangeListener(editTextFocusListener);
        edith1View.setOnFocusChangeListener(editTextFocusListener);
        edith2View.setOnFocusChangeListener(editTextFocusListener);
        edith3View.setOnFocusChangeListener(editTextFocusListener);
        edith4View.setOnFocusChangeListener(editTextFocusListener);
    }

    OnFocusChangeListener editTextFocusListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                buttonsView.setVisibility(VISIBLE);
                editButton.setVisibility(GONE);
            }
        }
    };

    public void setTitle(String title) {
        this.title = title;
        titleView.setText(title);
        edit1View.setFilters(new InputFilter[]{
                new InputFilter() {
                    public CharSequence filter(CharSequence src, int start,
                                               int end, Spanned dst, int dstart, int dend) {
                        if (src.equals("")) { // for backspace
                            return src;
                        }
                        if (src.toString().matches("[a-zA-Z ]+")) {
                            return src;
                        }
                        return "";
                    }
                }
        });

        edit2View.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        edith3View.setInputType(InputType.TYPE_CLASS_PHONE);
    }

    private boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private void showEmailToast() {
        Toast.makeText(getContext(),
                getResources().getString(R.string.email_validation), Toast.LENGTH_LONG).show();
    }

    public String getTitle() {
        return title;
    }

    public void setText1(String text) {
        this.text1 = text;
        setEdit(edit1View, text1);
    }

    public void setText2(String text) {
        this.text2 = text;
        setEdit(edit2View, text2);
    }

    public void setTexth1(String text) {
        this.texth1 = text;
        setEdit(edith1View, texth1);
    }

    public void setTexth2(String text) {
        this.texth2 = text;
        setEdit(edith2View, texth2);
    }

    public void setTexth3(String text) {
        this.texth3 = text;
        setEdit(edith3View, texth3);
    }

    public void setTexth4(String text) {
        this.texth4 = text;
        setEdit(edith4View, texth4);
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

    private void showNonEditMode() {
        buttonsView.setVisibility(GONE);
        editButton.setVisibility(VISIBLE);

        if (mContext instanceof GpActivity) {
            ((GpActivity) mContext).removeEditFocus();
        }
    }

    private void showEditMode() {
        buttonsView.setVisibility(VISIBLE);
        editButton.setVisibility(GONE);
    }

    private class Save extends AsyncTask<String, Void, Doctor> {
        @Override
        protected Doctor doInBackground(String... params) {
            List<Doctor> doctors = new ArrayList<>();
            Doctor doctor = new Doctor();
            doctor.setName(params[0]);
            doctor.setEmail(params[1]);
            doctor.setHospitalName(params[2]);
            doctor.setHospitalAddress(params[3]);
            doctor.setHospitalPhone(params[4]);
            doctor.setFax(params[5]);
            doctor.setType(doctorType);
            doctors.add(doctor);
            application.getDbHelper().insertOrReplaceDoctor(doctors);
            return doctor;
        }

        @Override
        protected void onPostExecute(Doctor doctor) {
            setText1(doctor.getName());
            setText2(doctor.getEmail());
            setTexth1(doctor.getHospitalName());
            setTexth2(doctor.getHospitalAddress());
            setTexth3(doctor.getHospitalPhone());
            setTexth4(doctor.getFax());
            showNonEditMode();
        }
    }
}
