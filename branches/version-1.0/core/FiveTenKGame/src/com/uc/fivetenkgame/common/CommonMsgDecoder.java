package com.uc.fivetenkgame.common;
/**
 * ����ƥ��ͽ���Common��Ϣ
 * @author lm
 *
 */
public class CommonMsgDecoder{

    private static int playerCount = 3;

    /**
     * �ж��źŵı�־�Ƿ�һ��
     * @param sourceMsg ��Ҫ�жϵ���Ϣ��Դ��Ϣ
     * @param commonMsg ��Ϣ��־
     * @return sourceMsg����commonMsg��־������true������false
     */
    public static boolean checkMessage(String sourceMsg, String commonMsg) {
        return sourceMsg.startsWith(commonMsg);
    }

    /**
     * ���������
     * @param sourceMsg Դ��Ϣ
     * @return ������,����������ţ�����-1
     */
    public static int getPlayerNumber(String sourceMsg) {
        if (checkMessage(sourceMsg, NetworkCommon.PLAYER_ACCEPTED)
                || checkMessage(sourceMsg, NetworkCommon.GIVE_UP)
                || checkMessage(sourceMsg, NetworkCommon.YOUR_TURN)) {
            return Integer.parseInt(sourceMsg.substring(
                    sourceMsg.indexOf('#') + 1).trim());
        } else if (checkMessage(sourceMsg, NetworkCommon.BEGIN_GAME)
                || checkMessage(sourceMsg, NetworkCommon.PLAY_END)) {
            return Integer.parseInt(sourceMsg.substring(
                    sourceMsg.indexOf('#') + 1, sourceMsg.indexOf(',')).trim());
        } else if (checkMessage(sourceMsg, NetworkCommon.PLAYER_NAME)
                && !sourceMsg.substring(sourceMsg.indexOf(',') + 1).contains(
                        ",")) {
            return Integer.parseInt(sourceMsg.substring(
                    sourceMsg.indexOf('#') + 1, sourceMsg.indexOf(',')).trim());
        } else if (checkMessage(sourceMsg, NetworkCommon.GAME_OVER)) {
            if (sourceMsg.contains(",")) { // ����Ӯ�Һź͸���ҷ���
                return Integer.parseInt(sourceMsg.substring(
                        sourceMsg.indexOf('#') + 1, sourceMsg.indexOf(','))
                        .trim());
            } else { // �����˳���ҵ����
                return Integer.parseInt(sourceMsg.substring(
                        sourceMsg.indexOf('#') + 1).trim());
            }
        }
        return -1;
    }

    /**
     * ����ƺ�
     * @param sourceMsg Դ��Ϣ
     * @return �ƺţ���ҳ����ƣ���ʼ�Ƶȣ�����������������null
     */
    public static String[] getCards(String sourceMsg) {
        if (checkMessage(sourceMsg, NetworkCommon.BEGIN_GAME)) {
            return sourceMsg.substring(sourceMsg.indexOf(',') + 1).trim()
                    .split(",");
        } else if (checkMessage(sourceMsg, NetworkCommon.PLAY_CARDS)) {
            return sourceMsg.substring(sourceMsg.indexOf('#') + 1).trim()
                    .split(",");
        } else if (checkMessage(sourceMsg, NetworkCommon.PLAY_END)) {
            String msg = sourceMsg.substring(sourceMsg.indexOf('#') + 1).trim();

            String str[] = new String((msg.substring(msg.indexOf(',') + 1)))
                    .split(",");

            String[] outList = new String[str.length - 4];
            for (int i = 0, count = str.length - 4; i < count; i++) {
                outList[i] = str[i];
            }

            return outList;
        }
        return null;
    }

    /**
     * ����������
     * @param sourceMsg Դ��Ϣ
     * @return ������֣�1������������������������null
     */
    public static String[] getPlayerNames(String sourceMsg) {
        if (checkMessage(sourceMsg, NetworkCommon.PLAYER_NAME)) {
            String[] names = sourceMsg.substring(sourceMsg.indexOf('#') + 1)
                    .trim().split(",");
            if (names.length > 2) {// player����
                return names;
            } else if (names.length == 2) {// server����
                String[] singleName = new String[1];
                singleName[0] = names[1];
                return singleName;
            }
        }
        return null;
    }

    /**
     * ��ø���ҷ���
     * @param sourceMsg Դ��Ϣ
     * @return ��ҷ������غϽ�������Ϸ����������������������null
     */
    public static String[] getPlayerScores(String sourceMsg) {
        if (checkMessage(sourceMsg, NetworkCommon.ROUND_END)) {
            return sourceMsg.substring(sourceMsg.indexOf('#') + 1).trim()
                    .split(",");
        } else if (checkMessage(sourceMsg, NetworkCommon.GAME_OVER)) {
            return sourceMsg.substring(sourceMsg.indexOf(',') + 1).trim()
                    .split(",");
        }
        return null;
    }

    /**
     * ���Ӯ�����
     * @param sourceMsg Դ��Ϣ
     * @return ��Ϸ����ʱ��Ӯ����ţ�ֻ��GAME_OVER�ź�֧�֣������ź��򷵻�-1
     */
    public static int getWinnerNumber(String sourceMsg) {
        if (checkMessage(sourceMsg, NetworkCommon.GAME_OVER)) {
            return Integer.parseInt(sourceMsg.substring(
                    sourceMsg.indexOf('#') + 1, sourceMsg.indexOf(',')).trim());
        }
        return -1;
    }

    /**
     * ��õ�ǰ�Ѽ���������
     * @param sourceMsg Դ��Ϣ
     * @return Ŀǰ�Ѽ�����Ϸ���������ֻ��PLAYER_NUMBER_UPDATE�ź�֧�֣������źŷ���-1
     */
    public static int getUpdatePlayerNumber(String sourceMsg) {
        if (checkMessage(sourceMsg, NetworkCommon.PLAYER_NUMBER_UPDATE)) {
            return Integer.parseInt(sourceMsg.substring(
                    sourceMsg.indexOf('#') + 1).trim());
        }
        return -1;
    }

    /**
     * ����������
     * @param sourceMsg Դ��Ϣ
     * @return Ŀǰһ�ֵ����������ֻ��PLAY_END�ź�֧�֣������źŷ���-1
     */
    public static int getTableScore(String sourceMsg) {
        if (checkMessage(sourceMsg, NetworkCommon.PLAY_END)) {
            String str[] = sourceMsg.substring(sourceMsg.indexOf('#') + 1)
                    .trim().split(",");

            return Integer.parseInt(str[str.length - playerCount - 1]);
        }
        return -1;
    }

    /**
     * ��ø����ʣ������
     * @param sourceMsg Դ��Ϣ
     * @return �����ʣ���������ֻ��PLAY_END�ź�֧�֣������źŷ���null
     */
    public static String[] getRemainCardNumbers(String sourceMsg) {
        if (checkMessage(sourceMsg, NetworkCommon.PLAY_END)) {
            String str[] = sourceMsg.substring(sourceMsg.indexOf('#') + 1)
                    .trim().split(",");
            String[] remainCards = new String[playerCount];
            for (int i = 0; i < playerCount; i++) {
                remainCards[i] = str[str.length - playerCount + i];
            }
            return remainCards;
        }
        return null;
    }

}
