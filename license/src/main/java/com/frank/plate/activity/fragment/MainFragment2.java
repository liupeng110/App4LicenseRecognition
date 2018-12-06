package com.frank.plate.activity.fragment;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.frank.plate.Configure;
import com.frank.plate.R;
import com.frank.plate.activity.MakeOrderSuccessActivity;
import com.frank.plate.activity.OrderDoneActivity;
import com.frank.plate.activity.OrderInfoActivity;
import com.frank.plate.activity.OrderPayActivity;
import com.frank.plate.adapter.OrderListAdapter;
import com.frank.plate.api.RxSubscribe;
import com.frank.plate.bean.BasePage;
import com.frank.plate.bean.OrderInfo;
import com.frank.plate.bean.OrderInfoEntity;

import java.util.List;

import butterknife.BindView;


/**
 * 主页页面：订单
 */
public class MainFragment2 extends BaseFragment {

    public static final String TAG = "MainFragment2";
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.easylayout)
    EasyRefreshLayout easylayout;

    List<OrderInfoEntity> list;
    OrderListAdapter ola;

    @Override
    public int setLayoutResourceID() {
        return R.layout.fragment2_main;
    }

    @Override
    protected void setUpView() {
        ola = new OrderListAdapter(R.layout.item_fragment2_main, list, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(ola);
        ola.setEmptyView(R.layout.order_list_empty_view, recyclerView);


        easylayout.setLoadMoreModel(LoadModel.NONE);//只显示下拉刷新
        easylayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
            }

            @Override
            public void onRefreshing() {
                getData();
            }
        });


        ola.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {

                switch (view.getId()) {
                    case R.id.ll://显示of隐藏button
                        View ll_button = adapter.getViewByPosition(recyclerView, position, R.id.ll_button_view);
                        View iv_icon = adapter.getViewByPosition(recyclerView, position, R.id.iv_icon);
                        if (ll_button.getVisibility() == View.VISIBLE) {
                            ((ImageView) iv_icon).setImageResource(R.mipmap.icon_down);
                            ll_button.setVisibility(View.GONE);
                            ll_button.setTag(false);

                        } else {
                            ((ImageView) iv_icon).setImageResource(R.mipmap.icon_up);
                            ll_button.setVisibility(View.VISIBLE);
                            ll_button.setTag(true);
                        }
                        break;

                    case R.id.button_show_details://查看订单

                        toActivity(OrderInfoActivity.class, Configure.ORDERINFOID, list.get(position).getId());

                        break;
                    case R.id.button_action://动作按钮


                        Api().orderDetail(list.get(position).getId()).subscribe(new RxSubscribe<OrderInfo>(getContext(), true) {
                            @Override
                            protected void _onNext(OrderInfo orderInfo) {
                                int order_staus = orderInfo.getOrderInfo().getOrder_status();
                                int pay_staus = orderInfo.getOrderInfo().getPay_status();

                                if (order_staus == 0)//未服务
                                    if (pay_staus == 2)
                                        sendOrderInfo(MakeOrderSuccessActivity.class, orderInfo);
                                    else
                                        toActivity(OrderInfoActivity.class, Configure.ORDERINFOID, list.get(position).getId());
                                else if (order_staus == 1) {//服务中
                                    if (pay_staus == 2)
                                        sendOrderInfo(OrderDoneActivity.class, orderInfo);
                                    else
                                        sendOrderInfo(OrderPayActivity.class, orderInfo);
                                } else
                                    Toast.makeText(getContext(), "订单已完成", Toast.LENGTH_SHORT).show();


                            }

                            @Override
                            protected void _onError(String message) {
                                Log.d(TAG, message);
                                Toast.makeText(getContext(), "查找订单失败", Toast.LENGTH_SHORT).show();
                            }
                        });


                }


            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        Api().orderList().subscribe(new RxSubscribe<BasePage<OrderInfoEntity>>(mContext, true) {
            @Override
            protected void _onNext(BasePage<OrderInfoEntity> basePage) {
                easylayout.refreshComplete();

                list = basePage.getList();
                ola.setNewData(list);
            }

            @Override
            protected void _onError(String message) {
                Log.d(TAG, message);
                easylayout.refreshComplete();
            }
        });
    }


    @Override
    protected String setTAG() {
        return TAG;
    }

}
