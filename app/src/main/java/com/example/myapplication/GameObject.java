package com.example.myapplication;

        import android.graphics.Rect;

public class GameObject {
    protected int PosX=0;
    protected int PosY=0;


    //当たり判定に使う矩形
    protected Rect hitArea = new Rect();
    public Rect GetHitArea(){return  hitArea;}
    public boolean HitCheck(int x,int y){
        return hitArea.contains(x,y);
    }




    protected String imageName;
    public void Draw(){App.Get().ImageMgr().Draw(imageName,PosX,PosY);}



}
