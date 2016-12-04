package com.niv.example.calcaulator;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.ImageReader;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private SurfaceView surfaceView;
    private int width = 250;
    private int height = 250;
    private int[] colors = new int[width * height];
    private boolean fullSize = true;
    private Toast m_toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        surfaceView = (SurfaceView) findViewById(R.id.surface_view);
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                tryDrawing(holder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                tryDrawing(holder);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }

    private void tryDrawing(SurfaceHolder holder) {
        showToast("Trying to draw...");
        Canvas canvas = holder.lockCanvas();
        if (canvas == null) {
            showToast("Cannot draw onto the canvas as it's null");
        } else {
            draw(canvas);
            holder.unlockCanvasAndPost(canvas);
        }
    }

    private void draw(Canvas canvas) {
        showToast("Drawing...");

        if (fullSize) {
            width = canvas.getWidth();
            height = canvas.getHeight();
            colors = new int[width * height];
        }

        Random rnd = new Random();
        for (int i = 0; i < colors.length; i++) {
            int r = rnd.nextInt(256);
            int g = rnd.nextInt(256);
            int b = rnd.nextInt(256);
            colors[i] = Color.rgb(r, g, b);
        }

        //Bitmap workingBitmap = Bitmap.createBitmap(colors, width, height, Bitmap.Config.ARGB_8888);
        Bitmap workingBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.boobs);
        canvas.drawBitmap(workingBitmap, canvas.getWidth() / 2 - width / 2, canvas.getHeight() / 2 - height / 2, null);

    }

    private void showToast(String text) {
        if (m_toast != null) {
            m_toast.cancel();
        }

        m_toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        m_toast.show();
    }



}
