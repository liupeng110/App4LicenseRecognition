package com.frank.plate.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.frank.plate.MyApplication;
import com.frank.plate.R;

import com.frank.plate.activity.ProductListActivity;
import com.frank.plate.activity.ProductMealListActivity;
import com.frank.plate.activity.SetProjectActivity;
import com.frank.plate.adapter.ProductListAdapter;
import com.frank.plate.api.RxSubscribe;
import com.frank.plate.bean.GoodsEntity;
import com.frank.plate.bean.ProductList;
import com.frank.plate.bean.ProductValue;
import com.frank.plate.util.ToastUtils;
import com.frank.plate.view.CarNoLocationConfirmDialog;
import com.frank.plate.view.ProductListDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.frank.plate.Configure.ORDERINFO;
import static com.frank.plate.Configure.goodName;
import static com.frank.plate.Configure.setProject;
import static com.frank.plate.Configure.valueId;

public class ProductListFragment extends BaseFragment {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    ProductListAdapter productListAdapter;
    List<GoodsEntity> list = new ArrayList<>();
    String category_id, brand_id;//种类id,品牌id

    int isShow = 0;

    public static ProductListFragment getInstance(int show) {
        ProductListFragment sf = new ProductListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("isShow", show);

        //fragment保存参数，传入一个Bundle对象
        sf.setArguments(bundle);
        return sf;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //取出保存的值
            isShow = getArguments().getInt("isShow", -1);
        }

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_product_list_fr;
    }


    @Override
    protected void setUpView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        productListAdapter = new ProductListAdapter(this, null, isShow);
        recyclerView.setAdapter(productListAdapter);
        productListAdapter.setEmptyView(R.layout.order_list_empty_view_p, recyclerView);


        if (isShow == 1) {
            productListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                    TextView tv_number = (TextView) adapter.getViewByPosition(recyclerView, position, R.id.tv_number);
                    TextView view_value = (TextView) adapter.getViewByPosition(recyclerView, position, R.id.tv_product_value);


                    int number = list.get(position).getNumber();//获取
                    switch (view.getId()) {
                        case R.id.ib_plus:
                            if (list.get(position).getProduct_id() == 0) {
                                ToastUtils.showToast("请选择规格");
                                return;
                            }
                            number++;

                            tv_number.setText(String.valueOf(number));

                            MyApplication.cartUtils.addProductData(list.get(position));

                            list.get(position).setNumber(number);//设置

                            sendMsg(Double.parseDouble(list.get(position).getRetail_price()));

                            adapter.notifyDataSetChanged();

                            break;

                        case R.id.ib_reduce:
                            number--;

                            tv_number.setText(String.valueOf(number));
                            if (number == 0) {
                                view.setVisibility(View.INVISIBLE);//隐藏减号
                                tv_number.setVisibility(View.INVISIBLE);
                            }

                            MyApplication.cartUtils.reduceData(list.get(position));

                            list.get(position).setNumber(number);//设置
                            sendMsg(-Double.parseDouble(list.get(position).getRetail_price()));


                            adapter.notifyDataSetChanged();
                            break;

                        case R.id.tv_product_value:

                            getProductValue(view_value, position);
                            break;
                    }
                }
            });
        } else if (ProductListActivity.setProject != -1) {
            productListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    GoodsEntity g = (GoodsEntity) adapter.getData().get(position);
                    Intent intent = new Intent(getContext(), SetProjectActivity.class);
                    intent.putExtra(ORDERINFO, g);
                    intent.putExtra(setProject, ProductListActivity.setProject);
                    startActivity(intent);
                }
            });


        }


    }

    //获取规格列表
    private void getProductValue(final TextView view, final int positions) {

        Api().sku(list.get(positions).getId()).subscribe(new RxSubscribe<ProductList>(getContext(), true) {
            @Override
            protected void _onNext(final ProductList p) {

                final ProductListDialog confirmDialog = new ProductListDialog(getContext(), p.getProductList());
                confirmDialog.show();
                confirmDialog.setClicklistener(new ProductListDialog.ClickListenerInterface() {
                    @Override
                    public void doConfirm(ProductValue pick_value) {
                        confirmDialog.dismiss();
                        view.setText(pick_value.getValue());
                        list.get(positions).setProduct_id(pick_value.getId());
                        list.get(positions).setGoods_specifition_ids(pick_value.getGoods_specification_ids());
                        list.get(positions).setRetail_price(pick_value.getRetail_price());
                        list.get(positions).setMarket_price(pick_value.getMarket_price());
                        list.get(positions).setPrimary_pic_url(pick_value.getList_pic_url());
                        list.get(positions).setGoods_specifition_name_value(pick_value.getValue());
                        list.get(positions).setGoods_sn(pick_value.getGoods_sn());
                    }

                    @Override
                    public void doCancel() {
                        confirmDialog.dismiss();
                    }
                });


            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });


    }


    @Override
    protected void msgManagement(double what) {


        ((ProductMealListActivity) getActivity()).onPulsTotalPrice(what);
    }

    public void switchData(String category_id, String brand_id, List<GoodsEntity> l) {
        this.category_id = category_id;
        this.brand_id = brand_id;
        this.list = l;
        productListAdapter.setNewData(list);
    }

    public static final String TAG = "ProductListFragment";

    @Override
    protected String setTAG() {
        return TAG;
    }

}
