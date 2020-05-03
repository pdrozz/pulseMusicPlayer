package com.pdrozz.pulsemusicplayer.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.pdrozz.pulsemusicplayer.R;
import com.pdrozz.pulsemusicplayer.adapter.PagerAdapterTabs;
import com.pdrozz.pulsemusicplayer.utils.PermissionUtil;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabs;
    private FloatingActionButton fabMain;
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

    private void configRecycler(){}

    private void configFabMainClickListener(){
        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PlaylistActivity.class).putExtra("create",true));
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