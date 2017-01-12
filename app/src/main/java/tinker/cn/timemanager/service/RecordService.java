package tinker.cn.timemanager.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import java.util.HashMap;

import tinker.cn.timemanager.R;
import tinker.cn.timemanager.model.ActivityInfo;
import tinker.cn.timemanager.model.NotificationInfo;
import tinker.cn.timemanager.model.RecordInfo;
import tinker.cn.timemanager.utils.BaseConstant;
import tinker.cn.timemanager.utils.FormatTime;
import tinker.cn.timemanager.utils.GenerateNotificationID;

/**
 * Created by tiankui on 1/10/17.
 */

public class RecordService extends Service {

    private Handler mRecordHandler;
    private HashMap<String, ActivityInfo> mActivityInfoMap;

    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread handlerThread = new HandlerThread("RecordHandlerThread");
        handlerThread.start();
        mRecordHandler = new Handler(handlerThread.getLooper());
        mActivityInfoMap = new HashMap<>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new RecordServiceBinder();
    }

    public class RecordServiceBinder extends Binder {

        //TODO：这里有点问题，如果是连续点击的话就会有问题；
        public void startRecorder(ActivityInfo info) {
            ActivityInfo activityInfo = cloneActivityInfo(info);
            mActivityInfoMap.put(info.getId(), activityInfo);
            if (activityInfo != null) {
                showNotification(activityInfo);
            }
        }

        public void pauseRecorder(ActivityInfo info) {
            ActivityInfo activityInfo = mActivityInfoMap.get(info.getId());
            if (activityInfo != null) {
                mRecordHandler.removeCallbacks(activityInfo.getRecordInfo().getRunnable());
                NotificationInfo notificationInfo = activityInfo.getNotificationInfo();
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancel(notificationInfo.getId());
                stopForeground(true);
            }
        }

        public void resumeRecorder(ActivityInfo info) {
            ActivityInfo activityInfo = mActivityInfoMap.get(info.getId());
            if (activityInfo != null) {
                if(info.getRecordInfo().getRecordState()==BaseConstant.STOP_STATE){
                    activityInfo.getRecordInfo().setTotalTime(0);
                }
                mRecordHandler.postDelayed(activityInfo.getRecordInfo().getRunnable(), 1000);
                NotificationInfo notificationInfo = activityInfo.getNotificationInfo();
                if (notificationInfo != null) {
                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(notificationInfo.getId(), notificationInfo.getNotification());
                }
            }
        }

        public void stopRecorder(ActivityInfo info) {
            ActivityInfo activityInfo = mActivityInfoMap.get(info.getId());
            if (activityInfo != null) {
                mRecordHandler.removeCallbacks(activityInfo.getRecordInfo().getRunnable());
                NotificationInfo notificationInfo = activityInfo.getNotificationInfo();
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancel(notificationInfo.getId());
                stopForeground(true);
            }
        }


        private ActivityInfo cloneActivityInfo(ActivityInfo info) {
            ActivityInfo activityInfo = new ActivityInfo();
            RecordInfo recordInfo = new RecordInfo();
            activityInfo.setFragmentTag(info.getFragmentTag());
            activityInfo.setCreateTime(info.getCreateTime());
            activityInfo.setNotificationInfo(info.getNotificationInfo());
            activityInfo.setId(info.getId());
            activityInfo.setName(info.getName());
            activityInfo.setParentGroupId(info.getParentGroupId());
            activityInfo.setType(info.getType());

            recordInfo.setRecordState(info.getRecordInfo().getRecordState());
            recordInfo.setBeginTime(info.getRecordInfo().getBeginTime());
            recordInfo.setEndTime(info.getRecordInfo().getEndTime());
            recordInfo.setDuration(info.getRecordInfo().getDuration());
            recordInfo.setTotalTime(info.getRecordInfo().getTotalTime());

            activityInfo.setRecordInfo(recordInfo);

            return activityInfo;
        }
    }

    private void showNotification(ActivityInfo info) {
        final RecordInfo recordInfo = info.getRecordInfo();
        final Notification notification = new Notification(R.mipmap.ic_launcher, null, 0);
        final RemoteViews notificationView = new RemoteViews(getPackageName(), R.layout.notification_content_view);
        notification.contentView = notificationView;

        Intent pauseIntent = new Intent();
        pauseIntent.putExtra("activityInfo", info);
        pauseIntent.setAction(BaseConstant.NOTIFICATION_CLICK_PAUSE);
        PendingIntent pendingPauseIntent = PendingIntent.getBroadcast(this, 0, pauseIntent, 0);
        notificationView.setOnClickPendingIntent(R.id.notification_iv_pause, pendingPauseIntent);

        Intent stopIntent = new Intent();
        stopIntent.putExtra("activityInfo", info);
        stopIntent.setAction(BaseConstant.NOTIFICATION_CLICK_STOP);
        PendingIntent pendingStopIntent = PendingIntent.getBroadcast(this, 0, stopIntent, 0);
        notificationView.setOnClickPendingIntent(R.id.notification_iv_stop, pendingStopIntent);

        final NotificationInfo notificationInfo = new NotificationInfo();
        notificationInfo.setRemoteViews(notificationView);
        notificationInfo.setId(GenerateNotificationID.getID());
        notificationInfo.setNotification(notification);
        info.setNotificationInfo(notificationInfo);
        final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationInfo.getId(), notification);

        startForeground(notificationInfo.getId(), notification);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                recordInfo.setTotalTime(recordInfo.getTotalTime() + 1000);
                notificationView.setTextViewText(R.id.notification_tv_record_time, FormatTime.calculateTimeString(recordInfo.getTotalTime()));
                mRecordHandler.postDelayed(this, 1000);
                recordInfo.setRunnable(this);
                notificationManager.notify(notificationInfo.getId(), notification);
            }
        };
        recordInfo.setRunnable(runnable);
        mRecordHandler.postDelayed(runnable, 1000);
    }

}
