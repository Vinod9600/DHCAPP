package com.dhc.app.ui.documentlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dhc.app.R;
import com.dhc.app.data.ArInvoice;
import com.dhc.app.data.DocListItem;
import com.dhc.app.data.Movie;

import java.util.List;

/**
 * Created by Vinod Durge on  21 Mar 2021
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

    private List<DocListItem> moviesList;

    private RowClickListener rowClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            genre = (TextView) view.findViewById(R.id.genre);
            year = (TextView) view.findViewById(R.id.year);
        }
    }


    public MoviesAdapter(List<DocListItem> moviesList, RowClickListener rowClickListener) {
        this.moviesList = moviesList;
        this.rowClickListener = rowClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DocListItem movie = moviesList.get(position);
        holder.title.setText("Document number:" + movie.getDocNumber());
        holder.genre.setText("Reference:" + movie.getRefNo());
        if (!movie.getDocStatus().isEmpty()) {
            if (movie.getDocStatus().equalsIgnoreCase("o")) {
                holder.year.setText("OPEN");
            } else {
                holder.year.setText("CLOSED");
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rowClickListener.onItemClickCallBack(movie.getDocEntry());
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}