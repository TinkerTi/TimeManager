package tinker.cn.timemanager;

import android.app.Application;

import tinker.cn.timemanager.utils.DaoManager;

/**
 * Created by tiankui on 1/6/17.
 */

public class App extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        DaoManager.getInstance().openDB(this,"01");
    }
}
