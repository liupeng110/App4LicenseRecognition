package com.eb.new_line_seller.activity;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.new_line_seller.R;
import com.eb.new_line_seller.adapter.ActivityListAdapter;

import com.eb.new_line_seller.api.RxSubscribe;
import com.juner.mvp.bean.ActivityEntityItem;
import com.juner.mvp.bean.ActivityPage;
import com.eb.new_line_seller.util.ToastUtils;

import java.util.List;

import butterknife.BindView;

public class ActivityPackageListActivity extends BaseActivity {

    private static final String TAG = "ActivityPackage";
    @BindView(R.id.rg_type)
    RadioGroup rg;

    @BindView(R.id.rv)
    RecyclerView rv;
    ActivityListAdapter ca;
    List<ActivityEntityItem> list;

    @Override
    protected void init() {
        tv_title.setText("活动列表");
    }

    @Override
    protected void setUpView() {

        rv.setLayoutManager(new LinearLayoutManager(this));
        ca = new ActivityListAdapter(list, this);
        rv.setAdapter(ca);
        getActivityList(0);


        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb1:

                        getActivityList(0);
                        break;
                    case R.id.rb2:
                        getActivityList(1);
                        break;


                }

            }
        });

        ca.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                toActivity(ActivityInfoActivity.class, "activity_id", list.get(position).getId());
            }
        });

    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_activity_package_list;
    }


    private void getActivityList(int type) {
        //1.平台活动 =0.门店活动
        Api().activityList(type).subscribe(new RxSubscribe<ActivityPage>(this, true) {
            @Override
            protected void _onNext(ActivityPage a) {
                list = a.getPage().getList();
                ca.setNewData(list);
            }

            @Override
            protected void _onError(String message) {
                Log.e(TAG, message);
                ToastUtils.showToast(message);
            }
        });

    }

}
