package com.example.threetaps;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class GalleryRVAdapter extends RecyclerView.Adapter<GalleryRVAdapter.ViewHolder> {

    private ArrayList<GalleryModal> galleryModalArrayList;

    @NonNull
    @Override
    public GalleryRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryRVAdapter.ViewHolder holder, int position) {
        holder.onBind(galleryModalArrayList.get(position));
    }

    public void setGalleryModalArrayList(ArrayList<GalleryModal> list){
        this.galleryModalArrayList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return galleryModalArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView galleryItem_IV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            galleryItem_IV = (ImageView) itemView.findViewById(R.id.GalleryItem_IV);
        }

        void onBind(@NonNull GalleryModal item){
            galleryItem_IV.setImageResource(item.getResourceId());
        }
    }
}