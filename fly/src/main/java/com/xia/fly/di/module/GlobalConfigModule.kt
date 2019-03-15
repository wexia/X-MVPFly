package com.xia.fly.di.module

import android.app.Application
import android.text.TextUtils
import com.bumptech.glide.Glide
import com.xia.fly.http.BaseUrl
import com.xia.fly.http.GlobalHttpHandler
import com.xia.fly.http.interceptors.RequestInterceptor
import com.xia.fly.http.log.DefaultFormatPrinter
import com.xia.fly.http.log.FormatPrinter
import com.xia.fly.integration.cache.Cache
import com.xia.fly.integration.cache.CacheType
import com.xia.fly.integration.cache.IntelligentCache
import com.xia.fly.integration.cache.LruCache
import com.xia.fly.ui.imageloader.BaseImageLoaderStrategy
import com.xia.fly.utils.FileUtils
import com.xia.fly.utils.Preconditions
import dagger.Module
import dagger.Provides
import me.jessyan.rxerrorhandler.handler.listener.ResponseErrorListener
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.internal.Util
import java.io.File
import java.util.*
import java.util.concurrent.*
import javax.inject.Singleton

/**
 * 框架独创的建造者模式 [Module],可向框架中注入外部配置的自定义参数
 *
 * @author xia
 * @date 2018/9/14.
 */
@Suppress("unused")
@Module
class GlobalConfigModule private constructor(builder: Builder) {
    private val mApiUrl: HttpUrl?
    private val mBaseUrl: BaseUrl?
    private val mLoaderStrategy: BaseImageLoaderStrategy<*>?
    private val mHandler: GlobalHttpHandler?
    private val mInterceptors: MutableList<Interceptor>?
    private val mErrorListener: ResponseErrorListener?
    private val mCacheFile: File?
    private val mRetrofitConfiguration: ClientModule.RetrofitConfiguration?
    private val mOkHttpConfiguration: ClientModule.OkHttpConfiguration?
    private val mRxCacheConfiguration: ClientModule.RxCacheConfiguration?
    private val mGsonConfiguration: AppModule.GsonConfiguration?
    private val mPrintHttpLogLevel: RequestInterceptor.Level?
    private val mFormatPrinter: FormatPrinter?
    private val mCacheFactory: Cache.Factory<String, Any>?
    private val mExecutorService: ExecutorService?

    init {
        this.mApiUrl = builder.mApiUrl
        this.mBaseUrl = builder.mBaseUrl
        this.mLoaderStrategy = builder.mImageLoaderStrategy
        this.mHandler = builder.mHttpHandler
        this.mInterceptors = builder.mInterceptors
        this.mErrorListener = builder.mResponseErrorListener
        this.mCacheFile = builder.mCacheFile
        this.mRetrofitConfiguration = builder.mRetrofitConfiguration
        this.mOkHttpConfiguration = builder.mOkHttpConfiguration
        this.mRxCacheConfiguration = builder.mRxCacheConfiguration
        this.mGsonConfiguration = builder.mGsonConfiguration
        this.mPrintHttpLogLevel = builder.mPrintHttpLogLevel
        this.mFormatPrinter = builder.mFormatPrinter
        this.mCacheFactory = builder.mCacheFactory
        this.mExecutorService = builder.mExecutorService
    }

    @Singleton
    @Provides
    internal fun provideInterceptors(): MutableList<Interceptor>? {
        return mInterceptors
    }

    /**
     * 提供 BaseUrl,默认使用 <"https://api.github.com/">
     */
    @Singleton
    @Provides
    internal fun provideBaseUrl(): HttpUrl? {
        return mBaseUrl?.url() ?: (mApiUrl ?: HttpUrl.parse("https://api.github.com/"))
    }

    /**
     * 提供图片加载框架,默认使用 [Glide]
     */
    @Singleton
    @Provides
    internal fun provideImageLoaderStrategy(): BaseImageLoaderStrategy<*>? {
        return mLoaderStrategy
    }

    /**
     * 提供处理 Http 请求和响应结果的处理类
     */
    @Singleton
    @Provides
    internal fun provideGlobalHttpHandler(): GlobalHttpHandler? {
        return mHandler
    }

    /**
     * 提供缓存文件
     */
    @Singleton
    @Provides
    internal fun provideCacheFile(): File {
        return mCacheFile ?: FileUtils.getCacheFile()
    }

    /**
     * 提供处理 RxJava 错误的管理器的回调
     */
    @Singleton
    @Provides
    internal fun provideResponseErrorListener(): ResponseErrorListener {
        return mErrorListener ?: ResponseErrorListener.EMPTY
    }

    @Singleton
    @Provides
    internal fun provideRetrofitConfiguration(): ClientModule.RetrofitConfiguration? {
        return mRetrofitConfiguration
    }

    @Singleton
    @Provides
    internal fun provideOkHttpConfiguration(): ClientModule.OkHttpConfiguration? {
        return mOkHttpConfiguration
    }

    @Singleton
    @Provides
    internal fun provideRxCacheConfiguration(): ClientModule.RxCacheConfiguration? {
        return mRxCacheConfiguration
    }

    @Singleton
    @Provides
    internal fun provideGsonConfiguration(): AppModule.GsonConfiguration? {
        return mGsonConfiguration
    }

    @Singleton
    @Provides
    internal fun providePrintHttpLogLevel(): RequestInterceptor.Level {
        return mPrintHttpLogLevel ?: RequestInterceptor.Level.NONE
    }

    @Singleton
    @Provides
    internal fun provideFormatPrinter(): FormatPrinter {
        return mFormatPrinter ?: DefaultFormatPrinter()
    }

    @Singleton
    @Provides
    internal fun provideCacheFactory(application: Application): Cache.Factory<String, Any> {
        return mCacheFactory ?: object : Cache.Factory<String, Any> {
            override fun build(type: CacheType): Cache<String, Any> {
                //若想自定义 LruCache 的 size, 或者不想使用 LruCache, 想使用自己自定义的策略
                //使用 GlobalConfigModule.Builder#cacheFactory() 即可扩展
                return when (type.getCacheTypeId()) {
                    //Activity、Fragment 以及 Extras 使用 IntelligentCache (具有 LruCache 和 可永久存储数据的 Map)
                    CacheType.EXTRAS_TYPE_ID, CacheType.ACTIVITY_CACHE_TYPE_ID, CacheType.FRAGMENT_CACHE_TYPE_ID
                    -> IntelligentCache(type.calculateCacheSize(application))
                    //其余使用 LruCache (当达到最大容量时可根据 LRU 算法抛弃不合规数据)
                    else -> LruCache(type.calculateCacheSize(application))
                }
            }
        }
    }

    /**
     * 返回一个全局公用的线程池,适用于大多数异步需求。
     * 避免多个线程池创建带来的资源消耗。
     *
     * @return [Executor]
     */
    @Singleton
    @Provides
    internal fun provideExecutorService(): ExecutorService {
        return mExecutorService ?: ThreadPoolExecutor(
                0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
                SynchronousQueue(), Util.threadFactory("Fly Executor", false)
        )
    }

    class Builder {
        var mApiUrl: HttpUrl? = null
        var mBaseUrl: BaseUrl? = null
        var mImageLoaderStrategy: BaseImageLoaderStrategy<*>? = null
        var mHttpHandler: GlobalHttpHandler? = null
        var mInterceptors: MutableList<Interceptor>? = null
        var mResponseErrorListener: ResponseErrorListener? = null
        var mCacheFile: File? = null
        var mRetrofitConfiguration: ClientModule.RetrofitConfiguration? = null
        var mOkHttpConfiguration: ClientModule.OkHttpConfiguration? = null
        var mRxCacheConfiguration: ClientModule.RxCacheConfiguration? = null
        var mGsonConfiguration: AppModule.GsonConfiguration? = null
        var mPrintHttpLogLevel: RequestInterceptor.Level? = null
        var mFormatPrinter: FormatPrinter? = null
        var mCacheFactory: Cache.Factory<String, Any>? = null
        var mExecutorService: ExecutorService? = null

        fun baseurl(baseUrl: String): Builder {//基础url
            if (TextUtils.isEmpty(baseUrl)) {
                throw NullPointerException("BaseUrl can not be empty")
            }
            this.mApiUrl = HttpUrl.parse(baseUrl)
            return this
        }

        fun baseurl(baseUrl: BaseUrl): Builder {
            this.mBaseUrl = Preconditions.checkNotNull(baseUrl, BaseUrl::class.java.canonicalName!! + "can not be null.")
            return this
        }

        fun imageLoaderStrategy(loaderStrategy: BaseImageLoaderStrategy<*>): Builder {//用来请求网络图片
            this.mImageLoaderStrategy = loaderStrategy
            return this
        }

        fun globalHttpHandler(handler: GlobalHttpHandler): Builder {//用来处理http响应结果
            this.mHttpHandler = handler
            return this
        }

        fun addInterceptor(interceptor: Interceptor): Builder {//动态添加任意个interceptor
            if (mInterceptors == null) {
                mInterceptors = ArrayList()
            }
            this.mInterceptors!!.add(interceptor)
            return this
        }

        fun responseErrorListener(listener: ResponseErrorListener): Builder {//处理所有RxJava的onError逻辑
            this.mResponseErrorListener = listener
            return this
        }

        fun cacheFile(cacheFile: File): Builder {
            this.mCacheFile = cacheFile
            return this
        }

        fun retrofitConfiguration(retrofitConfiguration: ClientModule.RetrofitConfiguration): Builder {
            this.mRetrofitConfiguration = retrofitConfiguration
            return this
        }

        fun okhttpConfiguration(okhttpConfiguration: ClientModule.OkHttpConfiguration): Builder {
            this.mOkHttpConfiguration = okhttpConfiguration
            return this
        }

        fun rxCacheConfiguration(rxCacheConfiguration: ClientModule.RxCacheConfiguration): Builder {
            this.mRxCacheConfiguration = rxCacheConfiguration
            return this
        }

        fun gsonConfiguration(gsonConfiguration: AppModule.GsonConfiguration): Builder {
            this.mGsonConfiguration = gsonConfiguration
            return this
        }

        fun printHttpLogLevel(printHttpLogLevel: RequestInterceptor.Level): Builder {//是否让框架打印 Http 的请求和响应信息
            this.mPrintHttpLogLevel = Preconditions.checkNotNull(printHttpLogLevel, "The printHttpLogLevel can not be null, use RequestInterceptor.Level.NONE instead.")
            return this
        }

        fun formatPrinter(formatPrinter: FormatPrinter): Builder {
            this.mFormatPrinter = Preconditions.checkNotNull(formatPrinter, FormatPrinter::class.java.canonicalName!! + "can not be null.")
            return this
        }

        fun cacheFactory(cacheFactory: Cache.Factory<String, Any>): Builder {
            this.mCacheFactory = cacheFactory
            return this
        }

        fun executorService(executorService: ExecutorService): Builder {
            this.mExecutorService = executorService
            return this
        }

        fun build(): GlobalConfigModule {
            return GlobalConfigModule(this)
        }
    }

    @Module
    companion object {

        fun builder(): Builder {
            return Builder()
        }
    }
}