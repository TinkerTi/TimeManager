package tinker.cn.timemanager.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import tinker.cn.timemanager.R;

/**
 * Created by tiankui on 2/13/17.
 */

public class BaseActivity extends FragmentActivity implements View.OnClickListener {

    private FrameLayout viewContainer;
    protected TextView nameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.setContentView(R.layout.base_activity);
        viewContainer = (FrameLayout) findViewById(R.id.ac_fl_base_container);
        findViewById(R.id.iv_navigate_back).setOnClickListener(this);
        nameTextView = (TextView) findViewById(R.id.tv_title);
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = View.inflate(getBaseContext(), layoutResID, null);
        setContentView(view);
    }

    @Override
    public void setContentView(View view) {
        viewContainer.addView(view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_navigate_back:
                finish();
                break;

        }
    }
}
