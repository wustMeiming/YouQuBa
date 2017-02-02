package com.dream4it.youquba.service;

import com.dream4it.youquba.api.ApiUrl;


import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by meiming on 17-2-2.
 */

public interface PictureItemService {
    String BASE_URL = ApiUrl.URL_GET_PICTURE;
    @GET("")
    Observable<String> getPictureItemData(@Query("cid") String cid, @Query("page") int page);
}
