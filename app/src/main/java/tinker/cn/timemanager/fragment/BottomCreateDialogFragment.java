package tinker.cn.timemanager.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import tinker.cn.timemanager.R;

/**
 * Created by tiankui on 1/3/17.
 */

public class BottomCreateDialogFragment extends DialogFragment {

    private String fragmentTag;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("fragmentTag")) {
            this.fragmentTag = bundle.getString("fragmentTag");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fr_bottom_dialog_view, null);
        view.findViewById(R.id.fr_ll_create_group).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateActivityGroupDialogFragment dialogFragment = new CreateActivityGroupDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString("fragmentTag", fragmentTag);
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getFragmentManager(), null);
                dismiss();
            }
        });
        view.findViewById(R.id.fr_ll_create_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateActivityDialogFragment dialogFragment = new CreateActivityDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString("fragmentTag", fragmentTag);
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getFragmentManager(), null);
                dismiss();
            }
        });
        builder.setView(view);
        return builder.create();
    }

}
