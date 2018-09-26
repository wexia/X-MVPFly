package com.xia.fly.http.callback;

import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;

import com.blankj.utilcode.util.CloseUtils;
import com.xia.fly.utils.Platform;

import okhttp3.ResponseBody;

/**
 * @author xia
 * @date 2018/7/28.
 */
@SuppressWarnings("WeakerAccess")
public abstract class AbstractStringCallback extends Callback<String> {

    public AbstractStringCallback(@NonNull LifecycleOwner lifecycleOwner) {
        super(lifecycleOwner);
    }

    @Override
    public void parseNetworkResponse(ResponseBody responseBody) throws Exception {
        final String result = responseBody.string();
        Platform.post(() -> onResponse(result));
        CloseUtils.closeIO(responseBody);
    }
}