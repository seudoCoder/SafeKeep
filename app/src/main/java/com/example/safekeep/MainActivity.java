package com.example.safekeep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton addNotebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         addNotebtn=findViewById(R.id.add_note_btn);
         addNotebtn.setOnClickListener((v)->startActivity(new Intent(MainActivity.this,NoteDetailActivity.class)));

    }
}