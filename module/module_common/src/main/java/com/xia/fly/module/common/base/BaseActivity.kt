package com.xia.fly.module.common.base

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import butterknife.ButterKnife
import butterknife.Unbinder
import com.blankj.utilcode.util.ClickUtils
import com.xia.fly.mvp.BaseMvpPresenter
import com.xia.fly.mvp.BaseMvpView
import com.xia.fly.ui.activities.FlySupportActivity

/**
 * @author weixia
 * @date 2019/7/16.
 */
abstract class BaseActivity<P : BaseMvpPresenter<BaseMvpView<P>>> : FlySupportActivity<P>() {
    private var mUnbinder: Unbinder? = null
    private val mOnClickListener = View.OnClickListener { view -> onWidgetClick(view) }

    protected abstract fun onWidgetClick(view: View)

    override fun onBindAny(view: View) {
        mUnbinder = ButterKnife.bind(this, view)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mUnbinder != null && mUnbinder !== Unbinder.EMPTY) {
            try {
                //fix Bindings already cleared
                mUnbinder!!.unbind()
            } catch (ignored: IllegalStateException) {
            }
            mUnbinder = null
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val view = currentFocus
            if (isShouldHideKeyboard(view, ev, onCallback = { editText ->
                        editText.isFocusable = false
                    })) {
                val iBinder = view?.windowToken ?: return super.dispatchTouchEvent(ev)
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(iBinder, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun isShouldHideKeyboard(view: View?, event: MotionEvent,
                                     onCallback: (editText: EditText) -> Unit): Boolean {
        if (view is EditText) {
            onCallback(view)

            val l = intArrayOf(0, 0)
            view.getLocationInWindow(l)
            val left = l[0]
            val top = l[1]
            val bottom = top + view.height
            val right = left + view.width
            return !(event.x > left && event.x < right
                    && event.y > top && event.y < bottom)
        }
        return false
    }

    protected fun applyWidgetClickListener(vararg views: View?) {
        ClickUtils.applySingleDebouncing(views, mOnClickListener)
    }
}
