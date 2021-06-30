package com.example.licenta;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.licenta.helpers.DatabaseAccess;
import com.example.licenta.helpers.InputValidation;
import com.example.licenta.model.User;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class Register extends AppCompatActivity  {

    private final AppCompatActivity activity = Register.this;
    private TextView outNume, outPrenume, outUsername, outEmail, outParola, outConfirmaParola, outAge;
    private EditText txtNume, txtPrenume, txtUsername, txtEmail, txtParola, txtConfirmaParola, txtAge;
    private Button ButtonRegister;
    private TextView txtLogin;
    private InputValidation inputValidation;
    private DatabaseAccess db;
    private User user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        initViews();//initializari
        initListeners();//initializare click listeneri
        initObjects();//deschidere baza de date, input validation, respectiv creare de user
    }
    private void initViews() {
        outNume = (TextView) findViewById(R.id.outNume);
        outPrenume = (TextView) findViewById(R.id.outPrenume);
        outUsername = (TextView) findViewById(R.id.outUsername);
        outEmail = (TextView) findViewById(R.id.outEmail);
        outParola = (TextView) findViewById(R.id.outParola);
        outConfirmaParola = (TextView) findViewById(R.id.outConfirm);
        outAge=(TextView)findViewById(R.id.outAge);
        txtNume = (EditText) findViewById(R.id.etNume);
        txtPrenume = (EditText) findViewById(R.id.etPrenume);
        txtUsername = (EditText) findViewById(R.id.etUsername);
        txtEmail = (EditText) findViewById(R.id.etEmail);
        txtParola = (EditText) findViewById(R.id.etParola);
        txtConfirmaParola = (EditText) findViewById(R.id.etConfirm);
        ButtonRegister = (Button) findViewById(R.id.btnReg);
        txtLogin = (TextView) findViewById(R.id.txtLogin);
        txtAge=(EditText)findViewById(R.id.age);
    }

    private void initListeners() {//initializare listeneri
        ButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    outNume.setText("");
                    outPrenume.setText("");
                    outUsername.setText("");
                    outEmail.setText("");
                    outParola.setText("");
                    outConfirmaParola.setText("");
                    outAge.setText("");
                    postDataToSQLite();
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        });
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initObjects() {//initializare obiecte
        inputValidation = new InputValidation(activity);
        db = DatabaseAccess.getInstance(this);
        db.open();
        user = new User();
    }

    private void postDataToSQLite() throws InvalidKeySpecException, NoSuchAlgorithmException {//validare inputuri si trimitere date catre SQLite
        if (!inputValidation.isInputEditTextFilled(txtNume, outNume, getString(R.string.error_message_name))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(txtPrenume, outPrenume, getString(R.string.error_message_name))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(txtUsername, outUsername, getString(R.string.error_message_username))) {
            return;
        }
        if(!inputValidation.isInputEditTextFilled(txtAge, outAge, getString(R.string.error_message_age))){
            return;
        }
        if (!inputValidation.isInputEditTextFilled(txtEmail, outEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(txtEmail, outEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(txtParola, outParola, getString(R.string.error_message_password))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(txtConfirmaParola, outConfirmaParola, getString(R.string.error_message_password))) {
            return;
        }
        if (!inputValidation.isInputEditTextMatches(txtParola, txtConfirmaParola,
                outConfirmaParola, getString(R.string.error_password_match))) {
            return;
        }
        if (!db.checkUser(txtEmail.getText().toString())) {
            user.setNume(txtNume.getText().toString());
            user.setPrenume(txtPrenume.getText().toString());
            user.setUsername(txtUsername.getText().toString());
            user.setAge(Integer.parseInt(txtAge.getText().toString()));
            user.setEmail(txtEmail.getText().toString());
            user.setPassword(txtParola.getText().toString());
            db.addUser(user);
            emptyInputEditText();
            finish();
        } else {
            outEmail.setText(getResources().getString(R.string.error_email_exists));
        }
    }

    private void emptyInputEditText() {//pentru stergerea inputurilor din EditText-uri
        txtNume.setText(null);
        txtPrenume.setText(null);
        txtUsername.setText(null);
        txtAge.setText(null);
        txtEmail.setText(null);
        txtParola.setText(null);
        txtConfirmaParola.setText(null);
    }
}
