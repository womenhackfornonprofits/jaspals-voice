package uk.co.jaspalsvoice.jv;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Ana on 3/21/2016.
 */
public class JvPreferences {

    private static final String PREFS_FILE_NAME = "JvPreferences.txt";

    private static final String PERSONAL_DETAILS_FIRST_NAME = "pd_name";
    private static final String PERSONAL_DETAILS_PREFERRED_NAME = "pd_preferred_name";
    private static final String PERSONAL_DETAILS_LIVE_WITH = "pd_live_with";
    private static final String PERSONAL_DETAILS_EMAIL = "pd_email";
    private static final String PERSONAL_DETAILS_DATE_OF_BIRTH = "pd_dob";
    private static final String PERSONAL_DETAILS_MAIN_CARER = "pd_main_carer";
    private static final String PERSONAL_DETAILS_CARER_TEL = "pd_carer_tel";
    private static final String PERSONAL_DETAILS_NEED_TRANSLATOR = "pd_need_translator";

    private static final String MEDICAL_ALLERGIES = "ma_medical_allergies";
    private static final String FOOD_ALLERGIES = "fa_food_allergies";

    private static final String LIKES_DISLIKES_ROUTINE = "ld_routine";
    private static final String LIKES_DISLIKES_HOBBIES = "ld_hobbies";
    private static final String LIKES_DISLIKES_MUSIC = "ld_music";
    private static final String LIKES_DISLIKES_TELEVISION = "ld_television";
    private static final String LIKES_DISLIKES_OTHER = "ld_other";

    private static final String DIAGNOSIS = "d_diagnosis";
    private static final String PERSONAL_DETAILS_STREET = "pd_street";
    private static final String PERSONAL_DETAILS_TOWN = "pd_town";
    private static final String PERSONAL_DETAILS_COUNTY = "pd_county";
    private static final String HOME_TELEPHONE = "pd_home_telephone";
    private static final String PERSONAL_DETAILS_LAST_NAME = "pd_last_name";
    private static final String MOBILE = "pd_mobile";
    private static final String GENDER = "pd_gender";
    private static final String CARER_LANG = "pd_carer_language";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public JvPreferences(Context context) {
        preferences = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public String getPersonalDetailsFirstName() {
        return preferences.getString(PERSONAL_DETAILS_FIRST_NAME, "");
    }

    public void setPersonalDetailsFirstName(String name) {
        editor.putString(PERSONAL_DETAILS_FIRST_NAME, name).apply();
    }

    public String getPersonalDetailsLastName() {
        return preferences.getString(PERSONAL_DETAILS_LAST_NAME, "");
    }

    public void setPersonalDetailsLastName(String name) {
        editor.putString(PERSONAL_DETAILS_LAST_NAME, name).apply();
    }

    public String getPersonalDetailsPreferredName() {
        return preferences.getString(PERSONAL_DETAILS_PREFERRED_NAME, "");
    }

    public void setPersonalDetailsPreferredName(String name) {
        editor.putString(PERSONAL_DETAILS_PREFERRED_NAME, name).apply();
    }

    public String getPersonalDetailsLiveWith() {
        return preferences.getString(PERSONAL_DETAILS_LIVE_WITH, "");
    }

    public void setPersonalDetailsLiveWith(String name) {
        editor.putString(PERSONAL_DETAILS_LIVE_WITH, name).apply();
    }

    public String getPersonalDetailsEmail() {
        return preferences.getString(PERSONAL_DETAILS_EMAIL, "");
    }

    public void setPersonalDetailsEmail(String email) {
        editor.putString(PERSONAL_DETAILS_EMAIL, email).apply();
    }

    public String getPersonalDetailsDateOfBirth() {
        return preferences.getString(PERSONAL_DETAILS_DATE_OF_BIRTH, "");
    }

    public void setPersonalDetailsDateOfBirth(String dob) {
        editor.putString(PERSONAL_DETAILS_DATE_OF_BIRTH, dob).apply();
    }

    public String getPersonalDetailsMainCarer() {
        return preferences.getString(PERSONAL_DETAILS_MAIN_CARER, "");
    }

    public void setPersonalDetailsMainCarer(String name) {
        editor.putString(PERSONAL_DETAILS_MAIN_CARER, name).apply();
    }

    public String getPersonalDetailsCarerTel() {
        return preferences.getString(PERSONAL_DETAILS_CARER_TEL, "");
    }

    public String getStreet() {
        return preferences.getString(PERSONAL_DETAILS_STREET, "");
    }
    public String getTown() {
        return preferences.getString(PERSONAL_DETAILS_TOWN, "");
    }
    public String getCounty() {
        return preferences.getString(PERSONAL_DETAILS_COUNTY, "");
    }

    public void setStreet(String name) {
        editor.putString(PERSONAL_DETAILS_STREET, name).apply();
    }

    public void setTown(String name) {
        editor.putString(PERSONAL_DETAILS_TOWN, name).apply();
    }

    public void setCounty(String name) {
        editor.putString(PERSONAL_DETAILS_COUNTY, name).apply();
    }

    public void setPersonalDetailsCarerTel(String tel) {
        editor.putString(PERSONAL_DETAILS_CARER_TEL, tel).apply();
    }

    public boolean getPersonalDetailsNeedTranslator() {
        return preferences.getBoolean(PERSONAL_DETAILS_NEED_TRANSLATOR, false);
    }

    public void setPersonalDetailsNeedTranslator(boolean needTranslator) {
        editor.putBoolean(PERSONAL_DETAILS_NEED_TRANSLATOR, needTranslator).apply();
    }

    public String getMedicalAllergies() {
        return preferences.getString(MEDICAL_ALLERGIES, "");
    }

    public void setMedicalAllergies(String allergies) {
        editor.putString(MEDICAL_ALLERGIES, allergies).apply();
    }

    public String getFoodAllergies() {
        return preferences.getString(FOOD_ALLERGIES, "");
    }

    public void setFoodAllergies(String allergies) {
        editor.putString(FOOD_ALLERGIES, allergies).apply();
    }

    public String getLikesDislikesRoutine() {
        return preferences.getString(LIKES_DISLIKES_ROUTINE, "");
    }

    public void setLikesDislikesRoutine(String routine) {
        editor.putString(LIKES_DISLIKES_ROUTINE, routine).apply();
    }

    public String getLikesDislikesHobbies() {
        return preferences.getString(LIKES_DISLIKES_HOBBIES, "");
    }

    public void setLikesDislikesHobbies(String hobbies) {
        editor.putString(LIKES_DISLIKES_HOBBIES, hobbies).apply();
    }

    public String getLikesDislikesMusic() {
        return preferences.getString(LIKES_DISLIKES_MUSIC, "");
    }

    public void setLikesDislikesMusic(String music) {
        editor.putString(LIKES_DISLIKES_MUSIC, music).apply();
    }

    public String getLikesDislikesTelevision() {
        return preferences.getString(LIKES_DISLIKES_TELEVISION, "");
    }

    public void setLikesDislikesTelevision(String television) {
        editor.putString(LIKES_DISLIKES_TELEVISION, television).apply();
    }

    public String getLikesDislikesOther() {
        return preferences.getString(LIKES_DISLIKES_OTHER, "");
    }

    public void setLikesDislikesOther(String other) {
        editor.putString(LIKES_DISLIKES_OTHER, other).apply();
    }

    public String getDiagnosis() {
        return preferences.getString(DIAGNOSIS, "");
    }

    public void setDiagnosis(String diagnosis) {
        editor.putString(DIAGNOSIS, diagnosis).apply();
    }

    public String getPersonalDetailHomeTel() {
        return preferences.getString(HOME_TELEPHONE, "");
    }

    public void setHomeTelephone(String homeTelephone) {
        editor.putString(HOME_TELEPHONE, homeTelephone).apply();
    }

    public void setMobile(String mobile) {
        editor.putString(MOBILE, mobile).apply();
    }

    public void setPersonalDetailGender(String gender) {
        editor.putString(GENDER, gender).apply();
    }

    public void setCarerLanguage(String carerLanguage) {
        editor.putString(CARER_LANG, carerLanguage).apply();
    }

    public String getPersonalDetailMobile() {
        return preferences.getString(MOBILE, "");
    }

    public String getPersonalDetailGender() {
        return preferences.getString(GENDER, "");
    }

    public String getCarerLanguage() {
        return preferences.getString(CARER_LANG, "");
    }
}
