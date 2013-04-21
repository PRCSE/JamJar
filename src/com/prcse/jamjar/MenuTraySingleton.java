package com.prcse.jamjar;

import android.app.Activity;
import android.content.Context;

import com.slidingmenu.lib.SlidingMenu;

public class MenuTraySingleton {
	
	private static SlidingMenu menu_tray;
	private static MenuTraySingleton menuSingleton;
	
	private MenuTraySingleton() {
	}
		
	public static synchronized MenuTraySingleton getInstance() {
		if(menuSingleton == null) {
			menuSingleton = new MenuTraySingleton();
		}
		
		return menuSingleton;
	}
	
	public void menuTraySetUp(Context context, int width) {
//		if(menu_tray == null) {
//			menu_tray = new SlidingMenu(context);
//		}
		
		menu_tray = new SlidingMenu(context);
		
		menu_tray.setMode(SlidingMenu.LEFT);
		menu_tray.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu_tray.setBehindOffset(width / 2);
		menu_tray.setFadeDegree(0.35f);
		menu_tray.setMenu(R.layout.menu_tray);
		menu_tray.attachToActivity((Activity) context, SlidingMenu.SLIDING_CONTENT);
		
	}

	public SlidingMenu getMenu_tray() {
		return menu_tray;
	}	
}
