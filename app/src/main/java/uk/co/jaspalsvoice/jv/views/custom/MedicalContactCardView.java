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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import uk.co.jaspalsvoice.jv.JvApplication;
import uk.co.jaspalsvoice.jv.R;
import uk.co.jaspalsvoice.jv.models.Doctor;

/**
 * Created by Ana on 2/21/2016.
 */
public class MedicalContactCardView extends CardView {
    private String title;
    private String text1;
    private String text2;
    private String text3;
    private String text4;
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

    private TextView labelf;
    private TextView textf;
    private EditText editf;


    private ViewGroup buttonsView;
    private Button cancelBtn;
    private Button saveBtn;
    private Button editButton;
    private LinearLayout mainLayout;
    View root;

    private JvApplication application;
    private String texttf;

    public MedicalContactCardView(Context context) {
        super(context);
        init(context);
    }

    public MedicalContactCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MedicalContactCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        application = (JvApplication) context.getApplicationContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        root = inflater.inflate(R.layout.medical_contact_card_view, this);

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

        labelf = (TextView) root.findViewById(R.id.labelf);
        textf= (TextView) root.findViewById(R.id.textf);
        editf= (EditText) root.findViewById(R.id.editf);

        buttonsView = (ViewGroup) root.findViewById(R.id.buttons);
        cancelBtn = (Button) root.findViewById(R.id.cancel);
        saveBtn = (Button) root.findViewById(R.id.save);
        editButton = (Button) root.findViewById(R.id.editButton);
//        mainLayout = (LinearLayout) root.findViewById(R.id.mainLayout);

        showDefaultText();

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
                if (TextUtils.isEmpty(edit4View.getText()) || isValidEmail(edit4View.getText())) {
                    new Save().execute(edit1View.getText().toString(), edit2View.getText().toString(),
                            edit3View.getText().toString(), edit4View.getText().toString(),
                            editf.getText().toString());
                } else {
                    showEmailToast();
                }
            }
        });
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
        edit3View.setInputType(InputType.TYPE_CLASS_PHONE);
        edit4View.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

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

    public void setTextf(String text) {
        this.texttf = text;
        textf.setText(text);
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

    public void setLabelFView(String text){
        labelf.setText(text);
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

        editf.setVisibility(GONE);
        textf.setVisibility(VISIBLE);


        buttonsView.setVisibility(GONE);
        showDefaultText();
        editButton.setVisibility(VISIBLE);
    }

    private void showEditMode() {
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
        editf.setVisibility(VISIBLE);
        textf.setVisibility(GONE);
        buttonsView.setVisibility(VISIBLE);
        editButton.setVisibility(GONE);
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
            doctor.setFax(params[4]);
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
            setTextf(doctor.getFax());
            showNonEditMode();
        }
    }
}
