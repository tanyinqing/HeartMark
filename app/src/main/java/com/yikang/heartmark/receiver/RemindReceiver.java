package com.yikang.heartmark.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.yikang.heartmark.activity.RemindSoundActivity;
import com.yikang.heartmark.model.Alarm;

  /*这是一个广播接受器，去启动一个闹钟的功能   启动闹钟的的广播接收器   */
public class RemindReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent intentSound = new Intent(context, RemindSoundActivity.class);
		
		Alarm alarm = (Alarm) intent.getSerializableExtra("alarm");
		
		Bundle bundle = new Bundle();
		bundle.putSerializable("remindAlarm", alarm);
		intentSound.putExtras(bundle);
		intentSound.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //在新的进程中 打开意图
		
		context.startActivity(intentSound);
		
		/**
		
		try {
			MediaPlayer	player = new MediaPlayer();
			//播放完毕监听
			player.setOnCompletionListener(new OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
//                	playCount--;
//                    if(playCount > 0){
//                    	player.start();
//                    }else{
//                    	player.stop();
//                    	playCount = 3;
//                    }
                }
            });
			Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
			player.setDataSource(context, alert);
			final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
			if (audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION) != 0) {
				player.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
				player.setLooping(false);
				player.prepare();
				player.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		 */
	}
	


}