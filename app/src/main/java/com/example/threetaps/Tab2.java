package com.example.threetaps;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_IMAGES;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

public class Tab2 extends Fragment {

    // to use the context here
    private MainActivity mainActivity;
    // creating variables for our array list, recycler view progress bar and adapter.
    private ArrayList<String> galleryImageArrayList;
    private RecyclerView galleryRV;
    private GalleryRVAdapter galleryRVAdapter;
    private ProgressBar loadingPB;

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
        galleryRV.setLayoutManager(new GridLayoutManager(mainActivity, 4));
        galleryRV.setAdapter(galleryRVAdapter);

        loadingPB = view.findViewById(R.id.PBLoading);
        requestPermissions();
    }

    private void requestPermissions() {
        ArrayList<String> permissionsList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            // LOLLIPOP 이상 버전의 장치인 경우, 최신 API 사용
            permissionsList.add(READ_MEDIA_IMAGES);

        } else {
            // LOLLIPOP 미만 버전의 장치인 경우, 대체 API 사용
            permissionsList.add(READ_EXTERNAL_STORAGE);
        }
        Dexter.withContext(mainActivity)
                .withPermissions(permissionsList)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            getAllPhotos();
                            Toast.makeText(mainActivity, "All the permissions are granted..", Toast.LENGTH_SHORT).show();
                        }
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(mainActivity, "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread().check();
    }

    private void showSettingsDialog() {

    }

    public void  getAllPhotos(){
        String[] projection = new String[]{
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, //the album it in
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.MIME_TYPE
        };
        Cursor cursor = mainActivity.getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        projection,
                        null,
                        null,
                        MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");
        if(cursor != null){
            while (cursor.moveToNext()){
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