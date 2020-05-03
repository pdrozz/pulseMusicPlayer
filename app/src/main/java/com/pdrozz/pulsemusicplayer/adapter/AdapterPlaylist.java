package com.pdrozz.pulsemusicplayer.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pdrozz.pulsemusicplayer.R;
import com.pdrozz.pulsemusicplayer.activity.FilesActivity;
import com.pdrozz.pulsemusicplayer.activity.PlaylistActivity;
import com.pdrozz.pulsemusicplayer.model.PlaylistModel;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AdapterPlaylist extends RecyclerView.Adapter<AdapterPlaylist.ViewHolderPlaylist> {

    private List<PlaylistModel> listPlaylist=new ArrayList<>();
    private Activity activity;

    public AdapterPlaylist(List<PlaylistModel> listPlaylist, Activity activity) {
        this.listPlaylist = listPlaylist;
        this.activity=activity;
    }

    @NonNull
    @Override
    public ViewHolderPlaylist onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_grid_playlists,
                parent,false);
        return new ViewHolderPlaylist(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPlaylist holder, final int position) {
        PlaylistModel model=listPlaylist.get(position);
        holder.textViewNamePlaylist.setText(model.getName());
        if (model.getACTION()!=0){
            holder.textViewNamePlaylist.setText("Crie uma playlist agora");
            holder.buttonPlaylist.setText("Criar");
            holder.buttonPlaylist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.startActivity(new Intent(activity, PlaylistActivity.class).putExtra("create",true));
                }
            });
        }
        else{
            holder.buttonPlaylist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(activity,PlaylistActivity.class);
                    i.putExtra("create",false);
                    i.putExtra("playlist",listPlaylist.get(position));
                    activity.startActivity(i);
                }
            });
            holder.textViewNamePlaylist.setText(model.getName().toUpperCase());
            File picture=new File(activity.getFilesDir(),model.getName().toLowerCase()+".jpeg");
            Glide.with(activity).load(picture).into(holder.imageViewPlaylist);
        }
    }

    @Override
    public int getItemCount() {
        return listPlaylist.size();
    }

    public class ViewHolderPlaylist extends RecyclerView.ViewHolder{
        ImageView imageViewPlaylist;
        TextView textViewNamePlaylist;
        Button buttonPlaylist;

        public ViewHolderPlaylist(@NonNull View itemView) {
            super(itemView);
            imageViewPlaylist=itemView.findViewById(R.id.imageViewPlaylist);
            textViewNamePlaylist=itemView.findViewById(R.id.textViewNamePlaylist);
            buttonPlaylist=itemView.findViewById(R.id.buttonPlaylist);
        }
    }
}
