package com.example.musicdemo2;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AlbumDetailsAdapter extends RecyclerView.Adapter<AlbumDetailsAdapter.MyAlbumViewHolder> {
    private Context mContext;
    static  ArrayList<MusicFiles> albumFiles;
    View view;
    public AlbumDetailsAdapter(Context mContext, ArrayList<MusicFiles> albumFiles) {
        this.mContext = mContext;
        this.albumFiles = albumFiles;
    }

    @NonNull
    @Override
    public MyAlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.music_items,parent,false);
        return new MyAlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumDetailsAdapter.MyAlbumViewHolder holder, final int position) {
        holder.album_name.setText(albumFiles.get(position).getTitle());
        byte[] image = getAlbumArt(albumFiles.get(position).getPath());

        if(image!=null)
        {
            Glide.with(mContext).asBitmap().load(image).into(holder.album_image);
        }
        else
        {
            Glide.with(mContext).load(R.drawable.music).into(holder.album_image);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,PlayerActivity.class);
                intent.putExtra("sender","albumDetails");
                intent.putExtra("position",position);

                mContext.startActivity(intent);
            }
        });
        }

    @Override
    public int getItemCount() {
        return albumFiles.size();
    }

    public class MyAlbumViewHolder extends RecyclerView.ViewHolder
    {

        TextView album_name;
        ImageView album_image;
        public MyAlbumViewHolder(@NonNull View itemView) {
            super(itemView);

            album_image = itemView.findViewById(R.id.music_img);
            album_name = itemView.findViewById(R.id.music_file_name);
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
