package tinker.cn.timemanager;

import android.content.Context;
import android.support.v4.app.Fragment;

import java.util.Stack;

/**
 * Created by tiankui on 1/6/17.
 */

public class AppContext {

    private Context context;
    private static AppContext sInstance;

    private static Stack<Fragment> fragments;

    public AppContext(Context context) {
        this.context = context;
    }

    public static void init(Context context) {
        sInstance = new AppContext(context);
        fragments = new Stack<>();
    }

    public static AppContext getInstance() {
        return sInstance;
    }

    public void pushFragment(Fragment fragment) {
        fragments.push(fragment);
    }

    public void popFragment() {

    }

}
