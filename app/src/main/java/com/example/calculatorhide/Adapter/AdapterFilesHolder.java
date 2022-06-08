package com.example.calculatorhide.Adapter;


import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.core.widget.CompoundButtonCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calculatorhide.Activity.ImportDocument;
import com.example.calculatorhide.Model.ModelFilesHolder;
import com.example.calculatorhide.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


public class AdapterFilesHolder extends RecyclerView.Adapter<AdapterFilesHolder.AdapterViewHolder> implements Filterable {
    private Filter MyFilesFilter = new Filter() { // from class: example.own.allofficefilereader.adapters.AdapterFilesHolder.1
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList arrayList = new ArrayList();
            if (charSequence == null || charSequence.length() == 0) {
                arrayList.addAll(AdapterFilesHolder.this.tempList);
            } else {
                String trim = charSequence.toString().toLowerCase().trim();
                Iterator<ModelFilesHolder> it2 = AdapterFilesHolder.this.tempList.iterator();
                while (it2.hasNext()) {
                    ModelFilesHolder next = it2.next();
                    if (next.getFileName().toLowerCase().contains(trim)) {
                        arrayList.add(next);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = arrayList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            AdapterFilesHolder.this.itemsList.clear();
            AdapterFilesHolder.this.itemsList.addAll((List) filterResults.values);
            AdapterFilesHolder.this.notifyDataSetChanged();
        }
    };
    ImportDocument activityFilesHolder;
    Context context;
    ArrayList<ModelFilesHolder> itemsList;
    ArrayList<ModelFilesHolder> tempList;

    public AdapterFilesHolder(Context context, ImportDocument activityFilesHolder, ArrayList<ModelFilesHolder> arrayList) {
        this.context = context;
        this.activityFilesHolder = activityFilesHolder;
        this.itemsList = arrayList;
        this.tempList = arrayList;
    }

    public static String getMimeType(Context context, String str) {
        return MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(str)).toString());
    }


    @Override
    public int getItemCount() {
        return this.itemsList.size();
    }

    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new AdapterViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view_ac_filesholder, viewGroup, false));
    }
    public static String getFormattedDate(File file) {
        Date lastModDate = new Date(file.lastModified());
        String[] formatDate = lastModDate.toString().split(" ");
        String time = formatDate[3];
        String[] formatTime = time.split(":");
        String date = formatTime[0] + ":" + formatTime[1];

        return formatDate[0] + ", " + formatDate[1] + " " + formatDate[2] + " at " + date;
    }

    public void onBindViewHolder(final AdapterViewHolder adapterViewHolder, final int i) {
        ModelFilesHolder modelFilesHolder = this.itemsList.get(i);
        adapterViewHolder.fileNameTv.setText(modelFilesHolder.getFileName());
        adapterViewHolder.time.setText(getFormattedDate(new File(modelFilesHolder.getFileUri())));
        adapterViewHolder.fileSizeTv.setText(getFileSize(new File(modelFilesHolder.getFileUri())));
        String mimeType = getMimeType(this.context, modelFilesHolder.getFileUri());
        mimeType.hashCode();
        mimeType.hashCode();
        char c = 65535;
        switch (mimeType.hashCode()) {
            case 99640:
                if (mimeType.equals(MainConstant.FILE_TYPE_DOC)) {
                    c = 0;
                    break;
                }
                break;
            case 99657:
                if (mimeType.equals(MainConstant.FILE_TYPE_DOT)) {
                    c = 1;
                    break;
                }
                break;
            case 110834:
                if (mimeType.equals("pdf")) {
                    c = 2;
                    break;
                }
                break;
            case 111189:
                if (mimeType.equals(MainConstant.FILE_TYPE_POT)) {
                    c = 3;
                    break;
                }
                break;
            case 111220:
                if (mimeType.equals(MainConstant.FILE_TYPE_PPT)) {
                    c = 4;
                    break;
                }
                break;
            case 113252:
                if (mimeType.equals("rtf")) {
                    c = 5;
                    break;
                }
                break;
            case 115312:
                if (mimeType.equals(MainConstant.FILE_TYPE_TXT)) {
                    c = 6;
                    break;
                }
                break;
            case 118783:
                if (mimeType.equals(MainConstant.FILE_TYPE_XLS)) {
                    c = 7;
                    break;
                }
                break;
            case 118784:
                if (mimeType.equals(MainConstant.FILE_TYPE_XLT)) {
                    c = '\b';
                    break;
                }
                break;
            case 118807:
                if (mimeType.equals("xml")) {
                    c = '\t';
                    break;
                }
                break;
            case 3088960:
                if (mimeType.equals(MainConstant.FILE_TYPE_DOCX)) {
                    c = '\n';
                    break;
                }
                break;
            case 3089476:
                if (mimeType.equals(MainConstant.FILE_TYPE_DOTM)) {
                    c = 11;
                    break;
                }
                break;
            case 3089487:
                if (mimeType.equals(MainConstant.FILE_TYPE_DOTX)) {
                    c = '\f';
                    break;
                }
                break;
            case 3213227:
                if (mimeType.equals("html")) {
                    c = '\r';
                    break;
                }
                break;
            case 3446968:
                if (mimeType.equals(MainConstant.FILE_TYPE_POTM)) {
                    c = 14;
                    break;
                }
                break;
            case 3446979:
                if (mimeType.equals(MainConstant.FILE_TYPE_POTX)) {
                    c = 15;
                    break;
                }
                break;
            case 3447929:
                if (mimeType.equals(MainConstant.FILE_TYPE_PPTM)) {
                    c = 16;
                    break;
                }
                break;
            case 3447940:
                if (mimeType.equals(MainConstant.FILE_TYPE_PPTX)) {
                    c = 17;
                    break;
                }
                break;
            case 3682382:
                if (mimeType.equals(MainConstant.FILE_TYPE_XLSM)) {
                    c = 18;
                    break;
                }
                break;
            case 3682393:
                if (mimeType.equals(MainConstant.FILE_TYPE_XLSX)) {
                    c = 19;
                    break;
                }
                break;
            case 3682413:
                if (mimeType.equals(MainConstant.FILE_TYPE_XLTM)) {
                    c = 20;
                    break;
                }
                break;
            case 3682424:
                if (mimeType.equals(MainConstant.FILE_TYPE_XLTX)) {
                    c = 21;
                    break;
                }
                break;
        }



    }

    private void setCheckBoxTintColor(CheckBox checkBox, int i) {
        if (Build.VERSION.SDK_INT < 21) {
            CompoundButtonCompat.setButtonTintList(checkBox, ColorStateList.valueOf(i));
        } else {
            checkBox.setButtonTintList(ColorStateList.valueOf(i));
        }
    }


    public String getFileSize(File file) {
        if (!file.isFile()) {
            return null;
        }
        double length = file.length();
        int i = (length > 1024.0d ? 1 : (length == 1024.0d ? 0 : -1));
        if (i < 0) {
            return String.valueOf(length).concat("B");
        }
        if (i <= 0 || length >= 1048576.0d) {
            return String.valueOf(Math.round((length / 1232896.0d) * 100.0d) / 100.0d).concat("MB");
        }
        return String.valueOf(Math.round((length / 1024.0d) * 100.0d) / 100.0d).concat("KB");
    }


    @Override // android.widget.Filterable
    public Filter getFilter() {
        return this.MyFilesFilter;
    }


    public static class AdapterViewHolder extends RecyclerView.ViewHolder {
        TextView fileNameTv;
        TextView fileSizeTv;
        ImageView imageView;
        TextView time;

        public AdapterViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.itemView_acFilesHolder_Iv);
            this.fileNameTv = (TextView) view.findViewById(R.id.itemView_acFilesHolder_FileNameTV);
            this.fileSizeTv = (TextView) view.findViewById(R.id.itemView_acFilesHolder_FileSizeTV);
            this.time = (TextView) view.findViewById(R.id.time);
        }
    }
}
