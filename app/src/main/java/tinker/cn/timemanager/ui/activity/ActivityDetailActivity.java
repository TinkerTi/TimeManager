package tinker.cn.timemanager.ui.activity;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import tinker.cn.timemanager.R;
import tinker.cn.timemanager.db.DaoManager;
import tinker.cn.timemanager.model.ActivityInfo;
import tinker.cn.timemanager.model.BaseConstant;
import tinker.cn.timemanager.model.SelectInfo;
import tinker.cn.timemanager.utils.DateUtils;
import tinker.cn.timemanager.ui.widget.SelectShowTimePopupWindow;

/**
 * Created by tiankui on 2/13/17.
 */

public class ActivityDetailActivity extends BaseActivity {

    private ActivityInfo activityInfo;

    private LineChart recordLineChart;
    private ImageView selectShowTypeImageView;
    private TextView descriptionTitleTextView;

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
        descriptionTitleTextView=(TextView)findViewById(R.id.tv_description_title);
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
                selectInfo.setCount(7);
                selectInfo.setLabel("最近七天记录");
                selectInfo.setType(BaseConstant.DAY_TYPE);
                drawRecordLineChart(selectInfo);
                descriptionTitleTextView.setText(R.string.seven_days);
                break;
            case BaseConstant.SELECT_SHOW_SEVEN_WEEK_RECORD:
                selectInfo.setCount(7);
                selectInfo.setType(BaseConstant.WEEK_TYPE);
                selectInfo.setLabel("最近七周记录");
                drawRecordLineChart(selectInfo);
                descriptionTitleTextView.setText(R.string.seven_weeks);
                break;
            case BaseConstant.SELECT_SHOW_ONE_YEAR_RECORD:
                selectInfo.setCount(12);
                selectInfo.setType(BaseConstant.MONTH_TYPE);
                selectInfo.setLabel("最近一年记录");
                descriptionTitleTextView.setText(R.string.one_year);
                drawRecordLineChart(selectInfo);
                break;
            case BaseConstant.SELECT_SHOW_SEVEN_YEAR_RECORD:
                selectInfo.setCount(7);
                selectInfo.setType(BaseConstant.YEAR_TYPE);
                selectInfo.setLabel("最近七年记录");
                drawRecordLineChart(selectInfo);
                descriptionTitleTextView.setText(R.string.seven_years);
                break;
        }
    }

    private void drawRecordLineChart(SelectInfo selectInfo) {
        try {
            List<Entry> entries = new ArrayList<>();

            for (int i = 0, j = selectInfo.getCount(); i < selectInfo.getCount(); i++, j--) {
                List<ActivityInfo> info = getSpecifiedTime(activityInfo.getId(),
                        String.valueOf(DateUtils.getStartTimeByType(selectInfo.getType(),i)),
                        String.valueOf(DateUtils.getEndTimeByType(selectInfo.getType(),i)));
                if (info.size() > 0) {
                    entries.add(0, new Entry(j, info.get(0).getRecordInfo().getDuration() / BaseConstant.MILLISECONDS_PER_HOUR));
                } else {
                    entries.add(0, new Entry(j, 0));
                }
            }
            LineDataSet dataSet = new LineDataSet(entries, selectInfo.getLabel());
            dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
            dataSet.setColor(Color.BLUE);
            dataSet.setValueTextColor(Color.BLACK);
            LineData lineData = new LineData(dataSet);
            lineData.setValueFormatter(new ValueFormatter());
            recordLineChart.setData(lineData);
            recordLineChart.setDoubleTapToZoomEnabled(false);
            //设置x轴属性
            XAxis xAxis=recordLineChart.getXAxis();
            xAxis.setDrawGridLines(false);
            xAxis.setDrawAxisLine(true);
            xAxis.setAxisMinimum(1);
            xAxis.setAxisMaximum(selectInfo.getCount());
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(1f);
            //设置y轴属性
            YAxis leftAxis=recordLineChart.getAxisLeft();
            YAxis rightAxis=recordLineChart.getAxisRight();
            leftAxis.setDrawGridLines(false);
            leftAxis.setDrawAxisLine(true);
            leftAxis.setDrawZeroLine(false);
            rightAxis.setDrawAxisLine(false);
            rightAxis.setDrawGridLines(false);
            rightAxis.setDrawLabels(false);

            recordLineChart.getLegend().setEnabled(false);
            Description description=new Description();
            description.setText("");
            recordLineChart.setDescription(description);
            recordLineChart.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<ActivityInfo> getSpecifiedTime(String id, String beginTime, String endTime) {
        Cursor cursor = DaoManager.getInstance().getSpecifiedTimeRecord(id, beginTime, endTime);
        return DaoManager.getInstance().parseCursor(cursor);
    }


    private class ValueFormatter implements IValueFormatter{

        private DecimalFormat mFormat;
        public ValueFormatter(){
            mFormat =new DecimalFormat("0.##");
        }
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return mFormat.format(value);
        }
    }
}
