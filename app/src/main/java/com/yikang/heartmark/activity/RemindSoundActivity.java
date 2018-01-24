package com.yikang.heartmark.activity;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import com.example.heartmark.R;
import com.yikang.heartmark.application.BaseActivity;
import com.yikang.heartmark.model.Alarm;
import com.yikang.heartmark.widget.MyDialog;

@SuppressWarnings("deprecation")
@SuppressLint("Wakelock")
/*作用是播放铃声，弹出对话框，屏幕解锁，屏灯亮*/
public class RemindSoundActivity extends BaseActivity {
	public MediaPlayer player;  //声音相关的类
	public Alarm alarm;
	public String title;
	public String message;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		player = new MediaPlayer();
		Intent intent = this.getIntent();
		alarm = (Alarm) intent.getSerializableExtra("remindAlarm");

		// 获取电源管理器对象
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		// 获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
		PowerManager.WakeLock wl = pm.newWakeLock(
				PowerManager.ACQUIRE_CAUSES_WAKEUP
						| PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
		// 点亮屏幕
		wl.acquire();

		// 得到键盘锁管理器对象
		KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
		// 参数是LogCat里用的Tag
		KeyguardLock kl = km.newKeyguardLock("unLock");
		// 解锁
		kl.disableKeyguard();

		
		
		// 播放铃声
		try {
			Uri alert = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_ALARM);
			
			player.setDataSource(RemindSoundActivity.this, alert);
			
			final AudioManager audioManager = (AudioManager) RemindSoundActivity.this
					.getSystemService(Context.AUDIO_SERVICE);
			
			if (audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION) != 0) {
				player.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
				player.setLooping(false);
				player.prepare();
				player.start();
			}

			// 播放监听 完毕后什么也不做
			player.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 显示dialog  决定弹出框的类型 标题和提示信息
		if (alarm.type.equals(Alarm.TYPE_CELING)) {
			title = "测量提醒";
			message = alarm.time;
		} else if (alarm.type.equals(Alarm.TYPE_YAO)) {
			title = "用药提醒";
			message = alarm.yaoName + "\n" + alarm.time;
		} else if (alarm.type.equals(Alarm.TYPE_HULI)) {
			title = "护理提醒";
			message = alarm.time;
		}
		showDialog(title, message);  //弹出提示框

		
		// 重新启用自动加锁
		//kl.reenableKeyguard();

		wl.release();  //屏灯关闭

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void showDialog(String title, String message) {
		MyDialog dialog = new MyDialog(RemindSoundActivity.this)
				.setTitle(title).setMessage(message)
				.setPositiveButton(R.string.ok, new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						player.stop();
						finish();
					}
				})
				.setNegativeButton(R.string.cancel, new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						player.stop();
						finish();
					}
				});

		dialog.create(null);
		dialog.showMyDialog();
	}
}
