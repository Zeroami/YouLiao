package com.zeroami.youliao.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.zeroami.commonlib.utils.LT;
import com.zeroami.youliao.R;
import com.zeroami.youliao.config.Constant;

import java.util.List;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：一页表情Adapter</p>
 */
public class ExpressionPageAdapter extends PagerAdapter{

    private Context mContext;
    private List<List<Integer>> mExpressionPageList;

    public ExpressionPageAdapter(Context context,List<List<Integer>> expressionPageList){
        mContext = context;
        mExpressionPageList = expressionPageList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_expression_page,container,false);
        container.addView(view);
        initRecyclerView((RecyclerView)view.findViewById(R.id.rv_expression),mExpressionPageList.get(position));
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mExpressionPageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    private void initRecyclerView(RecyclerView recyclerView,List<Integer> expressionIdList) {
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, Constant.EXPRESS_COL_SIZE));
        recyclerView.setAdapter(new ExpressionAdapter(expressionIdList));
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                LT.show(i);
            }
        });
    }
    
    /**
     * <p>作者：Zeroami</p>
     * <p>邮箱：826589183@qq.com</p>
     * <p>描述：一个表情Adapter</p>
     */
    public class ExpressionAdapter extends BaseQuickAdapter<Integer>{

        public ExpressionAdapter(List<Integer> data) {
            super(R.layout.item_expression,data);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, Integer resId) {
            baseViewHolder.setImageResource(R.id.iv_expression,resId);
        }
    }
}
