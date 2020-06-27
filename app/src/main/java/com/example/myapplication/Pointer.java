package com.example.myapplication;


import android.graphics.Point;

import java.util.Vector;

/*
*タッチされた一点分の情報
* ここでは解像度のことは考えずに、渡される時に対応
* Upイベントが走った後すぐ不要通知を行うので処理の順序はゲームの更新→PointerのUpdateにすること
*/
public class Pointer
{
    // 各種設定--------------------------------------------------------------->
    public static final long isLongTouch = 2000;        // 長押しと判別するまでの時間

    // isFlickTime未満のタッチで、かつisFlickDistance以上の移動があった時フリックとみなす
    public static final long isFlickTime = 300;         // フリックとみなす押してから離すまでの時間
    public static final int isFlickDistance = 80;       // フリックとみなすポインターの移動量
    // <---------------------------------------------------------------各種設定

    // 各取得関係-------------------------------------------------------------->
    public boolean OnDown(){return onDown;}     // 押された瞬間だけtrue
    public boolean OnUp(){return onUp;}       // 離された瞬間だけtrue
    public boolean OnFlick(){return onFlick;}   // 離された後フリックの条件を満たしていればtrue

    // 押された瞬間の場所
    public Point GetDownPos()
    {
        Point p = new Point(downPosX, downPosY);
        return p;
    }

    // 離された瞬間の場所
    public Point GetUpPos()
    {
        Point p = new Point(upPosX, upPosY);
        return p;
    }
    // <--------------------------------------------------------------各取得関係



    // 更新関数(タッチイベントが発生していなくても呼び出される)
    // 戻り値：このタッチが不要になったらfalse
    public boolean Update()
    {
        // タッチした瞬間のみtrueになるので、すぐ落とす
        if(onDown){onDown = false;}

        // 長押し期間に入ったか
        touchTime = System.currentTimeMillis()-downTime;
        if(touchTime > isLongTouch){onLongDown = true;}

        // upイベントが発生した後不要通知
        if(onUp){return false;}

        return true;
    }

    // タッチした時間
    private long downTime = System.currentTimeMillis();
    private long upTime = System.currentTimeMillis();
    public long GetTouchTime(){return touchTime;} // タッチしてから離すまでの時間

    // マルチタッチ対応
    private int indxId = 0; // Android側に振られるタッチID
    public void SetID(int id){indxId = id;}
    public int GetID(){return indxId;}

    // タッチ
    private int downPosX = 0;
    private int downPosY = 0;
    private int upPosX = 0;
    private int upPosY = 0;
    private boolean onDown = false;
    private boolean onUp = false;
    public void Down(int _tx, int _ty)
    {
        downPosX = _tx;
        downPosY = _ty;
        nowPosX = _tx;
        nowPosY = _ty;
        downTime = System.currentTimeMillis(); // タッチした瞬間の時間を覚える
        onDown = true;
    }
    public void Up(int _tx, int _ty)
    {
        upPosX = _tx;
        upPosY = _ty;
        upTime = System.currentTimeMillis(); // 上げた瞬間の時間を覚える

        // フリック判定
        if(GetTouchTime()<isFlickTime && GetDtoU().Length()>=isFlickDistance){onFlick=true;}
        onUp = true;
    }





    // 長押し(リスナーに居るらしいけど時間をこっちで調整したいので自前で)
    private boolean onLongDown = false;
    private long touchTime = 0;
    public boolean IsLongTouch(){return onLongDown;}

    // フリック入力
    private boolean onFlick = false;
    // 押したところから上げたところまでのベクトル
    public Vector2 GetDtoU()
    {
        Vector2 v1 = new Vector2(downPosX, downPosY);
        Vector2 v2 = new Vector2(upPosX, upPosY);
        return Vector2.To(v1, v2);
    }

    // スライド
    private int nowPosX = 0;
    private int nowPosY = 0;
    public Point GetNowPos()
    {
        Point p = new Point(nowPosX, nowPosY);
        return p;
    }
    Vector<Point> moveMem = new Vector<>();
    public void Move(int _x, int _y)
    {
        nowPosX = _x;
        nowPosY = _y;
        Point p = new Point(_x, _y);
        moveMem.add(p);
    }
    public Vector<Point> GetMoveList(){return moveMem;}
}
