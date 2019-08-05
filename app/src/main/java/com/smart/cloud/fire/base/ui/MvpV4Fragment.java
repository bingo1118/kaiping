package com.smart.cloud.fire.base.ui;

import android.os.Bundle;
import android.view.View;

import com.smart.cloud.fire.base.presenter.BasePresenter;

public abstract class MvpV4Fragment <P extends BasePresenter> extends BaseV4Fragment {
    protected P mvpPresenter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mvpPresenter = createPresenter();
    }

    protected abstract P createPresenter();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mvpPresenter != null) {
            mvpPresenter.detachView();
        }
    }
}
