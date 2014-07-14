package com.uc.fivetenkgame.view.entity;

import android.graphics.Bitmap;
import android.graphics.Rect;
/**
 * 
 * @author chensl@ucweb.com
 *
 * 下午5:09:21 2014-7-9
 */
public class Card {
	private int x=0;      							//横坐标
	private int y=0;	  							//纵坐标
	private int width;    							//宽度
	private int height;   							//高度
	private 	String cardId;							//图片 
	private boolean clicked=false;					//是否被点击
	public Card(int width,int height,String cardId){
		this.width=width;
		this.height=height;
		this.cardId=cardId;
	}
	public boolean isClicked(){
		return clicked;
	}
	public void setClick(boolean flag){
			clicked=flag;
	}
	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public void setLocation(int x,int y){
		this.x=x;
		this.y=y;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public Rect getSRC(){
		return new Rect(0,0,width,height);
	}
	public Rect getDST(){
		return new Rect(x, y,x+width, y+height);
	}
	@Override
	public String toString() {
		return "cardId="+cardId+",width="+width+",height="+height+";x="+x+";y="+y;
	}
}
