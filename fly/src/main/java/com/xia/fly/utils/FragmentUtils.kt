package com.xia.fly.utils

import android.os.Bundle

import androidx.fragment.app.Fragment
import me.yokeyword.fragmentation.ISupportFragment

/**
 * @author xia
 * @date 2018/7/10.
 */
@Suppress("UNCHECKED_CAST")
object FragmentUtils {

    @JvmStatic
    fun <T : ISupportFragment> newInstance(cls: Class<*>): T? {
        return newInstance(cls, null)
    }

    @JvmStatic
    fun <T : ISupportFragment> newInstance(
            cls: Class<*>, bundle: Bundle?): T? {
        try {
            val t = cls.newInstance() as T
            if (bundle != null && !bundle.isEmpty) {
                (t as Fragment).arguments = bundle
                t.putNewBundle(bundle)
            }
            return t
        } catch (ignored: IllegalAccessException) {
        } catch (ignored: InstantiationException) {
        }
        return null
    }
}
