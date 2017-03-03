package tinker.cn.timemanager;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.dumpapp.DumperPlugin;
import com.facebook.stetho.inspector.database.DefaultDatabaseConnectionProvider;
import com.facebook.stetho.inspector.protocol.ChromeDevtoolsDomain;

import tinker.cn.timemanager.db.DaoManager;
import tinker.cn.timemanager.stetho.TMDatabaseDriver;
import tinker.cn.timemanager.stetho.TMDatabaseFilesProvider;
import tinker.cn.timemanager.stetho.TMDbFilesDumperPlugin;

/**
 * Created by tiankui on 1/6/17.
 */

public class App extends Application{

    @Override
    public void onCreate() {
        super.onCreate();


        DaoManager.getInstance().openDB(this,"01");
        AppContext.init(getApplicationContext());
        Stetho.initialize(new Stetho.Initializer(this) {
            @Override
            protected Iterable<DumperPlugin> getDumperPlugins() {
                return new Stetho.DefaultDumperPluginsBuilder(App.this)
                        .provide(new TMDbFilesDumperPlugin(App.this, new TMDatabaseFilesProvider(App.this)))
                        .finish();
            }

            @Override
            protected Iterable<ChromeDevtoolsDomain> getInspectorModules() {
                Stetho.DefaultInspectorModulesBuilder defaultInspectorModulesBuilder = new Stetho.DefaultInspectorModulesBuilder(App.this);
                defaultInspectorModulesBuilder.provideDatabaseDriver(new TMDatabaseDriver(App.this, new TMDatabaseFilesProvider(App.this), new DefaultDatabaseConnectionProvider()));
                return defaultInspectorModulesBuilder.finish();
            }
        });
//        Stetho.initialize(Stetho.newInitializerBuilder(this)
//                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
//                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
//                .build());
//        Stetho.initializeWithDefaults(this);
    }
}
