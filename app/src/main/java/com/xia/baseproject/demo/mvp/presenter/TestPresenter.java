package com.xia.baseproject.demo.mvp.presenter;

import com.xia.baseproject.demo.mvp.view.TestView;
import com.xia.baseproject.mvp.BaseMvpPresenter;

/**
 * @author xia
 * @date 2018/7/20.
 */
public class TestPresenter extends BaseMvpPresenter<TestView> {

    public void load() {
//        Log.e("weixi", "load");
//        Log.e("weixi", "getView:" + getV());
//        Log.e("weixi", "boolean:" + getV().getBoolean());
//        Log.e("weixi", "" + getV().getNum());

        getV().setData("你好啊！！！");
    }
}