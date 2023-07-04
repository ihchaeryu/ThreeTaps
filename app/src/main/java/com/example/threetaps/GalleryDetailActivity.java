package com.example.threetaps;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class GalleryDetailActivity extends AppCompatActivity implements View.OnTouchListener {
    private String uriString;
    private int position;
    private ArrayList<String> galleryModalArrayList;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_detail);

        Intent intent = getIntent();
        uriString = intent.getStringExtra("uri_key");
        position = intent.getIntExtra("int_key", -1);
        galleryModalArrayList  = intent.getStringArrayListExtra("stringArrayList_key");

        if (uriString != null) {
            imageView = findViewById(R.id.detailImageView);
            imageView.setImageURI(Uri.parse(uriString));
            imageView.setOnTouchListener(this);
            Toast.makeText(this,  String.valueOf(position), Toast.LENGTH_SHORT).show();
        } else {
            Log.e("GalleryDetailActivity", "Uri is null");
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.detailImageView) {
            float screenWidth = v.getWidth();
            float touchX = event.getX();
            boolean touchInLeft20Percent = touchX <= screenWidth * 0.2;
            boolean touchInRight20Percent = touchX >= screenWidth * 0.8;

            if (touchInLeft20Percent) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 좌측 20% 영역 터치 시작
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 좌측 20% 영역 터치 이후 작동
                        break;
                    case MotionEvent.ACTION_UP:
                        // 좌측 20% 영역 터치 종료
                        if(position > 0){
                            imageView.setImageURI(Uri.parse(galleryModalArrayList.get(--position)));
                            Toast.makeText(this,  String.valueOf(position), Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            } else if (touchInRight20Percent) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 우측 20% 영역 터치 시작
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 우측 20% 영역 터치 이후 작동
                        break;
                    case MotionEvent.ACTION_UP:
                        // 우측 20% 영역 터치 종료
                        if(position < galleryModalArrayList.size() - 1){
                            imageView.setImageURI(Uri.parse(galleryModalArrayList.get(++position)));
                            Toast.makeText(this,  String.valueOf(position), Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        }
        return true;
    }
}