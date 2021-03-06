package com.xia.fly.mvp.nullobject

import androidx.annotation.UiThread
import com.xia.fly.mvp.common.IMvpPresenter
import com.xia.fly.mvp.common.IMvpView
import java.lang.ref.WeakReference
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @author weixia
 * @date 2019/3/19.
 */
abstract class MvpNullObjectBasePresenter<out V : IMvpView<*>> protected constructor() : IMvpPresenter<V> {
    private var mView: WeakReference<V>? = null
    private val mNullView: V

    init {
        try {
            // Scan the inheritance hierarchy until we reached MvpNullObjectBasePresenter
            var viewClass: Class<V>? = null
            var currentClass: Class<*>? = javaClass

            while (viewClass == null) {
                var genericSuperType: Type? = null
                if (currentClass != null) {
                    genericSuperType = currentClass.genericSuperclass
                }
                while (genericSuperType !is ParameterizedType) {
                    if (currentClass != null) {
                        // Scan inheritance tree until we find ParameterizedType which is probably a MvpSubclass
                        currentClass = currentClass.superclass
                        if (currentClass != null) {
                            genericSuperType = currentClass.genericSuperclass
                        }
                    }
                }

                val types = genericSuperType.actualTypeArguments
                for (type in types) {
                    val genericType = type as Class<*>
                    if (genericType.isInterface && isSubTypeOfMvpView(genericType)) {
                        @Suppress("UNCHECKED_CAST")
                        viewClass = genericType as Class<V>
                        break
                    }
                }
                // Continue with next class in inheritance hierarchy (see genericSuperType assignment at start of while loop)
                currentClass = currentClass!!.superclass
            }
            mNullView = NoOp.of(viewClass)
        } catch (t: Throwable) {
            throw IllegalArgumentException(
                    "The generic type <V extends MvpView> must be the first generic type argument of class "
                            + javaClass.simpleName
                            + " (per convention). Otherwise we can't determine which type of View this"
                            + " Presenter coordinates.", t)
        }

    }

    /**
     * Scans the interface inheritance hierarchy and checks if on the root is MvpView.class
     *
     * @param cls The leaf interface where to begin to scan
     * @return true if subtype of MvpView, otherwise false
     */
    private fun isSubTypeOfMvpView(cls: Class<*>): Boolean {
        if (cls == IMvpView::class.java) {
            return true
        }
        val superInterfaces = cls.interfaces
        for (superInterface in superInterfaces) {
            if (isSubTypeOfMvpView(superInterface)) {
                return true
            }
        }
        return false
    }

    @UiThread
    protected fun getV(): V {
        return mView?.get() ?: mNullView
    }

    @UiThread
    override fun attachView(view: @UnsafeVariance V) {
        this.mView = WeakReference(view)
    }

    @UiThread
    override fun detachView() {
        if (mView != null) {
            mView!!.clear()
            mView = null
        }
    }
}
