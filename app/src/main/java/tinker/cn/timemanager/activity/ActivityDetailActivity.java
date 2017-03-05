package tinker.cn.timemanager.activity;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import tinker.cn.timemanager.R;
import tinker.cn.timemanager.db.DaoManager;
import tinker.cn.timemanager.model.ActivityInfo;
import tinker.cn.timemanager.model.BaseConstant;
import tinker.cn.timemanager.utils.DateUtils;

/**
 * Created by tiankui on 2/13/17.
 */

public class ActivityDetailActivity extends BaseActivity {

    private ActivityInfo activityInfo;

    private LineChart recordLineChart;

    private int selectedShowType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_info);
        activityInfo = getIntent().getParcelableExtra("activityInfo");
        if (activityInfo != null) {
            nameTextView.setText(activityInfo.getName());
        }
        recordLineChart = (LineChart) findViewById(R.id.lc_the_latest_seven_day_record);
        initData();
    }

    private void initData() {
        selectedShowType = BaseConstant.SELECT_SHOW_SEVEN_DAY_RECORD;
        selectShowType(selectedShowType);
    }

    private void selectShowType(int selectedShowType) {
        switch (selectedShowType) {
            case BaseConstant.SELECT_SHOW_SEVEN_DAY_RECORD:
                drawRecordLineChart(DateUtils.getTodayMorning(), DateUtils.getTodayNight(), BaseConstant.MILLISECONDS_PER_DAY, 7);
                break;
            case BaseConstant.SELECT_SHOW_SEVEN_WEEK_RECORD:
                drawRecordLineChart(DateUtils.getCurrentWeekMorning(), DateUtils.getCurrentWeekNight(), BaseConstant.MILLISECONDS_PER_WEEK, 7);
                break;
            case BaseConstant.SELECT_SHOW_ONE_YEAR_RECORD:
                drawRecordLineChart(DateUtils.getCurrentMonthMorning(), DateUtils.getCurrentMonthNight(), BaseConstant.MILLISECONDS_PER_MONTH, 12);
                break;
            case BaseConstant.SELECT_SHOW_SEVEN_YEAR_RECORD:
                drawRecordLineChart(DateUtils.getCurrentYearStartTime(), DateUtils.getCurrentYearEndTime(), BaseConstant.MILLISECONDS_PER_YEAR, 7);
                break;
        }
    }

    private void drawRecordLineChart(long startTime, long endTime, long timeUnit, int count) {
        try {
            List<Entry> entries = new ArrayList<>();

            for (int i = 0, j = count; i < count; i++, j--) {
                List<ActivityInfo> info = getSpecifiedTime(activityInfo.getId(),
                        String.valueOf(startTime - i * timeUnit),
                        String.valueOf(endTime - i * timeUnit));
                if (info.size() > 0) {
                    entries.add(0, new Entry(j, info.get(0).getRecordInfo().getDuration() / BaseConstant.MILLISECONDS_PER_MINUTE));
                } else {
                    entries.add(0, new Entry(j, Float.valueOf(0)));
                }
            }
            LineDataSet dataSet = new LineDataSet(entries, "最近七天记录");
            dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
            dataSet.setColor(Color.GRAY);
            dataSet.setValueTextColor(Color.BLACK);
            LineData lineData = new LineData(dataSet);
            recordLineChart.setData(lineData);
            recordLineChart.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<ActivityInfo> getSpecifiedTime(String id, String beginTime, String endTime) {
        Cursor cursor = DaoManager.getInstance().getSpecifiedTimeRecord(id, beginTime, endTime);
        return DaoManager.getInstance().parseCursor(cursor);
    }
}
