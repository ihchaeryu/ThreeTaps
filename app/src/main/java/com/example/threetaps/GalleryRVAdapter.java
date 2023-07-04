package com.example.threetaps;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

class GalleryRVAdapter extends RecyclerView.Adapter<GalleryRVAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> galleryModalArrayList;
    public GalleryRVAdapter(Context context, ArrayList<String> galleryModalArrayList) {
        this.context = context;
        this.galleryModalArrayList = galleryModalArrayList;
    }

    @NonNull
    @Override
    public GalleryRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryRVAdapter.ViewHolder holder, int position) {
        holder.onBind(galleryModalArrayList.get(position));
        String uriString = galleryModalArrayList.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are opening a new activity and passing data to it.
                Intent intent = new Intent(context, GalleryDetailActivity.class);
                intent.putExtra("uri_key", uriString); // 문자열로 변환하여 추가
                intent.putExtra("int_key", holder.getAdapterPosition());
                intent.putExtra("stringArrayList_key", galleryModalArrayList);
                context.startActivity(intent);
            }
        });
    }

    public void setGalleryModalArrayList(ArrayList<String> list){
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
            galleryItem_IV = itemView.findViewById(R.id.GalleryItem_IV); // 여기서 id.gallery_item_iv는 이미지 뷰의 ID입니다.
        }

        void onBind(@NonNull String item){
            Glide.with(itemView)
                    .load(Uri.parse(item))
                    .centerCrop()
                    .into(galleryItem_IV);
        }
    }
}