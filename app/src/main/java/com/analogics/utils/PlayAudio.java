package com.analogics.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import com.analogics.R;



public class PlayAudio {


    public void playSuccess(Context mContext){
        MediaPlayer mp = new MediaPlayer( );
        mp = MediaPlayer.create(mContext, R.raw.beep_success);
        mp.start();

    }

    public void playFail(Context mContext){
        MediaPlayer mp = new MediaPlayer( );
        mp = MediaPlayer.create(mContext, R.raw.beep_error);
        mp.start();

    }

    public void playSound(Context context, String fileName){

        MediaPlayer mp=new MediaPlayer();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }

        });
        try{
            AssetFileDescriptor afd = context.getAssets().openFd(fileName);
        //    mp.setAudioStreamType(AudioManager.STREAM_RING); //set streaming according to ur needs
            mp.setVolume(100f, 100f);
            mp.setDataSource(afd.getFileDescriptor());
            mp.prepare();
            mp.start();

        }catch(Exception e){
            e.printStackTrace();
            if (!(mp == null) && mp.isPlaying())
            {
                mp.stop();
                mp.release(); //its a very good practice
            }
        }


    }

}
