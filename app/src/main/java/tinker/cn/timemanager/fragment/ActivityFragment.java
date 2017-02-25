package tinker.cn.timemanager.fragment;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tinker.cn.timemanager.R;
import tinker.cn.timemanager.activity.ActivityDetailActivity;
import tinker.cn.timemanager.activity.MainActivity;
import tinker.cn.timemanager.model.ActivityInfo;
import tinker.cn.timemanager.model.RecordInfo;
import tinker.cn.timemanager.service.RecordService;
import tinker.cn.timemanager.utils.AppContext;
import tinker.cn.timemanager.utils.BaseConstant;
import tinker.cn.timemanager.utils.DaoManager;
import tinker.cn.timemanager.utils.FormatTime;
import tinker.cn.timemanager.widget.ActivityListView;
import tinker.cn.timemanager.widget.AddImageButton;

import static tinker.cn.timemanager.utils.FormatTime.calculateTimeString;

/**
 * Created by tiankui on 1/2/17.
 */

public class ActivityFragment extends Fragment {

    //TODO: 在退出mainActivity或者activityFragment的时候，如果是在计时的话需要在后台仍然进行着；
    private ActivityListView mListView;
    private List<ActivityInfo> mActivityList;
    private ActivityListViewAdapter mActivityListViewAdapter;
    private static Handler mHandler;


    private int createTag;
    private ActivityInfo mActivityInfo;

    private RecordService.RecordServiceBinder mServiceBinder;

    private BroadcastReceiver mPauseTimeReceiver;
    private BroadcastReceiver mStopTimeReceiver;
    private RecordServiceConnection mServiceConnection;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityList = new ArrayList<>();
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

        if(createTag==BaseConstant.CREATE_ACTIVITY_ONLY){
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
        getActivity().getApplicationContext().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);

        //TODO:读取数据库里边的数据，思路是应该判断下mActivityInfo 是否为空，如果是的，应该是最开始的Fragment，否则的话应该是点击群组之后的进入的；
        if (mActivityInfo == null) {
            Cursor cursor = DaoManager.getInstance().getActivityInfo(new String[]{""});//根据parentGroupId所属群组来判断，如果是单个活动或者群组，则为空""，否则为所属群组id
            List<ActivityInfo> activityInfoList = DaoManager.getInstance().parseCursor(cursor);
            if (activityInfoList.size() > 0) {
                for (ActivityInfo info : activityInfoList) {
                    if (info.getType() == BaseConstant.TYPE_ACTIVITY) {
                        mActivityList.add(0, info);
                    } else {
                        mActivityList.add(mActivityList.size(), info);
                    }
                }
            }
        } else {
            Cursor cursor = DaoManager.getInstance().getActivityInfo(new String[]{mActivityInfo.getId()});
            mActivityList = DaoManager.getInstance().parseCursor(cursor);
        }


        ((MainActivity) getActivity()).getActivityInfoMap().put(getTag(), mActivityList);

    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_activity_list, container, false);

        AddImageButton addImageView = (AddImageButton) view.findViewById(R.id.fr_iv_add_activity);
        mListView = (ActivityListView) view.findViewById(R.id.fr_lv_activity_list);
        mActivityListViewAdapter = new ActivityListViewAdapter();
        mListView.setAdapter(mActivityListViewAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                Object object = parent.getItemAtPosition(position);
                if (object instanceof ActivityInfo) {
                    final ActivityInfo info = (ActivityInfo) object;
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

    public void updateList(List<ActivityInfo> list) {
        mActivityList = list;
        mActivityListViewAdapter.notifyDataSetChanged();
    }

    private class ActivityListViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (mActivityList != null) {
                return mActivityList.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (mActivityList != null) {
                return mActivityList.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ActivityInfo info = (ActivityInfo) getItem(position);
            final RecordInfo recordInfo = info.getRecordInfo();
            convertView = recordInfo.getView();
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.item_activity_list_view, null);
                recordInfo.setView(convertView);
            }
            final ImageView activityIconImageView = (ImageView) convertView.findViewById(R.id.item_iv_activity_icon);
            TextView activityNameTextView = (TextView) convertView.findViewById(R.id.item_tv_activity_name);
            final LinearLayout timeDisplayLinearLayout = (LinearLayout) convertView.findViewById(R.id.item_ll_activity_time_display);
            final TextView timeDisplayTextView = (TextView) convertView.findViewById(R.id.item_tv_activity_time_display);
            final ImageView startImageView = (ImageView) convertView.findViewById(R.id.item_iv_activity_start);
            final ImageView stopImageView = (ImageView) convertView.findViewById(R.id.item_iv_activity_stop);
            activityNameTextView.setText(info.getName());

            if (recordInfo.getRunnable() == null) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        //TODO: 设置暂停的图片
                        startImageView.setImageResource(R.mipmap.record_pause);
                        recordInfo.setRecordState(BaseConstant.RECORDING_STATE);
                        timeDisplayTextView.setText(FormatTime.calculateTimeString(recordInfo.getTotalTime()));
                        recordInfo.setRunnable(this);
                        updateInfo(position, recordInfo);
                        mHandler.postDelayed(this, 1000);
                    }
                };
                recordInfo.setRunnable(runnable);
            }
            //ui显示问题
            if (info.getType() == BaseConstant.TYPE_ACTIVITY) {
                activityIconImageView.setImageResource(R.mipmap.activity_icon);
                if (recordInfo.getRecordState() == BaseConstant.READY_STATE || recordInfo.getRecordState() == BaseConstant.STOP_STATE) {
                    timeDisplayTextView.setVisibility(View.INVISIBLE);
                    stopImageView.setVisibility(View.INVISIBLE);
                }
                if (recordInfo.getRecordState() == BaseConstant.PAUSE_STATE) {
                    timeDisplayTextView.setText(calculateTimeString(recordInfo.getTotalTime()));
                    startImageView.setImageResource(R.mipmap.record_start);
                }
                if (recordInfo.getRecordState() == BaseConstant.RECORDING_STATE) {
                    timeDisplayTextView.setText(calculateTimeString(recordInfo.getTotalTime()));
                    mHandler.removeCallbacks(recordInfo.getRunnable());
                    mHandler.postDelayed(recordInfo.getRunnable(), 1000);

                    if (mServiceBinder != null) {
                        mServiceBinder.startRecorder(info);
                    }

                }
            } else {
                activityIconImageView.setImageResource(R.mipmap.group_icon);
                timeDisplayLinearLayout.setVisibility(View.INVISIBLE);
            }


            startImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recordInfo.getRecordState() == BaseConstant.READY_STATE) {
                        timeDisplayTextView.setVisibility(View.VISIBLE);
                        stopImageView.setVisibility(View.VISIBLE);
                        timeDisplayTextView.setText(calculateTimeString(recordInfo.getTotalTime()));
                        mHandler.postDelayed(recordInfo.getRunnable(), 1000);
                        //点击记录活动开始的时间
                        String[] conditionArgs = new String[]{info.getId()};
                        recordInfo.setBeginTime(System.currentTimeMillis());
                        DaoManager.getInstance().updateRecordInfo(recordInfo, BaseConstant.FIRST_UPDATE_RECORD_TIME_WHERE_CONDITION, conditionArgs);
                        if (mServiceBinder != null) {
                            mServiceBinder.startRecorder(info);
                        }
                    } else if (recordInfo.getRecordState() == BaseConstant.STOP_STATE) {
                        //开始记录活动时间，并且是一条新的记录
                        timeDisplayTextView.setVisibility(View.VISIBLE);
                        stopImageView.setVisibility(View.VISIBLE);
                        recordInfo.setTotalTime(0);
                        timeDisplayTextView.setText(calculateTimeString(recordInfo.getTotalTime()));
                        mHandler.postDelayed(recordInfo.getRunnable(), 1000);
                        info.setCreateTime(System.currentTimeMillis());
                        recordInfo.setBeginTime(System.currentTimeMillis());
                        if (mServiceBinder != null) {
                            mServiceBinder.resumeRecorder(info);
                        }
                        DaoManager.getInstance().addActivity(info);
                    } else if (recordInfo.getRecordState() == BaseConstant.RECORDING_STATE) {
                        //TODO:设置开始的图片
                        startImageView.setImageResource(R.mipmap.record_start);
                        mHandler.removeCallbacks(recordInfo.getRunnable());
                        recordInfo.setRecordState(BaseConstant.PAUSE_STATE);
                        updateInfo(position, recordInfo);
                        recordInfo.setEndTime(System.currentTimeMillis());
                        recordInfo.setDuration(recordInfo.getEndTime() - recordInfo.getBeginTime());
                        //暂停的时候也需要暂停通知中的时间显示；
                        if (mServiceBinder != null) {
                            mServiceBinder.pauseRecorder(info);
                        }
                        //之前的totalTime加上这次的持续时间；
//                                    recordInfo.setTotalTime(recordInfo.getTotalTime()+recordInfo.getDuration());
                        DaoManager.getInstance().updateRecordInfo(recordInfo, BaseConstant.UPDATE_RECORD_TIME_WHERE_CONDITION, new String[]{String.valueOf(recordInfo.getBeginTime())});

                    } else if (recordInfo.getRecordState() == BaseConstant.PAUSE_STATE) {
                        //这段代码的目的是第一次进入app的时候需要bind service
                        startImageView.setImageResource(R.mipmap.record_pause);
                        mHandler.postDelayed(recordInfo.getRunnable(), 1000);
                        recordInfo.setRecordState(BaseConstant.RECORDING_STATE);
                        updateInfo(position, recordInfo);
                        info.setCreateTime(System.currentTimeMillis());
                        recordInfo.setBeginTime(System.currentTimeMillis());
                        //再次开始计时
                        if (mServiceBinder != null) {
                            mServiceBinder.resumeRecorder(info);
                        }
                        DaoManager.getInstance().addActivity(info);
                    }
                }
            });

            stopImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stopImageView.setVisibility(View.INVISIBLE);
                    timeDisplayTextView.setVisibility(View.INVISIBLE);
                    startImageView.setImageResource(R.mipmap.record_start);
                    mHandler.removeCallbacks(recordInfo.getRunnable());
                    updateInfo(position, recordInfo);
                    if (recordInfo.getRecordState() == BaseConstant.RECORDING_STATE) {
                        recordInfo.setEndTime(System.currentTimeMillis());
                        recordInfo.setDuration(recordInfo.getEndTime() - recordInfo.getBeginTime());
                    }
                    recordInfo.setRecordState(BaseConstant.STOP_STATE);
                    if (mServiceBinder != null) {
                        mServiceBinder.stopRecorder(info);
                    }
                    DaoManager.getInstance().updateRecordInfo(recordInfo, BaseConstant.UPDATE_RECORD_TIME_WHERE_CONDITION, new String[]{String.valueOf(recordInfo.getBeginTime())});
                }
            });

            IntentFilter pauseIntentFilter = new IntentFilter(BaseConstant.NOTIFICATION_CLICK_PAUSE);
            mPauseTimeReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    ActivityInfo activityInfo = intent.getParcelableExtra("activityInfo");
                    if (activityInfo != null) {
                        if (activityInfo.getId().equals(info.getId())) {
                            //TODO:设置开始的图片
                            startImageView.setImageResource(R.mipmap.record_start);
                            mHandler.removeCallbacks(recordInfo.getRunnable());
                            recordInfo.setRecordState(BaseConstant.PAUSE_STATE);
                            updateInfo(position, recordInfo);

                            recordInfo.setEndTime(System.currentTimeMillis());
                            recordInfo.setDuration(recordInfo.getEndTime() - recordInfo.getBeginTime());
                            if (mServiceBinder != null) {
                                mServiceBinder.pauseRecorder(info);
                            }
                            //之前的totalTime加上这次的持续时间；
//                                    recordInfo.setTotalTime(recordInfo.getTotalTime()+recordInfo.getDuration());
                            DaoManager.getInstance().updateRecordInfo(recordInfo, BaseConstant.UPDATE_RECORD_TIME_WHERE_CONDITION, new String[]{String.valueOf(recordInfo.getBeginTime())});
                        }
                    }
                }
            };
            try {
                getActivity().registerReceiver(mPauseTimeReceiver, pauseIntentFilter);
            } catch (Exception e) {

            }


            IntentFilter stopIntentFilter = new IntentFilter(BaseConstant.NOTIFICATION_CLICK_STOP);
            mStopTimeReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    ActivityInfo activityInfo = intent.getParcelableExtra("activityInfo");
                    if (activityInfo != null) {
                        if (activityInfo.getId().equals(info.getId())) {
                            stopImageView.setVisibility(View.INVISIBLE);
                            timeDisplayTextView.setVisibility(View.INVISIBLE);
                            startImageView.setImageResource(R.mipmap.record_start);
                            updateInfo(position, recordInfo);
                            if (recordInfo.getRecordState() == BaseConstant.RECORDING_STATE) {
                                recordInfo.setEndTime(System.currentTimeMillis());
                                recordInfo.setDuration(recordInfo.getEndTime() - recordInfo.getBeginTime());
                            }
                            mHandler.removeCallbacks(recordInfo.getRunnable());
                            if (mServiceBinder != null) {
                                mServiceBinder.stopRecorder(info);
                            }
                            recordInfo.setRecordState(BaseConstant.STOP_STATE);
                            DaoManager.getInstance().updateRecordInfo(recordInfo, BaseConstant.UPDATE_RECORD_TIME_WHERE_CONDITION, new String[]{String.valueOf(recordInfo.getBeginTime())});
                        }
                    }
                }
            };
            try {
                getActivity().registerReceiver(mStopTimeReceiver, stopIntentFilter);
            } catch (Exception e) {

            }

            return convertView;
        }
    }

    /**
     * 用于ui上的刷新
     *
     * @param position
     * @param recordInfo
     */
    private void updateInfo(int position, RecordInfo recordInfo) {
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null && activity.getActivityInfo(getTag()) != null) {
            if (activity.getActivityInfo(getTag()).size() > position) {
                activity.getActivityInfo(getTag()).get(position).setRecordInfo(recordInfo);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPauseTimeReceiver != null) {
            getActivity().unregisterReceiver(mPauseTimeReceiver);
        }
        if (mStopTimeReceiver != null) {
            getActivity().unregisterReceiver(mStopTimeReceiver);
        }
        for (ActivityInfo info : mActivityList) {
            mHandler.removeCallbacks(info.getRecordInfo().getRunnable());
        }

        AppContext.getInstance().popFragment();
    }

    private class RecordServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mServiceBinder = (RecordService.RecordServiceBinder) service;
            List<ActivityInfo> tempList = new ArrayList<>();
            tempList.addAll(mActivityList);
            for (ActivityInfo info : tempList) {
                if (mServiceBinder != null) {
                    for (String id : mServiceBinder.getNotificationActivityID()) {
                        if (info.getId().equals(id)) {
                            ActivityInfo activityInfo = mServiceBinder.getActivityInfoById(id);
                            mServiceBinder.cancelNotification(activityInfo);
                            int index = tempList.indexOf(info);
                            mActivityList.remove(index);
                            mActivityList.add(index, activityInfo);
                            mActivityListViewAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
}
