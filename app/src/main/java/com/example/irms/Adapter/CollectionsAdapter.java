package com.example.irms.Adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.example.irms.ExistingCollections;
import com.example.irms.NewCollections;

public class CollectionsAdapter extends FragmentStatePagerAdapter {
    final int page_count = 2;
    private String[] tab_titles = {"New","Existing"};
    public CollectionsAdapter(@NonNull FragmentManager fm) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Override
    public int getCount() {
        return page_count;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                 return new NewCollections();
            case 1:
              return new ExistingCollections();
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tab_titles[position];
    }
}
