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
 * 下午4:11:45 2014-7-31
 */
public interface IButton {
	/**
	 * 画Button原型  （活动在非UI线程）
	 */
	public void doDraw();
	/**
	 * 点击状态	（活动在主线程）
	 */
	public void onClick();
	/**
	 * 是否被点击
	 * @param x 
	 * @param y
	 * @return
	 */
	public boolean isClicked(float x,float y);
	}
