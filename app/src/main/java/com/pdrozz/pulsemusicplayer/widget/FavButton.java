package com.pdrozz.pulsemusicplayer.widget;

import android.content.Context;
import android.util.Log;
import android.widget.ImageButton;

import com.pdrozz.pulsemusicplayer.R;

public class FavButton extends androidx.appcompat.widget.AppCompatImageButton {

    private int defaultImage= -1;
    public static int LIKED=1;
    public static int UNLIKED=0;
    private int state=UNLIKED;
    private boolean bLIKED=false;

    public int pedrox;

    private int favImage=defaultImage;
    private int unFavImage=defaultImage;

    public FavButton(Context context) {
        super(context);
    }

    public int getState(){return state;}

    public void setState(int state){
        this.state=state;
        setImage();
    }

    public void setImage(){
        if (state==LIKED){
            setImageResource(favImage);
        }else {
            setImageResource(unFavImage);
        }
    }

    public boolean setupLikeListener(){
        if (isNotDefaultIcon()){
            if (state==UNLIKED){
                state=LIKED;
                setImage();
                return isLiked();
            }
            else {
                state=UNLIKED;
                setImage();
                return isLiked();
            }
        }
        else {
            return isLiked();
        }
    }

    public boolean isLiked(){
        if (state==LIKED){
            return true;
        }else {
            return false;
        }
    }

    private boolean isNotDefaultIcon(){
        if (favImage!=defaultImage && unFavImage!=defaultImage){
            return true;
        }
        else {
            Log.e("Default icons FavButton", "FavButton: setup your icons fav" +
                    " with setFavImages(R.drawable.yourFavIcon, R.drawable.yourUnFavIcon)");
            return false;
        }
    }

    public void setFavImages(int fav,int unFav){
        this.favImage=fav;
        this.unFavImage=unFav;
    }

}
