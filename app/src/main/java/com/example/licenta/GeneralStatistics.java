package com.example.licenta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.licenta.helpers.DatabaseAccess;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class GeneralStatistics extends AppCompatActivity implements interfata {
    private String email;
    private NavigationView nav_view;
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private BottomNavigationView bottomNav;
    private HorizontalBarChart barCaroserii;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_statistics);

        initViews();//initializari
        preluareDate();//preluare date din pagina anterioara
        if (email.equals(getResources().getString(R.string.standardEmail))){//daca se intra fara cont se va modifica meniul din bara de start
            barWithoutAccount();
        }
        else{
            barNavStart();
        }
        baraNavBottom();//bara de jos
        setCaroseriiChart();//se construieste graficul de tip bar Chart
    }

    @Override
    public void initViews() {
        nav_view=(NavigationView)findViewById(R.id.nav_view);
        dl=(DrawerLayout)findViewById(R.id.dl);
        abdt= new ActionBarDrawerToggle(this,dl,R.string.Open,R.string.Close);
        bottomNav=(BottomNavigationView)findViewById(R.id.bottomNav);
        barCaroserii=(HorizontalBarChart)findViewById(R.id.barCaroserii);
    }

    @Override
    public void preluareDate() {
        Intent intent= getIntent();
        email= intent.getStringExtra(intent.EXTRA_EMAIL);
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
                    Intent intent = new Intent(GeneralStatistics.this, Login.class);
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
                    Intent intent= new Intent(GeneralStatistics.this, Cont.class);
                    intent.putExtra(intent.EXTRA_EMAIL, email);
                    startActivity(intent);
                }
                else if(id==R.id.statistici){
                    Intent intent= new Intent(GeneralStatistics.this, Statistici.class);
                    intent.putExtra(intent.EXTRA_EMAIL, email);
                    startActivity(intent);
                }
                else if(id==R.id.myCars){
                    Intent intent= new Intent(GeneralStatistics.this, MyCars.class);
                    intent.putExtra(intent.EXTRA_EMAIL, email);
                    startActivity(intent);
                }
                else if(id==R.id.logout){
                    SharedPreferences preferences= getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor= preferences.edit();
                    editor.putString("remember", "false");
                    editor.apply();
                    Intent intent= new Intent(GeneralStatistics.this, Login.class);
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
                        Intent intent= new Intent(GeneralStatistics.this, Home.class);
                        intent.putExtra(intent.EXTRA_EMAIL, email);
                        startActivity(intent);
                    }
                    else if(id==R.id.statistici){
                        Intent intent=new Intent(GeneralStatistics.this, GeneralStatistics.class);
                        intent.putExtra(intent.EXTRA_EMAIL, email);
                        startActivity(intent);
                    }
                    else if(id==R.id.more){
                        Intent intent= new Intent(GeneralStatistics.this, Help.class);
                        intent.putExtra(intent.EXTRA_EMAIL, email);
                        startActivity(new Intent(intent));                    }
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
    private void setCaroseriiChart(){
        DatabaseAccess databaseAccess=DatabaseAccess.getInstance(getApplicationContext());
        databaseAccess.open();
        ArrayList<BarEntry> barEntries= new ArrayList<>();
        ArrayList caroserii= databaseAccess.getNumberCaroserii();
        ArrayList numere= new ArrayList();
        ArrayList denumiriCaroserii= new ArrayList();
        ArrayList id= new ArrayList();
        for(Object object: caroserii){
            boolean exista=false;
            for(Object x: id){
                if((int)x==(int)object) {
                    exista=true;
                    break;
                }
            }
            if(exista==false){
                int count = 0;
                for (Object obj : caroserii) {
                    if ((int) object == (int) obj) {
                        count++;
                    }
                }
                numere.add(count);
                id.add((int) object);
            }
        }
        int total=0;
        for(int i=0;i<numere.size();i++){
            total=total+(int)numere.get(i);
        }
        int j=0;
        for(int i=0;i<numere.size();i++){
            barEntries.add(new BarEntry(j, (int)numere.get(i)));
            j++;
            denumiriCaroserii.add(databaseAccess.getDenumireCaroserie(Integer.parseInt(id.get(i).toString())));
        }
        databaseAccess.close();
        float barWidth=0.8f;
        BarDataSet barDataSet;
        barDataSet= new BarDataSet(barEntries, getResources().getString(R.string.caroseriiTextChart));
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(10f);
        BarData data= new BarData(barDataSet);
        data.setBarWidth(barWidth);
        XAxis xAxis= barCaroserii.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(denumiriCaroserii));
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(caroserii.size());
        xAxis.setLabelRotationAngle(-50);
        barCaroserii.getXAxis().setValueFormatter(new IndexAxisValueFormatter(denumiriCaroserii));
        barCaroserii.setFitBars(true);
        barCaroserii.getDescription().setText(getResources().getString(R.string.chartPopularitate));
        barCaroserii.getDescription().setTextSize(15f);
        barCaroserii.animateY(2000);
        barCaroserii.setData(data);
        barCaroserii.setTouchEnabled(true);
        barCaroserii.setDragEnabled(true);
        barCaroserii.setScaleEnabled(true);
        barCaroserii.invalidate();

    }
}
