package com.dream4it.youquba.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.dream4it.youquba.R;
import com.dream4it.youquba.YouQuBaApplication;
import com.dream4it.youquba.data.PictureItemData;
import com.dream4it.youquba.utils.ImageLoaderUtil;

import java.util.List;

/**
 * Created by meiming on 17-2-2.
 */

public class PictureItemAdapter extends BaseAdapter<PictureItemData>{
    public PictureItemAdapter(Context context, List<PictureItemData> datas, boolean isOpenLoadMore) {
        super(context, datas, isOpenLoadMore);
    }

    @Override
    protected void convert(ViewHolder holder, PictureItemData data) {
        ImageLoaderUtil.load(YouQuBaApplication.getContext(), data.getImage(), (ImageView) holder.getView(R.id.iv_image));
        holder.setText(R.id.tv_title, data.getTitle());
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_picture_layout;
    }
}
