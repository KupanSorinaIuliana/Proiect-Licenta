package com.example.licenta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class Help extends AppCompatActivity implements interfata{
    private String email;
    private BottomNavigationView bottomNav;
    private NavigationView nav_view;
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private CardView page1, page2, page3, page4, page5, page6, page7;
    private TextView titlu1, titlu2, titlu3, titlu4, titlu5, titlu6, titlu7;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);

        initViews();//initilizare View-uri
        preluareDate();//preluare email si limba selectata
        if (email.equals(getResources().getString(R.string.standardEmail))){//daca se intra fara cont se va modifica meniul din bara de start
            barWithoutAccount();
        }
        else{
            barNavStart();
        }
        baraNavBottom();//bara bottom
        clickListeneri();//click listener pentru fiecare subpunct din help
    }

    @Override
    public void initViews() {
        dl=(DrawerLayout)findViewById(R.id.dl);
        abdt= new ActionBarDrawerToggle(this,dl,R.string.Open,R.string.Close);
        bottomNav=findViewById(R.id.bottomNav);
        nav_view=(NavigationView)findViewById(R.id.nav_view);
        page1=findViewById(R.id.page1);
        page2=findViewById(R.id.page2);
        page3=findViewById(R.id.page3);
        page4=findViewById(R.id.page4);
        page5=findViewById(R.id.page5);
        page6=findViewById(R.id.page6);
        page7=findViewById(R.id.page7);
        titlu1=findViewById(R.id.titlu1);
        titlu2=findViewById(R.id.titlu2);
        titlu3=findViewById(R.id.titlu3);
        titlu4=findViewById(R.id.titlu4);
        titlu5=findViewById(R.id.titlu5);
        titlu6=findViewById(R.id.titlu6);
        titlu7=findViewById(R.id.titlu7);
    }

    @Override
    public void preluareDate() {
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
                    Intent intent = new Intent(Help.this, Login.class);
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
                    Intent intent = new Intent(Help.this, Cont.class);
                    intent.putExtra(intent.EXTRA_EMAIL, email);
                    startActivity(intent);
                } else if (id == R.id.statistici) {
                    Intent intent = new Intent(Help.this, Statistici.class);
                    intent.putExtra(intent.EXTRA_EMAIL, email);
                    startActivity(intent);
                } else if (id == R.id.myCars) {
                    Intent intent = new Intent(Help.this, MyCars.class);
                    intent.putExtra(intent.EXTRA_EMAIL, email);
                    startActivity(intent);
                } else if (id == R.id.logout) {
                    SharedPreferences preferences= getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor= preferences.edit();
                    editor.putString("remember", "false");
                    editor.apply();
                    Intent intent= new Intent(Help.this, Login.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int id= item.getItemId();
                    if(id==R.id.home){
                        Intent intent= new Intent(Help.this, Home.class);
                        intent.putExtra(intent.EXTRA_EMAIL, email);
                        startActivity(intent);
                    }
                    else if(id==R.id.statistici){
                        Intent intent= new Intent(Help.this, GeneralStatistics.class);
                        intent.putExtra(intent.EXTRA_EMAIL, email);
                        startActivity(new Intent(intent));
                    }
                    else if(id==R.id.more){
                        Intent intent= new Intent(Help.this, Help.class);
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
    public void clickListeneri(){
        page1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Help.this, HelpPages.class);
                intent.putExtra(intent.EXTRA_EMAIL, email);
                intent.putExtra(intent.EXTRA_TITLE, titlu1.getText());
                intent.putExtra(intent.EXTRA_TEXT, getResources().getStringArray(R.array.accountHelpDescription));
                int[] images={R.drawable.p1, R.drawable.ic_baseline_person_24, R.drawable.ic_baseline_edit_24};
                intent.putExtra(intent.EXTRA_CONTENT_QUERY, images);
                startActivity(new Intent(intent));
            }
        });
        page2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Help.this, HelpPages.class);
                intent.putExtra(intent.EXTRA_EMAIL, email);
                intent.putExtra(intent.EXTRA_TITLE, titlu2.getText());
                intent.putExtra(intent.EXTRA_TEXT, getResources().getStringArray(R.array.passwordHelpDescription));
                int[] images={R.drawable.p1, R.drawable.ic_baseline_person_24};
                intent.putExtra(intent.EXTRA_CONTENT_QUERY, images);
                startActivity(new Intent(intent));
            }
        });
        page3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Help.this, HelpPages.class);
                intent.putExtra(intent.EXTRA_EMAIL, email);
                intent.putExtra(intent.EXTRA_TITLE, titlu3.getText());
                intent.putExtra(intent.EXTRA_TEXT, getResources().getStringArray(R.array.createAccountHelp));
                int[] images={};
                intent.putExtra(intent.EXTRA_CONTENT_QUERY, images);
                startActivity(new Intent(intent));
            }
        });
        page4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Help.this, HelpPages.class);
                intent.putExtra(intent.EXTRA_EMAIL, email);
                intent.putExtra(intent.EXTRA_TITLE, titlu4.getText());
                intent.putExtra(intent.EXTRA_TEXT, getResources().getStringArray(R.array.fpassHelp));
                int[] images={R.drawable.ic_register_hero};
                intent.putExtra(intent.EXTRA_CONTENT_QUERY, images);
                startActivity(new Intent(intent));
            }
        });
        page5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Help.this, HelpPages.class);
                intent.putExtra(intent.EXTRA_EMAIL, email);
                intent.putExtra(intent.EXTRA_TITLE, titlu5.getText());
                intent.putExtra(intent.EXTRA_TEXT, getResources().getStringArray(R.array.descHelp));
                int[] images={R.drawable.ic_baseline_description_24};
                intent.putExtra(intent.EXTRA_CONTENT_QUERY, images);
                startActivity(new Intent(intent));
            }
        });
        page6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Help.this, HelpPages.class);
                intent.putExtra(intent.EXTRA_EMAIL, email);
                intent.putExtra(intent.EXTRA_TITLE, titlu6.getText());
                intent.putExtra(intent.EXTRA_TEXT, getResources().getStringArray(R.array.filterHelp));
                int[] images={R.drawable.ic_baseline_equalizer_24};
                intent.putExtra(intent.EXTRA_CONTENT_QUERY, images);
                startActivity(new Intent(intent));
            }
        });
        page7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Help.this, HelpPages.class);
                intent.putExtra(intent.EXTRA_EMAIL, email);
                intent.putExtra(intent.EXTRA_TITLE, titlu7.getText());
                intent.putExtra(intent.EXTRA_TEXT, getResources().getStringArray(R.array.compareHelp));
                int[] images={R.drawable.ic_baseline_compare_arrows_24};
                intent.putExtra(intent.EXTRA_CONTENT_QUERY, images);
                startActivity(new Intent(intent));
            }
        });
    }
}
