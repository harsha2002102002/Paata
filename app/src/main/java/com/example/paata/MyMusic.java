package com.example.paata;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.paata.CustomAdapter;
import com.example.paata.Play;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
public class MyMusic extends AppCompatActivity {
    private static final int READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 101;
    private TextView Songs;
    private TextView Albums;
    private  TextView Folders;
    private ListView listView;
    private ArrayList<String> musicFilesList;
    private ArrayList<String> pathFilesList;
    private CustomAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_music);
        listView = findViewById(R .id.listView);
        getSupportActionBar().hide();

        Intent in = new Intent(MyMusic.this, Play.class);
        musicFilesList = new ArrayList<>();
        pathFilesList = new ArrayList<>();
        adapter = new CustomAdapter(this, musicFilesList,pathFilesList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String currentTitle = getTitle(position);
                String currentPath = getMusicFilePath(position);
                Intent intent = new Intent(MyMusic.this, Play.class);
                intent.putExtra("currentTitle", currentTitle);
                intent.putExtra("currentPath", currentPath);
                intent.putStringArrayListExtra("pathFilesList",pathFilesList);
                intent.putStringArrayListExtra("musicFilesList",musicFilesList);
                intent.putExtra("postion",position);
                startActivity(intent);
            }
        });
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            loadMusicFiles();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.MANAGE_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
            }
        }
    }

    private void loadMusicFiles() {
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
        };

        Cursor cursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                null
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));

                musicFilesList.add(title);
                pathFilesList.add(path);

            }
            cursor.close();
            adapter.notifyDataSetChanged();
        }
    }



    private String getTitle(int position) {
        Cursor cursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.TITLE},
                MediaStore.Audio.Media.IS_MUSIC + " != 0",
                null,
                null
        );

        if (cursor != null) {
            if (cursor.moveToPosition(position)) {
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));

                cursor.close();
                return title;
            }
            cursor.close();
        }

        return null;
    }


    private String getMusicFilePath(int position) {
        Cursor cursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.DATA},
                MediaStore.Audio.Media.IS_MUSIC + " != 0",
                null,
                null
        );

        if (cursor != null) {
            if (cursor.moveToPosition(position)) {
                String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                cursor.close();
                return filePath;
            }
            cursor.close();
        }

        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadMusicFiles();
            } else {
                // Handle permission denied case
                Log.e("MyMusic", "Permission denied");
            }
        }
    }
}
