package tinker.cn.timemanager.activity;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

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


public class MainActivity extends FragmentActivity implements CreateActivityGroupDialogFragment.NoticeDialogListener {

    private ViewPager mViewPager;
    private Map<String,List<ActivityInfo>> mActivityInfoMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
    }

    private void initData() {
        List<Fragment> fragmentList = new ArrayList<>();
        mActivityInfoMap=new HashMap<>();
        ActivityFragment fragment=new ActivityFragment();
        Bundle bundle=new Bundle();
        bundle.putInt("createTag", BaseConstant.CREATE_ACTIVITY_OR_GROUP);
        fragment.setArguments(bundle);
        fragmentList.add(fragment);
        fragmentList.add(new HistoryRecordsFragment());
        fragmentList.add(new MeFragment());
        MainFragmentPagerAdapter pagerAdapter = new MainFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setAdapter(pagerAdapter);
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.ac_vp_view_pager);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialogFragment, ActivityInfo info) {
        List<ActivityInfo> activityInfo=mActivityInfoMap.get(info.getFragmentTag());
        if(activityInfo==null){
            activityInfo=new ArrayList<>();
        }
        if(info!=null){
            if(info.getParentGroupId().equals("")&&info.getType().equals("activity")){
                activityInfo.add(0,info);
            }else {
                activityInfo.add(info);
            }
            mActivityInfoMap.put(info.getFragmentTag(),activityInfo);
        }
        ActivityFragment activityFragment=(ActivityFragment) getSupportFragmentManager().findFragmentByTag(info.getFragmentTag());
        if(activityFragment!=null){
            activityFragment.updateList(activityInfo);
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialogFragment) {

    }

    public List<ActivityInfo> getActivityInfo(String tag){
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

}
