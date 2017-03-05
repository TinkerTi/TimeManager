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
    private long currentTime;
    private List<ActivityInfo> zeroRecord;
    private List<ActivityInfo> oneRecord;
    private List<ActivityInfo> twoRecord;
    private List<ActivityInfo> threeRecord;
    private List<ActivityInfo> fourRecord;
    private List<ActivityInfo> fiveRecord;
    private List<ActivityInfo> sixRecord;
    private List<ActivityInfo> sevenRecord;

    private LineChart lineChart;
    private List<Long> recordTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_info);
        activityInfo = getIntent().getParcelableExtra("activityInfo");
        if (activityInfo != null) {
            nameTextView.setText(activityInfo.getName());
        }
        zeroRecord = new ArrayList<>();
        oneRecord = new ArrayList<>();
        twoRecord = new ArrayList<>();
        threeRecord = new ArrayList<>();
        fourRecord = new ArrayList<>();
        fiveRecord = new ArrayList<>();
        sixRecord = new ArrayList<>();
        sevenRecord = new ArrayList<>();
        recordTime = new ArrayList<>();
        lineChart = (LineChart) findViewById(R.id.lc_the_latest_seven_day_record);
        initData();
    }

    private void initData() {
        long todayMorning = DateUtils.getTimesMorning();
        long todayNight = DateUtils.getTimesNight();
        try {

            for(int i=0;i<7;i++){
                List<ActivityInfo> info=getSpecifiedTime(activityInfo.getId(),
                        String.valueOf(todayMorning-i*BaseConstant.MILLISECONDS_PER_DAY),
                        String.valueOf(todayNight-i*BaseConstant.MILLISECONDS_PER_DAY));
                if(info.size()>0){
                    recordTime.add(0,info.get(0).getRecordInfo().getDuration());
                }else {
                    recordTime.add(0,Long.valueOf(0));
                }
            }

            List<Entry> entries = new ArrayList<>();
            int i = 0;
            for (Long data : recordTime) {
                entries.add(new Entry(i++, data));
            }
            LineDataSet dataSet = new LineDataSet(entries, "最近七天记录");
            dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
            dataSet.setColor(Color.GRAY);
            dataSet.setValueTextColor(Color.BLACK);
            LineData lineData = new LineData(dataSet);
            lineChart.setData(lineData);
            lineChart.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<ActivityInfo> getSpecifiedTime(String id, String beginTime, String endTime) {
        Cursor cursor = DaoManager.getInstance().getSpecifiedTimeRecord(id, beginTime, endTime);
        return DaoManager.getInstance().parseCursor(cursor);
    }
}
