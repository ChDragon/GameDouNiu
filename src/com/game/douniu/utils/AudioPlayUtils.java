package com.game.douniu.utils;

import java.util.HashMap;

import com.game.douniu.R;
import com.game.douniu.custom.SharedPrefHelper;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

public class AudioPlayUtils {
	private static final String				TAG				= "[wzj]AudioPlayUtils";
	
	private static AudioPlayUtils sInstance;
	private static AudioManager sAudioManager;
	private Context mContext;
	
	private final SoundPool					soundPool		= new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
	private final HashMap<Integer, Integer>	soundPoolMap	= new HashMap<Integer, Integer>();
	
	private int soundId[] = new int[] {
			// ÄÐÉù
			
			// Å®Éù
			
			// ÆäËû
			R.raw.game_start, R.raw.game_end, R.raw.game_alert, R.raw.game_warn, R.raw.game_bankerinfo,
			R.raw.click, R.raw.background
			};

	
	public synchronized static AudioPlayUtils getInstance(Context context) {
		if (sInstance == null) {
			sAudioManager = (AudioManager) context
					.getSystemService(Context.AUDIO_SERVICE);
			sInstance = new AudioPlayUtils(context);
		}
		return sInstance;
	}
	
	private AudioPlayUtils(Context context) {
		mContext = context;
		for (int i = 0; i < soundId.length; i++) {
			soundPoolMap
					.put(soundId[i], soundPool.load(context, soundId[i], 1));
		}
	}
	
	public void play(int sound) {
		play(sound, 0);
	}

	public void play(int sound, int loop) {
		if (!SharedPrefHelper.getInstance(mContext).getBoolean("sound", true)) {
			Log.d(TAG, "not play as sound effect off");
			return;
		}
        float audioMaxVolum = sAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float audioCurrentVolum = sAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float audioRatio = 0.05F;//audioCurrentVolum / audioMaxVolum;
		int sd =  soundPoolMap.get(sound);
		if (sd != -1)
			soundPool.play(sd, audioRatio, audioRatio, 1, loop, 1.0F);
		else
			Log.w(TAG, "bad-sound");
	}
}
