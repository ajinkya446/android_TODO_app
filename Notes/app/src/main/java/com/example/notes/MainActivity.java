package com.example.notes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.singleton.Singleton;
import com.example.notes.storage.Database;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    Database db;
    ArrayList<String> color, title, creationTime;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new Database(this);
        color = new ArrayList<String>();
        title = new ArrayList<String>();
        creationTime = new ArrayList<String>();
        SQLiteDatabase dbInfo = db.getReadableDatabase();
        recyclerView = findViewById(R.id.recyclerView);
        Singleton singleton = new Singleton();
        singleton.setRecyclerView(recyclerView);
        TextView textView = findViewById(R.id.details);
        EditText searchBar = findViewById(R.id.searchBar);
        singleton.setTextView(textView);
        singleton.setSearchBar(searchBar);
        Cursor cursor = db.getAllValues();
        singleton.setMainContext(MainActivity.this);
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No Data available", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                title.add(cursor.getString(1));
                creationTime.add(cursor.getString(2).substring(0, cursor.getString(2).length() - 4));
                color.add(cursor.getString(3));
            }
            Log.d("Log", title.toString());
            singleton.recyclerView.setAdapter(db.insertNote(MainActivity.this, title, color, creationTime));
            singleton.recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        }

        Singleton.recyclerView.setVisibility(cursor.getCount() == 0 ? View.GONE : View.VISIBLE);
        Singleton.recyclerView.setNestedScrollingEnabled(true);
        singleton.detailsText.setVisibility(cursor.getCount() == 0 ? View.VISIBLE : View.GONE);
        singleton.searchBar.setVisibility(cursor.getCount() >= 10 ? View.VISIBLE : View.GONE);
        floatingActionButton = findViewById(R.id.fab);

        floatingActionButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AddNoteActivity.class);
            startActivity(intent);
        });
    }

}