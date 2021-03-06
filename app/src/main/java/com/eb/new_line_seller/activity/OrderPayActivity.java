package com.eb.new_line_seller.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.new_line_seller.util.A2bigA;
import com.juner.mvp.Configure;
import com.eb.new_line_seller.R;
import com.eb.new_line_seller.adapter.Brandadapter2;
import com.eb.new_line_seller.api.RxSubscribe;

import com.juner.mvp.bean.Coupon;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.OffLinePayType;
import com.juner.mvp.bean.OrderInfo;
import com.eb.new_line_seller.util.DateUtil;
import com.eb.new_line_seller.util.PayTypeList;
import com.eb.new_line_seller.util.ToastUtils;
import com.eb.new_line_seller.view.CommonPopupWindow;

import com.eb.new_line_seller.view.ConfirmDialog2;
import com.eb.new_line_seller.view.DecimalInputTextWatcher;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.eb.new_line_seller.util.MathUtil.twoDecimal;

public class OrderPayActivity extends BaseActivity {

    OrderInfo infoEntity;

    @BindView(R.id.tv_order_sn)
    TextView tv_order_sn;
    @BindView(R.id.tv_car_no)
    TextView tv_car_no;
    @BindView(R.id.tv_make_date)
    TextView tv_make_date;
    @BindView(R.id.tv_expect_date)
    TextView tv_expect_date;
    @BindView(R.id.tv_order_price)
    TextView tv_order_price;

    @BindView(R.id.tv_pick_pay_type)
    TextView tv_pick_pay_type;

    @BindView(R.id.tv_coupon_no)
    TextView tv_coupon_no;

    @BindView(R.id.et_discount)
    EditText et_discount;

    @BindView(R.id.et_discount2)
    EditText et_discount2;

    @BindView(R.id.et_car_code)
    EditText et_car_code;

    @BindView(R.id.cb_weixin)
    CheckBox cb_weixin;

    @BindView(R.id.ll_card_num)
    View ll_card_num;//卡号输入框

    RecyclerView commonPopupRecyclerView;

    CommonPopupWindow popupWindow;
    Brandadapter2 brandadapter;

    Coupon c;//选择的优惠券
    int pay_type = 11;//

    List<OffLinePayType> olpy;

    OffLinePayType olpt;//选择的支付方式；不包括11微信


    double balance_price;//结算金额
    @BindView(R.id.tv_price)
    TextView tv_price;

    @Override
    protected void init() {
        tv_title.setText("订单收款");
        et_car_code.setTransformationMethod(new A2bigA());
        olpy = PayTypeList.getList();
        infoEntity = getIntent().getParcelableExtra(Configure.ORDERINFO);
        Api().orderDetail(infoEntity.getOrderInfo().getId()).subscribe(new RxSubscribe<OrderInfo>(this, true) {
            @Override
            protected void _onNext(OrderInfo o) {
                Log.i("OrderInfo订单信息：", o.getOrderInfo().toString());
                infoEntity = o;
                setInfo();
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });

    }

    private void setInfo() {

        tv_order_sn.append(infoEntity.getOrderInfo().getOrder_sn());
        tv_car_no.append(infoEntity.getOrderInfo().getCar_no());
        tv_make_date.append(infoEntity.getOrderInfo().getAdd_time());
        tv_expect_date.append(DateUtil.getFormatedDateTime(infoEntity.getOrderInfo().getPlanfinishi_time()));
        tv_order_price.append(String.format("￥%s", infoEntity.getOrderInfo().getOrder_price()));
        tv_price.setText(String.format("%s", infoEntity.getOrderInfo().getOrder_price()));

        balance_price = infoEntity.getOrderInfo().getOrder_price();


        brandadapter = new Brandadapter2(olpy);
        brandadapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                popupWindow.dismiss();

                cb_weixin.setChecked(false);
                olpt = olpy.get(position);
                tv_pick_pay_type.setText(olpy.get(position).getType_string());
                pay_type = olpy.get(position).getPay_type();

                ToastUtils.showToast("支付方式:" + olpy.get(position).getType_string());

                if (pay_type == 21 || pay_type == 23)
                    ll_card_num.setVisibility(View.VISIBLE);
                else
                    ll_card_num.setVisibility(View.GONE);


            }

        });
        commonPopupRecyclerView = new RecyclerView(this);
        commonPopupRecyclerView.setAdapter(brandadapter);
        commonPopupRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        popupWindow = new CommonPopupWindow.Builder(this)
                .setView(commonPopupRecyclerView)
                .create();

        cb_weixin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b)
                    pay_type = 11;
                else
                    pay_type = 0;


            }
        });


        et_discount.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable e) {


                if (e.length() != 0) {
                    double d = Double.parseDouble(e.toString());
                    tv_price.setText(twoDecimal((balance_price * d) / 10));
                } else {
                    tv_price.setText(twoDecimal(balance_price));
                }
            }
        });

        et_discount.addTextChangedListener(new DecimalInputTextWatcher(et_discount, 1, 1));//限制输入位数：整数3位，小数点后两位

        et_discount2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable e) {

                if (e.length() != 0) {
                    double d = Double.parseDouble(e.toString());

                    if (balance_price < d) {
                        tv_price.setText("0");
                    } else

                        tv_price.setText(String.valueOf((balance_price - d)));
                } else {
                    tv_price.setText(String.valueOf(balance_price));
                }
            }
        });


    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_order_pay;
    }


    @OnClick({R.id.tv_enter_pay, R.id.tv_pick_pay_type, R.id.tv_pick_coupon})
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.tv_enter_pay:

                if (!TextUtils.isEmpty(et_discount.getText()) && !TextUtils.isEmpty(et_discount2.getText())) {
                    ToastUtils.showToast("折扣和减免不能同享");
                    return;
                }

                getPostInfo();

                if (pay_type == 0)
                    ToastUtils.showToast("请选择一种支付方式");
                else if (pay_type == 11) {//微信支付
                    Intent i = new Intent(OrderPayActivity.this, WeiXinPayCodeActivity.class);
                    i.putExtra("shop_name", infoEntity.getShop().getShopName());
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(Configure.ORDERINFO, infoEntity.getOrderInfo());
                    i.putExtras(bundle);
                    startActivity(i);
                } else
                    Pay();
                break;

            case R.id.tv_pick_pay_type:

                popupWindow.showAsDropDown(view, 0, 0);
                break;


            case R.id.tv_code:

                final ConfirmDialog2 confirmDialog = new ConfirmDialog2(this);
                confirmDialog.show();
                confirmDialog.setClicklistener(new ConfirmDialog2.ClickListenerInterface() {
                    @Override
                    public void doConfirm(String code) {
                        ((TextView) view).setText("嗨卡码：" + code);
                    }

                    @Override
                    public void doCancel() {
                        // TODO Auto-generated method stub
                        confirmDialog.dismiss();
                    }
                });
                break;

            case R.id.tv_pick_coupon:

                Intent i = new Intent(OrderPayActivity.this, PickCoupons.class);
                i.putExtra("order_price", String.valueOf(infoEntity.getOrderInfo().getOrder_price()));
                i.putExtra("order_price", "1000");
                i.putExtra(Configure.user_id, 1);
                startActivity(i);
                break;
        }

    }

    private void getPostInfo() {

        if (pay_type == 11) {
            infoEntity.getOrderInfo().setPay_type(11);
            infoEntity.getOrderInfo().setPay_name("微信");
        } else if (pay_type != 0) {
            infoEntity.getOrderInfo().setPay_type(olpt.getPay_type());
            infoEntity.getOrderInfo().setPay_name(olpt.getType_string());
        }

        infoEntity.getOrderInfo().setCoupon_id(null == c ? 0 : c.getId());
        infoEntity.getOrderInfo().setProvince(et_car_code.getText().toString());


        if (!TextUtils.isEmpty(et_discount.getText())) {
            infoEntity.getOrderInfo().setDiscount_price(et_discount.getText().toString());
            infoEntity.getOrderInfo().setCustom_cut_price(null);

        } else if (!TextUtils.isEmpty(et_discount2.getText())) {
            infoEntity.getOrderInfo().setCustom_cut_price(et_discount2.getText().toString());
            infoEntity.getOrderInfo().setDiscount_price(null);
        } else {
            infoEntity.getOrderInfo().setDiscount_price(null);
            infoEntity.getOrderInfo().setCustom_cut_price(null);
        }


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int code = intent.getIntExtra("code", -1);
        if (code == 110) {

            c = intent.getParcelableExtra("Coupon");
            tv_coupon_no.setText("已选择 1 张");

            if (balance_price >= c.getMin_amount())
                tv_price.setText(String.valueOf(balance_price - c.getType_money()));

        }


    }


    private void Pay() {


        // 确认支付
        Api().confirmPay(infoEntity.getOrderInfo()).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
            @Override
            protected void _onNext(NullDataEntity o) {

                ToastUtils.showToast("收款成功");
                if (infoEntity.getOrderInfo().getOrder_status() == 0) {

                    toMain(1);


                } else if (infoEntity.getOrderInfo().getOrder_status() == 1)
                    toActivity(OrderDoneActivity.class, Configure.ORDERINFOID, infoEntity.getOrderInfo().getId());
            }

            @Override
            protected void _onError(String message) {
                Log.i("OrderPayActivity", message);
                ToastUtils.showToast("收款失败：" + message);
            }
        });


    }


}
