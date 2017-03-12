package tinker.cn.timemanager.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import tinker.cn.timemanager.R;

/**
 * Created by tiankui on 2/13/17.
 */

public class BaseActivity extends FragmentActivity{

    private FrameLayout viewContainer;
    protected TextView nameTextView;
    private ViewGroup titleBar;
    private ImageView navigationImageView;
    private  ImageView searchImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.setContentView(R.layout.ac_base);
        viewContainer = (FrameLayout) findViewById(R.id.ac_fl_base_container);
        navigationImageView=(ImageView)findViewById(R.id.iv_navigate_back);
        nameTextView = (TextView) findViewById(R.id.tv_title);
        searchImageView=(ImageView)findViewById(R.id.iv_search_button);
        titleBar=(RelativeLayout)findViewById(R.id.rl_title_bar);
        navigationImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  BaseActivity.this.finish();
            }
        });

        onCreateTitleBar(new ActionBar());
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


    protected void onCreateTitleBar(ActionBar titleBar){}

    public class ActionBar{

        public void setBackgroundDrawable(Drawable drawable){
            titleBar.setBackground(drawable);
        }

        public View createTitleBar(int res){
            navigationImageView.setVisibility(View.GONE);
            nameTextView.setVisibility(View.GONE);
            searchImageView.setVisibility(View.GONE);
            return LayoutInflater.from(BaseActivity.this).inflate(res,titleBar,true);
        }

    }
}
