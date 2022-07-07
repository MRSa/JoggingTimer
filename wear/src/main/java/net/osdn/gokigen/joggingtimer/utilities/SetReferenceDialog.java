package net.osdn.gokigen.joggingtimer.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import net.osdn.gokigen.joggingtimer.R;

public class SetReferenceDialog extends DialogFragment
{
    private final String TAG = toString();
    int selectedId = 0;
    SetReferenceDialog.SetReferenceCallback callback = null;
    Dialog myDialog = null;

    public static SetReferenceDialog newInstance(@NonNull SetReferenceDialog.SetReferenceCallback callback)
    {
        SetReferenceDialog instance = new SetReferenceDialog();
        instance.prepare(callback);

        // パラメータはBundleにまとめておく
        Bundle arguments = new Bundle();
        instance.setArguments(arguments);

        return (instance);
    }

    /**
     *
     *
     */
    private void prepare(SetReferenceDialog.SetReferenceCallback callback)
    {
        this.callback = callback;
    }

    /**
     *
     *
     */
    @Override
    public @NonNull Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Activity activity = getActivity();
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        final View alertView = inflater.inflate(R.layout.select_set_reference_dialog, null, false);
        alertDialog.setView(alertView);

        final String[] objects = activity.getResources().getStringArray(R.array.reference_selection_array);
        final Spinner spinner = alertView.findViewById(R.id.spinner_reference_selection);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, objects);
        selectedId = 0;
        try
        {
            spinner.setAdapter(arrayAdapter);
            spinner.setSelection(selectedId);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Log.v(TAG, "onItemSelected : " + position + " (" + id + ")");
                    selectedId = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Log.v(TAG, "onNothingSelected");
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        alertDialog.setCancelable(true);
        alertDialog.setTitle(activity.getString(R.string.select_reference_title));
        String positiveLabel = activity.getString(R.string.dialog_positive_execute);
        String negativeLabel = activity.getString(R.string.dialog_negative_cancel);

        // ボタンを設定する（実行ボタン）
        alertDialog.setPositiveButton(positiveLabel,
                (dialog, which) -> {
                    Log.v(TAG, "ConfirmationDialog::OK");
                    if (callback != null)
                    {
                        callback.confirmed(selectedId);
                    }
                    dialog.dismiss();
                });

        // ボタンを設定する (キャンセルボタン）
        alertDialog.setNegativeButton(negativeLabel,
                (dialog, which) -> dialog.cancel());

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
    public interface SetReferenceCallback
    {
        void confirmed(int id); // OKを選択したとき
    }
}
