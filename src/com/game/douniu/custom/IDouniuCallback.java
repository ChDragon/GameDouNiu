package com.game.douniu.custom;

public interface IDouniuCallback {
	public abstract void loginCb(byte[] data, int datalen);
	public abstract void logoutCb(int value);
}
