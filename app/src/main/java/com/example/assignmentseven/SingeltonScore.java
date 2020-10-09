package com.example.assignmentseven;

import android.media.MediaPlayer;

//Class to hold highscore names and scores
public class SingeltonScore
{
    private static SingeltonScore instance;

    public static SingeltonScore getInstance()
    {
        if (instance == null)
        {
            instance = new SingeltonScore();
        }
        return instance;
    }

    private SingeltonScore() {};

    public String name1 = "None";
    public String name2 = "None";
    public String name3 = "None";
    public String name4 = "None";
    public String name5 = "None";

    public int score1 = 0;
    public int score2 = 0;
    public int score3 = 0;
    public int score4 = 0;
    public int score5 = 0;
}
