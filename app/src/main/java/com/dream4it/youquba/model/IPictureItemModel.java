package com.dream4it.youquba.model;

import rx.Observable;

/**
 * Created by meiming on 17-2-1.
 */

public interface IPictureItemModel {
    Observable<String> getPictureItemData(String suburl, int page);
}
