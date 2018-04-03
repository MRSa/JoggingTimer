package net.osdn.gokigen.joggingtimer.utilities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;


import net.osdn.gokigen.joggingtimer.R;


/**
 *
 *
 */
public class CreateModelDataDialog
{
    private final String TAG = toString();
    private final WearableActivity activity;

    public CreateModelDataDialog(WearableActivity activity)
    {
        this.activity = activity;
    }

    /**
     *
     * @param callback  結果をコールバック
     */
    public void show(final boolean isLap, String title, final int lapCount, final Callback callback, final long defaultValue)
    {
        Log.v(TAG, "show " + "def. : " + defaultValue);

        // 確認ダイアログの生成
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.wear2_dialog_theme));

        // Get the layout inflater
        LayoutInflater inflater = activity.getLayoutInflater();
        final View alertView = inflater.inflate(R.layout.time_model_picker, null, false);
        alertDialog.setView(alertView);

        final TextView titleText = alertView.findViewById(R.id.information_picker);
        final TextView lapStartText = alertView.findViewById(R.id.lap_start);
        final TextView lapEndText = alertView.findViewById(R.id.lap_end);
        final NumberPicker lap = alertView.findViewById(R.id.number_picker_lap_count);
        final NumberPicker hour = alertView.findViewById(R.id.number_picker_hours);
        final NumberPicker minute = alertView.findViewById(R.id.number_picker_minutes);
        final NumberPicker second = alertView.findViewById(R.id.number_picker_seconds);

        try
        {
            if (title != null)
            {
                titleText.setText(title);
            }
            if (isLap)
            {
                lap.setVisibility(View.VISIBLE);
                lapStartText.setVisibility(View.VISIBLE);
                lapEndText.setVisibility(View.VISIBLE);
                lap.setMinValue(1);
                lap.setMaxValue(99);
            }
            else
            {
                lap.setValue(lapCount);
                lap.setVisibility(View.GONE);
                lapStartText.setVisibility(View.GONE);
                lapEndText.setVisibility(View.GONE);
            }

            hour.setMinValue(0);
            hour.setMaxValue(72);
            minute.setMinValue(0);
            minute.setMaxValue(59);
            second.setMinValue(0);
            second.setMaxValue(59);
            second.setValue((int) (defaultValue / 1000) % 60);
            minute.setValue((int) ((defaultValue / (1000 * 60)) % 60));
            hour.setValue((int) ((defaultValue / (1000 * 60 * 60)) % 24));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        alertDialog.setCancelable(true);

        // ボタンを設定する（実行ボタン）
        alertDialog.setPositiveButton(activity.getString(R.string.dialog_positive_execute),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        try
                        {
                            Log.v(TAG, "ENTRY [" + lap.getValue() + "] " + hour.getValue() + ":" + minute.getValue() + ":" + second.getValue());
                            int lapC = (isLap) ? lap.getValue() : lapCount;
                            long newMillis = (hour.getValue() * 60 * 60 * 1000) + (minute.getValue() * 60 * 1000) + (second.getValue() * 1000);
                            callback.dataCreated(isLap, lapC, defaultValue, newMillis);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            callback.dataCreateCancelled();
                        }
                        dialog.dismiss();
                    }
                });

        // ボタンを設定する (キャンセルボタン）
        alertDialog.setNegativeButton(activity.getString(R.string.dialog_negative_cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        callback.dataCreateCancelled();
                        dialog.cancel();
                    }
                });

        // 確認ダイアログを表示する
        alertDialog.show();
    }

    // コールバックインタフェース
    public interface Callback
    {
        void dataCreated(boolean isLap, int lap, long previousValue, long newValue); // OKを選択したとき
        void dataCreateCancelled();  // キャンセルしたとき
    }
}
