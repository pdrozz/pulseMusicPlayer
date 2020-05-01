package com.pdrozz.pulsemusicplayer.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionUtil {

    public static boolean getPermission(String[] permissions, Activity activity,int requestCode){
        List<String> permissoesParaPedir=new ArrayList<>();
        for (String permissao:permissions){
            boolean haverPermission= ContextCompat.checkSelfPermission(activity,permissao)
                    == PackageManager.PERMISSION_GRANTED;
            if (!haverPermission){
                permissoesParaPedir.add(permissao);
            }
        }
        if (permissoesParaPedir.isEmpty())return true;
        else {
             String[] getPermissions=new String[permissoesParaPedir.size()];
            permissoesParaPedir.toArray(getPermissions);

            ActivityCompat.requestPermissions(activity,getPermissions,requestCode);
        }
        return true;
    }
}
