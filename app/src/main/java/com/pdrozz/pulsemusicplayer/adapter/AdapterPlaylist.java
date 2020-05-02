package com.pdrozz.pulsemusicplayer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.pdrozz.pulsemusicplayer.R;
import com.pdrozz.pulsemusicplayer.model.PlaylistModel;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdapterPlaylist extends RecyclerView.Adapter<AdapterPlaylist.ViewHolderPlaylist> {

    private List<PlaylistModel> listPlaylist=new ArrayList<>();

    @NonNull
    @Override
    public ViewHolderPlaylist onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_grid_playlists,
                parent,false);
        return new ViewHolderPlaylist(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPlaylist holder, int position) {
        PlaylistModel model=listPlaylist.get(position);
        holder.textViewNamePlaylist.setText(model.getName());
        if (model.getACTION()!=0){
            holder.textViewNamePlaylist.setText("Crie uma playlist agora");
            holder.buttonPlaylist.setText("Criar");
        }
        else{
            holder.textViewNamePlaylist.setText(model.getName());
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
