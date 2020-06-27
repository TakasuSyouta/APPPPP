package com.example.myapplication;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Matrix;
import android.view.View;

import java.io.InputStream;
import java.util.HashMap;

/*
*  画像管理クラス
*  甚だ遺憾であるが、管理部分を外に出したくないので、描画周りもマネージャが肩代わり
*  すでに読み込んでいるかのFayweight実装
*  基本は文字列で大丈夫だけど、読み込んだ画像になにかしたい場合はGet関数でBitmapを取得する
*  描画は画像の中央基準
*/
public class ImageManager
{
    public ImageManager()
    {
    }


    // 画像を読み込んでBitmapで返す
    // 文字列で指定するのであれば基本使わなくてもOK
    public Bitmap Get(String _pass)
    {
        OriginalView view= App.Get().GetView();
        if(view==null){return null;}

        // すでに読み込まれているものが無いか探す
        Bitmap plBmp = (Bitmap)bmpList.get(_pass);
        if(plBmp!=null){return plBmp;}

        // なかったので読み込み
        AssetManager am = view.getContext().getResources().getAssets();
        try {
            Bitmap bmp;
            InputStream is = am.open(_pass);
            bmp = BitmapFactory.decodeStream(is);
            bmpList.put(_pass, bmp);
            return bmp;
        } catch (Exception e) {
            return null;
        }
    }

    // 画像描画
    void Draw(String _imageName, float _x, float _y)
    {
        Matrix m = new Matrix();

        Bitmap bmp = Get(_imageName);
        if(bmp == null){return;}
        m.setTranslate(-bmp.getWidth()/2, -bmp.getHeight()/2);


        m.postTranslate(_x, _y);
        Draw(_imageName, m);
    }
    void Draw(String _imageName, float _x, float _y, float _rot)
    {
        Matrix m = new Matrix();

        Bitmap bmp = Get(_imageName);
        if(bmp == null){return;}
        m.setTranslate(-bmp.getWidth()/2, -bmp.getHeight()/2);
        m.postRotate(_rot);

        m.postTranslate(_x, _y);

        Draw(_imageName, m);
    }
    void Draw(String _imageName, float _x, float _y, float _sx, float _sy, float _rot)
    {
        Matrix m = new Matrix();

        Bitmap bmp = Get(_imageName);
        if(bmp == null){return;}
        m.setTranslate(-bmp.getWidth()/2, -bmp.getHeight()/2);
        m.postRotate(_rot);

        m.postScale(_sx, _sy);

        m.postTranslate(_x, _y);

        Draw(_imageName, m);
    }

    void Draw(String _imageName, Matrix _m)
    {
        // viewセット出来ているか
        OriginalView view= App.Get().GetView();
        if(view==null){return;}

        //描画可能か
        if(view.IsDraw()==false){return;}

        // 描画すべき画像が取得できるか
        Bitmap bmp = Get(_imageName);
        if(bmp == null){return;}

        // 描画先はあるか
        Canvas c = view.GetCanvas();
        if(c == null){return;}

        // 解像度に対応
        _m.postScale( App.Get().SD(), App.Get().SD() );

        // 描画
        c.drawBitmap(bmp, _m, paint);
    }



    private HashMap bmpList = new HashMap<String, Bitmap>();

    private Paint paint = new Paint();
}
