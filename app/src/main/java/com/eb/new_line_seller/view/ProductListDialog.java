package com.eb.new_line_seller.view;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.new_line_seller.MyApplication;
import com.eb.new_line_seller.R;
import com.eb.new_line_seller.adapter.ProductListAdpter;

import com.juner.mvp.bean.GoodsEntity;
import com.juner.mvp.bean.ProductValue;
import com.eb.new_line_seller.util.ToastUtils;

import java.util.List;


public class ProductListDialog extends Dialog {

    private Context context;
    private ClickListenerInterface clickListenerInterface;
    List<ProductValue> valueList;

    ProductValue pick_value;

    int cont;//数量
    TextView tv_number;
    TextView tv_value;
    TextView tv_price;
    View ib_plus;
    View ib_reduce;

    public interface ClickListenerInterface {

        void doConfirm(ProductValue productValue);

        void doCancel();
    }

    public ProductListDialog(Context context, List<ProductValue> list) {
        super(context, R.style.my_dialog);
        this.context = context;
        this.valueList = list;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);
        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_product_list, null);
        setContentView(view);


        RecyclerView recyclerView = view.findViewById(R.id.rv);

        ib_reduce = view.findViewById(R.id.ib_reduce);//减号
        ib_plus = view.findViewById(R.id.ib_plus);//加号
        tv_number = view.findViewById(R.id.tv_number);//数量
        tv_value = view.findViewById(R.id.tv_value);//规格值
        tv_price = view.findViewById(R.id.tv_price);//

        TextView tv_confirm = view.findViewById(R.id.tv_confirm);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));

        ProductListAdpter c = new ProductListAdpter(valueList);
        recyclerView.setAdapter(c);

        c.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                pick_value = valueList.get(position);
                tv_value.setText(pick_value.getValue());

                for (ProductValue c : valueList) {
                    c.setSelected(false);
                }

                valueList.get(position).setSelected(true);
                adapter.notifyDataSetChanged();


                setPrice();//每次更换规格都重新计算价格
            }
        });


        tv_confirm.setOnClickListener(new clickListener());
        ib_plus.setOnClickListener(new clickListener());
        ib_reduce.setOnClickListener(new clickListener());

        cont = Integer.parseInt(tv_number.getText().toString());

    }

    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            int id = v.getId();
            if (isNullValue()) {
                ToastUtils.showToast("请选择一个规格！");
                return;
            }
            switch (id) {
                case R.id.tv_confirm:
                    if (cont > 0)
                        clickListenerInterface.doConfirm(pick_value);
                    else ToastUtils.showToast("请选择数量！");

                    break;
                case R.id.ib_reduce://减
                    if (cont <= 0) return;
                    cont--;
                    setCont();
                    MyApplication.cartUtils.reduceDataNoCommit(toGood(pick_value));//添加不提交


                    break;

                case R.id.ib_plus://加

                    cont++;
                    setCont();
                    MyApplication.cartUtils.addDataNoCommit(toGood(pick_value));//添加不提交
                    break;
            }
        }
    }

    private void setCont() {

        tv_number.setText(String.valueOf(cont));
        pick_value.setNumber(cont);
        setPrice();

    }

    private void setPrice() {

        double parseDouble = Double.parseDouble(pick_value.getRetail_price());
        parseDouble = parseDouble * cont;
        tv_price.setText("￥" + String.valueOf(parseDouble));

    }

    private boolean isNullValue() {

        if (null != pick_value)
            return false;
        else
            return true;

    }


    private GoodsEntity toGood(ProductValue pv) {

        GoodsEntity goodsEntity = new GoodsEntity();

        goodsEntity.setId(pv.getGoods_id());
        goodsEntity.setProduct_id(pv.getId());
        goodsEntity.setGoods_specifition_ids(pv.getGoods_specification_ids());
        goodsEntity.setRetail_price(pv.getRetail_price());
        goodsEntity.setMarket_price(pv.getMarket_price());
        goodsEntity.setPrimary_pic_url(pv.getList_pic_url());
        goodsEntity.setGoods_specifition_name_value(pv.getValue());
        goodsEntity.setGoods_sn(pv.getGoods_sn());
        goodsEntity.setGoodsName(pv.getGoods_name());
        goodsEntity.setName(pv.getGoods_name());
        goodsEntity.setNumber(pv.getNumber());

        return goodsEntity;
    }

}