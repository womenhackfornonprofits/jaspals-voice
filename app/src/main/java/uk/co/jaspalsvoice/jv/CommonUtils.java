package uk.co.jaspalsvoice.jv;

import android.content.Context;
import android.renderscript.Long2;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import uk.co.jaspalsvoice.jv.activities.BaseActivity;

/**
 * Created by Srinivas Kalyani on 20/7/16.
 */
public class CommonUtils {

    public static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static void showEmailToast(Context context){
        Toast.makeText(context, context.getResources().
                getString(R.string.email_validation), Toast.LENGTH_LONG).show();
    }

    public static void hideKeyboard(BaseActivity activity){
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
