package com.pdrozz.pulsemusicplayer.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pdrozz.pulsemusicplayer.R;
import com.pdrozz.pulsemusicplayer.activity.CreatePlaylistActivity;
import com.pdrozz.pulsemusicplayer.activity.PlaylistActivity;
import com.pdrozz.pulsemusicplayer.model.PlaylistModel;
import com.pdrozz.pulsemusicplayer.sqlHelper.DAOplaylist;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AdapterPlaylist extends RecyclerView.Adapter<AdapterPlaylist.ViewHolderPlaylist> {

    private List<PlaylistModel> listPlaylist=new ArrayList<>();
    private Activity activity;
    private View.OnClickListener onClickCriar;
    private View.OnClickListener onClickAbrir;
    private Intent action;
    private AlertDialog alertDialog;

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
                    activity.startActivity(new Intent(activity, CreatePlaylistActivity.class));
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.startActivity(new Intent(activity, CreatePlaylistActivity.class));
                }
            });
        }
        else{
            holder.buttonPlaylist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    action=new Intent(activity, PlaylistActivity.class);
                    action.putExtra("playlist",listPlaylist.get(position));
                    activity.startActivity(action);
                }
            });
            holder.textViewNamePlaylist.setText(model.getName().toUpperCase());
            File picture=new File(activity.getFilesDir(),model.getName().toLowerCase()+".jpeg");
            Glide.with(activity).load(picture).into(holder.imageViewPlaylist);

            configViewClick(holder,position);
            configViewLongClick(holder,position);

        }
    }

    @Override
    public int getItemCount() {
        return listPlaylist.size();
    }

    private void configOnClickListenerCriar(){
        onClickCriar=new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
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

    private void configViewLongClick(ViewHolderPlaylist holder,final int position){
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showAlert("Deseja excluir a playlist "+listPlaylist.get(position).getName()
                        ,"Essa ação não pode ser desfeita",
                        position);
                AdapterPlaylist.this.notifyDataSetChanged();
                AdapterPlaylist.this.notifyItemRemoved(position);
                return false;
            }
        });
    }

    private void configViewClick(ViewHolderPlaylist holder,final int position){
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action=new Intent(activity, PlaylistActivity.class);
                action.putExtra("playlist",listPlaylist.get(position));
                activity.startActivity(action);
            }
        });
    }

    private void deletePlaylist(int position){
        DAOplaylist dao=new DAOplaylist(activity);
        boolean result=dao.deletePlaylist(listPlaylist.get(position).getName());
        if (result){
            listPlaylist.remove(position);
            Toast.makeText(activity, "SUCESSO AO DELETAR", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAlert(String title,String desc,final int position){
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(desc);
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deletePlaylist(position);
                AdapterPlaylist.this.notifyItemRemoved(position);
                File picture=new File(activity.getFilesDir(),listPlaylist.get(position)+".jpeg");
                try{
                    picture.delete();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        this.alertDialog=builder.create();
        this.alertDialog.show();
    }

}
