package com.frank.plate.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.frank.plate.R;
import com.frank.plate.bean.Technician;

import java.util.List;

public class TechnicianInfoAdpter extends BaseQuickAdapter<Technician, BaseViewHolder> {


    public TechnicianInfoAdpter(@Nullable List<Technician> data) {
        super(R.layout.activity_staff_management_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Technician item) {
        helper.setText(R.id.name, item.getUsername());
        helper.setText(R.id.phone, item.getUsername());
        helper.setText(R.id.lv, item.getRoleName());
    }
}