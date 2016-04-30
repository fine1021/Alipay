package com.yxkang.android.alipay.model;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;

import com.yxkang.android.alipay.R;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by yexiaokang on 2016/4/27.
 */
public class DialogModelImpl implements DialogModel {

    private Context mContext;
    private AlertDialog mAlertDialog;
    private ProgressDialog mProgressDialog;
    private final ReentrantLock mReentrantLock = new ReentrantLock(true);

    public DialogModelImpl(Context context) {
        mContext = context;
    }

    @Override
    public void showProgressDialog(CharSequence message) {
        showProgressDialog(message, false);
    }

    @Override
    public synchronized void showProgressDialog(CharSequence message, boolean cancelable) {
        mReentrantLock.lock();
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(cancelable);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        } finally {
            mReentrantLock.unlock();
        }
    }

    @Override
    public synchronized void dismissProgressDialog() {
        mReentrantLock.lock();
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
        } finally {
            mReentrantLock.unlock();
        }
    }

    @Override
    public void showAlertDialog(CharSequence message, boolean cancelable, DialogButton... listeners) {
        String title = mContext.getString(R.string.dialog_tip_title);
        showAlertDialog(title, message, cancelable, listeners);
    }

    @Override
    public void showAlertDialog(CharSequence message, DialogButton... listeners) {
        String title = mContext.getString(R.string.dialog_tip_title);
        showAlertDialog(title, message, false, listeners);
    }

    @Override
    public void showAlertDialog(CharSequence title, CharSequence message, DialogButton... listeners) {
        showAlertDialog(title, message, false, listeners);
    }

    @Override
    public synchronized void showAlertDialog(CharSequence title, CharSequence message, boolean cancelable, DialogButton... listeners) {
        mReentrantLock.lock();
        try {
            if (mAlertDialog != null && mAlertDialog.isShowing()) {
                mAlertDialog.dismiss();
                mAlertDialog = null;
            }
            mAlertDialog = new AlertDialog.Builder(mContext)
                    .setTitle(title)
                    .setMessage(message)
                    .setCancelable(cancelable)
                    .create();
            if (listeners != null && listeners.length > 0) {
                for (DialogButton button : listeners) {
                    mAlertDialog.setButton(button.getWhich(), button.getText(), button.getListener());
                }
            }
            mAlertDialog.show();
        } finally {
            mReentrantLock.unlock();
        }
    }

    @Override
    public synchronized void dismissAlertDialog() {
        mReentrantLock.lock();
        try {
            if (mAlertDialog != null && mAlertDialog.isShowing()) {
                mAlertDialog.dismiss();
                mAlertDialog = null;
            }
        } finally {
            mReentrantLock.unlock();
        }
    }

    @Override
    public AlertDialog getAlertDialog() {
        mReentrantLock.lock();
        try {
            return mAlertDialog;
        } finally {
            mReentrantLock.unlock();
        }
    }
}
