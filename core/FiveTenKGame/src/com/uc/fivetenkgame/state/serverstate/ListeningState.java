package com.uc.fivetenkgame.state.serverstate;

import android.util.Log;

import com.uc.fivetenkgame.common.CommonMsgDecoder;
import com.uc.fivetenkgame.common.NetworkCommon;
import com.uc.fivetenkgame.server.ServerContext;

/**
 * ����������״̬
 * 
 * @author liuzd
 *
 */
public class ListeningState extends ServerState {

	// ������ҵ�����
	public String[] playerNames;

	public ListeningState(ServerContext context) {
		mServerContext = context;
		playerNames = new String[NetworkCommon.TOTAL_PLAYER_NUM];
	}

	@Override
	public void handle(String msg) {
		
		if (CommonMsgDecoder.checkMessage(msg, NetworkCommon.GIVE_UP)) {//������˳�
			Log.i("send game over in listeningState", msg);
			int playerNumber = CommonMsgDecoder.getPlayerNumber(msg);
			mServerContext.getNetworkManager().sendMessage(
					NetworkCommon.GAME_OVER + playerNumber);
			mServerContext.resetServer();
		} else if (CommonMsgDecoder.checkMessage(msg, NetworkCommon.PLAYER_NAME)) {// ������ӳɹ�
			// �����������
		    int clientNum = mServerContext.getClientNum();
		    
            ++clientNum;
            mServerContext.setClientNum(clientNum);

            if (clientNum <= NetworkCommon.TOTAL_PLAYER_NUM) {
                mServerContext.getNetworkManager().sendMessage(
                        NetworkCommon.PLAYER_NUMBER_UPDATE + clientNum);
            }
            
			int playerNumber = CommonMsgDecoder.getPlayerNumber(msg);
			String playerName = CommonMsgDecoder.getPlayerNames(msg)[0];
			playerNames[playerNumber - 1] = playerName;
			Log.i("add player name", "player name is: "
					+ playerName);
			Log.i("��Ϸ����",mServerContext.getClientNum()+"");
			// �ﵽ��������������ʼ��Ϸ
			if (mServerContext.getClientNum() == NetworkCommon.TOTAL_PLAYER_NUM) {
				StringBuilder sb = new StringBuilder();
				
				sb.append(NetworkCommon.PLAYER_NAME);
				for (String name : playerNames) {
					sb.append(name + ',');
				}
				mServerContext.getNetworkManager().sendMessage(
						sb.deleteCharAt(sb.length() - 1).toString());

				mServerContext.setState(new GameStartState(mServerContext));
				mServerContext.handleMessage(null);
			}
		}
	}

}
