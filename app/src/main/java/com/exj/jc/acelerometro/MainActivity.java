package com.exj.jc.acelerometro;

import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener, MediaPlayer.OnCompletionListener{

    private Sensor mAccelerometer;
    private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        player = new MediaPlayer();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        try{

            AssetManager manager = this.getAssets();
            AssetFileDescriptor descriptor = manager.openFd("vinuela.mp3");
            player.setDataSource(descriptor.getFileDescriptor(),descriptor.getStartOffset(), descriptor.getLength());

        }catch (Exception e){};
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[SensorManager.DATA_X];
        if (x<-4 && x>-6){
            if (player.isPlaying()==false){

                try{
                    player.prepare();
                }catch (Exception e){};
                player.start();
                player.setOnCompletionListener(this);

            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onCompletion(MediaPlayer player) {
        player.stop();
    }

    protected void onResume(){
        super.onResume();
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> sensors = sm.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (sensors.size()>0){
            sm.registerListener(this, sensors.get(0), SensorManager.SENSOR_DELAY_GAME);

        }

    }

    protected void onPause(){
        SensorManager mSensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorManager.unregisterListener(this, mAccelerometer);
        super.onPause();
    }

    protected void onStop(){
        SensorManager mSensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorManager.unregisterListener(this, mAccelerometer);
        super.onStop();
    }
}
