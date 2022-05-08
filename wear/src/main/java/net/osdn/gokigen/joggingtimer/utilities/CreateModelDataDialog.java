package net.osdn.gokigen.joggingtimer.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import androidx.fragment.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;


import androidx.annotation.NonNull;

import net.osdn.gokigen.joggingtimer.R;


/**
 *
 *
 */
public class CreateModelDataDialog  extends DialogFragment
{
    private final String TAG = toString();

    private boolean isLap = true;
    private String title = "";
    private int lapCount = 0;
    private Callback callback = null;
    private long defaultValue = 0;
    Dialog myDialog = null;

    /**
     *
     *
     */
    public static CreateModelDataDialog newInstance(boolean isLap, String title, int lapCount, Callback callback, long defaultValue)
    {
        CreateModelDataDialog instance = new CreateModelDataDialog();
        instance.prepare(isLap, title, lapCount, callback, defaultValue);

        // パラメータはBundleにまとめておく
        Bundle arguments = new Bundle();
        arguments.putString("title", title);
        //arguments.putString("message", message);
        instance.setArguments(arguments);

        return (instance);
    }

    /**
     *
     *
     */
    private void prepare(boolean isLap, String title, int lapCount, Callback callback, long defaultValue)
    {
        this.isLap = isLap;
        this.title = title;
        this.lapCount = lapCount;
        this.callback = callback;
        this.defaultValue = defaultValue;
    }

    /**
     *
     *
     */
    @Override
    public @NonNull Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Log.v(TAG, "show " + "def. : " + defaultValue);

        Activity activity = getActivity();
        // 確認ダイアログの生成
        //final AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.wear2_dialog_theme));
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

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
                (dialog, which) -> {
                    try
                    {
                        Log.v(TAG, "ENTRY [" + lap.getValue() + "] " + hour.getValue() + ":" + minute.getValue() + ":" + second.getValue());
                        int lapC = (isLap) ? lap.getValue() : lapCount;
                        long newMillis = ((long) hour.getValue() * 60 * 60 * 1000) + ((long) minute.getValue() * 60 * 1000) + (second.getValue() * 1000L);
                        callback.dataCreated(isLap, lapC, defaultValue, newMillis);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        callback.dataCreateCancelled();
                    }
                    dialog.dismiss();
                });

        // ボタンを設定する (キャンセルボタン）
        alertDialog.setNegativeButton(activity.getString(R.string.dialog_negative_cancel),
                (dialog, which) -> {
                    try
                    {
                        callback.dataCreateCancelled();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    dialog.cancel();
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


    /**
     *  コールバックインタフェース
     *
     */
    public interface Callback
    {
        void dataCreated(boolean isLap, int lap, long previousValue, long newValue); // OKを選択したとき
        void dataCreateCancelled();  // キャンセルしたとき
    }
}
