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
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.yxkang.android.alipay.event.AlipayResponseEvent;
import com.yxkang.android.alipay.model.DialogModel;
import com.yxkang.android.alipay.model.DialogModelImpl;
import com.yxkang.android.alipay.util.SignaturesUtil;
import com.yxkang.rxandroid.RxEventBus;

import java.util.Iterator;
import java.util.List;

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
        Subscription subscription = RxEventBus.getInstance().ofType(AlipayResponseEvent.class).subscribe(new Action1<AlipayResponseEvent>() {
            @Override
            public void call(AlipayResponseEvent alipayResponseEvent) {
                mDialogModel.dismissProgressDialog();
                AlipayTradePrecreateResponse response = alipayResponseEvent.getResponse();
                if (response == null) {
                    Log.w(TAG, "call: response == null");
                }
                drawQRCode("https://openapi.alipay.com/gateway.do");
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                mDialogModel.dismissProgressDialog();
                Log.w(TAG, "call: ", throwable);
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                AlipayClient alipayClient = new DefaultAlipayClient(PayConstants.SERVER_URL, PayConstants.APP_ID, PayConstants.PRIVATE_KEY,
                        PayConstants.FORMAT_JSON, PayConstants.CHARSET_UTF8, PayConstants.ALIPAY_PUBLIC_KEY);
                AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
                AlipayTradePrecreateResponse response = null;
                try {
                    response = alipayClient.execute(request);
                } catch (AlipayApiException e) {
                    Log.e(TAG, "AlipayApiException", e);
                }
                RxEventBus.getInstance().post(new AlipayResponseEvent(response));
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        RxEventBus.getInstance().unsubscribe(this);
        super.onDestroy();
    }
}
