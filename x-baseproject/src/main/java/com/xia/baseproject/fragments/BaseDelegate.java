package com.xia.baseproject.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.xia.baseproject.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.ISupportFragment;

/**
 * @author xia
 * @date 2018/7/3.
 */
@SuppressWarnings("all")
public abstract class BaseDelegate extends AbstractSupportFragment {
    private Unbinder mUnbinder = null;

    public abstract int getLayoutId();

    public abstract void initData();

    public abstract void initView();

    public abstract void initEvent();

    protected boolean isLoadHeadView() {
        return true;
    }

    protected void onCreateHeadView(FrameLayout headContainer) {
    }

    protected void getBundleExtras(Bundle bundle) {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.base_layout_root_view, container, false);
        if (isLoadHeadView()) {
            final FrameLayout headContainer = rootView.findViewById(R.id.layout_root_view_head_container);
            onCreateHeadView(headContainer);
        }
        if (getLayoutId() > 0) {
            final FrameLayout contentContainer = rootView.findViewById(R.id.layout_root_view_content_container);
            View.inflate(getContext(), getLayoutId(), contentContainer);
        }
        mUnbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getBundle(getArguments());
        initData();
        initView();
        initEvent();
    }

    @Override
    public void onNewBundle(Bundle args) {
        super.onNewBundle(args);
        getBundle(args);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    private void getBundle(Bundle bundle) {
        if (bundle != null && !bundle.isEmpty()) {
            getBundleExtras(bundle);
        }
    }

    public static <T extends ISupportFragment> T newInstance(
            final Class<?> cls, final Bundle bundle) {
        try {
            final T t = (T) cls.newInstance();
            if (bundle != null && !bundle.isEmpty()) {
                ((Fragment) t).setArguments(bundle);
                t.putNewBundle(bundle);
            }
            return t;
        } catch (IllegalAccessException ignored) {
        } catch (java.lang.InstantiationException ignored) {
        }
        return null;
    }
}
