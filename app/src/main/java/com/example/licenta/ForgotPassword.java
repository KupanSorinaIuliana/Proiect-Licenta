package com.example.licenta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.licenta.helpers.DatabaseAccess;
import com.example.licenta.helpers.InputValidation;
import com.example.licenta.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Formatter;
import java.util.Random;

public class ForgotPassword extends AppCompatActivity {
    private EditText username, email, parola, confirma;
    private DatabaseAccess db;
    private InputValidation inputValidation;
    private CardView cv1, cv2, cv3, cv4, schimba;
    private TextView output1, output2, output3, output4, txtSchimba;
    private User user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        initViews();
        verificare();
    }

    public void initViews() {
        username=(EditText)findViewById(R.id.username);
        parola=(EditText)findViewById(R.id.parola);
        email=(EditText)findViewById(R.id.email);
        confirma=(EditText)findViewById(R.id.confirma);
        cv1=(CardView)findViewById(R.id.cv1);
        cv2=(CardView)findViewById(R.id.cv2);
        cv3=(CardView)findViewById(R.id.cv3);
        cv4=(CardView)findViewById(R.id.cv4);
        output1=(TextView)findViewById(R.id.output1);
        output2=(TextView)findViewById(R.id.output2);
        output3=(TextView)findViewById(R.id.output3);
        output4=(TextView)findViewById(R.id.output4);
        txtSchimba=(TextView)findViewById(R.id.txtSchimba);
        schimba=(CardView)findViewById(R.id.schimba);
    }

    public void verificare(){
        db = DatabaseAccess.getInstance(this);
        db.open();
        inputValidation = new InputValidation(ForgotPassword.this);
        email.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (db.checkUser(email.getText().toString())) {
                        cv2.setVisibility(View.VISIBLE);
                        output1.setText(getResources().getString(R.string.existEmail));
                    } else {
                        output1.setTextColor(getResources().getColor(R.color.red));
                        output1.setText(getResources().getString(R.string.existNotEmail));
                    }
                    return true;
                }
                return false;
            }
        });
        username.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (username.getText().toString().equals(db.getUsername(email.getText().toString()))) {
                        cv3.setVisibility(View.VISIBLE);
                        cv4.setVisibility(View.VISIBLE);
                        output2.setText(getResources().getString(R.string.corectUsername));
                    } else {
                        output2.setTextColor(getResources().getColor(R.color.red));
                        output2.setText(getResources().getString(R.string.notCorectUsername));
                    }
                    return true;
                }
                return false;
            }
        });
        parola.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (!inputValidation.isInputEditTextFilled(parola,output3, getResources().getString(R.string.fillParolaNoua))) {
                        output3.setTextColor(getResources().getColor(R.color.red));
                        return false;
                    }
                    else{
                        output3.setText(getResources().getString(R.string.txtParolaNoua));
                    }
                    return true;
                }
                return false;
            }
        });
        confirma.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (!inputValidation.isInputEditTextFilled(confirma, output4, getResources().getString(R.string.fillConfirmaParolaNoua))) {
                        output4.setTextColor(getResources().getColor(R.color.red));
                        return false;
                    } else {
                        output4.setText(getResources().getString(R.string.txtConfirma));
                    }
                    if (!inputValidation.isInputEditTextMatches(parola, confirma,
                            output4, getString(R.string.error_password_match))) {
                        return false;
                    } else {
                        output4.setText(getResources().getString(R.string.txtConfirma));
                        schimba.setVisibility(View.VISIBLE);
                    }
                    return true;
                }
                return false;
            }
        });
        schimba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
                databaseAccess.open();
                user= new User();
                user.setEmail(email.getText().toString());
                user.setPassword(parola.getText().toString());
                try {
                    databaseAccess.updateUserParola(user);
                    Intent intent= new Intent(ForgotPassword.this, Login.class);
                    startActivity(intent);
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
