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
public class TeamMemberCardView extends CardView {
    private String title;

    private String text1;
    private String text2;
    private String text3;
    private String text4;
    private String doctorType;
    private boolean editMode;

    private TextView titleView;
    private EditText edit1View;
    private EditText edit2View;
    ;
    private EditText edit3View;
    private EditText edit4View;
    private ViewGroup buttonsView;
    private JvApplication application;
    private Context mContext;


    public TeamMemberCardView(Context context) {
        super(context);
        init(context);
    }

    public TeamMemberCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TeamMemberCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        mContext = context;
        application = (JvApplication) context.getApplicationContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View root = inflater.inflate(R.layout.medical_contact_card_view, this);

        titleView = (TextView) root.findViewById(R.id.title);

        edit1View = (EditText) root.findViewById(R.id.edit1);
        edit2View = (EditText) root.findViewById(R.id.edit2);
        edit3View = (EditText) root.findViewById(R.id.edit3);
        edit4View = (EditText) root.findViewById(R.id.edit4);
        EditText edit5View = (EditText) root.findViewById(R.id.edit5);
        edit5View.setVisibility(GONE);

        buttonsView = (ViewGroup) root.findViewById(R.id.buttons);
        Button cancelBtn = (Button) root.findViewById(R.id.cancel);
        Button saveBtn = (Button) root.findViewById(R.id.save);
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
        cancelBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editMode = !editMode;

                setEdit(edit1View, text1);
                setEdit(edit2View, text2);
                setEdit(edit3View, text3);
                setEdit(edit4View, text4);
                showNonEditMode();
            }
        });

        saveBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edit4View.getText()) || isValidEmail(edit4View.getText())) {
                    editMode = !editMode;
                    new Save().execute(edit1View.getText().toString().trim(), edit2View.getText().toString().trim(),
                            edit3View.getText().toString().trim(), edit4View.getText().toString().trim());
                } else {
                    showEmailToast();
                }
            }
        });
    }

    private void registerFocusListener() {
        edit1View.setOnFocusChangeListener(editTextFocusListener);
        edit2View.setOnFocusChangeListener(editTextFocusListener);
        edit3View.setOnFocusChangeListener(editTextFocusListener);
        edit4View.setOnFocusChangeListener(editTextFocusListener);
    }

    OnFocusChangeListener editTextFocusListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                buttonsView.setVisibility(VISIBLE);
            }
        }
    };

    private boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

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

        edit4View.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
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
        edit1View.setText(text);
    }

    public void setText2(String text) {
        this.text2 = text;
        edit2View.setText(text);
    }

    public void setText3(String text) {
        this.text3 = text;
        edit3View.setText(text);
    }

    public void setText4(String text) {
        this.text4 = text;
        edit4View.setText(text);
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
        if (mContext instanceof GpActivity) {
            ((GpActivity) mContext).removeEditFocus();
        }
        buttonsView.setVisibility(GONE);
    }

    private void showEditMode() {
        buttonsView.setVisibility(VISIBLE);
    }

    private class Save extends AsyncTask<String, Void, Doctor> {
        @Override
        protected Doctor doInBackground(String... params) {
            List<Doctor> doctors = new ArrayList<>();
            Doctor doctor = new Doctor();
            doctor.setName(params[0]);
            doctor.setAddress(params[1]);
            doctor.setPhone(params[2]);
            doctor.setEmail(params[3]);
            doctor.setType(doctorType);
            doctors.add(doctor);
            application.getDbHelper().insertOrReplaceTeamMember(doctors);
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
