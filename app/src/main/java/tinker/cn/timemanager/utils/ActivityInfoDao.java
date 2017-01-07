package tinker.cn.timemanager.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import tinker.cn.timemanager.model.ActivityInfo;

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
            ContentValues contentValues=new ContentValues();
            contentValues.put(BaseConstant.Activities.COLUMN_ID,info.getId());
            contentValues.put(BaseConstant.Activities.COLUMN_NAME,info.getName());
            contentValues.put(BaseConstant.Activities.COLUMN_TYPE,info.getType());
            contentValues.put(BaseConstant.Activities.COLUMN_PARENT_GROUP_ID,info.getParentGroupId());

            database.insert(BaseConstant.Activities.TABLE_NAME,null,contentValues);
        }
    }

    public void updateRecordInfo(ActivityInfo info){
        if(info!=null){

        }

    }

    public void getActivityInfo(ActivityInfo info){

    }

}
