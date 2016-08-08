package uk.co.jaspalsvoice.jv.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import uk.co.jaspalsvoice.jv.CommonUtils;
import uk.co.jaspalsvoice.jv.R;

/**
 * Created by Ana on 2/8/2016.
 */
public class LikesDislikesActivity extends BaseActivity {

    private LinearLayout mainLayoutAboutMe;
    private LinearLayout buttonLayout;
    private EditText dailyRoutine;
    private EditText interestHobbies;
    private EditText music;
    private EditText television;
    private EditText other;
    private Button save;
    private Button cancel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_likes_dislikes);
        initViews();
        attachListeners();
        loadData();
    }

    private void initViews() {
        dailyRoutine = (EditText) findViewById(R.id.editDailyRoutine);
        interestHobbies = (EditText) findViewById(R.id.editInterstHobbies);
        music = (EditText) findViewById(R.id.editMusic);
        television = (EditText) findViewById(R.id.editTelevision);
        other = (EditText) findViewById(R.id.editOther);
        save = (Button) findViewById(R.id.save);
        cancel = (Button) findViewById(R.id.cancel);
        mainLayoutAboutMe = (LinearLayout) findViewById(R.id.mainLayoutAboutMe);
        buttonLayout = (LinearLayout) findViewById(R.id.buttons);
    }

    private void attachListeners() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.hideKeyboard(LikesDislikesActivity.this);
                preferences.setLikesDislikesRoutine(dailyRoutine.getText().toString().trim());
                preferences.setLikesDislikesHobbies(interestHobbies.getText().toString().trim());
                preferences.setLikesDislikesMusic(music.getText().toString().trim());
                preferences.setLikesDislikesTelevision(television.getText().toString().trim());
                preferences.setLikesDislikesOther(other.getText().toString().trim());
                mainLayoutAboutMe.requestFocus();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
                mainLayoutAboutMe.requestFocus();
                CommonUtils.hideKeyboard(LikesDislikesActivity.this);
            }
        });
        mainLayoutAboutMe.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    buttonLayout.setVisibility(View.GONE);
                } else {
                    buttonLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void loadData() {
        dailyRoutine.setText(preferences.getLikesDislikesRoutine());
        interestHobbies.setText(preferences.getLikesDislikesHobbies());
        music.setText(preferences.getLikesDislikesMusic());
        television.setText(preferences.getLikesDislikesTelevision());
        other.setText(preferences.getLikesDislikesOther());
    }
}