package com.game.douniu.custom;

public interface ILoginJoinCallback {
	//public void loginCb(int userid, String str);			//0
	public void logoutCb(int value);						//1
	
	public void joinRoomCb(String str);						//2
	public void exitRoomCb(String str);						//4
}
