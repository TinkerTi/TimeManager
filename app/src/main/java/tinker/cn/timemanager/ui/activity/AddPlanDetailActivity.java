package tinker.cn.timemanager.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.TextView;

import tinker.cn.timemanager.R;
import tinker.cn.timemanager.model.BaseConstant;
import tinker.cn.timemanager.ui.addPlan.AddPlanFragment;

/**
 * Created by tiankui on 3/12/17.
 */

public class AddPlanDetailActivity extends BaseActivity {

    private int planType;
    TextView cancelTextView;
    TextView okTextView;
    TextView planTitleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_add_plan_detail);
        AddPlanFragment addPlanFragment=new AddPlanFragment();
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fl_add_plan_detail_container,addPlanFragment).commit();
        Intent intent = getIntent();
        planType = intent.getIntExtra("planType", -1);
        switch (planType) {
            case BaseConstant.DAY_TYPE:
                planTitleTextView.setText(getString(R.string.add_plan_every_day));
                break;
            case BaseConstant.WEEK_TYPE:
                planTitleTextView.setText(getString(R.string.add_plan_this_week));
                break;
            case BaseConstant.LONG_TERM_TYPE:
                planTitleTextView.setText(getString(R.string.add_plan_long_time));
                break;
        }
    }


    @Override
    protected void onCreateTitleBar(ActionBar titleBar) {
        View view = titleBar.createTitleBar(R.layout.ac_add_plan_title);
        cancelTextView = (TextView) view.findViewById(R.id.tv_cancel_add_plan);
        okTextView = (TextView) view.findViewById(R.id.tv_ok_add_plan);
        planTitleTextView = (TextView) view.findViewById(R.id.tv_add_plan_title);

        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 AddPlanDetailActivity.this.finish();
            }
        });
        okTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
