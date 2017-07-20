package com.fengniao.remind.ui.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import butterknife.ButterKnife;

public abstract class BaseActivity<V, T extends BasePresenter<V>> extends AppCompatActivity {
    protected T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

        setContentView(provideContentViewId());
        ButterKnife.bind(this);

        //判断是否使用MVP模式
        mPresenter = createPresent();
        if (mPresenter != null) {
            mPresenter.addachView((V) this);   //之后所有的子类都要实现对应的View借口，所里这里可进行强转
        }

        initView();
        initData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //在setContentView()前调用，可以设置WindowFeature(如：this.requestWindowFeature(Window.FEATURE_NO_TITLE);)
    public void init() {

    }

    public void initView() {

    }

    public void initData() {

    }

    //创建presenter，并以此判断是否使用mvp模式
    public abstract T createPresent();

    //提供当前布局id（由子类实现）
    protected abstract int provideContentViewId();


    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
