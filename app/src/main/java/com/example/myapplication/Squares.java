package com.example.myapplication;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.Random;

public class Squares{
    //生存フラグ
    private  boolean mIsAlive=true;
    //押し込みフラグ
    private  boolean SelectSquares=false;


    //処理の状態
    enum StatusNow{
        Player,
        Computer,
    }

    private int h=1<<1;

    //マスの種類
    enum SquaresID{
        Square,//四角
        Circle,//マル
        Cross,//バツ
        SquareCross,// 四角バツ
        SquareCircle,// 四角マル
        CircleCross,// 丸バツ
        Annoying,//おじゃま
    }
    //画像の種類
    String[] IDName={"四角.png","マル.png","バツ.png"};
    String[] IDChangeName={"四角バツ.png","四角丸.png","丸バツ.png"};

    String  AnnoyingName="おじゃま.png";


    private SquaresID Square=SquaresID.Square;
    public void SetSquare(SquaresID id){Square=id;}

    //座標
    private int PosX=0;
    private int PosY=0;

    private int PrimitivePosX=0;
    private int PrimitivePosY=0;

    //マスのRect
    private Rect hitArea=new Rect();
    public Rect GetHitArea(){return  hitArea;}
    //矢印のRect
    private Rect ArrowArea=new Rect();
    private Rect GetArrowHitArea(){return ArrowArea;}
    public boolean HitArrowCheck(int x,int y){
        return hitArea.contains(x,y);
    }

    //キャラの画像
    private String imageName="マル.png";
    public  void SetImageName(String img){imageName=img;}
    private float BigPosX= (float) 1.2;
    private float BigPosY= (float) 1.2;

    //当たり判定（四角形）
    public boolean HitCheck(Rect r){
        return hitArea.intersect(r);
    }
    //当たり判定check
    public boolean HitCheck(int x,int y){
        return hitArea.contains(x,y);
    }

    //クリア条件の変数+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+
    private SquaresID ClearSquare=SquaresID.Square;
    private String ClearName;
    private int ClearNum;
    private boolean ClearFlg;

    //private float ClearPosX=32+16;
    //private float ClearPosY=120;
    private float ClearPosX=32+16;
    private float ClearPosY=32+10;

    private boolean NewSquares=false;

    private int ClearCount;

    private float LossAngle=0;

    private String S_ClearCount="0";

    private String QuestionCountName="0";
    private String QuestionMathName="0";

    //=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+

    //================(フィールド)====================

    //-----------------(メソッド)--------------------
    //新しい座標をセット
    public void SetPos(int x,int y){
        PosX=x;
        PosY=y;
    }
    //マスの種類をセット
    public void NewID(){
        //ランダム発生
        Random rnd=new Random();
        int Id=rnd.nextInt(IDName.length);
        //マスの画像の名前を再格納
        imageName=IDName[Id];
        switch (Id){
            case 0:Square = SquaresID.Square;
            break;
            case 1:Square = SquaresID.Circle;
                break;
            case 2:Square = SquaresID.Cross;
                break;
        }
    }

    //一番上のマスが出てきたら生存のフラグも上げる
    public void RevivalID(){
        //ランダム発生
        Random rnd=new Random();
        int Id=rnd.nextInt(IDName.length);
        //マスの画像の名前を再格納
        imageName=IDName[Id];
        switch (Id){
            case 0:Square = SquaresID.Square;
            break;
            case 1:Square = SquaresID.Circle;
                break;
            case 2:Square = SquaresID.Cross;
                break;
        }
        mIsAlive=true;
    }

    //====================================================
    //クリアの条件セット（配列[0][0]だけ使う）
    public void ClearIfSet(){
        ClearFlg=false;
        ClearCount=0;
        S_ClearCount=String.valueOf(ClearCount);
        Random rnd=new Random();
        int Id=rnd.nextInt(IDChangeName.length);
        ClearName=IDChangeName[Id];
        //マスの画像の名前を再格納
        if(Id==0){ClearSquare=SquaresID.SquareCross;}
        if(Id==1){ClearSquare=SquaresID.SquareCircle;}
        if(Id==2){ClearSquare=SquaresID.CircleCross;}
        ClearNum=rnd.nextInt(4)+1;
         }
    //同じマスのカウント
    public void ClearCheck(SquaresID ID_1,boolean s){
        if(!s) {
            if (ID_1 == ClearSquare) {
                ClearCount++;
            }
            if (ClearCount == ClearNum) {
                ClearFlg = true;
            }
        }
        S_ClearCount=String.valueOf(ClearCount);

    }
    //一度ループしてクリア条件がだめだったら
    public void EndClear(){
        ClearCount=0;
    }

    //クリアしたかどうか
    public boolean ReturnClear(){return ClearFlg;}
    public boolean ClearDown(SquaresID ID){
       boolean s=false;
       if(ID==ClearSquare) {
           s = true;
       }
       /*ここで組み合わせマスの全Clear
        if(ID==SquaresID.CircleCross||ID==SquaresID.SquareCircle||ID==SquaresID.SquareCross) {
            s = true;
        }

        */
            return s;

    }
    public void AnnoyingCheck(SquaresID ID){
        if(ID==SquaresID.CircleCross||ID==SquaresID.SquareCircle||ID==SquaresID.SquareCross) {
            Square=SquaresID.Annoying;
            imageName=AnnoyingName;
        }

    }
    //====================================================
    //更新
    public void Update(){
        if(!mIsAlive){return;}
        TouchProc();
        //Rect更新
        hitArea.left=PosX-32;
        hitArea.top=PosY-32;
        hitArea.right=PosX+32;
        hitArea.bottom=PosY+32;
    }

    //生存を返す
    public boolean mIsAliveCheck(){
        return mIsAlive;
    }
    //セレクトを返す
    public boolean SelectCheck(){
        return SelectSquares;
    }
    //セレクトが終わったら
    public void SelectEnd(){SelectSquares=false;}

    //生存を落とす
    public void mIsAliveDown(){mIsAlive=false;}


    //IDを返す
    public SquaresID RetrunID(){
        return Square;
    }
    //落ちてきたIDをセット
    public void SetSquaresID(SquaresID ID){
        mIsAlive=true;
        Square=ID;
        if(ID==SquaresID.Square){imageName=IDName[0]; }
        if(ID==SquaresID.Circle){imageName = IDName[1];}
        if(ID==SquaresID.Cross){imageName=IDName[2];}
        if(ID==SquaresID.SquareCross){imageName=IDChangeName[0];}
        if(ID==SquaresID.SquareCircle){imageName=IDChangeName[1];}
        if(ID==SquaresID.CircleCross){imageName=IDChangeName[2];}

    }
    //受け取ったIDをチェック
    public boolean IDCheck(SquaresID ID_1,SquaresID ID_2){
        //同じIDならフラグを落とす

        boolean Flg=true;
        if(ID_1==ID_2){
            mIsAlive=false;
            Flg=false;
        }
        if(ID_1== SquaresID.Cross) {
            if (ID_2 == SquaresID.Square) {Square = SquaresID.SquareCross;imageName = IDChangeName[0];Flg=false;}
            if (ID_2 == SquaresID.Circle) {Square = SquaresID.CircleCross;imageName = IDChangeName[2];Flg=false;}
        }
        if(ID_1== SquaresID.Square) {
            if (ID_2 == SquaresID.Cross) {Square = SquaresID.SquareCross;imageName = IDChangeName[0];Flg=false;}
            if (ID_2 == SquaresID.Circle) {Square = SquaresID.SquareCircle;imageName = IDChangeName[1];Flg=false;}
        }
        if(ID_1== SquaresID.Circle) {
            if (ID_2 == SquaresID.Square) {Square = SquaresID.SquareCircle;imageName = IDChangeName[1];Flg=false;}
            if (ID_2 == SquaresID.Cross) {Square = SquaresID.CircleCross;imageName = IDChangeName[2];Flg=false;}
        }


        return Flg;
    }

    //タッチの処理
    private void TouchProc() {
        Pointer p = App.Get().TouchMgr().GetTouch();
        if (p == null) {return;}//早期リターン
        if (p.OnDown()){
            if (HitCheck(p.GetDownPos().x, p.GetDownPos().y)) {
                SelectSquares = !SelectSquares;
            }
        }
        if (p.OnUp()) {
 //           mIsAlive=false;
        }
    }

    public void Draw(){
        if(!mIsAlive){return;}
        if(SelectSquares) {
            App.Get().ImageMgr().Draw(imageName, PosX, PosY, BigPosX,BigPosY,0);
        }else{
            App.Get().ImageMgr().Draw(imageName,PosX,PosY);
        }
    }

    //クリア条件描画
    public void GameClearDraw() {
        App.Get().ImageMgr().Draw("お題＿下地.png", 314/2+2, ClearPosY);
        App.Get().ImageMgr().Draw(ClearName, ClearPosX, ClearPosY,0.8f,0.8f,0);
        App.Get().ImageMgr().Draw("お題＿かける.png", ClearPosX + 64, ClearPosY,0.8f,0.8f,0);

        ClearMath();
        App.Get().ImageMgr().Draw(QuestionCountName, ClearPosX + 64*2, ClearPosY);

        App.Get().ImageMgr().Draw("お題＿下地２.png", ClearPosX + (64*4)-20, ClearPosY);
        App.Get().ImageMgr().Draw(QuestionMathName, ClearPosX + (64*4)-16, ClearPosY);


        /*
        String ClearNow=String.valueOf(ClearNum);
        App.Get().GetView().DrawString(ClearPosX+250, ClearPosY+250,"×"+ClearNow, Color.BLACK);

        App.Get().GetView().DrawString(ClearPosX+500, ClearPosY+250,S_ClearCount, Color.BLACK);
         */
    }
    private void ClearMath(){


        switch (ClearCount) {
            case 0:QuestionMathName="数字/ゼロ.png";break;
            case 1:QuestionMathName="数字/イチ.png";break;
            case 2:QuestionMathName="数字/二.png";break;
            case 3:QuestionMathName="数字/サン.png";break;
            case 4:QuestionMathName="数字/ヨン.png";break;
            case 5:QuestionMathName="数字/ゴ.png";break;
            case 6:QuestionMathName="数字/ロク.png";break;
            case 7:QuestionMathName="数字/ナナ.png";break;
            case 8:QuestionMathName="数字/ハチ.png";break;
            case 9:QuestionMathName="数字/キュウ.png";break;
            default:QuestionMathName="四角.png";break;
        }

        switch (ClearNum) {
        case 0:QuestionCountName="数字/ゼロ.png";break;
        case 1:QuestionCountName="数字/イチ.png";break;
        case 2:QuestionCountName="数字/二.png";break;
        case 3:QuestionCountName="数字/サン.png";break;
        case 4:QuestionCountName="数字/ヨン.png";break;
        case 5:QuestionCountName="数字/ゴ.png";break;
        case 6:QuestionCountName="数字/ロク.png";break;
        case 7:QuestionCountName="数字/ナナ.png";break;
        case 8:QuestionCountName="数字/ハチ.png";break;
        case 9:QuestionCountName="数字/キュウ.png";break;
        default:QuestionCountName="四角.png";break;
    }
    }

    public void LossDraw(){
        {App.Get().ImageMgr().Draw(imageName, PosX, PosY,10);}
    }


    public void SelectDraw(int ix,int iy,int ix_max,int iy_max){
        if(!mIsAlive){return;}
        if(SelectSquares) {
            if(iy!=0) {App.Get().ImageMgr().Draw("矢印.png", PosX, PosY-32);}//上
            if(iy!=iy_max-1) {App.Get().ImageMgr().Draw("矢印.png", PosX, PosY+32,180);}//下
            if(ix!=ix_max-1) {App.Get().ImageMgr().Draw("矢印.png", PosX+32, PosY,90);}//右
            if(ix!=0) {App.Get().ImageMgr().Draw("矢印.png", PosX-32, PosY,270);}//左
        }
    }
    //-----------------(メソッド)--------------------
}
