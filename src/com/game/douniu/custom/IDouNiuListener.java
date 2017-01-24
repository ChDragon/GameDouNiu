package com.game.douniu.custom;

public interface IDouNiuListener {
	public abstract boolean OnEventInitializeEnd();
	public abstract boolean OnEventTryingBankerEnd();
	public abstract boolean OnEventFaPaiEnd();
	public abstract boolean OnEventCalculatedEnd();
	public abstract boolean OnEventcheckoutEnd();
}
