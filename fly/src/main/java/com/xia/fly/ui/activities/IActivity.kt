package com.xia.fly.ui.activities

import android.app.Activity
import android.widget.EditText

import com.xia.fly.integration.cache.Cache
import com.xia.fly.integration.cache.LruCache

/**
 * @author xia
 * @date 2018/9/11.
 */
interface IActivity {

    /**
     * @return 用于布局加载
     * 如果[.getLayoutId]返回0，则不会生产视图
     */
    fun getLayoutId(): Int

    /**
     * @return 是否检查网络状态，默认为true
     */
    fun isCheckNetWork(): Boolean

    /**
     * @return 是否点击空白区域隐藏软键盘，默认为true
     */
    fun isDispatchTouchHideKeyboard(): Boolean

    /**
     * 提供在 [Activity] 生命周期内的缓存容器, 可向此 [Activity] 存取一些必要的数据
     * 此缓存容器和 [Activity] 的生命周期绑定, 如果 [Activity] 在屏幕旋转或者配置更改的情况下
     * 重新创建, 那此缓存容器中的数据也会被清空, 如果你想避免此种情况请使用 [LifecycleModel](https://github.com/JessYanCoding/LifecycleModel)
     *
     * @return like [LruCache]
     */
    fun provideCache(): Cache<String, Any>

    /**
     * 用于初始化MVP
     */
    fun initMvp()

    /**
     * 用于初始化数据
     */
    fun initData()

    /**
     * 用于初始化View
     */
    fun initView()

    /**
     * 用于初始化事件
     */
    fun initEvent()

    /**
     * 该方法执行于
     * [SupportActivity.initData]
     * [SupportActivity.initView]
     * [SupportActivity.initEvent]
     * 之后，可用于加载网络数据等
     */
    fun onLazyLoadData()

    /**
     * @param isAvailable 网络是否连接可用
     */
    fun onNetworkState(isAvailable: Boolean)

    /**
     * 用于网络连接恢复后加载
     */
    fun onNetReConnect()

    /**
     * 点击空白区域，隐藏软键盘时，EditText进行相关设置
     */
    fun onDispatchTouchHideKeyboard(editText: EditText)
}
