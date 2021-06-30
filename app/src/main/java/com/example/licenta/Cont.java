package com.example.licenta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.licenta.helpers.DatabaseAccess;
import com.example.licenta.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Text;

import java.util.List;

public class Cont extends AppCompatActivity implements interfata{
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private TextView numeV, prenumeV, usernameV, emailV, parola, ageV;
    private String email;
    private ImageView imgNume, imgPrenume, imgUsername, imgAge;
    private LinearLayout lNume, lPrenume, lUsername, lAge;
    private EditText eNume, ePrenume, eUsername, eAge;
    private int marginTop, width;
    private User user;
    private BottomNavigationView bottomNav;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cont);
        initViews();//initializari
        preluareDate();//preluare date din pagina anterioara
        barNavStart();//bara de start
        baraNavBottom();//bara de jos
        user();//completeaza datele utilizatorului pe baza emailului
        clickListeneri();//realizeaza click listenerii pentru imaginile de tip pencil
    }

    @Override
    public void initViews(){
        lUsername=findViewById(R.id.lUsername);
        lNume=findViewById(R.id.lNume);
        lPrenume=findViewById(R.id.lPrenume);
        lAge=findViewById(R.id.lAge);
        numeV=findViewById(R.id.numeV);
        prenumeV=findViewById(R.id.prenumeV);
        usernameV=findViewById(R.id.usernameV);
        emailV=findViewById(R.id.emailV);
        ageV=(TextView)findViewById(R.id.ageV);
        imgNume=findViewById(R.id.imgNume);
        imgPrenume=findViewById(R.id.imgPrenume);
        imgUsername=findViewById(R.id.imgUsername);
        imgAge=findViewById(R.id.imgAge);
        parola=findViewById(R.id.parola);
        bottomNav=findViewById(R.id.bottomNav);
        marginTop=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, this.getResources().getDisplayMetrics());
        width=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 170, this.getResources().getDisplayMetrics());
        dl=(DrawerLayout)findViewById(R.id.dl);
        abdt= new ActionBarDrawerToggle(this,dl,R.string.Open,R.string.Close);
    }

    @Override
    public void preluareDate() {
        Intent intent=getIntent();
        email= intent.getStringExtra(intent.EXTRA_EMAIL);
    }

    @Override
    public void barNavStart(){
        abdt.setDrawerIndicatorEnabled(true);
        dl.addDrawerListener(abdt);
        abdt.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final NavigationView navView= (NavigationView)findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id= item.getItemId();
                if(id==R.id.cont){
                    Intent intent= new Intent(Cont.this, Cont.class);
                    intent.putExtra(intent.EXTRA_EMAIL, email);
                    startActivity(intent);
                }
                else if(id==R.id.statistici){
                    Intent intent= new Intent(Cont.this, Statistici.class);
                    intent.putExtra(intent.EXTRA_EMAIL, email);
                    startActivity(intent);
                }
                else if(id==R.id.myCars){
                    Intent intent= new Intent(Cont.this, MyCars.class);
                    intent.putExtra(intent.EXTRA_EMAIL, email);
                    startActivity(intent);
                }
                else if(id==R.id.logout){
                    SharedPreferences preferences= getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor= preferences.edit();
                    editor.putString("remember", "false");
                    editor.apply();
                    Intent intent= new Intent(Cont.this, Login.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }//bara navigare start

    private BottomNavigationView.OnNavigationItemSelectedListener navListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int id= item.getItemId();
                    if(id==R.id.home){
                        Intent intent= new Intent(Cont.this, Home.class);
                        intent.putExtra(intent.EXTRA_EMAIL, email);
                        startActivity(intent);
                    }
                    else if(id==R.id.statistici){
                        Intent intent=new Intent(Cont.this, GeneralStatistics.class);
                        intent.putExtra(intent.EXTRA_EMAIL, email);
                        startActivity(intent);
                    }
                    else if(id==R.id.more){
                        Intent intent= new Intent(Cont.this, Help.class);
                        intent.putExtra(intent.EXTRA_EMAIL, email);
                        startActivity(new Intent(intent));                    }
                    return true;

                }
            };
    public boolean onOptionsItemSelected(MenuItem item){
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    public void baraNavBottom(){
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }//bara navigare bottom

    public void clickListeneri(){
        imgNume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eNume=new EditText(Cont.this);
                eNume.setTextColor(getResources().getColor(R.color.black));
                eNume.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                eNume.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                eNume.setBackground(getResources().getDrawable(R.drawable.border_name));
                LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.topMargin=marginTop;
                eNume.setLayoutParams(layoutParams);
                eNume.setText(numeV.getText());
                lNume.removeView(numeV);
                lNume.removeView(imgNume);
                eNume.setOnKeyListener(new View.OnKeyListener() {
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        // If the event is a key-down event on the "enter" button
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                                (keyCode == KeyEvent.KEYCODE_ENTER)) {
                            // Perform action on key press
                            numeV.setText(eNume.getText().toString());
                            user.setNume(numeV.getText().toString());
                            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
                            databaseAccess.open();
                            databaseAccess.updateUserNume(user);
                            databaseAccess.close();
                            lNume.removeView(eNume);
                            lNume.addView(numeV);
                            lNume.addView(imgNume);
                            return true;
                        }
                        return false;
                    }
                });
                lNume.addView(eNume);
            }
        });
        imgPrenume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ePrenume= new EditText(Cont.this);
                ePrenume.setTextColor(getResources().getColor(R.color.black));
                ePrenume.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                ePrenume.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                ePrenume.setBackground(getResources().getDrawable(R.drawable.border_name));
                LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.topMargin=marginTop;
                ePrenume.setLayoutParams(layoutParams);
                ePrenume.setText(prenumeV.getText());
                lPrenume.removeView(prenumeV);
                lPrenume.removeView(imgPrenume);
                ePrenume.setOnKeyListener(new View.OnKeyListener() {
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        // If the event is a key-down event on the "enter" button
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                                (keyCode == KeyEvent.KEYCODE_ENTER)) {
                            // Perform action on key press
                            prenumeV.setText(ePrenume.getText().toString());
                            user.setPrenume(prenumeV.getText().toString());
                            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
                            databaseAccess.open();
                            databaseAccess.updateUserPrenume(user);
                            databaseAccess.close();
                            lPrenume.removeView(ePrenume);
                            lPrenume.addView(prenumeV);
                            lPrenume.addView(imgPrenume);
                            return true;
                        }
                        return false;
                    }
                });
                lPrenume.addView(ePrenume);
            }
        });
        imgUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eUsername= new EditText(Cont.this);
                eUsername.setTextColor(getResources().getColor(R.color.black));
                eUsername.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                eUsername.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                eUsername.setBackground(getResources().getDrawable(R.drawable.border_name));
                LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.topMargin=marginTop;
                eUsername.setLayoutParams(layoutParams);
                eUsername.setText(usernameV.getText());
                lUsername.removeView(usernameV);
                lUsername.removeView(imgUsername);
                eUsername.setOnKeyListener(new View.OnKeyListener() {
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        // If the event is a key-down event on the "enter" button
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                                (keyCode == KeyEvent.KEYCODE_ENTER)) {
                            // Perform action on key press
                            usernameV.setText(eUsername.getText().toString());
                            user.setUsername(usernameV.getText().toString());
                            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
                            databaseAccess.open();
                            databaseAccess.updateUserUsername(user);
                            databaseAccess.close();
                            lUsername.removeView(eUsername);
                            lUsername.addView(usernameV);
                            lUsername.addView(imgUsername);
                            return true;
                        }
                        return false;
                    }
                });
                lUsername.addView(eUsername);
            }
        });
        imgAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eAge= new EditText(Cont.this);
                eAge.setTextColor(getResources().getColor(R.color.black));
                eAge.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                eAge.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                eAge.setBackground(getResources().getDrawable(R.drawable.border_name));
                LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.topMargin=marginTop;
                eAge.setLayoutParams(layoutParams);
                eAge.setText(ageV.getText());
                lAge.removeView(ageV);
                lAge.removeView(imgAge);
                eAge.setOnKeyListener(new View.OnKeyListener() {
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        // If the event is a key-down event on the "enter" button
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                                (keyCode == KeyEvent.KEYCODE_ENTER)) {
                            // Perform action on key press
                            ageV.setText(eAge.getText().toString());
                            user.setAge(Integer.parseInt(ageV.getText().toString()));
                            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
                            databaseAccess.open();
                            databaseAccess.updateUserAge(user);
                            databaseAccess.close();
                            lAge.removeView(eAge);
                            lAge.addView(ageV);
                            lAge.addView(imgAge);
                            return true;
                        }
                        return false;
                    }
                });
                lUsername.addView(eUsername);
            }
        });

        parola.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent accountsIntent= new Intent(Cont.this, SchimbaParola.class);
                accountsIntent.putExtra(accountsIntent.EXTRA_EMAIL, email);
                startActivity(accountsIntent);
            }
        });
    }
    public void user(){
        DatabaseAccess databaseAccess=DatabaseAccess.getInstance(getApplicationContext());
        databaseAccess.open();
        user= databaseAccess.getAllUser(email);
        databaseAccess.close();
        numeV.setText(user.getNume());
        prenumeV.setText(user.getPrenume());
        usernameV.setText(user.getUsername());
        ageV.setText(Integer.toString(user.getAge()));
        emailV.setText(user.getEmail());
    }
}
