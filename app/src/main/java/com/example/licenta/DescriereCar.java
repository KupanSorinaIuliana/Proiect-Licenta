package com.example.licenta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import com.example.licenta.helpers.DatabaseAccess;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class DescriereCar extends AppCompatActivity implements interfata{
    private ImageView imagine, expandMore, expandMorePro, expandMoreContra, expandLess;
    private TextView nume, txt;
    private LinearLayout desc, proL, contraL;
    private String email, car, descriere, pro, contra;
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private BottomNavigationView bottomNav;
    private NavigationView nav_view;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.descriere_car);

        initViews();//initializare View-uri
        preluareDate();//preluarea datelor din pagina anterioara si din baza de date
        if (email.equals(getResources().getString(R.string.standardEmail))){//daca se intra fara cont se va modifica meniul din bara de start
            barWithoutAccount();
        }
        else{
            barNavStart();
        }//creare bara start
        baraNavBottom();//creare bara navigare bottom
        expandDescriere();//click listener pentru sageata de expand de la descriere
        expandPro();//click listener pentru sageata de expand de la argumente pro
        expandContra();//click listener pentru sageata de expand de la argumente contra
    }
    public void initViews(){
        nav_view=(NavigationView)findViewById(R.id.nav_view);
        nume=(TextView)findViewById(R.id.nume);
        imagine=(ImageView)findViewById(R.id.img);
        dl=(DrawerLayout)findViewById(R.id.dl);
        abdt= new ActionBarDrawerToggle(this,dl,R.string.Open,R.string.Close);
        bottomNav=findViewById(R.id.bottomNav);
        expandMore=(ImageView)findViewById(R.id.expandMore);
        desc=(LinearLayout) findViewById(R.id.desc);
        expandMorePro=(ImageView)findViewById(R.id.expandMorePro);
        proL=(LinearLayout) findViewById(R.id.pro);
        expandMoreContra=(ImageView)findViewById(R.id.expandMoreContra);
        contraL=(LinearLayout) findViewById(R.id.contra);
    }
    public void preluareDate(){
        //din pagina anterioara
        Intent intent=getIntent();
        car=intent.getStringExtra(intent.EXTRA_TITLE);
        nume.setText(car);
        int image=intent.getIntExtra(Descriere.EXTRA_NUMBER, R.drawable.ic_baseline_directions_car_24);
        imagine.setImageResource(image);
        email=intent.getStringExtra(intent.EXTRA_EMAIL);

        String[] descrieriArray=getResources().getStringArray(R.array.descrieri);
        String[] proArray=getResources().getStringArray(R.array.pro);
        String[] contraArray=getResources().getStringArray(R.array.contra);
        //din baza de date
        DatabaseAccess databaseAccess=DatabaseAccess.getInstance(getApplicationContext());
        databaseAccess.open();
        int id=databaseAccess.getCaroserieId(car)-1;
        databaseAccess.close();
        descriere= descrieriArray[id];
        pro=proArray[id];
        contra=contraArray[id];
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
                    Intent intent = new Intent(DescriereCar.this, Login.class);
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
                    Intent intent= new Intent(DescriereCar.this, Cont.class);
                    intent.putExtra(intent.EXTRA_EMAIL, email);
                    startActivity(intent);
                }
                else if(id==R.id.statistici){
                    Intent intent= new Intent(DescriereCar.this, Statistici.class);
                    intent.putExtra(intent.EXTRA_EMAIL, email);;
                    startActivity(intent);
                }
                else if(id==R.id.myCars){
                    Intent intent= new Intent(DescriereCar.this, MyCars.class);
                    intent.putExtra(intent.EXTRA_EMAIL, email);
                    startActivity(intent);
                }
                else if(id==R.id.logout){
                    SharedPreferences preferences= getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor= preferences.edit();
                    editor.putString("remember", "false");
                    editor.apply();
                    Intent intent= new Intent(DescriereCar.this, Login.class);
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
                        Intent intent= new Intent(DescriereCar.this, Home.class);
                        intent.putExtra(intent.EXTRA_EMAIL, email);
                        startActivity(intent);
                    }
                    else if(id==R.id.statistici){
                        Intent intent=new Intent(DescriereCar.this, GeneralStatistics.class);
                        intent.putExtra(intent.EXTRA_EMAIL, email);
                        startActivity(intent);
                    }
                    else if(id==R.id.more){
                        Intent intent= new Intent(DescriereCar.this, Help.class);
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
    public void expandDescriere(){
        int txtMarginTop= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, this.getResources().getDisplayMetrics());
        int width= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, this.getResources().getDisplayMetrics());
        int height= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, this.getResources().getDisplayMetrics());
        expandMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt=new TextView(DescriereCar.this);
                txt.setTextColor(getResources().getColor(R.color.black));
                txt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                txt.setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                layoutParams.topMargin=txtMarginTop;
                txt.setLayoutParams(layoutParams);
                txt.setText(descriere);
                desc.addView(txt);
                expandLess=new ImageView(DescriereCar.this);
                expandLess.setLayoutParams(new LinearLayout.LayoutParams(width, height));
                expandLess.setImageResource(R.drawable.ic_baseline_expand_less_24);
                expandLess.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        desc.removeView(txt);
                        desc.removeView(expandLess);
                        desc.addView(expandMore);
                    }
                });
                desc.addView(expandLess);
                desc.removeView(expandMore);
            }
        });
    }
    public void expandPro(){
        int txtMarginTop= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, this.getResources().getDisplayMetrics());
        int width= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, this.getResources().getDisplayMetrics());
        int height= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, this.getResources().getDisplayMetrics());
        expandMorePro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt=new TextView(DescriereCar.this);
                txt.setTextColor(getResources().getColor(R.color.black));
                txt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                txt.setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                layoutParams.topMargin=txtMarginTop;
                txt.setLayoutParams(layoutParams);
                txt.setText(pro);
                proL.addView(txt);
                expandLess=new ImageView(DescriereCar.this);
                expandLess.setLayoutParams(new LinearLayout.LayoutParams(width, height));
                expandLess.setImageResource(R.drawable.ic_baseline_expand_less_24);
                expandLess.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        proL.removeView(txt);
                        proL.removeView(expandLess);
                        proL.addView(expandMorePro);
                    }
                });
                proL.addView(expandLess);
                proL.removeView(expandMorePro);
            }
        });
    }
    public void expandContra(){
        int txtMarginTop= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, this.getResources().getDisplayMetrics());
        int width= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, this.getResources().getDisplayMetrics());
        int height= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, this.getResources().getDisplayMetrics());
        expandMoreContra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt=new TextView(DescriereCar.this);
                txt.setTextColor(getResources().getColor(R.color.black));
                txt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                txt.setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                layoutParams.topMargin=txtMarginTop;
                txt.setLayoutParams(layoutParams);
                txt.setText(contra);
                contraL.addView(txt);
                expandLess=new ImageView(DescriereCar.this);
                expandLess.setLayoutParams(new LinearLayout.LayoutParams(width, height));
                expandLess.setImageResource(R.drawable.ic_baseline_expand_less_24);
                expandLess.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        contraL.removeView(txt);
                        contraL.removeView(expandLess);
                        contraL.addView(expandMoreContra);
                    }
                });
                contraL.addView(expandLess);
                contraL.removeView(expandMoreContra);
            }
        });
    }
}
