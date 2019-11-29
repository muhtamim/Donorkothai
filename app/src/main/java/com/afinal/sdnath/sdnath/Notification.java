package com.afinal.sdnath.sdnath;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Notification extends AppCompatActivity {


    private Button BloodDonar, BloodReciever;
    TextView lineDonar, lineReciever;
    private ViewPager viewPager;

    private PagerViewAdapter pagerViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Notification");




        BloodDonar = (Button) findViewById(R.id.bloodDonar);
        BloodReciever = (Button) findViewById(R.id.bloodReciever);

        lineDonar = findViewById(R.id.lineDonar);
        lineReciever = findViewById(R.id.lineReciever);


        viewPager = (ViewPager) findViewById(R.id.viewpager);

        pagerViewAdapter = new PagerViewAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerViewAdapter);


        Intent checkTypeofNotification= getIntent();
        String type=checkTypeofNotification.getStringExtra("type");

        if ("reciever".equals(type)) {
            viewPager.setCurrentItem(1);
            lineDonar.setVisibility(View.INVISIBLE);
            lineReciever.setVisibility(View.VISIBLE);
        }



        BloodDonar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(0);
            }
        });

        BloodReciever.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);
            }
        });


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                changeTabs(i);

            }


            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        Fragment bloodRecieverFragment = new BloodRecieverFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.viewpager, bloodRecieverFragment).commit();


    }


    private void changeTabs(int i) {
        if (i == 0) {

            lineReciever.setVisibility(View.INVISIBLE);
            lineDonar.setVisibility(View.VISIBLE);

        }
        if (i == 1) {
            lineDonar.setVisibility(View.INVISIBLE);
            lineReciever.setVisibility(View.VISIBLE);
        }
    }


    /************************************************************/



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }





}
