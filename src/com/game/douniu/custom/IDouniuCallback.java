package com.game.douniu.custom;

public interface IDouniuCallback {
	//public void loginCb(int userid, String str);			//0
	//public void logoutCb(int value);						//1
	
	//public void joinRoomCb(String str);						//2
	public void otherJoinRoomCb(String str);				//3
	//public void exitRoomCb(String str);						//4
	public void otherExitRoomCb(String str);				//5
	
	public void prepareCb(String str);						//6
	public void otherUserPrepareCb(String str);				//7
	public void willBankerCb(String str);					//8
	public void tryBankerCb(String str);					//9
	public void willStakeCb(String str);					//10
	public void stakeCb(String str);						//11
	public void otherUserStakeValueCb(String str);			//12
	public void willSubmitCb(String str);					//13
	public void submitCb(String str);						//14
	public void otherUserCardPatternCb(String str);			//15
	public void gameResultCb(String str);					//16
}
