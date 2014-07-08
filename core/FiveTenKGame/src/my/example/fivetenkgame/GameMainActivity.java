package my.example.fivetenkgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class GameMainActivity extends Activity {

	private Button m_newGameButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_main);
		
		m_newGameButton = (Button)findViewById(R.id.main_new_game_id);
		
		m_newGameButton.setOnClickListener(m_clickListener);
	}
	
	private OnClickListener m_clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			if( v == m_newGameButton ){
				Intent intent = new Intent();
				intent.setClass(GameMainActivity.this, WaitingGameActivity.class);
				startActivity(intent);
			}
		
		}
	};
}
