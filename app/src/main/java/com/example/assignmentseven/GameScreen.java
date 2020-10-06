package com.example.assignmentseven;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.gesture.Gesture;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;

public class GameScreen extends AppCompatActivity {

    String TAG = "TAG_GESTURE";
    Boolean fling = false;
    Boolean flinging = false;
    float startX = 0;
    float startY = 0;
    float endX = 0;
    float endY = 0;
    float currX = 0;
    float currY = 0;
    float moveX = 0;
    float moveY = 0;
    float vX = 0;
    float vY = 0;
    float velocity = 0;
    int direction = 1;
    Random rand = new Random();
    boolean start = true;
    int ranX = 0;
    TextView textView;
    //Circle[] circleArray;

    public class GraphicsView extends View {

        private GestureDetector gestureDetector;
        Paint paint1 = new Paint();
        Paint paint2 = new Paint();

        public GraphicsView(Context context) {
            super(context);
            gestureDetector = new GestureDetector(context, new MyGestureListener());

            paint1.setColor(getColor(R.color.colorPrimaryDark));
            paint2.setColor(getColor(R.color.colorAccent));
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            if (start == true)
            {
                ranX = rand.nextInt(canvas.getWidth()-50) + 25;
                start = false;
            }
            else
            {
                canvas.drawCircle(ranX, 80, 50, paint2);
            }

            if (fling == true)
            {
                canvas.drawCircle(startX, startY, 50, paint1);
            }

            currX = startX - moveX;
            currY = startY - moveY;

            //Check if ball has hit target
            if (currX > (ranX-50) && currX < (ranX + 50) && currY > 30 && currY < 130)
            {
                //Change value of score
                textView.setText("Score: 1");
                flinging = false;
            }

            else if (flinging == true)
            {
                //Check that ball is not on sides
                if ((currX-(startX-endX) * (velocity / 30000) * direction) < 25)
                {
                    direction = direction * -1;
                    currX = 25;
                }
                else if ((currX-(startX-endX) * (velocity / 30000) * direction) > (canvas.getWidth() - 25))
                {
                    direction = direction * - 1;
                    currX = canvas.getWidth() - 25;
                }

                currX -= (startX-endX) * (velocity / 20000) * direction;
                currY -= (startY-endY) * (velocity / 20000);

                moveX = startX - currX;
                moveY = startY - currY;

                canvas.drawCircle(currX, currY, 50, paint1);
            }
            invalidate();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {

            if (gestureDetector.onTouchEvent(event)) {
                return true;
            }
            return super.onTouchEvent(event);
        }

        class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

            @Override
            public boolean onDown(MotionEvent e) {
                Log.i(TAG, "onDOWN");
                if (flinging == false)
                {
                    fling = true;
                    startX = e.getX();
                    startY = e.getY();
                }
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                Log.i(TAG, "onFLING velocity x " + velocityX);
                Log.i(TAG, "onFLING velocity y " + velocityY);
                Log.i(TAG, "onFLING starts at x " + e1.getX() + "," + e1.getY());
                Log.i(TAG, "onFLING finishes at x " + e2.getX() + "," + e2.getY());

                if (flinging == false && fling == true)
                {
                    //Store values of fling
                    //startX = (int)e1.getX();
                    //startY = (int)e1.getY();
                    endX = e2.getX();
                    endY = e2.getY();
                    vX = velocityX * velocityX;
                    vX = (float) Math.sqrt(vX);
                    vY = velocityY * velocityY;
                    vY = (float) Math.sqrt(vY);

                    //Picking the velocity to use
                    if (vX > vY)
                    {
                        velocity = vX;
                    }
                    else
                    {
                        velocity = vY;
                    }

                    flinging = true;
                    fling = false;
                }

                return false;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        textView = (TextView) findViewById(R.id.text_score);

        //Hide the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //Enabling lean back mode
        int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        getWindow().getDecorView().setSystemUiVisibility(uiOptions);

        GraphicsView graphicsView = new GraphicsView(this);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.cl_game);
        linearLayout.addView(graphicsView);
    }
}