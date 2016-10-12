package com.bear.pocketask.view.record;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

/**
 * 录音按钮的观察者
 * Created by luoming on 10/12/2016.
 */
public class RecordObservable
{
	List<RecordObserver> observers = new ArrayList<RecordObserver>();

	boolean changed = false;

	private volatile static RecordObservable instance;

	public static RecordObservable getInstance()
	{
		if (instance == null)
			synchronized (RecordObservable.class)
			{
				if (instance == null)
					instance = new RecordObservable();
			}

		return instance;
	}

	protected RecordObservable()
	{
	}

	public void addObserver(RecordObserver observer)
	{
		if (observer == null)
		{
			throw new NullPointerException();
		}
		synchronized (this)
		{
			if (!observers.contains(observer))
				observers.add(observer);
		}
	}

	protected void clearChanged()
	{
		changed = false;
	}

	public int countObservers()
	{
		return observers.size();
	}

	public synchronized void deleteObserver(Observer observer)
	{
		observers.remove(observer);
	}

	public synchronized void deleteObservers()
	{
		observers.clear();
	}

	public boolean hasChanged()
	{
		return changed;
	}

	public void notifyObservers()
	{
		notifyObservers(0, false);
	}

	@SuppressWarnings("unchecked")
	public void notifyObservers(int id, boolean isPlay)
	{
		int size = 0;
		RecordObserver[] arrays = null;
		synchronized (this)
		{
			if (!hasChanged())
			{
				clearChanged();
				size = observers.size();
				arrays = new RecordObserver[size];
				observers.toArray(arrays);
			}
		}
		if (arrays != null)
		{
			for (RecordObserver observer : arrays)
			{
				observer.onUpdate(id, isPlay);
			}
		}
	}

	protected void setChanged()
	{
		changed = true;
	}
}
