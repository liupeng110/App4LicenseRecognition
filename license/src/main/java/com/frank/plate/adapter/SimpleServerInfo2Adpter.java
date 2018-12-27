package com.frank.plate.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.frank.plate.R;
import com.frank.plate.bean.GoodsEntity;
import com.frank.plate.bean.Server;
import com.frank.plate.util.MathUtil;

import java.util.List;

public class SimpleServerInfo2Adpter extends BaseQuickAdapter<Server, BaseViewHolder> {

    public SimpleServerInfo2Adpter(@Nullable List<Server> data) {
        super(R.layout.activity_simple_good_list_item2, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Server item) {


        helper.setText(R.id.name, item.getName());
        helper.setText(R.id.price, "￥" + MathUtil.twoDecimal(item.getPrice())).setText(R.id.tv_number,  "x1");
    }
}
