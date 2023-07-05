package com.example.threetaps;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Tab2 extends Fragment {

    // to use the context here
    private MainActivity mainActivity;
    // creating variables for our array list, recycler view progress bar and adapter.
    private ArrayList<String> galleryImageArrayList;
    private RecyclerView galleryRV;
    private GalleryRVAdapter galleryRVAdapter;
    private GridLayoutManager gridLayoutManager;
    private ProgressBar loadingPB;
    private ScaleGestureDetector scaleGestureDetector;
    private float scale = 1.0f;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // assign context here for later use
        mainActivity = (MainActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // on below line we are initializing our variables.
        galleryImageArrayList = new ArrayList<String>();
        galleryRVAdapter = new GalleryRVAdapter(mainActivity, galleryImageArrayList);
        galleryRV = view.findViewById(R.id.RVGalleries);
        gridLayoutManager = new GridLayoutManager(mainActivity, galleryRVAdapter.gridCount);
        galleryRV.setLayoutManager(gridLayoutManager);
        galleryRV.setAdapter(galleryRVAdapter);
        galleryRV.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                scaleGestureDetector.onTouchEvent(e);
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });


        loadingPB = view.findViewById(R.id.PBLoading);
//        requestPermissions(); don't need permissions again
        getAllPhotos();
        scaleGestureDetector = new ScaleGestureDetector(mainActivity, new ScaleGestureDetector.SimpleOnScaleGestureListener()  {
            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
                scale *= detector.getScaleFactor();
                if (scale > 1.0f) {
                    if (galleryRVAdapter.gridCount > 2)
                        galleryRVAdapter.gridCount--;
                    updateGridLayout();
                } else if (scale < 0.7f) {
                    if (galleryRVAdapter.gridCount < 10)
                        galleryRVAdapter.gridCount++;
                    updateGridLayout();
                }
                scale = 1.0f;
            }
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                scale *= detector.getScaleFactor();
                return true;
            }
        });

    }
    private void updateGridLayout() {
        gridLayoutManager.setSpanCount(galleryRVAdapter.gridCount);
        galleryRV.setLayoutManager(gridLayoutManager);
        galleryRV.setAdapter(galleryRVAdapter);
    }
    public void  getAllPhotos(){
        Uri collection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }
        String[] projection = new String[]{
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, //the album it in
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.MIME_TYPE
        };
        Cursor cursor = mainActivity.getContentResolver()
                .query(collection,
                        projection,
                        null,
                        null,
                        MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");
        if(cursor != null){
            while (cursor.moveToNext() && galleryImageArrayList.size() < 1000){
                int idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                long id = cursor.getLong(idColumn);
                Uri uri =  Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,String.valueOf(id));
                galleryImageArrayList.add(uri.toString());
            }
        }
        cursor.close();
        loadingPB.setVisibility(View.GONE);
    }
}