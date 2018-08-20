package com.mny.share.testrx.connection_interface;

import com.mny.share.testrx.model.Translation;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Crate by E470PD on 2018/8/16
 */
public interface GetRequest_InferfaceTranslate {
    @GET("ajax.php?a=fy&f=auto&t=auto&w=hi%20world")
    Observable<Translation> getCall();
}
