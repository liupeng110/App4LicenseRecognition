package com.eb.new_line_seller.activity.fragment;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.eb.new_line_seller.MyApplication;
import com.eb.new_line_seller.R;
import com.eb.new_line_seller.adapter.MealListAdapter;
import com.eb.new_line_seller.api.RxSubscribe;
import com.eb.new_line_seller.bean.Meal;
import com.eb.new_line_seller.bean.MealEntity;
import com.eb.new_line_seller.bean.MealL0Entity;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class ProductMealFragment extends BaseFragment {

    private static int id;
    private static String car_no;
    @BindView(R.id.rv)
    RecyclerView rv;
    MealListAdapter mealListAdapter;
    List<MultiItemEntity> list;

    public static ProductMealFragment getInstance(int user_id,String no) {
        ProductMealFragment sf = new ProductMealFragment();
        id = user_id;
        car_no = no;
        return sf;
    }


    @Override
    public int setLayoutResourceID() {
        return R.layout.fragment_meal_list_fr;
    }


    @Override
    protected void setUpView() {
        mealListAdapter = new MealListAdapter(null);

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(mealListAdapter);
        mealListAdapter.setEmptyView(R.layout.order_list_empty_view_p, rv);

        mealListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                MealEntity m = (MealEntity) adapter.getData().get(position);

                if (m.isSelected()) {
                    m.setSelected(false);
                    MyApplication.cartUtils.reduceMeal(m);
                } else {
                    m.setSelected(true);
                    MyApplication.cartUtils.addMeal(m);
                }
                adapter.notifyDataSetChanged();
            }
        });


    }


    @Override
    protected void onVisible() {
        super.onVisible();

        Api().queryUserAct(id,car_no).subscribe(new RxSubscribe<Meal>(getContext(), true) {
            @Override
            protected void _onNext(Meal mealList) {
                list = generateData(mealList.getList());
                mealListAdapter.setNewData(list);

            }

            @Override
            protected void _onError(String message) {
//                ToastUtils.showToast("套餐列表为空");
            }
        });


    }

    private List<MultiItemEntity> generateData(Map<String, List<MealEntity>> map) {

        List<MultiItemEntity> res = new ArrayList<>();

        for (List<MealEntity> list : map.values()) {

            MealL0Entity lv0 = new MealL0Entity(list.get(0).getActivityName(), list.get(0).getActivitySn());

            for (MealEntity entity : list) {
                lv0.addSubItem(entity);
            }
            res.add(lv0);

        }
        return res;
    }


    public static final String TAG = "ProductListFragment";

    @Override
    protected String setTAG() {
        return TAG;
    }

}
