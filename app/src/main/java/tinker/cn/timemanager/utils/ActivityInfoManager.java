package tinker.cn.timemanager.utils;

import android.content.Context;

import tinker.cn.timemanager.model.ActivityInfo;

/**
 * Created by tiankui on 1/6/17.
 */

public class ActivityInfoManager {

    ActivityInfoDao mActivityInfoDataBase;

    private static ActivityInfoManager sInstance=new ActivityInfoManager();
    public ActivityInfoManager(){
        mActivityInfoDataBase=new ActivityInfoDao();
    }

    public static ActivityInfoManager getInstance(){
         return sInstance;
    }

    public void addActivity(ActivityInfo info){
        mActivityInfoDataBase.addActivity(info);
    }

    public void openDB(Context context,String userId){
          mActivityInfoDataBase.open(context,userId);
    }

    public void closeDB(){
        mActivityInfoDataBase.close();
    }
}
