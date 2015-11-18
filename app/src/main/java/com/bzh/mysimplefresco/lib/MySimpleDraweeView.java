package com.bzh.mysimplefresco.lib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.NinePatch;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.bzh.mysimplefresco.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;

import java.io.File;


/**
 * ========================================================== <br>
 * <b>版权</b>：　　　别志华 版权所有(c) 2015 <br>
 * <b>作者</b>：　　　别志华 biezhihua@163.com<br>
 * <b>创建日期</b>：　2015/7/12 16:20 <br>
 * <b>版本</b>：　   V5.0 <br>
 * <b>修订历史</b>：　<br>
 * ========================================================== <br>
 */
public class MySimpleDraweeView extends SimpleDraweeView {

    // 默认的宽高比例
    public static final float DEF_RATIO = 2.0f;

    private Context mContext;

    private Postprocessor mPostProcessor;               // 后处理器（在图片下载完成后对图片进行处理-截取/缩放等）
    private ControllerListener mControllerListener;     // 下载监听器
    private ResizeOptions mResizeOptions;               // 图片缩放，优先级非常高；缩放的尺寸并不是按照指定的尺寸，而是根据内部来计算出一个合适的值；
    private ScalingUtils.ScaleType mDraweeViewScaleType;// 图片缩放类型，默认为CENTER_CROP


    private Drawable mOverlay;                          // 覆盖物-GIF/长图/数字
    private Drawable mProgressBar;                      // 加载进度
    private Drawable mPlaceholderDrawable;              // 加载背景
    private Drawable mGifChartOverlay;                  // GIF的覆盖物图
    private Drawable mLongChartOverlay;                 // 长图的覆盖物图
    private Drawable mNumberChartOverlay;               // 数字的覆盖物图

    private boolean mAutoPlayAnimations = false;        // 是否自动播放GIF图-不自动播放
    private boolean isProcessPNG2TargetColor;           // 是否处理PNG图片的透明背景为指定颜色
    private boolean isCutGif;                           // 是否裁剪GIF

    private double mHeightRatio;                        // 宽高比例

    private int mTargetImageSize = -1;                  // 指定的图片尺寸
    private int mTargetColor = Color.WHITE;             // 指定的PNG图片的透明背景颜色为白色
    private int mFadeDuration = 0;                      // 延迟加载毫秒数-0

    private ImageRequest.ImageType mImageType;          // 图片类型-默认
    private PictureType mPictureType = PictureType.JPEG;// 图片类型-默认JPEG
    private ImageRequest.RequestLevel mLowestPermittedRequestLevel;// 图片加载的请求类型-默认FULL_FETCH

    private String uriPathTag;                          // 加载图片的TAG，用于不重复加载图片

    public MySimpleDraweeView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
        init(context);
    }

    public MySimpleDraweeView(Context context) {
        super(context);
        init(context);
    }

    public MySimpleDraweeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context);
    }

    public MySimpleDraweeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        this.mContext = context;
        mPostProcessor = new MyBasePostProcessor(this);
        mImageType = ImageRequest.ImageType.DEFAULT;
        mControllerListener = new MyBaseControllerListener();
        mDraweeViewScaleType = ScalingUtils.ScaleType.CENTER_CROP;
        mLowestPermittedRequestLevel = ImageRequest.RequestLevel.FULL_FETCH;
        mPlaceholderDrawable = new ColorDrawable(Color.GRAY);
        mProgressBar = new LoadingProgressDrawable(getContext());
    }

    // =======start推荐使用的方法=======================

    /**
     * 是否自动播放GIF，默认False
     */
    public void setAutoPlayAnimations(boolean mAutoPlayAnimations) {
        this.mAutoPlayAnimations = mAutoPlayAnimations;
    }

    /**
     * 是否设置GIF标识
     */
    public void setGifChartIdentify(boolean isShowGifIdentify) {
        if (isShowGifIdentify) {
            mGifChartOverlay = mContext.getResources().getDrawable(R.mipmap.identify_gif);
        } else {
            mGifChartOverlay = null;
        }
    }

    /**
     * 设置长图标识
     */
    public void setLongChartIdentify(int imageWidth, int imageHeight) {
        setLongChartIdentify(imageHeight > imageWidth * DEF_RATIO);
    }

    /**
     * 是否设置长图标识
     */
    public void setLongChartIdentify(boolean isLongChartIdentify) {
        if (isLongChartIdentify) {
            mLongChartOverlay = mContext.getResources().getDrawable(R.mipmap.identify_long);
        } else {
            mLongChartOverlay = null;
        }
    }

    /**
     * 设置数字标识
     */
    public void setNumberChartIdentify(int number) {
        if (number > 1) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.identify_number);
            byte[] chunk = bitmap.getNinePatchChunk();
            if (NinePatch.isNinePatchChunk(chunk)) {
                mNumberChartOverlay = new MyNinePatchDrawable(getResources(), bitmap, chunk, NinePatchChunk.deserialize(chunk).mPaddings, null, number + "");
            } else {
                mNumberChartOverlay = null;
            }
        }
    }

    public void setControllerListener(ControllerListener mControllerListener) {
        this.mControllerListener = mControllerListener;
    }

    public void setPostProcessor(Postprocessor mPostProcessor) {
        this.mPostProcessor = mPostProcessor;
    }

    public void setDraweeViewUrl(String url) {
        setDraweeViewUri(Uri.parse(url));
    }

    public void setDraweeViewResId(int resId) {
        setDraweeViewUri(getUriByResId(resId));
    }

    public void setDraweeViewUri(Uri uri) {

        uriPathTag = uri.toString();

        // 识别图片类型
        recognitionPictureType(uriPathTag);

        // 不重复加载图片
        if (isNotEqualsUriPath(uriPathTag)) {
            setHierarchy();
            setController(uri);
        }
    }


    public void setRoundDraweeViewUrl(String url) {
        setRoundDraweeViewUri(Uri.parse(url));
    }

    /**
     * 默认是否动
     */
    public void setRoundDraweeViewUrl(String url, boolean isAuto) {
        setAutoPlayAnimations(isAuto);
        setRoundDraweeViewUri(Uri.parse(url));
    }

    public void setRoundDraweeViewResId(int resId) {
        setRoundDraweeViewUri(getUriByResId(resId));
    }

    public void setRoundDraweeViewUri(Uri uri) {

        uriPathTag = uri.toString();

        recognitionPictureType(uriPathTag);

        if (isNotEqualsUriPath(uriPathTag)) {
            setRoundHierarchy();
            setController(uri);
        }
    }

    public void setWidthAndHeight(float width, float height) {
        float picRatio = height / width;
        if (picRatio >= DEF_RATIO) {
            picRatio = DEF_RATIO;
        }
        this.setHeightRatio(picRatio);
    }

    public void setMaxWidthLayoutParams(final int maxWidth, final int width, final int height) {
        float widthRatio = (float) width / (float) maxWidth;
        float tmpheight = ((float) height / widthRatio);

        float ratio = tmpheight / maxWidth;
        if (ratio > DEF_RATIO) {
            ratio = DEF_RATIO;
        }
        tmpheight = maxWidth * ratio;

        // 设置单张图片的布局
        ViewGroup.LayoutParams layoutparams = getLayoutParams();
        layoutparams.height = (int) tmpheight;
        layoutparams.width = maxWidth;
        setLayoutParams(layoutparams);
    }

    public void setResizeOptions(ResizeOptions resizeOptions) {
        this.mResizeOptions = resizeOptions;
    }

    public void setProgressBar(Drawable mProgressBar) {
        this.mProgressBar = mProgressBar;
    }

    public void setDraweeViewScaleType(ScalingUtils.ScaleType mDraweeViewScaleType) {
        this.mDraweeViewScaleType = mDraweeViewScaleType;
    }

    public void setPlaceholderDrawable(Drawable placeholderDrawable) {
        this.mPlaceholderDrawable = placeholderDrawable;
    }

    public boolean isProcessPNG2TargetColor() {
        return isProcessPNG2TargetColor;
    }

    public void setIsProcessPNG2TargetColor(boolean isProcessPNG2TargetColor) {
        this.isProcessPNG2TargetColor = isProcessPNG2TargetColor;
    }

    public ImageRequest.ImageType getImageType() {
        return mImageType;
    }

    public void setImageType(ImageRequest.ImageType imageType) {
        this.mImageType = imageType;
    }

    public int getmTargetImageSize() {
        return mTargetImageSize;
    }

    public void setmTargetImageSize(int mTargetImageSize) {
        this.mTargetImageSize = mTargetImageSize;
    }

    public int getmTargetColor() {
        return mTargetColor;
    }

    public void setmTargetColor(int mTargetColor) {
        this.mTargetColor = mTargetColor;
    }

    public void setLowestPermittedRequestLevel(ImageRequest.RequestLevel mLowestPermittedRequestLevel) {
        this.mLowestPermittedRequestLevel = mLowestPermittedRequestLevel;
    }

    public PictureType getmPictureType() {
        return mPictureType;
    }

    /**
     * 判定Tag和Url是否相等，相等代表图片已经加载过，不需要从新加载
     */
    public boolean isNotEqualsUriPath(String imgUrl) {
        if (TextUtils.isEmpty(imgUrl) || TextUtils.isEmpty(this.getTag(R.id.uriPath) + "")) {
            return false;
        }
        return !(this.getTag(R.id.uriPath) + "").equals(imgUrl);
    }

    /**
     * 是否剪切GIF的第一帧
     */
    public void setIsCutGif(boolean isCutGif) {
        this.isCutGif = isCutGif;
    }

    // ========end推荐使用的方法 ==========================

    private void setHierarchy() {
        if (mContext == null) {
            throw new IllegalArgumentException("Context is not null");
        }
        setHierarchy(getGenericDraweeHierarchy(mContext, this));
    }

    private void setHierarchy(ScalingUtils.ScaleType actualImageScaleType) {
        if (mContext == null) {
            throw new IllegalArgumentException("Context is not null");
        }
        mDraweeViewScaleType = actualImageScaleType;
        setHierarchy(getGenericDraweeHierarchy(mContext, this));
    }

    private void setRoundHierarchy() {
        if (mContext == null) {
            throw new IllegalArgumentException("Context is not null");
        }
        setHierarchy(getRoundGenericDraweeHierarchy(mContext, this));
    }

    private void setController(String uri) {
        try {
            setController(Uri.parse(uri));
        } catch (Exception e) {
            setController(Uri.parse(""));
        }
    }

    private void setController(int resId) {
        setController(getUriByResId(resId));
    }

    private void setController(Uri uri) {
        setController(getDraweeController(getImageRequest(this, uri), this));
    }

    private GenericDraweeHierarchy getGenericDraweeHierarchy(Context context, MySimpleDraweeView draweeView) {
        return new GenericDraweeHierarchyBuilder(context.getResources())
                .setFadeDuration(draweeView.getFadeDuration())
                .setOverlay(draweeView.getmOverlay())
                .setActualImageScaleType(draweeView.getDraweeViewScaleType())
                .setProgressBarImage(draweeView.getProgressBar(), ScalingUtils.ScaleType.CENTER_INSIDE)
                .setPlaceholderImage(draweeView.getPlaceholderDrawable(), ScalingUtils.ScaleType.CENTER_CROP)
                .build();
    }

    private GenericDraweeHierarchy getRoundGenericDraweeHierarchy(Context context, MySimpleDraweeView draweeView) {
        return new GenericDraweeHierarchyBuilder(context.getResources())
                .setPlaceholderImage(draweeView.getPlaceholderDrawable())
                .setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP)
//                .setProgressBarImage(draweeView.getProgressBar(), ScalingUtils.ScaleType.CENTER_INSIDE)
                .setRoundingParams(RoundingParams.asCircle())
                .build();
    }

    private ImageRequest getImageRequest(MySimpleDraweeView view, Uri uri) {
        switch (mPictureType) {
            case JPEG:
            case JPG:
            case PNG:
                return ImageRequestBuilder.newBuilderWithSource(uri)
                        .setLowestPermittedRequestLevel(view.getLowestPermittedRequestLevel())
                        .setPostprocessor(view.getPostProcessor())//修改图片
                        .setResizeOptions(view.getResizeOptions())
                        .setAutoRotateEnabled(true)
                        .setLocalThumbnailPreviewsEnabled(true)
                        .setProgressiveRenderingEnabled(false)
                        .build();
            case GIF:
                // 对GIF图，Fresco没有处理，自己手动处理
                // 1，解析图片宽高，判断是否需要处理
                // 2，不需要处理，直接返回；需要处理则处理后，生成一个拷贝文件，并替换url
                if (uri.getScheme().toLowerCase().contains("file") && mPictureType.equals(PictureType.GIF) && isCutGif) {
                    uri = (new Uri.Builder()).scheme("file").path(Utils.getCopyFile(new File(uri.getPath())).getAbsolutePath()).build();
                    return ImageRequestBuilder.newBuilderWithSource(uri)
                            .setLowestPermittedRequestLevel(view.getLowestPermittedRequestLevel())
                            .setPostprocessor(view.getPostProcessor())//修改图片
                            .setResizeOptions(view.getResizeOptions())
                            .setAutoRotateEnabled(true)
                            .build();
                }
                return ImageRequestBuilder.newBuilderWithSource(uri)
                        .setLowestPermittedRequestLevel(view.getLowestPermittedRequestLevel())
                        .setAutoRotateEnabled(true)
                        .build();
        }
        throw new RuntimeException("must have a ImageRequest");
    }


    private DraweeController getDraweeController(ImageRequest imageRequest, MySimpleDraweeView view) {
        return Fresco.newDraweeControllerBuilder().setAutoPlayAnimations(view.getAutoPlayAnimations())//自动播放图片动画
                .setControllerListener(view.getControllerListener())
                .setImageRequest(imageRequest)
                .setOldController(view.getController())
                .build();
    }

    private void setHeightRatio(double ratio) {
        if (ratio != mHeightRatio) {
            mHeightRatio = ratio;
            requestLayout();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mHeightRatio > 0.0) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = (int) (width * mHeightRatio);
            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    private static Uri getUriByResId(int resId) {
        return (new Uri.Builder()).scheme("res").path(String.valueOf(resId)).build();
    }

    private boolean getAutoPlayAnimations() {
        return mAutoPlayAnimations;
    }

    /**
     * 获取遮盖物
     * 数字 > GIF > 长图
     */
    private Drawable getmOverlay() {
        if (null != mNumberChartOverlay) {
            return mNumberChartOverlay;
        } else if (null != mGifChartOverlay) {
            return mGifChartOverlay;
        } else if (null != mLongChartOverlay) {
            return mLongChartOverlay;
        }
        return new ColorDrawable(Color.TRANSPARENT);
    }

    private ControllerListener getControllerListener() {
        return mControllerListener;
    }

    private Postprocessor getPostProcessor() {
        return mPostProcessor;
    }

    private ResizeOptions getResizeOptions() {
        return mResizeOptions;
    }

    private Drawable getProgressBar() {
        return mProgressBar;
    }

    private ScalingUtils.ScaleType getDraweeViewScaleType() {
        return mDraweeViewScaleType;
    }

    private int getFadeDuration() {
        return mFadeDuration;
    }

    private Drawable getPlaceholderDrawable() {
        return mPlaceholderDrawable;
    }

    private ImageRequest.RequestLevel getLowestPermittedRequestLevel() {
        return mLowestPermittedRequestLevel;
    }

    private void recognitionPictureType(String url) {
        if (url.toLowerCase().endsWith("gif")) {
            mPictureType = PictureType.GIF;
        } else if (url.toLowerCase().endsWith("jpeg")) {
            mPictureType = PictureType.JPEG;
        } else if (url.toLowerCase().endsWith("jpg")) {
            mPictureType = PictureType.JPG;
        } else if (url.toLowerCase().endsWith("png")) {
            mPictureType = PictureType.PNG;
        }
    }

    public class MyBaseControllerListener extends BaseControllerListener<ImageInfo> {
        @Override
        public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
            super.onFinalImageSet(id, imageInfo, animatable);
            MySimpleDraweeView.this.setTag(R.id.uriPath, uriPathTag);
        }

        @Override
        public void onFailure(String id, Throwable throwable) {
            super.onFailure(id, throwable);
        }
    }

    public enum PictureType {
        JPEG, JPG, PNG, GIF
    }

}
