package com.example.android.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class AddNoteActivity extends AppCompatActivity {

    private EditText editTitle, editDesc;
    private Button buttonAdd;
    private RadioGroup radioGroup;
    private Spinner spinner;

    private NotesDBHelper dbHelper;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        initViews();
        initDatabase();
        buttonAdd.setOnClickListener(v -> {
            addNote();
        });
    }


    public static Intent newIntent(Context context) {
        return new Intent(context, AddNoteActivity.class);
    }

    private void initDatabase() {
        dbHelper = new NotesDBHelper(this);
        database = dbHelper.getWritableDatabase();
    }

    private void initViews() {
        editTitle = findViewById(R.id.edit_title);
        editDesc = findViewById(R.id.edit_desc);
        radioGroup = findViewById(R.id.radio_group);
        spinner = findViewById(R.id.spinner);
        buttonAdd = findViewById(R.id.btn_add_note);
    }

    private void addNote() {
        String title = editTitle.getText().toString().trim();
        String desc = editDesc.getText().toString().trim();
        int dayOfWeek = spinner.getSelectedItemPosition();
        int radioButtonId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(radioButtonId);
        int priority = Integer.parseInt(radioButton.getText().toString());
        if (isFilled(title, desc)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(NotesContract.NotesEntry.COLUMN_TITLE, title);
            contentValues.put(NotesContract.NotesEntry.COLUMN_DESCRIPTION, desc);
            contentValues.put(NotesContract.NotesEntry.COLUMN_DAY_OF_WEEK, dayOfWeek + 1);
            contentValues.put(NotesContract.NotesEntry.COLUMN_PRIORITY, priority);
            database.insert(NotesContract.NotesEntry.TABLE_NAME, null, contentValues);
            Intent intent = new Intent(AddNoteActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, R.string.fields_must_be_filled, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isFilled(String title, String desc) {
        return !title.isEmpty() && !desc.isEmpty();
    }
}