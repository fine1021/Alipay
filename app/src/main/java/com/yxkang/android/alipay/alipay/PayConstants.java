package com.yxkang.android.alipay.alipay;

/**
 * Created by yexiaokang on 2016/4/29.
 */
public class PayConstants {

    public static final String SERVER_URL = "https://openapi.alipay.com/gateway.do";
    public static final String APP_ID = "2016042801347717";
    public static final String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAM6o08JpqZnOJspA\n" +
            "4yKMkfkF6f6yA3WMlSIE13ZuUzM6qhnl2YQjnGZxDFFl3ERwMuUFpqlSPPLYDphf\n" +
            "lMsJvFQvr0fyxfkS7Hmm+WTNLWne65xySfBEjEyFMByfGPthtjD9BmTAhxJMV74C\n" +
            "SbkbMw505esm68eb3TlYYUoVbRs3AgMBAAECgYAl2W/nRHjmjYY3Nski08IMbNPd\n" +
            "bPcLOXM65s4bh7s/P7hVw+4hsG/9pyjNY62C71Wy8mUCz+gPxhhuOdtDkIXoRPYE\n" +
            "AHHqK/PyZRQ4eEtrwq1xFSd5KerVZ+0B2+gcnwpTthc55mcNkyYdnrQ/NqH1FsYt\n" +
            "9YLhKd4rIp6RqAhH6QJBAPFEDedozSXEIoUr3yYyVv7kKfM4ezFSqtkl8C9nsBcY\n" +
            "Ad59b72ICNPjHiXpSSeENdPZCfCHa+6h7pXs7Pz3WUMCQQDbR7zcS3o+4kqygAwM\n" +
            "42YCcrtyXb2lXVlamcIeOF/ZMlrgG/ifwg+Lca+lvLmvc3mY5bS1IF8Svu2q2csy\n" +
            "/kz9AkEAlqgEdqh+0kJzu+z1X4i3wlr/xx1R8C7K+OYz4aZB02nuYNy7VgMIYCfG\n" +
            "v625PK/WXP9+sXAe+disf3p/UkeyuQJAF4xmrcm1QReQKgcyRBgPlme31ZpXiXCy\n" +
            "7dXhUNmBAZQ0yih21MFEZhc5Y7UWw87E2jSg6Wqmix7R6y0kcz94CQJBAOX7qGjD\n" +
            "YNpAdPk1hvvbSQBUk2H6w95yp5J2e16+AdnYu/j/c+zKPkgY9LjYo/dbffT9rhk6\n" +
            "WoRCUYLn2VPukgo=";
    public static final String FORMAT_JSON = "json";
    public static final String CHARSET_UTF8 = "UTF-8";
    public static final String ALIPAY_PUBLIC_KEY = "";
    public static final String NOTIFY_URL = "";


    /**
     * 响应的返回码
     */
    public static final String SUCCESS = "10000"; // 成功
    public static final String PAYING = "10003";  // 用户支付中
    public static final String FAILED = "40004";  // 失败
    public static final String ERROR = "20000"; // 系统异常
}
