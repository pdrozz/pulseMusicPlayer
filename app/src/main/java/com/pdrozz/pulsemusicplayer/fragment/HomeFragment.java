package com.pdrozz.pulsemusicplayer.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pdrozz.pulsemusicplayer.R;
import com.pdrozz.pulsemusicplayer.adapter.AdapterPlaylist;
import com.pdrozz.pulsemusicplayer.model.PlaylistModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<PlaylistModel> listPlaylist=new ArrayList<>();
    private AdapterPlaylist adapterPlaylist;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        configWidgets(root);
        configRecycler();
        makePlaylist();

        adapterPlaylist=new AdapterPlaylist(listPlaylist,getActivity());
        recyclerView.setAdapter(adapterPlaylist);

        return root;
    }

    private void makePlaylist(){
        PlaylistModel model=new PlaylistModel();
        listPlaylist.add(model);
    }

    private void configRecycler(){
        layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        //recyclerView.setAdapter();
    }

    private void configWidgets(View v){
        recyclerView=v.findViewById(R.id.recyclerMain);
    }
}