package tinker.cn.timemanager.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import tinker.cn.timemanager.R;
import tinker.cn.timemanager.activity.ActivityDetailActivity;
import tinker.cn.timemanager.model.ActivityInfo;
import tinker.cn.timemanager.service.RecordService;
import tinker.cn.timemanager.utils.AppContext;
import tinker.cn.timemanager.utils.BaseConstant;
import tinker.cn.timemanager.utils.DaoManager;
import tinker.cn.timemanager.widget.AddImageButton;
import tinker.cn.timemanager.widget.TimeRecordItemView;

/**
 * Created by tiankui on 1/2/17.
 *
 * 注意写这个的时候，bindService的回调是异步的，所以一定要注意时序的问题，不要认为，程序是按照代码的顺序一步一步执行的；
 */

public class ActivityFragment extends Fragment {

    private List<ActivityInfo> mActivityList;
    private List<TimeRecordItemView> viewList;
    private int mGroupCount;
    private LinearLayout activityItemLinearLayout;
    private static Handler mHandler;
    private int createTag;
    private ActivityInfo mActivityInfo;
    private RecordService.RecordServiceBinder mServiceBinder;
    private RecordServiceConnection mServiceConnection;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityList = new ArrayList<>();
        viewList=new ArrayList<>();
        mGroupCount=0;
        mHandler = new Handler(Looper.getMainLooper());

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("createTag")) {
                createTag = bundle.getInt("createTag");
            }
            if (bundle.containsKey("activityInfo")) {
                mActivityInfo = bundle.getParcelable("activityInfo");
            }
        }

        if (createTag == BaseConstant.CREATE_ACTIVITY_ONLY) {
            getActivity().findViewById(R.id.iv_navigate_back).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.iv_navigate_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().popBackStack("groupDetailFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    getActivity().findViewById(R.id.iv_navigate_back).setVisibility(View.GONE);
                }
            });
        } else {
            getActivity().findViewById(R.id.iv_navigate_back).setVisibility(View.GONE);
        }


        Intent intent = new Intent(getActivity(), RecordService.class);
        getActivity().startService(intent);
        mServiceConnection = new RecordServiceConnection();
        getActivity().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);

        //读取数据库里边的数据，思路是应该判断下mActivityInfo 是否为空，如果是的，应该是最开始的Fragment，否则的话应该是点击群组之后的进入的；
        if (mActivityInfo == null) {
            Cursor cursor = DaoManager.getInstance().getActivityInfo(new String[]{""});//根据parentGroupId所属群组来判断，如果是单个活动或者群组，则为空""，否则为所属群组id
            List<ActivityInfo> activityInfoList = DaoManager.getInstance().parseCursor(cursor);
            if (activityInfoList.size() > 0) {
                for (ActivityInfo info : activityInfoList) {
                    if (info.getType() == BaseConstant.TYPE_ACTIVITY) {
                        mActivityList.add(0, info);
                    } else {
                        mActivityList.add(mActivityList.size(), info);
                        mGroupCount++;
                    }
                }
            }
        } else {
            Cursor cursor = DaoManager.getInstance().getActivityInfo(new String[]{mActivityInfo.getId()});
            mActivityList = DaoManager.getInstance().parseCursor(cursor);
        }
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_activity_list, container, false);
        AddImageButton addImageView = (AddImageButton) view.findViewById(R.id.fr_iv_add_activity);
        activityItemLinearLayout = (LinearLayout) view.findViewById(R.id.ll_activity_item_container);
        for (ActivityInfo info : mActivityList) {
            initActivity(info);
        }
        addImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (createTag == BaseConstant.CREATE_ACTIVITY_OR_GROUP) {
                    BottomCreateDialogFragment dialogFragment = new BottomCreateDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("fragmentTag", getTag());
                    dialogFragment.setArguments(bundle);
                    dialogFragment.show(getFragmentManager(), null);
                } else if (createTag == BaseConstant.CREATE_ACTIVITY_ONLY) {
                    CreateActivityDialogFragment dialogFragment = new CreateActivityDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("fragmentTag", getTag()); //标识当前Fragment对象，为了后续刷新活动时，找到这个Fragment对象；
                    bundle.putParcelable("activityInfo", mActivityInfo);
                    dialogFragment.setArguments(bundle);
                    dialogFragment.show(getFragmentManager(), null);
                }

            }
        });
        return view;
    }

    private void initActivity(ActivityInfo info){
        TimeRecordItemView itemView = new TimeRecordItemView(getActivity());
        itemView.setInfo(info, mHandler,mServiceBinder);
        viewList.add(itemView);
        if (info.getType() == BaseConstant.TYPE_ACTIVITY) {
            activityItemLinearLayout.addView(itemView, 0);
        } else {
            activityItemLinearLayout.addView(itemView);
        }
        setListener(itemView);
    }
    public void addActivity(ActivityInfo info) {
        TimeRecordItemView itemView = new TimeRecordItemView(getActivity());
        itemView.setInfo(info, mHandler,mServiceBinder);
        if (info.getType() == BaseConstant.TYPE_ACTIVITY) {
            activityItemLinearLayout.addView(itemView, 0);
        } else {
            activityItemLinearLayout.addView(itemView,activityItemLinearLayout.getChildCount()-mGroupCount++);
        }
        setListener(itemView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        for (ActivityInfo info : mActivityList) {
            mHandler.removeCallbacks(info.getRecordInfo().getRunnable());
        }
        AppContext.getInstance().popFragment();
    }

    private class RecordServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mServiceBinder = (RecordService.RecordServiceBinder) service;

            for (TimeRecordItemView view:viewList){
                view.setServiceBinder(mServiceBinder);
            }
            List<ActivityInfo> tempList=new ArrayList<>();
            tempList.addAll(mActivityList);
            for (ActivityInfo info : tempList) {
                if (mServiceBinder != null) {
                    for (String id : mServiceBinder.getNotificationActivityID()) {
                        if (info.getId().equals(id)) {
                            ActivityInfo activityInfo = mServiceBinder.getActivityInfoById(id);
                            mServiceBinder.cancelNotification(activityInfo);
                            int index = tempList.indexOf(info);
                            mActivityList.remove(index);
                            mActivityList.add(index,activityInfo);
                            TimeRecordItemView timeRecordItemView = new TimeRecordItemView(getActivity());
                            timeRecordItemView.setInfo(activityInfo, mHandler,mServiceBinder);
                            setListener(timeRecordItemView);
                            activityItemLinearLayout.removeViewAt(index);
                            activityItemLinearLayout.addView(timeRecordItemView, index);
                        }
                    }
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    private void setListener(TimeRecordItemView itemView) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof TimeRecordItemView) {
                    ActivityInfo info = ((TimeRecordItemView) v).getActivityInfo();
                    if (info.getType() == BaseConstant.TYPE_ACTIVITY) {
                        //TODO:点击进入对应activity的详情页面
                        Intent intent = new Intent(getActivity(), ActivityDetailActivity.class);
                        startActivity(intent);
                    } else {
                        ActivityFragment activityFragment = new ActivityFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("createTag", BaseConstant.CREATE_ACTIVITY_ONLY);
                        bundle.putParcelable("activityInfo", info);
                        activityFragment.setArguments(bundle);
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .addToBackStack("groupDetailFragment")
                                .replace(R.id.ac_fl_view_pager_container, activityFragment, info.getName())
                                .commit();
                    }
                }
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
    }



}
