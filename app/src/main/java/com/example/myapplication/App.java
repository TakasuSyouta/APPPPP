package com.example.myapplication;


import android.content.Context;
import android.graphics.Matrix;
import android.util.Log;
import android.view.SoundEffectConstants;

import java.util.concurrent.Future;

/*
* App
* リソース管理などのシステム面全般
* シングルトン
*
*/
public class App
{
    //配列
    static int SquaresNumX=5;
    static int SquaresNumY=6;
    Squares Squares[][]=new Squares[SquaresNumX][SquaresNumY];


    public Squares[][] GetSquares(){return Squares;}

    //時間制限
    Time Times=new Time();
    public Time GetTime(){return Times;}

    Arrow Arrow=new Arrow();
    public Arrow GetArrow(){return Arrow;}

    //背景
    BackGround Back=new BackGround();
    public BackGround GetBack(){return  Back;}

    //タイトル
    Title Titles=new Title();
    public Title GetTitle(){return Titles;}

    //リザルト
    Result Results=new Result();
    //GameObject Results=new Result();
    public GameObject GetResult(){return Results;}

    //スコア
    Score Scores=new Score();
    public Score GetScore(){return Scores;}

    SoundObject Sound=new SoundObject();
    public SoundObject GetSound(){return Sound;}

    // ゲームの実装------------------------------------------------>
    // 特殊な事をしない限りはこの間を編集するだけのはず
    int se1 = 0;
    int se2 = 0;
    int se3 = 0;
    int SEClear = 0;
    int SEGameCount_up = 0;
    int SEGameCount_normal = 0;
    int SEGameCount_Start= 0;

    int TimeCount=0;
    int SquaresCount=0;

    //ゲームの状態変異
    enum GameNow{
        Title,
        Game,
        Result,
    }
    private GameNow Puzzle=GameNow.Title;

    //ゲームフレーム（０～１００をカウント）
    private int GameFrame=0;
    private int BuzzerFrame=0;



    //ゲームクリア条件
    private boolean ClearFlg=false;
    //ゲームオーバー条件
    private boolean OverFlg=false;

    // アプリケーションが開始された時
    // 諸々の初期化は終わっているので、ここでロードをかけてもOK
    public void Start()
    {
        soundManager.PlayBGM("TitleBGM.mp3");
        //soundManager.PlayBGM("blip01.mp3");
        se1 = soundManager.LoadSE("se1.mp3");
        se2 = soundManager.LoadSE("se2.mp3");
        se3 = soundManager.LoadSE("blip01.mp3");
        SEClear = soundManager.LoadSE("GameClick.mp3");

        SEGameCount_up = soundManager.LoadSE("音声/低.mp3");
        SEGameCount_normal = soundManager.LoadSE("音声/中.mp3");
        SEGameCount_Start = soundManager.LoadSE("音声/Start.mp3");


        GameFrame=0;
        BuzzerFrame=50;

        //クリア条件
        ClearFlg=false;

        //ゲームオーバー条件
        OverFlg=false;

        Scores.Start();

        Times.Start();

        //マスの初期化
        for(int ix=0;ix<SquaresNumX;ix++) {
            for (int iy = 0; iy <  SquaresNumY; iy++) {
                Squares[ix][iy] = new Squares();//メモリ確保
                Squares[ix][iy].NewID();
                Squares[ix][iy].SetPos(32+ix*64,128+iy*64+64);
            }
        }
        Squares[0][0].ClearIfSet();

    }





    // 毎回呼び出される関数(30fps)
    Vector2 vp = new Vector2();
    Vector2 vm = new Vector2();

    //ゲームクリア設定など
    private void GameStartSet(){

    }

    //それぞれの更新関数
    private  void GameUpdate(){

        Scores.NowUpDate();

        //ゲームの進行処理
        if(GameFrame<10000) {
            GameFrame++;
        }
        if(GameFrame==10000){
            GameFrame=0;
        }

        //ゲーム開始前
        if(Times.Time_Return()) {
            if (GameFrame == 0) {
                Times.Time_3();
            }
            if (GameFrame == 30) {
                soundManager.PlaySE(SEGameCount_normal);
                Times.Time_2();
            }
            if (GameFrame == 60) {
                soundManager.PlaySE(SEGameCount_up);
                Times.Time_1();
            }
            if (GameFrame == 90) {
                soundManager.PlaySE(SEGameCount_Start);
                Times.Time_Start();
            }
            if (GameFrame == 110) {
                Times.Time_FlgDown();
            }

        }

        Times.TimePlusUpdate();

        //ゲーム開始後
        if(!Times.Time_Return()) {
            //時間関係
            if (GameFrame % 15 == 0) {
                Times.Update();
            }


            for (int ix = 0; ix < SquaresNumX; ix++) {
                for (int iy = 0; iy < SquaresNumY; iy++) {
                    Squares[ix][iy].Update();

                    //空白チェック(ここで落ちる処理が動く)
                    if (!Squares[ix][iy].mIsAliveCheck()) {
                        //一番上のマスを消したとき
                        if (iy == 0) {
                            Squares[ix][iy].RevivalID();
                        }
                        //それ以外のマスを押したとき（ここが落ちる処理）
                        else {
                            int Cy = iy;
                            do {
                                Squares[ix][Cy].SetSquaresID(Squares[ix][Cy - 1].RetrunID());
                                Cy--;
                            } while (Cy != 0);
                            Squares[ix][0].NewID();
                        }
                    }
                }
            }

            //クリア条件満たさなかったとき
            //Squares[0][0].NotClear();
            if (ClearFlg) {
                //お題のマスを削除する
                ClearDown();
                Squares[0][0].ClearIfSet();
                Squares[0][0].EndClear();
                ClearFlg = false;
                Times.TimePlus();
                Scores.ScorePlus();
            }
            //ゲーム終了
            if (Times.GameOver()) {
                GameFrame=0;
                SelectDown();
                Puzzle = GameNow.Result;
            }
        }
    }
    private  void TitleUpdate(){Titles.UpDate();}
    private  void ResultUpdate(){Results.Update();}

    private void ClearDown(){
        for(int ix=0;ix<SquaresNumX;ix++) {
            for (int iy = 0; iy < SquaresNumY; iy++) {
                if(Squares[0][0].ClearDown( Squares[ix][iy].RetrunID())) {
                    Squares[ix][iy].mIsAliveDown();
                }
            }
        }
    }

    private void SelectDown(){
        for(int ix=0;ix<SquaresNumX;ix++) {
            for (int iy = 0; iy < SquaresNumY; iy++) {
                    Squares[ix][iy].SelectEnd();
            }
        }
    }


    //タッチの処理（タッチアップ）
    private void GameTouchUpdate_UP(){

        for(int ix=0;ix<SquaresNumX;ix++) {
            for (int iy = 0; iy < SquaresNumY; iy++) {
                if(Squares[ix][iy].SelectCheck()) {



                    if (iy != 0) {if (Squares[ix][iy - 1].HitCheck((int) vp.x, (int) vp.y)){                                      //セレクト２と当たり判定
                        boolean SQFlg= Squares[ix][iy - 1].IDCheck(Squares[ix][iy - 1].RetrunID(), Squares[ix][iy].RetrunID());   //IDをチェックとマスの消失チェック

                        Squares[0][0].ClearCheck(Squares[ix][iy-1].RetrunID(),SQFlg);
                        ClearFlg=Squares[0][0].ReturnClear();



                        if(!SQFlg){
                            Squares[ix][iy].mIsAliveDown();
                            soundManager.PlaySE(SEClear);
                        }                                                               //消すフラグが立っていたら消すとこ
                        Squares[ix][iy].SelectEnd();                                                                              //セレクト終了１
                        Squares[ix][iy-1].SelectEnd();                                                                            //セレクト終了２
                    }
                    }//上
                    if (iy != SquaresNumY - 1) {if (Squares[ix][iy + 1].HitCheck((int) vp.x, (int) vp.y)){
                        boolean SQFlg=Squares[ix][iy+1].IDCheck(Squares[ix][iy + 1].RetrunID(),Squares[ix][iy].RetrunID());


                        //お題クリアしたかチェック
                        Squares[0][0].ClearCheck(Squares[ix][iy+1].RetrunID(), SQFlg);
                        ClearFlg=Squares[0][0].ReturnClear();



                        if(!SQFlg){
                            Squares[ix][iy].mIsAliveDown();
                            soundManager.PlaySE(SEClear);
                        }
                        Squares[ix][iy].SelectEnd();
                        Squares[ix][iy+1].SelectEnd();
                    }}//下
                    if (ix != SquaresNumX - 1) {if (Squares[ix+1][iy].HitCheck((int) vp.x, (int) vp.y)){
                        boolean SQFlg=Squares[ix+1][iy].IDCheck(Squares[ix+1][iy].RetrunID(),Squares[ix][iy].RetrunID());

                        Squares[0][0].ClearCheck(Squares[ix+1][iy].RetrunID(), SQFlg);
                        ClearFlg=Squares[0][0].ReturnClear();




                        if(!SQFlg){
                            Squares[ix][iy].mIsAliveDown();
                            soundManager.PlaySE(SEClear);
                        }
                        Squares[ix][iy].SelectEnd();
                        Squares[ix+1][iy].SelectEnd();
                    }}//右
                    if (ix != 0) {if (Squares[ix-1][iy].HitCheck((int) vp.x, (int) vp.y)){
                        boolean SQFlg=Squares[ix-1][iy].IDCheck( Squares[ix - 1][iy].RetrunID(),Squares[ix][iy].RetrunID());

                        Squares[0][0].ClearCheck(Squares[ix-1][iy].RetrunID(), SQFlg);
                        ClearFlg=Squares[0][0].ReturnClear();




                        if(!SQFlg){
                            Squares[ix][iy].mIsAliveDown();
                            soundManager.PlaySE(SEClear);
                        }
                        Squares[ix][iy].SelectEnd();
                        Squares[ix-1][iy].SelectEnd();
                    }}//左
                }


                //上下左右のチェック（同じならフラグを落とす）
                /*
                if(!Squares[ix][iy].SelectCheck()){
                    if (iy != 0) {Squares[ix][iy].IDCheck(Squares[ix][iy].RetrunID(), Squares[ix][iy - 1].RetrunID());}//上
                */
            }}


        //ここは斜めや無意味に押したとき
        int SQ=0;
        for(int ix=0;ix<SquaresNumX;ix++) {
            for (int iy = 0; iy < SquaresNumY; iy++) {
                if (Squares[ix][iy].SelectCheck()) {
                    SQ++;
                    if (SQ > 1) {
                        SelectDown();
                        SQ=0;
                    }
                }
            }
        }



    }
    private void TitleTouchUpdate_UP(){
        if(Titles.HitCheck((int)vp.x,(int)vp.y)){
            Times.Reset();
            Puzzle=GameNow.Game;
            Squares[0][0].ClearIfSet();

            Scores.Reset();

            //マスの再初期化
            for(int ix=0;ix<SquaresNumX;ix++) {
                for (int iy = 0; iy <  SquaresNumY; iy++) {
                    Squares[ix][iy].NewID();
                }
            }
        }

    }
    private void ResultTouchUpdate_UP(){
        if(Results.HitCheck((int)vp.x,(int)vp.y)){
            Results.TouchUp();
            Titles.TouchNotDOWN();
            Puzzle=GameNow.Title;
        }
    }

    //タッチの処理（タッチダウン）
    private void GameTouchUpdate_DOWN(){}
    private void TitleTouchUpdate_DOWN(){
        if(Titles.HitCheck((int)vp.x,(int)vp.y)) {
            Titles.TouchDOWN();
        }
    }
    private void ResultTouchUpdate_DOWN(){
    if(Results.HitCheck((int)vp.x,(int)vp.y)){
            Results.TouchDOWN();
        }
    }

    public boolean Update()
    {
        // ゲームの更新
        //通常の更新
        switch (Puzzle) {
            case Title:TitleUpdate();break;
            case Game:GameUpdate();break;
            case Result:ResultUpdate();break;
        }

        vp.Add(vm);

        // タッチの処理
        Pointer p = touchManager.GetTouch(); // ここでnullが帰る場合はタッチされていない
        if(p==null){ return true; }

        if(p.OnDown()) // 押された瞬間
        {
            if(BuzzerFrame!=0) {
                BuzzerFrame--;
            }
            vp.x = p.GetDownPos().x;
            vp.y = p.GetDownPos().y;

            vm.Clear();
            soundManager.PlaySE(se1);

            //タッチが押されたとき
            switch (Puzzle) {
                case Title:TitleTouchUpdate_DOWN();break;
                case Game:GameTouchUpdate_DOWN();break;
                case Result:ResultTouchUpdate_DOWN();break;
            }

        }

        if(p.OnFlick()) // 離された＆それがフリックと判定された
        {
            vm = p.GetDtoU();
        }

        if(p.OnUp())
        {
            //タッチが上がったら
            switch (Puzzle) {
                case Title:TitleTouchUpdate_UP();break;
                case Game:GameTouchUpdate_UP();break;
                case Result:ResultTouchUpdate_UP();break;
            }

            soundManager.PlaySE(se2);
            Log.d("touch", "OnUP!!!!!");
        }



        // 必ずゲーム更新の後に行う事
        touchManager.Update();
        return true;
    }



    // Androidから再描画命令を受けた時
    // ここ以外での描画は無視されます。
    // 早くシステムに返さないと行けないので、描画以外の余計なことはしない。
    // 30fpsで再描画以来はかけているが、呼び出される頻度は端末によります。
    // ここが安定して動いているとは思わないでください。

    //それぞれの描画関数
    private  void GameDraw(){
        for(int ix=0;ix<SquaresNumX;ix++) {
            for (int iy = 0; iy < SquaresNumY; iy++) {
                Squares[ix][iy].Draw();
            }
        }
        for(int ix=0;ix<SquaresNumX;ix++) {
            for (int iy = 0; iy < SquaresNumY; iy++) {
                // Squares[ix][iy].SelectDraw(ix,iy,SquaresNumX,SquaresNumY);
            }
        }

        Squares[0][0].GameClearDraw();
      //  Times.Draw();
        Times.CountDraw();
        Scores.NowDraw();




        Times.TimeDraw();
    }
    private  void TitleDraw(){ Titles.TitleMainDraw(); }
    private  void ResultDraw(){ Results.ResultMainDraw(); }



    public void Draw()
    {
        Back.Draw();//背景
        switch (Puzzle) {
            case Title:TitleDraw();break;
            case Game:GameDraw();break;
            case Result:ResultDraw();break;
        }


        //imageManage.Draw("ball.png", vp.x, vp.y);
    }

    // ホームボタンなどを押して裏側へ回った時
    public void Suspend(Context c)
    {
        soundManager.StopBGM();
    }

    // 再度アクティブになった時
    public void Resume(Context c)
    {
        soundManager.PlayBGM("TitleBGM.mp3");
    }

    // <--------------------------------------------------ゲームの実装





    // アプリケーション大本
    private OriginalView view = null;
    public OriginalView GetView(){return view;}
    public void SetView(OriginalView _ov)
    {
        view = _ov;

        // viewがないと初期化出来ないもののインスタンス化
        touchManager = new TouchManager();


        Start();
    }

    // 解像度対応
    private float sdPar = 0; // システム座標→ディスプレイ座標変換用
    private float dsPar = 0; // ディスプレイ座標→システム座標変換用
    public float SD(){return sdPar;}
    public float DS(){return dsPar;}

    // システム画面サイズと実機画面サイズが出揃った段階で比率を計算
    public void SetSDPar(float _dp, float _sp)
    {
        sdPar = _dp/_sp;
        dsPar = _sp/_dp;
    }

    // リソース管理
    private ImageManager imageManage = new ImageManager();
    public ImageManager ImageMgr(){return imageManage;}
    private SoundManager soundManager = new SoundManager();
    public SoundManager SoundMgr(){return soundManager;}

    // タッチ管理
    private TouchManager touchManager = null;
    public TouchManager TouchMgr(){return touchManager;}

    //シングルトン実装
    private App() { }
    private static App app = new App();
    public static App Get()
    {
        return app;
    }
}
