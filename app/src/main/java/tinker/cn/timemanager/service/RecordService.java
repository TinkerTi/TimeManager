package tinker.cn.timemanager.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

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

    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread handlerThread = new HandlerThread("RecordHandlerThread");
        handlerThread.start();
        mRecordHandler = new Handler(handlerThread.getLooper());
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

        public void startRecorder(ActivityInfo info) {
            if (info != null) {
                showNotification(info);
            }
        }

        public void pauseRecorder(ActivityInfo info) {
            if (info != null) {
                mRecordHandler.removeCallbacks(info.getRecordInfo().getServiceRunnable());
                NotificationInfo notificationInfo = info.getNotificationInfo();
                setClickIntent(info);
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancel(notificationInfo.getId());
                stopForeground(true);
            }
        }

        public void resumeRecorder(ActivityInfo info) {
            if (info != null) {
                if (info.getRecordInfo().getRecordState() == BaseConstant.STOP_STATE) {
                    info.getRecordInfo().setTotalTime(0);
                }
                mRecordHandler.postDelayed(info.getRecordInfo().getServiceRunnable(), 1000);
                NotificationInfo notificationInfo = info.getNotificationInfo();
                if (notificationInfo != null) {
                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(notificationInfo.getId(), notificationInfo.getNotification());
                    startForeground(notificationInfo.getId(), notificationInfo.getNotification());
                }
            }
        }

        public void stopRecorder(ActivityInfo info) {
//            ActivityInfo activityInfo = mActivityInfoMap.get(info.getId());
            if (info != null) {
                mRecordHandler.removeCallbacks(info.getRecordInfo().getServiceRunnable());
                NotificationInfo notificationInfo = info.getNotificationInfo();
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancel(notificationInfo.getId());
                stopForeground(true);
            }
        }

    }

    private void showNotification(ActivityInfo info) {
        final RecordInfo recordInfo = info.getRecordInfo();
        final Notification notification = new Notification(R.mipmap.ic_launcher, null, 0);
        final RemoteViews notificationView = new RemoteViews(getPackageName(), R.layout.notification_content_view);
        notification.contentView = notificationView;

        final NotificationInfo notificationInfo = new NotificationInfo();
        notificationInfo.setRemoteViews(notificationView);
        notificationInfo.setId(GenerateNotificationID.getID());
        notificationInfo.setNotification(notification);
        info.setNotificationInfo(notificationInfo);

        Intent pauseIntent = new Intent();
        pauseIntent.putExtra("activityInfo", info);
        pauseIntent.setAction(BaseConstant.NOTIFICATION_CLICK_PAUSE);
        PendingIntent pendingPauseIntent = PendingIntent.getBroadcast(this, notificationInfo.getId(), pauseIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        notificationView.setOnClickPendingIntent(R.id.notification_iv_pause, pendingPauseIntent);

        Intent stopIntent = new Intent();
        stopIntent.putExtra("activityInfo", info);
        stopIntent.setAction(BaseConstant.NOTIFICATION_CLICK_STOP);
        pauseIntent.setData(Uri.parse(String.valueOf(System.currentTimeMillis() + 1000)));
        PendingIntent pendingStopIntent = PendingIntent.getBroadcast(this, 0, stopIntent, 0);
        notificationView.setOnClickPendingIntent(R.id.notification_iv_stop, pendingStopIntent);


        final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationInfo.getId(), notification);

        startForeground(notificationInfo.getId(), notification);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                notificationView.setTextViewText(R.id.notification_tv_record_time, FormatTime.calculateTimeString(recordInfo.getTotalTime()));
                mRecordHandler.postDelayed(this, 1000);
                recordInfo.setServiceRunnable(this);
                notificationManager.notify(notificationInfo.getId(), notification);
            }
        };
        recordInfo.setServiceRunnable(runnable);
        mRecordHandler.postDelayed(runnable, 1000);
    }


    private void setClickIntent(ActivityInfo info) {
        Intent pauseIntent = new Intent();
        pauseIntent.putExtra("activityInfo", info);
        pauseIntent.setAction(BaseConstant.NOTIFICATION_CLICK_PAUSE);
        PendingIntent pendingPauseIntent = PendingIntent.getBroadcast(this, info.getNotificationInfo().getId(), pauseIntent, PendingIntent.FLAG_CANCEL_CURRENT);


        Intent stopIntent = new Intent();
        stopIntent.putExtra("activityInfo", info);
        stopIntent.setAction(BaseConstant.NOTIFICATION_CLICK_STOP);
        PendingIntent pendingStopIntent = PendingIntent.getBroadcast(this, info.getNotificationInfo().getId(), stopIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        if (info.getNotificationInfo() != null) {
            info.getNotificationInfo().getRemoteViews().setOnClickPendingIntent(R.id.notification_iv_pause, pendingPauseIntent);
            info.getNotificationInfo().getRemoteViews().setOnClickPendingIntent(R.id.notification_iv_stop, pendingStopIntent);

        }
    }

}
