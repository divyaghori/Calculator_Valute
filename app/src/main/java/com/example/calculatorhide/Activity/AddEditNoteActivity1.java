package com.example.calculatorhide.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.calculatorhide.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class AddEditNoteActivity1 extends AppCompatActivity {
    public static final String EXTRA_ID =
            "_id";
    public static final String EXTRA_DESCRIPTION =
            "desc";
    private EditText editTextTitle;
    FloatingActionButton floatingActionButton;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        editTextTitle = findViewById(R.id.edit_text_title);
        floatingActionButton = findViewById(R.id.button_add_node);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Note");
            editTextTitle.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
        } else {
        }
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote();
            }
        });
    }
    private void saveNote() {
        String title = editTextTitle.getText().toString();
        if (title.trim().isEmpty() ) {
            return;
        }
        Intent data = new Intent();
        data.putExtra(EXTRA_DESCRIPTION, title);
        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }
        setResult(RESULT_OK, data);
        finish();
    }


}
