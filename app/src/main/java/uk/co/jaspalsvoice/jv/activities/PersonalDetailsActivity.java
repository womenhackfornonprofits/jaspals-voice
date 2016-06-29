package uk.co.jaspalsvoice.jv.activities;

import android.os.Bundle;

import uk.co.jaspalsvoice.jv.R;
import uk.co.jaspalsvoice.jv.views.AddressCardView;
import uk.co.jaspalsvoice.jv.views.EditableCardView;
import uk.co.jaspalsvoice.jv.views.YesNoCardView;

/**
 * Created by Ana on 2/7/2016.
 */
public class PersonalDetailsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_personal_details);

        EditableCardView firstName = (EditableCardView) findViewById(R.id.pd_first_name);
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

        EditableCardView gender = (EditableCardView) findViewById(R.id.pd_gender);
        gender.setTitle(getString(R.string.personal_details_gender));
        gender.setTitleId(R.string.personal_details_gender);
        gender.setText(preferences.getPersonalDetailGender());

        EditableCardView carerLanguage = (EditableCardView) findViewById(R.id.pd_language_of_carer);
        carerLanguage.setTitle(getString(R.string.personal_details_carer_language));
        carerLanguage.setTitleId(R.string.personal_details_carer_language);
        carerLanguage.setText(preferences.getCarerLanguage());

        YesNoCardView translatorNeeded = (YesNoCardView) findViewById(R.id.pd_translator_needed);
        translatorNeeded.setTitle(getString(R.string.personal_details_translator_needed));
        translatorNeeded.setTitleId(R.string.personal_details_translator_needed);
        String[] answers = getResources().getStringArray(R.array.yes_no_spinner_item);
        translatorNeeded.setText(preferences.getPersonalDetailsNeedTranslator() ? answers[0] : answers[1]);
    }
}
