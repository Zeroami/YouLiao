package com.zeroami.commonlib.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;

import com.zeroami.commonlib.CommonLib;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：Bitmap工具类,获取Bitmap和对Bitmap的操作</p>
 */
public class LBitmapUtils {

    private LBitmapUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 图片压缩处理（使用Options的方法）
     *
     * @param options   图片参数
     * @param reqWidth  目标width
     * @param reqHeight 目标height
     * @return 改变后的options
     */
    public static BitmapFactory.Options calculateInSampleSize(
            final BitmapFactory.Options options, final int reqWidth,
            final int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width
                    / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio
                    : widthRatio;
        }
        // 设置压缩比例
        options.inSampleSize = inSampleSize;
        return options;
    }

    /**
     * 从流中获取Bitmap
     *
     * @param resId     资源id
     * @param reqWidth  目标width
     * @param reqHeight 目标height
     * @return
     */
    public static Bitmap getBitmapFromResource(int resId,
                                               int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(CommonLib.getContext().getResources(), resId, options);
        options = calculateInSampleSize(options, reqWidth,
                reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(CommonLib.getContext().getResources(), resId, options);

    }

    /**
     * 从文件中获取Bitmap
     *
     * @param path  文件路径
     * @param reqWidth  目标width
     * @param reqHeight 目标height
     * @return
     */
    public static Bitmap getBitmapFromFile(String path, int reqWidth,
                                           int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * 从数组中获取Bitmap
     *
     * @param data      字节数组
     * @param reqWidth  目标width
     * @param reqHeight 目标height
     * @return
     */
    public static Bitmap getBitmapFromByteArray(byte[] data, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        options = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }

    /**
     * 把bitmap转化为bytes
     *
     * @param bitmap
     * @return
     */
    public static byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 从View获取Bitmap
     *
     * @param view view
     * @return bitmap
     */
    public static Bitmap getBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.layout(view.getLeft(), view.getTop(), view.getRight(),
                view.getBottom());
        view.draw(canvas);
        return bitmap;
    }

    /**
     * 将Drawable转化为Bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap getBitmapFromDrawable(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable
                .getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }


    /**
     * 获取圆角Bitmap
     *
     * @param bitmap
     * @param radius 圆角半径
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float radius) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        canvas.drawRoundRect(rectF, radius, radius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * 获取圆形Bitmap
     *
     * @param bitmap
     * @return
     */
    public static Bitmap getCircleBitmap(Bitmap bitmap) {
        int width = Math.min(bitmap.getWidth(), bitmap.getHeight());
        Bitmap output = Bitmap.createBitmap(width,
                width, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, width, width);
        RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        canvas.drawCircle(width * 1.0f / 2, width * 1.0f / 2, width * 1.0f / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, -(bitmap.getWidth() - width) * 1.0f / 2, -(bitmap.getHeight() - width) * 1.0f / 2, paint);
        return output;
    }


    /**
     * 缩放图片
     *
     * @param bitmap
     * @param targetWidth  目标宽度
     * @param targetHeight 目标高度
     * @return
     */
    public static Bitmap zoomImage(Bitmap bitmap, int targetWidth, int targetHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidht = ((float) targetWidth / width);
        float scaleHeight = ((float) targetHeight / height);
        matrix.postScale(scaleWidht, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                matrix, true);
        return newbmp;
    }

    /**
     * 压缩图片大小
     *
     * @param bitmap
     * @param kb     压缩的最大值，单位为kb
     * @return
     */
    public static Bitmap compressImage(Bitmap bitmap, int kb) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > kb) { // 循环判断如果压缩后图片是否大于多少kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap output = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return output;
    }

    /**
     * 将彩色图转换为灰度图
     *
     * @param bitmap
     * @return
     */
    public static Bitmap convertGreyImage(Bitmap bitmap) {
        int width = bitmap.getWidth(); // 获取位图的宽
        int height = bitmap.getHeight(); // 获取位图的高

        int[] pixels = new int[width * height]; // 通过位图的大小创建像素点数组

        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = 0xFF << 24;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];

                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);

                grey = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
                grey = alpha | (grey << 16) | (grey << 8) | grey;
                pixels[width * i + j] = grey;
            }
        }
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        result.setPixels(pixels, 0, width, 0, 0, width, height);
        return result;
    }

    /**
     * 保存图片到本地
     * @param bitmap
     * @param dir
     * @param name
     * @param isScan 是否插入系统媒体库
     * @return
     */
    public static boolean saveImage(Bitmap bitmap, String dir, String name, boolean isScan) {
        File path = new File(dir);
        if (!path.exists()) {
            path.mkdirs();
        }
        File file = new File(path + "/" + name);
        if (file.exists()) {
            file.delete();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return true;
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100,
                    fileOutputStream);
            fileOutputStream.flush();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 其次把文件插入到系统图库
        if (isScan) {
            try {
                MediaStore.Images.Media.insertImage(CommonLib.getContext().getContentResolver(),
                        file.getAbsolutePath(), name, null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // 最后通知图库更新
            CommonLib.getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file)));
        }

        return true;
    }

    /**
     * 获取当前屏幕截图，包含状态栏，但是获取不到状态栏内容
     * @param activity
     * @return
     */
    public static Bitmap takeScreenShot(Activity activity) {
        //通过window的源码可以看出：检索顶层窗口的装饰视图，可以作为一个窗口添加到窗口管理器
        View view = activity.getWindow().getDecorView();
        //启用或禁用绘图缓存
        view.setDrawingCacheEnabled(true);
        //创建绘图缓存
        view.buildDrawingCache();
        //拿到绘图缓存
        Bitmap bitmap = view.getDrawingCache();

        int width = LDisplayUtils.getScreenWidth();
        int height = LDisplayUtils.getScreenHeight();

        Bitmap bp = null;
        bp = Bitmap.createBitmap(bitmap, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;
    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     * @param activity
     * @return
     */
    public static Bitmap takeScreenShotWithoutStatusBar(Activity activity) {
        //通过window的源码可以看出：检索顶层窗口的装饰视图，可以作为一个窗口添加到窗口管理器
        View view = activity.getWindow().getDecorView();
        //启用或禁用绘图缓存
        view.setDrawingCacheEnabled(true);
        //创建绘图缓存
        view.buildDrawingCache();
        //拿到绘图缓存
        Bitmap bitmap = view.getDrawingCache();

        //状态栏高度
        int statusBarHeight = LDisplayUtils.getStatusHeight();
        int width = LDisplayUtils.getScreenWidth();
        int height = LDisplayUtils.getScreenHeight();

        Bitmap bp = null;
        bp = Bitmap.createBitmap(bitmap, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;
    }

    /**
     * 获取视频的缩略图
     * 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
     * @param videoPath 视频的路径
     * @param width 指定输出视频缩略图的宽度
     * @param height 指定输出视频缩略图的高度度
     * @param kind 参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
     *            其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
     * @return 指定大小的视频缩略图
     */
    public static Bitmap getVideoThumbnail(String videoPath, int width, int height,
                                     int kind) {
        Bitmap bitmap = null;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

}
