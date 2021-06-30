package com.example.licenta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
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
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import com.example.licenta.helpers.DatabaseAccess;
import com.example.licenta.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;


import java.util.ArrayList;
import java.util.List;

public class Masina extends AppCompatActivity implements interfata{
    private ListView lstCaroserii;
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private BottomNavigationView bottomNav;
    private RelativeLayout rel1, relLayout, relLayout1, sec;
    private CardView rel2, cardView, not;
    private String email, caroserie;
    private ImageView est;
    private int countIntrebare,countVarianta, btnId;
    private TextView  intrebare;
    private Resources res;
    private String[] intrebari;
    private String[]  bugetAchizitie, combustibil;
    private String[][] variante ;
    private String raspunsuri[];
    private RadioGroup rg;
    private RadioButton[] rb ;
    private RadioButton radioBtn;
    private String[] answers;
    private User user;
    private NavigationView nav_view;
    private GridLayout grid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.masina);
        initViews();//initializari
        preluareDate();//preluare date din pagina anterioara
        if (email.equals(getResources().getString(R.string.standardEmail))){//daca se intra fara cont se va modifica meniul din bara de start
            barWithoutAccount();
        }
        else{
            barNavStart();
        }
        baraNavBottom();//bara de jos
        afisareCaroserii();//afisarea meniului de caroserii
        selectareCaroserie();//depozitarea caroseriei si apelarea functiei Intrebari care afiseaza restul intrebarilor, aceasta la randul ei apeland functia de prelucrare a rezultatelor
    }

    @Override
    public void initViews() {
        nav_view=(NavigationView)findViewById(R.id.nav_view);
        res=getResources();
        intrebari= res.getStringArray(R.array.intrebariMasini);
        bugetAchizitie=res.getStringArray(R.array.bugetAchizitie);
        combustibil=res.getStringArray(R.array.combustibil);
        variante=new String[][] {bugetAchizitie, combustibil};
        answers= new String[20];
        dl=(DrawerLayout)findViewById(R.id.dl);
        abdt= new ActionBarDrawerToggle(this,dl,R.string.Open,R.string.Close);
        user= new User();
        bottomNav=findViewById(R.id.bottomNav);
        lstCaroserii=(ListView)findViewById(R.id.lstCaroserii);
        rel2=findViewById(R.id.rel2);
        rel1=(RelativeLayout)findViewById(R.id.rel1);
        intrebare=(TextView)findViewById(R.id.intrebare);
        est=(ImageView)findViewById(R.id.est);
        rg=(RadioGroup)findViewById(R.id.rg);
        relLayout=findViewById(R.id.relLayout);
        relLayout1=findViewById(R.id.relLayout1);
        sec=findViewById(R.id.sec);
        grid= findViewById(R.id.grid);
        not=(CardView) findViewById(R.id.not);
    }

    @Override
    public void preluareDate() {
        Intent intent=getIntent();
        email=intent.getStringExtra(intent.EXTRA_EMAIL);
        user.setEmail(email);
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
                    Intent intent = new Intent(Masina.this, Login.class);
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
                    Intent intent= new Intent(Masina.this, Cont.class);
                    intent.putExtra(intent.EXTRA_EMAIL, email);
                    startActivity(intent);
                }
                else if(id==R.id.statistici){
                    Intent intent= new Intent(Masina.this, Statistici.class);
                    intent.putExtra(intent.EXTRA_EMAIL, email);
                    startActivity(intent);
                }
                else if(id==R.id.myCars){
                    Intent intent= new Intent(Masina.this, MyCars.class);
                    intent.putExtra(intent.EXTRA_EMAIL, email);
                    startActivity(intent);
                }
                else if(id==R.id.logout){
                    SharedPreferences preferences= getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor= preferences.edit();
                    editor.putString("remember", "false");
                    editor.apply();
                    Intent intent= new Intent(Masina.this, Login.class);
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
                        Intent intent= new Intent(Masina.this, Home.class);
                        intent.putExtra(intent.EXTRA_EMAIL, email);
                        startActivity(intent);
                    }
                    else if(id==R.id.statistici){
                        Intent intent= new Intent(Masina.this, GeneralStatistics.class);
                        intent.putExtra(intent.EXTRA_EMAIL, email);
                        startActivity(new Intent(intent));
                    }
                    else if(id==R.id.more){
                        Intent intent= new Intent(Masina.this, Help.class);
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
    private void afisareCaroserii(){
        DatabaseAccess databaseAccess=DatabaseAccess.getInstance(getApplicationContext());
        databaseAccess.open();
        List<String> s=databaseAccess.getCaroserii();
        s.remove(7);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.m,R.id.textView, s);
        lstCaroserii.setAdapter(arrayAdapter);
        databaseAccess.close();
    }
    private void selectareCaroserie(){
        lstCaroserii.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                caroserie=lstCaroserii.getItemAtPosition(position).toString();
                Intrebari();
            }
        });
    }
    private void Intrebari(){
        rel2.setVisibility(View.INVISIBLE);
        rel1.setVisibility(View.VISIBLE);
        rb= new RadioButton[20];
        rg.setOrientation(RadioGroup.VERTICAL);
        countIntrebare=0;
        intrebare.setText(intrebari[countIntrebare]);
        raspunsuri=variante[0];
        for(int l=0;l<raspunsuri.length;l++){
            rb[l]= new RadioButton(Masina.this);
            rb[l].setText(raspunsuri[l]);
            rb[l].setTextColor(this.getResources().getColor(R.color.black));
            rg.addView(rb[l]);
        }
        countVarianta=1;
        raspunsuri=variante[countVarianta];
        est.setOnClickListener(new View.OnClickListener() {
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
                        rb[j]  = new RadioButton(Masina.this);
                        rb[j].setTextColor(Masina.this.getResources().getColor(R.color.black));
                        rb[j].setText(raspunsuri[j]);
                        rb[j].setId(j);
                        rg.addView(rb[j]);
                        count=j;
                    }
                    rb[count].setChecked(true);
                }catch(Exception e){
                    rg.removeAllViews();
                    prelucrareRez();
                }

                countVarianta=countVarianta+1;
                if(countVarianta<variante.length){
                    raspunsuri=variante[countVarianta];
                }
            }
        });
    }
    private void prelucrareRez(){
        int j=0;
        String combustibilA=answers[j+1];
        String bugetA=answers[j];

        DatabaseAccess databaseAccess=DatabaseAccess.getInstance(getApplicationContext());
        databaseAccess.open();

        if(bugetA.equals(bugetAchizitie[0])){
            bugetA =databaseAccess.getDenumireBuget("<5K");
        }else if(bugetA.equals(bugetAchizitie[1])){
            bugetA =databaseAccess.getDenumireBuget("<10K");
        }else if(bugetA.equals(bugetAchizitie[2])){
            bugetA =databaseAccess.getDenumireBuget(">10K");
        }else{
            bugetA=databaseAccess.getDenumireBuget(">");
        }
        if(combustibilA.equals(combustibil[0])){
            combustibilA =databaseAccess.getDenumireCombustibil("bzn");
        }else if(combustibilA.equals(combustibil[1])){
            combustibilA =databaseAccess.getDenumireCombustibil("dsl");
        }else if(combustibilA.equals(combustibil[2])){
            combustibilA =databaseAccess.getDenumireCombustibil("elc");
        }else{
            combustibilA=databaseAccess.getDenumireCombustibil("hbd");
        }


        grid.removeAllViews();
        ArrayList modeleSugerate= databaseAccess.getModeletot(caroserie, bugetA, combustibilA);
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
        if(!modeleSugerate.isEmpty()) {
            for (Object object : modeleSugerate) {
                detalii = databaseAccess.getModeleDetalii((int) object);
                cardView = new CardView(this);
                cardView.setRadius(cvRadius);
                cardView.setCardElevation(cvElevation);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(cvDim1, cvDim, 1.0f);
                layoutParams.setMargins(cvMargin, cvMargin, cvMargin, cvMargin);
                cardView.setLayoutParams(layoutParams);
                grid.addView(cardView);
                LinearLayout ly = new LinearLayout(this);
                ly.setLayoutParams(new LinearLayout.LayoutParams(cvMarginTop, LinearLayout.LayoutParams.MATCH_PARENT));
                ly.setOrientation(LinearLayout.HORIZONTAL);
                ly.setVerticalGravity(Gravity.CENTER_VERTICAL);
                cardView.addView(ly);
                LinearLayout ly1 = new LinearLayout(this);
                ly1.setLayoutParams(new LinearLayout.LayoutParams(cvMarginTop2, LinearLayout.LayoutParams.MATCH_PARENT));
                ly1.setOrientation(LinearLayout.HORIZONTAL);
                ly1.setVerticalGravity(Gravity.CENTER_VERTICAL);
                ly1.setPaddingRelative(paddingLeft, paddingTop, paddingRight, paddingBottom);
                ly.addView(ly1);
                LinearLayout ly2 = new LinearLayout(this);
                ly2.setLayoutParams(new LinearLayout.LayoutParams(cvMarginTop1, LinearLayout.LayoutParams.MATCH_PARENT));
                ly2.setOrientation(LinearLayout.VERTICAL);
                ly2.setVerticalGravity(Gravity.CENTER_HORIZONTAL);
                ly2.setPaddingRelative(paddingLeft1, paddingTop, paddingRight, paddingBottom);
                ly.addView(ly2);
                ImageView iv = new ImageView(this);
                iv.setLayoutParams(new LinearLayout.LayoutParams(cvLayout2, cvLayout));
                iv.setImageResource(getResources().getIdentifier(detalii.get(1).toString(), "drawable", getPackageName()));
                ly1.addView(iv);
                TextView tv = new TextView(this);
                LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams1.topMargin = tvMarginTop;
                tv.setLayoutParams(layoutParams1);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                tv.setTextColor(Masina.this.getResources().getColor(R.color.black));
                tv.setText(detalii.get(0).toString());
                ly2.addView(tv);
                Button bt = new Button(Masina.this);
                bt.setText(getResources().getString(R.string.details));
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams2.topMargin = tvMarginTop1;
                bt.setLayoutParams(layoutParams2);
                bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Masina.this, DetaliiMasini.class);
                        intent.putExtra(intent.EXTRA_EMAIL, email);
                        intent.putExtra(intent.EXTRA_TEXT, (int) object);
                        startActivity(intent);
                    }
                });
                ly2.addView(bt);
            }
            relLayout.removeView(relLayout1);
            rel1.setVisibility(View.INVISIBLE);
            est.setVisibility(View.INVISIBLE);
        }
        else if(modeleSugerate.isEmpty()){
            not.setVisibility(View.VISIBLE);
            relLayout.removeView(relLayout1);
            rel1.setVisibility(View.INVISIBLE);
            est.setVisibility(View.INVISIBLE);
        }
        databaseAccess.close();
    }
}
