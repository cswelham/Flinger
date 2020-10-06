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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameScreen extends AppCompatActivity {

    String TAG = "TAG_GESTURE";
    Boolean flinging = false;
    int score = 0;
    Random rand = new Random();
    boolean start = true;
    TextView textViewScore;
    List<Circle> circleList = new ArrayList<Circle>();
    List<Ball> ballList = new ArrayList<Ball>();

    //Creates an abstract class circle
    public abstract class Circle
    {
        public float x;
        public float y;
        public int radius;
        public Paint paint;
        public String effect;

        abstract void draw(Canvas canvas);
    }

    //Creates a ball class that extends circle
    public class Ball extends Circle
    {
        public float startX;
        public float startY;
        public float endX;
        public float endY;
        public float velocity;
        public float direction;

        public Ball(float startX, float startY, int radius, Paint paint, float endX, float endY, float velocity)
        {
            this.x = startX;
            this.y = startY;
            this.radius = radius;
            this.paint = paint;
            this.startX = startX;
            this.startY = startY;
            this.endX = endX;
            this.endY = endY;
            this.velocity = velocity;
            direction = 1;
            effect = "Ball";
        }

        //Draw the ball
        public void draw(Canvas canvas)
        {
            canvas.drawCircle(x, y, radius, paint);
        }

        //Check if the ball collides with another object
        public Circle checkCollision()
        {
            //Loop through circles
            for (int i=0; i < circleList.size(); i++)
            {
                Circle current = circleList.get(i);
                if (this.x > (current.x - current.radius) && this.x < (current.x + current.radius) && this.y > (current.y - current.radius) && this.y < (current.y + current.radius))
                {
                    return current;
                }
            }
            //No collision found so return null
            return null;
        }

        //Move the ball
        public void Move(Canvas canvas)
        {
            //Check that ball is not on sides
            if ((x-(startX-endX) * (velocity / 10000) * direction) < radius)
            {
                direction = direction * -1;
                x = radius;
            }
            else if ((x-(startX-endX) * (velocity / 10000) * direction) > (canvas.getWidth() - radius))
            {
                direction = direction * - 1;
                x = canvas.getWidth() - radius;
            }

            x -= (startX-endX) * (velocity / 10000) * direction;
            y -= (startY-endY) * (velocity / 10000);
        }
    }

    //Creates a class obstacle that extends circle
    public class Obstacle extends Circle
    {
        public Obstacle(float x, float y, int radius, Paint paint, String effect)
        {
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.paint = paint;
            this.effect = effect;
        }

        //Draw the ball
        public void draw(Canvas canvas)
        {
            canvas.drawCircle(x, y, radius, paint);
        }
    }

    //Creates a target class that extends circle
    public class Target extends Circle
    {
        public Target(float x, float y, int radius, Paint paint)
        {
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.paint = paint;
            effect = "Target";
        }

        //Draw the ball
        public void draw(Canvas canvas)
        {
            canvas.drawCircle(x, y, radius, paint);
        }

        //Randomize the x coordinate
        public void randomX(Canvas canvas)
        {
            x = rand.nextInt(canvas.getWidth()-(radius*3)) + (radius*3);;
        }
    }

    public class GraphicsView extends View {

        private GestureDetector gestureDetector;
        Paint paint1 = new Paint();
        Paint paint2 = new Paint();
        //Create new target object
        Target target1 = new Target(0, 80, 75, paint2);

        public GraphicsView(Context context) {
            super(context);
            gestureDetector = new GestureDetector(context, new MyGestureListener());

            paint1.setColor(getColor(R.color.colorPrimaryDark));
            paint2.setColor(getColor(R.color.colorAccent));
            circleList.add(target1);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            if (start == true)
            {
                //Randomize x variable
                target1.randomX(canvas);
                start = false;
            }
            else
            {
                //Draw the target
                target1.draw(canvas);

                if (flinging == true)
                {
                    Ball currentBall = null;
                    //Draw the ball
                    for (int i = 0; i <ballList.size(); i++)
                    {
                        currentBall = ballList.get(i);
                        currentBall.draw(canvas);
                    }

                    //Check for collisions
                    Circle collideCircle = currentBall.checkCollision();

                    if (collideCircle != null)
                    {
                        if (collideCircle.effect == "Target")
                        {
                            //Update score
                            score++;
                            textViewScore.setText("Score: " + score);
                            ballList.clear();
                            flinging = false;
                            start = true;
                        }
                    }

                    //Check that ball is not on sides
                    if (currentBall.x < 25)
                    {
                        currentBall.direction = currentBall.direction * -1;
                        currentBall.x = 25;
                    }
                    else if (currentBall.x > (canvas.getWidth() - 25))
                    {
                        currentBall.direction = currentBall.direction * -1;
                        currentBall.x = canvas.getWidth() - 25;
                    }

                    //Move the ball
                    currentBall.Move(canvas);
                }
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
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                Log.i(TAG, "onFLING velocity x " + velocityX);
                Log.i(TAG, "onFLING velocity y " + velocityY);
                Log.i(TAG, "onFLING starts at x " + e1.getX() + "," + e1.getY());
                Log.i(TAG, "onFLING finishes at x " + e2.getX() + "," + e2.getY());

                if (flinging == false)
                {
                    float velocity = 0;
                    float vX = velocityX * velocityX;
                    vX = (float) Math.sqrt(vX);

                    //Create new ball object
                    Ball ball = new Ball(e1.getX(), e1.getY(), 50, paint1, e2.getX(), e2.getY(), vX);
                    //Add ball to ball array
                    ballList.add(ball);

                    flinging = true;
                }

                return false;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        textViewScore = (TextView) findViewById(R.id.text_score);

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