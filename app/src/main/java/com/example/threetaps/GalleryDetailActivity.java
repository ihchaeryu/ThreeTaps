package com.example.threetaps;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;

public class GalleryDetailActivity extends AppCompatActivity {
    private String uriString;
    private int position;
    private ArrayList<String> galleryModalArrayList;
    private PhotoView photoView;
    private Button btnPrevious, btnNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_detail);

        Intent intent = getIntent();
        uriString = intent.getStringExtra("uri_key");
        position = intent.getIntExtra("int_key", -1);
        galleryModalArrayList  = intent.getStringArrayListExtra("stringArrayList_key");

        if (uriString != null) {
            photoView = findViewById(R.id.detailPhotoView);
            photoView.setImageURI(Uri.parse(uriString));
            photoView.setMaximumScale(10.0f);
            photoView.setMinimumScale(1.0f);
//            Toast.makeText(this,  String.valueOf(position), Toast.LENGTH_SHORT).show();

            btnPrevious = findViewById(R.id.btnPrevious);
            btnNext = findViewById(R.id.btnNext);
            setButtonClickListeners();
        } else {
            Log.e("GalleryDetailActivity", "Uri is null");
        }
    }
    private void setButtonClickListeners() {
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPreviousImage();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNextImage();
            }
        });
    }
    private void showPreviousImage() {
        if(position > 0){
            photoView.setImageURI(Uri.parse(galleryModalArrayList.get(--position)));
//            Toast.makeText(this,  String.valueOf(position), Toast.LENGTH_SHORT).show();
        }
    }
    private void showNextImage() {
        if(position < galleryModalArrayList.size() - 1){
            photoView.setImageURI(Uri.parse(galleryModalArrayList.get(++position)));
//            Toast.makeText(this,  String.valueOf(position), Toast.LENGTH_SHORT).show();
        }
    }
}