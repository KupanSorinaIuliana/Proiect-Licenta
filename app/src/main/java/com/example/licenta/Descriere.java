package com.example.licenta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.licenta.helpers.DatabaseAccess;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class Descriere extends AppCompatActivity implements interfata{
    private String email;
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private BottomNavigationView bottomNav;
    private CardView cardView;
    private GridLayout grid;
    private int imagine;
    private int images[]={R.drawable.micro, R.drawable.sedan, R.drawable.hatchback, R.drawable.coupe, R.drawable.station_wagon,R.drawable.roadster, R.drawable.cabriolet,R.drawable.muscle_car, R.drawable.sport_car, R.drawable.suv, R.drawable.suv_compact, R.drawable.pickup, R.drawable.minivan, R.drawable.van};
    public static final String EXTRA_NUMBER="com.example.application.EXTRA_NUMBER";
    private NavigationView nav_view;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.descriere);

        initViews();//se initializeaza View-urile
        preluareDate();//se preiau limba si email-ul din pagina anterioara
        if (email.equals(getResources().getString(R.string.standardEmail))){//daca se intra fara cont se va modifica meniul din bara de start
            barWithoutAccount();
        }
        else{
            barNavStart();
        }//se creaza bara de start
        baraNavBottom();//se creaza bara de navigatie din partea de jos a UI-ului
        setareDenumiri();//se creaza cate un CardView pentru fiecare caroserie in parte, se extrage denumirea din baza de date si de asemenea imaginile din images
    }
    @Override
    public void initViews(){
        dl=(DrawerLayout)findViewById(R.id.dl);
        abdt= new ActionBarDrawerToggle(this,dl,R.string.Open,R.string.Close);
        bottomNav=findViewById(R.id.bottomNav);
        nav_view=(NavigationView)findViewById(R.id.nav_view);
        grid = (GridLayout) findViewById(R.id.grid);
        imagine=0;
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
                    Intent intent = new Intent(Descriere.this, Login.class);
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
                    Intent intent= new Intent(Descriere.this, Cont.class);
                    intent.putExtra(intent.EXTRA_EMAIL, email);
                    startActivity(intent);
                }
                else if(id==R.id.statistici){
                    Intent intent= new Intent(Descriere.this, Statistici.class);
                    intent.putExtra(intent.EXTRA_EMAIL, email);
                    startActivity(intent);
                }
                else if(id==R.id.myCars){
                    Intent intent= new Intent(Descriere.this, MyCars.class);
                    intent.putExtra(intent.EXTRA_EMAIL, email);
                    startActivity(intent);
                }
                else if(id==R.id.logout){
                    SharedPreferences preferences= getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor= preferences.edit();
                    editor.putString("remember", "false");
                    editor.apply();
                    Intent intent= new Intent(Descriere.this, Login.class);
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
                        Intent intent= new Intent(Descriere.this, Home.class);
                        intent.putExtra(intent.EXTRA_EMAIL, email);
                        startActivity(intent);
                    }
                    else if(id==R.id.statistici){
                        Intent intent=new Intent(Descriere.this, GeneralStatistics.class);
                        intent.putExtra(intent.EXTRA_EMAIL, email);
                        startActivity(intent);
                    }
                    else if(id==R.id.more){
                        Intent intent= new Intent(Descriere.this, Help.class);
                        intent.putExtra(intent.EXTRA_EMAIL, email);
                        startActivity(new Intent(intent));
                    }
                    return true;

                }
            };
    @Override
    public void baraNavBottom(){
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }//bara navigare bottom
    public boolean onOptionsItemSelected(MenuItem item){
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
    public void setareDenumiri() {
        DatabaseAccess databaseAccess= DatabaseAccess.getInstance(getApplicationContext());
        databaseAccess.open();
        List<String> caroserii = databaseAccess.getCaroserii();
        databaseAccess.close();
        int paddingLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, this.getResources().getDisplayMetrics());
        int paddingRight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, this.getResources().getDisplayMetrics());
        int paddingTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, this.getResources().getDisplayMetrics());
        int paddingBottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, this.getResources().getDisplayMetrics());
        int cvLayout= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, this.getResources().getDisplayMetrics());
        int cvMargin= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, this.getResources().getDisplayMetrics());
        int cvRadius= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, this.getResources().getDisplayMetrics());
        int cvElevation= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, this.getResources().getDisplayMetrics());
        int cvDim= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, this.getResources().getDisplayMetrics());
        int tvMarginTop= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, this.getResources().getDisplayMetrics());
        for (Object object : caroserii) {
            cardView = new CardView(this);
            cardView.setRadius(cvRadius);
            cardView.setCardElevation(cvElevation);
            LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(cvDim, cvDim, 1.0f);
            layoutParams.setMargins(cvMargin,cvMargin,cvMargin,cvMargin);
            cardView.setLayoutParams(layoutParams);
            grid.addView(cardView);
            LinearLayout ly = new LinearLayout(this);
            ly.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            ly.setOrientation(LinearLayout.VERTICAL);
            ly.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
            ly.setVerticalGravity(Gravity.CENTER_VERTICAL);
            ly.setPaddingRelative(paddingLeft, paddingTop, paddingRight,  paddingBottom);
            cardView.addView(ly);
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(new LinearLayout.LayoutParams(cvLayout, cvLayout));
            iv.setImageResource(images[imagine]);
            int image=images[imagine];
            ly.addView(iv);
            TextView tv = new TextView(this);
            tv.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams layoutParams1= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams1.topMargin=tvMarginTop;
            tv.setLayoutParams(layoutParams1);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            tv.setTextColor(Descriere.this.getResources().getColor(R.color.black));
            tv.setText(object.toString());
            ly.addView(tv);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(Descriere.this, DescriereCar.class);
                    String caroserie=tv.getText().toString();
                    intent.putExtra(Intent.EXTRA_TITLE, caroserie);
                    intent.putExtra(EXTRA_NUMBER, image);
                    intent.putExtra(intent.EXTRA_EMAIL, email);
                    startActivity(intent);
                }
            });
            imagine++;
        }
    }
}


