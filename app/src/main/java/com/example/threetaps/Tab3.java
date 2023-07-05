package com.example.threetaps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

public class Tab3 extends Fragment {

    // variables
    // to use the context here
    private MainActivity mainActivity;
    private ArrayList<MusicModal> musicModalArrayList;
    private RecyclerView musicRV;
    private MusicRVAdapter musicRVAdapter;
    private ProgressBar loadingPB;
    // activity result launcher to solve deprecation
    private ActivityResultLauncher<Intent> launcher;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // activity result launcher to solve deprecation
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Handle the result here
                    }
                });

        return inflater.inflate(R.layout.fragment_tab3, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // initialize variables
        musicModalArrayList = new ArrayList<>();
        musicRV = view.findViewById(R.id.RVMusic);
        loadingPB = view.findViewById(R.id.PBLoading);

        // calling method to prepare our recycler view.
        prepareMusicRV();

        // get music; permissions were requested together in tab1
        getMusic();
    }

    private void prepareMusicRV() {
        // prepare RV adapter
        musicRVAdapter = new MusicRVAdapter(mainActivity, musicModalArrayList);
        // set layout manager
        musicRV.setLayoutManager(new LinearLayoutManager(mainActivity));
        // set RV with adapter
        musicRV.setAdapter(musicRVAdapter);
    }

    @SuppressLint("Range")
    private void getMusic() {
        // this method is doing the work! get the music data from user's device
        // some string variables
        Long musicID;
        Long albumId;
        String displayName = "";
        String titleName = "";
        String artistName = "";
        Uri contentUri;
        Uri artUri = Uri.parse("content://media/external/audio/albumart");
        Uri albumArtUri;

        // on below line we are calling our content resolver for getting music
        Uri collection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }
        String[] projection = new String[] {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.MIME_TYPE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.IS_MUSIC,
                MediaStore.Audio.Media.ALBUM_ID
        };
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";      // sort order sql
        Cursor cursor = mainActivity.getContentResolver()
                .query(collection,
                        projection,
                        null,
                        null,
                        sortOrder);

        // on below line we are checking the count for our cursor.
        if (cursor.getCount() > 0) {
            // if the count is greater than 0 then we are running a loop to move our cursor to next.
            while (cursor.moveToNext()) {
                // An indicator of whether this is a music or not
                int isMusic = Integer.parseInt(cursor
                        .getString(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC)));
                if (isMusic > 0) {
                    // check audio file format (only mp3 or wav)
                    displayName = cursor.getString(cursor
                            .getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    if (displayName.endsWith(".mp3") || displayName.endsWith(".wav") || displayName.endsWith(".m4a"))
                    {
                        musicID = cursor.getLong(cursor
                                .getColumnIndex(MediaStore.Audio.Media._ID));
//                    displayName = cursor.getString(cursor
//                            .getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
//                            .split("_")[0];
                        contentUri = ContentUris.withAppendedId(collection, musicID);
                        titleName = cursor.getString(cursor
                                .getColumnIndex(MediaStore.Audio.Media.TITLE));
                        artistName = cursor.getString(cursor
                                .getColumnIndex(MediaStore.Audio.Media.ARTIST));
                        albumId = cursor.getLong(cursor
                                .getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                        albumArtUri = ContentUris.withAppendedId(artUri, albumId);

                        musicModalArrayList.add(new MusicModal(contentUri, albumArtUri, titleName, artistName));
                    }
                }
            }
        }
        // on below line we are closing our cursor.
        cursor.close();
        // on below line we are hiding our progress bar and notifying our adapter class.
        loadingPB.setVisibility(View.GONE);
        musicRVAdapter.notifyDataSetChanged();
    }

}