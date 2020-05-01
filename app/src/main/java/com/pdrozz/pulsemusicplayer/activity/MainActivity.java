package com.pdrozz.pulsemusicplayer.activity;

import android.Manifest;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.pdrozz.pulsemusicplayer.R;
import com.pdrozz.pulsemusicplayer.adapter.PagerAdapterTabs;
import com.pdrozz.pulsemusicplayer.utils.PermissionUtil;
import com.pdrozz.pulsemusicplayer.widget.FavButton;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabs;
    FloatingActionButton fabMain;
    String[] permissoes={Manifest.permission.READ_EXTERNAL_STORAGE};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configWidgets();
        setupTabsViewPager();

        PermissionUtil.getPermission(permissoes,this,30);

        configFabMainClickListener();

    }


    private void configFabMainClickListener(){
        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void setupTabsViewPager(){
        PagerAdapterTabs pagerAdapterTabs = new PagerAdapterTabs(this, getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapterTabs);

        tabs.setupWithViewPager(viewPager);
    }

    private void configWidgets(){
        viewPager = findViewById(R.id.view_pager);
        tabs = findViewById(R.id.tabs);
        fabMain = findViewById(R.id.fab);
    }
}