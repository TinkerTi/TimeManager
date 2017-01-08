package tinker.cn.timemanager.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

/**
 * Created by tiankui on 1/4/17.
 */

public class RecordInfo implements Parcelable{

    private Runnable runnable;
    private View view;

    private long duration;
    private long beginTime;
    private long endTime;
    private int recordState;
    private long totalTime;

    public RecordInfo(){}
    protected RecordInfo(Parcel in) {
        recordState = in.readInt();
        duration = in.readLong();
        beginTime = in.readLong();
        endTime = in.readLong();
        totalTime=in.readLong();
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
        dest.writeInt(recordState);
        dest.writeLong(duration);
        dest.writeLong(beginTime);
        dest.writeLong(endTime);
        dest.writeLong(totalTime);
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


    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

}
