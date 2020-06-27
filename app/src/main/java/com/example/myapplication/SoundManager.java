package com.example.myapplication;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;

import java.io.IOException;

public class SoundManager
{
    static final int SOUND_POOL_MAX = 6;

    public SoundManager()
    {
        // ロリポップ以前の初期化
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool(SOUND_POOL_MAX, AudioManager.STREAM_MUSIC, 0);
        }

        // ロリポップ以降推奨の初期化
        else {
            AudioAttributes attr = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(attr)
                    .setMaxStreams(SOUND_POOL_MAX)
                    .build();
        }
    }

    public void PlayBGM(String _pass)
    {
        // 既に鳴っているBGMがあった
        if(mp!=null)
        {
            mp.stop();
            mp = null;
        }

        // BGMの再生
        mp = new MediaPlayer();

        AssetManager am = App.Get().GetView().getContext().getResources().getAssets();
        try {
            AssetFileDescriptor fd = am.openFd(_pass);

            mp.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
            mp.setLooping(true);
            mp.prepare(); // 再生可能に鳴るまで待機

            mp.start(); // 再生
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    // 任意のタイミングで止める
    public void StopBGM()
    {
        mp.stop();
    }

    int LoadSE(String _pass)
    {
        AssetManager am = App.Get().GetView().getContext().getResources().getAssets();

        int id = -1;
        try {
            AssetFileDescriptor fd = am.openFd(_pass);
            id = soundPool.load( fd, 1 );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return id;
    }

    void PlaySE(int _id)
    {
        soundPool.play(_id, 1.0F, 1.0F, 1, 0, 1.0F);
    }


    private MediaPlayer mp = null;
    private SoundPool soundPool = null;


}
