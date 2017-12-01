package hu.ait.apod;

import android.app.Dialog;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnLongClick;
import hu.ait.apod.image.TouchImageView;

public class ImageActivity extends AppCompatActivity {

    @BindView(R.id.imgFull)
    TouchImageView imgFull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        ButterKnife.bind(this);

        if (getIntent() != null) {
            String imgUrl = getIntent().getStringExtra(MainActivity.IMG_URL);
            Glide.with(ImageActivity.this).load(imgUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    imgFull.setImageBitmap(resource);
                }
            });

            /*Glide.with(ImageActivity.this)
                    .load(imgUrl)
                    .into(wallpaperImage);*/
        }
    }

    @OnLongClick(R.id.imgFull)
    public boolean imgLongClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Set Wallpaper?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        imgFull.buildDrawingCache();
                        Bitmap bmp = imgFull.getDrawingCache();

                        WallpaperManager wm = WallpaperManager.getInstance(ImageActivity.this);
                        try {
                            wm.setBitmap(bmp);
                        } catch (IOException e) {
                            Toast.makeText(ImageActivity.this,
                                    "There was a problem setting the wallpaper",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

        return true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}
