package com.mobiliha.eventsbadesaba.data.remote;

import com.mobiliha.eventsbadesaba.data.remote.model.RemoteTask;
import com.mobiliha.eventsbadesaba.data.remote.model.ShareInfo;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface BadeSabaApi {

    /**
     * Save task in the server so we can share the associated task link.
     * @param task Task to be shared.
     * @return Share information.
     */
    @POST("event/share")
    Single<ShareInfo> saveTask(@Body RemoteTask task);

}
