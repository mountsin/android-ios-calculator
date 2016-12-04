package com.niv.example.calcaulator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

public class SecondActivity extends AppCompatActivity {


    private static float bubbleSize =100f;
    private static float smudgeAmount = 150f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_second);

        LinearLayout ll01 = (LinearLayout)findViewById(R.id.activity_second);

        SampleView sv = new SampleView(this);
        ll01.addView(sv);
    }


    private static class SampleView extends View {
        static int WIDTH = 8; // sections
        static int HEIGHT = 8;
        static int COUNT = (WIDTH + 1) * (HEIGHT + 1); // total verts count

        Bitmap mBitmap; // declaring a bitmap
        float[] matrixVertsMoved = new float[COUNT*2]; // declaring an array with double amount of vert count, one for x and one for y
        float[] matrixOriganal = new float[COUNT*2];

        float clickX;
        float clickY;

        static void setXY(float[] array, int index, float x, float y) {
            array[index*2 + 0] = x;
            array[index*2 + 1] = y;
        }

        public SampleView(Context context) {
            super(context);
            setFocusable(true);

            mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.boobs);


            // construct our mesh
            int index = 0;
            for (int y = 0; y <= HEIGHT; y++) {
                float fy = mBitmap.getHeight() * y / HEIGHT;

                for (int x = 0; x <= WIDTH; x++) {
                    float fx = mBitmap.getWidth() * x / WIDTH;
                    setXY(matrixVertsMoved, index, fx, fy);
                    setXY(matrixOriganal, index, fx, fy);
                    index += 1;
                }

            }

        }



        @Override
        protected void onDraw(Canvas canvas) {


            canvas.drawBitmapMesh(mBitmap, WIDTH, HEIGHT, matrixVertsMoved, 0,  null, 0, null);

            Paint p1 = new Paint();
            p1.setColor(0x660000FF);

            Paint p2 = new Paint();
            p2.setColor(0x99FF0000);

            Paint p3 = new Paint();
            p3.setColor(0xFFFFFB00);

            for (int i = 0; i < COUNT*2; i += 2) {
                float x = matrixOriganal[i+0];
                float y = matrixOriganal[i+1];
                canvas.drawCircle(x, y, 4, p1);

                float x1 = matrixOriganal[i+0];
                float y1 = matrixOriganal[i+1];
                float x2 = matrixVertsMoved[i+0];
                float y2 = matrixVertsMoved[i+1];
                canvas.drawLine(x1, y1, x2, y2, p1);
            }

            for (int i = 0; i < COUNT*2; i += 2) {
                float x = matrixVertsMoved[i+0];
                float y = matrixVertsMoved[i+1];
                canvas.drawCircle(x, y, 4, p2);
            }

            canvas.drawCircle(clickX, clickY, 6, p3);


        }

        private void smudge2() {


            for (int i = 0; i < COUNT*2; i += 2) {

                float xOriginal = matrixOriganal[i+0];
                float yOriginal = matrixOriganal[i+1];

                float distX = Math.abs(clickX-xOriginal);
                float distY = Math.abs(clickY-yOriginal);

                float dist = (float)Math.sqrt( distX*distX + distY*distY );

                float coof = ( bubbleSize - dist ) / bubbleSize;

                float oc = (float) -Math.sin(coof * 2*Math.PI) * 0.15f ;

                if ( dist < bubbleSize )
                {
                    matrixVertsMoved[i+0] = xOriginal + smudgeAmount * (coof+oc);
                    matrixVertsMoved[i+1] = yOriginal;
                }
                else
                {
                    matrixVertsMoved[i+0] = xOriginal;
                    matrixVertsMoved[i+1] = yOriginal;
                }


            }

            invalidate();
        }

        private void smudge() {


            for (int i = 0; i < COUNT*2; i += 2) {

                float xOriginal = matrixOriganal[i+0];
                float yOriginal = matrixOriganal[i+1];

                float dist_click_to_origin_x = clickX - xOriginal; // distance from current vertex in the original matrix to the place clicked.
                float dist_click_to_origin_y = clickY - yOriginal;

                float kv_kat = dist_click_to_origin_x*dist_click_to_origin_x + dist_click_to_origin_y*dist_click_to_origin_y;

                float pull = ( 1000000 / kv_kat / (float)Math.sqrt(kv_kat) );

                if (pull >= 1) {
                    matrixVertsMoved[i+0] = clickX;
                    matrixVertsMoved[i+1] = clickY;
                } else {
                    matrixVertsMoved[i+0] = xOriginal + dist_click_to_origin_x * pull;
                    matrixVertsMoved[i+1] = yOriginal + dist_click_to_origin_y * pull;
                }

            }


        }




        @Override
        public boolean onTouchEvent(MotionEvent event) {

            clickX = event.getX();
            clickY = event.getY();
            smudge(); // change the matrix.
            invalidate(); // calls a redraw on the canvas.

            return true;
        }



    }
}