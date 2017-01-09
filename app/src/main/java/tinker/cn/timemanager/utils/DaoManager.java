package tinker.cn.timemanager.utils;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import tinker.cn.timemanager.model.ActivityInfo;
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
//    public List<ActivityInfo> parseCursor(Cursor cursor) {
//        List<ActivityInfo> infoList = new ArrayList<>();
//        if (cursor != null) {
//            try {
//                if (cursor.moveToFirst()) {
//                    do {
//                        ActivityInfo info = new ActivityInfo();
//                        info.setId(cursor.getString(1));
//                        info.setName(cursor.getString(2));
//                        info.setType(cursor.getInt(3));
//                        info.setParentGroupId(cursor.getString(4));
//                        RecordInfo recordInfo = new RecordInfo();
//                        recordInfo.setBeginTime(cursor.getLong(5));
//                        recordInfo.setEndTime(cursor.getLong(6));
//                        recordInfo.setDuration(cursor.getLong(7));
//                        recordInfo.setRecordState(cursor.getInt(8));
//                        info.setRecordInfo(recordInfo);
//                        info.setCreateTime(cursor.getLong(9));
//                        infoList.add(info);
//                    } while (cursor.moveToNext());
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return infoList;
//    }

    public List<ActivityInfo> parseCursor(Cursor cursor) {
        List<ActivityInfo> infoList = new ArrayList<>();
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    do {
                        ActivityInfo info = new ActivityInfo();
                        info.setId(cursor.getString(1));
                        info.setName(cursor.getString(2));
                        info.setType(cursor.getInt(3));
                        info.setParentGroupId(cursor.getString(4));
                        RecordInfo recordInfo = new RecordInfo();
                        recordInfo.setBeginTime(cursor.getLong(5));
                        recordInfo.setEndTime(cursor.getLong(6));
                        recordInfo.setDuration(cursor.getLong(7));
                        recordInfo.setRecordState(cursor.getInt(8));
                        info.setRecordInfo(recordInfo);
                        info.setCreateTime(cursor.getLong(9));
                        recordInfo.setTotalTime(cursor.getLong(7));
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
}
