package tinker.cn.timemanager.activity;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

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
import tinker.cn.timemanager.model.SelectInfo;
import tinker.cn.timemanager.utils.DateUtils;
import tinker.cn.timemanager.widget.SelectShowTimePopupWindow;

/**
 * Created by tiankui on 2/13/17.
 */

public class ActivityDetailActivity extends BaseActivity {

    private ActivityInfo activityInfo;

    private LineChart recordLineChart;
    private ImageView selectShowTypeImageView;

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
        selectShowTypeImageView = (ImageView) findViewById(R.id.iv_select_show_type);
        selectShowTypeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectShowTimePopupWindow popupWindow = new SelectShowTimePopupWindow(ActivityDetailActivity.this);
                popupWindow.showPopupWindow(selectShowTypeImageView);
            }
        });
        initData();
    }

    private void initData() {
        selectedShowType = BaseConstant.SELECT_SHOW_SEVEN_DAY_RECORD;
        selectShowType(selectedShowType);
    }

    public void selectShowType(int selectedShowType) {
        SelectInfo selectInfo = new SelectInfo();
        switch (selectedShowType) {
            case BaseConstant.SELECT_SHOW_SEVEN_DAY_RECORD:
                selectInfo.setBeginTime(DateUtils.getTodayMorning());
                selectInfo.setEndTime(DateUtils.getTodayNight());
                selectInfo.setTimeUnit(BaseConstant.MILLISECONDS_PER_DAY);
                selectInfo.setCount(7);
                selectInfo.setLabel("最近七天记录");
                drawRecordLineChart(selectInfo);
                break;
            case BaseConstant.SELECT_SHOW_SEVEN_WEEK_RECORD:
                selectInfo.setBeginTime(DateUtils.getCurrentWeekMorning());
                selectInfo.setEndTime(DateUtils.getCurrentWeekNight());
                selectInfo.setCount(7);
                selectInfo.setTimeUnit(BaseConstant.MILLISECONDS_PER_WEEK);
                selectInfo.setLabel("最近七周记录");
                drawRecordLineChart(selectInfo);
                break;
            case BaseConstant.SELECT_SHOW_ONE_YEAR_RECORD:
                selectInfo.setBeginTime(DateUtils.getCurrentMonthMorning());
                selectInfo.setEndTime(DateUtils.getCurrentMonthNight());
                selectInfo.setCount(12);
                selectInfo.setTimeUnit(BaseConstant.MILLISECONDS_PER_MONTH);
                selectInfo.setLabel("最近一年记录");
                drawRecordLineChart(selectInfo);
                break;
            case BaseConstant.SELECT_SHOW_SEVEN_YEAR_RECORD:
                selectInfo.setBeginTime(DateUtils.getCurrentYearStartTime());
                selectInfo.setEndTime(DateUtils.getCurrentYearEndTime());
                selectInfo.setCount(7);
                selectInfo.setTimeUnit(BaseConstant.MILLISECONDS_PER_YEAR);
                selectInfo.setLabel("最近七年记录");
                drawRecordLineChart(selectInfo);
                break;
        }
    }

    private void drawRecordLineChart(SelectInfo selectInfo) {
        try {
            List<Entry> entries = new ArrayList<>();

            for (int i = 0, j = selectInfo.getCount(); i < selectInfo.getCount(); i++, j--) {
                List<ActivityInfo> info = getSpecifiedTime(activityInfo.getId(),
                        String.valueOf(selectInfo.getBeginTime() - i * selectInfo.getTimeUnit()),
                        String.valueOf(selectInfo.getEndTime() - i * selectInfo.getTimeUnit()));
                if (info.size() > 0) {
                    entries.add(0, new Entry(j, info.get(0).getRecordInfo().getDuration() / BaseConstant.MILLISECONDS_PER_MINUTE));
                } else {
                    entries.add(0, new Entry(j, Float.valueOf(0)));
                }
            }
            LineDataSet dataSet = new LineDataSet(entries, selectInfo.getLabel());
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
