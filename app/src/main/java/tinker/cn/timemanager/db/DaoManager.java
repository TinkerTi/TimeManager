package tinker.cn.timemanager.db;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import tinker.cn.timemanager.model.ActivityInfo;
import tinker.cn.timemanager.model.BaseConstant;
import tinker.cn.timemanager.model.RecordInfo;

/**
 * Created by tiankui on 1/6/17.
 */

public class DaoManager {

    ActivityInfoDao mActivityInfoDataBase;

    private static DaoManager sInstance = new DaoManager();

    public DaoManager() {
        mActivityInfoDataBase = new ActivityInfoDao();
    }

    public static DaoManager getInstance() {
        return sInstance;
    }


    public void openDB(Context context, String userId) {
        mActivityInfoDataBase.open(context, userId);
    }

    public void closeDB() {
        mActivityInfoDataBase.close();
    }

    public void addActivity(ActivityInfo info) {
        mActivityInfoDataBase.addActivity(info);
    }

    public Cursor getActivityInfo(String[] columns, String selection, String[] selectionArgs, String orderBy,String groupBy) {
        return mActivityInfoDataBase.getActivityInfo(columns, selection, selectionArgs, orderBy,groupBy);
    }

    public Cursor getActivityInfo(String[] args){
        return mActivityInfoDataBase.getActivityInfo(args);
    }

    public List<ActivityInfo> parseCursor(Cursor cursor) {
        List<ActivityInfo> infoList = new ArrayList<>();
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    do {
                        ActivityInfo info = new ActivityInfo();
                        info.setId(cursor.getString(cursor.getColumnIndex(BaseConstant.Activities.COLUMN_ID)));
                        info.setName(cursor.getString(cursor.getColumnIndex(BaseConstant.Activities.COLUMN_NAME)));
                        info.setType(cursor.getInt(cursor.getColumnIndex(BaseConstant.Activities.COLUMN_TYPE)));
                        info.setParentGroupId(cursor.getString(cursor.getColumnIndex(BaseConstant.Activities.COLUMN_PARENT_GROUP_ID)));
                        RecordInfo recordInfo = new RecordInfo();
                        recordInfo.setBeginTime(cursor.getLong(cursor.getColumnIndex(BaseConstant.Activities.COLUMN_BEGIN_TIME)));
                        recordInfo.setEndTime(cursor.getLong(cursor.getColumnIndex(BaseConstant.Activities.COLUMN_END_TIME)));
                        recordInfo.setDuration(cursor.getLong(7));
                        recordInfo.setRecordState(cursor.getInt(cursor.getColumnIndex(BaseConstant.Activities.COLUMN_RECORD_STATE)));
                        info.setRecordInfo(recordInfo);
                        info.setCreateTime(cursor.getLong(cursor.getColumnIndex(BaseConstant.Activities.COLUMN_CREATE_TIME)));
                        recordInfo.setTotalTime(cursor.getLong(cursor.getColumnIndex(BaseConstant.Activities.COLUMN_TOTAL_TIME)));
                        info.setOriginCreateTime(cursor.getLong(cursor.getColumnIndex(BaseConstant.Activities.COLUMN_ORIGIN_CREATE_TIME)));
                        info.setTag(cursor.getString(cursor.getColumnIndex(BaseConstant.Activities.COLUMN_TAG)));
                        infoList.add(info);
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return infoList;
    }

    public int updateRecordInfo(RecordInfo info, String condition, String[] args){
       return mActivityInfoDataBase.updateRecordInfo(info,condition,args);
    }

    public Cursor getActivityInfo(String selection,String[] args){
        return mActivityInfoDataBase.getActivityInfo(selection,args);
    }

    public Cursor getTodayRecord(String id){
        return mActivityInfoDataBase.getTodayRecord(id);
    }
}
