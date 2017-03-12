package tinker.cn.timemanager.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import tinker.cn.timemanager.R;
import tinker.cn.timemanager.model.BaseConstant;
import tinker.cn.timemanager.ui.widget.AddImageButton;

/**
 * Created by tiankui on 3/10/17.
 */

public class ContentActivity extends BaseActivity implements View.OnClickListener {

    private FrameLayout contentLinearLayout;
    private AddImageButton addImageButton;
    private LinearLayout addPlanContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_content);
        nameTextView.setText(getString(R.string.app_name));
        contentLinearLayout = (FrameLayout) findViewById(R.id.ll_content_container);
        addImageButton = (AddImageButton) findViewById(R.id.aib_add_plan);
        addPlanContainer = (LinearLayout) findViewById(R.id.ll_add_plan_container);
        TextView everydayPlan = (TextView) findViewById(R.id.tv_add_every_day_plan);
        TextView thisWeekPlan = (TextView) findViewById(R.id.tv_add_this_week_plan);
        TextView longTimePlan = (TextView) findViewById(R.id.tv_add_this_long_time_plan);
        everydayPlan.setOnClickListener(this);
        thisWeekPlan.setOnClickListener(this);
        longTimePlan.setOnClickListener(this);
        addImageButton.setOnClickListener(this);
        addImageButton.setSelected(false);
    }

    /**
     * 测试设置title bar的接口是否有效；
     *
     * @param titleBar
     */
    @Override
    protected void onCreateTitleBar(ActionBar titleBar) {
        View view=titleBar.createTitleBar(R.layout.ac_content_title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add_every_day_plan:
                setAddImageButtonState(BaseConstant.DAY_TYPE);

                break;
            case R.id.tv_add_this_week_plan:
                setAddImageButtonState(BaseConstant.WEEK_TYPE);
                break;
            case R.id.tv_add_this_long_time_plan:
                setAddImageButtonState(BaseConstant.LONG_TERM_TYPE);
                break;
            case R.id.aib_add_plan:
                if (!addImageButton.isSelected()) {
                    addImageButton.animate().setDuration(300).rotation(45).start();
                    addPlanContainer.setVisibility(View.VISIBLE);
                    addImageButton.setSelected(true);
                } else {
                    addPlanContainer.setVisibility(View.GONE);
                    addImageButton.animate().setDuration(300).rotation(0).start();
                    addImageButton.setSelected(false);
                }
                break;
        }
    }

    private void setAddImageButtonState(int type) {
        addPlanContainer.setVisibility(View.GONE);
        addImageButton.animate().setDuration(300).rotation(0).start();
        addImageButton.setSelected(false);
        Intent intent = new Intent(this, AddPlanDetailActivity.class);
        intent.putExtra("planType", type);
        startActivity(intent);
    }

}
