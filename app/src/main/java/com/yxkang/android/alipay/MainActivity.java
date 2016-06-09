package com.yxkang.android.alipay;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayRequest;
import com.alipay.api.AlipayResponse;
import com.alipay.api.SignItem;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.hikvision.alipaysdk.AlipayClientFactory;
import com.hikvision.alipaysdk.PayConstants;
import com.hikvision.alipaysdk.builder.AlipayTradePrecreateRequestBuilder;
import com.hikvision.alipaysdk.builder.AlipayTradeQueryRequestBuilder;
import com.hikvision.alipaysdk.internal.parser.json.JsonConverter;
import com.hikvision.alipaysdk.model.GoodsDetail;
import com.yxkang.android.alipay.event.AlipayTradePrecreateEvent;
import com.yxkang.android.alipay.event.AlipayTradeQueryEvent;
import com.yxkang.android.alipay.model.DialogModel;
import com.yxkang.android.alipay.model.DialogModelImpl;
import com.yxkang.android.alipay.util.SignaturesUtil;
import com.yxkang.android.alipay.util.Utils;
import com.yxkang.rxandroid.RxEventBus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import rx.Subscription;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE = 0x10;
    @BindView(R.id.text)
    TextView mTextView;
    private DialogModel mDialogModel;
    private final ExecutorService mThreadPool = Executors.newCachedThreadPool();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mTextView.setText(SignaturesUtil.getSelfSignatures(this));
        setSubscriber();
        mDialogModel = new DialogModelImpl(this);
    }

    private void setSubscriber() {
        Subscription subscription = RxEventBus.getInstance().ofType(AlipayTradePrecreateEvent.class).subscribe(new Action1<AlipayTradePrecreateEvent>() {
            @Override
            public void call(AlipayTradePrecreateEvent alipayTradePrecreateEvent) {
                mDialogModel.dismissProgressDialog();
                AlipayTradePrecreateResponse response = alipayTradePrecreateEvent.getResponse();
                if (response == null) {
                    Toast.makeText(MainActivity.this, "response == null", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i(TAG, "call: body = " + response.getBody());
                    AlipayTradePrecreateRequest request = alipayTradePrecreateEvent.getRequest();
                    JsonConverter jsonConverter = new JsonConverter();
                    try {
                        SignItem signItem = jsonConverter.getSignItem(request, response.getBody());
                        Log.i(TAG, "call: sign = " + signItem.getSign());
                        Log.i(TAG, "call: signSourceDate = " + signItem.getSignSourceDate());
                    } catch (AlipayApiException e) {
                        e.printStackTrace();
                    }
                    if (response.isSuccess()) {
                        queryAlipayTrade(request, response);
                        drawQRCode(response.getQrCode());
                    } else {
                        Toast.makeText(MainActivity.this, response.getSubMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                mDialogModel.dismissProgressDialog();
                Log.w(TAG, "call: ", throwable);
            }
        });
        RxEventBus.getInstance().subscribe(this, subscription);

        subscription = RxEventBus.getInstance().ofType(AlipayTradeQueryEvent.class).subscribe(new Action1<AlipayTradeQueryEvent>() {
            @Override
            public void call(AlipayTradeQueryEvent alipayTradeQueryEvent) {
                AlipayTradeQueryResponse response = alipayTradeQueryEvent.getResponse();
                if (response == null) {
                    Log.i(TAG, "AlipayTradeQueryResponse response == null");
                    return;
                }
                Log.i(TAG, "call: body = " + response.getBody());
                AlipayTradeQueryRequest request = alipayTradeQueryEvent.getRequest();
                JsonConverter jsonConverter = new JsonConverter();
                try {
                    SignItem signItem = jsonConverter.getSignItem(request, response.getBody());
                    Log.i(TAG, "call: sign = " + signItem.getSign());
                    Log.i(TAG, "call: signSourceDate = " + signItem.getSignSourceDate());
                } catch (AlipayApiException e) {
                    e.printStackTrace();
                }
                if (!response.isSuccess()) {
                    Toast.makeText(MainActivity.this, response.getSubMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        RxEventBus.getInstance().subscribe(this, subscription);
    }

    private void drawQRCode(String url) {
        Intent intent = new Intent();
        intent.setPackage("com.zxing.support.demo");
        PackageManager manager = getPackageManager();
        List<ResolveInfo> list = manager.queryIntentActivities(intent, 0);
        if (list.size() == 0) {
            Toast.makeText(MainActivity.this, "not found zxing app", Toast.LENGTH_SHORT).show();
        } else if (list.size() == 1) {
            Iterator<ResolveInfo> iterator = list.iterator();
            ResolveInfo info = iterator.next();
            String packageName = info.activityInfo.packageName;
            String name = info.activityInfo.name;
            Log.i(TAG, packageName + "/" + name);
            intent.setComponent(new ComponentName(packageName, name));

            intent.putExtra("url", url);
            startActivityForResult(intent, REQUEST_CODE);
        } else {
            Toast.makeText(MainActivity.this, "list size > 1", Toast.LENGTH_SHORT).show();
        }

    }

    @OnLongClick(R.id.text)
    public boolean copySignature() {
        ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        manager.setPrimaryClip(ClipData.newPlainText(null, mTextView.getText()));
        Toast.makeText(MainActivity.this, "signature copied", Toast.LENGTH_SHORT).show();
        return true;
    }

    @OnClick(R.id.submit)
    public void sendPrecreateRequest() {
        mDialogModel.showProgressDialog(getString(R.string.request_processing));
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                AlipayClient alipayClient = AlipayClientFactory.getAlipayClient();
                AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
                request.setNotifyUrl(PayConstants.NOTIFY_URL);
                List<GoodsDetail> list = new ArrayList<>();
                list.add(GoodsDetail.newInstance("apple-01", "apple", 5, 1).setBody("apple phone detail"));
                list.add(GoodsDetail.newInstance("milk-01", "milk", 5, 1).setBody("milk detail"));
                AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder();
                builder.setOutTradeNo(Utils.createTradeNo())
                        .setTotalAmount("0.1")
                        .setSubject("Orange Juice")
                        .setTimeoutExpress("5m")
                        .setGoodsDetailList(list);
                Log.i(TAG, "sendPrecreateRequest: " + builder.toJsonString());
                request.setBizContent(builder.toJsonString());
                AlipayTradePrecreateResponse response = null;
                try {
                    response = alipayClient.execute(request);
                } catch (AlipayApiException e) {
                    Log.e(TAG, "AlipayApiException", e);
                } catch (Error error) {
                    Log.e(TAG, "Error", error);
                }
                RxEventBus.getInstance().post(new AlipayTradePrecreateEvent(request, response));
            }
        };
        mThreadPool.submit(runnable);
    }

    private void queryAlipayTrade(AlipayRequest request, AlipayResponse response) {
        if (request instanceof AlipayTradePrecreateRequest && response instanceof AlipayTradePrecreateResponse) {
            final AlipayTradePrecreateResponse precreateResponse = (AlipayTradePrecreateResponse) response;
            Callable<Boolean> callable = new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    AlipayClient alipayClient = AlipayClientFactory.getAlipayClient();
                    AlipayTradeQueryRequest queryRequest = new AlipayTradeQueryRequest();
                    AlipayTradeQueryRequestBuilder builder = new AlipayTradeQueryRequestBuilder();
                    builder.setOutTradeNo(precreateResponse.getOutTradeNo());
                    Log.i(TAG, "queryAlipayTrade: " + builder.toJsonString());
                    queryRequest.setBizContent(builder.toJsonString());
                    AlipayTradeQueryResponse queryResponse = alipayClient.execute(queryRequest);
                    RxEventBus.getInstance().post(new AlipayTradeQueryEvent(queryRequest, queryResponse));
                    return true;
                }
            };
            mThreadPool.submit(callable);
        }
    }

    @Override
    protected void onDestroy() {
        mThreadPool.shutdownNow();
        RxEventBus.getInstance().unsubscribe(this);
        super.onDestroy();
    }
}
