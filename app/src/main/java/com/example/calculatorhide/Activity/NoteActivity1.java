package com.example.calculatorhide.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calculatorhide.Adapter.NoteAdapter1;
import com.example.calculatorhide.Model.NoteModel;
import com.example.calculatorhide.R;
import com.example.calculatorhide.Utils.CustomProgressDialogue;
import com.example.calculatorhide.Utils.HideFiles;
import com.example.calculatorhide.database.DBController;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class NoteActivity1 extends AppCompatActivity {
    public static final int ADD_NODE_REQUEST = 1;
    public static final int EDIT_NODE_REQUEST = 2;
    HideFiles hideFiles;
    TextView maintext;
    ImageView back;
    TextView tvNoData;
    Activity activity;
    ImageView image;
    CustomProgressDialogue dialogue;
    DBController db;
    private List<NoteModel> noteModels = new ArrayList<>();
    RecyclerView recyclerView;
    private NoteAdapter1 adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainnote);
        activity = this;
        hideFiles = new HideFiles(activity);
        db = new DBController(this);
        dialogue = new CustomProgressDialogue(activity);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);
        maintext = findViewById(R.id.maintext);
        maintext.setText(SplashActivity.resources.getString(R.string.Notes));
        tvNoData = findViewById(R.id.tvNodata);
        tvNoData.setText(SplashActivity.resources.getString(R.string.No_files_added));
        image = findViewById(R.id.image);
        FloatingActionButton buttonAddNote = findViewById(R.id.button_add_node);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoteActivity1.this, AddEditNoteActivity1.class);
                startActivityForResult(intent, ADD_NODE_REQUEST);
            }
        });
        hideFiles.getSuccess(new HideFiles.SuccessInterface() {
            @Override
            public void onSuccess(boolean value) {
                getImages();
                dialogue.dismiss();
            }

            @Override
            public void onLoading(boolean value) {
                dialogue.show();
            }
        });
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_NODE_REQUEST && resultCode == RESULT_OK) {
            String description = data.getStringExtra(AddEditNoteActivity1.EXTRA_DESCRIPTION);
            hideFiles.createnote(description);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getImages();
                }
            }, 1000);
        } else if (requestCode == EDIT_NODE_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditNoteActivity1.EXTRA_ID, -1);
            if (id == -1) {
                return;
            }
            String description = data.getStringExtra(AddEditNoteActivity1.EXTRA_DESCRIPTION);
            db.updatenotedata(id, description);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getImages();
                }
            }, 1000);
        } else {
        }
    }

    @Override
    protected void onResume() {
        getImages();
        super.onResume();
    }

    private void getImages() {
        noteModels.clear();
        noteModels = db.getnote();
        if (noteModels.size() != 0) {
            tvNoData.setVisibility(View.GONE);
            image.setVisibility(View.GONE);
            adapter = new NoteAdapter1(getApplicationContext(), noteModels, new NoteAdapter1.OnItemClickListner() {
                @Override
                public void OnItemClick(List<NoteModel> note, int position) {
                    Intent intent = new Intent(NoteActivity1.this, AddEditNoteActivity1.class);
                    intent.putExtra(AddEditNoteActivity1.EXTRA_ID, note.get(position).get_id());
                    intent.putExtra(AddEditNoteActivity1.EXTRA_DESCRIPTION, note.get(position).getDesc());
                    startActivityForResult(intent, EDIT_NODE_REQUEST);
                }

                @Override
                public void onLongClick(int position) {
                    showRestorePopup(position);
                }
            });
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            tvNoData.setVisibility(View.VISIBLE);
            image.setVisibility(View.VISIBLE);
        }
    }

    public void showRestorePopup(int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.delete_file_layout, null);
        dialogBuilder.setView(dialogView);
        TextView tvDelete = dialogView.findViewById(R.id.tvDelete);
        AlertDialog alertDialog = dialogBuilder.create();
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deletenote(noteModels.get(position).get_id());
                noteModels.remove(position);
                adapter.notifyDataSetChanged();
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(NoteActivity1.this, HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
