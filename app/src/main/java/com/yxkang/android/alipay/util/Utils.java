package com.yxkang.android.alipay.util;


public class Utils {

    public static String createTradeNo() {
        return "tradeNo" + System.currentTimeMillis() + (long) (Math.random() * 10000000L);
    }
}
