package uk.co.jaspalsvoice.jv.activities;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import uk.co.jaspalsvoice.jv.JvApplication;
import uk.co.jaspalsvoice.jv.JvPreferences;
import uk.co.jaspalsvoice.jv.MainActivity;
import uk.co.jaspalsvoice.jv.R;

public class FontSizeActivity extends AppCompatActivity {

    RadioGroup fontSizeRadioGroup;
    RadioButton smallRadioButton;
    RadioButton normalRadioButton;
    RadioButton largeRadioButton;
    JvPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_font_size);
        initViews();
        initValues();
        attachListeners();
    }

    private void initValues() {
        preferences = ((JvApplication)getApplicationContext()).getPreferences();
    }

    private void attachListeners() {
        fontSizeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.smallRadioButton) {
                    preferences.setFontSize(0.75f);
                } else if (checkedId == R.id.normalRadioButton) {
                    preferences.setFontSize(1f);
                } else if (checkedId == R.id.largeRadioButton) {
                    preferences.setFontSize(1.15f);
                }
                Intent intent = new Intent(FontSizeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initViews() {
        fontSizeRadioGroup = (RadioGroup) findViewById(R.id.fontSizeRadioGroup);
        smallRadioButton = (RadioButton) findViewById(R.id.smallRadioButton);
        normalRadioButton = (RadioButton) findViewById(R.id.normalRadioButton);
        largeRadioButton = (RadioButton) findViewById(R.id.largeRadioButton);

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
