package uk.co.jaspalsvoice.jv.views;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import uk.co.jaspalsvoice.jv.JvApplication;
import uk.co.jaspalsvoice.jv.JvPreferences;
import uk.co.jaspalsvoice.jv.R;

/**
 * Created by Ana on 2/21/2016.
 */
public class AddressCardView extends CardView {
    private String title;
    private String subtitle;
    private String streetText;
    private String townText;
    private String countyText;
    private boolean editMode;
    private int mainTitleId;
    private int streetTitleId;
    private int townTitleId;
    private int countyTitleId;

    private TextView titleView;
    private TextView streetTitleView;
    private TextView townTitleView;
    private TextView countyTitleView;
    //private TextView optionalSubtitleView;
   // private TextView textView;
    private TextView streetTextView;
    private TextView townTextView;
    private TextView countyTextView;

    private EditText streetEditView;
    private EditText townEditView;
    private EditText countyEditView;

    private ViewGroup buttonsView;
    private Button cancelBtn;
    private Button saveBtn;

    private JvPreferences preferences;

    public AddressCardView(Context context) {
        super(context);
        init(context);
    }

    public AddressCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AddressCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        preferences = ((JvApplication) context.getApplicationContext()).getPreferences();

        LayoutInflater inflater = LayoutInflater.from(context);
        View root = inflater.inflate(R.layout.address_card_view, this);

        titleView = (TextView) root.findViewById(R.id.title);
        streetTitleView = (TextView) root.findViewById(R.id.title2);
        townTitleView = (TextView) root.findViewById(R.id.title3);
        countyTitleView = (TextView) root.findViewById(R.id.title4);
//        optionalSubtitleView = (TextView) root.findViewById(R.id.subtitle);
//        textView = (TextView) root.findViewById(R.id.text);
        streetTextView = (TextView)root.findViewById(R.id.text2);
        townTextView = (TextView)root.findViewById(R.id.text3);
        countyTextView = (TextView)root.findViewById(R.id.text4);
        streetEditView = (EditText) root.findViewById(R.id.edit2);
        townEditView = (EditText) root.findViewById(R.id.edit3);
        countyEditView = (EditText) root.findViewById(R.id.edit4);
        buttonsView = (ViewGroup) root.findViewById(R.id.buttons);
        cancelBtn = (Button) root.findViewById(R.id.cancel);
        saveBtn = (Button) root.findViewById(R.id.save);

        showDefaultText();

        streetTitleView.setOnClickListener(new OnClickListener() {
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

        townTitleView.setOnClickListener(new OnClickListener() {
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

        countyTitleView.setOnClickListener(new OnClickListener() {
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

                setEdit(streetText, townText, countyText);
                showNonEditMode();
            }
        });

        saveBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editMode = !editMode;

                setText(streetEditView.getText().toString(),
                        townEditView.getText().toString(),
                        countyEditView.getText().toString());
                showNonEditMode();
                save();
            }
        });
    }

    public void setTitle(String addressTitle, String streetTitle, String townTitle, String
                         countyTitle) {
        this.title = addressTitle;
        titleView.setText(title);
        streetTitleView.setText(streetTitle);
        townTitleView.setText(townTitle);
        countyTitleView.setText(countyTitle);
    }

    public String getTitle() {
        return title;
    }

   /* public int getTitleId() {
        return titleId;
    }

    public void setTitleId(int titleId) {
        this.titleId = titleId;
    }*/

    public String getSubtitle() {
        return subtitle;
    }
/*

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
        optionalSubtitleView.setVisibility(TextUtils.isEmpty(subtitle) ? GONE : VISIBLE);
        optionalSubtitleView.setText(subtitle);
    }
*/

    public void setText(String streetText, String townText, String countyText) {
        this.streetText = streetText;
        this.townText = townText;
        this.countyText = countyText;
        streetTextView.setText(TextUtils.isEmpty(streetText) ? getResources().getString(R.string.default_text_when_not_specified) : streetText);
        townTextView.setText(TextUtils.isEmpty(townText) ? getResources().getString(R.string.default_text_when_not_specified) : townText);
        countyTextView.setText(TextUtils.isEmpty(countyText) ? getResources().getString(R.string.default_text_when_not_specified) : countyText);
    }

    /*public String getText() {
        return streetText;
    }*/

   public String getStreetText() {
       return streetText;
   }

    public String getTownText() {
        return townText;
    }

    public String getCountyText() {
        return countyText;
    }

    public void setEdit(String streetText, String townText, String countyText) {
        streetEditView.setText(streetText);
        townEditView.setText(townText);
        countyEditView.setText(countyText);
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public boolean isEditMode() {
        return editMode;
    }

    private void showNonEditMode() {
//        editView.setVisibility(GONE);
        streetEditView.setVisibility(GONE);
        townEditView.setVisibility(GONE);
        countyEditView.setVisibility(GONE);
        buttonsView.setVisibility(GONE);
        //textView.setVisibility(VISIBLE);
        streetTextView.setVisibility(VISIBLE);
        townTextView.setVisibility(VISIBLE);
        countyTextView.setVisibility(VISIBLE);
        showDefaultText();
        //optionalSubtitleView.setVisibility(TextUtils.isEmpty(subtitle) ? GONE : VISIBLE);
        streetTitleView.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_action_edit), null);
        townTitleView.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_action_edit), null);
        countyTitleView.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_action_edit), null);
    }

    private void showEditMode() {
        titleView.setCompoundDrawables(null, null, null, null);
       // optionalSubtitleView.setVisibility(TextUtils.isEmpty(subtitle) ? GONE : VISIBLE);
//        textView.setVisibility(GONE);
        streetTextView.setVisibility(GONE);
        townTextView.setVisibility(GONE);
        countyTextView.setVisibility(GONE);
        streetEditView.setText(preferences.getStreet());
        townEditView.setText(preferences.getTown());
        countyEditView.setText(preferences.getCounty());
//        editView.setVisibility(VISIBLE);
        streetEditView.setVisibility(VISIBLE);
        townEditView.setVisibility(VISIBLE);
        countyEditView.setVisibility(VISIBLE);
        buttonsView.setVisibility(VISIBLE);
    }

    private void showDefaultText() {
        if (TextUtils.isEmpty(streetText)) {
            streetTextView.setText(R.string.default_text_when_not_specified);
        }

        if (TextUtils.isEmpty(townText)) {
            townTextView.setText(R.string.default_text_when_not_specified);
        }

        if (TextUtils.isEmpty(countyText)) {
            countyTextView.setText(R.string.default_text_when_not_specified);
        }

    }

    private void save() {
        preferences.setStreet(streetText);
        preferences.setCounty(countyText);
        preferences.setTown(townText);
    }

   /* private String getSavedText() {
        String text = "";
        switch (titleId) {
            case R.string.personal_details_first_name:
                text = preferences.getPersonalDetailsFirstName();
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
        }
        return text;
    }*/

    public int getMainTitleId() {
        return mainTitleId;
    }

    public void setMainTitleId(int mainTitleId) {
        this.mainTitleId = mainTitleId;
    }

    public int getStreetTitleId() {
        return streetTitleId;
    }

    public void setStreetTitleId(int streetTitleId) {
        this.streetTitleId = streetTitleId;
    }

    public int getTownTitleId() {
        return townTitleId;
    }

    public void setTownTitleId(int townTitleId) {
        this.townTitleId = townTitleId;
    }

    public int getCountyTitleId() {
        return countyTitleId;
    }

    public void setCountyTitleId(int countyTitleId) {
        this.countyTitleId = countyTitleId;
    }
}
