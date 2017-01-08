package tinker.cn.timemanager.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import tinker.cn.timemanager.model.ActivityInfo;
import tinker.cn.timemanager.model.RecordInfo;

/**
 * Created by tiankui on 1/5/17.
 */

public class ActivityInfoDao {

    SQLiteDatabase database;


    public ActivityInfoDao(){}

    public void open(Context context, String userId) {
        ActivityInfoDaoHelper.setDatabasePath(context, userId);
        try {
            ActivityInfoDaoHelper daoHelper = new ActivityInfoDaoHelper(context);
            database = daoHelper.getReadableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (database != null) {
            database.close();
            database = null;
        }
    }

    public void addActivity(ActivityInfo info){
        if(info!=null){
            RecordInfo recordInfo=info.getRecordInfo();
            ContentValues contentValues=new ContentValues();
            contentValues.put(BaseConstant.Activities.COLUMN_ID,info.getId());
            contentValues.put(BaseConstant.Activities.COLUMN_NAME,info.getName());
            contentValues.put(BaseConstant.Activities.COLUMN_TYPE,info.getType());
            contentValues.put(BaseConstant.Activities.COLUMN_PARENT_GROUP_ID,info.getParentGroupId());
            contentValues.put(BaseConstant.Activities.COLUMN_CREATE_TIME,info.getCreateTime());
            contentValues.put(BaseConstant.Activities.COLUMN_BEGIN_TIME,recordInfo.getBeginTime());
            contentValues.put(BaseConstant.Activities.COLUMN_END_TIME,recordInfo.getEndTime());
            contentValues.put(BaseConstant.Activities.COLUMN_DURATION,recordInfo.getDuration());
            contentValues.put(BaseConstant.Activities.COLUMN_RECORD_STATE,recordInfo.getRecordState());
            database.insert(BaseConstant.Activities.TABLE_NAME,null,contentValues);
        }
    }


    public Cursor getActivityInfo(String[] columns,String selection,String[] selectionArgs,String orderBy){

        return database.query(BaseConstant.Activities.TABLE_NAME,columns,selection,selectionArgs,null,null,orderBy);
    }

    public int updateRecordInfo(RecordInfo info, String condition, String[] args){
        int count=-2;
        if(info!=null){
            ContentValues values=new ContentValues();
            values.put(BaseConstant.Activities.COLUMN_BEGIN_TIME,info.getBeginTime());
            values.put(BaseConstant.Activities.COLUMN_END_TIME,info.getEndTime());
            values.put(BaseConstant.Activities.COLUMN_DURATION,info.getDuration());
            values.put(BaseConstant.Activities.COLUMN_RECORD_STATE,info.getRecordState());

            count =database.update(BaseConstant.Activities.TABLE_NAME,values,condition,args);
        }
        return count;
    }

}
