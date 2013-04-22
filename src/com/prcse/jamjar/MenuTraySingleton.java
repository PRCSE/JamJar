package com.prcse.jamjar;

import android.app.Activity;
import android.content.Context;

import com.slidingmenu.lib.SlidingMenu;

//Singleton class to keep track of sliding menu setup.
//Cleaner and more efficient way of handling having a sliding menu on each activity.
public class MenuTraySingleton {
	
	//Initialise static variables. 
	private static SlidingMenu menu_tray;
	private static MenuTraySingleton menuSingleton;
	
	//Singleton constructor. Checks if it has already been initialised. If it hasn't it initialises it.
	//Otherwise it returns the existing one
	private MenuTraySingleton() {
	}
		
	public static synchronized MenuTraySingleton getInstance() {
		if(menuSingleton == null) {
			menuSingleton = new MenuTraySingleton();
		}
		
		return menuSingleton;
	}
	
	//Method for setting up sliding menu. Width is passed from an activity to programmatically set menu to open exactly halfway.
	public void menuTraySetUp(Context context, int width) {
		
		menu_tray = new SlidingMenu(context);
		
		menu_tray.setMode(SlidingMenu.LEFT);
		menu_tray.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu_tray.setBehindOffset(width / 2);
		menu_tray.setFadeDegree(0.35f);
		menu_tray.setMenu(R.layout.menu_tray);
		menu_tray.attachToActivity((Activity) context, SlidingMenu.SLIDING_CONTENT);
		
	}

	//Getter for current instance of sliding menu object. Pretty standard.
	public SlidingMenu getMenu_tray() {
		return menu_tray;
	}	
}
