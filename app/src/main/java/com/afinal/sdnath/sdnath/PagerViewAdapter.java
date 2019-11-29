package com.afinal.sdnath.sdnath;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class PagerViewAdapter extends FragmentPagerAdapter {
    public PagerViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {

            case 0:
                BloodDonarFragment myBloodDonarFragment  = new BloodDonarFragment();
                return  myBloodDonarFragment;
            case 1:
                BloodRecieverFragment myBloodRecieverFragment  = new BloodRecieverFragment();
                return  myBloodRecieverFragment;


             default:
                 return  null;



        }


    }

    @Override
    public int getCount() {
        return 2;
    }
}
