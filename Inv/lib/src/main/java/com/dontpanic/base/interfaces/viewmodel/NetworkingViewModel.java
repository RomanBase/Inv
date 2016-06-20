package com.dontpanic.base.interfaces.viewmodel;

import com.dontpanic.base.model.Model;
import com.dontpanic.base.networking.volley.BaseVolleyRequest;
import com.dontpanic.base.networking.volley.RequestBuilder;

public interface NetworkingViewModel<T extends Model, U> {

    BaseVolleyRequest<U> createRequest(RequestBuilder request);

    T createModel(U response);
}
