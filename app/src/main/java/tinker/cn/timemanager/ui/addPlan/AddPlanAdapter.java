package tinker.cn.timemanager.ui.addPlan;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tinker.cn.timemanager.R;
import tinker.cn.timemanager.model.BaseConstant;
import tinker.cn.timemanager.model.ItemModel;

/**
 * Created by tiankui on 3/12/17.
 */

public class AddPlanAdapter extends RecyclerView.Adapter{

    List<ItemModel> modelList;

    public AddPlanAdapter(){
        modelList=new ArrayList<>();
    }
    @Override
    public int getItemViewType(int position) {
        ItemModel model=modelList.get(position);
        return model.getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        ItemHolder holder=null;
        View view;
        switch (viewType){
            case BaseConstant.PLAN_SUMMARY_TYPE:
                view=inflater.inflate(R.layout.item_add_plan_summary,parent,false);
                holder=new PlanSummaryViewHolder(view);
                break;
            case BaseConstant.PLAN_DETAIL_TYPE:
                view=inflater.inflate(R.layout.item_add_plan_detail,parent,false);
                holder=new PlanDetailViewHolder(view);
                break;
            case BaseConstant.ADD_PLAN_TYPE:
                view=inflater.inflate(R.layout.item_add_plan_detail,parent,false);
                holder=new AddPlanViewHolder(view);
                break;

        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ItemHolder) holder).update(position);
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }


    public abstract class ItemHolder extends RecyclerView.ViewHolder{

        public ItemHolder(View itemView) {
            super(itemView);
        }

        public abstract void update(int position);
    }

    public class PlanSummaryViewHolder extends ItemHolder{
        private TextView addPlanName;
        private TextView deletePlan;
        public PlanSummaryViewHolder(View itemView) {
            super(itemView);
            addPlanName=(TextView)itemView.findViewById(R.id.tv_add_plan_name);
            deletePlan=(TextView)itemView.findViewById(R.id.tv_delete_this_plan);
        }

        @Override
        public void update(int position) {
            ItemModel.PlanSummaryModel planSummaryModel=(ItemModel.PlanSummaryModel) modelList.get(position);
            addPlanName.setText(planSummaryModel.getPlanName());
        }
    }

    public class PlanDetailViewHolder extends ItemHolder{

        public PlanDetailViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void update(int position) {

        }
    }

    public class AddPlanViewHolder extends ItemHolder{

        public AddPlanViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void update(int position) {

        }
    }

    public List<ItemModel> getModelList() {
        return modelList;
    }

    public void setModelList(List<ItemModel> modelList) {
        this.modelList = modelList;
    }
}
