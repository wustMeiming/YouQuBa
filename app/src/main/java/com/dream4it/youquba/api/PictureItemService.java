package com.dream4it.youquba.api;

import com.dream4it.youquba.api.ApiUrl;


import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by meiming on 17-2-2.
 */

public interface PictureItemService {
    String BASE_URL = ApiUrl.URL_GET_PICTURE;

    @GET("{suburl}_{page}.html")
    Observable<String> getPictureItemData(@Path("suburl") String suburl, @Path("page") int page);
}
