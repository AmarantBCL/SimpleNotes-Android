package com.example.android.notes;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private NotesDatabase database;

    public MainViewModel(@NonNull Application application) {
        super(application);
        database = NotesDatabase.newInstance(application);
    }

    public LiveData<List<Note>> getNotes() {
        return database.notesDAO().getAllNotes();
    }

    public void removeNote(Note note) {
        new Thread(() -> database.notesDAO().deleteNote(note)).start();
    }
}
