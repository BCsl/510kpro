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
	int x=0;      							//横坐标
	int y=0;	  							//纵坐标
	int width;    							//宽度
	int height;   							//高度
	String cardId;							//图片 
	String name; 							//Card的名称
	boolean rear=true;						//是否是背面
	boolean clicked=false;					//是否被点击
	public Card(int width,int height,String cardId){
		this.width=width;
		this.height=height;
		this.cardId=cardId;
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
	public void setName(String name){
		this.name=name;
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
