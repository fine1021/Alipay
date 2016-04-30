package com.yxkang.android.alipay.event;

import com.alipay.api.response.AlipayTradePrecreateResponse;

/**
 * Created by yexiaokang on 2016/4/29.
 */
public class AlipayResponseEvent {

    private final AlipayTradePrecreateResponse mResponse;

    public AlipayResponseEvent(AlipayTradePrecreateResponse response) {
        mResponse = response;
    }

    public AlipayTradePrecreateResponse getResponse() {
        return mResponse;
    }
}
