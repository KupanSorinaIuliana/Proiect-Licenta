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
import com.example.licenta.model.User;
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

public class Statistici extends AppCompatActivity implements interfata{
    private HorizontalBarChart caroseriiChart;
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private BottomNavigationView bottomNav;
    private String email;
    private User user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistici);

        initViews();//initializari
        preluareDate();//preluare date din pagina anterioara
        barNavStart();//bara de start
        baraNavBottom();//bara de jos
        setDataCaroserii();//setarea graficului de tip bar chart
    }

    @Override
    public void initViews() {
        caroseriiChart=(HorizontalBarChart)findViewById(R.id.barCaroserii);
        dl=(DrawerLayout)findViewById(R.id.dl);
        abdt= new ActionBarDrawerToggle(this,dl,R.string.Open,R.string.Close);
        bottomNav=findViewById(R.id.bottomNav);
        user= new User();
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
                    Intent intent= new Intent(Statistici.this, Cont.class);
                    intent.putExtra(intent.EXTRA_EMAIL, email);
                    startActivity(intent);
                }
                else if(id==R.id.statistici){
                    Intent intent= new Intent(Statistici.this, Statistici.class);
                    intent.putExtra(intent.EXTRA_EMAIL, email);
                    startActivity(intent);
                }
                else if(id==R.id.myCars){
                    Intent intent= new Intent(Statistici.this, MyCars.class);
                    intent.putExtra(intent.EXTRA_EMAIL, email);
                    startActivity(intent);
                }
                else if(id==R.id.logout){
                    SharedPreferences preferences= getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor= preferences.edit();
                    editor.putString("remember", "false");
                    editor.apply();
                    Intent intent= new Intent(Statistici.this, Login.class);
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
                        Intent intent= new Intent(Statistici.this, Home.class);
                        intent.putExtra(intent.EXTRA_EMAIL, email);
                        startActivity(intent);
                    }
                    else if(id==R.id.statistici){
                        Intent intent= new Intent(Statistici.this, GeneralStatistics.class);
                        intent.putExtra(intent.EXTRA_EMAIL, email);
                        startActivity(new Intent(intent));
                    }
                    else if(id==R.id.more){
                        Intent intent= new Intent(Statistici.this, Help.class);
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

    private void setDataCaroserii(){
        DatabaseAccess databaseAccess=DatabaseAccess.getInstance(getApplicationContext());
        databaseAccess.open();
        user.setId(databaseAccess.getUserId(user.getEmail()));
        ArrayList carProcente=databaseAccess.getStatistici(user);
        ArrayList date;
        ArrayList<BarEntry> barEntries= new ArrayList<>();
        int i=0;
        List<String> caroserii= new ArrayList<>();
        for(Object object: carProcente){
            date= databaseAccess.getStatisticiTot((int)object);
            barEntries.add(new BarEntry(i, (int)date.get(0)));
            i++;
            caroserii.add(databaseAccess.getDenumireCaroserie((int)date.get(1)));
        }
        databaseAccess.close();
        float barWidth=0.8f;
        BarDataSet barDataSet;
        barDataSet= new BarDataSet(barEntries, getResources().getString(R.string.caroseriiTextChart));
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(25f);
        BarData data= new BarData(barDataSet);
        data.setBarWidth(barWidth);
        XAxis xAxis= caroseriiChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(caroserii));
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(caroserii.size());
        xAxis.setLabelRotationAngle(-50);
        caroseriiChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(caroserii));
        caroseriiChart.setFitBars(true);
        caroseriiChart.getDescription().setText(getResources().getString(R.string.chartComparatie));
        caroseriiChart.getDescription().setTextSize(15f);
        caroseriiChart.animateY(2000);
        caroseriiChart.setData(data);
        caroseriiChart.setTouchEnabled(true);
        caroseriiChart.setDragEnabled(true);
        caroseriiChart.setScaleEnabled(true);
        caroseriiChart.invalidate();
    }

}
