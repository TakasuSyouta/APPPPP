package com.example.myapplication;

import android.graphics.Rect;

public class Title extends GameObject {
    protected int PosX=0;
    protected int PosY=0;

    protected String imageName;
    public void Draw(){App.Get().ImageMgr().Draw(imageName,PosX,PosY);}

    //タイトルロゴ
    private float Expansion=1.0f;
    private boolean ExpansionFlg=false;
    private void ExpansionUpdate(){
        if(ExpansionFlg){
            Expansion+=0.01f;
            if(Expansion>1.1f){
                ExpansionFlg=false;
            }
        }
        if(!ExpansionFlg){
            Expansion-=0.01f;
            if(Expansion<0.9f){
                ExpansionFlg=true;
            }
        }
    }

    private int TitlePosX=320/2;
    private int TitlePosY=64+32;
    private String TitleimageName="タイトル.png";
    public void DrawTitle(){App.Get().ImageMgr().Draw(TitleimageName,TitlePosX,TitlePosY,Expansion,Expansion,0);}


    //スタートボタン
    private int StartPosX=320/2;
    private int StartPosY=64*4;
    private Rect StartArea=new Rect();
    private boolean TouchFlg=false;
    public boolean HitCheck(int x,int y){return StartArea.contains(x,y);}
    private String StartimageName="スタート.png";
    public void DrawStart(){App.Get().ImageMgr().Draw(StartimageName,StartPosX,StartPosY);}
    public void GetStringStart(String name){StartimageName=name;}

    //あそびかた
    private int PlayrPosX=80;
    private int PlayrPosY=64*6;
    private Rect PlayrArea=new Rect();
    public boolean HitCheck_P(int x,int y){return PlayrArea.contains(x,y);}
    private String PlayrimageName="あそびかた.png";
    public void DrawPlayr(){App.Get().ImageMgr().Draw(PlayrimageName,PlayrPosX,PlayrPosY);}


    //クレジット
    private int CreditPosX=320/2+80;
    private int CreditPosY=64*6;
    private Rect CreditArea=new Rect();
    public boolean HitCheck_C(int x,int y){return CreditArea.contains(x,y);}
    private String CreditimageName="クレジット.png";
    public void DrawCredit(){App.Get().ImageMgr().Draw(CreditimageName,CreditPosX,CreditPosY);}


    public  void TitleMainDraw(){
        DrawStart();
        DrawTitle();
        DrawPlayr();
        DrawCredit();
    }

    public void UpDate(){
        ExpansionUpdate();

    StartArea.left=StartPosX-192/2;
    StartArea.right=StartPosX+192/2;
    StartArea.top=StartPosY-32;
    StartArea.bottom=StartPosY+32;
    }

    public void TouchDOWN(){
        StartimageName="スタート反転.png";
    }

    public void TouchNotDOWN(){
        StartimageName="スタート.png";
    }

    private void TouchProc() {
        Pointer p = App.Get().TouchMgr().GetTouch();
        if (p == null) {return;}//早期リターン
        if (p.OnDown()){
            if (HitCheck(p.GetDownPos().x, p.GetDownPos().y)) {
                StartimageName="スタート反転.png";
            }
        }
        if (p.OnUp()) {
            if (HitCheck(p.GetDownPos().x, p.GetDownPos().y)) {

            }
        }
    }

}
