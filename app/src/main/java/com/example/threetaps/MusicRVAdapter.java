package com.example.threetaps;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class MusicRVAdapter extends RecyclerView.Adapter<MusicRVAdapter.ViewHolder> {

    // creating variables for context and array list.
    private Context context;
    private ArrayList<MusicModal> musicModalArrayList;

    // constructor
    public MusicRVAdapter(Context context, ArrayList<MusicModal> musicModalArrayList) {
        this.context = context;
        this.musicModalArrayList = musicModalArrayList;
    }

    @NonNull
    @Override
    public MusicRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MusicRVAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.music_rv_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MusicRVAdapter.ViewHolder holder, int position) {
        MusicModal modal = musicModalArrayList.get(position);
        holder.musicTV.setText(modal.getFileName());

        // click listener to go to detail activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are opening a new activity and passing data to it.
                Intent i = new Intent(context, MusicDetailActivity.class);
                i.putExtra("name", modal.getFileName());
                // on below line we are starting a new activity,
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return musicModalArrayList.size();
    }

    // nested view holder class
    public class ViewHolder extends RecyclerView.ViewHolder {

        // variables
        private ImageView musicIV;
        private TextView musicTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            musicIV = itemView.findViewById(R.id.MusicItem_IV);
            musicTV = itemView.findViewById(R.id.MusicItem_TV);
        }
    }

}
