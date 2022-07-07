package com.example.android.notes;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final List<Note> notes = new ArrayList<>();

    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    private NotesAdapter adapter;
    private NotesDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        database = NotesDatabase.newInstance(this);
        getData();
        adapter = new NotesAdapter(notes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        fab.setOnClickListener(v -> {
            startActivity(AddNoteActivity.newIntent(MainActivity.this));
        });
        adapter.setOnItemClickListener(new NotesAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(MainActivity.this, "Click!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(int position) {
                removeNote(position);
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                removeNote(viewHolder.getAdapterPosition());
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view);
        fab = findViewById(R.id.fab);
    }

    private void removeNote(int position) {
        Note note = notes.get(position);
        database.notesDAO().deleteNote(note);
        getData();
        adapter.notifyDataSetChanged();
    }

    private void getData() {
        List<Note> notesFromDB = database.notesDAO().getAllNotes();
        notes.clear();
        notes.addAll(notesFromDB);
    }
}