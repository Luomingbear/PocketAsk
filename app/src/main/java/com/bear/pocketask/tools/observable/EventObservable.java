package com.bear.pocketask.tools.observable;

import java.util.ArrayList;
import java.util.List;

/**
 * 观察者模式
 * Created by ming.luo on 10/28/2016.
 */

public class EventObservable {
    private List<EventObserver> observers = new ArrayList<EventObserver>();

    private boolean changed = false;

    private volatile static EventObservable instance;

    public static EventObservable getInstance() {
        if (instance == null) {
            synchronized (EventObservable.class) {
                if (instance == null)
                    instance = new EventObservable();
            }
        }
        return instance;
    }

    protected EventObservable() {
    }

    public void addObserver(EventObserver observer) {
        if (observer == null) {
            throw new NullPointerException();
        }
        synchronized (this) {
            if (!observers.contains(observer))
                observers.add(observer);
        }
    }

    protected void clearChanged() {
        changed = false;
    }

    public int countObservers() {
        return observers.size();
    }

    public synchronized void deleteObserver(EventObserver observer) {
        observers.remove(observer);
    }

    public synchronized void deleteObservers() {
        observers.clear();
    }

    public boolean hasChanged() {
        return changed;
    }

    public void notifyObservers() {
        notifyObservers(-1, null);
    }

    @SuppressWarnings("unchecked")
    public void notifyObservers(int id, Object data) {
        int size = 0;
        EventObserver[] arrays = null;
        synchronized (this) {
            if (!hasChanged()) {
                clearChanged();
                size = observers.size();
                arrays = new EventObserver[size];
                observers.toArray(arrays);
            }
        }
        if (arrays != null) {
            for (EventObserver observer : arrays) {
                observer.onNotify(this, id, data);
            }
        }
    }

    protected void setChanged() {
        changed = true;
    }
}
