package com.example.myapplication;

import android.graphics.Rect;
import android.text.method.Touch;

public class Arrow {

    private boolean aliva=true;
    public boolean IsAlive(){return  aliva;}

    private  boolean TouchCheck=false;

    //撃ったキャラ(enum)
    enum OwnerID{
        Player,
        Enemy,
    }
    //int a=Player;この形はできない
    private OwnerID owner=OwnerID.Player;
    public void SetOwner(OwnerID id){owner=id;}

    private int PosX = 320;
    private int PosY = 600;

    public void SetPos(int x, int y) {
        PosX = x;
        PosY = y;
    }

    //キャラの画像
    private String imageName="ball.png";
    public  void SetImageName(String img){imageName=img;}

    private Rect hitArea = new Rect();
    //--------------------------------------------------------------------------------

    //--------------------------------------------------------------------------------
    public Rect GetHitArea() {
        return hitArea;
    }
    //当たり判定（四角形）
    public boolean HitCheck(Rect r) { return hitArea.intersect(r); }

    public void Update() {
        //Rect更新
        hitArea.left = PosX - 32;
        hitArea.top = PosY - 32;
        hitArea.right = PosX + 32;
        hitArea.bottom = PosY + 32;


    }

    public void SelectDraw(int ix,int iy,int ix_max,int iy_max) {
        if (iy != 0) {
            App.Get().ImageMgr().Draw("矢印.png", PosX, PosY - 32);
        }//上
        if (iy != iy_max - 1) {
            App.Get().ImageMgr().Draw("矢印.png", PosX, PosY + 32, 180);
        }//下
        if (ix != ix_max - 1) {
            App.Get().ImageMgr().Draw("矢印.png", PosX + 32, PosY, 90);
        }//右
        if (ix != 0) {
            App.Get().ImageMgr().Draw("矢印.png", PosX - 32, PosY, 270);
        }//左
    }

    private void TouchProc() {
        Pointer p = App.Get().TouchMgr().GetTouch();
        if (p == null) {return;}//早期リターン
        if (p.OnDown()) { }
        if (p.IsLongTouch()) {}
        if (p.OnUp()) {}
        }

    public void SelectUpdate(boolean TouchFlg,int ix,int iy,int ix_max,int iy_max){
        if(TouchFlg){

        }
        /*
            if(iy!=0) {App.Get().ImageMgr().Draw("矢印.png", PosX, PosY-32);}//上
            if(iy!=iy_max-1) {App.Get().ImageMgr().Draw("矢印.png", PosX, PosY+32,180);}//下
            if(ix!=ix_max-1) {App.Get().ImageMgr().Draw("矢印.png", PosX+32, PosY,90);}//右
            if(ix!=0) {App.Get().ImageMgr().Draw("矢印.png", PosX-32, PosY,270);}//左

         */
    }
}
