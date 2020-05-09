package com.pdrozz.pulsemusicplayer.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pdrozz.pulsemusicplayer.R;
import com.pdrozz.pulsemusicplayer.model.MusicModel;
import com.pdrozz.pulsemusicplayer.sqlHelper.DAOplaylist;
import com.pdrozz.pulsemusicplayer.widget.favbutton.FavButton;

import java.util.ArrayList;
import java.util.List;

public class AdapterMusicFiles extends RecyclerView.Adapter<AdapterMusicFiles.MeuViewHolder> {

    private List<MusicModel> listMusic=new ArrayList<>();
    private List<MusicModel> selecionado=new ArrayList<>();
    private Activity activity;
    private DAOplaylist daoPlaylist;
    private String namePlaylist;

    public void setNamePlaylist(String namePlaylist) {
        this.namePlaylist = namePlaylist;
    }

    public AdapterMusicFiles(List<MusicModel> listMusic, Activity activity) {
        this.listMusic=listMusic;
        this.activity=activity;
        daoPlaylist=new DAOplaylist(activity);
    }

    @NonNull
    @Override
    public MeuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_music_item,parent,false);
        return new MeuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MeuViewHolder holder, int position) {
        final MusicModel model=listMusic.get(position);

        int totalDuration=Integer.parseInt(model.getDuration());

        String duration=(totalDuration/1000/60)+"";
        duration=duration+":"+(totalDuration/1000%60);




        holder.name.setText(model.getName());
        holder.artist.setText(model.getArtist());
        holder.duration.setText(duration);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    selecionado.add(model);
                }else{
                    selecionado.remove(model);
                }
            }
        });

        holder.name.setSelected(true);
        holder.artist.setSelected(true);

    }

    @Override
    public int getItemCount() {
        return listMusic.size();
    }

    public void saveSelected(){
        for (int i=0;i<selecionado.size();i++){
            Log.i("selecionado", "saveSelected: "+selecionado.get(i).getName());
            daoPlaylist.inserirNaPlaylist(namePlaylist,selecionado.get(i));
        }
        Toast.makeText(activity, "Adicionado com sucesso", Toast.LENGTH_SHORT).show();
    }

    public class MeuViewHolder extends RecyclerView.ViewHolder{
        ImageView imageMusic;
        TextView name,artist,duration;
        ImageButton favButton;
        CheckBox checkBox;
        public MeuViewHolder(@NonNull View itemView) {
            super(itemView);

            imageMusic=itemView.findViewById(R.id.imageMusic);
            name=itemView.findViewById(R.id.textMusicName);
            artist=itemView.findViewById(R.id.textArtist);
            duration=itemView.findViewById(R.id.textDuration);
            checkBox=itemView.findViewById(R.id.adicionar);

        }
    }

}

