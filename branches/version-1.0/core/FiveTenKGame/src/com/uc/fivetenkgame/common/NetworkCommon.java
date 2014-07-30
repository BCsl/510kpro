package com.uc.fivetenkgame.common;

/**
 * �����࣬һЩ�������ݶ���
 * 
 * @author liuzd
 * 
 */
public class NetworkCommon {

	// ������
	public static final int SERVER_NUM = 1;
	public static final int PLAYER1_NUM = 2;
	public static final int PLAYER2_NUM = 3;

	// ������Ϣͷ��Ŷ���
	//���ź�
	public static final String PLAYER_REFUSED = "2#";// ����ʧ���ź�:
	public static final String GAME_PAUSE = "10#";// ĳ�������ͣ��Ϸ�����������ͣ
	public static final String GAME_RESUME = "11#";// ������һָ���Ϸ
	public static final String GAME_EXIT = "12#";// ĳ������˳���Ϸ����������˳�
	public static final String PLAY_AGAIN = "14#"; // ������Ϣ
	//ֻ����������
	public static final String GIVE_UP = "0#";// �Լ���������0#+��Һ�
	public static final String PLAYER_ACCEPTED = "1#";// ���ӳɹ��źţ�1#+��ǰ������
	public static final String YOUR_TURN = "4#";// ������ҳ����źţ�4#+������
	//���������ź��ƺ�
	public static final String BEGIN_GAME = "3#";// ��Ϸ��ʼ�źţ�3#+������+�ƺ�+ ��+�ƺ�....
	public static final String PLAY_CARDS = "9#";// �Լ����ƣ�9#+��Һ�+�ƺ� + , +.....
	//������Һź��������
	public static final String PLAYER_NAME = "13#";// 1.server���գ�13#+������+������� 2.player���գ�13#���1����+���2����+���3����
	//��������ҷ���
	public static final String ROUND_END = "6#";// �غϽ����źţ�������ҵķ�����6#+���1����+���2����+���3����
	//����Ӯ�Һţ�����ҷ���
	public static final String GAME_OVER = "7#";// ��Ϸ�����źţ�7#+ʤ����+���1����+���2����+���3����
	//�����ȴ��������
	public static final String PLAYER_NUMBER_UPDATE = "8#";// ���µȴ����������8#+�ȴ��������
	//������Һţ��ƺţ�������������ʣ������
    public static final String PLAY_END = "5#";// ����ת���źţ�5#+��Һ�+�ƺ� + , + ....+�������+���1����+���2����+���3��������4λ��

    
	// handler��Ϣ������
	public static final int UPDATE_WAITING_PLAYER_NUM = 1;
	public static final int START_GAME = 2;
	public static final int END_GAME = 3;
	public static final int HOST_FULL = 4;
	public static final int TIME_OUT = 5;
	public static final int GAME_STATE_CHANGE = 6;// ��GAME_PAUSE,GAME_RESUME��GAME_EXITƥ��
	public static final int PLAYER_LEFT = 7;

	//player״̬�ı��ڲ���Ϣ
	public static final String PLAYER_STATE_CHANGE = "0@";
	// ������״̬�ı��ڲ���Ϣ
	public static final String SERVER_LISTENING = "1@";
	public static final String GAME_START = "2@";
	public static final String GAME_END = "3@";
	// ���������
	public static final int TOTAL_PLAYER_NUM = 3;

	// ��Ϣ����β
	public static final String MESSAGE_END = "#@";

}
