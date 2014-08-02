package com.uc.fivetenkgame.common;

public interface ICommonMsgDecoder {

    /**
     * �ж��źŵı�־�Ƿ�һ��
     * @param sourceMsg ��Ҫ�жϵ���Ϣ��Դ��Ϣ
     * @param commonMsg ��Ϣ��־
     * @return sourceMsg����commonMsg��־������true������false
     */
    boolean checkMessage(String sourceMsg, String commonMsg);
    
    /**
     * ���������
     * @param sourceMsg Դ��Ϣ
     * @return ������,����������ţ�����-1
     */
    int getPlayerNumber(String sourceMsg);
    
    /**
     * ����ƺ�
     * @param sourceMsg Դ��Ϣ
     * @return �ƺţ���ҳ����ƣ���ʼ�Ƶȣ�����������������null
     */
    String[] getCards(String sourceMsg);
    
    /**
     * ����������
     * @param sourceMsg Դ��Ϣ
     * @return ������֣�1������������������������null
     */
    String[] getPlayerNames(String sourceMsg);
    
    /**
     * ��ø���ҷ���
     * @param sourceMsg Դ��Ϣ
     * @return ��ҷ������غϽ�������Ϸ����������������������null
     */
    String[] getPlayerScores(String sourceMsg);
    
    /**
     * ���Ӯ�����
     * @param sourceMsg Դ��Ϣ
     * @return ��Ϸ����ʱ��Ӯ����ţ�ֻ��GAME_OVER�ź�֧�֣������ź��򷵻�-1
     */
    int getWinnerNumber(String sourceMsg);
    
    /**
     * ��õ�ǰ�Ѽ���������
     * @param sourceMsg Դ��Ϣ
     * @return Ŀǰ�Ѽ�����Ϸ���������ֻ��PLAYER_NUMBER_UPDATE�ź�֧�֣������źŷ���-1
     */
    int getUpdatePlayerNumber(String sourceMsg);
    
    /**
     * ����������
     * @param sourceMsg Դ��Ϣ
     * @return Ŀǰһ�ֵ����������ֻ��PLAY_END�ź�֧�֣������źŷ���-1
     */
    int getTableScore(String sourceMsg);
    
    /**
     * ��ø����ʣ������
     * @param sourceMsg Դ��Ϣ
     * @return �����ʣ���������ֻ��PLAY_END�ź�֧�֣������źŷ���null
     */
    String[] getRemainCardNumbers(String sourceMsg);
}
