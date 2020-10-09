package com.example.assignmentseven;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.gesture.Gesture;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    boolean end = false;
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

                float xDif = this.x - current.x;
                float yDif = this.y - current.y;
                float distanceSquared = xDif * xDif + yDif * yDif;
                boolean collision = distanceSquared < (this.radius + current.radius) * (this.radius + current.radius);

                if (collision == true)
                {
                    return current;
                }
            }
            //No collision found so return null
            return null;
        }

        //Check that fling is acceptable
        public boolean checkFling(Canvas canvas)
        {
            if (this.startY > (canvas.getHeight()-this.radius*3) && this.endY > (canvas.getHeight()*0.25))
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        //Move the ball
        public void move(Canvas canvas)
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

            x -= (startX-endX) * (velocity / 50000) * direction;
            y -= (startY-endY) * (velocity / 50000);
        }
    }

    //Creates a class obstacle that extends circle
    public class Obstacle extends Circle
    {
        int direction;

        public Obstacle(float xCoord, float yCoord, int r, Paint p, String e)
        {
            x = xCoord;
            y = yCoord;
            radius = r;
            paint = p;
            effect = e;
            direction = 1;
        }

        //Draw the ball
        public void draw(Canvas canvas)
        {

            canvas.drawCircle(x, y, radius, paint);
        }

        //Randomize the x, y and radius
        public void randomize(Canvas canvas)
        {
            radius = rand.nextInt(51) + 30;
            x = rand.nextInt(canvas.getWidth()-(radius*6)) + (radius * 3);
            y = rand.nextInt(canvas.getHeight()/2 + 1) + canvas.getHeight()/4;
        }

        //Move the obstacle, designed for die obstacle
        public void moveX(Canvas canvas)
        {
            x += 3 * direction;
        }
    }

    //Creates a target class that extends circle
    public class Target extends Circle
    {
        public Target(float xCoord, float yCoord, int r, Paint p)
        {
            x = xCoord;
            y = yCoord;
            radius = r;
            paint = p;
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
            x = rand.nextInt(canvas.getWidth()-(radius*6)) + (radius * 3);
        }
    }

    public class GraphicsView extends View {

        private GestureDetector gestureDetector;
        Paint paintBall = new Paint();
        Paint paintTarget = new Paint();
        Paint paintMinus = new Paint();
        Paint paintDie = new Paint();
        Paint paintSwitch = new Paint();

        //Create new target object
        Target target1 = new Target(0, 80, 75, paintTarget);
        //Create all possible obstacles
        Obstacle minus1 = new Obstacle(0, 0, 0, paintMinus, "Minus");
        Obstacle minus2 = new Obstacle(0, 0, 0, paintMinus, "Minus");
        Obstacle switch1 = new Obstacle(0, 0, 0, paintSwitch, "Switch");
        Obstacle switch2 = new Obstacle(0, 0, 0, paintSwitch, "Switch");
        Obstacle die = new Obstacle(0, 0, 0, paintDie, "Die");

        public GraphicsView(Context context) {
            super(context);
            gestureDetector = new GestureDetector(context, new MyGestureListener());

            paintBall.setColor(getColor(R.color.colorPrimaryDark));
            paintTarget.setColor(getColor(R.color.colorTarget));
            paintMinus.setColor(getColor(R.color.colorMinus));
            paintDie.setColor(getColor(R.color.colorDie));
            paintSwitch.setColor(getColor(R.color.colorSwitch));
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            boolean dieMove = false;

            if (start == true)
            {
                circleList.clear();

                //Randomize x variable
                target1.randomX(canvas);
                minus1.randomize(canvas);
                minus2.randomize(canvas);
                switch1.randomize(canvas);
                switch2.randomize(canvas);
                die.randomize(canvas);
                start = false;

                //Draw obstacles depending on score
                circleList.add(minus1);
                circleList.add(switch1);
                circleList.add(target1);

                target1.radius = 75;

                //Add obstacles and change radius depending on score
                if (score > 5)
                {
                    circleList.add(die);
                    target1.radius -= 10;
                }
                if(score > 10)
                {
                    circleList.add(minus2);
                    target1.radius -= 10;
                }
                if (score > 15)
                {
                    circleList.add(switch2);
                    target1.radius -= 10;
                }

            }
            else
            {
                //Draw circles
                for (int i=0;i<circleList.size();i++)
                {
                    circleList.get(i).draw(canvas);

                    if (circleList.get(i).effect == "Die")
                    {
                        dieMove = true;
                    }
                }

                //Check if we have a moving die obstacle
                if (dieMove == true)
                {
                    //Check that die is not on sides
                    if (die.x < die.radius)
                    {
                        die.direction = die.direction * -1;
                        die.x = die.radius;
                    }
                    else if (die.x > (canvas.getWidth() - 25))
                    {
                        die.direction = die.direction * -1;
                        die.x = canvas.getWidth() - die.radius;
                    }

                    //Move the die
                    die.moveX(canvas);
                }

                if (flinging == true)
                {
                    Ball currentBall = null;
                    //Draw the ball
                    for (int i = 0; i <ballList.size(); i++)
                    {
                        currentBall = ballList.get(i);

                        //Check that fling is acceptable
                        if (currentBall.checkFling(canvas) == true)
                        {
                            currentBall.draw(canvas);
                        }
                        else
                        {
                            Toast.makeText(getContext(), "Fling must start from bottom of screen to the middle", Toast.LENGTH_SHORT).show();
                            //Clear ball list
                            ballList.clear();
                            flinging = false;
                        }
                    }

                    //Check for collisions
                    Circle collideCircle = currentBall.checkCollision();

                    if (collideCircle != null)
                    {
                        //Ball collided with target
                        if (collideCircle.effect == "Target")
                        {
                            //Update score
                            score++;
                            textViewScore.setText("Score: " + score);
                            ballList.clear();
                            flinging = false;
                            start = true;
                        }
                        //Ball collided with minus
                        else if (collideCircle.effect == "Minus")
                        {
                            //Update score
                            score--;
                            textViewScore.setText("Score: " + score);
                            circleList.remove(collideCircle);
                        }
                        //Ball collided with minus
                        else if (collideCircle.effect == "Switch")
                        {
                            //Change direction
                            currentBall.direction = currentBall.direction * -1;
                            circleList.remove(collideCircle);
                        }
                        //Ball collided with die
                        else
                        {
                            end = true;
                        }
                    }

                    //Check that ball has missed target
                    if (currentBall.y < 10)
                    {
                        end = true;
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
                    currentBall.move(canvas);
                }
            }
            if (end == false)
            {
                invalidate();
            }
            else
            {
                flinging = false;
                end = false;
                submitScore();
            }
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
                    float velocity = velocityY * velocityY;
                    velocity = (float) Math.sqrt(velocity);

                    //Create new ball object
                    Ball ball = new Ball(e1.getX(), e1.getY(), 50, paintBall, e2.getX(), e2.getY(), velocity);
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

        //Play music when game starts
        MediaPlayer player = MediaPlayer.create(this, R.raw.bensound_moose);
        player.setLooping(true);
        player.start();

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

    //Go to submitScore activity and pass the current score
    protected void submitScore()
    {
        Intent i = new Intent(this, SubmitScore.class);
        i.putExtra("score", score);
        startActivity(i);
        finish();
    }
}