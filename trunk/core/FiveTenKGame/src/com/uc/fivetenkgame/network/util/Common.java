package com.uc.fivetenkgame.network.util;

/**
 * �����࣬һЩ�������ݶ���
 *
 */
public class Common {

	//������
	public static final int PLAYER1_NUM = 1;
	public static final int PLAYER2_NUM = 2;
	public static final int PLAYER3_NUM	= 3;
	//���������
	public static final int TOTAL_PLAYER_NUM = 3;
	
	//������Ϣͷ��Ŷ���
	public static final String PLAYER_ACCEPTED = "1#";
	public static final String PLAYER_REFUSED = "2#";
	public static final String BEGIN_GAME = "3#";
	public static final String SEND_CARDS = "4#";
	public static final String YOUR_TURN = "5#";
	public static final String PLAYER_CARDS = "6#";
	public static final String ROUND_SCORE = "7#";
	public static final String PLAYERS_SCORE = "8#";
	public static final String PLAYERS_REMAIN_CARDS ="9#";
	public static final String GAME_OVER = "10#";
	public static final String WINNING_PLAYER = "11#";
	public static final String PLAY_CARDS = "12#";
	public static final String GIVE_UP = "0#";
	
	//handler��Ϣ������
	public static final int UPDATE_WAITING_PLAYER_NUM = 1;
	public static final int START_GAME = 2;
	
	//������״̬�ı��ڲ���Ϣ
	public static final String SERVER_LISTENING = "1@";
	public static final String GAME_START = "2@";
	public static final String GAME_END = "3@";
	
}
