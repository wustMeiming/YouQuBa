package com.dream4it.youquba.ui.view;

import com.dream4it.youquba.data.PictureItemData;

import java.util.List;

/**
 * Created by meiming on 17-2-1.
 */

public interface PictureItemView extends IBaseView {
    void onSuccess(List<PictureItemData> data);
}
