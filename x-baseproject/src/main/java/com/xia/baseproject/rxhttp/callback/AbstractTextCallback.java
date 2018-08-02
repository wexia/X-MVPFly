package com.xia.baseproject.rxhttp.callback;

import android.content.Context;

import com.blankj.utilcode.util.CloseUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import okhttp3.ResponseBody;

/**
 * @author xia
 * @date 2018/7/28.
 */
public abstract class AbstractTextCallback extends Callback<String> {

    public AbstractTextCallback(Context context) {
        super(context);
    }

    @Override
    public String parseNetworkResponse(ResponseBody responseBody) throws Exception {
        final InputStream is = responseBody.byteStream();
        final Reader reader = new InputStreamReader(is);
        final BufferedReader bufferedReader = new BufferedReader(reader);
        String line;
        final StringBuilder result = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            result.append(line);
        }
        CloseUtils.closeIO(is, reader, bufferedReader);
        return result.toString();
    }
}