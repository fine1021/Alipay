package com.yxkang.android.alipay.alipay;

import com.alipay.api.AlipayClient;

/**
 * Created by yexiaokang on 2016/5/4.
 */
public class AlipayClientFactory {

    private static final AlipayClient alipayClient = new DefaultAlipayClient(PayConstants.SERVER_URL, PayConstants.APP_ID, PayConstants.PRIVATE_KEY,
            PayConstants.FORMAT_JSON, PayConstants.CHARSET_UTF8, PayConstants.ALIPAY_PUBLIC_KEY);

    public static AlipayClient getAlipayClient() {
        return alipayClient;
    }
}
