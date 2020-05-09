package com.pdrozz.pulsemusicplayer.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pdrozz.pulsemusicplayer.R;
import com.pdrozz.pulsemusicplayer.adapter.AdapterPlaylist;
import com.pdrozz.pulsemusicplayer.model.PlaylistModel;
import com.pdrozz.pulsemusicplayer.sqlHelper.DAOplaylist;
import com.pdrozz.pulsemusicplayer.utils.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<PlaylistModel> listPlaylist=new ArrayList<>();
    private AdapterPlaylist adapterPlaylist;
    private RecyclerItemClickListener recyclerItemClickListener;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        configWidgets(root);
        configRecycler();



        adapterPlaylist=new AdapterPlaylist(listPlaylist,getActivity());
        recyclerView.setAdapter(adapterPlaylist);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        makePlaylist();
        adapterPlaylist.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        listPlaylist.clear();
    }

    private void makePlaylist(){
        DAOplaylist dao=new DAOplaylist(getActivity());
        List<PlaylistModel> auxList=dao.getPlaylists();
        if (auxList.isEmpty()){
            PlaylistModel model=new PlaylistModel();
            model.setACTION(PlaylistModel.ACTION_CREATE);
            listPlaylist.add(model);
        }
        else {
            listPlaylist.addAll(dao.getPlaylists());
        }
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