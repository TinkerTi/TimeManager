package tinker.cn.timemanager.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

/**
 * Created by tiankui on 1/5/17.
 */

public class ActivityInfoDaoHelper extends SQLiteOpenHelper {

    private static String dbPath;

    public ActivityInfoDaoHelper(Context context) {
        super(new DatabaseContext(context, dbPath), BaseConstant.DB_NAME, null, BaseConstant.DB_VERSION);
    }


    public static void setDatabasePath(Context context, String userId) {
        dbPath = context.getFilesDir().getAbsolutePath();
        dbPath = dbPath + File.separator + userId;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BaseConstant.Activities.SQL_CREATE_ACTIVITIES_TABLE);
        db.execSQL(BaseConstant.Activities.ACTIVITIES_TABLE_ADD_INDEX);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
