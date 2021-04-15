package com.dhc.app.ui.documentlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dhc.app.R;
import com.dhc.app.data.ArInvoice;

import java.util.List;

/**
 * Created by Vinod Durge on  20 Mar 2021
 */
class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.ViewHolder>{

    private List<ArInvoice> list;

    public DocumentAdapter(List<ArInvoice> list) {
        this.list = list;
    }

    public void addDate(List<ArInvoice> list){
        this.list = list;
    }

    @NonNull
    @Override
    public DocumentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.document_item, null, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentAdapter.ViewHolder holder, int position) {
        ArInvoice arInvoice = list.get(position);

        TextView textView = holder.documentName;
        textView.setText(arInvoice.getDocNumber());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView documentName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            documentName = itemView.findViewById(R.id.tv_document_number);
        }
    }
}
