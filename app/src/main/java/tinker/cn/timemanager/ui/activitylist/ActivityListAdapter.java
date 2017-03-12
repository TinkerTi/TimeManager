package tinker.cn.timemanager.ui.activitylist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;

import java.util.ArrayList;
import java.util.List;

import tinker.cn.timemanager.R;
import tinker.cn.timemanager.model.BaseConstant;
import tinker.cn.timemanager.model.ItemModel;

/**
 * Created by tiankui on 3/10/17.
 */

public class ActivityListAdapter extends RecyclerView.Adapter {

    List<ItemModel> modelList;
    private ActivityListFragment fragment;

    public ActivityListAdapter(ActivityListFragment fragment) {
        this.fragment = fragment;
        this.modelList = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        return modelList.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemHolder viewHolder = null;
        View view;
        switch (viewType) {
            case BaseConstant.DIVIDER_TYPE:
                view=inflater.inflate(R.layout.item_plan_divider,parent,false);
                viewHolder=new DividerViewHolder(view);
                break;
            case BaseConstant.TITLE_TYPE:
                view = inflater.inflate(R.layout.item_plan_title, parent, false);
                viewHolder = new TitleViewHolder(view);
                break;
            case BaseConstant.CONTENT_TYPE:
                view=inflater.inflate(R.layout.item_plan_content,parent,false);
                viewHolder=new ContentViewHolder(view);
                break;
            case BaseConstant.CONTENT_DETAIL_TYPE:
                view=inflater.inflate(R.layout.item_plan_content_detail,parent,false);
                viewHolder=new ContentDetailHolder(view);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public abstract class ItemHolder extends RecyclerView.ViewHolder {

        public ItemHolder(View itemView) {
            super(itemView);
        }

        abstract void update(int position);
    }

    public class DividerViewHolder extends ItemHolder{

        public DividerViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        void update(int position) {
        }
    }

    public class TitleViewHolder extends ItemHolder {
        private TextView titleTextView;

        public TitleViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.tv_plan_title);
        }

        @Override
        void update(int position) {
            ItemModel.TitleModel titleModel = (ItemModel.TitleModel) modelList.get(position);
            titleTextView.setText(titleModel.getTitleName());
        }
    }

    public class ContentViewHolder extends ItemHolder {
        private TextView nameTextView;
        private TextView recordingTimeTextView;

        public ContentViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.tv_content_name);
            recordingTimeTextView = (TextView) itemView.findViewById(R.id.tv_recording_time);
        }

        @Override
        void update(int position) {
            ItemModel.ContentModel contentModel = (ItemModel.ContentModel) modelList.get(position);
            nameTextView.setText(contentModel.getContentName());
            recordingTimeTextView.setText(String.valueOf(contentModel.getTime()));
        }
    }

    public class ContentDetailHolder extends ItemHolder {

        private BarChart barChart;
        private TextView totalTimeTextView;
        private TextView beginTextView;
        private TextView stopTextView;
        private TextView finishTextView;

        public ContentDetailHolder(View itemView) {
            super(itemView);
            barChart = (BarChart) itemView.findViewById(R.id.bc_time_bar);
            totalTimeTextView = (TextView) itemView.findViewById(R.id.tv_total_time);
            beginTextView = (TextView) itemView.findViewById(R.id.tv_begin);
            stopTextView = (TextView) itemView.findViewById(R.id.tv_stop);
            finishTextView = (TextView) itemView.findViewById(R.id.tv_finish);
        }

        @Override
        void update(int position) {
            ItemModel.ContentDetailModel contentDetailModel = (ItemModel.ContentDetailModel) modelList.get(position);
            totalTimeTextView.setText(String.valueOf(contentDetailModel.getTotalTime()));
        }
    }
}
