package com.example.threetaps;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
        int pos = position;
        MusicModal modal = musicModalArrayList.get(pos);
        // get uri
        Uri contentUri = modal.getContentUri();
        // setting data for text views
        holder.musicTV.setText(modal.getFileName());
        holder.artistTV.setText(modal.getArtistName());
        // setting data for image view
        Bitmap thumbnail;

        // click listener to go to detail activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are opening a new activity and passing data to it.
                Intent i = new Intent(context, MusicDetailActivity.class);
                i.putExtra("list", musicModalArrayList);
                i.putExtra("pos", pos);
                i.putExtra("uri", contentUri.toString());
                i.putExtra("title", modal.getFileName());
                i.putExtra("artist", modal.getArtistName());
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
        private TextView artistTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            musicIV = itemView.findViewById(R.id.MusicItem_IV);
            musicTV = itemView.findViewById(R.id.MusicItem_TV);
            artistTV = itemView.findViewById(R.id.MusicItem_ArtistTV);
        }
    }

}
