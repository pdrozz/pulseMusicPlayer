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
        makePlaylist();
        configRecyclerItemClick();


        adapterPlaylist=new AdapterPlaylist(listPlaylist,getActivity());
        recyclerView.setAdapter(adapterPlaylist);
        recyclerView.addOnItemTouchListener(recyclerItemClickListener);

        return root;
    }

    private void makePlaylist(){
        PlaylistModel model=new PlaylistModel();
        model.setACTION(1);
        listPlaylist.add(model);
        DAOplaylist dao=new DAOplaylist(getActivity());
        listPlaylist.addAll(dao.getPlaylists());
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

    private void configRecyclerItemClick(){
        recyclerItemClickListener= new RecyclerItemClickListener(getActivity(),
                recyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        DAOplaylist dao=new DAOplaylist(getActivity());
                        boolean result=dao.deletePlaylist(listPlaylist.get(position).getName());
                        if (result){
                            listPlaylist.remove(position);
                            adapterPlaylist.notifyDataSetChanged();
                            recyclerView.setAdapter(adapterPlaylist);
                            Toast.makeText(getActivity(), "SUCESSO AO DELETAR", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                });
    }
}