package uk.co.jaspalsvoice.jv.views;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.LongSparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethod;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import uk.co.jaspalsvoice.jv.JvApplication;
import uk.co.jaspalsvoice.jv.JvPreferences;
import uk.co.jaspalsvoice.jv.R;

/**
 * Created by Ana on 2/21/2016.
 */
public class EditableCardView extends CardView {
    private String title;
    private String subtitle;
    private String text;
    private boolean editMode;
    private int titleId;

    private TextView titleView;
    private TextView optionalSubtitleView;
    private TextView textView;
    private EditText editView;
    private ViewGroup buttonsView;
    private Button cancelBtn;
    private Button saveBtn;

    private JvPreferences preferences;

    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    private void updateLabel() {

        String myFormat = "dd MMM yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
        editView.setText(sdf.format(myCalendar.getTime()));
    }

    public EditableCardView(Context context) {
        super(context);
        init(context);
    }

    public EditableCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EditableCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        preferences = ((JvApplication) context.getApplicationContext()).getPreferences();

        LayoutInflater inflater = LayoutInflater.from(context);
        View root = inflater.inflate(R.layout.editable_card_view, this);

        titleView = (TextView) root.findViewById(R.id.title);
        optionalSubtitleView = (TextView) root.findViewById(R.id.subtitle);
        textView = (TextView) root.findViewById(R.id.text);
        editView = (EditText) root.findViewById(R.id.edit);
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

                setEdit(text);
                showNonEditMode();
            }
        });

        saveBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editMode = !editMode;

                setText(editView.getText().toString());
                showNonEditMode();
                save();
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

    public int getTitleId() {
        return titleId;
    }

    public void setTitleId(int titleId) {
        this.titleId = titleId;
        if (titleId == R.string.personal_details_mobile ||
                titleId == R.string.personal_details_home_tel || titleId == R.string.personal_details_carer_tel) {
            editView.setInputType(InputType.TYPE_CLASS_PHONE);
        } else if (titleId == R.string.personal_details_email) {
            editView.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        } else if (titleId == R.string.personal_details_first_name || titleId == R.string.personal_details_last_name
                || titleId == R.string.town_title || titleId == R.string.county_title
                || titleId == R.string.personal_details_carer_language || titleId == R.string.personal_details_name_to_be_called||
                titleId == R.string.personal_details_main_carer || titleId == R.string.personal_details_live_with ){
            editView.setFilters(new InputFilter[] {
                    new InputFilter() {
                        public CharSequence filter(CharSequence src, int start,
                                                   int end, Spanned dst, int dstart, int dend) {
                            if(src.equals("")){ // for backspace
                                return src;
                            }
                            if(src.toString().matches("[a-zA-Z ]+")){
                                return src;
                            }
                            return "";
                        }
                    }
            });
        }
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
        optionalSubtitleView.setVisibility(TextUtils.isEmpty(subtitle) ? GONE : VISIBLE);
        optionalSubtitleView.setText(subtitle);
    }

    public void setText(String text) {
        this.text = text;
        textView.setText(TextUtils.isEmpty(text) ? getResources().getString(R.string.default_text_when_not_specified) : text);
    }

    public String getText() {
        return text;
    }

    public void setEdit(String text) {
        editView.setText(text);
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public boolean isEditMode() {
        return editMode;
    }

    private void showNonEditMode() {
        editView.setVisibility(GONE);
        buttonsView.setVisibility(GONE);
        textView.setVisibility(VISIBLE);
        showDefaultText();
        optionalSubtitleView.setVisibility(TextUtils.isEmpty(subtitle) ? GONE : VISIBLE);
        titleView.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_action_edit), null);
    }

    private void showEditMode() {
        titleView.setCompoundDrawables(null, null, null, null);
        optionalSubtitleView.setVisibility(TextUtils.isEmpty(subtitle) ? GONE : VISIBLE);
        textView.setVisibility(GONE);
        editView.setVisibility(VISIBLE);
        buttonsView.setVisibility(VISIBLE);
        if (titleId == R.string.personal_details_dob){
            new DatePickerDialog(getContext(), date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        } else {
            editView.setText(getSavedText());
        }
    }

    private void showDefaultText() {
        if (TextUtils.isEmpty(text)) {
            textView.setText(R.string.default_text_when_not_specified);
        }
    }

    private void save() {
        switch (titleId) {
            case R.string.personal_details_first_name:
                preferences.setPersonalDetailsFirstName(text);
                break;
            case R.string.personal_details_last_name:
                preferences.setPersonalDetailsLastName(text);
                break;
            case R.string.personal_details_name_to_be_called:
                preferences.setPersonalDetailsPreferredName(text);
                break;
            case R.string.personal_details_live_with:
                preferences.setPersonalDetailsLiveWith(text);
                break;
            case R.string.personal_details_email:
                if (isValidEmail(text)) {
                    preferences.setPersonalDetailsEmail(text);
                } else {
                    showEmailToast();
                }
                break;
            case R.string.personal_details_dob:
                preferences.setPersonalDetailsDateOfBirth(text);
                break;
            case R.string.personal_details_main_carer:
                preferences.setPersonalDetailsMainCarer(text);
                break;
            case R.string.personal_details_carer_tel:
                preferences.setPersonalDetailsCarerTel(text);
                break;
            case R.string.medical_allergies_title:
                preferences.setMedicalAllergies(text);
                break;
            case R.string.food_allergies_title:
                preferences.setFoodAllergies(text);
                break;
            case R.string.likes_dislikes_daily_routine:
                preferences.setLikesDislikesRoutine(text);
                break;
            case R.string.likes_dislikes_hobbies:
                preferences.setLikesDislikesHobbies(text);
                break;
            case R.string.likes_dislikes_music:
                preferences.setLikesDislikesMusic(text);
                break;
            case R.string.likes_dislikes_tv:
                preferences.setLikesDislikesTelevision(text);
                break;
            case R.string.likes_dislikes_other:
                preferences.setLikesDislikesOther(text);
                break;
            case R.string.diagnosis_title:
                preferences.setDiagnosis(text);
                break;
            case R.string.personal_details_home_tel:
                preferences.setHomeTelephone(text);
                break;
            case R.string.personal_details_mobile:
                preferences.setMobile(text);
                break;
           /* case R.string.personal_details_gender:
                preferences.setPersonalDetailGender(text);
                break;*/
            case R.string.personal_details_carer_language:
                preferences.setCarerLanguage(text);
                break;
        }
    }

    private void showEmailToast() {
        Toast.makeText(getContext(),
                getResources().getString(R.string.email_validation), Toast.LENGTH_LONG).show();
    }

    private String getSavedText() {
        String text = "";
        switch (titleId) {
            case R.string.personal_details_first_name:
                text = preferences.getPersonalDetailsFirstName();
                break;
            case R.string.personal_details_last_name:
                text = preferences.getPersonalDetailsLastName();
                break;
            case R.string.personal_details_name_to_be_called:
                text = preferences.getPersonalDetailsPreferredName();
                break;
            case R.string.personal_details_live_with:
                text = preferences.getPersonalDetailsLiveWith();
                break;
            case R.string.personal_details_email:
                text = preferences.getPersonalDetailsEmail();
                break;
            case R.string.personal_details_dob:
                text = preferences.getPersonalDetailsDateOfBirth();
                break;
            case R.string.personal_details_main_carer:
                text = preferences.getPersonalDetailsMainCarer();
                break;
            case R.string.personal_details_carer_tel:
                text = preferences.getPersonalDetailsCarerTel();
                break;
            case R.string.medical_allergies_title:
                text = preferences.getMedicalAllergies();
                break;
            case R.string.food_allergies_title:
                text = preferences.getFoodAllergies();
                break;
            case R.string.likes_dislikes_daily_routine:
                text = preferences.getLikesDislikesRoutine();
                break;
            case R.string.likes_dislikes_hobbies:
                text = preferences.getLikesDislikesHobbies();
                break;
            case R.string.likes_dislikes_music:
                text = preferences.getLikesDislikesMusic();
                break;
            case R.string.likes_dislikes_tv:
                text = preferences.getLikesDislikesTelevision();
                break;
            case R.string.likes_dislikes_other:
                text = preferences.getLikesDislikesOther();
                break;
            case R.string.diagnosis_title:
                text = preferences.getDiagnosis();
                break;
            case R.string.personal_details_home_tel:
                text = preferences.getPersonalDetailHomeTel();
                break;
            case R.string.personal_details_mobile:
                text = preferences.getPersonalDetailMobile();
                break;
           /* case R.string.personal_details_gender:
                text = preferences.getPersonalDetailGender();
                break;*/
            case R.string.personal_details_carer_language:
                text = preferences.getCarerLanguage();
                break;
        }
        return text;
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}
