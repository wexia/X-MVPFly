package com.xia.baseproject.demo.ui.fragments;

import android.content.Intent;
import android.util.Log;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.xia.baseproject.demo.R;
import com.xia.baseproject.demo.base.AbstractHeadViewDelegate;
import com.xia.baseproject.demo.helper.DialogHelper;

import java.util.List;

import butterknife.OnClick;
import me.yokeyword.fragmentation.ISupportFragment;

/**
 * @author xia
 * @date 2018/7/3.
 */
public class MainDelegate extends AbstractHeadViewDelegate {

    private ISupportFragment[] mFragments = new ISupportFragment[1];

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    public void initData() {
        final ISupportFragment firstFragment = findChildFragment(TestDelegate.class);
        if (firstFragment == null) {
            mFragments[0] = new TestDelegate();
            loadMultipleRootFragment(R.id.main_fl, 0, mFragments);
        } else {
            mFragments[0] = firstFragment;
        }
    }

    @Override
    public void initView() {
    }

    @Override
    public void initEvent() {
    }

    @Override
    public void onVisibleLazyLoadData() {
    }

    @OnClick(R.id.main_btn)
    public void onViewClicked() {
        Log.e("weixi", "zzzzzzz");
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (mIsRequestPermission) {
            permission();
        }
        mIsRequestPermission = false;
        if (mIsToAppSetting) {
            mIsRequestPermission = true;
            mIsToAppSetting = false;
        }
    }

    private boolean mIsRequestPermission = true;
    private boolean mIsToAppSetting = false;

    private void permission() {
        PermissionUtils.permission(PermissionConstants.CAMERA, PermissionConstants.STORAGE)
                .rationale(shouldRequest -> DialogHelper.showRationaleDialog(MainDelegate.this, shouldRequest))
                .callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(List<String> permissionsGranted) {
                    }

                    @Override
                    public void onDenied(List<String> permissionsDeniedForever,
                                         List<String> permissionsDenied) {
                        DialogHelper.showOpenAppSettingDialog(MainDelegate.this);
                    }
                })
                .request();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DialogHelper.APP_SETTINGS_CODE) {
            mIsToAppSetting = true;
        }
    }
}