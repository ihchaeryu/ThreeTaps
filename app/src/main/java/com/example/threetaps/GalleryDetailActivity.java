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

public class GalleryDetailActivity extends AppCompatActivity implements View.OnTouchListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_detail);

        Intent intent = getIntent();
        String uriString = intent.getStringExtra("uri_key");

        if (uriString != null) {
            Uri uri = Uri.parse(uriString);
            ImageView imageView = findViewById(R.id.detailImageView);
            imageView.setImageURI(uri);
            imageView.setOnTouchListener(this);
            Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();
        } else {
            Log.e("GalleryDetailActivity", "Uri is null");
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.detailImageView) {
            float screenWidth = v.getWidth();
            float touchX = event.getX();

            // 좌측 20% 내인지 확인
            boolean touchInLeft20Percent = touchX <= screenWidth * 0.2;
            // 우측 20% 내인지 확인
            boolean touchInRight20Percent = touchX >= screenWidth * 0.8;

            if (touchInLeft20Percent) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 좌측 20% 영역에서 터치가 시작됐을 때 수행할 작업을 여기에 작성합니다.
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 좌측 20% 영역에서 터치한 상태에서 움직일 때 수행할 작업을 여기에 작성합니다.
                        break;
                    case MotionEvent.ACTION_UP:
                        // 좌측 20% 영역에서 터치가 끝났을 때 수행할 작업을 여기에 작성합니다.
                        Toast.makeText(this, "touchInLeft20Percent", Toast.LENGTH_SHORT).show();

                        break;
                }
            } else if (touchInRight20Percent) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 우측 20% 영역에서 터치가 시작됐을 때 수행할 작업을 여기에 작성합니다.
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 우측 20% 영역에서 터치한 상태에서 움직일 때 수행할 작업을 여기에 작성합니다.
                        break;
                    case MotionEvent.ACTION_UP:
                        // 우측 20% 영역에서 터치가 끝났을 때 수행할 작업을 여기에 작성합니다.
                        Toast.makeText(this, "touchInRight20Percent", Toast.LENGTH_SHORT).show();
                        ImageView imageView = findViewById(R.id.detailImageView);
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.id_user4));
                        break;
                }
            }
        }
        return true;
    }
}