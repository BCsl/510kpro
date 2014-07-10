package com.uc.fivetenkgame.view.entity;

import android.graphics.Bitmap;
import android.graphics.Rect;
/**
 * 
 * @author chensl@ucweb.com
 *
 * ����5:09:21 2014-7-9
 */
public class Card {
	int x=0;      							//������
	int y=0;	  							//������
	int width;    							//���
	int height;   							//�߶�
	String cardId;							//ͼƬ 
	String name; 							//Card������
	boolean rear=true;						//�Ƿ��Ǳ���
	boolean clicked=false;					//�Ƿ񱻵��
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
