package com.afinal.sdnath.sdnath;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {
    LinearLayout l1;
    ImageView iv;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();

        l1= findViewById(R.id.l1);
        iv=findViewById(R.id.imageview1);
        tv=findViewById(R.id.textview1);
        Animation myanim= AnimationUtils.loadAnimation(this,R.anim.mytransition);
        Animation uptodown=AnimationUtils.loadAnimation(this, R.anim.uptodown);
        l1.setAnimation(uptodown);
        tv.startAnimation(myanim);
        iv.startAnimation(myanim);
        final Intent intent=new Intent(SplashActivity.this,login.class);
        Thread timer=new Thread(){
            public  void run(){
                try{
                    sleep(5000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    startActivity(intent);
                    //overridePendingTransition(R.anim.activitytransition,R.anim.activitytransition);
                    finish();
                }
            }
        };
        timer.start();
    }
}
