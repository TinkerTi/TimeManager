package tinker.cn.timemanager.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tiankui on 1/3/17.
 */

public class ActivityInfo implements Parcelable{

    /**
     * activity有两类，一类是独立的Activity，一类是存在于群组类的Activity，
     * 其实这两类也可以看做一类，即都是Activity，只是第一类Activity所属的群组为空
     * 第二类归属于某一个群组；
     *
     */
    private String fragmentTag;
    private RecordInfo recordInfo;

    private String id;//每一个后动或者群组对应一个id；这个要个数据库中的每一排的唯一的id区分开
    private String name;    //活动或者群组名；
    private int type;   //是活动还是群组，
    private String parentGroupId;//所属群组，如果是单个活动或者群组，则为空""，否则为所属群组id



    private long createTime;

    public ActivityInfo(){}

    protected ActivityInfo(Parcel in) {
        name = in.readString();
        fragmentTag = in.readString();
        recordInfo = in.readParcelable(RecordInfo.class.getClassLoader());
        id = in.readString();
        type = in.readInt();
        parentGroupId = in.readString();
        createTime=in.readLong();
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

}
