package com.example.notes;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.notes.adapter.NoteAdapter;
import com.example.notes.singleton.Singleton;
import com.example.notes.storage.Database;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class AddNoteActivity extends AppCompatActivity {
    EditText titleEditText;
    Button button;
    Database db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        db = new Database(this);
        SQLiteDatabase dbInfo = db.getReadableDatabase();
        titleEditText = findViewById(R.id.title);
        button = findViewById(R.id.submitButton);
        button.setOnClickListener(v -> {
            if (titleEditText.getText().toString().isEmpty()) {
                View parentLayout = findViewById(android.R.id.content);
                Snackbar snackbar = Snackbar.make(parentLayout, "Please Enter Title", Snackbar.LENGTH_LONG);
                snackbar.getView().setBackgroundColor(Color.parseColor("#1eb2a6"));
                snackbar.setTextColor(Color.parseColor("#FFFFFF"))
                        .show();
            } else {
                ArrayList<String> color, title, creationTime;
                color = new ArrayList<String>();
                title = new ArrayList<String>();
                creationTime = new ArrayList<String>();
                Boolean status = db.insertNote(titleEditText.getText().toString());
                if (status) {
                    Toast.makeText(this, "Notes added successfully", Toast.LENGTH_SHORT).show();
                    titleEditText.setText("");

                    Cursor cursor = db.getAllValues();
                    if (cursor.getCount() == 0) {
                        Toast.makeText(this, "No Data available", Toast.LENGTH_SHORT).show();
                    } else {
                        while (cursor.moveToNext()) {
                            title.add(cursor.getString(1));
                            creationTime.add(cursor.getString(2).substring(0, cursor.getString(2).length() - 4));
                            color.add(cursor.getString(3));
                        }
                        Log.d("Log", title.toString());
                        NoteAdapter noteAdapter = db.insertNote(Singleton.mainContext, title, color, creationTime);
                        Singleton.recyclerView.setAdapter(noteAdapter);
                        Singleton.recyclerView.setLayoutManager(new LinearLayoutManager(Singleton.mainContext));
                        Singleton.recyclerView.setVisibility(cursor.getCount() == 0 ? View.GONE : View.VISIBLE);
                        Singleton.recyclerView.setNestedScrollingEnabled(true);
                    }

                    Singleton.detailsText.setVisibility(cursor.getCount() == 0 ? View.VISIBLE : View.GONE);
                    Singleton.searchBar.setVisibility(cursor.getCount() >= 0 ? View.VISIBLE : View.GONE);
                } else {
                    View parentLayout = findViewById(android.R.id.content);
                    Snackbar snackbar = Snackbar.make(parentLayout, "Error Occurred", Snackbar.LENGTH_LONG);
                    snackbar.getView().setBackgroundColor(Color.parseColor("#1eb2a6"));
                    snackbar.setTextColor(Color.parseColor("#FFFFFF"))
                            .show();
                    titleEditText.setText("");
                }
            }
        });

    }
}