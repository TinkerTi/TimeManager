package tinker.cn.timemanager.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.widget.RemoteViews;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import tinker.cn.timemanager.R;
import tinker.cn.timemanager.activity.MainActivity;
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
    private Map<String, ActivityInfo> mActivityInfoMap;


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

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public class RecordServiceBinder extends Binder {

        public void cancelNotification(ActivityInfo info) {
            mRecordHandler.removeCallbacks(info.getRecordInfo().getServiceRunnable());
            NotificationInfo notificationInfo = info.getNotificationInfo();
            setClickIntent(info);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(notificationInfo.getId());
            stopForeground(true);
        }

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
                if (notificationInfo != null) {
                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.cancel(notificationInfo.getId());
//                    stopForeground(true);
                }
            }
        }

        public void resumeRecorder(ActivityInfo info) {
            if (info != null) {
                if (info.getRecordInfo().getRecordState() == BaseConstant.STOP_STATE) {
                    info.getRecordInfo().setTotalTime(0);
                }
                mRecordHandler.postDelayed(info.getRecordInfo().getServiceRunnable(), 1000);
                NotificationInfo notificationInfo = info.getNotificationInfo();
                setClickIntent(info);
                if (notificationInfo != null) {
                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(notificationInfo.getId(), notificationInfo.getNotification());
//                    startForeground(notificationInfo.getId(), notificationInfo.getNotification());
                } else {
                    showNotification(info);
                }
            }
        }

        public void stopRecorder(ActivityInfo info) {
            if (info != null) {
                mRecordHandler.removeCallbacks(info.getRecordInfo().getServiceRunnable());
                NotificationInfo notificationInfo = info.getNotificationInfo();
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (notificationInfo != null) {
                    manager.cancel(notificationInfo.getId());
//                    stopForeground(true);
                }
            }
        }

        public Set<String> getNotificationActivityID() {
            return mActivityInfoMap.keySet();
        }

        public ActivityInfo getActivityInfoById(String id) {
            return mActivityInfoMap.get(id);
        }


    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void showNotification(final ActivityInfo info) {
        final RecordInfo recordInfo = info.getRecordInfo();

        final RemoteViews notificationView = new RemoteViews(getPackageName(), R.layout.notification_content_view);
        Intent startActivityIntent = new Intent(RecordService.this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), BaseConstant.NOTIFICATION_START_ACTIVITY, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        final Notification notification = new Notification.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContent(notificationView)
                .setContentIntent(pendingIntent)
                .build();

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
        notificationManager.cancel(notificationInfo.getId());
        notificationManager.notify(notificationInfo.getId(), notification);
//        startForeground(notificationInfo.getId(), notification);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                recordInfo.setTotalTime(recordInfo.getTotalTime() + 1000);
                notificationView.setTextViewText(R.id.notification_tv_record_time, FormatTime.calculateTimeString(recordInfo.getTotalTime()));
                mRecordHandler.postDelayed(this, 1000);
                recordInfo.setServiceRunnable(this);
                notificationManager.notify(notificationInfo.getId(), notification);
            }
        };
        recordInfo.setServiceRunnable(runnable);
        mRecordHandler.postDelayed(runnable, 1000);
        mActivityInfoMap.put(info.getId(), info);
    }

    private void setClickIntent(ActivityInfo info) {
        if (info.getNotificationInfo() != null) {
            Intent pauseIntent = new Intent();
            pauseIntent.putExtra("activityInfo", info);
            pauseIntent.setAction(BaseConstant.NOTIFICATION_CLICK_PAUSE);
            PendingIntent pendingPauseIntent = PendingIntent.getBroadcast(this, info.getNotificationInfo().getId(), pauseIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            Intent stopIntent = new Intent();
            stopIntent.putExtra("activityInfo", info);
            stopIntent.setAction(BaseConstant.NOTIFICATION_CLICK_STOP);
            PendingIntent pendingStopIntent = PendingIntent.getBroadcast(this, info.getNotificationInfo().getId(), stopIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            info.getNotificationInfo().getRemoteViews().setOnClickPendingIntent(R.id.notification_iv_pause, pendingPauseIntent);
            info.getNotificationInfo().getRemoteViews().setOnClickPendingIntent(R.id.notification_iv_stop, pendingStopIntent);
        }
    }


}
