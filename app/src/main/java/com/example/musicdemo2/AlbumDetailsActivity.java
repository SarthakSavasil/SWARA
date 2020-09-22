package com.example.musicdemo2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static com.example.musicdemo2.MainActivity.musicFiles;

public class AlbumDetailsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView albumPhoto;
    String albumName;
    ArrayList<MusicFiles> albumSongs = new ArrayList<>();
    AlbumDetailsAdapter albumDetailsAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_details);
        recyclerView = findViewById(R.id.recyclerview);
        albumPhoto = findViewById(R.id.albumPhoto);
        albumName = getIntent().getStringExtra("albumName");
        int j=0;
        for (int i = 0; i <musicFiles.size() ; i++)
        {
            assert albumName != null;
            if(albumName.equals(musicFiles.get(i).getAlbum()))
            {
                albumSongs.add(j, musicFiles.get(i));
                j++;
            }
        }

        byte[] image = getAlbumArt(albumSongs.get(0).getPath());
        if(image != null)
        {
            Glide.with(this)
                    .load(image)
                    .into(albumPhoto);
        }
        else
        {

            Glide.with(this)
                    .load(R.drawable.music)
                    .into(albumPhoto);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!(albumSongs.size()<1))
        {
            albumDetailsAdapter = new AlbumDetailsAdapter(this,albumSongs);
            recyclerView.setAdapter(albumDetailsAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));

        }
    }

    private byte[] getAlbumArt(String uri)
    {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }
}