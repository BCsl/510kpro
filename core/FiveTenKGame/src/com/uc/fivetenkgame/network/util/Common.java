package com.uc.fivetenkgame.network.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.uc.fivetenkgame.view.entity.Card;

/**
 * �����࣬һЩ�������ݶ���
 * 
 * @author liuzd
 *
 */
public class Common {

	// ������
	public static final int SERVER_NUM = 1;
	public static final int PLAYER1_NUM = 2;
	public static final int PLAYER2_NUM = 3;

	//������Ϣͷ��Ŷ���
	public static final String PLAYER_ACCEPTED = "1#";//���ӳɹ��źţ�1#+��ǰ������
	public static final String PLAYER_REFUSED = "2#";//����ʧ���ź�:2#
	
	public static final String BEGIN_GAME = "3#";//��Ϸ��ʼ�źţ�3#+������+�ƺ�+ ��+�ƺ�....
	public static final String YOUR_TURN = "4#";//������ҳ����źţ�4#+������
	public static final String PLAY_END = "5#";//����ת���źţ�5#+��Һ�+�ƺ� + , + .... //+�������+���1����+���2����+���3��������4λ��
	public static final String ROUND_END = "6#";//�غϽ����źţ�������ҵķ�����6#+���1����+���2����+���3����
	public static final String GAME_OVER = "7#";//��Ϸ�����źţ�7#+ʤ����+����ҷ���
	public static final String PLAYER_NUMBER_UPDATE = "8#";//���µȴ����������8#+�ȴ��������
	
	public static final String PLAY_CARDS = "9#";//�Լ����ƣ�9#+��Һ�+�Ƶ���� + , +.....	
	public static final String GIVE_UP = "0#";//�Լ���������0#+��Һ�
	
	public static final String GAME_PAUSE = "10#";//ĳ�������ͣ��Ϸ�����������ͣ
	public static final String GAME_RESUME = "11#";//������һָ���Ϸ
	public static final String GAME_EXIT = "12#";//ĳ������˳���Ϸ����������˳�

	public static final String PLAYER_NAME = "13#";//1.server���գ�13#+������+�������    2.player���գ�13#���1����+���2����+���3����
	public static final String PLAY_AGAIN = "14#"; //������Ϣ
		
	// handler��Ϣ������
	public static final int UPDATE_WAITING_PLAYER_NUM = 1;
	public static final int START_GAME = 2;
	public static final int END_GAME = 3;
	public static final int HOST_FULL = 4;
	public static final int TIME_OUT = 5;
	public static final int GAME_STATE_CHANGE = 6;//��GAME_PAUSE,GAME_RESUME��GAME_EXITƥ��
	public static final int PLAYER_LEFT = 7;
	
	// ������״̬�ı��ڲ���Ϣ
	public static final String SERVER_LISTENING = "1@";
	public static final String GAME_START = "2@";
	public static final String GAME_END = "3@";
	// ���������
	public static final int TOTAL_PLAYER_NUM = 3;
	
	//��Ϣ����β
	public static final String MESSAGE_END = "#@";
	
	//sharepreference���
	public static final String TABLE_PLAYERS="players";
	public static final String TABLE_SETTING="setting";
	public static final String SP_QRCODE_FLAG="qrcode";
	public static final String SP_MUSIC_FLAG="music";
	
	public static final int SOUND_BUTTON_PRESS=0;
	public static final int SOUND_GAME_START=1;
	public static final int SOUND_WIN = 2;
	public static final int SOUND_FAILD =3;
	public static final int SOUND_SECOND_CALL =4;
//	public static final int SOUND_SECOND_CALL_1 =5;
//	public static final int SOUND_SECOND_CALL_2 =6;
	public static final int SOUND_OUTPUT_CARDS =7;
	public static final int SOUND_PASS =8;
	public static final int SOUND_BEEP =9;
	
	public static void setOrder(List<Card> list){
		Collections.sort(list, new Comparator<Card>() {
			@Override
			public int compare(Card o1, Card o2) {
				int a1 = (Integer.valueOf(o1.getCardId()) - 1 ) / 13; // ��ɫ
				int a2 = (Integer.valueOf(o2.getCardId()) - 1 ) / 13;
				
				int b1 = (Integer.valueOf(o1.getCardId()) - 1 ) % 13;// ��ֵ
				int b2 = (Integer.valueOf(o2.getCardId()) - 1 ) % 13;

				//��joker�����
				if( a1 == 4 || a2 == 4 ){
					if( a2 == a1 )
						return b2 - b1;
					return a2 - a1;
				}
				
				//2,1 ���
				if( b2 < 2 )
					b2 += 13;
				if( b1 < 2 )
					b1 += 13;
				
				if (b2 - b1 == 0)
					return a2 - a1;
				else {
					return b2 - b1;
				}
			}
		});
	}
	/**
	 * ���IP�Ƿ���Ч
	 * 
	 * @param ipaddr �����IP�ַ���
	 * @return   �Ƿ���Ч
	 */
	public static boolean isIPAddress(String ipaddr) {
		boolean flag = false;
		Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])"
				+ "\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])"
				+ "\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])"
				+ "\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
		Matcher m = pattern.matcher(ipaddr);
		flag = m.matches();
		return flag;
		}
}
