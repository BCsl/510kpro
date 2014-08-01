package com.uc.fivetenkgame;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import my.example.fivetenkgame.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.uc.fivetenkgame.application.GameApplication;
import com.uc.fivetenkgame.common.NetworkCommon;
import com.uc.fivetenkgame.common.SharePerferenceCommon;
import com.uc.fivetenkgame.common.SoundPoolCommon;
import com.uc.fivetenkgame.player.Player;
import com.uc.fivetenkgame.view.GameView;

public class GameViewActivity extends Activity {

	private AlertDialog backPressDialog;// 本玩家按返回键出现的dialog
	private AlertDialog pauseDialog;// 其他玩家暂停时本玩家出现的dialog
	private AlertDialog winningDialog;// 游戏结束时出现的dialog
	private AlertDialog waitForRestartDialog;// 重玩时等待其他玩家的dialog
	private AlertDialog mHistoryScoreDialog;// 重玩时等待其他玩家的dialog
	private View mWinningView;
	private View mHistoryView;
	private ListView mListView;
	private ArrayList<String> mHistoryList;
	private GameView mView;
	private GameApplication mGameApplication;
	private HistoryAdapter mHistoryAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Player.getInstance().setHistoryRecordPath(
				getApplicationContext().getFilesDir().getAbsolutePath());
		Log.i(TAG, "oncreate");
		// 隐藏标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 隐藏状态栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		// 锁定横屏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		mView = new GameView(getApplicationContext(), Player.getInstance()
				.getPlayerNumber(), mHandler);
		mHistoryList = new ArrayList<String>();
		Player.getInstance().setViewControler(mView.getViewControler());
		Player.getInstance().setEventListener();
		Player.getInstance().setHandler(mHandler);
		Player.getInstance().initView();
		((GameApplication) getApplication())
				.playSound(SoundPoolCommon.SOUND_GAME_START);
		setContentView(mView);
		mGameApplication = (GameApplication) getApplication();
		mWinningView = LayoutInflater.from(this).inflate(
				R.layout.dialog_winning, null);
		mHistoryView = LayoutInflater.from(this).inflate(
				R.layout.dialog_history, null);
		mListView = (ListView) mHistoryView
				.findViewById(R.id.history_score_list);
		mHistoryAdapter = new HistoryAdapter(this, mHistoryList);
		mListView.setAdapter(mHistoryAdapter);
		initDialog();
	}

	private void UpdateHistoryList() {
		List temp;
		if ((temp = Player.getInstance().getPlayerGameHistory()) != null
				&& temp.size() > 0) {
			mHistoryList.clear();
			mHistoryList.addAll(temp);
			temp.clear();
			if ((temp = Player.getInstance().getPlayerMoney()) != null
					&& temp.size() > 0) {
				mHistoryList.addAll(temp);
			}
		}
		if (temp != null && temp.size() > 0)
			temp.clear();
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			Log.i(TAG, "handler receive msg:" + msg);
			switch (msg.what) {
			case NetworkCommon.END_GAME:
				String[] res = (String[]) msg.obj;
				if (Integer.valueOf(res[0]) == Player.getInstance()
						.getPlayerNumber())
					((GameApplication) getApplication())
							.playSound(SoundPoolCommon.SOUND_WIN);
				else
					((GameApplication) getApplication())
							.playSound(SoundPoolCommon.SOUND_FAILD);
				showWinningDialog(res);
				break;

			case NetworkCommon.PLAY_RESTART:
				waitForRestartDialog.cancel();
				break;

			case NetworkCommon.GAME_STATE_CHANGE:
				String objMsg = (String) msg.obj;
				Log.i("objMsg is ", objMsg.length() + "");
				if (objMsg.startsWith(NetworkCommon.GAME_PAUSE)) {
					if (!mGameApplication.isPause()) {
						pauseDialog.show();// 其他玩家通知
						mGameApplication.setPause(true);
						Log.i("pauseDialog", "show");
					}
				} else if (objMsg.startsWith(NetworkCommon.GAME_RESUME)) {
					if (mGameApplication.isPause()) {
						pauseDialog.cancel();// 其他玩家通知
						mGameApplication.setPause(false);
						Log.i("pauseDialog", "cancel");
					}
				} else if (objMsg.startsWith(NetworkCommon.GAME_EXIT)) {
					Timer mTimer = new Timer();
					mTimer.schedule(new TimerTask() {

						@Override
						public void run() {
							GameViewActivity.this.finish();
						}
					}, 1000);
					Toast.makeText(getApplicationContext(), "其他玩家退出游戏",
							Toast.LENGTH_LONG).show();
				}
				break;

			case NetworkCommon.SHOW_HISTORY:
				showHistoryScoreDialog();
				break;
			}

		}
	};

	private void initDialog() {
		// 设置backPressDialog
		backPressDialog = new AlertDialog.Builder(this)
				.setTitle("暂停")
				.setPositiveButton("返回", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mGameApplication.setPause(false);
						Player.getInstance().sendMsg(NetworkCommon.GAME_RESUME);// 恢复游戏
					}
				})
				.setNegativeButton("退出", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Player.getInstance().sendMsg(
								NetworkCommon.GAME_EXIT
										+ Player.getInstance()
												.getPlayerNumber());
						GameViewActivity.this.finish();// 退出游戏
					}
				}).create();
		backPressDialog.setCanceledOnTouchOutside(false);
		backPressDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				mGameApplication.setPause(false);
				Player.getInstance().sendMsg(NetworkCommon.GAME_RESUME);// 再次按返回键，返回游戏
			}
		});

		// 设置pauseDialog
		pauseDialog = new AlertDialog.Builder(this)
				.setTitle("其他玩家暂停游戏")
				.setPositiveButton("不想等了，退出本次游戏",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Player.getInstance().sendMsg(
										NetworkCommon.GAME_EXIT);
								GameViewActivity.this.finish();
							}
						}).create();
		pauseDialog.setCancelable(false);// 除了退出游戏外，只能等待其他玩家

		winningDialog = new AlertDialog.Builder(this)
				.setTitle(getResources().getString(R.string.game_over))
				.setView(mWinningView)
				.setNegativeButton("退出", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				})
				.setPositiveButton("重玩", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Player.getInstance().sendMsg(NetworkCommon.PLAY_AGAIN);
						showWaitForRestartDialog();
					}
				}).setCancelable(false).create();

		mHistoryScoreDialog = new AlertDialog.Builder(this)
				.setTitle(getResources().getString(R.string.history_score))
				.setView(mHistoryView)
				.setPositiveButton(
						getResources().getString(R.string.confirm_str),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						}).setCancelable(true).create();
	}

	protected void showWinningDialog(String[] res) {
		((TextView) mWinningView.findViewById(R.id.text_winning_player))
				.setText(getResources().getString(R.string.winner).replace("#",
						res[0]));
		((TextView) mWinningView.findViewById(R.id.text_score_player1))
				.setText(getResources().getString(R.string.player1_score)
						.replace("#", res[1]));
		((TextView) mWinningView.findViewById(R.id.text_score_player2))
				.setText(getResources().getString(R.string.player2_score)
						.replace("#", res[2]));
		((TextView) mWinningView.findViewById(R.id.text_score_player3))
				.setText(getResources().getString(R.string.player3_score)
						.replace("#", res[3]));
		winningDialog.show();
	}

	protected void showWaitForRestartDialog() {
		if (waitForRestartDialog == null) {
			View waitForRestartView = LayoutInflater.from(this).inflate(
					R.layout.dialog_wait_restart, null);
			waitForRestartDialog = new AlertDialog.Builder(this)
					.setTitle(R.string.game_restart)
					.setView(waitForRestartView).setCancelable(false).create();
		}
		waitForRestartDialog.show();
	}

	protected void showHistoryScoreDialog() {
		UpdateHistoryList();
		if (mHistoryScoreDialog != null) {
			mHistoryAdapter.notifyDataSetChanged();
			mHistoryScoreDialog.show();
		}

	}

	@Override
	public void onBackPressed() {
		backPressDialog.show();
		mGameApplication.setPause(true);
		Player.getInstance().sendMsg(NetworkCommon.GAME_PAUSE);// 暂停游戏
	}

	private String TAG = "GameViewActivity";

	@Override
	protected void onResume() {
		Log.i(TAG, "onResume " + mGameApplication.isPause());
		super.onResume();
		mView.initHolder();
		if (mGameApplication.isPause()) {
			mGameApplication.setPause(true);
			Player.getInstance().sendMsg(NetworkCommon.GAME_RESUME);// 通知其他玩家恢复游戏
		}
	}

	protected void onPause() {
		super.onPause();
		Log.i(TAG, "onPause");

		Player.getInstance().sendMsg(NetworkCommon.GAME_PAUSE);// 通知其他玩家暂停游戏
		mGameApplication.setPause(true);
	};

	@Override
	protected void onDestroy() {
		Player.getInstance().resetPlayer();
		super.onDestroy();
	}

	private class HistoryAdapter extends BaseAdapter {
		private Context mContext;
		private List<String> mScores;
		private int[] colors = { Color.WHITE, Color.rgb(219, 238, 244) };
		private SharedPreferences mSharedPreferences;

		public HistoryAdapter(Context context, List<String> score) {
			this.mContext = context;
			this.mScores = score;
			mSharedPreferences = context.getSharedPreferences(
					SharePerferenceCommon.TABLE_PLAYERS, MODE_PRIVATE);
		}

		@Override
		public int getCount() {
			return mScores == null ? 0 : mScores.size() / 3 + 1;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			ViewHolder vh = null;
			if (view == null) {
				vh = new ViewHolder();
				view = LayoutInflater.from(mContext).inflate(
						R.layout.history_list_item, null);
				vh.tv_round = (TextView) view
						.findViewById(R.id.history_game_round);
				vh.tv_player1 = (TextView) view
						.findViewById(R.id.history_player1);
				vh.tv_player2 = (TextView) view
						.findViewById(R.id.history_player2);
				vh.tv_player3 = (TextView) view
						.findViewById(R.id.history_player3);
				view.setTag(vh);
			}
			vh = (ViewHolder) view.getTag();
			if (position == 0) {
				initView(vh);
				view.setBackgroundColor(colors[position % 2]);// 每隔item之间颜色不同
				return view;
			}
			if (position == getCount()) {

				vh.tv_round.setText(mContext.getResources().getString(
						R.string.histor_final));
			} else {

				vh.tv_round.setText(mContext.getResources()
						.getString(R.string.histor_rounds)
						.replace("#", String.valueOf(position)));
			}
			vh.tv_player1.setText(mScores.get((position - 1) * 3));
			vh.tv_player2.setText(mScores.get((position - 1) * 3 + 1));
			vh.tv_player3.setText(mScores.get((position - 1) * 3 + 2));
			view.setBackgroundColor(colors[position % 2]);// 每隔item之间颜色不同
			return view;
		}

		public void initView(ViewHolder vh) {
			vh.tv_round
					.setText(getResources().getString(R.string.histor_round));
			vh.tv_player1.setText(mSharedPreferences.getString(
					SharePerferenceCommon.FIELD_PAAYER1,
					SharePerferenceCommon.FIELD_PAAYER1));
			vh.tv_player2.setText(mSharedPreferences.getString(
					SharePerferenceCommon.FIELD_PAAYER2,
					SharePerferenceCommon.FIELD_PAAYER2));
			vh.tv_player3.setText(mSharedPreferences.getString(
					SharePerferenceCommon.FIELD_PAAYER3,
					SharePerferenceCommon.FIELD_PAAYER3));
		}

	}

	class ViewHolder {
		TextView tv_round;
		TextView tv_player1;
		TextView tv_player2;
		TextView tv_player3;
	}
}
