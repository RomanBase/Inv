package com.ankhrom.base.interfaces.viewmodel;

import com.ankhrom.base.model.Model;
import com.ankhrom.base.networking.volley.BaseVolleyRequest;
import com.ankhrom.base.networking.volley.RequestBuilder;

public interface NetworkingViewModel<T extends Model, U> {

    BaseVolleyRequest<U> createRequest(RequestBuilder request);

    T createModel(U response);
}
