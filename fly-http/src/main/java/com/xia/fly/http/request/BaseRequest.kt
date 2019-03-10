package com.xia.fly.http.request

import com.xia.fly.http.api.RestService
import com.xia.fly.http.utils.ParamsUtils
import com.xia.fly.utils.FlyUtils
import io.reactivex.Observable
import okhttp3.ResponseBody
import java.io.File
import java.util.*

/**
 * @author xia
 * @date 2018/8/3.
 */
@Suppress("UNCHECKED_CAST", "unused")
abstract class BaseRequest<R : BaseRequest<R>>(@JvmField protected val mUrl: String) {
    @JvmField
    protected val mParams = LinkedHashMap<String, String>()
    @JvmField
    protected val mFileParams = LinkedHashMap<String, File>()

    fun addParam(key: String, value: String?): R {
        mParams[key] = value ?: ""
        return this as R
    }

    fun params(params: Map<String, String>?): R {
        if (params != null) {
            mParams.putAll(params)
            ParamsUtils.escapeParams(mParams)
        }
        return this as R
    }

    fun addFileParam(key: String, file: File): R {
        mFileParams[key] = file
        return this as R
    }

    fun fileParams(fileParams: Map<String, File>?): R {
        if (fileParams != null) {
            mFileParams.putAll(fileParams)
        }
        return this as R
    }

    protected abstract fun generateRequest(): Observable<ResponseBody>

    fun build(): RequestCall {
        return RequestCall(generateRequest())
    }

    companion object {

        @JvmField
        val mRestService = FlyUtils.getAppComponent().repositoryManager()
                .obtainRetrofitService(RestService::class.java)
    }
}