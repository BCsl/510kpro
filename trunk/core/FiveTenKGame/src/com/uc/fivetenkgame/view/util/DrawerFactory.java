package com.uc.fivetenkgame.view.util;

import com.uc.fivetenkgame.view.GameView.CardSizeHolder;
import com.uc.fivetenkgame.view.GameView.ScreenSizeHolder;

/**
 * 
 * @author chensl@ucweb.com
 *
 * 下午5:09:36 2014-7-9
 */
public class DrawerFactory {
	/**
	 * 根据游戏人数，返回不同的drawer来画不同的界面
	 * @param num 		游戏人数
	 * @return			
	 */
	public static IDrawer getDrawer(int num,ScreenSizeHolder screenHolder,CardSizeHolder cardSizeHolder){
		IDrawer drawer = null;
			switch(num)
				{
			case 3:drawer = new ThreePersonGameDrawer(screenHolder,cardSizeHolder);break;
			default : new IllegalArgumentException(num +"is undefinded!") ;	
				}
			return drawer;
	}
}
