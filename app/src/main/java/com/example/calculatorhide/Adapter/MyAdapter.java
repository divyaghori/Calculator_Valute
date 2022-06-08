package com.example.calculatorhide.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.calculatorhide.Activity.FileListActivity;
import com.example.calculatorhide.Model.ModelFilesHolder;
import com.example.calculatorhide.R;

import java.io.File;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

    Context context;
    File[] filesAndFolders;

    public MyAdapter(Context context, File[] filesAndFolders){
        this.context = context;
        this.filesAndFolders = filesAndFolders;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        File selectedFile = filesAndFolders[position];
        File[] listFiles = selectedFile.listFiles();
//        if (listFiles != null) {
//            for (int i = 0; i < listFiles.length; i++) {
//                if (listFiles[i].isDirectory() && !listFiles[i].getAbsolutePath().contains(".galleryvault_DoNotDelete") && !listFiles[i].getAbsolutePath().contains(".Shared")) {
//                    allDocsFiles(listFiles[i]);
//                } else if ((listFiles[i].getName().endsWith(".pdf") || listFiles[i].getName().endsWith(".doc") || listFiles[i].getName().endsWith(".doc") || listFiles[i].getName().endsWith(".ppt") || listFiles[i].getName().endsWith(".xlsx") || listFiles[i].getName().endsWith(".xls") || listFiles[i].getName().endsWith(".html") || listFiles[i].getName().endsWith(".pptx") || listFiles[i].getName().endsWith(".txt") || listFiles[i].getName().endsWith(".xml") || listFiles[i].getName().endsWith(".rtf")) && !this.itemsList.contains(listFiles[i])) {
//                    listFiles.add(new ModelFilesHolder(listFiles[i].getName(), listFiles[i].getAbsolutePath(), false));
//                }
//            }
//        }
        holder.textView.setText(selectedFile.getName());
        if(selectedFile.isDirectory()){
            holder.imageView.setImageResource(R.drawable.ic_baseline_folder_24);
        }else{
//            holder.imageView.setImageResource(R.drawable.ic_baseline_insert_drive_file_24);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedFile.isDirectory()){
                    Intent intent = new Intent(context, FileListActivity.class);
                    String path = selectedFile.getAbsolutePath();
                    intent.putExtra("path",path);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }else{
                    try {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        String type = "application/pdf";
                        intent.setDataAndType(Uri.parse(selectedFile.getAbsolutePath()), type);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(context.getApplicationContext(),"Cannot open the file",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context,v);
                popupMenu.getMenu().add("DELETE");
                popupMenu.getMenu().add("MOVE");
                popupMenu.getMenu().add("RENAME");
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("DELETE")){
                            boolean deleted = selectedFile.delete();
                            if(deleted){
                                Toast.makeText(context.getApplicationContext(),"DELETED ",Toast.LENGTH_SHORT).show();
                                v.setVisibility(View.GONE);
                            }
                        }
                        if(item.getTitle().equals("MOVE")){
                            Toast.makeText(context.getApplicationContext(),"MOVED ",Toast.LENGTH_SHORT).show();
                        }
                        if(item.getTitle().equals("RENAME")){
                            Toast.makeText(context.getApplicationContext(),"RENAME ",Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });
                popupMenu.show();
                return true;
            }
        });
    }
    @Override
    public int getItemCount() {
        return filesAndFolders.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.file_name_text_view);
            imageView = itemView.findViewById(R.id.icon_view);
        }
    }
}
