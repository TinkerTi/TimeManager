package tinker.cn.timemanager.ui.activity;

import android.os.Bundle;
import android.widget.LinearLayout;

import tinker.cn.timemanager.R;

/**
 * Created by tiankui on 3/10/17.
 */

public class ContentActivity extends BaseActivity{

    private LinearLayout contentLinearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_content);
        nameTextView.setText(getString(R.string.app_name));
        contentLinearLayout=(LinearLayout)findViewById(R.id.ll_content_container);

    }

    /**
     * 测试设置title bar的接口是否有效；
     * @param titleBar
     */
    @Override
    protected void onCreateTitleBar(ActionBar titleBar) {
//         View view=titleBar.createTitleBar(R.layout.test_title_bar);
//        titleBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorAccent)));
    }
}
