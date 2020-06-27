package com.example.myapplication;

import java.util.ArrayList;
import java.util.List;

public class Time extends GameObject{

    private int GameCount=100;
    private String TenRank="..数字/ゼロ.png";
    private String OneRank="./数字/ゼロ.png";

    private  int TitleBarY=128+16;


    private ArrayList<TimePlus> TimePluses=new ArrayList<>();
    public ArrayList<TimePlus> GetTimePlus(){return TimePluses;}


    private float BarScale= 1.0f;
    private float ZureX=0;
    //-------------------------------


    //ゲーム開始準備カウント^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    int StartTimeX=320/2;
    int StartTimeY=300;
    boolean StartFlg=true;
    private String StartTimeName="数字/サン.png";
    private String BlackBackName="ブラックバック";

    //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    public void Time_3(){if(StartFlg){StartTimeName="数値＿３.png";BlackBackName="ブラックバック.png";}}
    public void Time_2(){if(StartFlg){StartTimeName="数値＿２.png";BlackBackName="ブラックバック_70.png";}}
    public void Time_1(){if(StartFlg){StartTimeName="数値＿１.png";BlackBackName="ブラックバック_40.png";}}
    public void Time_Start(){if(StartFlg){StartTimeName="スタート＿ゲーム.png";BlackBackName="ブラックバック_20.png";}}
    public void Time_FlgDown(){StartFlg=false;}

    public boolean Time_Return(){return StartFlg;}

    public void TimeDraw() {
        if (StartFlg) {
            App.Get().ImageMgr().Draw(BlackBackName, 0,0);
            App.Get().ImageMgr().Draw(StartTimeName, StartTimeX, StartTimeY);

        }
    }
    //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

    public void Start(){
        imageName="タイム.png";
        TenRank="数字/キュウ.png";
        OneRank="数字/キュウ.png";

        BarScale= 1.0f;
        ZureX=0;

        PosX=96;
        PosY=100;
        GameCount=99;

        StartFlg=true;
    }

    public void Reset(){
        BarScale= 1.0f;
        ZureX=0;
        GameCount=99;
        TenRank="数字/キュウ.png";
        OneRank="数字/キュウ.png";
        TimePluses.clear();
        StartTimeName="数値＿３.png";
        BlackBackName="ブラックバック.png";
        StartFlg=true;
    }

    public  void Update(){

        if(GameCount>0) {
            GameCount--;
            ZureX--;
            ZureX-=0.5f;
        }

        BarScale=(float)GameCount/(float)100;

        TenRank=SetImagename((GameCount/10)%10);
        OneRank=SetImagename(GameCount%10);


    }
    public  void TimePlusUpdate(){
        /*
        for(TimePlus p:TimePluses){
            p.Update();
            if(p.ReturnAlive()){
                TimePluses.remove(p);
            }
        }

         */

        for(int i=0;i<TimePluses.size();i++){
            TimePluses.get(i).Update();
            if(TimePluses.get(i).ReturnAlive()==false){
                TimePluses.remove(i);
            }
        }

    }

    //お題クリアしたらタイムを加算する
    public void TimePlus(){

        TimePlus Tp=new TimePlus();
        TimePluses.add(Tp);
        Tp.Set();

        if(GameCount+5<100) {
            GameCount += 5;
        }
        else{
            GameCount=99;
            TenRank="数字/キュウ.png";
            OneRank="数字/キュウ.png";
        }
        ZureX+=5;
        ZureX+=0.5f;
        ZureX+=0.5f;
        ZureX+=0.5f;
        ZureX+=0.5f;
        ZureX+=0.25f;
        BarScale=(float)GameCount/(float)100;


    }

    private String SetImagename(int Math){

        String SetName;

        switch (Math) {
            case 0:SetName="数字/ゼロ.png";break;
            case 1:SetName="数字/イチ.png";break;
            case 2:SetName="数字/二.png";break;
            case 3:SetName="数字/サン.png";break;
            case 4:SetName="数字/ヨン.png";break;
            case 5:SetName="数字/ゴ.png";break;
            case 6:SetName="数字/ロク.png";break;
            case 7:SetName="数字/ナナ.png";break;
            case 8:SetName="数字/ハチ.png";break;
            case 9:SetName="数字/キュウ.png";break;
            default:SetName="四角.png";break;
        }

        return SetName;
    }

    //ゲーム終了
    public boolean GameOver(){
        boolean s=false;
        if(GameCount==0){
            s=true;
        }

        return s;
    }


    public void CountDraw(){

        App.Get().ImageMgr().Draw("タイムバー＿下地.png",150+10,PosY);
        for(int i=0;i<GameCount;i++) {
            //   App.Get().ImageMgr().Draw("タイムバー＿バー.png", 150 + ZureX + 10, TitleBarY, BarScale, 1.0f, 0);
            App.Get().ImageMgr().Draw("タイムバー＿バー.png", 12+(i*3),PosY);
        }
        App.Get().ImageMgr().Draw("タイムバー＿装飾.png",150+10,PosY);


        App.Get().ImageMgr().Draw(imageName,PosX-32-16,PosY,0.3f,0.3f,0);
        //App.Get().ImageMgr().Draw(TenRank,PosX+64+64+32+16,PosY,0.3f,0.3f,0);
        //App.Get().ImageMgr().Draw(OneRank,PosX+128+64,PosY,0.3f,0.3f,0);



//        App.Get().ImageMgr().Draw(TenRank,PosX+64+64+16,PosY,0.8f,0.8f,0);
  //      App.Get().ImageMgr().Draw(OneRank,PosX+128+64,PosY,0.8f,0.8f,0);

/*
        App.Get().ImageMgr().Draw("タイムバー＿下地.png",150+10,TitleBarY);
        for(int i=0;i<GameCount;i++) {
         //   App.Get().ImageMgr().Draw("タイムバー＿バー.png", 150 + ZureX + 10, TitleBarY, BarScale, 1.0f, 0);
            App.Get().ImageMgr().Draw("タイムバー＿バー.png", 12+(i*3), TitleBarY);
        }
        App.Get().ImageMgr().Draw("タイムバー＿装飾.png",150+10,TitleBarY);


 */


        for(TimePlus p:TimePluses){
            p.CountDraw();
        }
    }


}
