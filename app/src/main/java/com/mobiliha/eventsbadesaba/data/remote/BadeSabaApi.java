package com.mobiliha.eventsbadesaba.data.remote;

import com.mobiliha.eventsbadesaba.data.remote.model.RemoteTask;
import com.mobiliha.eventsbadesaba.data.remote.model.ShareInfo;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BadeSabaApi {

    /**
     * Save task in the server so we can share the associated task link.
     * @param task Task to be shared.
     * @return Share information.
     */
    @POST("event/share")
    Single<ShareInfo> saveTask(@Body RemoteTask task);

    /**
     * Get the task saved in the server using its token.
     * @param token Token of task.
     * @return Task with the given token.
     */
    @POST("event/get/{token}")
    Single<RemoteTask> getTask(@Path("token") String token);

}
