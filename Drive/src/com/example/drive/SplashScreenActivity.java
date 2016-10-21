package com.example.drive;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class SplashScreenActivity extends Activity {

	private AnimationDrawable mAnimation;
	private ImageView mAnimLogo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		mAnimLogo = (ImageView) findViewById(R.id.loading_image);
		mAnimation = (AnimationDrawable) mAnimLogo.getDrawable();
		mAnimLogo.post(new Runnable() {
				@Override
				public void run() {
					Log.d("Drawable","run of displating");
					mAnimLogo.setVisibility(View.VISIBLE);
					mAnimation.start();
				}
			});
		Log.d("Drawable","displayed");
		new Handler().postDelayed(new Runnable() {
            public void run() {
            	Log.d("Drawable","in run of closing");
                    startActivity(new Intent(SplashScreenActivity.this,MainActivity.class));
                    finish();
            }
    },4200);
	}
}