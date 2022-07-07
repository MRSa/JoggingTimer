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


public class SelectReferenceViewModeDialog extends DialogFragment
{
    private final String TAG = toString();
    public static final String PREF_KEY_DISPLAY_LAPGRAPHIC = "DISP_LAPGRPH";
    public static final String PREF_KEY_REFERENCE_TIME_SELECTION = "REF_TIME_SEL";

    int selectedId = 0;
    int selectedMode = 0;
    SelectReferenceViewModeDialog.SelectReferenceCallback callback = null;
    Dialog myDialog = null;

    public static SelectReferenceViewModeDialog newInstance(boolean viewMode, int referenceId, @NonNull SelectReferenceViewModeDialog.SelectReferenceCallback callback)
    {
        SelectReferenceViewModeDialog instance = new SelectReferenceViewModeDialog();
        instance.prepare(callback, viewMode, referenceId);

        // パラメータはBundleにまとめておく
        Bundle arguments = new Bundle();
        instance.setArguments(arguments);

        return (instance);
    }

    /**
     *
     *
     */
    private void prepare(SelectReferenceViewModeDialog.SelectReferenceCallback callback, boolean viewMode, int referenceId)
    {
        this.callback = callback;
        selectedMode = (viewMode) ? 1 : 0;
        selectedId = referenceId;
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
        final View alertView = inflater.inflate(R.layout.select_reference_dialog, null, false);
        alertDialog.setView(alertView);

        final String[] viewObjects = activity.getResources().getStringArray(R.array.show_laptime_array);
        final Spinner spinner0 = alertView.findViewById(R.id.show_laptime_mode);
        ArrayAdapter<String> arrayAdapter0 = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, viewObjects);
        try
        {
            spinner0.setAdapter(arrayAdapter0);
            spinner0.setSelection(selectedMode);
            spinner0.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Log.v(TAG, "onItemSelected : " + position + " (" + id + ")");
                    selectedMode = position;
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

        final String[] objects = activity.getResources().getStringArray(R.array.reference_selection_array);
        final Spinner spinner = alertView.findViewById(R.id.spinner_select_reference);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, objects);
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

        String positiveLabel = activity.getString(R.string.dialog_positive_execute);
        String negativeLabel = activity.getString(R.string.dialog_negative_cancel);

        // ボタンを設定する（実行ボタン）
        alertDialog.setPositiveButton(positiveLabel,
                (dialog, which) -> {
                    Log.v(TAG, "ConfirmationDialog::OK " + selectedId + " " + selectedMode);
                    if (callback != null)
                    {
                        callback.selectedReferenceViewMode(selectedId, selectedMode);
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
    public interface SelectReferenceCallback
    {
        void selectedReferenceViewMode(int referenceId, int viewMode); // OKを選択したとき
    }
}
