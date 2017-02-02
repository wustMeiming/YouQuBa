package com.dream4it.youquba.model.impl;

import com.dream4it.youquba.model.IPictureItemModel;
import com.dream4it.youquba.net.NetManager;
import com.dream4it.youquba.service.PictureItemService;

import rx.Observable;

/**
 * Created by meiming on 17-2-1.
 */

public class PictureItemModelImpl implements IPictureItemModel {

    @Override
    public Observable<String> getPictureItemData(String cid, int page) {
        PictureItemService service = NetManager.getInstance().createScalars(PictureItemService.class);
        return service.getPictureItemData(cid, page);
    }
}
