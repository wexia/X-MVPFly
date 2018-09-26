package com.xia.fly.base.delegate;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.xia.fly.constant.NetworkState;
import com.xia.fly.integration.rxbus.RxBusEventTag;
import com.xia.fly.integration.rxbus.RxBusHelper;
import com.xia.fly.receiver.NetworkChangeReceiver;
import com.xia.fly.ui.activities.IActivity;
import com.xia.fly.utils.Platform;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * {@link ActivityDelegate} 默认实现类
 *
 * @author xia
 * @date 2018/9/20.
 */
@SuppressWarnings("WeakerAccess")
public class ActivityDelegateImpl implements ActivityDelegate {
    private Activity mActivity;
    private IActivity mIActivity;
    private Unbinder mUnbinder;

    //网络状态监听广播
    private NetworkChangeReceiver mNetworkChangeReceiver;
    //记录上一次网络连接状态
    private int mLastNetStatus = NetworkState.NETWORK_DEFAULT;
    //网络是否重新连接
    private boolean mNetReConnect;

    public ActivityDelegateImpl(@NonNull Activity activity) {
        this.mActivity = activity;
        this.mIActivity = (IActivity) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (mIActivity.getLayoutId() != 0) {
            final View rootView = mActivity.getLayoutInflater().inflate(mIActivity.getLayoutId(), null);
            mActivity.setContentView(rootView);
            mUnbinder = ButterKnife.bind(mActivity, rootView);
        }
        mIActivity.initMvp();
        mIActivity.initData();
        mIActivity.initView();
        mIActivity.initEvent();
        mIActivity.onLazyLoadData();

        checkNetEvent();
    }

    private void checkNetEvent() {
        if (mIActivity.isCheckNetWork()) {
            mNetworkChangeReceiver = NetworkChangeReceiver.register(mActivity);
            RxBusHelper.subscribeWithTags(mActivity, (eventTag, rxBusMessage) -> {
                if (eventTag.equals(RxBusEventTag.NETWORK_CHANGE)) {
                    final boolean isAvailable = (boolean) rxBusMessage.mObj;
                    hasNetWork(isAvailable);
                }
            }, RxBusEventTag.NETWORK_CHANGE);
        }
    }

    private void hasNetWork(boolean isAvailable) {
        final int currentNetStatus = isAvailable ? NetworkState.NETWORK_ON : NetworkState.NETWORK_OFF;
        if (currentNetStatus != mLastNetStatus || mNetReConnect) {
            //判断网络是否是重连接的
            if (isAvailable && mLastNetStatus == NetworkState.NETWORK_OFF) {
                mNetReConnect = true;
            }
            //APP位于前台并且当前页处于栈顶（可见）时执行
            if (AppUtils.isAppForeground()
                    && ActivityUtils.getTopActivity() == mActivity) {
                mIActivity.onNetworkState(isAvailable);
                if (isAvailable && mNetReConnect) {
                    mIActivity.onNetReConnect();
                    mNetReConnect = false;
                }
                mLastNetStatus = currentNetStatus;
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onResume() {
        mIActivity.onVisibleLazyLoad();
        if (mIActivity.isCheckNetWork()) {
            hasNetWork(NetworkUtils.isConnected());
        }
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onDestroy() {
        Platform.getHandler().removeCallbacksAndMessages(null);
        RxBusHelper.unregister(mActivity);
        if (mUnbinder != null && mUnbinder != Unbinder.EMPTY) {
            mUnbinder.unbind();
            mUnbinder = null;
        }
        if (mNetworkChangeReceiver != null) {
            mActivity.unregisterReceiver(mNetworkChangeReceiver);
            mNetworkChangeReceiver = null;
        }
        this.mIActivity = null;
        this.mActivity = null;
    }
}