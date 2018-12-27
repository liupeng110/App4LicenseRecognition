package com.frank.plate.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.frank.plate.R;
import com.frank.plate.bean.GoodsEntity;
import com.frank.plate.bean.Server;

import java.util.List;

public class SimpleServiceInfoAdpter extends BaseQuickAdapter<Server, BaseViewHolder> {


    boolean isShowPlusAndReduce;

    public SimpleServiceInfoAdpter(@Nullable List<Server> data, boolean is) {
        super(R.layout.activity_simple_good_list_item, data);
        isShowPlusAndReduce = is;
    }

    @Override
    protected void convert(BaseViewHolder helper, Server item) {


        helper.setText(R.id.name, item.getName())
                .setText(R.id.price, "￥" + item.getPrice())
                .setText(R.id.tv_number, "x1");


        if (!isShowPlusAndReduce) {

            helper.setVisible(R.id.ib_plus, false);
            helper.setVisible(R.id.ib_reduce, false);

        }

    }


}
