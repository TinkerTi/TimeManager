package tinker.cn.timemanager.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import tinker.cn.timemanager.R;
import tinker.cn.timemanager.model.ActivityInfo;
import tinker.cn.timemanager.model.RecordInfo;
import tinker.cn.timemanager.service.RecordService;
import tinker.cn.timemanager.model.BaseConstant;
import tinker.cn.timemanager.db.DaoManager;

import static tinker.cn.timemanager.utils.FormatTime.calculateTimeString;

/**
 * Created by tiankui on 2/25/17.
 */

public class TimeRecordItemView extends LinearLayout {

    private ActivityInfo activityInfo;
    private RecordService.RecordServiceBinder mServiceBinder;
    private Handler mHandler;
    private BroadcastReceiver mPauseTimeReceiver;
    private BroadcastReceiver mStopTimeReceiver;

    private ImageView activityIconImageView;
    private TextView activityNameTextView;
    LinearLayout timeDisplayLinearLayout;
    TextView timeDisplayTextView;
    ImageView startImageView;
    ImageView stopImageView;
    RecordInfo recordInfo;

    public TimeRecordItemView(Context context) {
        super(context);
        init(context, null);

    }

    public TimeRecordItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.item_activity_list_view, this, true);
        activityIconImageView = (ImageView) findViewById(R.id.item_iv_activity_icon);
        activityNameTextView = (TextView) findViewById(R.id.item_tv_activity_name);
        timeDisplayLinearLayout = (LinearLayout) findViewById(R.id.item_ll_activity_time_display);
        timeDisplayTextView = (TextView) findViewById(R.id.item_tv_activity_time_display);
        startImageView = (ImageView) findViewById(R.id.item_iv_activity_start);
        stopImageView = (ImageView) findViewById(R.id.item_iv_activity_stop);


    }

    public void setInfo(final ActivityInfo info, Handler handler, RecordService.RecordServiceBinder binder) {
        this.activityInfo = info;
        this.mHandler = handler;
        this.mServiceBinder=binder;

        activityNameTextView.setText(info.getName());
        recordInfo = info.getRecordInfo();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                startImageView.setImageResource(R.mipmap.record_pause);
                timeDisplayTextView.setVisibility(View.VISIBLE);
                stopImageView.setVisibility(View.VISIBLE);
                recordInfo.setRecordState(BaseConstant.RECORDING_STATE);
                timeDisplayTextView.setText(calculateTimeString(recordInfo.getTotalTime()));
                recordInfo.setRunnable(this);
                mHandler.postDelayed(this, 1000);
            }
        };
        recordInfo.setRunnable(runnable);
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
                startImageView.setImageResource(R.mipmap.record_pause);
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
//                    recordInfo.setDuration(0);
//                    recordInfo.setEndTime(0);
//                    recordInfo.setRecordState(BaseConstant.RECORDING_STATE);
                    if (mServiceBinder != null) {
                        mServiceBinder.resumeRecorder(info);
                    }
                    DaoManager.getInstance().addActivity(info);
                } else if (recordInfo.getRecordState() == BaseConstant.RECORDING_STATE) {
                    //TODO:设置开始的图片
                    startImageView.setImageResource(R.mipmap.record_start);
                    mHandler.removeCallbacks(recordInfo.getRunnable());
                    startImageView.setImageResource(R.mipmap.record_start);
                    recordInfo.setRecordState(BaseConstant.PAUSE_STATE);
//                    updateInfo(position, recordInfo);
                    recordInfo.setEndTime(System.currentTimeMillis());
                    recordInfo.setDuration(recordInfo.getEndTime() - recordInfo.getBeginTime());
                    //暂停的时候也需要暂停通知中的时间显示；
                    if (mServiceBinder != null) {
                        mServiceBinder.pauseRecorder(info);
                    }
                    //之前的totalTime加上这次的持续时间；
                    DaoManager.getInstance().updateRecordInfo(recordInfo, BaseConstant.UPDATE_RECORD_TIME_WHERE_CONDITION, new String[]{String.valueOf(recordInfo.getBeginTime())});

                } else if (recordInfo.getRecordState() == BaseConstant.PAUSE_STATE) {
                    //这段代码的目的是第一次进入app的时候需要bind service
                    startImageView.setImageResource(R.mipmap.record_pause);
                    mHandler.removeCallbacks(recordInfo.getRunnable());
                    mHandler.postDelayed(recordInfo.getRunnable(), 1000);
                    recordInfo.setRecordState(BaseConstant.RECORDING_STATE);
//                    updateInfo(position, recordInfo);
                    info.setCreateTime(System.currentTimeMillis());
                    recordInfo.setBeginTime(System.currentTimeMillis());
//                    recordInfo.setEndTime(0);
//                    recordInfo.setDuration(0);
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
//                updateInfo(position, recordInfo);
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

                        recordInfo.setEndTime(System.currentTimeMillis());
                        recordInfo.setDuration(recordInfo.getEndTime() - recordInfo.getBeginTime());
                        if (mServiceBinder != null) {
                            mServiceBinder.pauseRecorder(info);
                        }
                        //之前的totalTime加上这次的持续时间；
                        DaoManager.getInstance().updateRecordInfo(recordInfo, BaseConstant.UPDATE_RECORD_TIME_WHERE_CONDITION, new String[]{String.valueOf(recordInfo.getBeginTime())});
                    }
                }
            }
        };

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
            getContext().registerReceiver(mPauseTimeReceiver, pauseIntentFilter);
            getContext().registerReceiver(mStopTimeReceiver, stopIntentFilter);
        } catch (Exception e) {
        }
    }


    public ActivityInfo getActivityInfo() {
        return activityInfo;
    }

    public void setActivityInfo(ActivityInfo activityInfo) {
        this.activityInfo = activityInfo;
    }

    public RecordService.RecordServiceBinder getServiceBinder() {
        return mServiceBinder;
    }

    public void setServiceBinder(RecordService.RecordServiceBinder mServiceBinder) {
        this.mServiceBinder = mServiceBinder;
    }

    public Handler getHandler() {
        return mHandler;
    }

    public void setHandler(Handler mHandler) {
        this.mHandler = mHandler;
    }

    public BroadcastReceiver getPauseTimeReceiver() {
        return mPauseTimeReceiver;
    }

    public void setPauseTimeReceiver(BroadcastReceiver mPauseTimeReceiver) {
        this.mPauseTimeReceiver = mPauseTimeReceiver;
    }

    public BroadcastReceiver getStopTimeReceiver() {
        return mStopTimeReceiver;
    }

    public void setStopTimeReceiver(BroadcastReceiver mStopTimeReceiver) {
        this.mStopTimeReceiver = mStopTimeReceiver;
    }


}
