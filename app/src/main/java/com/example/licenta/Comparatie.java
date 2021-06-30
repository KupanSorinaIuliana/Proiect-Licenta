package com.example.licenta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.licenta.helpers.DatabaseAccess;
import com.example.licenta.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

public class Comparatie extends AppCompatActivity implements interfata{

    private TextView nume1, nume2, intrebare, txtProcent, txtProcent1, txtProcent2, txtAfter, nrIntrebare;
    private Button btnAfla;
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private BottomNavigationView bottomNav;
    private ImageView est;
    private int countIntrebare, countVarianta, btnId, nr;
    private String[] intrebari, firma, utilizare, copii, concediu, sex, experienta, principala, raspunsuri, answers;
    private String[][] variante;
    private RadioGroup rg;
    private RadioButton[] rb ;
    private RadioButton radioBtn;
    private String carA, carB, email;
    private User user;
    private NavigationView nav_view;
    private int idImage1, idImage2;
    private ImageView image1, image2;
    private int images[]={R.drawable.micro, R.drawable.sedan, R.drawable.hatchback, R.drawable.coupe, R.drawable.station_wagon,R.drawable.roadster, R.drawable.cabriolet,R.drawable.muscle_car, R.drawable.sport_car, R.drawable.suv, R.drawable.suv_compact, R.drawable.pickup, R.drawable.minivan, R.drawable.van};
    public static final String EXTRA_EMAIL = "android.intent.extra.EMAIL";
    private LinearLayout star1, star2;
    double c1 = 0;
    double c2 = 0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comparatie);

        initViews();//initializare
        preluareDate();//preluarea datelor din pagina anterioara
        if (email.equals(getResources().getString(R.string.standardEmail))){//daca se intra fara cont se va modifica meniul din bara de start
            barWithoutAccount();
        }
        else{
            barNavStart();
        }
        baraNavBottom();//meniul de jos
        Intrebari();//afisarea intrebarilor, preluarea raspunsurilor si prelucurarea rezultatelor
        clickBtnAfla();//redirectionarea catre pagina de sugestii modele
    }

    @Override
    public void initViews() {
        nav_view=(NavigationView)findViewById(R.id.nav_view);
        nume1=(TextView)findViewById(R.id.nume1);
        nume2=(TextView)findViewById(R.id.nume2);
        dl=(DrawerLayout)findViewById(R.id.dl);
        abdt= new ActionBarDrawerToggle(this,dl,R.string.Open,R.string.Close);
        user= new User();
        btnAfla=(Button)findViewById(R.id.btnAfla);
        bottomNav=findViewById(R.id.bottomNav);
        nrIntrebare=(TextView)findViewById(R.id.nrIntrebare);
        intrebare=(TextView)findViewById(R.id.intrebare);
        est=(ImageView)findViewById(R.id.est);
        txtProcent=(TextView)findViewById(R.id.txtProcent);
        txtProcent1=(TextView)findViewById(R.id.txtProcent1);
        txtProcent2=(TextView)findViewById(R.id.txtProcent2);
        txtAfter=(TextView)findViewById(R.id.txtAfter);
        Resources res = getResources();
        intrebari= res.getStringArray(R.array.intrebari);
        firma= res.getStringArray(R.array.firma);
        utilizare= res.getStringArray(R.array.utilizare);
        copii= res.getStringArray(R.array.copii);
        concediu= res.getStringArray(R.array.concediu);
        sex= res.getStringArray(R.array.sex);
        experienta= res.getStringArray(R.array.experienta);
        principala= res.getStringArray(R.array.principala);
        variante= new String[][] { firma, utilizare, copii, concediu, sex, experienta, principala};
        answers= new String[20];
        rg=(RadioGroup)findViewById(R.id.rg);
        image1=(ImageView)findViewById(R.id.image1);
        image2=(ImageView)findViewById(R.id.image2);
        star1=(LinearLayout)findViewById(R.id.star1);
        star2=(LinearLayout)findViewById(R.id.star2);
    }

    @Override
    public void preluareDate() {
        Intent intent=getIntent();
        carA=intent.getStringExtra("caroserie1");
        carB=intent.getStringExtra("caroserie2");
        email=intent.getStringExtra(EXTRA_EMAIL);
        idImage1=intent.getIntExtra("image1", 0);
        image1.setImageResource(images[idImage1]);
        idImage2=intent.getIntExtra("image2", 0);
        image2.setImageResource(images[idImage2]);
        user.setEmail(email);
        nume1.setText(carA);
        nume2.setText(carB);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
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
                    Intent intent = new Intent(Comparatie.this, Login.class);
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
                    Intent intent= new Intent(Comparatie.this, Cont.class);
                    intent.putExtra(EXTRA_EMAIL, email);
                    startActivity(intent);
                }
                else if(id==R.id.statistici){
                    Intent intent= new Intent(Comparatie.this, Statistici.class);
                    intent.putExtra(EXTRA_EMAIL, email);
                    startActivity(intent);
                }
                else if(id==R.id.myCars){
                    Intent intent= new Intent(Comparatie.this, MyCars.class);
                    intent.putExtra(EXTRA_EMAIL, email);
                    startActivity(intent);
                }
                else if(id==R.id.logout){
                    SharedPreferences preferences= getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor= preferences.edit();
                    editor.putString("remember", "false");
                    editor.apply();
                    Intent intent= new Intent(Comparatie.this, Login.class);
                    startActivity(intent);
                }
                return true;
            }
        });

    }//bara navigare start


    private final BottomNavigationView.OnNavigationItemSelectedListener navListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int id= item.getItemId();
                    if(id==R.id.home){
                        Intent intent= new Intent(Comparatie.this, Home.class);
                        intent.putExtra(EXTRA_EMAIL, email);
                        startActivity(intent);                    }
                    else if(id==R.id.statistici){
                        Intent intent=new Intent(Comparatie.this, GeneralStatistics.class);
                        intent.putExtra(EXTRA_EMAIL, email);
                        startActivity(intent);
                    }
                    else if(id==R.id.more){
                        Intent intent= new Intent(Comparatie.this, Help.class);
                        intent.putExtra(EXTRA_EMAIL, email);
                        startActivity(new Intent(intent));                    }
                    return true;

                }
            };
    public void baraNavBottom(){
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }//bara navigare bottom
    public void Intrebari(){
        nrIntrebare.setText("1/8");
        nr=1;
        rb= new RadioButton[10];
        rg.setOrientation(RadioGroup.VERTICAL);
        countIntrebare=0;
        intrebare.setText(intrebari[countIntrebare]);
        raspunsuri=variante[0];
        rb[0]= new RadioButton(Comparatie.this);
        rb[0].setText(raspunsuri[0]);
        rb[1]= new RadioButton(Comparatie.this);
        rb[1].setText(raspunsuri[1]);
        for(int m=0;m<2;m++){
            rb[m].setTextColor(this.getResources().getColor(R.color.black));
        }
        rg.addView(rb[0]);
        rg.addView(rb[1]);
        rb[0].setChecked(true);
        countVarianta=1;
        raspunsuri=variante[countVarianta];
        est.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    int count=1;
                    btnId=rg.getCheckedRadioButtonId();
                    radioBtn=findViewById(btnId);
                    answers[countIntrebare]= radioBtn.getText().toString();
                    star(countIntrebare);
                    if(nr<8){
                        nr++;
                    }
                    String nrIntrebareTxt=nr+"/8";
                    nrIntrebare.setText(nrIntrebareTxt);
                    rg.removeAllViews();
                    countIntrebare = countIntrebare + 1;
                    String x=intrebari[countIntrebare];
                    intrebare.setText(x);
                    for(int j=0;j<raspunsuri.length;j++){
                        rb[j]  = new RadioButton(Comparatie.this);
                        rb[j].setTextColor(Comparatie.this.getResources().getColor(R.color.black));
                        rb[j].setText(raspunsuri[j]);
                        rb[j].setId(j);
                        rg.addView(rb[j]);
                    }
                    rb[count].setChecked(true);
                }catch(Exception e){
                    star(countIntrebare);
                    rg.removeAllViews();
                    intrebare.setVisibility(View.INVISIBLE);
                    prelucrareRez();
                }

                countVarianta=countVarianta+1;
                if(countVarianta<variante.length){
                    raspunsuri=variante[countVarianta];
                }
            }
        });
    }//afisare intrebari si raspunsuri

    public void star(int intrebare){
        int cvLayout= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, this.getResources().getDisplayMetrics());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(cvLayout, cvLayout);
        int cvMargin= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, this.getResources().getDisplayMetrics());
        int cvMargin2= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, this.getResources().getDisplayMetrics());
        layoutParams.setMargins(cvMargin2, cvMargin2, cvMargin, cvMargin2);
        double i=14.28;
        DatabaseAccess databaseAccess=DatabaseAccess.getInstance(getApplicationContext());
        databaseAccess.open();
        ArrayList car1;
        car1 = databaseAccess.getCaroserie(nume1.getText().toString());
        ArrayList car2;
        car2 = databaseAccess.getCaroserie(nume2.getText().toString());
        switch (intrebare)
        {
            case 0:
                String firmaA = answers[intrebare];
                int firmaN = 0;
                if(firmaA.equals(firma[0])){
                    firmaN =1;
                }
                ImageView imv= new ImageView(this);
                imv.setImageResource(R.drawable.i2);
                imv.setLayoutParams(layoutParams);
                ImageView imv2= new ImageView(this);
                imv2.setImageResource(R.drawable.i2);
                imv2.setLayoutParams(layoutParams);
                //compararea valorii alese de utilizator cu valoarea specifica caroseriilor pentru campul FIRMA
                if((firmaN == (int) car1.get(0)) && (firmaN == (int) car2.get(0))){
                    star1.addView(imv);
                    star2.addView(imv2);
                    c1=c1+i;
                    c2=c2+i;
                }else if(firmaN ==(int) car2.get(0)){
                    star2.addView(imv2);
                    c2=c2+i;
                } else if(firmaN == (int) car1.get(0)){
                    star1.addView(imv);
                    c1=c1+i;
                }
                break;
            case 1:
                String utilizareA = answers[intrebare];
                //legare cu denumirile din baza de date ale tabelului UTILIZARE
                if(utilizareA.equals(utilizare[0])){
                    utilizareA =databaseAccess.getDenumireUtilizare("UB");
                }else if(utilizareA.equals(utilizare[1])){
                    utilizareA =databaseAccess.getDenumireUtilizare("EXUB");
                }else{
                    utilizareA =databaseAccess.getDenumireUtilizare("MX");
                }
                ImageView imvU= new ImageView(this);
                imvU.setImageResource(R.drawable.i7);
                imvU.setLayoutParams(layoutParams);
                ImageView imvU2= new ImageView(this);
                imvU2.setImageResource(R.drawable.i7);
                imvU2.setLayoutParams(layoutParams);
                if((utilizareA.equals(databaseAccess.getDenumireUtilizare(car1.get(1).toString())) || car1.get(1).equals("MX")) && (utilizareA.equals(databaseAccess.getDenumireUtilizare(car2.get(1).toString())) || car2.get(1).equals("MX"))){
                    star1.addView(imvU);
                    star2.addView(imvU2);
                    c1=c1+i;
                    c2=c2+i;
                }else if(utilizareA.equals(databaseAccess.getDenumireUtilizare(car2.get(1).toString())) || car2.get(1).equals("MX")){
                    star2.addView(imvU2);
                    c2=c2+i;
                }else if(utilizareA.equals(databaseAccess.getDenumireUtilizare(car1.get(1).toString())) || car1.get(1).equals("MX")){
                    star1.addView(imvU);
                    c1=c1+i;
                }
                break;
            case 2:
                String familieA = answers[intrebare];
                //legare cu denumirile din baza de date ale tabelului COPII
                if(familieA.equals(copii[0])){
                    familieA =databaseAccess.getCopii(1);
                }else if (familieA.equals(copii[1])){
                    familieA =databaseAccess.getCopii(2);
                }else if (familieA.equals(copii[2])){
                    familieA =databaseAccess.getCopii(3);
                }
                ImageView imvF= new ImageView(this);
                imvF.setImageResource(R.drawable.i5);
                imvF.setLayoutParams(layoutParams);
                ImageView imvF2= new ImageView(this);
                imvF2.setImageResource(R.drawable.i5);
                imvF2.setLayoutParams(layoutParams);
                //compararea valorii alese de utilizator cu valoarea specifica caroseriilor pentru tabelul COPII
                if((familieA.equals(databaseAccess.getCopii(Integer.parseInt(car1.get(2).toString()))))&&(familieA.equals(databaseAccess.getCopii(Integer.parseInt(car2.get(2).toString()))))){
                    star1.addView(imvF);
                    star2.addView(imvF2);
                    c1=c1+i;
                    c2=c2+i;
                }else if(familieA.equals(databaseAccess.getCopii(Integer.parseInt(car2.get(2).toString())))){
                    star2.addView(imvF2);
                    c2=c2+i;
                }else if(familieA.equals(databaseAccess.getCopii(Integer.parseInt(car1.get(2).toString())))){
                    star1.addView(imvF);
                    c1=c1+i;
                }
                break;
            case 3:
                String concediuA = answers[intrebare];
                int conc = 0;
                //legare cu valoarea numerica pentru a corespunde cu valorile din baza de date din campul CONCEDIU
                if(concediuA.equals(concediu[0])){
                    conc =1;
                }
                ImageView imvC= new ImageView(this);
                imvC.setImageResource(R.drawable.i1);
                imvC.setLayoutParams(layoutParams);
                ImageView imvC2= new ImageView(this);
                imvC2.setImageResource(R.drawable.i1);
                imvC2.setLayoutParams(layoutParams);
                //compararea valorii alese de utilizator cu valoarea specifica caroseriilor pentru campul CONCEDIU
                if((conc ==(int) car1.get(3)) && (conc ==(int) car2.get(3))){
                    star1.addView(imvC);
                    star2.addView(imvC2);
                    c1=c1+i;
                    c2=c2+i;
                }else if (conc ==(int) car2.get(3)){
                    star2.addView(imvC2);
                    c2=c2+i;
                }else if(conc ==(int) car1.get(3)){
                    star1.addView(imvC);
                    c1=c1+i;
                }
                break;
            case 4:
                String sexA = answers[intrebare];
                //legare cu denumirile din baza de date ale tabelului SEX
                if(sexA.equals(sex[0])){
                    sexA =databaseAccess.getDenumireSex("F");
                }else if (sexA.equals(sex[1])){
                    sexA =databaseAccess.getDenumireSex("M");
                }else{
                    sexA =databaseAccess.getDenumireSex("MX");
                }
                ImageView imvSX= new ImageView(this);
                imvSX.setImageResource(R.drawable.i4);
                imvSX.setLayoutParams(layoutParams);
                ImageView imvSX2= new ImageView(this);
                imvSX2.setImageResource(R.drawable.i4);
                imvSX2.setLayoutParams(layoutParams);
                //compararea valorii alese de utilizator cu valoarea specifica caroseriilor pentru tabelul SEX
                if(((sexA.equals(databaseAccess.getDenumireSex(car1.get(4).toString())) || car1.get(4).equals("mixt")) && (sexA.equals(databaseAccess.getDenumireSex(car2.get(4).toString())) || car2.get(4).equals("mixt")))){
                    star1.addView(imvSX);
                    star2.addView(imvSX2);
                    c1=c1+i;
                    c2=c2+i;
                }else if(sexA.equals(databaseAccess.getDenumireSex(car2.get(4).toString())) || car2.get(4).equals("mixt")){
                    star2.addView(imvSX2);
                    c2=c2+i;
                }else if((sexA.equals(databaseAccess.getDenumireSex(car1.get(4).toString()))) || car1.get(4).equals("mixt")){
                    star1.addView(imvSX);
                    c1=c1+i;
                }
                break;
            case 5:
                String experientaA = answers[intrebare];
                //legare cu denumirile din baza de date ale tabelului EXPERIENTA
                if (experientaA.equals(experienta[0])){
                    experientaA =databaseAccess.getDenumireExperienta("icr");
                }else if(experientaA.equals(experienta[1])){
                    experientaA =databaseAccess.getDenumireExperienta("mdu");
                }else{
                    experientaA =databaseAccess.getDenumireExperienta("avt");
                }
                ImageView imvE= new ImageView(this);
                imvE.setImageResource(R.drawable.i3);
                imvE.setLayoutParams(layoutParams);
                ImageView imvE2= new ImageView(this);
                imvE2.setImageResource(R.drawable.i3);
                imvE2.setLayoutParams(layoutParams);
                //compararea valorii alese de utilizator cu valoarea specifica caroseriilor pentru tabelul EXPERIENTA
                if((experientaA.equals(databaseAccess.getDenumireExperienta(car1.get(5).toString())) || car1.get(5).equals("t")) && (experientaA.equals(databaseAccess.getDenumireExperienta(car2.get(5).toString())) || car2.get(5).equals("t"))){
                    star1.addView(imvE);
                    star2.addView(imvE2);
                    c1=c1+i;
                    c2=c2+i;
                }else if(experientaA.equals(databaseAccess.getDenumireExperienta(car1.get(5).toString())) || car1.get(5).equals("t")){
                    star1.addView(imvE);
                    c1=c1+i;
                }else if(experientaA.equals(databaseAccess.getDenumireExperienta(car2.get(5).toString())) || car2.get(5)=="t"){
                    star2.addView(imvE2);
                    c2=c2+i;
                }
                //compararea valorii alese de utilizator cu valoarea specifica caroseriilor pentru tabelul EXPERIENTA
                if((experientaA.equals("incepator") || experientaA.equals("mediu")) && car1.get(5).equals("im")){
                    star1.addView(imvE);
                    c1=c1+i;
                }
                if((experientaA.equals("incepator") || experientaA.equals("mediu")) && car2.get(5).equals("im")){
                    star2.addView(imvE2);
                    c2=c2+i;
                }

                if((experientaA.equals("mediu") || experientaA.equals("avansat")) && car1.get(5).equals("ma")){
                    star1.addView(imvE);
                    c1=c1+i;
                }
                if((experientaA.equals("mediu") || experientaA.equals("avansat")) && car2.get(5).equals("ma")){
                    star2.addView(imvE2);
                    c2=c2+i;
                }
                break;
            case 6:
                String principalaA = answers[intrebare];
                int princ = 0;
                //legare cu valoarea numerica pentru a corespunde cu valorile din baza de date din campul PRINCIPALA
                if(principalaA.equals(principala[0])){
                    princ =1;
                }
                ImageView imvP= new ImageView(this);
                imvP.setImageResource(R.drawable.emoji_happy);
                imvP.setLayoutParams(layoutParams);
                ImageView imvP2= new ImageView(this);
                imvP2.setImageResource(R.drawable.emoji_happy);
                imvP2.setLayoutParams(layoutParams);
                //compararea valorii alese de utilizator cu valoarea specifica caroseriilor pentru campul PRINCIPALA
                if(princ == (int) car1.get(6) && princ == (int) car2.get(6)){
                    star1.addView(imvP);
                    star2.addView(imvP2);
                    c1=c1+i;
                    c2=c2+i;
                }else if(princ == (int) car1.get(6)){
                    star1.addView(imvP);
                    c1=c1+i;
                }else if(princ == (int) car2.get(6)){
                    star2.addView(imvP2);
                    c2=c2+i;
                }
                break;
        }
    }
    public void prelucrareRez(){
        DatabaseAccess databaseAccess=DatabaseAccess.getInstance(getApplicationContext());
        databaseAccess.open();
        Formatter formatter = new Formatter();
        formatter.format("%.2f", c1);
        Formatter formatter2= new Formatter();
        formatter2.format("%.2f", c2);
        txtProcent.setText(getResources().getString(R.string.procente));
        String txtCarA=carA+": "+ formatter.toString() +"%";
        String txtCarB=carB+": "+ formatter2.toString() +"%";
        txtProcent1.setText(txtCarA);
        txtProcent2.setText(txtCarB);
        est.setVisibility(View.INVISIBLE);
        txtAfter.setVisibility(View.VISIBLE);
        btnAfla.setVisibility(View.VISIBLE);

        user= databaseAccess.getAllUser(user.getEmail());
        if (user.getEmail().equals(getResources().getString(R.string.standardEmail))) {
            databaseAccess.addStatistici(user,c1, databaseAccess.getCaroserieId(carA));
            databaseAccess.addStatistici(user,c2, databaseAccess.getCaroserieId(carB));
        }
        else{
            if(databaseAccess.verificaStatistici(user.getId(), databaseAccess.getCaroserieId(carA))==0){
                databaseAccess.addStatistici(user, c1, databaseAccess.getCaroserieId(carA) );
            }
            else{
                databaseAccess.updateStatistici(user, databaseAccess.getCaroserieId(carA), c1);
            }
            if(databaseAccess.verificaStatistici(user.getId(), databaseAccess.getCaroserieId(carB))==0){
                databaseAccess.addStatistici(user, c2, databaseAccess.getCaroserieId(carB) );
            }
            else{
                databaseAccess.updateStatistici(user, databaseAccess.getCaroserieId(carB), c2);
            }
        }
    }
    public void clickBtnAfla(){
        btnAfla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Comparatie.this, Masina.class);
                intent.putExtra(intent.EXTRA_EMAIL, email);
                startActivity(intent);
            }
        });
    }

}