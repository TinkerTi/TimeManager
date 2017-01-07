package tinker.cn.timemanager.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

/**
 * Created by tiankui on 1/4/17.
 */

public class RecordInfo implements Parcelable{
    public static final int READY_STATE=0;
    public static final int RECORDING_STATE=1;
    public static final int PAUSE_STATE=2;
    public static final int STOP_STATE=3;

    private long recordTime=0;

    private Runnable runnable;
    private View view;

    private long duration;
    private long beginTime;
    private long endTime;
    private int recordState;

    public RecordInfo(){}
    protected RecordInfo(Parcel in) {
        recordTime = in.readLong();
        recordState = in.readInt();
        duration = in.readLong();
        beginTime = in.readLong();
        endTime = in.readLong();
    }

    public static final Creator<RecordInfo> CREATOR = new Creator<RecordInfo>() {
        @Override
        public RecordInfo createFromParcel(Parcel in) {
            return new RecordInfo(in);
        }

        @Override
        public RecordInfo[] newArray(int size) {
            return new RecordInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(recordTime);
        dest.writeInt(recordState);
        dest.writeLong(duration);
        dest.writeLong(beginTime);
        dest.writeLong(endTime);
    }

    public long getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(long recordTime) {
        this.recordTime = recordTime;
    }

    public int getRecordState() {
        return recordState;
    }

    public void setRecordState(int recordState) {
        this.recordState = recordState;
    }
    public Runnable getRunnable() {
        return runnable;
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }


    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }
}
