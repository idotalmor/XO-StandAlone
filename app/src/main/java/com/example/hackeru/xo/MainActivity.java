package com.example.hackeru.xo;

import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private boolean IsO;
    byte[][] x;
    byte counter,xscore,oscore;
    TableLayout parent;
    MediaPlayer ding,winning,tie;
    AnimationDrawable xanim,oturn,xturn;
    LinearLayout linearly;
    TextView scorexttl,scoreottl;
    ImageView scoroimg,scorximg;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parent=(TableLayout)findViewById(R.id.table);//tablelayout
        linearly=(LinearLayout)findViewById(R.id.linearly);//linearlayout - btn on top

        scorexttl=(TextView)findViewById(R.id.scorexttl);//xscore
        scoreottl=(TextView)findViewById(R.id.scoreottl);//oscore

        scoroimg=(ImageView)findViewById(R.id.scoroimg);//o bottom img
        oturn=(AnimationDrawable)scoroimg.getDrawable();//o bottom animation

        scorximg=(ImageView)findViewById(R.id.scorximg);//x bottom img
        xturn=(AnimationDrawable)scorximg.getDrawable();//x bottom animation


        ding=MediaPlayer.create(this,R.raw.ding);
        winning=MediaPlayer.create(this,R.raw.winning);
        tie=MediaPlayer.create(this,R.raw.tie);

        reset();
        addbuttons();
        toggleturn();
    }




    public void onBtClick(View v){

        String str=v.getTag().toString();
           String []stra= str.split(",");
        int row=Integer.parseInt(stra[0]),column=Integer.parseInt(stra[1]);
        //if(x[row][column]!=0) return;
            if(!IsO){
                ((ImageView)v).setImageResource(R.drawable.xdra);
                xanim=(AnimationDrawable) ((ImageView)v).getDrawable();
                xanim.setOneShot(true);xanim.start();
                x[row][column]=1;
                okpress(v);
        }else{
                ((ImageView)v).setImageResource(R.drawable.o);
                x[row][column]=2;
                okpress(v);
            }

        if(counter>=5){check(row,column);}
        IsO =!IsO;
        toggleturn();
    }

    public void okpress(View v){
        ding.start();
        counter ++;
        v.setClickable(false);
    }

  public void check(int row,int column){
      int i,matches,player=IsO?2:1,xlength=x.length,dis=Math.abs(row-column);

      for(i=0,matches=0;i<xlength&&x[row][i]==player;i++){matches++;}
      if(matches==xlength){won();return;}

      for(i=0,matches=0;i<xlength&&x[i][column]==player;i++){matches++;}
      if(matches==xlength){won();return;}

      if(row+column==xlength-1||row==column){
          if(x[0][xlength-1]==player&&x[xlength-1][0]==player){
              for(i=0,matches=0;i<xlength&&x[i][xlength-1-i]==player;i++){matches++;}
              if(matches==xlength){won();return;}
          }
          if(x[0][0]==player&&x[xlength-1][xlength-1]==player){
              for(i=0,matches=0;i<xlength&&x[i][i]==player;i++){matches++;}
              if(matches==xlength){won();return;}
          }
      }

      if(counter==(byte)Math.pow(xlength,2)){
          tie.start();
          FinishAlert("It's A Tie","Fight Again?");
      }
  }

    public void won(){
        winning.start();
        if(IsO)oscore++;else xscore++;
        RefreshScore();
        String ttl= IsO ?"O":"X";
        ttl +=" Is The Ultimate Winner";
        FinishAlert(ttl,"Fight Again?");

    }
    public void reset(){ //reset array, table and counter
        x=new byte[3][3];//reset array
        counter=0;//reset counter
        for(int i=0;i<parent.getChildCount();i++){//set frame img source to all imageview in table
            TableRow t=(TableRow)parent.getChildAt(i);
        for(int j=0;j<t.getChildCount();j++){
            ( (ImageView)t.getChildAt(j)).setImageResource(R.drawable.frame);
            ( (ImageView)t.getChildAt(j)).setClickable(true);
        }}
    }



    public void FinishAlert(String ttl, String body){
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setCancelable(false);
        dialog.setTitle(ttl).setMessage(body).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for(int i=0;i<parent.getChildCount();i++){//set frame img source to all imageview in table
                    TableRow t=(TableRow)parent.getChildAt(i);
                    for(int j=0;j<t.getChildCount();j++){
                        ( (ImageView)t.getChildAt(j)).setClickable(false);
                    }}
            }
        }).setPositiveButton("Reset Game", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reset();
            }
        }).show();
    }
    private void addbuttons(){//add top buttons
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT,1.0f);
        Button RES=(Button) LayoutInflater.from(this).inflate(R.layout.btn,null);//add reset score btn
        RES.setText("Reset Score");
        RES.setLayoutParams(lp);
        RES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xscore=0;
                oscore=0;
                Toast.makeText(MainActivity.this,"A Fresh Start!",Toast.LENGTH_LONG).show();
                RefreshScore();

            }
        });
        linearly.addView(RES);
        Button NEWG=(Button) LayoutInflater.from(this).inflate(R.layout.btnr,null);//add reset score btn
        NEWG.setText("Reset Game");
        NEWG.setLayoutParams(lp);
        NEWG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });
        linearly.addView(NEWG);


    }

    private void RefreshScore(){
        scorexttl.setText(""+xscore);
        scoreottl.setText(""+oscore);

    }

    private void toggleturn(){
        if(IsO){oturn.start();xturn.stop();}
        else {oturn.stop();xturn.start();}
    }
}
