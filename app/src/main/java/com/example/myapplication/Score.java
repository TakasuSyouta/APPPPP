package com.example.myapplication;

public class Score {

    //スコアの桁数
    enum SCOREDIGIT{
        DIGIT_1,
        DIGIT_10,
        DIGIT_100,
        DIGIT_1000,
        DIGIT_10000,
        DIGIT_100000,
        DIGIT_1000000,
        DIGIT_10000000,
        DIGIT_100000000,
    }


    private static int ScoreDigits=9;
    private String ScorePointName="数字/ゼロ.png";

    private ScoreMaterial[] Scores=new ScoreMaterial[9];

    private int NowScoreX=192/2-40;
    //private int NowScoreY=448+64+32+10;
    private int NowScoreY=100+32;
    private float ScoreSize=0.6f;

    ////////////////////////////////////////////
    private int MathScale=20;
    private float MathScaleSize=0.4f;

    private static int MAXSCORE=999999999;
    private int NowScore=0;
    private int UpingScore=0;

    //================================

    public void Start() {
        for(int i=0;i<ScoreDigits;i++) {
            Scores[i] = new ScoreMaterial();
            Scores[i].Set();
        }
        NowScore=0;
        UpingScore=0;
    }

    public void Reset(){
        for(int i=0;i<ScoreDigits;i++) {
            Scores[i] = new ScoreMaterial();
            Scores[i].Set();
        }
        NowScore=0;
        UpingScore=0;
    }



    public void NowUpDate(){
        //マックス判定
        if(NowScore>MAXSCORE){
            NowScore=MAXSCORE;
        }

        if(UpingScore<NowScore){
            UpingScore+=1;
        }


        //スコア更新部分
        for(int i=0;i<ScoreDigits;i++){
            int s= (int) Math.pow(10,i);  //桁部分の抽出
            int n=(UpingScore/s)%10;        //桁部分の数字を抽出
            if(i==0){
                n=UpingScore%10;
            }
            Scores[i].ScoreName=SetImagename(n);//文字セット
        }


//        TenRank=SetImagename((GameCount/10)%10);
  //      OneRank=SetImagename(GameCount%10);

    }
    public void NowDraw(){

        App.Get().ImageMgr().Draw("スコア.png",NowScoreX,NowScoreY,ScoreSize,ScoreSize,0);


        for(int i=0;i<ScoreDigits;i++){
//            App.Get().ImageMgr().Draw(Scores[i].ScoreName,NowScoreX+80+(20*i),NowScoreY+2,0.4f,0.4f,0);
            App.Get().ImageMgr().Draw(Scores[i].ScoreName,(NowScoreX+70+20*ScoreDigits)-(MathScale*i),NowScoreY+2,MathScaleSize,MathScaleSize,0);
        }
    }
    public void ScorePlus(){NowScore+=100;}
    private String SetImagename(int Math){

        String SetName;

        switch (Math) {
            case 0:SetName="数字/ゼロ.png";break;
            case 1:SetName="数字/イチ.png";break;
            case 2:SetName="数字/二.png";break;
            case 3:SetName="数字/サン.png";break;
            case 4:SetName="数字/ヨン.png";break;
            case 5:SetName="数字/ゴ.png";break;
            case 6:SetName="数字/ロク.png";break;
            case 7:SetName="数字/ナナ.png";break;
            case 8:SetName="数字/ハチ.png";break;
            case 9:SetName="数字/キュウ.png";break;
            default:SetName="四角.png";break;
        }

        return SetName;
    }


    public void RankingUpDate(){}
    public void RankingDraw(){}


}
