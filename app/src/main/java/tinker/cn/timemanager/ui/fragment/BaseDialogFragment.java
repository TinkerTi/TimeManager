package tinker.cn.timemanager.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import tinker.cn.timemanager.model.ActivityInfo;

/**
 * Created by tiankui on 1/3/17.
 */

public class BaseDialogFragment extends DialogFragment {

    protected NoticeDialogListener mListener;
    protected String fragmentTag;
    protected ActivityInfo mActivityInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("fragmentTag")) {
                this.fragmentTag = bundle.getString("fragmentTag");
            }
            if (bundle.containsKey("activityInfo")) {
                this.mActivityInfo = bundle.getParcelable("activityInfo");
            }
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (NoticeDialogListener) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface NoticeDialogListener {
        void onDialogPositiveClick(DialogFragment dialogFragment, ActivityInfo info);

        void onDialogNegativeClick(DialogFragment dialogFragment);
    }
}
