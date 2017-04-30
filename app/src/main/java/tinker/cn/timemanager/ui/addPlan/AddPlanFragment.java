package tinker.cn.timemanager.ui.addPlan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import tinker.cn.timemanager.R;
import tinker.cn.timemanager.model.ItemModel;

/**
 * Created by tiankui on 3/12/17.
 */

public class AddPlanFragment extends Fragment {

    AddPlanAdapter addPlanAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView view = (RecyclerView) inflater.inflate(R.layout.fr_add_plan, container, false);
        view.setLayoutManager(new LinearLayoutManager(getActivity()));
        addPlanAdapter = new AddPlanAdapter();
        view.setAdapter(addPlanAdapter);
        initModelData();
        return view;
    }

    private void initModelData() {
        final List<ItemModel> modelList = new ArrayList<>();
        addPlan(modelList);
    }

    private void addPlan(final List<ItemModel> modelList) {
        final ItemModel.AddPlanModel addPlanModel = new ItemModel.AddPlanModel();
        modelList.add(addPlanModel);
        final ItemModel.AddPlanSummaryModel summaryModel = new ItemModel.AddPlanSummaryModel();
        addPlanModel.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        addPlanModel.setAddListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modelList.add(0, summaryModel);
                summaryModel.setPlanName(addPlanModel.getEditText().getText().toString());
                modelList.remove(modelList.size() - 1);
                addPlan(modelList);
                addPlanAdapter.setModelList(modelList);
            }
        });
        addPlanAdapter.setModelList(modelList);
    }

}
