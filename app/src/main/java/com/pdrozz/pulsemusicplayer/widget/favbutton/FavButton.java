package com.pdrozz.pulsemusicplayer.widget.favbutton;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.pdrozz.pulsemusicplayer.R;

public class FavButton extends androidx.appcompat.widget.AppCompatImageButton implements View.OnClickListener {

    private int defaultImage= -1;
    public static int LIKED=1;
    public static int UNLIKED=0;
    private int state;

    private OnFavListener onFavListener;

    private boolean imagesAreSet=false;
    private boolean listenerSet=false;

    private int favImage=defaultImage;
    private int unFavImage=defaultImage;

    public FavButton(Context context) {
        super(context);
    }

    public FavButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FavButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setImagesLike(int likeIcon,int unLikePic){
        this.favImage=likeIcon;
        this.unFavImage=unLikePic;
        this.imagesAreSet=true;
    }

    @Override
    public void onClick(View v) {
        if (state==UNLIKED){
            onFavListener.liked(this);
            state=LIKED;
        }
        else{
            onFavListener.unliked(this);
            state=LIKED;
        }
    }

    private void managerState(int stat){

    }

    @SuppressLint("ResourceAsColor")
    private void setTheColors(int stat){
        if (stat==LIKED){
            setBackgroundColor(R.color.colorAccent);
        }
        else {
            setBackgroundColor(R.color.colorPrimaryDark);
        }
    }

    public void setOnFavListener(OnFavListener onFavListener){
        this.onFavListener=onFavListener;
        this.listenerSet=true;
    }
}
