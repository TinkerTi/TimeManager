package tinker.cn.timemanager.ui.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import tinker.cn.timemanager.R;
import tinker.cn.timemanager.ui.activity.ActivityDetailActivity;
import tinker.cn.timemanager.model.BaseConstant;

/**
 * Created by tiankui on 3/5/17.
 */

public class SelectShowTimePopupWindow extends PopupWindow {

    private ActivityDetailActivity activityDetailActivity = null;

    public SelectShowTimePopupWindow(Activity context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.select_show_time_popup_window, null);
        TextView showSevenDayTextView = (TextView) view.findViewById(R.id.tv_show_seven_day);
        TextView showSevenWeekTextView = (TextView) view.findViewById(R.id.tv_show_seven_weeks);
        TextView showOneYearTextView = (TextView) view.findViewById(R.id.tv_show_one_year);
        TextView showSevenYearTextView = (TextView) view.findViewById(R.id.tv_show_seven_years);

        setContentView(view);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable(24420863);
        setBackgroundDrawable(dw);
        update();

        if (context instanceof ActivityDetailActivity) {
            activityDetailActivity = (ActivityDetailActivity) context;
        }

        showSevenDayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityDetailActivity != null) {
                    activityDetailActivity.selectShowType(BaseConstant.SELECT_SHOW_SEVEN_DAY_RECORD);
                    dismiss();
                }
            }
        });

        showSevenWeekTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityDetailActivity != null) {
                    activityDetailActivity.selectShowType(BaseConstant.SELECT_SHOW_SEVEN_WEEK_RECORD);
                    dismiss();
                }
            }
        });

        showOneYearTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityDetailActivity != null) {
                    activityDetailActivity.selectShowType(BaseConstant.SELECT_SHOW_ONE_YEAR_RECORD);
                    dismiss();
                }
            }
        });

        showSevenYearTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityDetailActivity != null) {
                    activityDetailActivity.selectShowType(BaseConstant.SELECT_SHOW_SEVEN_YEAR_RECORD);
                    dismiss();
                }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void showPopupWindow(View anchor) {
        if (!isShowing()) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                showAsDropDown(anchor, -60, 10);
            } else {
                showAsDropDown(anchor, -90, 0, Gravity.TOP | Gravity.RIGHT);
            }
        } else {
            dismiss();
        }
    }
}
