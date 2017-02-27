package tinker.cn.timemanager.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.util.UUID;

import tinker.cn.timemanager.R;
import tinker.cn.timemanager.model.ActivityInfo;
import tinker.cn.timemanager.model.RecordInfo;
import tinker.cn.timemanager.utils.DaoManager;
import tinker.cn.timemanager.utils.BaseConstant;

/**
 * Created by tiankui on 1/3/17.
 */

public class CreateActivityGroupDialogFragment extends BaseDialogFragment{



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fr_create_group_dialog, null);
        final EditText editText = (EditText) view.findViewById(R.id.fr_et_group_name);
        builder.setTitle(R.string.bottom_text_create_group)
                .setView(view)
                .setPositiveButton(R.string.create_ok_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityInfo info = new ActivityInfo();
                        info.setId(UUID.randomUUID().toString());
                        info.setType(BaseConstant.TYPE_GROUP);
                        //todo:设置是否为从群组内部创建还是单独创建
                        info.setParentGroupId(BaseConstant.Parent_GROUP_ID);
                        info.setName(editText.getText().toString().trim());
                        info.setFragmentTag(fragmentTag);
                        info.setName(editText.getText().toString().trim());
                        info.setCreateTime(System.currentTimeMillis());
                        info.setOriginCreateTime(System.currentTimeMillis());
                        info.setRecordInfo(new RecordInfo());
                        DaoManager.getInstance().addActivity(info);
                        if (mListener != null) {
                            mListener.onDialogPositiveClick(CreateActivityGroupDialogFragment.this, info);
                        }
                        dismiss();
                    }

                })
                .setNegativeButton(R.string.create_cancel_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mListener != null) {
                            mListener.onDialogNegativeClick(CreateActivityGroupDialogFragment.this);
                        }
                    }
                });
        return builder.create();
    }
}
