package com.example.myapplication;


import android.graphics.Point;

/*
 ベクトル計算クラス
 */
public class Vector2 {
    public float x = 0;
    public float y = 0;

    // このベクトルに対する処理----------------------------------------------->
    public Vector2(){}
    public Vector2(float _x, float _y)
    {
        x = _x;
        y = _y;
    }

    // ベクトルの加算
    public void Add(Vector2 va)
    {
        x += va.x;
        y += va.y;
    }
    public void Clear(){x = y = 0;}

    // 自分のベクトルの長さを返す
    public float Length(){return Distance(Zero, this);}

    // 正規化
    public void Nomalize()
    {
        float len = Length();
        x = x/len;
        y = y/len;
    }

    // ベクトルの計算---------------------------------------------------->
    // 二点間の距離を求める
    public static float Distance(Vector2 p1, Vector2 p2)
    {
        return (float) Math.sqrt( (p1.x-p2.x)*(p1.x-p2.x) + (p1.y-p2.y)*(p1.y-p2.y) );
    }
    public static float Distance(float p1x,float p1y, float p2x,float p2y)
    {
        return (float) Math.sqrt( (p1x-p2x)*(p1x-p2x) + (p1y-p2y)*(p1y-p2y) );
    }

    // v1→v2ベクトルを返す
    public static Vector2 To(Vector2 v1, Vector2 v2) {
        return new Vector2(v2.x - v1.x, v2.y - v1.y);
    }

    public static final Vector2 Zero = new Vector2(0,0);
}
