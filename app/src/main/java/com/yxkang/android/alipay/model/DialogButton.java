package com.yxkang.android.alipay.model;

import android.content.DialogInterface;

/**
 * Created by yexiaokang on 2016/4/27.
 */
public class DialogButton {

    private final int which;
    private final String text;
    private final DialogInterface.OnClickListener listener;

    public DialogButton(int which, String text, DialogInterface.OnClickListener listener) {
        this.which = which;
        this.text = text;
        this.listener = listener;
    }

    public int getWhich() {
        return which;
    }

    public String getText() {
        return text;
    }

    public DialogInterface.OnClickListener getListener() {
        return listener;
    }
}
