package uk.co.jaspalsvoice.jv.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import uk.co.jaspalsvoice.jv.R;

/**
 * Created by claudia on 22/04/2017.
 */
public class TermsAndConditionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
