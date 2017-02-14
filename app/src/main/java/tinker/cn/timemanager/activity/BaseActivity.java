package tinker.cn.timemanager.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import tinker.cn.timemanager.R;

/**
 * Created by tiankui on 2/13/17.
 */

public class BaseActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.base_activity);
    }
}
