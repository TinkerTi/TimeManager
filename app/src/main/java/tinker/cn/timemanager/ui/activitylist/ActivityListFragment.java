package tinker.cn.timemanager.ui.activitylist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tinker.cn.timemanager.R;

/**
 * Created by tiankui on 3/10/17.
 */

public class ActivityListFragment extends Fragment{

    /**
     * 在添加数据列表的时候，逻辑应该是先把数据添加到数据库中然后再从数据库中读取出来展示到界面中；
     * 需要从数据库中读取数据然后展示出来,
     *
     * 突然想到一个问题，就是不能用RecyclerView了，原因是，实时更新数据的时候
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView=(RecyclerView) inflater.inflate(R.layout.fr_plan_activity_list,container);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ActivityListAdapter adapter=new ActivityListAdapter(this);
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }
}
