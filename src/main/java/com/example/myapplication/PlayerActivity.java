package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    Button playbtn,nextbtn,prevbtn;
    TextView songtext,seekstarttxt,seekendtxt;
    SeekBar seekBar;

    String sname;
    public static final String EXTRA_NAME = "song_name";
    static MediaPlayer mediaPlayer;
    int position;
    ArrayList<File> songs;
    Thread updateseekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        prevbtn = findViewById(R.id.prevBtn);
        nextbtn = findViewById(R.id.nextBtn);
        playbtn = findViewById(R.id.playBtn);
        songtext = findViewById(R.id.songtext);
        seekstarttxt = findViewById(R.id.txtStart);
        seekendtxt = findViewById(R.id.txtStop);
        seekBar = findViewById(R.id.seekBar);

        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        songs = (ArrayList) bundle.getParcelableArrayList("songs");
        String songName = i.getStringExtra("songname");
        position = bundle.getInt("pos",0);
        songtext.setSelected(true);
        Uri uri = Uri.parse(songs.get(position).toString());
        sname = songs.get(position).getName();
        songtext.setText(sname);

        mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
        mediaPlayer.start();


        updateseekbar = new Thread(){
            @Override
            public void run() {
                int duration = mediaPlayer.getDuration();
                int currpos = 0;

                while(currpos<duration){
                    try {
                        sleep(500);
                        currpos = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currpos);
                    }
                    catch (InterruptedException | IllegalStateException e){
                        e.printStackTrace();
                    }
                }
            }
        };
        seekBar.setMax(mediaPlayer.getDuration());
        updateseekbar.start();
        seekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.purple_200), PorterDuff.Mode.MULTIPLY);
        seekBar.getThumb().setColorFilter(getResources().getColor(R.color.purple_200),PorterDuff.Mode.SRC_IN);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        String endTime = createTime(mediaPlayer.getDuration());
        seekendtxt.setText(endTime);

        final Handler handler = new Handler();
        final int delay = 1000;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String currentTime = createTime(mediaPlayer.getCurrentPosition());
                seekstarttxt.setText(currentTime);
                handler.postDelayed(this,delay);
            }
        },delay);


        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()){
                    playbtn.setBackgroundResource(R.drawable.play_arrow);
                    mediaPlayer.pause();
                }
                else{
                    playbtn.setBackgroundResource(R.drawable.pause_button);
                    mediaPlayer.start();
                }
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                nextbtn.performClick();
            }
        });

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position = ((position+1)%songs.size());
                Uri u = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),u);
                sname = songs.get(position).getName();
                songtext.setText(sname);
                mediaPlayer.start();
                playbtn.setBackgroundResource(R.drawable.pause_button);
            }
        });

        prevbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position = ((position-1)<0)?(songs.size()-1):(position-1);
                Uri u = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),u);
                sname = songs.get(position).getName();
                songtext.setText(sname);
                mediaPlayer.start();
                playbtn.setBackgroundResource(R.drawable.pause_button);
            }
        });

    }
    public String createTime(int duration){
        String time = "";
        int min = duration/60000;
        int sec = duration/1000%60;
        time+=min+":";

        if(sec<10){
            time+="0";
        }
        time+=sec;
        return time;

    }
}