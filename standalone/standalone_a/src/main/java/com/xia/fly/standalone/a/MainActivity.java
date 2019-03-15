package com.xia.fly.standalone.a;

import com.xia.fly.module.a.ui.AFragment;
import com.xia.fly.ui.activities.ProxyActivity;
import com.xia.fly.ui.fragments.SupportFragment;

import androidx.annotation.NonNull;

public class MainActivity extends ProxyActivity {

    @NonNull
    @Override
    protected Class<? extends SupportFragment> setRootFragment() {
        return AFragment.class;
    }
}