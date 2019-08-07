package com.guahoo.eggz.Utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Objects;

public class PlaySound {




    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void playSound(SharedPreferences sharedPreferences, Context context, int resid) {
        if (Objects.equals ( sharedPreferences.getString ( "soundOff", null ), "on" )) {
            final MediaPlayer mp = MediaPlayer.create ( context,resid );
            mp.start ();
        }
    }
}
