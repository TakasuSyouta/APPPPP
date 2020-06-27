package com.example.myapplication;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/*
/ タッチ入力管理者
*/
public class TouchManager implements View.OnTouchListener
{
    public TouchManager()
    {
        // タッチ受け取りクラスに登録(AppにViewを受け渡した後にすること)
        App.Get().GetView().setOnTouchListener(this);
    }

    public void Update()
    {
        if(singleTouch!=null)
        {
            if(singleTouch.Update()==false){
                singleTouch = null;
            }
        }

        if(secondTouch!=null)
        {
            if(secondTouch.Update()==false){
                secondTouch = null;
            }
        }
    }


    public boolean onTouch(View v, MotionEvent event){

        float tx = event.getX()*App.Get().DS();
        float ty = event.getY()*App.Get().DS();

        int pointerIndex = event.getActionIndex();          // 操作されたポインタ番号
        int pointerId = event.getPointerId(pointerIndex);   // 押下されたポインタごとに振られるIDを取得

        switch(event.getAction())
        {
            // 一点目のタッチ
            case MotionEvent.ACTION_DOWN:
                singleTouch = new Pointer();
                singleTouch.Down((int)tx, (int)ty);
                singleTouch.SetID(pointerId);

                break;

            // 二点目以降のタッチ
            case MotionEvent.ACTION_POINTER_DOWN:
                // 二点目以降も来てしまうのでnullチェック
                if(secondTouch == null) {
                    secondTouch = new Pointer();
                    secondTouch.Down((int)tx, (int)ty);
                    secondTouch.SetID(pointerId);
                }
                break;

            // 一点目に限らずすべての移動
            case MotionEvent.ACTION_MOVE:
                if(singleTouch!=null) {
                    if (pointerId == singleTouch.GetID()) {
                        singleTouch.Move((int) tx, (int) ty);
                    }
                }

                if(secondTouch!=null) {
                    if (pointerId == secondTouch.GetID()) {
                        secondTouch.Move((int) tx, (int) ty);
                    }
                }
                break;

            // 二点以上のタッチがある状態でいずれかのタッチが離された
            case MotionEvent.ACTION_POINTER_UP:
                if(singleTouch!=null) {
                    if (pointerId == singleTouch.GetID()) {
                        singleTouch.Up((int) tx, (int) ty);

                        // 一点目が離された状態で2点目をもう一度タッチした時のためにsecondを格上げしておく
                        singleTouch = secondTouch;
                        secondTouch = null;
                    }
                }

                if(secondTouch!=null) {
                    if (pointerId == secondTouch.GetID()) {
                        secondTouch.Up((int) tx, (int) ty);
                    }
                }
                break;

            // 最後のタッチが離された
            case MotionEvent.ACTION_UP:
                if(singleTouch!=null) {
                    singleTouch.Up((int) tx, (int) ty);
                }
                if(secondTouch!=null){
                    secondTouch.Up((int) tx, (int) ty);
                }
                break;
        }
        return true;
    }

    // 一点目
    private Pointer singleTouch = null;
    public Pointer GetTouch(){return singleTouch;}

    // 二点目
    // かなり無理矢理な作り方をしています。
    // 三点目を対応しようと思っている方はこんなやり方をしないでください。
    private Pointer secondTouch = null;
    public Pointer GetSecondTouch(){return secondTouch;}

}
