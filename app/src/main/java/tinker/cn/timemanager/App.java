package tinker.cn.timemanager;

import android.app.Application;

import tinker.cn.timemanager.utils.ActivityInfoManager;

/**
 * Created by tiankui on 1/6/17.
 */

public class App extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        ActivityInfoManager.getInstance().openDB(this,"01");
    }
}
