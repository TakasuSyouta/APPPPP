package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.Log;
import android.view.View;

/*
*OriginalView
*
* AndroidView、メインループ管理クラス
*
*
*/

public class OriginalView extends View {
    public OriginalView(Context context) {
        super(context);
    }

    // スレッドスタート
    public void start()
    {
        // アプリケーション本体にView情報を渡す
        App.Get().SetView(this);

        Thread trd = new Thread(new Runnable(){
            public void run() {
                while(loop) {

                    long prev = System.currentTimeMillis();

                    // ゲーム根幹処理
                    mHandler.post(new Runnable() {
                        public void run() {
                            loop = Update(); // 自作のループ処理開始
                        }
                    });

                    // メインスレッドに再描画を任せる
                    mHandler.post(new Runnable() {
                        public void run() {postInvalidate();} // 再描画依頼
                    });

                    // FPS制御
                    long ptTime = 32-(System.currentTimeMillis()-prev);
                    try {
                        if(ptTime<0) {
                            Thread.sleep(1);
                        }else{
                            Thread.sleep(ptTime);
                            //Log.d("sleep", "ptTime : " + ptTime);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        });
        trd.start();
    }

    // メインループ
    private boolean Update()
    {
        return App.Get().Update();
    }

    // Androidから呼び出される描画関数
    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);
        canvas = c; // メインキャンバスを覚える

        isDraw = true;
        App.Get().Draw();
        isDraw = false;
    }




    // 文字列描画
    public void DrawString(float _x, float _y, String _str, int _color)
    {
        if(canvas == null){return;}

        paint.setColor(_color);
        paint.setTextSize(150);

        canvas.drawText(_str, _x, _y, paint);
    }


    private boolean loop = true;                // 終了検知
    private Handler mHandler = new Handler();   // スレッドハンドル

    private Canvas canvas = null;               // 描画先
    public Canvas GetCanvas(){return canvas;}

    private Paint paint = new Paint();          // 描画情報
    private int frameCnt = 0;                   // 処理フレームカウント用

    // 描画して良いかどうか
    private boolean isDraw = false;
    public boolean IsDraw(){return isDraw;}
}
