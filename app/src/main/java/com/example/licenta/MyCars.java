package com.example.licenta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.example.licenta.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MyCars extends AppCompatActivity implements interfata{
    private String email;
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private BottomNavigationView bottomNav;
    private CardView cardView;
    private GridLayout grid;
    private NavigationView nav_view;
    private User user= new User();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_cars);

        initViews();//initializari
        preluareDate();//preluarea datelor din pagina anterioara
        barNavStart();//bara de start
        baraNavBottom();//bara de jos
        afisareMasiniSalvate(email);//afisarea masinilor salvate in functie de email
    }

    @Override
    public void initViews() {
        nav_view=(NavigationView)findViewById(R.id.nav_view);
        dl=(DrawerLayout)findViewById(R.id.dl);
        abdt= new ActionBarDrawerToggle(this,dl,R.string.Open,R.string.Close);
        bottomNav=findViewById(R.id.bottomNav);
        grid=findViewById(R.id.grid);
    }

    @Override
    public void preluareDate() {
        Intent intent= getIntent();
        email= intent.getStringExtra(intent.EXTRA_EMAIL);
        user.setEmail(email);
    }

    @Override
    public void barNavStart() {
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
                    Intent intent= new Intent(MyCars.this, Cont.class);
                    intent.putExtra(intent.EXTRA_EMAIL, email);
                    startActivity(intent);
                }
                else if(id==R.id.statistici){
                    Intent intent= new Intent(MyCars.this, Statistici.class);
                    intent.putExtra(intent.EXTRA_EMAIL, email);
                    startActivity(intent);
                }
                else if(id==R.id.myCars){
                    Intent intent= new Intent(MyCars.this, MyCars.class);
                    intent.putExtra(intent.EXTRA_EMAIL, email);
                    startActivity(intent);
                }
                else if(id==R.id.logout){
                    SharedPreferences preferences= getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor= preferences.edit();
                    editor.putString("remember", "false");
                    editor.apply();
                    Intent intent= new Intent(MyCars.this, Login.class);
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
                        Intent intent= new Intent(MyCars.this, Home.class);
                        intent.putExtra(intent.EXTRA_EMAIL, email);
                        startActivity(intent);
                    }
                    else if(id==R.id.statistici){
                        Intent intent= new Intent(MyCars.this, GeneralStatistics.class);
                        intent.putExtra(intent.EXTRA_EMAIL, email);
                        startActivity(new Intent(intent));
                    }
                    else if(id==R.id.more){
                        Intent intent= new Intent(MyCars.this, Help.class);
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

    private void afisareMasiniSalvate(String emailUser){
        DatabaseAccess databaseAccess=DatabaseAccess.getInstance(getApplicationContext());
        databaseAccess.open();
        user.setId(databaseAccess.getUserId(user.getEmail()));
        ArrayList modeleSalvate= databaseAccess.getCars(user);
        int paddingLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, this.getResources().getDisplayMetrics());
        int paddingLeft1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, this.getResources().getDisplayMetrics());
        int paddingRight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, this.getResources().getDisplayMetrics());
        int paddingTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, this.getResources().getDisplayMetrics());
        int paddingBottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, this.getResources().getDisplayMetrics());
        int cvLayout= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 350, this.getResources().getDisplayMetrics());
        int cvLayout2= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 230, this.getResources().getDisplayMetrics());
        int cvMargin= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, this.getResources().getDisplayMetrics());
        int cvMarginTop=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 390, this.getResources().getDisplayMetrics());
        int cvMarginTop1=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 130, this.getResources().getDisplayMetrics());
        int cvMarginTop2=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 260, this.getResources().getDisplayMetrics());
        int cvRadius= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, this.getResources().getDisplayMetrics());
        int cvElevation= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, this.getResources().getDisplayMetrics());
        int cvDim= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, this.getResources().getDisplayMetrics());
        int cvDim1= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 380, this.getResources().getDisplayMetrics());
        int tvMarginTop= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, this.getResources().getDisplayMetrics());
        int tvMarginTop1= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, this.getResources().getDisplayMetrics());
        int widthButton=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, this.getResources().getDisplayMetrics());
        int heightButton=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, this.getResources().getDisplayMetrics());
        ArrayList detalii;
        for(Object object: modeleSalvate){
            detalii=databaseAccess.getModeleDetalii((int) object);
            cardView = new CardView(this);
            cardView.setRadius(cvRadius);
            cardView.setCardElevation(cvElevation);
            LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(cvDim1, cvDim, 1.0f);
            layoutParams.setMargins(cvMargin,cvMargin,cvMargin,cvMargin);
            cardView.setLayoutParams(layoutParams);
            grid.addView(cardView);
            LinearLayout ly = new LinearLayout(this);
            ly.setLayoutParams(new LinearLayout.LayoutParams(cvMarginTop, LinearLayout.LayoutParams.WRAP_CONTENT));
            ly.setOrientation(LinearLayout.HORIZONTAL);
            ly.setVerticalGravity(Gravity.CENTER_VERTICAL);
            cardView.addView(ly);
            LinearLayout ly1 = new LinearLayout(this);
            ly1.setLayoutParams(new LinearLayout.LayoutParams(cvMarginTop2, LinearLayout.LayoutParams.WRAP_CONTENT));
            ly1.setOrientation(LinearLayout.HORIZONTAL);
            ly1.setVerticalGravity(Gravity.CENTER_VERTICAL);
            ly1.setPaddingRelative(paddingLeft, paddingTop, paddingRight,  paddingBottom);
            ly.addView(ly1);
            LinearLayout ly2 = new LinearLayout(this);
            ly2.setLayoutParams(new LinearLayout.LayoutParams(cvMarginTop1, LinearLayout.LayoutParams.MATCH_PARENT));
            ly2.setOrientation(LinearLayout.VERTICAL);
            ly2.setVerticalGravity(Gravity.CENTER_HORIZONTAL);
            ly2.setPaddingRelative(paddingLeft1, paddingTop, paddingRight,  paddingBottom);
            ly.addView(ly2);
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(new LinearLayout.LayoutParams(cvLayout2, cvLayout));
            iv.setImageResource(getResources().getIdentifier(detalii.get(1).toString(), "drawable", getPackageName()));
            ly1.addView(iv);
            TextView tv = new TextView(this);
            LinearLayout.LayoutParams layoutParams1= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams1.topMargin=tvMarginTop;
            tv.setLayoutParams(layoutParams1);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            tv.setTextColor(MyCars.this.getResources().getColor(R.color.black));
            tv.setText(detalii.get(0).toString());
            ly2.addView(tv);
            Button bt= new Button(MyCars.this);
            bt.setText(getResources().getString(R.string.details));
            LinearLayout.LayoutParams layoutParams2= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams2.topMargin=tvMarginTop1;
            bt.setLayoutParams(layoutParams2);
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(MyCars.this, DetaliiMasiniSalvate.class);
                    intent.putExtra(intent.EXTRA_EMAIL, email);
                    intent.putExtra(intent.EXTRA_TEXT, (int)object);
                    startActivity(intent);
                }
            });
            ly2.addView(bt);
        }
    }
}
