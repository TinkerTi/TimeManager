package tinker.cn.timemanager.model;

import android.provider.BaseColumns;


/**
 * Created by tiankui on 1/5/17.
 */

public final class BaseConstant {

    public static final String NOTIFICATION_CLICK_PAUSE = "notification_click_pause";
    public static final String NOTIFICATION_CLICK_STOP = "notification_click_stop";
    public static final String NOTIFICATION_REAL_TIME = "notification_real_time";

    public static final int NOTIFICATION_START_ACTIVITY = 111;


    public static final String DB_NAME = "activityInfo";
    public static final int DB_VERSION = 1;

    public static final int CREATE_ACTIVITY_OR_GROUP = 1;
    public static final int CREATE_ACTIVITY_ONLY = 2;

    public static final String Parent_GROUP_ID = "";

    public static final int TYPE_ACTIVITY = 0;
    public static final int TYPE_GROUP = 1;

    //record state;
    public static final int READY_STATE = 0;
    public static final int RECORDING_STATE = 1;
    public static final int PAUSE_STATE = 2;
    public static final int STOP_STATE = 3;


    public static final float MILLISECONDS_PER_SECOND = 1000.00f;
    public static final float MILLISECONDS_PER_MINUTE = 60 * MILLISECONDS_PER_SECOND;
    public static final float MILLISECONDS_PER_HOUR = 60 * MILLISECONDS_PER_MINUTE;
    public static final long MILLISECONDS_PER_DAY = 24 * 60 * 60 * 1000;
    public static final long MILLISECONDS_PER_YEAR = 365 * MILLISECONDS_PER_DAY;
    public static final long MILLISECONDS_PER_MONTH = 30 * MILLISECONDS_PER_DAY;
    public static final long MILLISECONDS_PER_WEEK = 7 * MILLISECONDS_PER_DAY;

    public static final int DAY_TYPE = 0;
    public static final int WEEK_TYPE = 1;
    public static final int MONTH_TYPE = 2;
    public static final int YEAR_TYPE = 3;


    public static final int SELECT_SHOW_SEVEN_DAY_RECORD = 0;
    public static final int SELECT_SHOW_SEVEN_WEEK_RECORD = 1;
    public static final int SELECT_SHOW_ONE_YEAR_RECORD = 2;
    public static final int SELECT_SHOW_SEVEN_YEAR_RECORD = 3;


    public static final String RAW_QUERY_SELECT_MAX_TOTAL_TIME = "select "
            + Activities._ID + ","
            + Activities.COLUMN_ID + ","
            + Activities.COLUMN_NAME + ","
            + Activities.COLUMN_TYPE + ","
            + Activities.COLUMN_PARENT_GROUP_ID + ","
            + Activities.COLUMN_BEGIN_TIME + ","
            + Activities.COLUMN_END_TIME + ","
            + "sum(" + Activities.COLUMN_DURATION + "),"
            + Activities.COLUMN_RECORD_STATE + ","
            + Activities.COLUMN_CREATE_TIME + ","
            + Activities.COLUMN_TOTAL_TIME + ","
            + Activities.COLUMN_ORIGIN_CREATE_TIME + ","
            + Activities.COLUMN_TAG + " "
            + "from " + Activities.TABLE_NAME
            + " where " + Activities.COLUMN_PARENT_GROUP_ID
            + " = ?"
            + "group by " + Activities.COLUMN_ID
            + " order by " + Activities.COLUMN_ORIGIN_CREATE_TIME + " desc"
            + ";";

    public static final String FIRST_UPDATE_RECORD_TIME_WHERE_CONDITION = Activities.COLUMN_ID + " = ?";

    //根据活动开始的时间来更新活动记录的数据，而不是根据活动的id，因为同一个活动可以有很多条记录，这样的话就会把所有的记录都更改了；
    public static final String UPDATE_RECORD_TIME_WHERE_CONDITION = Activities.COLUMN_BEGIN_TIME + " = ?";
    public static final String GET_RECORDING_STATE_ACTIVITY = Activities.COLUMN_RECORD_STATE + " =?";

    private BaseConstant() {
    }

    public static class Activities implements BaseColumns {

        //TODO:需要添加一个字段，总共累积的时间
        public static final String TABLE_NAME = "activities";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_PARENT_GROUP_ID = "parentGroupId";
        public static final String COLUMN_BEGIN_TIME = "beginTime";
        public static final String COLUMN_END_TIME = "endTime";
        public static final String COLUMN_DURATION = "timeDuration";
        public static final String COLUMN_RECORD_STATE = "recordState";
        public static final String COLUMN_CREATE_TIME = "createTime";
        public static final String COLUMN_TOTAL_TIME = "totalTime";
        public static final String COLUMN_ORIGIN_CREATE_TIME = "originCreateTime";
        public static final String COLUMN_TAG = "tag";


        public static final String SQL_CREATE_ACTIVITIES_TABLE = "create table if not exists " + Activities.TABLE_NAME
                + " ("
                + _ID + " integer primary key,"
                + COLUMN_ID + " text,"
                + COLUMN_NAME + " text,"
                + COLUMN_TYPE + " integer,"
                + COLUMN_PARENT_GROUP_ID + " text,"
                + COLUMN_BEGIN_TIME + " integer,"
                + COLUMN_END_TIME + " integer,"
                + COLUMN_DURATION + " integer,"
                + COLUMN_RECORD_STATE + " integer,"
                + COLUMN_CREATE_TIME + " integer,"
                + COLUMN_TOTAL_TIME + " integer,"
                + COLUMN_ORIGIN_CREATE_TIME + " integer,"
                + COLUMN_TAG + " text"
                + ");";

        public static final String ACTIVITIES_TABLE_ADD_INDEX = "create index "
                + "if not exists "
                + "idx_activities_id_type_parentGroupId_beginTime on activities(id,type,parentGroupId,beginTime);";


    }
}
