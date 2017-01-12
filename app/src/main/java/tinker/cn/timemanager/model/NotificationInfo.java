package tinker.cn.timemanager.model;

import android.app.Notification;
import android.app.NotificationManager;
import android.widget.RemoteViews;

/**
 * Created by tiankui on 1/12/17.
 */

public class NotificationInfo {

    private int id;
    private Notification notification;
    private RemoteViews remoteViews;
    private NotificationManager manager;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RemoteViews getRemoteViews() {
        return remoteViews;
    }

    public void setRemoteViews(RemoteViews remoteViews) {
        this.remoteViews = remoteViews;
    }

    public NotificationManager getManager() {
        return manager;
    }

    public void setManager(NotificationManager manager) {
        this.manager = manager;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }
}
