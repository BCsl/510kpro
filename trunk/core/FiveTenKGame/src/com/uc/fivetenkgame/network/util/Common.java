package com.uc.fivetenkgame.network.util;

/**
 * �����࣬һЩ�������ݶ���
 * 
 * @author liuzd
 *
 */
public class Common {

	//������
	public static final int SERVER_NUM	= 1;
	public static final int PLAYER1_NUM = 2;
	public static final int PLAYER2_NUM = 3;
	
	//������Ϣͷ��Ŷ���
	public static final String PLAYER_ACCEPTED = "1#";//���ӳɹ��źţ�1#+������
	public static final String PLAYER_REFUSED = "2#";//����ʧ���ź�
	public static final String BEGIN_GAME = "3#";//��Ϸ��ʼ�źţ�3#+������+�ƺ�+ ��+�ƺ�....
	public static final String YOUR_TURN = "4#";//������ҳ����ź�
	public static final String PLAY_END = "5#";//����ת���źţ�5#+��Һ�+�ƺ� + , + .... 
												//+�������+���1����+���2����+���3����
	public static final String ROUND_END = "6#";//�غϽ����źţ�������ҵķ�����6#+���1����+���2����+���3����
	public static final String GAME_OVER = "7#";//��Ϸ�����źţ�7#+ʤ����
	public static final String WINNING_PLAYER = "8#";
	
	public static final String PLAY_CARDS = "9#";//�Լ����ƣ�12#+�Ƶ���� + , +.....	
	public static final String GIVE_UP = "0#";//�Լ���������
	
	//handler��Ϣ������
	public static final int UPDATE_WAITING_PLAYER_NUM = 1;
	public static final int START_GAME = 2;
}
