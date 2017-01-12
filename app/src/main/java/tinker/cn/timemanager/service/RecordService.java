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
        final ActivityInfo info = intent.getParcelableExtra("activityInfo");
        if (info != null) {
            mActivityInfoMap.put(info.getId(), info);
            final RecordInfo recordInfo = info.getRecordInfo();
            final Notification notification = new Notification(R.mipmap.ic_launcher, null, 0);
            final RemoteViews notificationView = new RemoteViews(getPackageName(), R.layout.notification_content_view);
            notification.contentView = notificationView;
            Intent pauseIntent = new Intent();
            PendingIntent pendingPauseIntent = PendingIntent.getBroadcast(this, 0, pauseIntent, 0);
            notificationView.setOnClickPendingIntent(R.id.notification_iv_pause, pendingPauseIntent);
            notificationView.setOnClickPendingIntent(R.id.notification_iv_stop, pendingPauseIntent);
            //TODO:set TextView text
            startForeground(BaseConstant.ONGOING_NOTIFICATION_ID, notification);
            final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(BaseConstant.ONGOING_NOTIFICATION_ID, notification);
            NotificationInfo notificationInfo=new NotificationInfo();
            notificationInfo.setRemoteViews(notificationView);
            notificationInfo.setId(BaseConstant.ONGOING_NOTIFICATION_ID);
            notificationInfo.setNotification(notification);
            notificationInfo.setManager(notificationManager);
            info.setNotificationInfo(notificationInfo);
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    recordInfo.setTotalTime(recordInfo.getTotalTime() + 1000);
                    notificationView.setTextViewText(R.id.notification_tv_record_time, FormatTime.calculateTimeString(recordInfo.getTotalTime()));
                    mRecordHandler.postDelayed(this, 1000);
                    recordInfo.setRunnable(this);
                    notificationManager.notify(BaseConstant.ONGOING_NOTIFICATION_ID, notification);
                }
            };
            mRecordHandler.postDelayed(runnable, 1000);
        }
        return new RecordServiceBinder();
    }

    public class RecordServiceBinder extends Binder {

        public void pauseRecorder(String id) {
            ActivityInfo info = mActivityInfoMap.get(id);
            if(info!=null){
                mRecordHandler.removeCallbacks(info.getRecordInfo().getRunnable());
                NotificationInfo notificationInfo=info.getNotificationInfo();
                RemoteViews remoteViews=notificationInfo.getRemoteViews();
                if(remoteViews!=null){
                     remoteViews.setImageViewResource(R.id.notification_iv_pause,R.mipmap.record_start);
                }
                NotificationManager manager=notificationInfo.getManager();
                manager.notify(notificationInfo.getId(),notificationInfo.getNotification());
            }
        }

        public void resumeRecorder(String id){
            ActivityInfo info=mActivityInfoMap.get(id);
            mRecordHandler.postDelayed(info.getRecordInfo().getRunnable(),1000);
            NotificationInfo notificationInfo=info.getNotificationInfo();
            RemoteViews remoteViews=notificationInfo.getRemoteViews();
            if(remoteViews!=null){
                remoteViews.setImageViewResource(R.id.notification_iv_pause,R.mipmap.record_pause);
            }
            NotificationManager manager=notificationInfo.getManager();
            manager.notify(notificationInfo.getId(),notificationInfo.getNotification());
        }

        public void stopRecorder(String id){
            ActivityInfo info=mActivityInfoMap.get(id);
            mRecordHandler.removeCallbacks(info.getRecordInfo().getRunnable());
            NotificationInfo notificationInfo=info.getNotificationInfo();
            NotificationManager manager=notificationInfo.getManager();
            manager.cancel(notificationInfo.getId());
            mActivityInfoMap.remove(id);
            if(mActivityInfoMap.size()==0){
                stopForeground(true);
            }
        }
    }

}
