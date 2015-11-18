package com.bzh.mysimplefresco.lib;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.ImageRequest;

import java.io.File;

/**
 * ========================================================== <br>
 * <b>版权</b>：　　　音悦台 版权所有(c) 2015 <br>
 * <b>作者</b>：　　　别志华 biezhihua@163.com<br>
 * <b>创建日期</b>：　2015/11/17 13:38 <br>
 * <b>描述</b>：　　　Fresco相关的工具类<br>
 * <b>版本</b>：　   V1.0 <br>
 * <b>修订历史</b>：　<br>
 * ========================================================== <br>
 */
public class FrescoUtils {
    private static final String TAG = "FrescoUtils";

    /**
     * 是否有磁盘缓存
     */
    public static boolean isThereDiskCache(String uri) {
        final CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(uri));
        final BinaryResource resource = ImagePipelineFactory.getInstance().getMainDiskStorageCache().getResource(cacheKey);
        if (resource != null) {
            File file = ((FileBinaryResource) resource).getFile();
            if (file.exists()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 从本地获取File,此url必须是原始的url未经过滤的
     */
    public static Bitmap getBitmapByUrlFromFile(String url) {
        Bitmap mBitmap = null;
        ImageRequest imageRequest = ImageRequest.fromUri(url);
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(imageRequest);
        ImagePipelineFactory instance = ImagePipelineFactory.getInstance();
        BinaryResource resource = instance.getMainDiskStorageCache().getResource(cacheKey);

        if (resource != null) {
            File file = ((FileBinaryResource) resource).getFile();
            if (file.exists()) {
                mBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            }
        }
        return mBitmap;
    }
}
