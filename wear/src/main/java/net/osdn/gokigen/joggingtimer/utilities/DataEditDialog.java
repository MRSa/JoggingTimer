package net.osdn.gokigen.joggingtimer.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import androidx.fragment.app.DialogFragment;
//import android.app.DialogFragment;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import net.osdn.gokigen.joggingtimer.R;

/**
 *
 *
 */
public class DataEditDialog  extends DialogFragment
{
    private final String TAG = toString();
    private int iconResId = 0;
    private String title = "";
    private int selectedPosition = 0;
    private DataEditDialog.Callback callback = null;
    Dialog myDialog = null;

    /**
     *
     *
     */
    public static DataEditDialog newInstance(int iconResId, String title,  @NonNull DataEditDialog.Callback callback)
    {
        DataEditDialog instance = new DataEditDialog();
        instance.prepare(iconResId, title, callback);

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
    private void prepare(int iconResId, String title,  @NonNull DataEditDialog.Callback callback)
    {
        this.iconResId = iconResId;
        this.title = title;
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

        // 確認ダイアログの生成
        //final AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.wear2_dialog_theme));
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);


        // Get the layout inflater
        LayoutInflater inflater = activity.getLayoutInflater();
        final View alertView = inflater.inflate(R.layout.information_dialog, null, false);
        alertDialog.setView(alertView);

        final String[] objects = activity.getResources().getStringArray(R.array.icon_selection_id);
        final Spinner spinner = alertView.findViewById(R.id.spinner_selection);
        final EditText titleText = alertView.findViewById(R.id.edit_title);

        // もー苦肉の策だ。。。
        if (Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.M)
        {
            titleText.setTextColor(Color.BLACK);
        }

        try
        {
            titleText.setText(title);
            IconListAdapter adapter = new IconListAdapter(activity, R.layout.icon_list, objects);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Log.v(TAG, "onItemSelected : " + position + " (" + id + ")");
                    selectedPosition = position;
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
        //alertDialog.setIcon(iconResId);
        //alertDialog.setMessage(activity.getString(R.string.dialog_message_data_edit));
        alertDialog.setCancelable(true);

        // ボタンを設定する（実行ボタン）
        alertDialog.setPositiveButton(activity.getString(R.string.dialog_positive_execute),
                (dialog, which) -> {
                    try
                    {
                        Activity activity1 = getActivity();
                        if (activity1 != null)
                        {
                            String[] array = activity1.getResources().getStringArray(R.array.icon_selection_id);
                            if (callback != null)
                            {
                                callback.dataEdited(Integer.parseInt(array[selectedPosition]), titleText.getText().toString());
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        if (callback != null)
                        {
                            callback.cancelled();
                        }
                    }
                    dialog.dismiss();
                });

        // ボタンを設定する (キャンセルボタン）
        alertDialog.setNegativeButton(activity.getString(R.string.dialog_negative_cancel),
                (dialog, which) -> {
                    if (callback != null)
                    {
                        callback.cancelled();
                    }
                    dialog.cancel();
                });

        // 確認ダイアログを応答する
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
     * コールバックインタフェース
     *
     */
    public interface Callback
    {
        void dataEdited(int iconId, String title); // OKを選択したとき
        void cancelled();                          // キャンセルしたとき
    }

    public class IconListAdapter extends ArrayAdapter<String>
    {
        private final LayoutInflater inflater;
        private final int layoutResourceId;
        private final String[]  stringList;

        private IconListAdapter(Activity activity, int layoutResourceId, String[] strings)
        {
            super(activity, layoutResourceId, strings);
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            stringList = strings;
            this.layoutResourceId = layoutResourceId;
        }

        @Override
        public int getCount()
        {
            return stringList.length;
        }

        @Override
        public String getItem(int position)
        {
            return stringList[position];
        }

        @Override
        public long getItemId(int position)
        {
            return (position);
        }

        @Override
        public @NonNull View getView(int position, View convertView, @NonNull ViewGroup parent)
        {
            View row = inflater.inflate(layoutResourceId, parent, false);
            try
            {
                TextView target = row.findViewById(R.id.selection_icon);
                TypedArray imgs = getActivity().getResources().obtainTypedArray(R.array.icon_selection);
                int rscId = imgs.getResourceId(position, R.drawable.ic_label_outline_black_24dp);
                target.setCompoundDrawablesWithIntrinsicBounds(rscId, 0, 0, 0);
                imgs.recycle();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return (row);
        }

        @Override
        public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent)
        {
            return (getView(position, convertView, parent));
        }
    }
}
