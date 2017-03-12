package tinker.cn.timemanager.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import tinker.cn.timemanager.R;

/**
 * Created by tiankui on 3/11/17.
 */

public class SelectPlanPopupWindow extends PopupWindow {
    Context context;
    int popupWidth;
    int popupHeight;
    public SelectPlanPopupWindow(Activity context){
        this.context=context;
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.add_plan_popup_window,null);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupWidth=view.getMeasuredWidth();
        popupHeight=view.getMeasuredHeight();
        setContentView(view);
        TextView everydayPlan=(TextView) view.findViewById(R.id.tv_add_every_day_plan);
        TextView thisWeekPlan=(TextView)view.findViewById(R.id.tv_add_this_week_plan);
        TextView longTimePlan=(TextView)view.findViewById(R.id.tv_add_this_long_time_plan);

        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
//        setTouchable(true);
        everydayPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        thisWeekPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        longTimePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        //必须要调用这个；
//        update();


    }


    public void showPopupWindow(View anchor){
        int[] location=new int[2];
        anchor.getLocationOnScreen(location);
        if(!isShowing()){
            showAtLocation(anchor, Gravity.NO_GRAVITY,(location[0]+anchor.getWidth()-popupWidth),(location[1]-popupHeight));
//            showAsDropDown(anchor);
        }else {
            dismiss();
        }
    }


}
