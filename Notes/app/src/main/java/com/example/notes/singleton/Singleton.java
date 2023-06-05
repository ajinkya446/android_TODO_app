package com.example.notes.singleton;

import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class Singleton {
    // private static instance variable to hold the singleton instance
    private static volatile Singleton INSTANCE = null;

    public static Context mainContext;

    public static RecyclerView recyclerView;
    public static TextView detailsText;
    public static EditText searchBar;


    public void setMainContext(final Context context) {
        mainContext = context;
    }

    public void setSearchBar(EditText view) {
        searchBar = view;
    }

    public void setRecyclerView(RecyclerView view) {
        recyclerView = view;
    }

    public void setTextView(TextView textView) {
        detailsText = textView;
    }

    // private constructor to prevent instantiation of the class
    public Singleton() {
    }

    // public static method to retrieve the singleton instance
    public static Singleton getInstance() {
        // Check if the instance is already created
        if (INSTANCE == null) {
            // synchronize the block to ensure only one thread can execute at a time
            synchronized (Singleton.class) {
                // check again if the instance is already created
                if (INSTANCE == null) {
                    // create the singleton instance
                    INSTANCE = new Singleton();
                }
            }
        }
        // return the singleton instance
        return INSTANCE;
    }
}
