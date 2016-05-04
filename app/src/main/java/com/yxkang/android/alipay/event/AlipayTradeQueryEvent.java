package com.yxkang.android.alipay.event;

import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;

/**
 * Created by yexiaokang on 2016/5/4.
 */
public class AlipayTradeQueryEvent {

    private final AlipayTradeQueryRequest mRequest;
    private final AlipayTradeQueryResponse mResponse;

    public AlipayTradeQueryEvent(AlipayTradeQueryRequest request, AlipayTradeQueryResponse response) {
        mRequest = request;
        mResponse = response;
    }

    public AlipayTradeQueryRequest getRequest() {
        return mRequest;
    }

    public AlipayTradeQueryResponse getResponse() {
        return mResponse;
    }
}
