package com.example.testseekbar;

import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.app.Activity;
import android.content.ContentResolver;
import android.util.Log;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends Activity {

	private int mScreenBrightness;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initSeekBar();
	}

	private void initSeekBar() {
		SeekBar mSeekBar = (SeekBar) findViewById(R.id.seekbar);
		mSeekBar.setMax(255);
		mSeekBar.setProgress(getScreenBrightness());
		mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				saveScreenBrightness();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				Log.i("cyl", "onProgressChanged(" + seekBar + " , " + progress
						+ "  ," + fromUser + ")");
				if (fromUser) {
					WindowManager.LayoutParams lp = getWindow().getAttributes();
					lp.screenBrightness = (float) (progress / (255 * 1.0));
					mScreenBrightness = progress;
					getWindow().setAttributes(lp);
				}
			}
		});
	}

	private int getScreenBrightness() {
		int nowBrightness = 0;
		ContentResolver resolver = getContentResolver();
		try {
			nowBrightness = android.provider.Settings.System.getInt(resolver,
					Settings.System.SCREEN_BRIGHTNESS);
		} catch (SettingNotFoundException e) {
			e.printStackTrace();
		}
		return nowBrightness;
	}

	private void saveScreenBrightness() {
		ContentResolver resolver = getContentResolver();
		Uri uri = android.provider.Settings.System
				.getUriFor("screen_brightness");
		android.provider.Settings.System.putInt(resolver, "screen_brightness",
				mScreenBrightness);
		resolver.notifyChange(uri, null);
	}
}
