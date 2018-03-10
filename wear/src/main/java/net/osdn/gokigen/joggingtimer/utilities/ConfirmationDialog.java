package net.osdn.gokigen.joggingtimer.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import net.osdn.gokigen.joggingtimer.R;

/**
 *   確認ダイアログの表示
 *
 *
 */
public class ConfirmationDialog
{
    private final Context context;

    public ConfirmationDialog(Context context)
    {
        this.context = context;
    }

    /**
     *
     * @param titleResId    ダイアログタイトル
     * @param messageResId  ダイアログメッセージ
     * @param callback       結果をコールバック
     */
    public void show(int titleResId, int messageResId, final Callback callback)
    {
        String title = "";
        String message = "";

        // タイトルとメッセージをのダイアログを表示する
        if (context != null)
        {
            title = context.getString(titleResId);
            message = context.getString(messageResId);
        }
        show(title, message, callback);
    }

    /**
     *
     * @param titleResId   ダイアログタイトル
     * @param message      ダイアログメッセージ
     * @param callback     結果をコールバック
     */
    public void show(int titleResId, String message, final Callback callback)
    {
        String title = "";

        // タイトルとメッセージをのダイアログを表示する
        if (context != null)
        {
            title = context.getString(titleResId);
        }
        show(title, message, callback);
    }

    /**
     *
     * @param title     ダイアログタイトル
     * @param message   ダイアログメッセージ
     * @param callback  結果をコールバック
     */
    public void show(String title, String message, final Callback callback)
    {
        // 確認ダイアログの生成
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(title);
        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(true);

        // ボタンを設定する（実行ボタン）
        alertDialog.setPositiveButton(context.getString(R.string.dialog_positive_execute),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        callback.confirm();
                        dialog.dismiss();
                    }
                });

        // ボタンを設定する (キャンセルボタン）
        alertDialog.setNegativeButton(context.getString(R.string.dialog_negative_cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.cancel();
                    }
                });

        // 確認ダイアログを表示する
        alertDialog.show();
    }

    // コールバックインタフェース
    public interface Callback
    {
        void confirm(); // OKを選択したとき
    }
}
