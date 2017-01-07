package tinker.cn.timemanager.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;

/**
 * Created by tiankui on 1/5/17.
 */

public class DatabaseContext extends ContextWrapper {

    private String databaseDir;

    public DatabaseContext(Context context, String path) {
        super(context);
        this.databaseDir = path;
    }

    @Override
    public File getDatabasePath(String name) {
        File pathName = new File(databaseDir + File.separator + name);

        if (!pathName.getParentFile().exists()) {
            pathName.getParentFile().mkdir();
        }
        return pathName;
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name),factory);
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name).getAbsolutePath(),factory,errorHandler);
    }
}
