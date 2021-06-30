package com.example.licenta;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EntryPage extends AppCompatActivity  {
    RelativeLayout relL;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry_page);
        final FloatingActionButton button = findViewById(R.id.fab);
        relL=findViewById(R.id.relL);
        button.setOnClickListener(new View.OnClickListener() {//navigare catre entry page prin click pe ecran
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EntryPage.this, Login.class);
                startActivity(intent);
            }
        });
    }
}
