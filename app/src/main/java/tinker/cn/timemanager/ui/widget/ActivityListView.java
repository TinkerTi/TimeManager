package tinker.cn.timemanager.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by tiankui on 1/9/17.
 */

public class ActivityListView extends ListView{


    public ActivityListView(Context context) {
        super(context);
    }

    public ActivityListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ActivityListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
