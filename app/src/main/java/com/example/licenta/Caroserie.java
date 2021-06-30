package com.example.licenta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.licenta.helpers.DatabaseAccess;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class Caroserie extends AppCompatActivity implements interfata{
    private String email;// se preiau din pagina anterioara
    private TextView  intrebare, txtAfter;
    private DrawerLayout dl;
    public RelativeLayout sec, relLayout, relLayout1;
    private ActionBarDrawerToggle abdt;
    private BottomNavigationView bottomNav;
    private ImageView sageata;
    private int countIntrebare,countVarianta, btnId;
    private Resources res;
    private String[] intrebari;
    private String[] firma, utilizare, copii, concediu, sex, experienta, principala;
    private String[][] variante ;
    private String[] raspunsuri;
    private RadioGroup rg;
    private RadioButton[] rb ;
    private RadioButton radioBtn;
    private String[] answers;
    private ListView lstCaroserii;
    private NavigationView nav_view;
    private Button btnAfla;
    double c1 = 0;
    double c2 = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.caroserie);
        initViews();//initializare
        preluareDate();//preluarea datelor din pagina anterioara
        if (email.equals(getResources().getString(R.string.standardEmail))){//daca se intra fara cont se va modifica meniul din bara de start
            barWithoutAccount();
        }
        else{
            barNavStart();
        }
        baraNavBottom();//meniul de jos
        Intrebari();//afisare intrebari, preluare raspunsuri, prelucrare rezultate, afisare rezultate
        clickBtnAfla();//redirectionare catre pagina de sugestii modele
    }
    public void initViews(){
        nav_view=(NavigationView)findViewById(R.id.nav_view);
        dl=(DrawerLayout)findViewById(R.id.dl);
        abdt= new ActionBarDrawerToggle(this,dl,R.string.Open,R.string.Close);
        res= getResources();
        intrebari= res.getStringArray(R.array.intrebari);
        firma= res.getStringArray(R.array.firma);
        utilizare= res.getStringArray(R.array.utilizare);
        copii=res.getStringArray(R.array.copii);
        concediu=res.getStringArray(R.array.concediu);
        sex=res.getStringArray(R.array.sex);
        experienta=res.getStringArray(R.array.experienta);
        principala=res.getStringArray(R.array.principala);
        variante= new String[][] { firma, utilizare, copii, concediu, sex, experienta, principala};
        answers= new String[20];
        relLayout1=(RelativeLayout)findViewById(R.id.relLayout1);
        intrebare=(TextView)findViewById(R.id.intrebare);
        sageata=(ImageView)findViewById(R.id.est);
        rg=(RadioGroup)findViewById(R.id.rg);
        relLayout=(RelativeLayout)findViewById(R.id.relLayout);
        sec=(RelativeLayout)findViewById(R.id.sec);
        lstCaroserii=(ListView)findViewById(R.id.lstCaroserii);
        btnAfla=findViewById(R.id.btnAfla);
        txtAfter=findViewById(R.id.txtAfter);
    }
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
                    Intent intent = new Intent(Caroserie.this, Login.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }
    @Override
    public void barNavStart(){
        nav_view.inflateMenu(R.menu.navigation_menu);
        dl.addDrawerListener(abdt);
        abdt.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final NavigationView navView= (NavigationView)findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id= item.getItemId();
                if(id==R.id.cont){
                    Intent intent= new Intent(Caroserie.this, Cont.class);
                    intent.putExtra(intent.EXTRA_EMAIL, email);
                    startActivity(intent);
                }
                else if(id==R.id.statistici){
                    Intent intent= new Intent(Caroserie.this, Statistici.class);
                    intent.putExtra(intent.EXTRA_EMAIL, email);
                    startActivity(intent);
                }
                else if(id==R.id.myCars){
                    Intent intent= new Intent(Caroserie.this, MyCars.class);
                    intent.putExtra(intent.EXTRA_EMAIL, email);
                    startActivity(intent);
                }
                else if(id==R.id.logout){
                    SharedPreferences preferences= getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor= preferences.edit();
                    editor.putString("remember", "false");
                    editor.apply();
                    Intent intent= new Intent(Caroserie.this, Login.class);
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
                        Intent intent= new Intent(Caroserie.this, Home.class);
                        intent.putExtra(intent.EXTRA_EMAIL, email);
                        startActivity(intent);
                    }
                    else if(id==R.id.statistici){
                        Intent intent=new Intent(Caroserie.this, GeneralStatistics.class);
                        intent.putExtra(intent.EXTRA_EMAIL, email);
                        startActivity(intent);
                    }
                    else if(id==R.id.more){
                        Intent intent= new Intent(Caroserie.this, Help.class);
                        intent.putExtra(intent.EXTRA_EMAIL, email);
                        startActivity(new Intent(intent));
                    }
                    return true;

                }
            };
    public void baraNavBottom(){
        bottomNav=findViewById(R.id.bottomNav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }//bara navigare bottom
    public boolean onOptionsItemSelected(MenuItem item){
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
    public void Intrebari(){
        rb= new RadioButton[10];
        rg.setOrientation(RadioGroup.VERTICAL);
        countIntrebare=0;
        intrebare.setText(intrebari[countIntrebare]);
        raspunsuri=variante[0];
        rb[0]= new RadioButton(Caroserie.this);
        rb[0].setText(raspunsuri[0]);
        rb[1]= new RadioButton(Caroserie.this);
        rb[1].setText(raspunsuri[1]);
        for(int m=0;m<2;m++){
            rb[m].setTextColor(this.getResources().getColor(R.color.black));
        }
        rg.addView(rb[0]);
        rg.addView(rb[1]);
        rb[0].setChecked(true);
        countVarianta=1;
        raspunsuri=variante[countVarianta];
        sageata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnId=rg.getCheckedRadioButtonId();
                radioBtn=findViewById(btnId);
                answers[countIntrebare]= radioBtn.getText().toString();
                try{
                    int count=0;
                    rg.removeAllViews();
                    countIntrebare = countIntrebare + 1;
                    String x=intrebari[countIntrebare];
                    intrebare.setText(x);
                    for(int j=0;j<raspunsuri.length;j++){
                        rb[j]  = new RadioButton(Caroserie.this);
                        rb[j].setTextColor(Caroserie.this.getResources().getColor(R.color.black));
                        rb[j].setText(raspunsuri[j]);
                        rb[j].setId(j);
                        rg.addView(rb[j]);
                        count=j;
                    }
                    rb[count].setChecked(true);
                }catch(Exception e){
                    rg.removeAllViews();
                    intrebare.setText("Nu mai sunt intrebari");
                    prelucrareRez();
                }

                countVarianta=countVarianta+1;
                if(countVarianta<variante.length){
                    raspunsuri=variante[countVarianta];
                }
            }
        });
    }//afisare intrebari si raspunsuri
    public void prelucrareRez(){
        int j = 0;
        String firmaA = answers[j];
        String utilizareA = answers[j + 1];
        String familieA = answers[j + 2];
        String concediuA = answers[j + 3];
        String sexA = answers[j + 4];
        String experientaA = answers[j + 5];
        String principalaA = answers[j + 6];
        int princ = 0;
        int conc = 0;
        int firmaN = 0;
        DatabaseAccess databaseAccess=DatabaseAccess.getInstance(getApplicationContext());
        databaseAccess.open();

        //legare cu valoarea numerica pentru a corespunde cu valorile din baza de date din campul FIRMA
        if(firmaA.equals(firma[0])){
            firmaN =1;
        }
        //legare cu valoarea numerica pentru a corespunde cu valorile din baza de date din campul CONCEDIU
        if(concediuA.equals(concediu[0])){
            conc =1;
        }
        //legare cu valoarea numerica pentru a corespunde cu valorile din baza de date din campul PRINCIPALA
        if(principalaA.equals(principala[0])){
            princ =1;
        }
        //legare cu denumirile din baza de date ale tabelului UTILIZARE
        if(utilizareA.equals(utilizare[0])){
            utilizareA =databaseAccess.getDenumireUtilizare("UB");
        }else if(utilizareA.equals(utilizare[1])){
            utilizareA =databaseAccess.getDenumireUtilizare("EXUB");
        }else{
            utilizareA =databaseAccess.getDenumireUtilizare("MX");
        }
        //legare cu denumirile din baza de date ale tabelului COPII
        if(familieA.equals(copii[0])){
            familieA =databaseAccess.getCopii(1);
        }else if (familieA.equals(copii[1])){
            familieA =databaseAccess.getCopii(2);
        }else if (familieA.equals(copii[2])){
            familieA =databaseAccess.getCopii(3);
        }else{
            familieA =databaseAccess.getCopii(4);
        }
        //legare cu denumirile din baza de date ale tabelului SEX
        if(sexA.equals(sex[0])){
            sexA =databaseAccess.getDenumireSex("F");
        }else if (sexA.equals(sex[1])){
            sexA =databaseAccess.getDenumireSex("M");
        }else{
            sexA =databaseAccess.getDenumireSex("MX");
        }
        //legare cu denumirile din baza de date ale tabelului EXPERIENTA
        if (experientaA.equals(experienta[0])){
            experientaA =databaseAccess.getDenumireExperienta("icr");
        }else if(experientaA.equals(experienta[1])){
            experientaA =databaseAccess.getDenumireExperienta("mdu");
        }else{
            experientaA =databaseAccess.getDenumireExperienta("avt");
        }
        List caroserii=databaseAccess.getCaroserii();
        ArrayList detalii;
        List<Double> procente= new ArrayList<Double>();
        double i=14.28;
        for (Object object:caroserii) {
             detalii=databaseAccess.getCaroserie(object.toString());
             double procent=0;
            //firma
            if(firmaN== (int) detalii.get(0)){
                procent=i;
            }
            //utilizare
            if(utilizareA.equals(databaseAccess.getDenumireUtilizare(detalii.get(1).toString()))){
                procent=procent+i;
            }
            //copii
            if(familieA.equals(databaseAccess.getCopii(Integer.parseInt(detalii.get(2).toString())))){
                procent=procent+i;
            }
            //concediu
            if(conc==(int) detalii.get(3)){
                procent=procent+i;
            }
            //sex
            if(sexA.equals(databaseAccess.getDenumireSex(detalii.get(4).toString())) || detalii.get(4).equals("MX")){
                procent=procent+i;
            }
            //experienta
            if(experientaA.equals(databaseAccess.getDenumireExperienta(detalii.get(5).toString())) || detalii.get(5).equals("t")){
                procent=procent+i;
            }
            if((experientaA.equals("incepator") || experientaA.equals("mediu")) && detalii.get(5).equals("im")){
                procent=procent+i;
            }
            if((experientaA.equals("mediu") || experientaA.equals("avansat")) && detalii.get(5).equals("ma")){
                procent=procent+i;
            }
            //principala
            if(princ== (int) detalii.get(6)){
                procent=procent+i;
            }
            procente.add(procent);
        }
        databaseAccess.close();
        List<String> caroseriiPotrivite=new ArrayList<String>();
        for(int m=0;m<caroserii.size();m++){
            if(procente.get(m)>=42.84){
                caroseriiPotrivite.add(caroserii.get(m).toString());
           }
        }
        if(caroseriiPotrivite.isEmpty()) {
            for (int m = 0; m < caroserii.size(); m++) {
                if (procente.get(m) >= 28.5) {
                    caroseriiPotrivite.add(caroserii.get(m).toString());
                }
            }
        }
        relLayout.removeView(relLayout1);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.m,R.id.textView, caroseriiPotrivite);
        lstCaroserii.setAdapter(arrayAdapter);
        sageata.setVisibility(View.INVISIBLE);
        sec.setVisibility(View.VISIBLE);
        txtAfter.setVisibility(View.VISIBLE);
        btnAfla.setVisibility(View.VISIBLE);
    }
    public void clickBtnAfla(){
        btnAfla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Caroserie.this, Masina.class);
                intent.putExtra(intent.EXTRA_EMAIL, email);
                startActivity(intent);
            }
        });
    }
}
