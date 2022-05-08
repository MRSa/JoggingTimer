package net.osdn.gokigen.joggingtimer.utilities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import net.osdn.gokigen.joggingtimer.R;

/**
 *   確認ダイアログの表示
 *
 *
 */
public class ConfirmationDialog extends DialogFragment
{
    private final String TAG = toString();
    String title = "";
    String message = "";
    Callback callback = null;
    Dialog myDialog = null;

    public static ConfirmationDialog newInstance(String title, String message, @NonNull Callback callback)
    {
        ConfirmationDialog instance = new ConfirmationDialog();
        instance.prepare(callback, title, message);

        // パラメータはBundleにまとめておく
        Bundle arguments = new Bundle();
        arguments.putString("title", title);
        arguments.putString("message", message);
        instance.setArguments(arguments);

        return (instance);
    }

    /**
     *
     *
     */
    private void prepare(Callback callback, String title, String message)
    {
        this.callback = callback;
        this.title = title;
        this.message = message;
    }

    /**
     *
     *
     */
    @Override
    public @NonNull Dialog onCreateDialog(Bundle savedInstanceState)
    {
        String title = this.title;
        String message = this.message;
        if (savedInstanceState != null)
        {
            title = savedInstanceState.getString("title");
            message = savedInstanceState.getString("message");
        }
        Context context = getContext();
        //AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.wear2_dialog_theme));
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        alertDialog.setTitle(title);
        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(true);
        String positiveLabel = "OK";
        String negativeLabel = "Cancel";
        if (context != null)
        {
            positiveLabel = context.getString(R.string.dialog_positive_execute);
            negativeLabel = context.getString(R.string.dialog_negative_cancel);
        }

        // ボタンを設定する（実行ボタン）
        alertDialog.setPositiveButton(positiveLabel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Log.v(TAG, "ConfirmationDialog::OK");
                        if (callback != null)
                        {
                            callback.confirm();
                        }
                        dialog.dismiss();
                    }
                });

        // ボタンを設定する (キャンセルボタン）
        alertDialog.setNegativeButton(negativeLabel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.cancel();
                    }
                });

        myDialog = alertDialog.create();
        return (myDialog);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        Log.v(TAG, "AlertDialog::onPause()");
        if (myDialog != null)
        {
            myDialog.cancel();
        }
    }


    // コールバックインタフェース
    public interface Callback
    {
        void confirm(); // OKを選択したとき
    }
}
