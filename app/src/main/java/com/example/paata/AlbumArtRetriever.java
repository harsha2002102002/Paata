package com.example.paata;

import android.media.MediaMetadataRetriever;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;

public class AlbumArtRetriever {

    public static Bitmap getAlbumArt(String filePath) throws IOException {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(filePath);

        byte[] albumArtBytes = retriever.getEmbeddedPicture();
        Bitmap albumArt = null;

        if (albumArtBytes != null) {
            albumArt = BitmapFactory.decodeByteArray(albumArtBytes, 0, albumArtBytes.length);
        }

        retriever.release();
        return albumArt;
    }
}

