package com.bear.pocketask.model.record;

import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * 录音管理者 单例
 * Created by bear on 16/11/9.
 */

public class RecordManager {
    private static final String TAG = "RecordManager";
    private volatile static RecordManager instance; //单例
    private ExtAudioRecorder mAudioRecorder; //录音器
    private MediaPlayer mMediaPlayer; //播放器
    private String fileDir; //音频保存的路径

    public static RecordManager getInstance() {
        if (instance == null) {
            synchronized (RecordManager.class) {
                if (instance == null)
                    instance = new RecordManager();
            }
        }
        return instance;
    }

    protected RecordManager() {
        fileDir = Environment.getExternalStorageDirectory() + "/PocketAsk/Audio";
        File file = new File(fileDir);
        if (!file.exists())
            file.mkdirs();
    }

    public void startRecord(String name) {
        mAudioRecorder = ExtAudioRecorder.getInstanse(false);
        mAudioRecorder.setOnAudioRecordListener(new ExtAudioRecorder.OnAudioRecordListener() {
            @Override
            public void onDuration(int duration) {
                Log.i(TAG, "onDuration: duration+++" + duration);
                if (onRecordListener != null && duration > 500)
                    onRecordListener.onDurationLong();
            }
        });
        String filePath = fileDir + "/" + name + ".wav";

        mAudioRecorder.recordChat(fileDir + "/", name + ".wav");

        //设置文件保存的地址

    }

    public void startRecordTemp() {
        startRecord("audio_temp");
    }

    public void stopRecord() {
        mAudioRecorder.stopRecord();
//        mAudioRecorder.
        mAudioRecorder = null;
    }

    public void startPlay(String filePath) {

        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(filePath);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlay();
                    if (onRecordListener != null)
                        onRecordListener.onCompletion(mp);
                }
            });
        } catch (IOException e) {
            Log.e(TAG, "播放失败");
        }
    }

    public void startPlayTemp() {
        String path = fileDir + "/audio_temp.wav";
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            stopPlay();
        }
        startPlay(path);
    }

    public void stopPlay() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private OnRecordListener onRecordListener;

    public void setOnRecordListener(OnRecordListener onRecordListener) {
        this.onRecordListener = onRecordListener;
    }

    public interface OnRecordListener {
        //播放完毕
        void onCompletion(MediaPlayer mp);

        //录音时间达到最小时间限制
        void onDurationLong();
    }
}
