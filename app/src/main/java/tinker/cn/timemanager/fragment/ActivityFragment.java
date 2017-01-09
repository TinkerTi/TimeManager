package tinker.cn.timemanager.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tinker.cn.timemanager.R;
import tinker.cn.timemanager.activity.MainActivity;
import tinker.cn.timemanager.model.ActivityInfo;
import tinker.cn.timemanager.model.RecordInfo;
import tinker.cn.timemanager.utils.BaseConstant;
import tinker.cn.timemanager.utils.DaoManager;

/**
 * Created by tiankui on 1/2/17.
 */

public class ActivityFragment extends Fragment {

    //TODO: 在退出mainActivity或者activityFragment的时候，如果是在计时的话需要在后台仍然进行着；
    private ListView mListView;
    private List<ActivityInfo> mActivityList;
    private ActivityListViewAdapter mActivityListViewAdapter;
    private static Handler mHandler;


    private int createTag;
    private ActivityInfo mActivityInfo;

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

        //TODO:读取数据库里边的数据，思路是应该判断下mActivityInfo 是否为空，如果是的，应该是最开始的Fragment，否则的话应该是点击群组之后的进入的；
        if (mActivityInfo == null) {
//            Cursor cursor = DaoManager.getInstance().getActivityInfo(null, BaseConstant.PARENT_GROUP_SELECTION, new String[]{""}, BaseConstant.ORDER_BY_CREATE_TIME,BaseConstant.Activities.COLUMN_ID);
            Cursor cursor=DaoManager.getInstance().getActivityInfo(new String[]{""});
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
            Cursor cursor = DaoManager.getInstance().getActivityInfo(null, BaseConstant.PARENT_GROUP_SELECTION, new String[]{mActivityInfo.getId()}, BaseConstant.ORDER_BY_CREATE_TIME,BaseConstant.Activities.COLUMN_ID);
            mActivityList = DaoManager.getInstance().parseCursor(cursor);
        }

        ((MainActivity)getActivity()).getActivityInfoMap().put(getTag(),mActivityList);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_activity_list, container, false);

        ImageView imageView = (ImageView) view.findViewById(R.id.fr_iv_add_activity);
        mListView = (ListView) view.findViewById(R.id.fr_lv_activity_list);
        mActivityListViewAdapter = new ActivityListViewAdapter();
        mListView.setAdapter(mActivityListViewAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                Object object = parent.getItemAtPosition(position);
                if (object instanceof ActivityInfo) {
                    final ActivityInfo info = (ActivityInfo) object;
                    final LinearLayout timeDisplayLinearLayout = (LinearLayout) view.findViewById(R.id.item_ll_activity_time_display);
                    final TextView timeDisplayTextView = (TextView) view.findViewById(R.id.item_tv_activity_time_display);
                    final ImageView startImageView = (ImageView) view.findViewById(R.id.item_iv_activity_start);
                    ImageView stopImageView = (ImageView) view.findViewById(R.id.item_iv_activity_stop);

                    if (info.getType() == BaseConstant.TYPE_ACTIVITY) {
                        final RecordInfo recordInfo;
                        final Runnable runnable;
                        if (view.getTag() == null) {
                            recordInfo =info.getRecordInfo();
                            recordInfo.setView(view);
                            runnable = new Runnable() {
                                @Override
                                public void run() {
                                    //TODO: 设置暂停的图片
                                    startImageView.setImageResource(R.mipmap.record_pause);
                                    recordInfo.setRecordState(BaseConstant.RECORDING_STATE);

                                    recordInfo.setTotalTime(recordInfo.getTotalTime()+1000);
                                    timeDisplayTextView.setText(calculateTimeString(recordInfo.getTotalTime()));
                                    recordInfo.setRunnable(this);
                                    updateInfo(position, recordInfo);
                                    mHandler.postDelayed(this, 1000);
                                }
                            };
                            view.setTag(recordInfo);
                        } else {
                            recordInfo = (RecordInfo) view.getTag();
                            runnable = recordInfo.getRunnable();
                        }

                        timeDisplayLinearLayout.setVisibility(View.VISIBLE);
                        timeDisplayTextView.setText(calculateTimeString(recordInfo.getTotalTime()));

                        if (recordInfo.getRecordState() == BaseConstant.READY_STATE || recordInfo.getRecordState() == BaseConstant.STOP_STATE) {
                            mHandler.postDelayed(runnable, 1000);
                            //点击记录活动开始的时间
                            String[] conditionArgs;
                            if (recordInfo.getBeginTime() == 0) {
                                conditionArgs = new String[]{String.valueOf(0)};
                                recordInfo.setBeginTime(System.currentTimeMillis());
                            } else {
                                conditionArgs = new String[]{String.valueOf(recordInfo.getBeginTime())};
                            }
                            DaoManager.getInstance().updateRecordInfo(recordInfo, BaseConstant.UPDATE_RECORD_TIME_WHERE_CONDITION, conditionArgs);
                        }

                        startImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (recordInfo.getRecordState() == BaseConstant.RECORDING_STATE) {
                                    //TODO:设置开始的图片
                                    startImageView.setImageResource(R.mipmap.record_start);
                                    mHandler.removeCallbacks(runnable);
                                    recordInfo.setRecordState(BaseConstant.PAUSE_STATE);
                                    updateInfo(position, recordInfo);

                                    recordInfo.setEndTime(System.currentTimeMillis());
                                    recordInfo.setDuration(recordInfo.getEndTime()-recordInfo.getBeginTime());
                                    //之前的totalTime加上这次的持续时间；
//                                    recordInfo.setTotalTime(recordInfo.getTotalTime()+recordInfo.getDuration());
                                    DaoManager.getInstance().updateRecordInfo(recordInfo, BaseConstant.UPDATE_RECORD_TIME_WHERE_CONDITION, new String[]{String.valueOf(recordInfo.getBeginTime())});

                                } else if (recordInfo.getRecordState() == BaseConstant.PAUSE_STATE) {
                                    //TODO:这个地方的逻辑需要理清楚一下；
                                    //TODO:如果是暂停了又开始，那么就需要重新计算持续时间（数据库里边的数据），但是显示的却是累积 的时间；
                                    startImageView.setImageResource(R.mipmap.record_pause);
                                    mHandler.postDelayed(runnable, 1000);
                                    recordInfo.setRecordState(BaseConstant.RECORDING_STATE);
                                    updateInfo(position, recordInfo);

                                    info.setCreateTime(System.currentTimeMillis());
                                    recordInfo.setBeginTime(System.currentTimeMillis());
                                    DaoManager.getInstance().addActivity(info);
                                }
                            }
                        });

                        stopImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mHandler.removeCallbacks(runnable);
                                startImageView.setImageResource(R.mipmap.record_pause);
                                timeDisplayLinearLayout.setVisibility(View.GONE);
                                recordInfo.setRecordState(BaseConstant.STOP_STATE);
                                updateInfo(position, recordInfo);
                                recordInfo.setEndTime(System.currentTimeMillis());
                                recordInfo.setDuration(recordInfo.getEndTime()-recordInfo.getBeginTime());

//                                recordInfo.setTotalTime(recordInfo.getTotalTime()+recordInfo.getDuration());
                                DaoManager.getInstance().updateRecordInfo(recordInfo, BaseConstant.UPDATE_RECORD_TIME_WHERE_CONDITION, new String[]{String.valueOf(recordInfo.getBeginTime())});
                            }
                        });
                    } else {
                        ActivityFragment activityFragment = new ActivityFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("createTag", BaseConstant.CREATE_ACTIVITY_ONLY);
                        bundle.putParcelable("activityInfo", info);
                        activityFragment.setArguments(bundle);
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .addToBackStack(null)
                                .replace(R.id.ac_fl_view_pager_container, activityFragment, info.getName())
                                .commit();
                    }
                }
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
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
        public View getView(int position, View convertView, ViewGroup parent) {
            ActivityInfo info = (ActivityInfo) getItem(position);
            RecordInfo recordInfo = info.getRecordInfo();
            View view=null;
            if (recordInfo != null) {
                view = recordInfo.getView();
            }
            if (view == null) {
                view = View.inflate(getActivity(), R.layout.item_activity_list_view, null);
            }
            ImageView activityIconImageView = (ImageView) view.findViewById(R.id.item_iv_activity_icon);
            TextView activityNameTextView = (TextView) view.findViewById(R.id.item_tv_activity_name);
            final LinearLayout timeDisplayLinearLayout = (LinearLayout) view.findViewById(R.id.item_ll_activity_time_display);
            final TextView timeDisplayTextView = (TextView) view.findViewById(R.id.item_tv_activity_time_display);
            final ImageView startImageView = (ImageView) view.findViewById(R.id.item_iv_activity_start);
            ImageView stopImageView = (ImageView) view.findViewById(R.id.item_iv_activity_stop);
            activityNameTextView.setText(info.getName());
            //TODO:这个地方是有问题的，什么情况下该显示时间记录的情况，是由条件限制的；第一次创建群组或者活动的时候，recordInfo是空的；
            //TODO:所以不会显示，其他时候暂时还没有考虑；
            if (info.getType() == BaseConstant.TYPE_ACTIVITY) {
                activityIconImageView.setImageResource(R.mipmap.activity_icon);
                if (recordInfo != null) {
                    if (recordInfo.getRecordState() == BaseConstant.RECORDING_STATE ||
                            recordInfo.getRecordState() == BaseConstant.PAUSE_STATE) {
                        timeDisplayLinearLayout.setVisibility(View.VISIBLE);
                    }
                }
                if (recordInfo != null) {
                    timeDisplayTextView.setText(calculateTimeString(recordInfo.getTotalTime()));
                    if (recordInfo.getRecordState() == BaseConstant.RECORDING_STATE) {
                        mHandler.removeCallbacks(recordInfo.getRunnable());
                        mHandler.postDelayed(recordInfo.getRunnable(), 1000);
                    }
                }
            }else {
                activityIconImageView.setImageResource(R.mipmap.group_icon);
            }

            return view;
        }
    }

    private String calculateTimeString(long timeMillis) {
        long timeSeconds=timeMillis/1000;
        StringBuilder builder = new StringBuilder();
        if (timeSeconds < 60) {
            builder.append("00:00:")
                    .append(formatTime(timeSeconds));
        } else if (timeSeconds < 60 * 60) {
            long minutes = timeSeconds / 60;
            long seconds = timeSeconds % 60;
            builder.append("00:")
                    .append(formatTime(minutes))
                    .append(":")
                    .append(formatTime(seconds));
        } else if (timeSeconds < 99 * 3600) {
            long hours = timeSeconds / 3600;
            long rest = timeSeconds % 3600;
            long minutes = rest / 60;
            long seconds = rest % 60;
            builder.append(formatTime(hours))
                    .append(":")
                    .append(formatTime(minutes))
                    .append(":")
                    .append(formatTime(seconds));
        } else {
            long days = timeSeconds / (24 * 3600);
            long restTime = timeSeconds % (24 * 3600);
            long hours = restTime / 3600;
            long rest = restTime % 3600;
            long minutes = rest / 60;
            long seconds = rest % 60;
            builder.append(formatTime(days))
                    .append("天")
                    .append(formatTime(hours))
                    .append(":")
                    .append(formatTime(minutes))
                    .append(":")
                    .append(formatTime(seconds));
        }
        return builder.toString();
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

    private String formatTime(long time) {
        StringBuilder builder = new StringBuilder();
        if (time < 10) {
            builder.append("0")
                    .append(String.valueOf(time));
        } else {
            builder.append(String.valueOf(time));
        }
        return builder.toString();
    }



}
