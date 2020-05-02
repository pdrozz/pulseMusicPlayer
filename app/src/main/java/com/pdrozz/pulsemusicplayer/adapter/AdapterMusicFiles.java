package com.pdrozz.pulsemusicplayer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pdrozz.pulsemusicplayer.R;
import com.pdrozz.pulsemusicplayer.model.MusicModel;
import com.pdrozz.pulsemusicplayer.widget.favbutton.FavButton;

import java.util.ArrayList;
import java.util.List;

public class AdapterMusicFiles extends RecyclerView.Adapter<AdapterMusicFiles.MeuViewHolder> {

    private List<MusicModel> listMusic=new ArrayList<>();

    public AdapterMusicFiles( List<MusicModel> listMusic) {
        this.listMusic=listMusic;
    }

    @NonNull
    @Override
    public MeuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_music_item,parent,false);
        return new MeuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MeuViewHolder holder, int position) {
        MusicModel model=listMusic.get(position);

        holder.name.setText(model.getName());
        holder.artist.setText(model.getArtist());
        holder.duration.setText(model.getDuration());

    }

    @Override
    public int getItemCount() {
        return listMusic.size();
    }

    public class MeuViewHolder extends RecyclerView.ViewHolder{
        ImageView imageMusic;
        TextView name,artist,duration;
        ImageButton favButton;
        public MeuViewHolder(@NonNull View itemView) {
            super(itemView);

            imageMusic=itemView.findViewById(R.id.imageMusic);
            name=itemView.findViewById(R.id.textMusicName);
            artist=itemView.findViewById(R.id.textArtist);
            duration=itemView.findViewById(R.id.textDuration);

        }
    }

}

