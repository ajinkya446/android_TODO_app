package com.example.notes.interfaces;

import android.content.Context;

import com.example.notes.adapter.NoteAdapter;

import java.util.ArrayList;

public interface DBInterface {

    NoteAdapter insertNote(Context ctx, ArrayList<String> title, ArrayList<String> color, ArrayList<String> creationDate);
}
