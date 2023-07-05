package com.example.threetaps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gauravk.audiovisualizer.visualizer.BarVisualizer;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

public class MusicDetailActivity extends AppCompatActivity {

    // variables
    private Uri contentUri;
    private Uri albumArtUri;
    private String musicTitle, musicArtist;
    private TextView titleTV, artistTV, songStartTV, songEndTV;
    private ImageView thumbnailIV;
    private Button playBtn, previousBtn, nextBtn;
    private SeekBar seekBar;
    private BarVisualizer barVisualizer;
    private static MediaPlayer mediaPlayer;
    private Thread updateSeekBar;
    private ArrayList<MusicModal> musicModalArrayList;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_detail);

//        // on below line we are getting data which
//        // we passed in our adapter class with intent.
//        contentUri = Uri.parse(getIntent().getStringExtra("uri"));
//        musicTitle = getIntent().getStringExtra("title");
//        musicArtist = getIntent().getStringExtra("artist");
//        musicModalArrayList = (ArrayList) getIntent().getStringArrayListExtra("list");
        musicModalArrayList = getIntent().getParcelableArrayListExtra("list");
        position = getIntent().getIntExtra("pos", 0);

        // initializing our views.
        // bind each view with id
        titleTV = findViewById(R.id.TitleTV);
        titleTV.setSelected(true);
        artistTV = findViewById(R.id.ArtistTV);
        artistTV.setSelected(true);
        songStartTV = findViewById(R.id.SongStartTV);
        songEndTV = findViewById(R.id.SongEndTV);
        thumbnailIV = findViewById(R.id.ThumbnailIV);

        // initializing our other objects.
        // bind each object with id
        playBtn = findViewById(R.id.PlayBtn);
        previousBtn = findViewById(R.id.PreviousBtn);
        nextBtn = findViewById(R.id.NextBtn);
        seekBar = findViewById(R.id.SeekBar);
        barVisualizer = findViewById(R.id.Wave);

//        // initialize visualizer
//        barVisualizer.setVisibility(View.VISIBLE);
//        barVisualizer.setColor(ContextCompat.getColor(this, R.color.dark_purple));
//        // define a custom number of bars we want in the visualizer it is between (10 - 256).
//        barVisualizer.setDensity(50);

        prepareMusicVariablesAndStart(position);

//        //Checking if any song playing or not
//        if (mediaPlayer != null) {
//            //we will start mediaPlayer if currently there is no songs in it
//            mediaPlayer.start();
//            mediaPlayer.release();
//        }
//
//        // setting the text views
//        titleTV.setText(musicTitle);
//        artistTV.setText(musicArtist);
//
//        // passing the song path to the Media Player
//        mediaPlayer = MediaPlayer.create(getApplicationContext(), contentUri);
//        mediaPlayer.start();
//
//        // preparing the time format for setting to textView
//        String endTime = createDuration(mediaPlayer.getDuration());
//
//        // set music ending time
//        songEndTV.setText(endTime);
//
////        // show bar visualizer
////        showVisualizer();
//
        // Thread to update the seekBar while playing song
        updateSeekBar = new Thread() {
            @Override
            public void run() {

                int TotalDuration = mediaPlayer.getDuration();
                int CurrentPosition = 0;

                while (CurrentPosition < TotalDuration) {
                    try {
                        sleep(500);
                        CurrentPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(CurrentPosition);

                    } catch (InterruptedException | IllegalStateException e) {
                        e.printStackTrace();
                    }
                }

            }
        };

        // setting the seekbar's max progress to the maximum duration of the media file
        seekBar.setMax(mediaPlayer.getDuration());
        updateSeekBar.start();

        // setting the Music player from the position of the seekbar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //getting the progress of the seek bar and setting it to Media Player
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        //Creating the Handler to update the current duration (songStartTV)
        final Handler handler = new Handler();
        final int delay = 1000;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Getting the current duration from the media player
                String currentTime = createDuration(mediaPlayer.getCurrentPosition());

                //Setting the current duration in textView
                songStartTV.setText(currentTime);
                handler.postDelayed(this, delay);
            }
        }, delay);

        // click listener for play button
        playBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // check if media is playing or not
                if (mediaPlayer.isPlaying()) {
                    // set to play icon
                    playBtn.setBackgroundResource(R.drawable.ic_play);
                    // pause
                    mediaPlayer.pause();
                } else {
                    // set to pause icon
                    playBtn.setBackgroundResource(R.drawable.ic_pause);
                    // play
                    mediaPlayer.start();


                }

            }
        });

        // click listener for prev button
        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // decrement position
                position = (position - 1) % musicModalArrayList.size();
                if (position < 0) {
                    position = musicModalArrayList.size() - 1;
                }

                prepareMusicVariablesAndStart(position);
            }
        });

        // click listener for next button
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // increment position
                position = (position + 1) % musicModalArrayList.size();

                prepareMusicVariablesAndStart(position);
            }
        });

        // clicking on next button after the completion of song
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                nextBtn.performClick();
            }
        });

    }

    public void prepareMusicVariablesAndStart(int position) {
        MusicModal modal = musicModalArrayList.get(position);

        // get uri, title, artist
        contentUri = modal.getContentUri();
        albumArtUri = modal.getAlbumArtUri();
        musicTitle = modal.getFileName();
        musicArtist = modal.getArtistName();

        //Checking if any song playing or not
        if (mediaPlayer != null) {
            //we will start mediaPlayer if currently there is no songs in it
            mediaPlayer.start();
            mediaPlayer.release();
        }

        // setting the text views
        titleTV.setText(musicTitle);
        artistTV.setText(musicArtist);

        // setting the image view
        thumbnailIV.setImageURI(albumArtUri);

        // passing the song path to the Media Player
        mediaPlayer = MediaPlayer.create(getApplicationContext(), contentUri);
        mediaPlayer.start();

        // preparing the time format for setting to textView
        String endTime = createDuration(mediaPlayer.getDuration());

        // set music ending time
        songEndTV.setText(endTime);

        // show visualizer
        showVisualizer();

//        // Thread to update the seekBar while playing song
//        updateSeekBar = new Thread() {
//            @Override
//            public void run() {
//
//                int TotalDuration = mediaPlayer.getDuration();
//                int CurrentPosition = 0;
//
//                while (CurrentPosition < TotalDuration) {
//                    try {
//                        sleep(500);
//                        CurrentPosition = mediaPlayer.getCurrentPosition();
//                        seekBar.setProgress(CurrentPosition);
//
//                    } catch (InterruptedException | IllegalStateException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            }
//        };
//
//        // setting the seekbar's max progress to the maximum duration of the media file
//        seekBar.setMax(mediaPlayer.getDuration());
//        updateSeekBar.start();

    }

    public String createDuration(int duration) {

        String time = "";
        int min = duration / 1000 / 60;
        int sec = duration / 1000 % 60;

        time = time + min + ":";

        if (sec < 10) {

            time += "0";

        }
        time += sec;

        return time;
    }

    public void showVisualizer() {
        //Extracting and Setting the current media id to the Visualizer
        int audioSessionId = mediaPlayer.getAudioSessionId();
        if (audioSessionId != -1) {
            barVisualizer.setAudioSessionId(audioSessionId);
        }
    }

    //Releasing the BarVisualizer on Closing the Activity
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (barVisualizer != null)
            barVisualizer.release();
    }

}