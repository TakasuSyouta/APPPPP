package com.example.myapplication;

import java.util.Random;

public class TimePlus extends GameObject{

    private boolean mIsalive=true;

    public  void Set(){
        Random rnd=new Random();
        int Bure=rnd.nextInt(12);
        PosX = 230+Bure;
        PosY = 32+Bure;
        mIsalive=true;
    }


    public  void Update() {

        //ここのせいで落ちるよ！
    PosY-=2;
    if(PosY<-32) {
        mIsalive = false;
    }
    }

    public boolean ReturnAlive(){return mIsalive;}

    public void CountDraw() {
        App.Get().ImageMgr().Draw("タイム＿プラス.png", PosX, PosY);
    }
}
