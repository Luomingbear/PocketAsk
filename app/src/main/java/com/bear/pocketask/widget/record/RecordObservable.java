package com.bear.pocketask.widget.record;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

/**
 * 录音按钮的观察者
 * Created by luoming on 10/12/2016.
 */
public class RecordObservable {
    private List<RecordObserver> observers = new ArrayList<RecordObserver>();

    private boolean changed = false;

    private volatile static RecordObservable instance;

    public static RecordObservable getInstance() {
        if (instance == null)
            synchronized (RecordObservable.class) {
                if (instance == null)
                    instance = new RecordObservable();
            }

        return instance;
    }

    private RecordObservable() {
    }

    public void addObserver(RecordObserver observer) {
        if (observer == null) {
            throw new NullPointerException();
        }
        synchronized (this) {
            if (!observers.contains(observer))
                observers.add(observer);
        }
    }

    private void clearChanged() {
        changed = false;
    }

    public int countObservers() {
        return observers.size();
    }

    public synchronized void deleteObserver(Observer observer) {
        observers.remove(observer);
    }

    public synchronized void deleteObservers() {
        observers.clear();
    }

    private boolean hasChanged() {
        return changed;
    }

    public void notifyObservers() {
        notifyObservers(0, false);
    }

    @SuppressWarnings("unchecked")
    public void notifyObservers(int id, boolean isPlay) {
        int size;
        RecordObserver[] arrays = null;
        synchronized (this) {
            if (!hasChanged()) {
                clearChanged();
                size = observers.size();
                arrays = new RecordObserver[size];
                observers.toArray(arrays);
            }
        }
        if (arrays != null) {
            for (RecordObserver observer : arrays) {
                observer.onUpdate(id, isPlay);
            }
        }
    }

    protected void setChanged() {
        changed = true;
    }

    /**
     * 语音播放的按钮的观察者
     * Created by luoming on 10/12/2016.
     */
    public interface RecordObserver {
        void onUpdate(int id, boolean isPlay);
    }
}
