package com.game.douniu.utils;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.game.douniu.custom.Constant;

import android.os.Handler;
import android.util.Log;

/**
*
* ��������Դ����ϵ q344717871
* 
*/

public class UserClock {

	/** ���ʱ�� **/
	protected MyTimerTask	myTask;
	/** ���ʱ�� **/
	protected Timer			timer;
	/** ����λ�� **/
	protected static int	m_nChairID	= Constant.INVALID_CHAIR;
	/** ����λ�� **/
	protected static int	m_nViewID	= Constant.INVALID_CHAIR;
	/** ʱ��ID **/
	protected static int	m_nClockID;
	/** ���ʱ�� **/
	protected static int	m_nTime;

	Handler					m_TimeHandler;

	public static ArrayList<ClockItem>	lst	= new ArrayList<ClockItem>();
	
	
	public UserClock(Handler timehandler) {
		m_TimeHandler = timehandler;
	}
	
	public void RemoveClock(MyTimerTask	myTask ,Timer	 	timer )
	{
		for(int i=0;i<lst.size();i++)
		{
			ClockItem item=lst.get(i);
			if (item.myTask != myTask && item.timer == timer)
			{
				item.myTask=null;
				item.timer=null;
				lst.remove(i);
			} 
		}
	}
	
	public static void KillAllClock()
	{
		for(int i=0;i<lst.size();i++)
		{
			ClockItem item=lst.get(i);
			if (item.myTask != null)
				item.myTask.cancel();
			if (item.timer != null)
				item.timer.purge();
 
			item.timer = null;
			item.myTask = null;
		}
	}
	
	/**
	 * ��ʱ��
	 */
	protected class MyTimerTask extends TimerTask {

		@Override
		public void run() {
			if (m_nClockID != 0 && m_nChairID != Constant.INVALID_CHAIR && m_nViewID != Constant.INVALID_CHAIR) {
				if (m_nTime >= 0) {
					if (m_TimeHandler != null) {
						m_TimeHandler.obtainMessage(m_nClockID, m_nChairID, m_nTime).sendToTarget();
					}
					m_nTime--;
					//Log.d("[wzj]userClock", "m_nTime:"+m_nTime);
					if (m_nTime < 0) {
						killGameClock(m_nClockID);
						return;
					}
				}
			}
		}
	}
	  

	public void setGameClock(int chairid, int clockid, int time) {

		m_nChairID = chairid;
		m_nTime = time;
		m_nClockID = clockid;
		m_nViewID = 0;//GameEngine.getInstance().SwitchViewChairID(chairid);

		if (timer == null || myTask == null) {
			timer = new Timer();
			myTask = new MyTimerTask();
			timer.schedule(myTask, 0, 1000);
			ClockItem o=new ClockItem();
			o.timer=timer;
			o.myTask=myTask;
			lst.add(o);
		}
	}

	public void killGameClock(int clockid) {
		if (clockid == m_nClockID || clockid == 0) {
			
			RemoveClock(myTask,timer);
			
			if (myTask != null)
				myTask.cancel();
			if (timer != null)
				timer.purge();

			m_nClockID = 0;
			m_nTime = 0;
			m_nChairID = Constant.INVALID_CHAIR;
			m_nViewID = Constant.INVALID_CHAIR;

			timer = null;
			myTask = null;
		}
	}

	public static int getGameClock(int chairid) {
		if (chairid == m_nChairID)
			return m_nTime;
		return 0;
	}

	public static int getUserClock(int viewid) {
		if (viewid == m_nViewID)
			return m_nTime;
		return 0;
	}

}
