package com.example.licenta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.Random;

import pl.droidsonroids.gif.GifImageView;

public class Joke extends AppCompatActivity implements interfata {
    private String email;
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private BottomNavigationView bottomNav;
    private NavigationView nav_view;
    private TextView descriereTxt;
    private String[] banc;
    private Button awesome, nice, horrible;
    private LinearLayout linButton;
    private GifImageView gif;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joke);
    initViews();//initilizare View-uri
    preluareDate();//preluare email si limba selectata
        if (email.equals(getResources().getString(R.string.standardEmail))){//daca se intra fara cont se va modifica meniul din bara de start
        barWithoutAccount();
    }
        else{
        barNavStart();
    }
    baraNavBottom();//bara bottom
        generareGlumaRandom();
        listeneri();
}

    @Override
    public void initViews(){
        dl=(DrawerLayout)findViewById(R.id.dl);
        abdt= new ActionBarDrawerToggle(this,dl,R.string.Open,R.string.Close);
        bottomNav=findViewById(R.id.bottomNav);
        nav_view=(NavigationView)findViewById(R.id.nav_view);
        descriereTxt=(TextView)findViewById(R.id.descriereTxt);
        banc=getResources().getStringArray(R.array.glume);
        awesome=(Button)findViewById(R.id.awesome);
        nice=(Button)findViewById(R.id.nice);
        horrible=(Button)findViewById(R.id.horrible);
        linButton=(LinearLayout)findViewById(R.id.linButton);
        gif=(GifImageView)findViewById(R.id.gif);
    }
    private void generareGlumaRandom(){
        Random r = new Random();
        int size=banc.length-1;
        int i = r.nextInt((size - 0) +1) + 0;
        descriereTxt.setText(banc[i]);
    }
    public void listeneri(){
        awesome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linButton.removeView(awesome);
                linButton.removeView(nice);
                linButton.removeView(horrible);
                gif.setImageResource(R.drawable.veryhappy);
            }
        });
        nice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linButton.removeView(awesome);
                linButton.removeView(nice);
                linButton.removeView(horrible);
                gif.setImageResource(R.drawable.happy);
            }
        });
        horrible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linButton.removeView(awesome);
                linButton.removeView(nice);
                linButton.removeView(horrible);
                gif.setImageResource(R.drawable.cry);
            }
        });
    }

    @Override
    public void preluareDate(){
        Intent intent=getIntent();
        email=intent.getStringExtra(intent.EXTRA_EMAIL);
    }

    public void barWithoutAccount(){
        nav_view.inflateMenu(R.menu.without_account);
        abdt.setDrawerIndicatorEnabled(true);
        dl.addDrawerListener(abdt);
        abdt.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final NavigationView navView= (NavigationView)findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.login) {
                    Intent intent = new Intent(Joke.this, Login.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }
    @Override
    public void barNavStart(){
        nav_view.inflateMenu(R.menu.navigation_menu);
        abdt.setDrawerIndicatorEnabled(true);
        dl.addDrawerListener(abdt);
        abdt.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final NavigationView navView= (NavigationView)findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.cont) {
                    Intent intent = new Intent(Joke.this, Cont.class);
                    intent.putExtra(intent.EXTRA_EMAIL, email);
                    startActivity(intent);
                } else if (id == R.id.statistici) {
                    Intent intent = new Intent(Joke.this, Statistici.class);
                    intent.putExtra(intent.EXTRA_EMAIL, email);
                    startActivity(intent);
                } else if (id == R.id.myCars) {
                    Intent intent = new Intent(Joke.this, MyCars.class);
                    intent.putExtra(intent.EXTRA_EMAIL, email);
                    startActivity(intent);
                } else if (id == R.id.logout) {
                    SharedPreferences preferences= getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor= preferences.edit();
                    editor.putString("remember", "false");
                    editor.apply();
                    Intent intent= new Intent(Joke.this, Login.class);
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
                        Intent intent= new Intent(Joke.this, Home.class);
                        intent.putExtra(intent.EXTRA_EMAIL, email);
                        startActivity(intent);
                    }
                    else if(id==R.id.statistici){
                        Intent intent= new Intent(Joke.this, GeneralStatistics.class);
                        intent.putExtra(intent.EXTRA_EMAIL, email);
                        startActivity(new Intent(intent));
                    }
                    else if(id==R.id.more){
                        Intent intent= new Intent(Joke.this, Help.class);
                        intent.putExtra(intent.EXTRA_EMAIL, email);
                        startActivity(new Intent(intent));
                    }
                    return true;

                }
            };
    @Override
    public void baraNavBottom() {
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }
    public boolean onOptionsItemSelected(MenuItem item){
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
}
