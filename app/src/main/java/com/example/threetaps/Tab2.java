package com.example.threetaps;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

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
    private ArrayList<GalleryModal> galleryImageArrayList;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_tab2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // on below line we are initializing our variables.
        galleryImageArrayList = new ArrayList<GalleryModal>();
        galleryRVAdapter =new GalleryRVAdapter();
        galleryRV = view.findViewById(R.id.RVGalleries);
        galleryRV.setLayoutManager(new GridLayoutManager(mainActivity, 4));
        galleryRV.setAdapter(galleryRVAdapter);

        loadingPB = view.findViewById(R.id.PBLoading);

        for(int i=1;i<=60;i++){
            switch(i%5){
                case 0 :
                    galleryImageArrayList.add(new GalleryModal(R.drawable.id_user0));
                    break;
                case 1 :
                    galleryImageArrayList.add(new GalleryModal(R.drawable.id_user1));
                    break;
                case 2 :
                    galleryImageArrayList.add(new GalleryModal(R.drawable.id_user2));
                    break;
                case 3 :
                    galleryImageArrayList.add(new GalleryModal(R.drawable.id_user3));
                    break;
                case 4 :
                    galleryImageArrayList.add(new GalleryModal(R.drawable.id_user4));
                    break;
            }
        }


        galleryRVAdapter.setGalleryModalArrayList(galleryImageArrayList);
        loadingPB.setVisibility(View.GONE);
        galleryRVAdapter.notifyDataSetChanged();
    }

}