package com.dhc.app.data;

/**
 * Created by Vinod Durge on  08 Nov 2020
 */
public interface ResponseCallback<T> {
    void onSuccess(T t);

    void onError(Error error);
}
