package com.example.puzzlefun;

import android.app.WallpaperManager;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_SET_WALLPAPER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button changeWallpaperButton = findViewById(R.id.change_wallpaper_button);
        changeWallpaperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkWallpaperPermission()) {
                    setWallpaper();
                } else {
                    requestWallpaperPermission();
                }
            }
        });
    }

    private boolean checkWallpaperPermission() {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.SET_WALLPAPER)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestWallpaperPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.SET_WALLPAPER},
                REQUEST_SET_WALLPAPER);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_SET_WALLPAPER && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            setWallpaper();
        } else {
            Toast.makeText(this, "Разрешение отклонено", Toast.LENGTH_SHORT).show();
        }
    }

    private void setWallpaper() {
        new Thread(() -> {
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
            try {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.amazingphoto);
                if (bitmap == null) {
                    runOnUiThread(() -> Toast.makeText(this, "Ошибка: изображение не найдено", Toast.LENGTH_SHORT).show());
                    return;
                }
                wallpaperManager.setBitmap(bitmap);
                runOnUiThread(() -> Toast.makeText(this, "Обои успешно изменены!", Toast.LENGTH_SHORT).show());
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Ошибка при смене обоев", Toast.LENGTH_SHORT).show());
            } catch (SecurityException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Нет разрешения на смену обоев", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}