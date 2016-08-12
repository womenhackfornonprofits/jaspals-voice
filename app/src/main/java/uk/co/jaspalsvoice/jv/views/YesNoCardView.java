package uk.co.jaspalsvoice.jv.views;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import uk.co.jaspalsvoice.jv.JvApplication;
import uk.co.jaspalsvoice.jv.JvPreferences;
import uk.co.jaspalsvoice.jv.R;

/**
 * Created by Ana on 2/21/2016.
 */
public class YesNoCardView extends CardView {
    private String title;
    private String subtitle;
    private String text;
    private boolean editMode;
    private int titleId;

    private String[] options;

    private TextView titleView;
    private TextView optionalSubtitleView;
    private TextView textView;
    private Spinner spinnerView;
    private ViewGroup buttonsView;
    private Button cancelBtn;
    private Button saveBtn;

    private JvPreferences preferences;

    public YesNoCardView(Context context) {
        super(context);
        init(context);
    }

    public YesNoCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public YesNoCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        preferences = ((JvApplication) context.getApplicationContext()).getPreferences();

        LayoutInflater inflater = LayoutInflater.from(context);
        View root = inflater.inflate(R.layout.yes_no_card_view, this);

        titleView = (TextView) root.findViewById(R.id.title);
        optionalSubtitleView = (TextView) root.findViewById(R.id.subtitle);
        textView = (TextView) root.findViewById(R.id.text);
        spinnerView = (Spinner) root.findViewById(R.id.spinner);
        buttonsView = (ViewGroup) root.findViewById(R.id.buttons);
        cancelBtn = (Button) root.findViewById(R.id.cancel);
        saveBtn = (Button) root.findViewById(R.id.save);

        //options = getResources().getStringArray(R.array.yes_no_spinner_item);
//        textView.setText(options[0]);

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
                setSpinner(text);
                showNonEditMode();
            }
        });

        saveBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editMode = !editMode;
                setText(spinnerView.getSelectedItem().toString());
                showNonEditMode();
                save();
            }
        });

//        getData();
    }


    public void setTitle(String title) {
        this.title = title;
        titleView.setText(title);
    }

    public String getTitle() {
        return title;
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
        textView.setText(text);
    }

    public String getText() {
        return text;
    }


    public void setSpinner(String text) {
        for (int i = 0; i < options.length; i++) {
            if (options[i].equals(text)) {
                spinnerView.setSelection(i);
                break;
            }
        }
    }

    public void setTitleId(int titleId) {
        this.titleId = titleId;
        if (titleId == R.string.personal_details_gender) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    getContext(), android.R.layout.simple_spinner_item,
                    getResources().getStringArray(R.array.gender_spinner));
            options = getResources().getStringArray(R.array.gender_spinner);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerView.setAdapter(adapter);
        } else {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    getContext(), android.R.layout.simple_spinner_item,
                    getResources().getStringArray(R.array.yes_no_spinner_item));
            options = getResources().getStringArray(R.array.yes_no_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerView.setAdapter(adapter);
        }


        textView.setText(options[0]);
    }

    public void showNonEditMode() {
        spinnerView.setVisibility(GONE);
        buttonsView.setVisibility(GONE);
        textView.setVisibility(VISIBLE);
        optionalSubtitleView.setVisibility(TextUtils.isEmpty(subtitle) ? GONE : VISIBLE);
        titleView.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_action_edit), null);
    }

    public void showEditMode() {
        titleView.setCompoundDrawables(null, null, null, null);
        optionalSubtitleView.setVisibility(TextUtils.isEmpty(subtitle) ? GONE : VISIBLE);
        textView.setVisibility(GONE);
        spinnerView.setVisibility(VISIBLE);
        setSpinner(textView.getText().toString());
        if (titleId == R.string.renal_dosage || titleId == R.string.hepatic_dosage){
            buttonsView.setVisibility(GONE);
        } else {
            buttonsView.setVisibility(VISIBLE);
        }
    }

    private void save() {
        int selection = spinnerView.getSelectedItemPosition();
        switch (titleId) {
            case R.string.personal_details_translator_needed:
                preferences.setPersonalDetailsNeedTranslator(selection == 0);
                break;
            case R.string.about_me_breathing:
                preferences.setBreathingDifficultiesStatus(selection == 0);
                break;
            case R.string.about_me_physical_abilities:
                preferences.setPhysicalAbilitiesStatus(selection == 0);
                break;
            case R.string.about_me_swallowing_difficulties:
                preferences.setSwallowingAbilitiesStatus(selection == 0);
                break;
            case R.string.personal_details_gender:
                preferences.setGender(selection == 0);
        }
    }

    public int getSelection() {
        return spinnerView.getSelectedItemPosition();
    }

    public void disableTitle(boolean setData) {
        titleView.setCompoundDrawables(null, null, null, null);
        titleView.setClickable(false);
        titleView.setFocusable(false);
        if (setData) {
            textView.setText((String) spinnerView.getSelectedItem());
        }
    }

   /* private void getData() {
        switch (titleId) {
            case R.string.personal_details_translator_needed:
                textView.setText();
                break;
            case R.string.about_me_breathing:
                preferences.setBreathingDifficultiesStatus(selection == 0);
                break;
            case R.string.about_me_physical_abilities:
                preferences.setPhysicalAbilitiesStatus(selection == 0);
                break;
            case R.string.about_me_swallowing_difficulties:
                preferences.setSwallowingAbilitiesStatus(selection == 0);
                break;
        }
    }*/
}
