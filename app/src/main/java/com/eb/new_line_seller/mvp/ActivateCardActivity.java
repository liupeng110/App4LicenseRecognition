package com.eb.new_line_seller.mvp;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.new_line_seller.R;
import com.eb.new_line_seller.activity.CarInfoInputActivity;
import com.eb.new_line_seller.activity.MemberManagementInfoActivity;
import com.eb.new_line_seller.adapter.CarListAdapter;
import com.eb.new_line_seller.adapter.MealInfoListAdapter;
import com.eb.new_line_seller.bean.Meal2;
import com.eb.new_line_seller.bean.MealEntity;
import com.eb.new_line_seller.util.A2bigA;
import com.eb.new_line_seller.view.CardInputConfirmDialog;
import com.eb.new_line_seller.view.ConfirmDialog2;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.CarInfoRequestParameters;
import com.eb.new_line_seller.mvp.contacts.ActivityCardContacts;
import com.eb.new_line_seller.mvp.presenter.ActivateCardPtr;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.eb.new_line_seller.util.DateUtil.getFormatedDateTime;


public class ActivateCardActivity extends BaseActivity<ActivityCardContacts.ActivityCardPtr> implements ActivityCardContacts.ActivityCardUI, BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener {


    @BindView(R.id.et_user_mobile)
    EditText et_mobile;//手机号码

    @BindView(R.id.et_user_name)
    EditText et_name;//车主姓名

    @BindView(R.id.et_car_code)
    EditText et_code;//卡号

    @BindView(R.id.ll_view)
    View ll_view;//未车主测检隐藏的部分

    @BindView(R.id.v_date1)
    TextView v_date1;//开始时间
    @BindView(R.id.v_date2)
    TextView v_date2;//结束时间

    @BindView(R.id.tv_meal_name)
    TextView tv_meal_name;//选择的套卡名


    @BindView(R.id.tv_manager)
    TextView tv_manager;//录卡人
    @BindView(R.id.rv_car)
    RecyclerView rv_car;
    @BindView(R.id.rv_meal)
    RecyclerView rv_meal;

    CarListAdapter carListAdapter;//车辆列表
    MealInfoListAdapter mealInfoListAdapter;//套卡商品列表

    @Override
    protected void init() {
        tv_title.setText("纸卡录入");


        carListAdapter = new CarListAdapter(null);
        mealInfoListAdapter = new MealInfoListAdapter(null);

        rv_car.setLayoutManager(new LinearLayoutManager(this));
        rv_car.setAdapter(carListAdapter);

        rv_meal.setLayoutManager(new LinearLayoutManager(this));
        rv_meal.setAdapter(mealInfoListAdapter);


        carListAdapter.setOnItemClickListener(this);
        carListAdapter.setOnItemChildClickListener(this);


        v_date1.setText(getFormatedDateTime(Calendar.getInstance().getTime()));
        v_date2.setText(getFormatedDateTime(Calendar.getInstance().getTime()));

        et_code.setTransformationMethod(new A2bigA());
        et_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                getPresenter().setCardSn(editable.toString());
            }
        });


        //获取登陆用户的信息，设置录卡人
        getPresenter().getInfo();
    }


    @OnClick({R.id.tv_check, R.id.tv_enter_order, R.id.v_date1, R.id.v_date2, R.id.tv_add_car, R.id.tv_pick_meal})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_check://检测
                getPresenter().checkMember(et_mobile.getText().toString(), et_name.getText().toString());
                break;

            case R.id.tv_enter_order://确认录入

                getPresenter().showConfirmDialog();//显示确认Dialog

                break;
            case R.id.v_date1://开始时间
                getPresenter().setStartData(v_date1);
                break;
            case R.id.v_date2://结束时间
                getPresenter().setEndData(v_date2);
                break;
            case R.id.tv_add_car://添加车辆
                toCarInfoInputActivity(0);
                break;
            case R.id.tv_pick_meal://选择套卡

                toActivity(PickMealCardActivity.class);
                break;

        }


    }

    /**
     * 从选择套卡页面返回一个套卡对象
     *
     * @param intent Configure.act_tag = 101//从添加车辆页面返回
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        switch (intent.getIntExtra(Configure.act_tag, 0)) {

            case 101://从添加车辆页面返回
                getPresenter().checkMember(et_mobile.getText().toString(), et_name.getText().toString());
                break;
            default:
                getPresenter().setMealList((Meal2) intent.getParcelableExtra("Meal2"));
                break;
        }


    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_activate_card;
    }


    @Override
    public ActivityCardContacts.ActivityCardPtr onBindPresenter() {
        return new ActivateCardPtr(this);
    }

    @Override
    public void setCarList(List<CarInfoRequestParameters> carList) {
        carListAdapter.setNewData(carList);
    }

    @Override
    public void setMealInfoList(List<MealEntity> mealInfoList, String mealName) {
        mealInfoListAdapter.setNewData(mealInfoList);


        tv_meal_name.setVisibility(View.VISIBLE);
        tv_meal_name.setText(mealName);
    }

    @Override
    public void showView() {
        ll_view.setVisibility(View.VISIBLE);
    }

    @Override
    public void onShowConfirmDialog() {

        getPresenter().confirmInput();
    }

    @Override
    public void setUserName(String userName) {
        tv_manager.setText(userName);
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {


        if (carListAdapter.getData().get(position).isSelected()) {
            carListAdapter.getData().get(position).setSelected(false);
            getPresenter().setCarNo("");
        } else {
            for (CarInfoRequestParameters c : carListAdapter.getData()) {
                c.setSelected(false);
            }
            carListAdapter.getData().get(position).setSelected(true);
            getPresenter().setCarNo(carListAdapter.getData().get(position).getCarNo());
        }
        carListAdapter.notifyDataSetChanged();


    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        toCarInfoInputActivity(((CarInfoRequestParameters) adapter.getData().get(position)).getId());
    }

    private void toCarInfoInputActivity(int car_id) {
        Intent intent = new Intent(this, CarInfoInputActivity.class);
        intent.putExtra("result_code", 2);
        intent.putExtra(Configure.CARID, car_id);
        //跳转到车辆信息页面
        startActivity(intent);
    }
}
