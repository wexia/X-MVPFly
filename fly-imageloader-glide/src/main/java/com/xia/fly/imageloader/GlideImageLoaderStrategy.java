package com.xia.fly.imageloader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.xia.fly.imageloader.integration.GlideApp;
import com.xia.fly.imageloader.integration.GlideRequest;
import com.xia.fly.imageloader.integration.GlideRequests;
import com.xia.fly.ui.imageloader.BaseImageLoaderStrategy;
import com.xia.fly.utils.Platform;
import com.xia.fly.utils.Preconditions;

import io.reactivex.schedulers.Schedulers;

/**
 * @author xia
 * @date 2018/9/17.
 */
public class GlideImageLoaderStrategy implements BaseImageLoaderStrategy<ImageConfigImpl>, GlideAppliesOptions {

    @SuppressWarnings("unchecked")
    @SuppressLint("CheckResult")
    @Override
    public void loadImage(Context ctx, ImageConfigImpl config) {
        Preconditions.checkNotNull(ctx, "Context is required");
        Preconditions.checkNotNull(config, "ImageConfigImpl is required");
        Preconditions.checkNotNull(config.getImageView(), "ImageView is required");

        final GlideRequests requests = GlideApp.with(ctx);
        final GlideRequest<Drawable> glideRequest = requests.load(config.getUrl());
        switch (config.getCacheStrategy()) {//缓存策略
            case 0:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.ALL);
                break;
            case 1:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.NONE);
                break;
            case 2:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                break;
            case 3:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.DATA);
                break;
            case 4:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
                break;
            default:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.ALL);
                break;
        }

        //是否使用淡入淡出过渡动画
        if (config.isCrossFade()) {
            glideRequest.transition(DrawableTransitionOptions.withCrossFade());
        }

        //是否将图片剪切为 CenterCrop
        if (config.isCenterCrop()) {
            glideRequest.centerCrop();
        }

        //是否将图片剪切为圆形
        if (config.isCircle()) {
            glideRequest.circleCrop();
        }

        //设置圆角大小
        if (config.isImageRadius()) {
            glideRequest.transform(new RoundedCorners(config.getImageRadius()));
        }

        //高斯模糊值, 值越大模糊效果越大(blurValue 建议设置为 15)
        if (config.isBlurImage()) {
            glideRequest.transform(new BlurTransformation(config.getBlurValue()));
        }

        //glide用它来改变图形的形状
        if (config.getTransformation() != null) {
            glideRequest.transform(config.getTransformation());
        }

        //设置占位符
        if (config.getPlaceholder() != 0) {
            glideRequest.placeholder(config.getPlaceholder());
        }

        //设置错误的图片
        if (config.getErrorPic() != 0) {
            glideRequest.error(config.getErrorPic());
        }

        //设置请求 url 为空图片
        if (config.getFallback() != 0) {
            glideRequest.fallback(config.getFallback());
        }

        if (config.getImageView() != null) {
            glideRequest.into(config.getImageView());
        } else if (config.getTarget() != null) {
            glideRequest.into(config.getTarget());
        }
    }

    @Override
    public void clear(final Context ctx, ImageConfigImpl config) {
        Preconditions.checkNotNull(ctx, "Context is required");
        Preconditions.checkNotNull(config, "ImageConfigImpl is required");

        if (config.getImageView() != null) {
            GlideApp.get(ctx).getRequestManagerRetriever().get(ctx).clear(config.getImageView());
        }

        if (config.getImageViews() != null && config.getImageViews().length > 0) {//取消在执行的任务并且释放资源
            for (ImageView imageView : config.getImageViews()) {
                GlideApp.get(ctx).getRequestManagerRetriever().get(ctx).clear(imageView);
            }
        }

        if (config.isClearDiskCache()) {//清除本地缓存
            Platform.post(Schedulers.io(), () -> GlideApp.get(ctx).clearDiskCache());
        }

        if (config.isClearMemory()) {//清除内存缓存
            Platform.post(() -> GlideApp.get(ctx).clearMemory());
        }
    }

    @Override
    public void applyGlideOptions(Context context, GlideBuilder builder) {
    }
}
