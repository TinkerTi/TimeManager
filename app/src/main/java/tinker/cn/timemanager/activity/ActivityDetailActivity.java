package tinker.cn.timemanager.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import tinker.cn.timemanager.R;
import tinker.cn.timemanager.db.DaoManager;
import tinker.cn.timemanager.model.ActivityInfo;

/**
 * Created by tiankui on 2/13/17.
 */

public class ActivityDetailActivity extends BaseActivity {

    private ActivityInfo activityInfo;
    private long currentTime;
    private List<ActivityInfo> todayRecordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_info);
        activityInfo = getIntent().getParcelableExtra("activityInfo");
        if (activityInfo != null) {
            nameTextView.setText(activityInfo.getName());
        }
        todayRecordList=new ArrayList<>();
        initData();
    }

    private void initData() {
        Cursor cursor=DaoManager.getInstance().getTodayRecord(activityInfo.getId());
        todayRecordList=DaoManager.getInstance().parseCursor(cursor);
        Log.e("dd","ddd");
    }
}
