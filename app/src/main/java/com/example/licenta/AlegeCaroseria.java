package com.example.licenta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.licenta.helpers.DatabaseAccess;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class AlegeCaroseria extends AppCompatActivity implements interfata{
    private ListView lstCaroserii1,lstCaroserii2;
    private RelativeLayout relCompara;
    private TextView caroserieTxt1, caroserieTxt2;
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private BottomNavigationView bottomNav;
    private String email;
    private NavigationView nav_view;
    private ImageView image1, image2;
    private int idCaroserieBun1, idCaroserieBun2;
    private int images[]={R.drawable.micro, R.drawable.sedan, R.drawable.hatchback, R.drawable.coupe, R.drawable.station_wagon,R.drawable.roadster, R.drawable.cabriolet,R.drawable.muscle_car, R.drawable.sport_car, R.drawable.suv, R.drawable.suv_compact, R.drawable.pickup, R.drawable.minivan, R.drawable.van};
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alege_caroseria);
        initViews();//initializare
        preluareDate();//preluarea datelor din pagina anterioara
        if (email.equals(getResources().getString(R.string.standardEmail))){//daca se intra fara cont se va modifica meniul din bara de start
            barWithoutAccount();
        }
        else{
            barNavStart();
        }
        baraNavBottom();//bara de jos
        preluareCarBD();//preluarea caroseriilor din baza de date
        afisareCarSelectate();//la click pe numele caroseriei, se va adauga in text view-ul aferent listei numele caroseriei
        buttonCompara();//butonul Compara va trimite utilizatorul spre pagina comparatie
   }

    @Override
    public void initViews() {
        dl=(DrawerLayout)findViewById(R.id.dl);
        abdt= new ActionBarDrawerToggle(this,dl,R.string.Open,R.string.Close);
        bottomNav=findViewById(R.id.bottomNav);
        lstCaroserii1=(ListView)findViewById(R.id.lstCaroserii1);
        lstCaroserii2=(ListView)findViewById(R.id.lstCaroserii2);
        caroserieTxt1=findViewById(R.id.caroserieTxt1);
        caroserieTxt2=findViewById(R.id.caroserieTxt2);
        relCompara=(RelativeLayout) findViewById(R.id.relCompara);
        nav_view=(NavigationView)findViewById(R.id.nav_view);
        image1=(ImageView)findViewById(R.id.image1);
        image2=(ImageView)findViewById(R.id.image2);
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
                    Intent intent = new Intent(AlegeCaroseria.this, Login.class);
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
                   Intent intent= new Intent(AlegeCaroseria.this, Cont.class);
                   intent.putExtra(intent.EXTRA_EMAIL, email);
                   startActivity(intent);
               }
               else if(id==R.id.statistici){
                   Intent intent= new Intent(AlegeCaroseria.this, Statistici.class);
                   intent.putExtra(intent.EXTRA_EMAIL, email);
                   startActivity(intent);
               }
               else if(id==R.id.myCars){
                   Intent intent= new Intent(AlegeCaroseria.this, MyCars.class);
                   intent.putExtra(intent.EXTRA_EMAIL, email);
                   startActivity(intent);
               }
               else if(id==R.id.logout){
                   SharedPreferences preferences= getSharedPreferences("checkbox", MODE_PRIVATE);
                   SharedPreferences.Editor editor= preferences.edit();
                   editor.putString("remember", "false");
                   editor.apply();
                   Intent intent= new Intent(AlegeCaroseria.this, Login.class);
                   startActivity(intent);
               }
               return true;
           }
       });
   }//bara navigare start
    final private BottomNavigationView.OnNavigationItemSelectedListener navListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int id= item.getItemId();
                    if(id==R.id.home){
                        Intent intent= new Intent(AlegeCaroseria.this, Home.class);
                        intent.putExtra(intent.EXTRA_EMAIL, email);
                        startActivity(intent);
                    }
                    else if(id==R.id.statistici){
                        Intent intent= new Intent(AlegeCaroseria.this, GeneralStatistics.class);
                        intent.putExtra(intent.EXTRA_EMAIL, email);
                        startActivity(new Intent(intent));
                    }
                    else if(id==R.id.more){
                        Intent intent= new Intent(AlegeCaroseria.this, Help.class);
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
   public void OpenActivity(){
        String  caroserie1, caroserie2;
        if(!(caroserieTxt1.getText().toString().equals(getResources().getString(R.string.primaCaroserie))) && !(caroserieTxt2.getText().toString().equals(getResources().getString(R.string.adouaCaroserie)))){
            Intent intent= new Intent(this, Comparatie.class);
            caroserie1=caroserieTxt1.getText().toString();
            caroserie2=caroserieTxt2.getText().toString();
            intent.putExtra("caroserie1",caroserie1);
            intent.putExtra("caroserie2",caroserie2);
            intent.putExtra(intent.EXTRA_EMAIL, email);
            intent.putExtra("image1", idCaroserieBun1);
            intent.putExtra("image2", idCaroserieBun2);
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.titlu), Toast.LENGTH_LONG).show();
        }
   }//trimitere catre pagina comparatie cu date legate de numele caroseriilor selectate
   public void preluareCarBD(){
       DatabaseAccess databaseAccess=DatabaseAccess.getInstance(getApplicationContext());
       databaseAccess.open();
       List<String> s=databaseAccess.getCaroserii();
       ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.m,R.id.textView, s);
       lstCaroserii1.setAdapter(arrayAdapter);
       lstCaroserii2.setAdapter(arrayAdapter);
   }//afisare caroserii din BD
   public void afisareCarSelectate(){
       DatabaseAccess databaseAccess=DatabaseAccess.getInstance(getApplicationContext());
       databaseAccess.open();
       lstCaroserii1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               caroserieTxt1.setText(lstCaroserii1.getItemAtPosition(position).toString());
               int idCaroserie= databaseAccess.getCaroserieId(lstCaroserii1.getItemAtPosition(position).toString());
               idCaroserieBun1=idCaroserie-1;
               image1.setImageResource(images[idCaroserieBun1]);
           }
       });
       lstCaroserii2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               caroserieTxt2.setText(lstCaroserii2.getItemAtPosition(position).toString());
               int idCaroserie= databaseAccess.getCaroserieId(lstCaroserii1.getItemAtPosition(position).toString());
               idCaroserieBun2=idCaroserie-1;
               image2.setImageResource(images[idCaroserieBun2]);
           }
       });
   }//dupa selectarea caroseriilor se va putea vizualiza mai jos ce s-a selectat
   public void buttonCompara(){
       relCompara.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               OpenActivity();
           }
       });
   }//trimitere catre comparatie.java/xml
}