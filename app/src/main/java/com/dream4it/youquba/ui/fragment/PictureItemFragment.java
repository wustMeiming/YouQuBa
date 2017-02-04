package com.dream4it.youquba.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.dream4it.youquba.R;
import com.dream4it.youquba.data.PictureItemData;
import com.dream4it.youquba.presenter.PictureItemPresenter;
import com.dream4it.youquba.service.DataService;
import com.dream4it.youquba.ui.activity.PictureDetailActivity;
import com.dream4it.youquba.ui.adapter.OnItemClickListeners;
import com.dream4it.youquba.ui.adapter.OnLoadMoreListener;
import com.dream4it.youquba.ui.adapter.PictureItemAdapter;
import com.dream4it.youquba.ui.adapter.ViewHolder;
import com.dream4it.youquba.ui.view.PictureItemView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by meiming on 17-2-1.
 */

public class PictureItemFragment extends BaseMvpFragment<PictureItemView, PictureItemPresenter> implements PictureItemView, SwipeRefreshLayout.OnRefreshListener {
    private int PAGE_COUNT = 1;
    private String mSubtype;
    private int mTempPageCount = 2;

    private PictureItemAdapter mPictureItemAdapter;

    private boolean isLoadMore;//是否是底部加载更多

    @BindView(R.id.type_item_recyclerview)
    RecyclerView mRecyclerView;

    @BindView(R.id.type_item_swipfreshlayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onRefresh() {
        isLoadMore = false;
        PAGE_COUNT = 1;
        fetchData();
    }

    @Override
    protected PictureItemPresenter initPresenter() {
        return new PictureItemPresenter();
    }

    @Override
    protected void fetchData() {
        mPresenter.getPictureItemData(mSubtype, PAGE_COUNT);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_type_item_layout;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        //实现首次自动显示加载提示
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });

        mPictureItemAdapter = new PictureItemAdapter(mActivity, new ArrayList<PictureItemData>(), true);
        mPictureItemAdapter.setLoadingView(R.layout.load_loading_layout);
        mPictureItemAdapter.setOnItemClickListener(new OnItemClickListeners<PictureItemData>() {
            @Override
            public void onItemClick(ViewHolder viewHolder, PictureItemData PictureItemData, int position) {
                Intent intent = new Intent(mActivity, PictureDetailActivity.class);
                intent.putExtra("picture_item_data", PictureItemData);
                startActivity(intent);
            }
        });

        mPictureItemAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(boolean isReload) {
                if (PAGE_COUNT == mTempPageCount && !isReload) {
                    return;
                }
                isLoadMore = true;
                PAGE_COUNT = mTempPageCount;
                fetchData();
            }
        });

        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);//可防止Item切换
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setAdapter(mPictureItemAdapter);
    }

    @Override
    protected void initData() {
        if (getArguments() == null) {
            return;
        }
        mSubtype = getArguments().getString(SUB_TYPE);
    }

    @Override
    public void onSuccess(List<PictureItemData> data) {
        DataService.startService(mActivity, data, mSubtype);
    }

    @Override
    public void onError() {
        if (isLoadMore) {
            mPictureItemAdapter.setLoadFailedView(R.layout.load_failed_layout);
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    public static PictureItemFragment newInstance(String subtype) {
        PictureItemFragment fragment = new PictureItemFragment();
        Bundle arguments = new Bundle();
        arguments.putString(SUB_TYPE, subtype);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void dataEvent(List<PictureItemData> data) {
        if (!data.get(0).getSubtype().equals(mSubtype)) {
            return;
        }

        if (isLoadMore) {
            if (data.size() == 0) {
                mPictureItemAdapter.setLoadEndView(R.layout.load_end_layout);
            } else {
                mTempPageCount++;
                mPictureItemAdapter.setLoadMoreData(data);
            }
        } else {
            mPictureItemAdapter.setNewData(data);
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }
}
