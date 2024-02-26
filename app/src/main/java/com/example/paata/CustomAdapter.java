package com.example.paata;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.paata.AlbumArtRetriever;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<String> {
    private Context context;
    private ArrayList<String> musicFilesList;
    private ArrayList<String> pathFilesList;

    public CustomAdapter(Context context, ArrayList<String> musicFilesList, ArrayList<String> pathFilesList) {
        super(context, 0, musicFilesList);
        this.context = context;
        this.musicFilesList = musicFilesList;
        this.pathFilesList = pathFilesList;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adopter, parent, false);
        }

        TextView titleTextView = convertView.findViewById(R.id.text);
        titleTextView.setText(musicFilesList.get(position));

        String currentFilePath = pathFilesList.get(position);
        Bitmap albumArt = null;
        try {
            albumArt = AlbumArtRetriever.getAlbumArt(currentFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ImageView phot = convertView.findViewById(R.id.imagetana);
        if (albumArt != null) {
            phot.setImageBitmap(albumArt);
        } else {
            // Set a default image or hide the ImageView
            phot.setImageResource(R.drawable.music2);
        }
        return convertView;
    }
}


