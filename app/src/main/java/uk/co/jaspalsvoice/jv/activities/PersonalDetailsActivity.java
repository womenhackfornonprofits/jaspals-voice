package uk.co.jaspalsvoice.jv.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import uk.co.jaspalsvoice.jv.CommonUtils;
import uk.co.jaspalsvoice.jv.DateUtils;
import uk.co.jaspalsvoice.jv.JvApplication;
import uk.co.jaspalsvoice.jv.R;
import uk.co.jaspalsvoice.jv.views.AddressCardView;
import uk.co.jaspalsvoice.jv.views.EditableCardView;
import uk.co.jaspalsvoice.jv.views.YesNoCardView;

/**
 * Created by Ana on 2/7/2016.
 */
public class PersonalDetailsActivity extends BaseActivity {

    EditText firstName;
    EditText lastName;
    EditText preferredName;
    EditText dob;
    EditText streetAddress;
    EditText townAddress;
    EditText state;
    EditText postalCode;
    EditText country;
    EditText homeTelephone;
    EditText mobileNumber;
    EditText email;
    EditText nextOfKin;
    EditText liveWith;
    EditText mainCarer;
    EditText carerLang;
    EditText carerTel;
    EditText firstLang;
    Spinner genderSpinner;
    Spinner translatorNeededSpinner;
    LinearLayout buttonsLayout;
    Button savebutton;
    Button cancelButton;
    LinearLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_details_activity);
        initViews();
        initSpinners();
        attachListeners();
        loadData();
    }

    private void attachListeners() {
        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.hideKeyboard(PersonalDetailsActivity.this);
                if (CommonUtils.isValidEmail(email.getText())) {
                    saveData();
                    showToast(getResources().getString(R.string.save_message), Toast.LENGTH_LONG);
                } else {
                    CommonUtils.showEmailToast(PersonalDetailsActivity.this);
                }
                mainLayout.requestFocus();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
                mainLayout.requestFocus();
                CommonUtils.hideKeyboard(PersonalDetailsActivity.this);
            }
        });

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsLayout.setVisibility(View.VISIBLE);
                DateUtils.showDatePicker(dob, PersonalDetailsActivity.this);
            }
        });

        mainLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    buttonsLayout.setVisibility(View.GONE);
                } else {
                    buttonsLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if ((preferences.getGender() && position == 0)
                        || (!preferences.getGender() && position == 1)) {
                    buttonsLayout.setVisibility(View.GONE);
                } else {
                    buttonsLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        translatorNeededSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if ((preferences.getPersonalDetailsNeedTranslator() && position == 0)
                        || (!preferences.getPersonalDetailsNeedTranslator() && position == 1)) {
                    buttonsLayout.setVisibility(View.GONE);
                } else {
                    buttonsLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void loadData() {
        firstName.setText(preferences.getPersonalDetailsFirstName());
        lastName.setText(preferences.getPersonalDetailsLastName());
        preferredName.setText(preferences.getPersonalDetailsPreferredName());
        liveWith.setText(preferences.getPersonalDetailsLiveWith());
        email.setText(preferences.getPersonalDetailsEmail());
        dob.setText(preferences.getPersonalDetailsDateOfBirth());
        mainCarer.setText(preferences.getPersonalDetailsMainCarer());
        carerTel.setText(preferences.getPersonalDetailsCarerTel());
        homeTelephone.setText(preferences.getPersonalDetailHomeTel());
        mobileNumber.setText(preferences.getPersonalDetailMobile());
        carerLang.setText(preferences.getCarerLanguage());
        postalCode.setText(preferences.getZipCode());
        country.setText(preferences.getCountry());
        genderSpinner.setSelection(preferences.getGender() ? 0 : 1);

        translatorNeededSpinner.setSelection(preferences.getPersonalDetailsNeedTranslator() ? 0 : 1);
        nextOfKin.setText(preferences.getNextOfKin());
        firstLang.setText(preferences.getFirstLanguage());
        streetAddress.setText(preferences.getStreet());
        townAddress.setText(preferences.getTown());
        state.setText(preferences.getCounty());
    }

    private void saveData() {
        preferences.setPersonalDetailsFirstName(firstName.getText().toString());
        preferences.setPersonalDetailsLastName(lastName.getText().toString());
        preferences.setPersonalDetailsPreferredName(preferredName.getText().toString());
        preferences.setPersonalDetailsLiveWith(liveWith.getText().toString());
        preferences.setPersonalDetailsEmail(email.getText().toString());
        preferences.setPersonalDetailsDateOfBirth(dob.getText().toString());
        preferences.setPersonalDetailsMainCarer(mainCarer.getText().toString());
        preferences.setPersonalDetailsCarerTel(carerTel.getText().toString());
        preferences.setHomeTelephone(homeTelephone.getText().toString());
        preferences.setMobile(mobileNumber.getText().toString());
        preferences.setCarerLanguage(carerLang.getText().toString());
        preferences.setStreet(streetAddress.getText().toString());
        preferences.setTown(townAddress.getText().toString());
        preferences.setCounty(state.getText().toString());
        preferences.setZipCode(postalCode.getText().toString());
        preferences.setCountry(country.getText().toString());
        preferences.setGender(genderSpinner.getSelectedItemPosition() == 0);
        preferences.setPersonalDetailsNeedTranslator
                (translatorNeededSpinner.getSelectedItemPosition() == 0);
        preferences.setNextOfKin(nextOfKin.getText().toString());
        preferences.setFirstLanguage(firstLang.getText().toString());
        mainLayout.requestFocus();
    }

    private void initSpinners() {

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,
                getResources().getStringArray(R.array.gender_spinner));
        genderAdapter.setDropDownViewResource(R.layout.spinner_item);
        genderSpinner.setAdapter(genderAdapter);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,
                getResources().getStringArray(R.array.yes_no_spinner_item));
        adapter.setDropDownViewResource(R.layout.spinner_item);
        translatorNeededSpinner.setAdapter(adapter);

    }

    private void initViews() {
        firstName = (EditText) findViewById(R.id.firstNameEdittext);
        lastName = (EditText) findViewById(R.id.lastNameEdittext);
        preferredName = (EditText) findViewById(R.id.preferredNameEdittext);
        dob = (EditText) findViewById(R.id.dobEdittext);
        streetAddress = (EditText) findViewById(R.id.streetEdittext);
        townAddress = (EditText) findViewById(R.id.townEdittext);
        state = (EditText) findViewById(R.id.countyEdittext);
        postalCode = (EditText) findViewById(R.id.zipCodeEdittext);
        country = (EditText) findViewById(R.id.countryEdittext);
        homeTelephone = (EditText) findViewById(R.id.homeTelephoneEdittext);
        mobileNumber = (EditText) findViewById(R.id.mobileNumberEdittext);
        email = (EditText) findViewById(R.id.emailEdittext);
        nextOfKin = (EditText) findViewById(R.id.nextOfKinEdittext);
        liveWith = (EditText) findViewById(R.id.liveWithEdittext);
        mainCarer = (EditText) findViewById(R.id.mainCarerEdittext);
        carerLang = (EditText) findViewById(R.id.carerLanguageEdittext);
        carerTel = (EditText) findViewById(R.id.carerTelEdittext);
        firstLang = (EditText) findViewById(R.id.firstLanguageEdittext);
        genderSpinner = (Spinner) findViewById(R.id.genderSpinner);
        translatorNeededSpinner = (Spinner) findViewById(R.id.translatorNeededSpinner);
        buttonsLayout = (LinearLayout) findViewById(R.id.buttons);
        savebutton = (Button) findViewById(R.id.save);
        cancelButton = (Button) findViewById(R.id.cancel);
        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
    }


    /* EditableCardView firstName = (EditableCardView) findViewById(R.id.pd_first_name);
        firstName.setTitle(getString(R.string.personal_details_first_name));
        firstName.setTitleId(R.string.personal_details_first_name);
        firstName.setText(preferences.getPersonalDetailsFirstName());

        EditableCardView lastName = (EditableCardView) findViewById(R.id.pd_last_name);
        lastName.setTitle(getString(R.string.personal_details_last_name));
        lastName.setTitleId(R.string.personal_details_last_name);
        lastName.setText(preferences.getPersonalDetailsLastName());


        EditableCardView nickname = (EditableCardView) findViewById(R.id.pd_name_to_be_called);
        nickname.setTitle(getString(R.string.personal_details_name_to_be_called));
        nickname.setTitleId(R.string.personal_details_name_to_be_called);
        nickname.setText(preferences.getPersonalDetailsPreferredName());

        EditableCardView liveWith = (EditableCardView) findViewById(R.id.pd_live_with);
        liveWith.setTitle(getString(R.string.personal_details_live_with));
        liveWith.setTitleId(R.string.personal_details_live_with);
        liveWith.setText(preferences.getPersonalDetailsLiveWith());

        EditableCardView email = (EditableCardView) findViewById(R.id.pd_email);
        email.setTitle(getString(R.string.personal_details_email));
        email.setTitleId(R.string.personal_details_email);
        email.setText(preferences.getPersonalDetailsEmail());

        EditableCardView dob = (EditableCardView) findViewById(R.id.pd_dob);
        dob.setTitle(getString(R.string.personal_details_dob));
        dob.setTitleId(R.string.personal_details_dob);
        dob.setText(preferences.getPersonalDetailsDateOfBirth());

        EditableCardView mainCarer = (EditableCardView) findViewById(R.id.pd_main_carer);
        mainCarer.setTitle(getString(R.string.personal_details_main_carer));
        mainCarer.setTitleId(R.string.personal_details_main_carer);
        mainCarer.setText(preferences.getPersonalDetailsMainCarer());

        EditableCardView carerTel = (EditableCardView) findViewById(R.id.pd_carer_tel);
        carerTel.setTitle(getString(R.string.personal_details_carer_tel));
        carerTel.setTitleId(R.string.personal_details_carer_tel);
        carerTel.setText(preferences.getPersonalDetailsCarerTel());

        AddressCardView addressCardView = (AddressCardView) findViewById(R.id.pd_address);
        addressCardView.setTitle(getString(R.string.address_title),
                getString(R.string.street_title),
                getString(R.string.town_title),
                getString(R.string.county_title));
        addressCardView.setMainTitleId(R.string.address_title);
        addressCardView.setStreetTitleId(R.string.street_title);
        addressCardView.setTownTitleId(R.string.town_title);
        addressCardView.setCountyTitleId(R.string.county_title);
        addressCardView.setText(preferences.getStreet(), preferences.getTown(), preferences.getCounty());

        EditableCardView homeTelephone = (EditableCardView) findViewById(R.id.pd_home_telephone);
        homeTelephone.setTitle(getString(R.string.personal_details_home_tel));
        homeTelephone.setTitleId(R.string.personal_details_home_tel);
        homeTelephone.setText(preferences.getPersonalDetailHomeTel());

        EditableCardView mobile = (EditableCardView) findViewById(R.id.pd_mobile_number);
        mobile.setTitle(getString(R.string.personal_details_mobile));
        mobile.setTitleId(R.string.personal_details_mobile);
        mobile.setText(preferences.getPersonalDetailMobile());

        YesNoCardView gender = (YesNoCardView) findViewById(R.id.pd_gender);
        gender.setTitle(getString(R.string.personal_details_gender));
        gender.setTitleId(R.string.personal_details_gender);
        String[] answers = getResources().getStringArray(R.array.gender_spinner);
        gender.setText(preferences.getGender() ? answers[0] : answers[1]);

        *//*EditableCardView gender = (EditableCardView) findViewById(R.id.pd_gender);
        gender.setTitle(getString(R.string.personal_details_gender));
        gender.setTitleId(R.string.personal_details_gender);
        gender.setText(preferences.getPersonalDetailGender());
*//*
        EditableCardView carerLanguage = (EditableCardView) findViewById(R.id.pd_language_of_carer);
        carerLanguage.setTitle(getString(R.string.personal_details_carer_language));
        carerLanguage.setTitleId(R.string.personal_details_carer_language);
        carerLanguage.setText(preferences.getCarerLanguage());

        YesNoCardView translatorNeeded = (YesNoCardView) findViewById(R.id.pd_translator_needed);
        translatorNeeded.setTitle(getString(R.string.personal_details_translator_needed));
        translatorNeeded.setTitleId(R.string.personal_details_translator_needed);
        answers = getResources().getStringArray(R.array.yes_no_spinner_item);
        translatorNeeded.setText(preferences.getPersonalDetailsNeedTranslator() ? answers[0] : answers[1]);*/

}
