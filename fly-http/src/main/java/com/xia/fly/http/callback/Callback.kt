package com.xia.fly.http.callback

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import io.reactivex.disposables.Disposable
import okhttp3.ResponseBody
import java.lang.ref.WeakReference

/**
 * @author xia
 * @date 2018/7/27.
 */
@Suppress("UNUSED_PARAMETER")
abstract class Callback<T>(private val lifecycleOwner: LifecycleOwner) {
    private var mContext: WeakReference<Context>? = null

    init {
        var context: Context? = null
        if (lifecycleOwner is AppCompatActivity) {
            context = lifecycleOwner
        } else if (lifecycleOwner is Fragment) {
            context = lifecycleOwner.context
        }
        context?.let {
            mContext = WeakReference(it)
        }
    }

    fun getContext(): Context? {
        return mContext?.get()
    }

    fun getLifecycleOwner(): LifecycleOwner {
        return lifecycleOwner
    }

    fun onSubscribe(d: Disposable) {}

    fun onError(e: Throwable) {}

    fun onComplete() {}

    fun inProgress(progress: Float, total: Long) {}

    @Throws(Exception::class)
    abstract fun parseNetworkResponse(responseBody: ResponseBody)

    abstract fun onResponse(response: T?)
}
