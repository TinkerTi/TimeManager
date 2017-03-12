package tinker.cn.timemanager.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tiankui on 1/3/17.
 */

public class ActivityInfo implements Parcelable {

    /**
     * activity有两类，一类是独立的Activity，一类是存在于群组类的Activity，
     * 其实这两类也可以看做一类，即都是Activity，只是第一类Activity所属的群组为空
     * 第二类归属于某一个群组；
     */
    private String id;//每一个后动或者群组对应一个id；这个要个数据库中的每一排的唯一的id区分开
    private String name;    //活动或者群组名；

    private int priority;//目标优先级


    private long planTime;//计划时间
    /**
     *   旧的规则是：所属群组，如果是单个活动或者群组，则为空""，否则为所属群组id
     *   新的规则：当前的如果是每日目标，则可以选择加入一个周目标，那么日目标活动时间将会计入到周目标的时间内；
     *   或者是周目标可以加入一个长期目标；
     */
    private String parentGroupId;
    /**
     * 目标持续时间，举例来说如果是每日目标，值为2时，表示该目标会连续两天出现在每日目标的列表中，然后失效；
     */
    private long keepTime;
    private long originCreateTime;//活动创建的时间
    private String tag;//标识活动的类别，为日后的统计分类用；

    /**
     * 旧的用法：区别是活动还是群组，0是活动，1是群组
     *
     */
    private int type;




    private long createTime;//一次完整的时间记录中开始记录的时间（从start到stop的时间）
    private RecordInfo recordInfo;
    private NotificationInfo notificationInfo;
    private String fragmentTag;

    public ActivityInfo() {
    }

    protected ActivityInfo(Parcel in) {
        name = in.readString();
        fragmentTag = in.readString();
        recordInfo = in.readParcelable(RecordInfo.class.getClassLoader());
        id = in.readString();
        type = in.readInt();
        parentGroupId = in.readString();
        createTime = in.readLong();
        originCreateTime=in.readLong();
        tag=in.readString();
        priority=in.readInt();
        planTime=in.readLong();
        keepTime=in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(fragmentTag);
        dest.writeParcelable(recordInfo, flags);
        dest.writeString(id);
        dest.writeInt(type);
        dest.writeString(parentGroupId);
        dest.writeLong(createTime);
        dest.writeLong(originCreateTime);
        dest.writeString(tag);
        dest.writeInt(priority);
        dest.writeLong(planTime);
        dest.writeLong(keepTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ActivityInfo> CREATOR = new Creator<ActivityInfo>() {
        @Override
        public ActivityInfo createFromParcel(Parcel in) {
            return new ActivityInfo(in);
        }

        @Override
        public ActivityInfo[] newArray(int size) {
            return new ActivityInfo[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RecordInfo getRecordInfo() {
        return recordInfo;
    }

    public void setRecordInfo(RecordInfo recordInfo) {
        this.recordInfo = recordInfo;
    }

    public String getFragmentTag() {
        return fragmentTag;
    }

    public void setFragmentTag(String fragmentTag) {
        this.fragmentTag = fragmentTag;
    }


    public String getParentGroupId() {
        return parentGroupId;
    }

    public void setParentGroupId(String parentGroupId) {
        this.parentGroupId = parentGroupId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }


    public NotificationInfo getNotificationInfo() {
        return notificationInfo;
    }

    public void setNotificationInfo(NotificationInfo notificationInfo) {
        this.notificationInfo = notificationInfo;
    }



    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public long getOriginCreateTime() {
        return originCreateTime;
    }

    public void setOriginCreateTime(long originCreateTime) {
        this.originCreateTime = originCreateTime;
    }



    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public long getPlanTime() {
        return planTime;
    }

    public void setPlanTime(long planTime) {
        this.planTime = planTime;
    }

    public long getKeepTime() {
        return keepTime;
    }

    public void setKeepTime(long keepTime) {
        this.keepTime = keepTime;
    }


}
