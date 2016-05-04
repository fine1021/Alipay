package com.yxkang.android.alipay.event;

import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;

/**
 * Created by yexiaokang on 2016/4/29.
 */
public class AlipayTradePrecreateEvent {

    private final AlipayTradePrecreateRequest mRequest;
    private final AlipayTradePrecreateResponse mResponse;

    public AlipayTradePrecreateEvent(AlipayTradePrecreateRequest request, AlipayTradePrecreateResponse response) {
        mRequest = request;
        mResponse = response;
    }

    public AlipayTradePrecreateRequest getRequest() {
        return mRequest;
    }

    public AlipayTradePrecreateResponse getResponse() {
        return mResponse;
    }
}
