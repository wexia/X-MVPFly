package com.xia.fly.demo.app;

import android.app.Application;
import android.content.Context;

import com.xia.fly.base.delegate.AppLifecycles;
import com.xia.fly.di.module.GlobalConfigModule;
import com.xia.fly.http.cookie.CookieJarImpl;
import com.xia.fly.http.cookie.store.MemoryCookieStore;
import com.xia.fly.http.interceptors.RequestInterceptor;
import com.xia.fly.http.utils.HttpsUtils;
import com.xia.fly.http.utils.RxJavaUtils;
import com.xia.fly.imageloader.GlideImageLoaderStrategy;
import com.xia.fly.integration.ConfigModule;

import java.net.Proxy;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author xia
 * @date 2018/9/18.
 */
public final class GlobalConfiguration implements ConfigModule {

    private static final HttpsUtils.SSLParams SSL_PARAMS = HttpsUtils.getSslSocketFactory();

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlobalConfigModule.Builder builder) {
        //RxJava2 取消订阅后，抛出的异常无法捕获，将导致程序崩溃
        RxJavaUtils.setRxJavaErrorHandler();

//        if (!BuildConfig.DEBUG) { //Release 时,让框架不再打印 Http 请求和响应的信息
        builder.printHttpLogLevel(RequestInterceptor.Level.NONE);
//        }
        builder.baseurl("http://www.wanandroid.com")
                .imageLoaderStrategy(new GlideImageLoaderStrategy())
                .okhttpConfiguration((context1, okHttpBuilder) -> okHttpBuilder
                        //打印返回信息
                        .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                        //阻止第三方使用 Fiddler 或 Charles 进行抓包
                        .proxy(Proxy.NO_PROXY)
                        //设置超时
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS)
                        //错误重连
                        .retryOnConnectionFailure(true)
                        //cookie认证
                        .cookieJar(new CookieJarImpl(new MemoryCookieStore()))
                        .hostnameVerifier(new HttpsUtils.SAFE_HOSTNAME_VERIFIER())
                        .sslSocketFactory(SSL_PARAMS.sSLSocketFactory, SSL_PARAMS.trustManager)
                )
                .retrofitConfiguration((context12, retrofitBuilder) -> {
                })
                .responseErrorListener(new ResponseErrorListenerImpl());
    }

    @Override
    public void injectAppLifecycle(@NonNull Context context, @NonNull List<AppLifecycles> lifecycles) {
        lifecycles.add(new AppLifecyclesImpl());
    }

    @Override
    public void injectActivityLifecycle(@NonNull Context context, @NonNull List<Application.ActivityLifecycleCallbacks> lifecycles) {
    }

    @Override
    public void injectFragmentLifecycle(@NonNull Context context, @NonNull List<FragmentManager.FragmentLifecycleCallbacks> lifecycles) {
    }
}
