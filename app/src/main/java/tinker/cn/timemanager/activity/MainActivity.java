package tinker.cn.timemanager.activity;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tinker.cn.timemanager.R;
import tinker.cn.timemanager.fragment.ActivityFragment;
import tinker.cn.timemanager.fragment.CreateActivityGroupDialogFragment;
import tinker.cn.timemanager.fragment.HistoryRecordsFragment;
import tinker.cn.timemanager.fragment.MeFragment;
import tinker.cn.timemanager.model.ActivityInfo;
import tinker.cn.timemanager.utils.BaseConstant;
import tinker.cn.timemanager.widget.FragmentViewPager;
import tinker.cn.timemanager.widget.TabIndicatorItemView;


public class MainActivity extends BaseActivity implements CreateActivityGroupDialogFragment.NoticeDialogListener, View.OnClickListener {

    private static final int ACTIVITY_ITEM = 0;
    private static final int RECORD_ITEM = 1;
    private static final int MINE_ITEM = 2;

    private FragmentViewPager mViewPager;
    private LinearLayout indicator;
    private TabIndicatorItemView activityTab;
    private TabIndicatorItemView recordTab;
    private TabIndicatorItemView meTab;

    private Map<String, List<ActivityInfo>> mActivityInfoMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
    }

    private void initData() {
        List<Fragment> fragmentList = new ArrayList<>();
        mActivityInfoMap = new HashMap<>();
        ActivityFragment fragment = new ActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("createTag", BaseConstant.CREATE_ACTIVITY_OR_GROUP);
        fragment.setArguments(bundle);
        fragmentList.add(fragment);
        fragmentList.add(new HistoryRecordsFragment());
        fragmentList.add(new MeFragment());
        MainFragmentPagerAdapter pagerAdapter = new MainFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(ACTIVITY_ITEM);
        indicator.getChildAt(ACTIVITY_ITEM).setSelected(true);

    }

    private void initView() {
        findViewById(R.id.iv_navigate_back).setVisibility(View.GONE);
        mViewPager = (FragmentViewPager) findViewById(R.id.ac_vp_view_pager);
        indicator = (LinearLayout) findViewById(R.id.ll_indicator);
        activityTab = (TabIndicatorItemView) findViewById(R.id.tab_activity);
        recordTab = (TabIndicatorItemView) findViewById(R.id.tab_record);
        meTab = (TabIndicatorItemView) findViewById(R.id.tab_me);
        activityTab.setOnClickListener(this);
        recordTab.setOnClickListener(this);
        meTab.setOnClickListener(this);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < indicator.getChildCount(); i++) {
                    View child = indicator.getChildAt(i);
                    child.setSelected(i == position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialogFragment, ActivityInfo info) {
//        if (info == null) {
//            return;
//        }
//        List<ActivityInfo> activityInfo = mActivityInfoMap.get(info.getFragmentTag());
//        if (activityInfo == null) {
//            activityInfo = new ArrayList<>();
//        }
//
//        if (info.getParentGroupId().equals("") && info.getType() == BaseConstant.TYPE_ACTIVITY) {
//            activityInfo.add(0, info);
//        } else {
//            activityInfo.add(info);
//        }
//        mActivityInfoMap.put(info.getFragmentTag(), activityInfo);
        ActivityFragment activityFragment = (ActivityFragment) getSupportFragmentManager().findFragmentByTag(info.getFragmentTag());
        if (activityFragment != null) {
            activityFragment.addActivity(info);
        }

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialogFragment) {

    }

    public List<ActivityInfo> getActivityInfo(String tag) {
        return mActivityInfoMap.get(tag);
    }


    private class MainFragmentPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList;

        public MainFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.fragmentList = list;
        }

        @Override
        public Fragment getItem(int position) {
            if (fragmentList != null) {
                return fragmentList.get(position);
            }
            return null;
        }

        @Override
        public int getCount() {
            if (fragmentList != null) {
                return fragmentList.size();
            }
            return 0;
        }
    }

    public Map<String, List<ActivityInfo>> getActivityInfoMap() {
        return mActivityInfoMap;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_activity:
                mViewPager.setCurrentItem(0);
                getSupportFragmentManager().popBackStack("groupDetailFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
            case R.id.tab_record:
                mViewPager.setCurrentItem(1);
//                AppContext.getInstance().popFragment();
                getSupportFragmentManager().popBackStack("groupDetailFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
            case R.id.tab_me:
                mViewPager.setCurrentItem(2);
                getSupportFragmentManager().popBackStack("groupDetailFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
        }
    }
}
