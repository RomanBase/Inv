package com.dontpanic.base.networking.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;

import java.util.Map;

import com.dontpanic.base.common.statics.StringHelper;

public abstract class BaseVolleyRequest<T> extends Request<T> {

    private final Response.Listener<T> listener;
    private final String contentType;
    private final Map<String, String> header;
    private final byte[] body;

    public BaseVolleyRequest(int method, String url, String contentType, Map<String, String> header, byte[] body, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);

        this.contentType = contentType;
        this.header = header;
        this.body = body;
        this.listener = listener;
    }

    @Override
    public String getBodyContentType() {
        return !StringHelper.isEmpty(contentType) ? contentType : super.getBodyContentType();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return header != null ? header : super.getHeaders();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return body != null ? body : super.getBody();
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }
}
