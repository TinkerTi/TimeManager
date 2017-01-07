package tinker.cn.timemanager.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
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

/**
 * Created by tiankui on 1/2/17.
 */

public class ActivityFragment extends Fragment implements CreateActivityGroupDialogFragment.NoticeDialogListener {

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

        Bundle bundle=getArguments();
        if(bundle!=null){
            if(bundle.containsKey("createTag")){
                createTag=bundle.getInt("createTag");
            }
            if(bundle.containsKey("activityInfo")){
                mActivityInfo=bundle.getParcelable("activityInfo");
            }
        }

        //TODO:读取数据库里边的数据，思路是应该判断下mActivityInfo 是否为空，如果是的，应该是最开始的Fragment，否则的话应该是点击群组之后的进入的；

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
                    ActivityInfo info = (ActivityInfo) object;
                    final LinearLayout timeDisplayLinearLayout = (LinearLayout) view.findViewById(R.id.item_ll_activity_time_display);
                    final TextView timeDisplayTextView = (TextView) view.findViewById(R.id.item_tv_activity_time_display);
                    final ImageView startImageView = (ImageView) view.findViewById(R.id.item_iv_activity_start);
                    ImageView stopImageView = (ImageView) view.findViewById(R.id.item_iv_activity_stop);

                    if (info.getType() == BaseConstant.CREATE_TYPE_ACTIVITY) {
                        final RecordInfo recordInfo;
                        final Runnable runnable;
                        if (view.getTag() == null) {
                            recordInfo = new RecordInfo();
                            recordInfo.setView(view);
                            runnable = new Runnable() {
                                @Override
                                public void run() {
                                    //TODO: 设置暂停的图片
                                    startImageView.setImageResource(R.mipmap.record_pause);
                                    recordInfo.setRecordState(RecordInfo.RECORDING_STATE);
                                    recordInfo.setRecordTime(recordInfo.getRecordTime() + 1);
                                    timeDisplayTextView.setText(calculateTimeString(recordInfo.getRecordTime()));
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
                        timeDisplayTextView.setText(calculateTimeString(recordInfo.getRecordTime()));


                        if (recordInfo.getRecordState() == RecordInfo.READY_STATE || recordInfo.getRecordState() == RecordInfo.STOP_STATE) {
                            mHandler.postDelayed(runnable, 1000);
                        }

                        startImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (recordInfo.getRecordState() == RecordInfo.RECORDING_STATE) {
                                    //TODO:设置开始的图片
                                    startImageView.setImageResource(R.mipmap.record_start);
                                    mHandler.removeCallbacks(runnable);
                                    recordInfo.setRecordState(RecordInfo.PAUSE_STATE);
                                    updateInfo(position, recordInfo);

                                } else if (recordInfo.getRecordState() == RecordInfo.PAUSE_STATE) {
                                    //TODO:设置暂停的图片
                                    startImageView.setImageResource(R.mipmap.record_pause);
                                    mHandler.postDelayed(runnable, 1000);
                                    recordInfo.setRecordState(RecordInfo.RECORDING_STATE);
                                    updateInfo(position, recordInfo);

                                }
                            }
                        });

                        stopImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mHandler.removeCallbacks(runnable);
                                startImageView.setImageResource(R.mipmap.record_pause);
                                timeDisplayLinearLayout.setVisibility(View.GONE);
                                recordInfo.setRecordState(RecordInfo.STOP_STATE);
                                updateInfo(position, recordInfo);
                            }
                        });
                    } else {
                        ActivityFragment activityFragment = new ActivityFragment();
                        Bundle bundle=new Bundle();
                        bundle.putInt("createTag", BaseConstant.CREATE_ACTIVITY_ONLY);
                        bundle.putParcelable("activityInfo",info);
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
                if(createTag== BaseConstant.CREATE_ACTIVITY_OR_GROUP){
                    BottomCreateDialogFragment dialogFragment = new BottomCreateDialogFragment();
                    Bundle bundle=new Bundle();
                    bundle.putString("fragmentTag",getTag());
                    dialogFragment.setArguments(bundle);
                    dialogFragment.show(getFragmentManager(), null);
                }else if(createTag== BaseConstant.CREATE_ACTIVITY_ONLY){
                    CreateActivityDialogFragment dialogFragment=new CreateActivityDialogFragment();
                    Bundle bundle=new Bundle();
                    bundle.putString("fragmentTag",getTag()); //标识当前Fragment对象，为了后续刷新活动时，找到这个Fragment对象；
                    bundle.putParcelable("activityInfo",mActivityInfo);
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

    @Override
    public void onDialogPositiveClick(DialogFragment dialogFragment, ActivityInfo info) {
        mActivityList.add(info);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialogFragment) {

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
            if (recordInfo != null) {
                convertView = recordInfo.getView();
            }
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.item_activity_list_view, null);
            }
            ImageView activityIconImageView = (ImageView) convertView.findViewById(R.id.item_iv_activity_icon);
            TextView activityNameTextView = (TextView) convertView.findViewById(R.id.item_tv_activity_name);
            final LinearLayout timeDisplayLinearLayout = (LinearLayout) convertView.findViewById(R.id.item_ll_activity_time_display);
            final TextView timeDisplayTextView = (TextView) convertView.findViewById(R.id.item_tv_activity_time_display);
            final ImageView startImageView = (ImageView) convertView.findViewById(R.id.item_iv_activity_start);
            ImageView stopImageView = (ImageView) convertView.findViewById(R.id.item_iv_activity_stop);
            //TODO:根据是群组还是活动来决定使用对应的图标

            activityNameTextView.setText(info.getName());
            //TODO:这个地方是有问题的，什么情况下该显示时间记录的情况，是由条件限制的；第一次创建群组或者活动的时候，recordInfo是空的；
            //TODO:所以不会显示，其他时候暂时还没有考虑；
            if (recordInfo != null) {
                timeDisplayLinearLayout.setVisibility(View.VISIBLE);
                timeDisplayTextView.setText(calculateTimeString(recordInfo.getRecordTime()));
                if (recordInfo.getRecordState() == RecordInfo.RECORDING_STATE) {
                    mHandler.removeCallbacks(recordInfo.getRunnable());
                    mHandler.postDelayed(recordInfo.getRunnable(), 1000);
                }
            }
            return convertView;
        }
    }

    private String calculateTimeString(long time) {
        StringBuilder builder = new StringBuilder();
        if (time < 60) {
            builder.append("00:00:")
                    .append(String.valueOf(time));
        } else if (time < 60 * 60) {
            long minutes = time / 60;
            long seconds = time % 60;
            builder.append("00:")
                    .append(String.valueOf(minutes))
                    .append(":")
                    .append(String.valueOf(seconds));
        } else if (time < 99 * 3600) {
            long hours = time / 3600;
            long rest = time % 3600;
            long minutes = rest / 60;
            long seconds = rest % 60;
            builder.append(String.valueOf(hours))
                    .append(":")
                    .append(String.valueOf(minutes))
                    .append(":")
                    .append(String.valueOf(seconds));
        } else {
            long days = time / (24 * 3600);
            long restTime = time % (24 * 3600);
            long hours = restTime / 3600;
            long rest = restTime % 3600;
            long minutes = rest / 60;
            long seconds = rest % 60;
            builder.append(String.valueOf(days))
                    .append("天")
                    .append(String.valueOf(hours))
                    .append(":")
                    .append(String.valueOf(minutes))
                    .append(":")
                    .append(String.valueOf(seconds));
        }
        return builder.toString();
    }

    private void updateInfo(int position, RecordInfo recordInfo) {
        MainActivity activity = (MainActivity) getActivity();
        if(activity!=null&&activity.getActivityInfo(getTag())!=null){
            if(activity.getActivityInfo(getTag()).size()>position){
                activity.getActivityInfo(getTag()).get(position).setRecordInfo(recordInfo);
            }
        }
    }


}
