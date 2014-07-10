package com.uc.fivetenkgame;

import java.util.ArrayList;
import java.util.List;

import com.uc.fivetenkgame.view.GameView;
import com.uc.fivetenkgame.view.entity.Card;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class TestActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Òþ²Ø±êÌâÀ¸
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Òþ²Ø×´Ì¬À¸
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// Ëø¶¨ºáÆÁ
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		GameView view =new GameView(getApplicationContext(), null,3,3);

		setContentView(view);
		
	}

}
