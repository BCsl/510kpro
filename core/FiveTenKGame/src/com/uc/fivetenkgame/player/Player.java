package com.uc.fivetenkgame.player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.util.EncodingUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.util.Log;

import com.uc.fivetenkgame.application.GameApplication;
import com.uc.fivetenkgame.common.CommonMsgDecoder;
import com.uc.fivetenkgame.common.NetworkCommon;
import com.uc.fivetenkgame.common.SharePerferenceCommon;
import com.uc.fivetenkgame.common.SoundPoolCommon;
import com.uc.fivetenkgame.network.NetworkInterface;
import com.uc.fivetenkgame.network.OnReceiveMessageListener;
import com.uc.fivetenkgame.ruleController.IRule;
import com.uc.fivetenkgame.ruleController.RuleManager;
import com.uc.fivetenkgame.state.State;
import com.uc.fivetenkgame.state.playerstate.InitState;
import com.uc.fivetenkgame.state.playerstate.PlayerState;
import com.uc.fivetenkgame.util.OredrUtil;
import com.uc.fivetenkgame.view.EventListener;
import com.uc.fivetenkgame.view.IViewControler;
import com.uc.fivetenkgame.view.entity.Card;
import com.uc.fivetenkgame.view.util.CardUtil;

/**
 * 玩家类, 修改：player类不再作为抽象类，与clientplayer类合并
 * 
 * @author fuyx
 * 
 */
@SuppressLint("UseValueOf")
public class Player implements PlayerContext {
	private IRule mRule;
	private State mState;
	private boolean doneHandCard = false;
	private boolean isRestart = false;
	private IViewControler viewController;
	private Context mApplicationContext;
	private OnReceiveMessageListener mReceiveMessage = new OnReceiveMessageListener() {

		@Override
		public void reveiveMessage(String msg) {
			Log.i("player receive msg", msg);
			if (CommonMsgDecoder.checkMessage(msg, NetworkCommon.GAME_PAUSE)
					|| CommonMsgDecoder.checkMessage(msg,
							NetworkCommon.GAME_RESUME)
					|| CommonMsgDecoder.checkMessage(msg,
							NetworkCommon.GAME_EXIT)) {
				mHandler.obtainMessage(NetworkCommon.GAME_STATE_CHANGE, msg)
						.sendToTarget(); // 由activity处理
				Log.i("!!!player", "msg sent to target");
			} else {
				mState.handle(msg); // 由状态机处理
			}
		}
	};
	private int currentPlayer;
	private PlayerModel mPlayerModel;
	private NetworkInterface mNetworkManager;
	private Handler mHandler;
	private List<Card> formerCardList;
	private List<Card> mHandList;
	private EventListener mEventListener = new EventListener() {
		@Override
		public boolean handCard(List<Card> handList, boolean timeOut) {
			if (timeOut) {
				if (currentPlayer == mPlayerModel.getPlayerNumber()) {
					outTimeAction();
				} else {
					return false;
				}

			}
			mHandList = new ArrayList<Card>();
			if (formerCardList == null) {
				// 第一次出牌
				if (handList == null) {
					// 第一个玩家不能放弃出牌
					viewController.handCardFailed();
					return false;
				} else if (mRule.firstPlayCards(handList) == 1) {
					mHandList.addAll(handList);
					CardUtil.removeCards(mPlayerModel.getCardList(), handList);
					setDoneHandCards(true);
					Log.i("当前手牌数",
							String.valueOf(mPlayerModel.getRemainCardsNum()));
				} else {
					// 不能什么牌都不点就出牌
					viewController.handCardFailed();
					return false;
				}
			} else {
				Log.i("formerCardList", formerCardList.toString());
				if (handList == null) {
					mHandList = null;
					setDoneHandCards(true);
					Log.i("当前手牌数",
							String.valueOf(mPlayerModel.getRemainCardsNum()));
				} else if (handList.size() == 0) {
					viewController.handCardFailed();
					return false;
				} else if (mRule.checkCards(handList, formerCardList) == 1) {
					mHandList.addAll(handList);

					CardUtil.removeCards(mPlayerModel.getCardList(), handList);
					OredrUtil.setOrder(mHandList);

					setDoneHandCards(true);
					Log.i("当前手牌数",
							String.valueOf(mPlayerModel.getRemainCardsNum()));
				} else {
					// 不能什么牌都不点就出牌
					viewController.handCardFailed();
					return false;
				}
			}
			// 出牌成功，将牌交给状态机处理
			String cardString = getCardsToBePlayed();
			if (cardString == null) {// 放弃
				handle(NetworkCommon.GIVE_UP + getPlayerNumber());
			} else {// 选完牌
				handle(NetworkCommon.PLAY_CARDS + cardString);
			}

			return true;
		}
	};
	private String mHistoryPath;
	private String mRuleName;
	int[] historyMoney = null;

	public void startPlay(String addr, String name) {
		mPlayerModel.setPlayerName(name);
		Log.i("start play", "player name: " + mPlayerModel.getPlayerName());
		setState(new InitState(this));
		handle(addr);
	}

	public void setViewControler(IViewControler viewControler) {
		this.viewController = viewControler;
	}

	// 设置规则
	private void setRule(String ruleName) {
		mRule = new RuleManager().getRule(ruleName);
		Log.i(this.getClass().getName() + ": setRule", mRule.getRuleName());
	}

	// 启动网络模块
	public void initNetwork(String addr) {
		mNetworkManager.initNetwork(addr);
	}

	private static Player gInstance;// 唯一的Player实例

	// 返回一个唯一的player实例
	public static Player getInstance() {
		if (null == gInstance) {
			gInstance = new Player();
		}

		return gInstance;
	}

	public void setHandler(Handler handler) {
		mHandler = handler;
	}

	public Handler getHandler() {
		return mHandler;
	}

	public void setState(PlayerState state) {
		mState = state;
	}

	private Player() {
		// mNetworkManager = ClientManager.getInstance();
		// mNetworkManager.setOnReceiveMessage(mReceiveMessage);
		mPlayerModel = new PlayerModel();
		mRuleName = "BasicRule";

		setRule(mRuleName);
		setState(new InitState(this));
		// mPlayerModel.setCardList(null);
		// mPlayerModel.setPlayerNumber(-1);
		// mPlayerModel.setScore(0);
	}

	public void setNetworkManager(NetworkInterface networkInterface) {
		mNetworkManager = networkInterface;
		mNetworkManager.setOnReceiveMessage(mReceiveMessage);
	}

	public void setInitPlayerCards(String cards) {
		Log.i("初始手牌:", cards);
		String[] tCard = cards.split(",");
		ArrayList<Card> cardList = new ArrayList<Card>();
		for (int i = 0, count = tCard.length; i < count; i++) {
			cardList.add(new Card(tCard[i]));
		}
		mPlayerModel.setCardList(cardList);
		// List<Integer> number = new ArrayList<Integer>();
		// number.add(cardList.size());
		// number.add(cardList.size());
		// number.add(cardList.size());
		// viewController.setCardNumber(number);
	}

	/**
	 * 
	 * @return null, if no card to be played
	 */
	public String getCardsToBePlayed() {
		if (formerCardList == null) {
			if (mHandList == null) {
				Log.i("第一个出牌的人不能放弃", "不应该到这里");
			}
		}
		if (mHandList == null)
			return null;
		StringBuffer sb = new StringBuffer();
		for (int i = 0, count = mHandList.size(); i < count; i++) {
			sb.append(mHandList.get(i).getCardId());
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		return new String(sb);
	}

	@Override
	public void sendMsg(String msg) {
		mNetworkManager.sendMessage(msg);
	}

	@SuppressLint("UseValueOf")
	@Override
	public void roundEndAction(String[] playerScore) {
		List<Integer> score = new ArrayList<Integer>();
		score.add(new Integer(playerScore[0]));
		score.add(new Integer(playerScore[1]));
		score.add(new Integer(playerScore[2]));
		formerCardList = null;
		mPlayerModel.setScore(Integer.parseInt(playerScore[mPlayerModel
				.getPlayerNumber() - 1]));
		viewController.setScroeList(score);
		viewController.roundOver();
	}

	@Override
	public void playCardsEndAction(String[] outList, String playerNumber,
			String tableScore, String[] remainCards) {
		if (outList == null)
			return;

		// 设置outList
		List<Card> cardList = new ArrayList<Card>();
		for (int i = 0, count = outList.length; i < count; i++) {
			cardList.add(new Card(outList[i]));
		}
		if (mRule.isBoom(cardList))
			((GameApplication) mApplicationContext.getApplicationContext())
					.playSound(SoundPoolCommon.SOUND_BOOM);
		viewController.setPlayersOutList(Integer.parseInt(playerNumber),
				cardList);
		viewController.setPlayersOutList(Integer.parseInt(playerNumber),
				cardList);
		if (cardList != null)
			formerCardList = cardList;

		// 设置剩余牌数
		List<Integer> cardScore = new ArrayList<Integer>();
		cardScore.add(Integer.valueOf(remainCards[0]));
		cardScore.add(Integer.valueOf(remainCards[1]));
		cardScore.add(Integer.valueOf(remainCards[2]));
		viewController.setCardNumber(cardScore);

		// 设置当前分数
		viewController.setGameScore(Integer.parseInt(tableScore));
	}

	@Override
	public void handle(String msg) {
		mState.handle(msg);
	}

	@Override
	public void setPlayerNumber(int playerNumber) {
		mPlayerModel.setPlayerNumber(playerNumber);
	}

	@Override
	public int getPlayerNumber() {
		// Log.i("getPlayerNumber:",String.valueOf(mPlayerModel.getPlayerNumber()));
		return mPlayerModel.getPlayerNumber();
	}

	@Override
	public void gameOver(String[] str) {
		viewController.gameOver();
		formerCardList = null;
		mHandler.obtainMessage(NetworkCommon.END_GAME, str).sendToTarget();
	}

	public void setEventListener() {
		viewController.setEventListener(mEventListener);
	}

	@Override
	/**
	 * 初始化游戏界面
	 * 
	 */
	public void initView() {
		viewController.setCards(mPlayerModel.getCardList());
		// 剩余牌数
		List<Integer> leftCardsNum = new ArrayList<Integer>();
		leftCardsNum.add(new Integer(36));
		leftCardsNum.add(new Integer(36));
		leftCardsNum.add(new Integer(36));
		viewController.setCardNumber(leftCardsNum);
	}

	@Override
	public boolean doneHandCards() {
		return doneHandCard;
	}

	@Override
	public void setDoneHandCards(boolean flag) {
		// TODO Auto-generated method stub
		doneHandCard = flag;
	}

	@Override
	public void setMyTurn(boolean flag) {
		Log.i("setMyTurn", String.valueOf(flag));
		viewController.setMyTurn(flag);
	}

	@Override
	public boolean isFirstPlayer() {
		if (formerCardList == null)
			return true;
		else
			return false;
	}

	@Override
	public void setCurrentPlayer(int palyerId) {
		while (viewController == null)
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		currentPlayer = palyerId;
		viewController.setCurrentPlayer(palyerId);
	}

	@Override
	public boolean hasCard() {
		if (mPlayerModel.getRemainCardsNum() == 0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void playerGiveUp(int playerId) {
		List<Card> cardList = new ArrayList<Card>();
		cardList.add(new Card(Card.CARD_PASS_ID));
		viewController.setPlayersOutList(playerId, cardList);
	}

	@Override
	public void resetPlayer() {
		mPlayerModel = new PlayerModel();
		formerCardList = null;
		mNetworkManager.reset();
		Log.i("Player", "reset player");
	}

	private void outTimeAction() {
		mHandList = new ArrayList<Card>();
		if (isFirstPlayer()) {
			int cardNumber = mPlayerModel.getCardList().size() - 1;
			Log.i("out time action: ", String.valueOf(cardNumber));
			mHandList.add(mPlayerModel.getCardList().get(cardNumber));
			mPlayerModel.getCardList().removeAll(mHandList);
			viewController
					.setPlayersOutList(-1, new ArrayList<Card>(mHandList));
			Log.i("选牌超时(第一个打牌)",
					"当前手牌数: "
							+ String.valueOf(mPlayerModel.getRemainCardsNum()));
			setDoneHandCards(true);
		} else {
			mHandList = null;
			Log.i("选牌超时(不是第一个打)",
					"当前手牌数: "
							+ String.valueOf(mPlayerModel.getRemainCardsNum()));
			setDoneHandCards(true);
		}
	}

	@Override
	public void reStartGame(String[] str) {
		try {
			addPlayerGameHistory(str);
			updatePlayerMoney(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mPlayerModel.setCardList(null);
		mPlayerModel.setScore(0);
		List<Integer> score = new ArrayList<Integer>();
		score.add(0);
		score.add(0);
		score.add(0);
		viewController.setScroeList(score);
		viewController.setMyTurn(false);
		viewController.setGameScore(0);
		List<Integer> number = new ArrayList<Integer>();
		number.add(36);
		number.add(36);
		number.add(36);
		viewController.setCardNumber(number);
		// 通知gameViewActivity重绘界面

	}

	@Override
	public String getPlayerName() {
		return mPlayerModel.getPlayerName();
	}

	/**
	 * 必须是已经存在的路径
	 * 
	 * @param path
	 */
	public void setHistoryRecordPath(String path) {
		mHistoryPath = path + "player_history.txt";
	}

	private void addPlayerGameHistory(String[] str) throws IOException {
		File file = new File(mHistoryPath);
		if (!file.exists())
			file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file, true);
		byte[] bytes;
		for (int i = 1, count = str.length; i < count; i++) {
			bytes = (str[i] + ",").getBytes();
			try {
				fos.write(bytes);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// int[] money = new int[3];

		// if(score[0]>score[1]&&score[0]>score[1])
		// PlayerMoney[0]+=money[0];
		// PlayerMoney[1]+=money[1];
		// PlayerMoney[2]+=money[2];
	}

	private void updatePlayerMoney(String[] str) {
		if (historyMoney == null) {
			historyMoney = new int[3];
			historyMoney[0] = 0;
			historyMoney[1] = 0;
			historyMoney[2] = 0;
		}
		int[] score = new int[3];
		score[0] = Integer.valueOf(str[1]);
		score[1] = Integer.valueOf(str[2]);
		score[2] = Integer.valueOf(str[3]);
		int MoneyWithoutTheLast = 0; //除了最后一个玩家的钱
		int avgScore = (score[0] + score[1] + score[2]) / 3;
		for (int i = 0, count = historyMoney.length-1; i < count; i++) {
			historyMoney[i] += (score[i] - avgScore);
			MoneyWithoutTheLast += historyMoney[i];
		}
		historyMoney[historyMoney.length-1] = -MoneyWithoutTheLast;
	}

	public List<String> getPlayerGameHistory() {
		String record = null;
		try {
			FileInputStream fis = new FileInputStream(mHistoryPath);
			int length = fis.available();
			byte[] buffer = new byte[length];
			fis.read(buffer);
			record = EncodingUtils.getString(buffer, "UTF-8");
			// Log.i("Player", "getPlayerGameHistory: " + record);
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (record == null || record.length() == 0) {
			Log.i(this.getClass().getName(),
					"getPlayerGameHistory: record == null!");
			return null;
		}
		StringBuffer sb = new StringBuffer(record);
		sb.deleteCharAt(sb.length() - 1);
		record = new String(sb);
		String[] resultStrs = record.split(",");
		List<String> result = new ArrayList<String>();
		for (String str : resultStrs)
			result.add(str);
		Log.i(this.getClass().getName(), "getPlayerGameHistory(), record is "
				+ result.toString());
		return result;
	}

	public List<String> getPlayerMoney() {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < 3; i++) {
			list.add(String.valueOf(historyMoney[i]));
		}
		Log.i(this.getClass().getName(), "getPlayerGameHistory(), money is "
				+ list.toString());
		return list;
	}

	public void setContext(Context context) {
		mApplicationContext = context;

	}

	@Override
	public void setPlayersName(String[] playerNames) {
		Editor editor = mApplicationContext.getSharedPreferences(
				SharePerferenceCommon.TABLE_PLAYERS,
				mApplicationContext.MODE_PRIVATE).edit();
		for (int i = 0; i < playerNames.length; i++) {
			editor.putString(String.valueOf(i + 1), playerNames[i]);
		}
		editor.commit();
	}

	@Override
	public void setRestart(boolean isRestart) {
		this.isRestart = isRestart;
	}

	@Override
	public boolean isRestart() {
		return isRestart;
	}

	@Override
	public void playSound(int soundKey) {
		((GameApplication) mApplicationContext.getApplicationContext())
				.playSound(soundKey);
	}

	public void clearHistoryFile() throws IOException {
		File file = new File(mHistoryPath);
		if (!file.exists()) {
			Log.i(this.getClass().getName(),
					"clearHistoryFile: File don't exist!");
			return;
		}
		file.delete();
		
		historyMoney = new int[3];
		historyMoney[0] = 0;
		historyMoney[1] = 0;
		historyMoney[2] = 0;
	}
}
