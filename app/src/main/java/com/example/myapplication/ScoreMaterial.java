package com.example.myapplication;

public class ScoreMaterial {
    public int ScoreNow;
    public String ScoreName;

    public void Set(){
        ScoreNow = 0;
        ScoreName = "数字/ゼロ.png";
    }

    public void Draw(int PosX,int PosY){
        App.Get().ImageMgr().Draw(ScoreName,PosX,PosY,0.4f,0.4f,0);
    }

}
