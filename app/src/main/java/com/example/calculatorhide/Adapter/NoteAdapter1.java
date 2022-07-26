package com.example.calculatorhide.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calculatorhide.Model.NoteModel;
import com.example.calculatorhide.R;

import java.util.List;


public class NoteAdapter1 extends RecyclerView.Adapter<NoteAdapter1.NoteHolder> {
    private OnItemClickListner listner;
    private List<NoteModel> notes;
    Context context;

    public NoteAdapter1(Context context, List<NoteModel> itemList,OnItemClickListner listner) {
        this.context = context;
        notes = itemList;
        this.listner = listner;
    }
    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new NoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        NoteModel currentNote = notes.get(position);
        holder.textViewTitle.setText(currentNote.getDesc());
        holder.textViewPriority.setText(String.valueOf(currentNote.getDesc()));
        holder.textViewDescription.setText(currentNote.getDesc());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }



    class NoteHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewDescription;
        private TextView textViewPriority;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPriority = itemView.findViewById(R.id.text_view_priority);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listner.OnItemClick(notes,getAdapterPosition());
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listner.onLongClick(getAdapterPosition());
                    return false;
                }
            });
        }
    }

    public interface OnItemClickListner {
        void OnItemClick(List<NoteModel> note,int position);
        void onLongClick(int position);
    }

}
