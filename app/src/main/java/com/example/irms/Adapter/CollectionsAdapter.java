package com.example.irms.Adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.irms.ExistingCollections;
import com.example.irms.NewCollections;

public class CollectionsAdapter extends FragmentStatePagerAdapter {
    final int page_count = 2;
    Bundle bundle;
    private String[] tab_titles = {"New", "Existing"};

    public CollectionsAdapter(@NonNull FragmentManager fm, Bundle bundle) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.bundle = bundle;
    }

    @Override
    public int getCount() {
        return page_count;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                NewCollections newCollections = new NewCollections();
                newCollections.setArguments(bundle);
                return newCollections;
            case 1:
                ExistingCollections existingCollections = new ExistingCollections();
                existingCollections.setArguments(bundle);
                return existingCollections;
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
