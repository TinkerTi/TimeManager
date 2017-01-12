package tinker.cn.timemanager.model;

import android.app.Notification;
import android.widget.RemoteViews;

/**
 * Created by tiankui on 1/12/17.
 */

public class NotificationInfo {

    private int id;
    private Notification notification;
    private RemoteViews remoteViews;

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


    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }
}
