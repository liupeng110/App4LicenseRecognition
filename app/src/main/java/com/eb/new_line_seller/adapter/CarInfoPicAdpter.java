package com.eb.new_line_seller.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.new_line_seller.R;
import com.juner.mvp.bean.UpDataPicEntity;

import java.util.List;

public class CarInfoPicAdpter extends BaseQuickAdapter<UpDataPicEntity, BaseViewHolder> {


    Context context;


    public CarInfoPicAdpter(@Nullable List<UpDataPicEntity> data, Context context) {


        super(R.layout.item_car_info_pic, data);
        this.context = context;


    }

    @Override
    protected void convert(BaseViewHolder helper, UpDataPicEntity item) {

        ImageView iv = helper.getView(R.id.iv);
        Glide.with(context)
                .load(item.getImageUrl())
                .into(iv);

    }
}
