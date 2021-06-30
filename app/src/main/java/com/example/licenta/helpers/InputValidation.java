package com.example.licenta.helpers;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;


public class InputValidation {
    private Context context;

    public InputValidation(Context context) {
        this.context = context;
    }

    public boolean isInputEditTextFilled(EditText txt, TextView out, String message) {
        String value = txt.getText().toString().trim();
        if (value.isEmpty()) {
            out.setText(message);
            hideKeyboardFrom(txt);
            return false;
        }
        return true;
    }

    public boolean isInputEditTextEmail(EditText txt, TextView out, String message) {
        String value = txt.getText().toString().trim();
        if (value.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            out.setText(message);
            hideKeyboardFrom(txt);
            return false;
        }
        return true;
    }
    public boolean isInputEditTextMatches(EditText txt1, EditText txt2, TextView out, String message) {
        String value1 = txt1.getText().toString().trim();
        String value2 = txt2.getText().toString().trim();
        if (!value1.contentEquals(value2)) {
            out.setText(message);
            hideKeyboardFrom(txt2);
            return false;
        }
        return true;
    }
    /**
     * method to Hide keyboard
     *
     * @param view
     */
    private void hideKeyboardFrom(View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
