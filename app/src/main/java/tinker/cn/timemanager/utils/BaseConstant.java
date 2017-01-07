package tinker.cn.timemanager.utils;

import android.provider.BaseColumns;

/**
 * Created by tiankui on 1/5/17.
 */

public final class BaseConstant {

    public static final String DB_NAME="activityInfo";
    public static final int DB_VERSION=1;

    public static final int CREATE_ACTIVITY_OR_GROUP=1;
    public static final int CREATE_ACTIVITY_ONLY=2;

    public static final String Parent_GROUP_ID="";

    public static final int CREATE_TYPE_ACTIVITY=0;
    public static final int CREATE_TYPE_GROUP=1;

    private BaseConstant(){}

    public static class Activities implements BaseColumns{

        public static final String TABLE_NAME="activities";
        public static final String COLUMN_ID="id";
        public static final String COLUMN_NAME="name";
        public static final String COLUMN_TYPE="type";
        public static final String COLUMN_PARENT_GROUP_ID="parentGroupId";
        public static final String COLUMN_BEGIN_TIME="beginTime";
        public static final String COLUMN_END_TIME="endTime";
        public static final String COLUMN_DURATION="timeDuration";
        public static final String COLUMN_RECORD_STATE="recordState";


        public static final String SQL_CREATE_ACTIVITIES_TABLE="create table if not exists "+Activities.TABLE_NAME
                +" ("
                +Activities._ID+" integer primary key,"
                +Activities.COLUMN_ID+" text,"
                +Activities.COLUMN_NAME+" text,"
                +Activities.COLUMN_TYPE+" integer,"
                +Activities.COLUMN_PARENT_GROUP_ID +" text,"
                +Activities.COLUMN_BEGIN_TIME+" integer,"
                +Activities.COLUMN_END_TIME+" integer,"
                +Activities.COLUMN_DURATION+" integer,"
                +Activities.COLUMN_RECORD_STATE+" integer"
                +");";

        public static final String ACTIVITIES_TABLE_ADD_INDEX="create index "
                +"if not exists "
                +"idx_activities_id_type_parentGroupId on activities(id,type,parentGroupId);";
    }
}
