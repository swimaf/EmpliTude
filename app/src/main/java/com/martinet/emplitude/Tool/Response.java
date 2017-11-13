package com.martinet.emplitude.Tool;

public interface Response<T, S> {
    void onSuccess(T value);
    void onError(S message);
}
