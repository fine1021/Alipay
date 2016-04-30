package com.yxkang.android.alipay.model;

import android.support.v7.app.AlertDialog;

/**
 * Created by yexiaokang on 2016/4/27.
 */
public interface DialogModel {

    /**
     * show a progress dialog with message
     *
     * @param message dialog message
     */
    void showProgressDialog(CharSequence message);

    /**
     * show a progress dialog with message and cancelable
     *
     * @param message    dialog message
     * @param cancelable dialog cancelable, default value is {@code false}
     */
    void showProgressDialog(CharSequence message, boolean cancelable);

    /**
     * dismiss the progress dialog
     */
    void dismissProgressDialog();

    /**
     * show a alert dialog with message、cancelable and listener
     *
     * @param message    dialog message
     * @param cancelable dialog cancelable, default value is {@code false}
     * @param listeners  dialog button listeners, no more then three, list as followed:
     *                   <ul>
     *                   <li>{@link AlertDialog#BUTTON_POSITIVE}</li>
     *                   <li>{@link AlertDialog#BUTTON_NEGATIVE}</li>
     *                   <li>{@link AlertDialog#BUTTON_NEUTRAL}</li>
     *                   </ul>
     */
    void showAlertDialog(CharSequence message, boolean cancelable, DialogButton... listeners);

    /**
     * show a alert dialog with message and listener
     *
     * @param message   dialog message
     * @param listeners dialog button listeners, no more then three, list as followed:
     *                  <ul>
     *                  <li>{@link AlertDialog#BUTTON_POSITIVE}</li>
     *                  <li>{@link AlertDialog#BUTTON_NEGATIVE}</li>
     *                  <li>{@link AlertDialog#BUTTON_NEUTRAL}</li>
     *                  </ul>
     */
    void showAlertDialog(CharSequence message, DialogButton... listeners);

    /**
     * show a alert dialog with title、 message and listener
     *
     * @param title     dialog title
     * @param message   dialog message
     * @param listeners dialog button listeners, no more then three, list as followed:
     *                  <ul>
     *                  <li>{@link AlertDialog#BUTTON_POSITIVE}</li>
     *                  <li>{@link AlertDialog#BUTTON_NEGATIVE}</li>
     *                  <li>{@link AlertDialog#BUTTON_NEUTRAL}</li>
     *                  </ul>
     */
    void showAlertDialog(CharSequence title, CharSequence message, DialogButton... listeners);

    /**
     * show a alert dialog with title、 message、 cancelable and listener
     *
     * @param title      dialog title
     * @param message    dialog message
     * @param cancelable dialog cancelable, default value is {@code false}
     * @param listeners  dialog button listeners, no more then three, list as followed:
     *                   <ul>
     *                   <li>{@link AlertDialog#BUTTON_POSITIVE}</li>
     *                   <li>{@link AlertDialog#BUTTON_NEGATIVE}</li>
     *                   <li>{@link AlertDialog#BUTTON_NEUTRAL}</li>
     *                   </ul>
     */
    void showAlertDialog(CharSequence title, CharSequence message, boolean cancelable, DialogButton... listeners);

    /**
     * dismiss the alert dialog
     */
    void dismissAlertDialog();

    /**
     * get the alert dialog
     */
    AlertDialog getAlertDialog();
}
