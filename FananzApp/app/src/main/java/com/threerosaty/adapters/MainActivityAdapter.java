package com.threerosaty.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.threerosaty.fragments.CorporateRegFragment;
import com.threerosaty.fragments.FreelanceRegFragment;

/**
 * Created by shrinivas on 15-12-2016.
 */
public class MainActivityAdapter extends FragmentPagerAdapter {
    private int itemCount;

    public MainActivityAdapter(FragmentManager fm, int count) {
        super(fm);
        this.itemCount = count;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                CorporateRegFragment corporateRegFragment = new CorporateRegFragment();
                return corporateRegFragment;
            case 1:
                FreelanceRegFragment freelanceRegFragment = new FreelanceRegFragment();
                return freelanceRegFragment;

            default:
                return null;
        }


    }

    @Override
    public int getCount() {
        return itemCount;
    }
}
