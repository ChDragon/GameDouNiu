package com.game.douniu.custom;

public interface IDouniuCallback {
	public void loginCb(int userid, String str);			//0
	public void otherLoginCb(String str);					//1
	public void logoutCb(int value);						//2
	public void otherLogoutCb(int value);					//3
	public void prepareCb(String str);						//4
	public void otherUserPrepareCb(String str);				//5
	public void willBankerCb(String str);					//6
	public void tryBankerCb(String str);					//7
	public void willStakeCb(String str);					//8
	public void stakeCb(String str);						//9
	public void otherUserStakeValueCb(String str);			//10
	public void willStartCb(String str);					//11 willSubmitCb
	public void playCb(String str);							//12 submitCb
	public void otherUserCardPatternCb(String str);			//13
	public void gameResultCb(String str);					//14
}
