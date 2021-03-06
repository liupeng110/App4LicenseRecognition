package com.eb.new_line_seller.mvp;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.eb.new_line_seller.R;
import com.eb.new_line_seller.activity.MainActivity;
import com.eb.new_line_seller.mvp.contacts.LoginContacts;
import com.eb.new_line_seller.mvp.presenter.LoginPtr;

import butterknife.BindView;
import butterknife.OnClick;


public class LoginActivity2 extends BaseActivity<LoginContacts.LoginPtr> implements LoginContacts.LoginUI {


    @BindView(R.id.btu_get_phone_code)
    TextView tv_code;

    @BindView(R.id.et_phone)
    EditText et_phone;


    @BindView(R.id.et_car_code)
    EditText et_car_code;


    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_login;
    }

    @OnClick({R.id.but_login, R.id.btu_get_phone_code})
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.but_login:
                //登录
                getPresenter().login(et_phone.getText().toString(), et_car_code.getText().toString());
                break;
            case R.id.btu_get_phone_code:
                //发送短信
                getPresenter().smsSendSms(et_phone.getText().toString());
                break;
        }
    }

    @Override
    protected void init() {
        hideHeadView();
    }

    @Override
    public LoginContacts.LoginPtr onBindPresenter() {
        return new LoginPtr(this);
    }

    @Override
    public void loginSuccess() {
        showToast("登录成功");
        toActivity(MainActivity.class);
        finish();
    }

    @Override
    public void loginFailure(String masage) {
        showToast(masage);

    }

    @Override
    public void countDown(String cou) {
        tv_code.setText(cou);
    }

    @Override
    public void setCodeViewClickable(boolean isClickable) {
        tv_code.setClickable(isClickable);
    }


    @Override
    protected void onStop() {
        super.onStop();
        getPresenter().stopCountDown();

    }
}
