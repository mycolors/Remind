package com.fengniao.remind.ui.base;


import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

public class BasePresenter<V> {

    public BaseActivity mContext;

    public BasePresenter(BaseActivity mContext) {
        this.mContext = mContext;
    }

    protected Reference<V> mViewRef;

    public void addachView(V view) {
        mViewRef = new WeakReference<V>(view);
    }

    public boolean isViewAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }

    public void detachView() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }

    public V getView() {
        return mViewRef != null ? mViewRef.get() : null;
    }

    public void start() {
    }
}
