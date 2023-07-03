package com.example.threetaps;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    private void getMusic() {
        // this method is doing the work! get the music data from user's device

    }

}