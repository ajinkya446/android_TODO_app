package com.example.notes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.adapter.NoteAdapter;
import com.example.notes.singleton.Singleton;
import com.example.notes.storage.Database;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    Database db;
    ArrayList<String> color, title, creationTime;
    EditText searchBar;

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
        searchBar = findViewById(R.id.searchBar);
        singleton.setTextView(textView);
        singleton.setSearchBar(searchBar);
        Cursor cursor = db.getAllValues();
        singleton.setMainContext(MainActivity.this);


        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No Data available", Toast.LENGTH_SHORT).show();
        } else {
            ArrayList<String> arrayList=new ArrayList<>();
            while (cursor.moveToNext()) {
                arrayList.add(cursor.getString(1));
                title.add(cursor.getString(1));
                creationTime.add(cursor.getString(2).substring(0, cursor.getString(2).length() - 4));
                color.add(cursor.getString(3));
            }
            singleton.setTitleTempArray(arrayList);
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
//        ArrayList<String> tempList;
        if (Singleton.titleTempArray.isEmpty()) {
            while (cursor.moveToNext()) {
                Singleton.titleTempArray.add(cursor.getString(1));
            }
        }

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    NoteAdapter adapter = new NoteAdapter(MainActivity.this, color, Singleton.titleTempArray, creationTime);
                    Singleton.recyclerView.setAdapter(adapter);
                    Singleton.recyclerView.setLayoutManager(new LinearLayoutManager(Singleton.mainContext));
                    Singleton.recyclerView.setVisibility(cursor.getCount() == 0 ? View.GONE : View.VISIBLE);
                    Singleton.recyclerView.setNestedScrollingEnabled(true);
                    adapter.notifyDataSetChanged();
                } else {
                    NoteAdapter adapter = new NoteAdapter(MainActivity.this, color, title, creationTime);
                    adapter.getFilter().filter(s.toString());
                }

            }
        });
    }
}