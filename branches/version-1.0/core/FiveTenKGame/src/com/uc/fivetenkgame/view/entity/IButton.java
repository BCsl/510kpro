/**<P>Title:ucweb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2010</p>
 *<p>Company: ucweb.com</p>
 *@author chensl@ucweb.com
 *@version 
 */
package com.uc.fivetenkgame.view.entity;

/**
 * @author chensl@ucweb.com
 *
 * ����4:11:45 2014-7-31
 */
public interface IButton {
	/**
	 * ��Buttonԭ��  ����ڷ�UI�̣߳�
	 */
	public void doDraw();
	/**
	 * ���״̬	��������̣߳�
	 */
	public void onClick();
	/**
	 * �Ƿ񱻵��
	 * @param x 
	 * @param y
	 * @return
	 */
	public boolean isClicked(float x,float y);
	}
