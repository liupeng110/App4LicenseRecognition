package com.frank.plate.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.frank.plate.R;
import com.frank.plate.bean.CarEntity;
import com.frank.plate.bean.GoodsEntity;

import java.util.List;

public class SimpleCarInfoAdpter extends BaseQuickAdapter<CarEntity, BaseViewHolder> {

    public SimpleCarInfoAdpter(@Nullable List<CarEntity> data) {
        super(R.layout.activity_car_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CarEntity item) {

        helper.setText(R.id.tv_car_no, item.getCarNo())
                .setText(R.id.tv_car_model, item.getCarModel());


    }


}
