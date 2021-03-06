package com.eb.new_line_seller.adapter;

import android.support.annotation.Nullable;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.eb.new_line_seller.R;
import com.eb.new_line_seller.bean.MealEntity;
import com.eb.new_line_seller.bean.MealL0Entity;
import com.eb.new_line_seller.bean.MyMultipleItem;

import java.util.List;


//纸卡录入 选择套卡
public class MealPickListAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {


    public MealPickListAdapter(@Nullable List<MultiItemEntity> data) {
        super(data);
        addItemType(MyMultipleItem.FIRST_TYPE, R.layout.activity_pick_meal_list_item);
        addItemType(MyMultipleItem.SECOND_TYPE, R.layout.activity_pick_meal_list_item_item);


    }

    @Override
    protected void convert(final BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case MyMultipleItem.FIRST_TYPE:
                final MealL0Entity m = (MealL0Entity) item;

                helper.setText(R.id.tv_name, m.getActivityName());


                final ImageView iv = helper.getView(R.id.iv_pick);
                helper.addOnClickListener(R.id.ll_item);
                pick(m.isExpanded(), iv);

                break;
            case MyMultipleItem.SECOND_TYPE:
                MealEntity me = (MealEntity) item;
                helper.setText(R.id.tv_name, me.getName()).setText(R.id.tv_number, String.valueOf(me.getNumber()));
                helper.addOnClickListener(R.id.ib_reduce);
                helper.addOnClickListener(R.id.ib_plus);
                break;

        }
    }


    private void pick(boolean isPick, ImageView iv) {
        if (isPick)
            iv.setImageResource(R.drawable.icon_pick2);
        else
            iv.setImageResource(R.drawable.icon_unpick2);


    }


}