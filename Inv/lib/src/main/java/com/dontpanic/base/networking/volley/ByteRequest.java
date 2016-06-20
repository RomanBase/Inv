package com.dontpanic.base.networking.volley;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.util.Map;

public class ByteRequest extends BaseVolleyRequest<byte[]> {

    public ByteRequest(int method, String url, String contentType, Map<String, String> header, byte[] body, Response.Listener<byte[]> listener, Response.ErrorListener errorListener) {
        super(method, url, contentType, header, body, listener, errorListener);
    }

    @Override
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response));
    }
}
