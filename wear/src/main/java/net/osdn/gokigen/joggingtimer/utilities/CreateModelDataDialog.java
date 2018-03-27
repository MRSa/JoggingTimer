package net.osdn.gokigen.joggingtimer.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
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
     * @param iconResId  アイコンリソース
     * @param callback  結果をコールバック
     */
    public void show(int iconResId, String title, final Callback callback)
    {
        // 確認ダイアログの生成
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

        // Get the layout inflater
        LayoutInflater inflater = activity.getLayoutInflater();
        final View alertView = inflater.inflate(R.layout.time_model_picker, null, false);
        alertDialog.setView(alertView);

        final TextView titleText = alertView.findViewById(R.id.information_picker);
        final NumberPicker lap = alertView.findViewById(R.id.number_picker_lap_count);
        final NumberPicker hour = alertView.findViewById(R.id.number_picker_hours);
        final NumberPicker minute = alertView.findViewById(R.id.number_picker_minutes);
        final NumberPicker second = alertView.findViewById(R.id.number_picker_seconds);
        try
        {
            titleText.setText(title);
            lap.setMinValue(1);
            lap.setMaxValue(99);
            hour.setMinValue(0);
            hour.setMaxValue(72);
            minute.setMinValue(0);
            minute.setMaxValue(59);
            second.setMinValue(0);
            second.setMaxValue(59);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        alertDialog.setIcon(iconResId);
        alertDialog.setMessage(activity.getString(R.string.information_time_picker));
        alertDialog.setCancelable(true);

        // ボタンを設定する（実行ボタン）
        alertDialog.setPositiveButton(activity.getString(R.string.dialog_positive_execute),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        try
                        {
                            Log.v(TAG, "ENTRY [" + lap.getValue() + "] " + hour.getValue() + ":" + minute.getValue() + ":" + second.getValue());
                            callback.dataCrated(lap.getValue(), hour.getValue(), minute.getValue(), second.getValue());
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
        void dataCrated(int lap, int hour, int minute, int second); // OKを選択したとき
        void dataCreateCancelled();  // キャンセルしたとき
    }
}
