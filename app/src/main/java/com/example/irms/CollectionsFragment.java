package com.example.irms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.irms.Adapter.CollectionsAdapter;
import com.google.android.material.tabs.TabLayout;

public class CollectionsFragment extends Fragment {
    ViewPager collectionsViewPager;
    CollectionsAdapter collectionsAdapter;
    TabLayout collectionTabLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_collections,container,false);
       collectionsViewPager = view.findViewById(R.id.collections_view_pager);
       collectionsAdapter = new CollectionsAdapter(getFragmentManager());
       collectionsViewPager.setAdapter(collectionsAdapter);
       collectionTabLayout = view.findViewById(R.id.collections_tab_layout);
       collectionTabLayout.setupWithViewPager(collectionsViewPager);
               return view;
    }
}
