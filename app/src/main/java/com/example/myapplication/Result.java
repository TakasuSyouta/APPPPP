package com.example.myapplication;
public class Result  extends GameObject{



    protected int PosX=320/2;
    protected int PosY=64*4;

    public boolean HitCheck(int X,int Y){return hitArea.contains(X,Y);}

    protected String imagename="タイトル戻る.png";
    public void DrawResult(){App.Get().ImageMgr().Draw(imagename,PosX,PosY);}
    public void ResultMainDraw(){
        DrawResult();
    }

    public void Update(){
        hitArea.left=PosX-192/2;
        hitArea.right=PosX+192/2;
        hitArea.top=PosY-32;
        hitArea.bottom=PosY+32;

    }

    public void TouchDOWN(){
        imagename="タイトル戻る＿押し.png";
    }

    public void TouchUp(){
        imagename="タイトル戻る.png";
    }


}
