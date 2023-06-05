package com.example.notes.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.R;
import com.example.notes.singleton.Singleton;
import com.example.notes.storage.Database;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private final ArrayList<String> color;
    private final ArrayList<String> title;
    private final ArrayList<String> creationTime;
    private final Context context;


    public NoteAdapter(Context context, ArrayList<String> color, ArrayList<String> title, ArrayList<String> creationTime) {
        this.color = color;
        this.creationTime = creationTime;
        this.title = title;
        this.context = context;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        @SuppressLint("ResourceType") View view = inflater.inflate(R.layout.note_item_layout, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.title.setText(String.valueOf(title.get(position)));
        holder.createTime.setText(String.valueOf(creationTime.get(position)));
        String value = String.valueOf(color.get(position));
        String substring = value.substring(1, value.length());
        if (substring.length() > 6) {
            value = '#' + substring.substring(0, 6);
        } else if (substring.length() <= 6) {
            value = '#' + substring;

        }

        GradientDrawable drawable = (GradientDrawable) holder.layout.getBackground().mutate();
        drawable.setColor(Color.parseColor(value));
        holder.deleteButton.setOnClickListener(v -> {
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.alert_dialog_layout);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            Button buttonClose = dialog.findViewById(R.id.buttonClose);
            Boolean status = holder.db.deleteNote(String.valueOf(title.get(position)));
            buttonClose.setOnClickListener(v1 -> {
                if (status) {
                    Toast.makeText(context, "Deleted Note item", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "something went wrong", Toast.LENGTH_SHORT).show();
                }

                Cursor cursor = holder.db.getAllValues();
                Singleton.detailsText.setVisibility(cursor.getCount() == 0 ? View.VISIBLE : View.GONE);
                Singleton.searchBar.setVisibility(cursor.getCount() >= 0 ? View.VISIBLE : View.GONE);
                Singleton.recyclerView.setAdapter(holder.db.insertNote(Singleton.mainContext, title, color, creationTime));
                Singleton.recyclerView.setLayoutManager(new LinearLayoutManager(Singleton.mainContext));
                Singleton.recyclerView.setVisibility(cursor.getCount() == 0 ? View.GONE : View.VISIBLE);
                Singleton.recyclerView.setNestedScrollingEnabled(true);
                color.remove(position);
                title.remove(position);
                creationTime.remove(position);
                notifyDataSetChanged();
                dialog.dismiss();

            });
            dialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return title.size();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView title, createTime;
        LinearLayout layout;
        ImageButton deleteButton;
        Database db;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            db = new Database(itemView.getContext());
            SQLiteDatabase dbInfo = db.getReadableDatabase();

            title = itemView.findViewById(R.id.noteTitle);
            createTime = itemView.findViewById(R.id.createTime);
            layout = itemView.findViewById(R.id.item_notes);
            deleteButton = itemView.findViewById(R.id.deleteButton);

        }
    }
}
