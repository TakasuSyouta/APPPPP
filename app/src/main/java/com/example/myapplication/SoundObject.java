package com.example.myapplication;

import android.graphics.Rect;

public class SoundObject {


public  void PlaySEManager(int SE) {
        App.Get().SoundMgr().PlaySE(SE);
}

}
