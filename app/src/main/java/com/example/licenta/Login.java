package com.example.licenta;

import android.animation.Animator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.licenta.helpers.DatabaseAccess;
import com.example.licenta.helpers.InputValidation;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Locale;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Login extends AppCompatActivity {
    private final AppCompatActivity activity =Login.this;
    private TextView out1, out2, txt_info, txt_without, forgot;
    private EditText txtEmail;
    private EditText txtParola;
    private Button ButtonLogin;
    private InputValidation inputValidation;
    private DatabaseAccess db;
    private CheckBox remember;
    private ScrollView background;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.do_not_move, R.anim.do_not_move);
        setContentView(R.layout.login);

        background = findViewById(R.id.scrollView);

        if (savedInstanceState == null) {
            background.setVisibility(View.INVISIBLE);

            final ViewTreeObserver viewTreeObserver = background.getViewTreeObserver();

            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onGlobalLayout() {
                        circularRevealActivity();
                        background.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }

                });
            }

        }
        initViews();//initializari
        getSupportActionBar().hide();
        SharedPreferences preferences= getSharedPreferences("checkbox", MODE_PRIVATE);
        String checkbox= preferences.getString("remember", "");
        String email= preferences.getString("email", "");

        if(checkbox.equals("true")){
            Intent intent= new Intent(Login.this, Home.class);
            intent.putExtra(Intent.EXTRA_EMAIL, email);
            startActivity(intent);
        }else if(checkbox.equals("false")){
            Toast.makeText(this, "Please sign in", Toast.LENGTH_SHORT).show();
        }
        initListeners();//setare click listeneri
        initObjects();//deschidere BD si InputValidation
    }


    private void initViews() {//initializare view-uri
        out1=(TextView)findViewById(R.id.output);
        out2=(TextView)findViewById(R.id.output1);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtParola = (EditText) findViewById(R.id.txtParola);
        ButtonLogin = (Button) findViewById(R.id.btn_login);
        txt_info=(TextView)findViewById(R.id.txt_info);
        txt_without=findViewById(R.id.txt_without);
        forgot=(TextView)findViewById(R.id.schimbaParola);
        remember=(CheckBox)findViewById(R.id.remember);
    }

    private void initListeners() {//initializare listeneri
        ButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    verifyFromSQLite();
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        });
        txt_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to RegisterActivity
                Intent intentRegister = new Intent(getApplicationContext(), Register.class);
                startActivity(intentRegister);
            }
         });
        txt_without.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent accountsIntent = new Intent(activity, Home.class);
                accountsIntent.putExtra(accountsIntent.EXTRA_EMAIL, getResources().getString(R.string.standardEmail));
                startActivity(accountsIntent);
            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Login.this,ForgotPassword.class);
                startActivity(intent);
            }
        });
        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    SharedPreferences preferences= getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor= preferences.edit();
                    editor.putString("remember", "true");
                    editor.putString("email", txtEmail.getText().toString());
                    editor.apply();
                    Toast.makeText(Login.this, "Checked", Toast.LENGTH_SHORT).show();
                }else if(!compoundButton.isChecked()){
                    SharedPreferences preferences= getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor= preferences.edit();
                    editor.putString("remember", "false");
                    editor.apply();
                    Toast.makeText(Login.this, "Unchecked", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initObjects() {//initializare obiecte ce urmeaza a fi folosite
        db = DatabaseAccess.getInstance(this);
        db.open();
        inputValidation = new InputValidation(activity);
    }

    private void verifyFromSQLite() throws InvalidKeySpecException, NoSuchAlgorithmException {//pentru validarea inputurilor si verificarea credentialelor pentru login din SQLite
        if (!inputValidation.isInputEditTextFilled(txtEmail, out1, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(txtEmail, out1, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(txtParola, out2, getString(R.string.error_message_password))) {
            return;
        }
        if (db.checkUser(txtEmail.getText().toString().trim(), txtParola.getText().toString().trim())) {
            Intent accountsIntent = new Intent(activity, Home.class);
            accountsIntent.putExtra(accountsIntent.EXTRA_EMAIL, txtEmail.getText().toString().trim());
            emptyInputEditText();
            startActivity(accountsIntent);
        } else {
           out1.setText(getString(R.string.error_valid_email_password));
        }
    }

    private void emptyInputEditText() {//pentru a goli toate EditText-urile
        txtEmail.setText(null);
        txtParola.setText(null);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void circularRevealActivity() {
        int cx = background.getRight() - getDips(44);
        int cy = background.getBottom() - getDips(44);

        float finalRadius = Math.max(background.getWidth(), background.getHeight());

        Animator circularReveal = ViewAnimationUtils.createCircularReveal(
                background,
                cx,
                cy,
                0,
                finalRadius);

        circularReveal.setDuration(3000);
        background.setVisibility(View.VISIBLE);
        circularReveal.start();

    }

    private int getDips(int dps) {
        Resources resources = getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dps,
                resources.getDisplayMetrics());
    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int cx = background.getWidth() - getDips(44);
            int cy = background.getBottom() - getDips(44);

            float finalRadius = Math.max(background.getWidth(), background.getHeight());
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(background, cx, cy, finalRadius, 0);

            circularReveal.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    background.setVisibility(View.INVISIBLE);
                    finish();
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            circularReveal.setDuration(3000);
            circularReveal.start();
        }
        else {
            super.onBackPressed();
        }
    }
}
