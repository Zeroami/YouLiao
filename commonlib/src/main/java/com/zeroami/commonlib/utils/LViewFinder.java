package com.zeroami.commonlib.utils;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：findViewById 替代工具类</p>
 */
public class LViewFinder {

    private LFindWrapper mWapper;
    private SparseArray<View> mViews;

    public LViewFinder(View view) {
        mWapper = new LViewWrapper(view);
        mViews = new SparseArray<>();
    }

    public LViewFinder(Window window) {
        mWapper = new LWindowWrapper(window);
        mViews = new SparseArray<>();
    }

    public LViewFinder(Activity activity) {
        this(activity.getWindow());
    }

    /**
     * 根据id获取View
     *
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public <V extends View> V find(int id) {
        if (mViews.get(id) == null) {
            mViews.put(id, mWapper.findViewById(id));
        }
        return (V) mViews.get(id);
    }

    /**
     * 指定获取ImageView
     *
     * @param id
     * @return
     */
    public ImageView imageView(int id) {
        return find(id);
    }

    /**
     * 指定获取CompundButton
     *
     * @param id
     * @return
     */
    public CompoundButton compoundButton(int id) {
        return find(id);
    }

    /**
     * 指定获取TextView
     *
     * @param id
     * @return
     */
    public TextView textView(int id) {
        return find(id);
    }

    /**
     * 根据id找到View并设置内容
     *
     * @param id
     * @param content
     * @return
     */
    public LViewFinder setText(int id, CharSequence content) {
        textView(id).setText(content);
        return this;
    }

    /**
     * 根据id找到View并设置内容
     *
     * @param id
     * @param stringId
     * @return
     */
    public LViewFinder setText(int id, int stringId) {
        return setText(id, mWapper.getResources().getString(stringId));
    }

    /**
     * 根据id找到View并设置内容
     *
     * @param id
     * @param bitmap
     * @return
     */
    public LViewFinder setImageBitmap(int id, Bitmap bitmap) {
        imageView(id).setImageBitmap(bitmap);
        return this;
    }

    /**
     * 根据id找到View并设置内容
     *
     * @param id
     * @param drawable
     * @return
     */
    public LViewFinder setImageDrawable(int id, Drawable drawable) {
        imageView(id).setImageDrawable(drawable);
        return this;
    }

    /**
     * 根据id找到View并设置内容
     *
     * @param id
     * @param imageId
     * @return
     */
    public LViewFinder setImageResource(int id, int imageId) {
        imageView(id).setImageResource(imageId);
        return this;
    }

    /**
     * 根据id找到View并设置内容
     *
     * @param id
     * @param checked
     * @return
     */
    public LViewFinder setChecked(int id, boolean checked) {
        compoundButton(id).setChecked(checked);
        return this;
    }

    /**
     * 根据id找到View并设置内容
     *
     * @param id
     * @param visibility
     * @return
     */
    public LViewFinder setVisibility(int id, int visibility) {
        find(id).setVisibility(visibility);
        return this;
    }


    /**
     * 根据id找到View并设置监听器
     *
     * @param id
     * @param listener
     * @return
     */
    public LViewFinder onClick(int id, OnClickListener listener) {
        find(id).setOnClickListener(listener);
        return this;
    }

    /**
     * 根据一组id找到View并设置监听器
     *
     * @param listener
     * @param ids
     */
    public LViewFinder onClick(OnClickListener listener, int... ids) {
        for (int id : ids)
            find(id).setOnClickListener(listener);
        return this;
    }


    /**
     * 根据id找到View并设置监听器
     *
     * @param id
     * @param listener
     * @return
     */
    public LViewFinder onCheckChanged(int id,
                                      OnCheckedChangeListener listener) {
        compoundButton(id).setOnCheckedChangeListener(listener);
        return this;
    }


    /**
     * 根据一组id找到View并设置监听器
     *
     * @param ids
     * @param listener
     */
    public LViewFinder onCheckChanged(OnCheckedChangeListener listener,
                                      int... ids) {
        for (int id : ids)
            compoundButton(id).setOnCheckedChangeListener(listener);
        return this;
    }


    /**
     * <p>作者：Zeroami</p>
     * <p>邮箱：826589183@qq.com</p>
     * <p>描述：Find的基本接口</p>
     */
    private interface LFindWrapper {
        View findViewById(int id);

        Resources getResources();
    }

    /**
     * <p>作者：Zeroami</p>
     * <p>邮箱：826589183@qq.com</p>
     * <p>描述：WindowWrapper子类</p>
     */
    private static class LWindowWrapper implements LFindWrapper {
        private Window window;

        LWindowWrapper(Window window) {
            this.window = window;
        }

        public View findViewById(int id) {
            return window.findViewById(id);
        }

        public Resources getResources() {
            return window.getContext().getResources();
        }
    }

    /**
     * <p>作者：Zeroami</p>
     * <p>邮箱：826589183@qq.com</p>
     * <p>描述：ViewWapper子类</p>
     */
    private static class LViewWrapper implements LFindWrapper {

        private View view;

        LViewWrapper(View view) {
            this.view = view;
        }

        public View findViewById(int id) {
            return view.findViewById(id);
        }

        public Resources getResources() {
            return view.getResources();
        }
    }

}