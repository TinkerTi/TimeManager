package tinker.cn.timemanager.ui.addPlan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
        RecyclerView view=(RecyclerView) inflater.inflate(R.layout.fr_add_plan,container,false);
        view.setLayoutManager(new LinearLayoutManager(getActivity()));
        addPlanAdapter=new AddPlanAdapter();
        view.setAdapter(addPlanAdapter);
        initModelData();
       return view;
    }

    private void initModelData(){
        List<ItemModel> modelList=new ArrayList<>();
        ItemModel.PlanSummaryModel planSummaryModel=new ItemModel.PlanSummaryModel();
        planSummaryModel.setPlanName("写app");
        modelList.add(planSummaryModel);
        addPlanAdapter.setModelList(modelList);
//        addPlanAdapter.notifyItemChanged(0);
    }

}
