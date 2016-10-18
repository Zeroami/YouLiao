package com.zeroami.commonlib.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.GlideModule;
import com.zeroami.commonlib.utils.LFileUtils;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：默认的Glide配置</p>
 */
public class LDefaultGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {

        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);

        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();

        int customMemoryCacheSize = (int) (1.2 * defaultMemoryCacheSize);
        int customBitmapPoolSize = (int) (1.2 * defaultBitmapPoolSize);

        builder.setMemoryCache(new LruResourceCache(customMemoryCacheSize));
        builder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));

        if (LFileUtils.checkSDCardAvailable()) {
            builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, "image_cache", 100 * 1024 * 1024));
        } else {
            builder.setDiskCache(new InternalCacheDiskCacheFactory(context, "image_cache", 100 * 1024 * 1024));
        }


    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        // nothing to do here
    }
}