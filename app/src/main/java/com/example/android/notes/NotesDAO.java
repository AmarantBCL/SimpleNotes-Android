package com.example.android.notes;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NotesDAO {
    @Query("SELECT * FROM notes ORDER BY dayOfWeek")
    LiveData<List<Note>> getAllNotes();

    @Insert
    void addNote(Note note);

    @Delete
    void deleteNote(Note note);

    @Query("DELETE FROM notes")
    void deleteAllNotes();
}
