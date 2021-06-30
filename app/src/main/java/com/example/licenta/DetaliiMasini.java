package com.example.licenta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import java.util.ArrayList;

public class DetaliiMasini extends AppCompatActivity implements interfata {
    private String email;
    private int id;
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private BottomNavigationView bottomNav;
    private NavigationView nav_view;
    private TextView titlu, caroserie, marca, anul, caiPutere, capCilindrica, alimentare, buget;
    private ImageView pozaMasina;
    private Button save;
    private User user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalii_masini);
        initViews();//initializari
        preluareDate();//preluare date din pagina anterioara
        if (email.equals(getResources().getString(R.string.standardEmail))){//daca se intra fara cont se va modifica meniul din bara de start
            barWithoutAccount();
            save.setVisibility(View.INVISIBLE);
        }
        else{
            barNavStart();
        }
        baraNavBottom();//bara de jos
        completareDate(id);//completarea datelor despre modelul respectiv
    }

    @Override
    public void initViews() {
        nav_view=(NavigationView)findViewById(R.id.nav_view);
        dl=(DrawerLayout)findViewById(R.id.dl);
        abdt= new ActionBarDrawerToggle(this,dl,R.string.Open,R.string.Close);
        bottomNav=findViewById(R.id.bottomNav);
        titlu=(TextView)findViewById(R.id.titlu);
        pozaMasina=(ImageView)findViewById(R.id.pozaMasina);
        caroserie=(TextView)findViewById(R.id.caroseriaTxt);
        marca=(TextView)findViewById(R.id.marcaTxt);
        anul=(TextView)findViewById(R.id.anulTxt);
        caiPutere=(TextView)findViewById(R.id.caiPutereTxt);
        capCilindrica=(TextView)findViewById(R.id.capCilindricaTxt);
        alimentare=(TextView)findViewById(R.id.alimentareTxt);
        buget=(TextView)findViewById(R.id.bugetTxt);
        save=(Button)findViewById(R.id.saveButton);
    }

    @Override
    public void preluareDate() {
        Intent intent=getIntent();
        email=intent.getStringExtra(intent.EXTRA_EMAIL);
        user= new User();
        user.setEmail(email);
        id=intent.getIntExtra(intent.EXTRA_TEXT, 1);
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
                    Intent intent = new Intent(DetaliiMasini.this, Login.class);
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
                int id= item.getItemId();
                if(id==R.id.cont){
                    Intent intent= new Intent(DetaliiMasini.this, Cont.class);
                    intent.putExtra(intent.EXTRA_EMAIL, email);
                    startActivity(intent);
                }
                else if(id==R.id.statistici){
                    Intent intent= new Intent(DetaliiMasini.this, Statistici.class);
                    intent.putExtra(intent.EXTRA_EMAIL, email);
                    startActivity(intent);
                }
                else if(id==R.id.myCars){
                    Intent intent= new Intent(DetaliiMasini.this, MyCars.class);
                    intent.putExtra(intent.EXTRA_EMAIL, email);
                    startActivity(intent);
                }
                else if(id==R.id.logout){
                    SharedPreferences preferences= getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor= preferences.edit();
                    editor.putString("remember", "false");
                    editor.apply();
                    Intent intent= new Intent(DetaliiMasini.this, Login.class);
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
                        Intent intent= new Intent(DetaliiMasini.this, Home.class);
                        intent.putExtra(intent.EXTRA_EMAIL, email);
                        startActivity(intent);
                    }
                    else if(id==R.id.statistici){
                        Intent intent= new Intent(DetaliiMasini.this, GeneralStatistics.class);
                        intent.putExtra(intent.EXTRA_EMAIL, email);
                        startActivity(new Intent(intent));
                    }
                    else if(id==R.id.more){
                        Intent intent= new Intent(DetaliiMasini.this, Help.class);
                        intent.putExtra(intent.EXTRA_EMAIL, email);
                        startActivity(new Intent(intent));
                    }
                    return true;

                }
            };
    public void baraNavBottom(){
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }//bara navigare bottom

    public boolean onOptionsItemSelected(MenuItem item){
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
    private void completareDate(int idModel){
        DatabaseAccess databaseAccess=DatabaseAccess.getInstance(getApplicationContext());
        databaseAccess.open();
        ArrayList detalii;
        detalii=databaseAccess.getModeleDetalii(idModel);
        titlu.setText(detalii.get(0).toString());
        pozaMasina.setImageResource(getResources().getIdentifier(detalii.get(1).toString(), "drawable", getPackageName()));
        caroserie.setText(detalii.get(2).toString());
        buget.setText(detalii.get(3).toString());
        alimentare.setText(detalii.get(4).toString());
        marca.setText(detalii.get(5).toString());
        anul.setText(detalii.get(6).toString());
        if(detalii.get(7)!=null) {
            caiPutere.setText(detalii.get(7).toString());
            String capCilin = detalii.get(8).toString() + " cm3";
            capCilindrica.setText(capCilin);
        }
        user.setId(databaseAccess.getUserId(user.getEmail()));
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseAccess.open();
                databaseAccess.addCars(user , idModel);
            }
        });
    }
}
