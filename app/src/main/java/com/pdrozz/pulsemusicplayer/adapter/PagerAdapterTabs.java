package com.pdrozz.pulsemusicplayer.adapter;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.pdrozz.pulsemusicplayer.R;
import com.pdrozz.pulsemusicplayer.fragment.FavoriteFragment;
import com.pdrozz.pulsemusicplayer.fragment.HomeFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class PagerAdapterTabs extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private final Context mContext;

    private FavoriteFragment favoriteFragment=new FavoriteFragment();
    private HomeFragment homeFragment=new HomeFragment();

    public PagerAdapterTabs(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }



    @Override
    public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return homeFragment;
                case 1:
                    return favoriteFragment;
            }
            return homeFragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Home";
            case 1:
                return "Favorite";
        }
        return "Home";
    }

    @Override
    public int getCount() {
        return 2;
    }
}