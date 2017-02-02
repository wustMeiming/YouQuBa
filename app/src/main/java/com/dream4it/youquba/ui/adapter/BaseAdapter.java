package com.dream4it.youquba.ui.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by meiming on 17-2-2.
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    //定义各种类型的View
    public static final int TYPE_COMMON_VIEW = 100001;
    public static final int TYPE_FOOTER_VIEW = 100002;
    public static final int TYPE_EMPTY_VIEW = 100003;
    public static final int TYPE_DEFAULT_VIEW = 100004;

    //加载更多监听器
    private OnLoadMoreListener mLoadMoreListener;
    //点击监听器
    private OnItemClickListeners<T> mItemClickListener;

    //上下文
    protected Context mContext;

    //数据列表
    protected List<T> mDatas;

    //是否打开加载更多
    private boolean mOpenLoadMore;
    //是否自动加载更多
    private boolean isAutoLoadMore = true;

    //各类型的view
    private View mLoadingView;
    private View mLoadFailedView;
    private View mLoadEndView;
    private View mEmptyView;

    //底部的布局
    private RelativeLayout mFooterLayout;

    //转换接口
    protected abstract void convert(ViewHolder holder, T data);

    //获取数据项的布局id
    protected abstract int getItemLayoutId();

    //构造函数
    public BaseAdapter(Context context, List<T> datas, boolean isOpenLoadMore) {
        mContext = context;
        mDatas = datas == null ? new ArrayList<T>() : datas;
        mOpenLoadMore = isOpenLoadMore;
    }

    /**
     * 根据类型创建各种类型的 ViewHolder
     * @param parent　父节点
     * @param viewType view类型
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        switch (viewType) {
            case TYPE_COMMON_VIEW:
                viewHolder = ViewHolder.create(mContext, getItemLayoutId(), parent);
                break;
            case TYPE_FOOTER_VIEW:
                if (mFooterLayout == null) {
                    mFooterLayout = new RelativeLayout(mContext);
                }
                viewHolder = ViewHolder.create(mFooterLayout);
                break;
            case TYPE_EMPTY_VIEW:
                viewHolder = ViewHolder.create(mEmptyView);
                break;
            case TYPE_DEFAULT_VIEW:
                viewHolder = ViewHolder.create(new View(mContext));
                break;
        }
        return viewHolder;
    }

    /**
     * 绑定ViewHolder
     * @param holder　viewHolder
     * @param position 位置
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TYPE_COMMON_VIEW:
                bindCommonItem(holder, position);
                break;
        }
    }

    /**
     * 绑定数据项
     * @param holder　viewHolder
     * @param position 位置
     */
    private void bindCommonItem(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        convert(viewHolder, mDatas.get(position));

        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemClickListener.onItemClick(viewHolder, mDatas.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size() + getFooterViewCount();
    }

    /**
     * 根据位置获取itemView的类型
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (mDatas.isEmpty() && mEmptyView != null) {
            return TYPE_EMPTY_VIEW;
        }

        if (isFooterView(position)) {
            return TYPE_FOOTER_VIEW;
        }

        if (mDatas.isEmpty()) {
            return TYPE_DEFAULT_VIEW;
        }

        return TYPE_COMMON_VIEW;
    }

    /**
     * 根据位置获取数据项
     * @param position
     * @return
     */
    public T getItem(int position) {
        return mDatas.get(position);
    }

    /**
     * 判断是否是底部视图
     * @param position
     * @return
     */
    private boolean isFooterView(int position) {
        return mOpenLoadMore && getItemCount() > 1 && position >= getItemCount() - 1;
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (isFooterView(holder.getLayoutPosition())) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) layoutManager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (isFooterView(position)) {
                        return gridManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }

        startLoadMore(recyclerView, layoutManager);
    }

    /**
     * 开始加载更多数据
     * @param recyclerView
     * @param layoutManager
     */
    private void startLoadMore(RecyclerView recyclerView, final RecyclerView.LayoutManager layoutManager) {
        if (!mOpenLoadMore || mLoadMoreListener == null) {
            return;
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!isAutoLoadMore && findLastVisibleItemPosition(layoutManager) + 1 == getItemCount()) {
                        scrollLoadMore();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isAutoLoadMore && findLastVisibleItemPosition(layoutManager) + 1 == getItemCount()) {
                    scrollLoadMore();
                } else if (isAutoLoadMore) {
                    isAutoLoadMore = false;
                }
            }
        });
    }

    /**
     * 滚动加载更多
     */
    private void scrollLoadMore() {
        if (mFooterLayout.getChildAt(0) == mLoadingView) {
            mLoadMoreListener.onLoadMore(false);
        }
    }

    /**
     * 查找最后可见的数据项的位置
     * @param layoutManager
     * @return
     */
    private int findLastVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(null);
            return findMax(lastVisibleItemPositions);
        }
        return -1;
    }

    /**
     * 查找最大的位置
     * @param lastVisiblePositions
     * @return
     */
    private int findMax(int[] lastVisiblePositions) {
        int max = lastVisiblePositions[0];
        for (int value : lastVisiblePositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    /**
     * 移除底部 view
     */
    private void removeFooterView() {
        mFooterLayout.removeAllViews();
    }

    /**
     * 增加底部 view
     * @param footerView
     */
    private void addFooterView(View footerView) {
        if (footerView == null) {
            return;
        }

        if (mFooterLayout == null) {
            mFooterLayout = new RelativeLayout(mContext);
        }
        removeFooterView();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mFooterLayout.addView(footerView, params);
    }

    /**
     * 设置加载更多的数据
     * @param datas
     */
    public void setLoadMoreData(List<T> datas) {
        int size = mDatas.size();
        mDatas.addAll(datas);
        notifyItemInserted(size);
    }

    /**
     * 设置数据
     * @param datas
     */
    public void setData(List<T> datas) {
        mDatas.addAll(0, datas);
        notifyDataSetChanged();
    }

    /**
     * 设置新获取的数据
     * @param datas
     */
    public void setNewData(List<T> datas) {
        mDatas.clear();
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    /**
     * 根据视图设置加载视图
     * @param loadingView
     */
    public void setLoadingView(View loadingView) {
        mLoadingView = loadingView;
        addFooterView(mLoadingView);
    }

    /**
     * 根据id设置加载视图
     * @param loadingId
     */
    public void setLoadingView(int loadingId) {
        setLoadingView(inflate(loadingId));
    }

    /**
     * 更加视图设置加载失败的视图
     * @param loadFailedView
     */
    public void setLoadFailedView(View loadFailedView) {
        if (loadFailedView == null) {
            return;
        }
        mLoadFailedView = loadFailedView;
        addFooterView(mLoadFailedView);
        mLoadFailedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFooterView(mLoadingView);
                mLoadMoreListener.onLoadMore(true);
            }
        });
    }

    /**
     * 更加id设置加载失败的视图
     * @param loadFailedId
     */
    public void setLoadFailedView(int loadFailedId) {
        setLoadFailedView(inflate(loadFailedId));
    }

    /**
     * 根据视图设置结束的视图
     * @param loadEndView
     */
    public void setLoadEndView(View loadEndView) {
        mLoadEndView = loadEndView;
        addFooterView(mLoadEndView);
    }

    /**
     * 根据id设置结束的视图
     * @param loadEndId
     */
    public void setLoadEndView(int loadEndId) {
        setLoadEndView(inflate(loadEndId));
    }

    /**
     * 更加视图设置空视图
     * @param emptyView
     */
    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
    }

    /**
     * 设置底部视图的数量
     * @return
     */
    public int getFooterViewCount() {
        return mOpenLoadMore ? 1 : 0;
    }


    /**
     * 设置加载更多的监听器
     * @param loadMoreListener
     */
    public void setOnLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        mLoadMoreListener = loadMoreListener;
    }

    /**
     * 设置点击项的监听器
     * @param itemClickListener
     */
    public void setOnItemClickListener(OnItemClickListeners<T> itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    /**
     * 通过id加载布局
     * @param layoutId
     * @return
     */
    private View inflate(int layoutId) {
        if (layoutId <= 0) {
            return null;
        }
        return LayoutInflater.from(mContext).inflate(layoutId, null);
    }
}
