package com.example.musicdemo2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Random;

import static com.example.musicdemo2.AlbumDetailsAdapter.albumFiles;
import static com.example.musicdemo2.MainActivity.musicFiles;
import static com.example.musicdemo2.MainActivity.repeatBool;
import static com.example.musicdemo2.MainActivity.shuffleBool;

public class PlayerActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {


    int c =0;

    TextView song_name,artist_name,duration_played,duration_total;
    ImageView cover_art,nextBtn,prevBtn,backBtn,shuffleBtn,repeatBtn;
    FloatingActionButton playBtn;
    SeekBar seekbar;
    int position = -1;

    static ArrayList<MusicFiles> listSongs = new ArrayList<>();
    static Uri uri;
    static MediaPlayer mediaPlayer;
    private Handler handler = new Handler();

    private Thread playThread,prevThread,nextThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initView();
        Log.w("log12", "onCreate:x "+ "i is" +" "+c);
        c++;
        getIntentMethod();
        Log.w("log13", "onCreate:x "+ "i is" +" "+c);
        c++;
        song_name.setText(listSongs.get(position).getTitle());
        artist_name.setText(listSongs.get(position).getArtist());
        mediaPlayer.setOnCompletionListener(this);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer !=null && fromUser)
                {
                    mediaPlayer.seekTo(progress*1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer !=null)
                {
                    Log.w("log14", "onCreate:x "+ "i is"  +" "+c);
                    c++;
                    int mCurrentPosition = mediaPlayer.getCurrentPosition()/1000;
                    seekbar.setProgress(mCurrentPosition);
                    duration_played.setText(formattedTime(mCurrentPosition));
                    int duration=mediaPlayer.getDuration()/1000;
                    duration_total.setText(formattedTime(duration));
                }
                handler.postDelayed(this,1000);
            }
        });
        shuffleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shuffleBool)
                {
                    shuffleBool = false;
                    shuffleBtn.setImageResource(R.drawable.ic_shuffle_off);
                }
                else
                {

                    shuffleBool = true;
                    shuffleBtn.setImageResource(R.drawable.ic_shuffle_on);
                }
            }
        });
        repeatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("log15", "onCreate:x "+ "i is" +" "+c);
                c++;
                if(repeatBool)
                {
                    repeatBool = false;
                    repeatBtn.setImageResource(R.drawable.ic_repeat_off);
                }
                else
                {

                    repeatBool = true;
                    repeatBtn.setImageResource(R.drawable.ic_repeat_on);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        Log.w("log16", "onCreate:x "+ "i is"  +" "+c);
        c++;
        playThreadBtn();
        nextThreadBtn();
        prevThreadBtn();
        super.onResume();

    }

    private void prevThreadBtn() {
        Log.w("log17", "onCreate:x "+ "i is"  +" "+c);
        c++;
        prevThread = new Thread(){
            @Override
            public void run() {
                super.run();
                Log.w("log18", "onCreate:x "+ "i is"  +" "+c);
                c++;
                prevBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        prevBtnClicked();
                    }
                });
            }};
        prevThread.start();
    }

    private void prevBtnClicked() {
        Log.w("log19", "onCreate:x "+ "i is" +" "+c);
        c++;
        if(mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            Log.w("log20", "onCreate:x "+ "i is"  +" "+c);
            c++;
            if(shuffleBool && !repeatBool)
            {
                position = getRandom(listSongs.size()-1);
            }
            else if(!shuffleBool && !repeatBool)
            {
                position = ((position-1)<0)?(listSongs.size()-1):(position-1);
            }
            //else -> position is same


            Log.w("log21", "onCreate:x "+ "i is" +" "+c);
            c++;
            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            metaData(uri);
            song_name.setText(listSongs.get(position).getTitle());
            artist_name.setText(listSongs.get(position).getArtist());seekbar.setMax(mediaPlayer.getDuration()/1000);

            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer !=null)
                    {
                        Log.w("log22", "onCreate:x "+ "i is" +" "+c);
                        c++;
                        int mCurrentPosition = mediaPlayer.getCurrentPosition()/1000;
                        seekbar.setProgress(mCurrentPosition);
                        duration_played.setText(formattedTime(mCurrentPosition));
                        int duration=mediaPlayer.getDuration()/1000;
                        duration_total.setText(formattedTime(duration));
                    }
                    handler.postDelayed(this,1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            playBtn.setBackgroundResource(R.drawable.ic_pause);
            mediaPlayer.start();


        }
        else {
            Log.w("log23", "onCreate:x "+ "i is"  +" "+c);
            c++;
            mediaPlayer.stop();
            mediaPlayer.release();

            if(shuffleBool && !repeatBool)
            {
                position = getRandom(listSongs.size()-1);
            }
            else if(!shuffleBool && !repeatBool)
            {
                position = ((position-1)<0)?(listSongs.size()-1):(position-1);
            }
            //else -> position is

            Log.w("log24", "onCreate:x "+ "i is"  +" "+c);
            c++;
            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            metaData(uri);
            song_name.setText(listSongs.get(position).getTitle());
            artist_name.setText(listSongs.get(position).getArtist());seekbar.setMax(mediaPlayer.getDuration()/1000);

            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer !=null)
                    {
                        Log.w("log25", "onCreate:x "+ "i is" +" "+c);
                        c++;
                        int mCurrentPosition = mediaPlayer.getCurrentPosition()/1000;
                        seekbar.setProgress(mCurrentPosition);
                        duration_played.setText(formattedTime(mCurrentPosition));
                        int duration=mediaPlayer.getDuration()/1000;
                        duration_total.setText(formattedTime(duration));
                    }
                    handler.postDelayed(this,1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);

            playBtn.setBackgroundResource(R.drawable.ic_play);
        }
    }

    private void nextThreadBtn() {
        Log.w("log26", "onCreate:x "+ "i is"  +" "+c);
        c++;
        nextThread = new Thread(){
            @Override
            public void run() {
                super.run();
                Log.w("log27", "onCreate:x "+ "i is"  +" "+c);
                c++;
                nextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nextBtnClicked();
                    }
                });
            }};
        nextThread.start();
    }

    private void nextBtnClicked() {
        Log.w("log28", "onCreate:x "+ "i is"  +" "+c);
        c++;
        if(mediaPlayer.isPlaying())
        {
            Log.w("log29", "onCreate:x "+ "i is"  +" "+c);
            c++;
            mediaPlayer.stop();
            mediaPlayer.release();
            if(shuffleBool && !repeatBool)
            {
                position = getRandom(listSongs.size()-1);
            }
            else if(!shuffleBool && !repeatBool)
            {
            position = (position+1)%(listSongs.size());
            }
            //else -> position is same
            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            metaData(uri);
            song_name.setText(listSongs.get(position).getTitle());
            artist_name.setText(listSongs.get(position).getArtist());seekbar.setMax(mediaPlayer.getDuration()/1000);

            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.w("log30", "onCreate:x "+ "i is"  +" "+c);
                    c++;
                    if(mediaPlayer !=null)
                    {
                        Log.w("log31", "onCreate:x "+ "i is"  +" "+c);
                        c++;
                        int mCurrentPosition = mediaPlayer.getCurrentPosition()/1000;
                        seekbar.setProgress(mCurrentPosition);
                        duration_played.setText(formattedTime(mCurrentPosition));
                        int duration=mediaPlayer.getDuration()/1000;
                        duration_total.setText(formattedTime(duration));
                    }
                    handler.postDelayed(this,1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            playBtn.setBackgroundResource(R.drawable.ic_pause);
            mediaPlayer.start();


        }
        else {
            Log.w("log32", "onCreate:x "+ "i is"  +" "+c);
            c++;
            mediaPlayer.stop();
            mediaPlayer.release();

            if(shuffleBool && !repeatBool)
            {
                position = getRandom(listSongs.size()-1);
            }
            else if(!shuffleBool && !repeatBool)
            {
                position = (position+1)%(listSongs.size());
            }
            //else -> position is same
            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            metaData(uri);
            song_name.setText(listSongs.get(position).getTitle());
            artist_name.setText(listSongs.get(position).getArtist());seekbar.setMax(mediaPlayer.getDuration()/1000);

            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer !=null)
                    {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition()/1000;
                        seekbar.setProgress(mCurrentPosition);
                        duration_played.setText(formattedTime(mCurrentPosition));
                        int duration=mediaPlayer.getDuration()/1000;
                        duration_total.setText(formattedTime(duration));
                    }
                    handler.postDelayed(this,1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            playBtn.setBackgroundResource(R.drawable.ic_play);
        }
    }

    private int getRandom(int i) {
        Random random = new Random();

        return random.nextInt(i+1);
    }

    private void playThreadBtn() {
        Log.w("log1", "onCreate:x "+ "i is"  +" "+c);
        c++;
        playThread = new Thread(){
            @Override
            public void run() {
                super.run();
                Log.w("log2", "onCreate:x "+ "i is"  +" "+c);
                c++;
                playBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playBtnClicked();
                    }
                });
        }};
        playThread.start();
    }

    private void playBtnClicked() {
        Log.w("log3", "onCreate:x "+ "i is" +" "+c);
        c++;
        if(mediaPlayer.isPlaying())
        {
            Log.w("log4", "onCreate:x "+ "i is"  +" "+c);
            c++;
            playBtn.setImageResource(R.drawable.ic_play);
            mediaPlayer.pause();
            seekbar.setMax(mediaPlayer.getDuration()/1000);

            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.w("log5", "onCreate:x "+ "i is"  +" "+c);
                    c++;
                    if(mediaPlayer !=null)
                    {Log.w("log6", "onCreate:x "+ "i is" +" "+c);
                        c++;
                        int mCurrentPosition = mediaPlayer.getCurrentPosition()/1000;
                        seekbar.setProgress(mCurrentPosition);
                        duration_played.setText(formattedTime(mCurrentPosition));
                        int duration=mediaPlayer.getDuration()/1000;
                        duration_total.setText(formattedTime(duration));
                    }
                    handler.postDelayed(this,1000);
                }
            });
        }
        else
        {
            Log.w("7", "onCreate:x "+ "i is"  +" "+c);
            c++;
            playBtn.setImageResource(R.drawable.ic_pause);
            mediaPlayer.start();
            seekbar.setMax(mediaPlayer.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.w("log8", "onCreate:x "+ "i is"  +" "+c);
                    c++;
                    if(mediaPlayer !=null)
                    {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition()/1000;
                        seekbar.setProgress(mCurrentPosition);
                        duration_played.setText(formattedTime(mCurrentPosition));
                        int duration=mediaPlayer.getDuration()/1000;
                        duration_total.setText(formattedTime(duration));
                    }
                    handler.postDelayed(this,1000);
                }
            });

        }
    }

    private String formattedTime(int mCurrentPosition) {
        String totalOut,totalNew;
        String minutes = String.valueOf(mCurrentPosition/60);
        String seconds = String.valueOf(mCurrentPosition%60);
        totalOut = minutes + ":" + seconds;
        totalNew = minutes + ":0"+seconds;
        if(seconds.length()==1)
        {
            return totalNew;
        }
        else
        {
            return totalOut;
        }

    }

    private void getIntentMethod() {
    position = getIntent().getIntExtra("position",-1);
    String sender = getIntent().getStringExtra("sender");
    if(sender!=null && sender.equals("albumDetails"))
    {
        listSongs = albumFiles;
    }
    else {
        listSongs = musicFiles;
    }

    Log.v("sdf"+uri,"fsd"+musicFiles);

    if(listSongs != null)
    {
        playBtn.setImageResource(R.drawable.ic_pause);
        uri = Uri.parse(listSongs.get(position).getPath());
    }
    if(mediaPlayer !=null)
    {
        mediaPlayer.stop();
        mediaPlayer.release();
    }
        mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
        mediaPlayer.start();

        seekbar.setMax(mediaPlayer.getDuration()/1000);
        metaData(uri);
    }

    private void initView(){
        song_name = findViewById(R.id.song_name);
        artist_name = findViewById(R.id.song_artist);
        duration_played = findViewById(R.id.duration_played);
        duration_total = findViewById(R.id.duration_total);
        cover_art = findViewById(R.id.cover_art);
        nextBtn = findViewById(R.id.next_btn);
        prevBtn = findViewById(R.id.prev_btn);
      //  backBtn = findViewById(R.id.back_btn);
        shuffleBtn = findViewById(R.id.shuffle_btn);
        repeatBtn = findViewById(R.id.repeat);
        playBtn = findViewById(R.id.play_pause);
        seekbar = findViewById(R.id.seeBar);
    }

    private void metaData(Uri uri) {
    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
    retriever.setDataSource(uri.toString());
    byte[] art = retriever.getEmbeddedPicture();

        Bitmap bitmap ;

    if(art!=null)
    {
        Glide.with(this)
                .asBitmap()
                .load(art)
                .into(cover_art);
                    // Palette API
//        bitmap = BitmapFactory.decodeByteArray(art,0,art.length);
//        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
//            @Override
//            public void onGenerated(@Nullable Palette palette) {
//                Palette.Swatch swatch = palette.getDominantSwatch();
//                if(swatch != null)
//                {
//                    ImageView gradient = findViewById(R.id.imageViewGradient);
//                    RelativeLayout mContainer = findViewById(R.id.mContainer);
//                    gradient.setBackgroundResource(R.drawable.gradient_bg);
//                    mContainer.setBackgroundResource(R.drawable.main_bg);
//                    GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[] {swatch.getRgb(),0x00000000});
//                    gradient.setBackground(gradientDrawable);
//                    GradientDrawable gradientDrawableBg = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[] {swatch.getRgb()});
//                    mContainer.setBackground(gradientDrawableBg);
//                    song_name.setTextColor(swatch.getTitleTextColor());
//                    artist_name.setTextColor(swatch.getBodyTextColor());
//                }
//                else
//                {
//                    ImageView gradient = findViewById(R.id.imageViewGradient);
//                    RelativeLayout mContainer = findViewById(R.id.mContainer);
//                    gradient.setBackgroundResource(R.drawable.gradient_bg);
//                    mContainer.setBackgroundResource(R.drawable.main_bg);
//                    GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[] {0xff000000,0x00000000});
//                    gradient.setBackground(gradientDrawable);
//                    GradientDrawable gradientDrawableBg = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[] {0xff000000,0xff000000});
//                    mContainer.setBackground(gradientDrawableBg);
//                    song_name.setTextColor(Color.WHITE);
//                    artist_name.setTextColor(Color.DKGRAY);
//                }
//
//            }
//        });
    }
    else
    {
        Glide.with(this)
                .asBitmap()
                .load(R.drawable.music)
                .into(cover_art);
        //                    ImageView gradient = findViewById(R.id.imageViewGradient);
        //                    RelativeLayout mContainer = findViewById(R.id.mContainer);
        //                    gradient.setBackgroundResource(R.drawable.gradient_bg);
        //                    mContainer.setBackground(gradientDrawableBg);
        //                    song_name.setTextColor(Color.WHITE);
        //                    artist_name.setTextColor(Color.DKGRAY);





    }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.w("log9", "onCreate:x "+ "i is" +" "+c);
        c++;
        nextBtnClicked();
        Log.w("log10", "onCreate:x "+ "i is"  +" "+c);
        c++;
        if(mediaPlayer!=null)
        {
            Log.w("log11", "onCreate:x "+ "i is"  +" "+c);
            c++;
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(this);
        }
    }
}