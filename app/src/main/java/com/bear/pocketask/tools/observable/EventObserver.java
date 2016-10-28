package com.bear.pocketask.tools.observable;

/**
 * 观察者
 * Created by ming.luo on 10/28/2016.
 */

public interface EventObserver
{
	void onNotify(Object sender, int eventId, Object... args);
}
