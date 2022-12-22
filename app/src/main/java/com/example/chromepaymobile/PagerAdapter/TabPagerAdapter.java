package com.example.chromepaymobile.PagerAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.chromepaymobile.Fragments.FinancialActivitiesFragment;
import com.example.chromepaymobile.Fragments.LandRegistrationFragment;
import com.example.chromepaymobile.Fragments.PersonalFragment;

public class TabPagerAdapter extends FragmentPagerAdapter {

    int tabCount;

    public TabPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);

        tabCount = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0: return new PersonalFragment();
            case 1: return new LandRegistrationFragment();
            case 2: return new FinancialActivitiesFragment();

            default: return new PersonalFragment();
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
