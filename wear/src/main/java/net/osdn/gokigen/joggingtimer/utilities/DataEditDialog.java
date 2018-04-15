package net.osdn.gokigen.joggingtimer.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import net.osdn.gokigen.joggingtimer.R;

/**
 *
 *
 */
public class DataEditDialog
{
    private final String TAG = toString();
    private final WearableActivity activity;
    private int selectedPosition = 0;

    public DataEditDialog(WearableActivity activity)
    {
        this.activity = activity;
    }

    /**
     *
     * @param iconResId  アイコンリソース
     * @param callback  結果をコールバック
     */
    public void show(int iconResId, String title, final DataEditDialog.Callback callback)
    {
        // 確認ダイアログの生成
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

        // Get the layout inflater
        LayoutInflater inflater = activity.getLayoutInflater();
        final View alertView = inflater.inflate(R.layout.information_dialog, null, false);
        alertDialog.setView(alertView);

        final String[] objects = activity.getResources().getStringArray(R.array.icon_selection_id);
        final Spinner spinner = alertView.findViewById(R.id.spinner_selection);
        final EditText titleText = alertView.findViewById(R.id.edit_title);
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

        alertDialog.setIcon(iconResId);
        alertDialog.setMessage(activity.getString(R.string.dialog_message_data_edit));
        alertDialog.setCancelable(true);

        // ボタンを設定する（実行ボタン）
        alertDialog.setPositiveButton(activity.getString(R.string.dialog_positive_execute),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        try
                        {
                            String array[] = activity.getResources().getStringArray(R.array.icon_selection_id);
                            callback.dataEdited(Integer.parseInt(array[selectedPosition]),  titleText.getText().toString());
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            callback.cancelled();
                        }
                        dialog.dismiss();
                    }
                });

        // ボタンを設定する (キャンセルボタン）
        alertDialog.setNegativeButton(activity.getString(R.string.dialog_negative_cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        callback.cancelled();
                        dialog.cancel();
                    }
                });

        // 確認ダイアログを表示する
        alertDialog.show();
    }

    // コールバックインタフェース
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

        private IconListAdapter(WearableActivity activity, int layoutResourceId, String[] strings)
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
            TextView target = row.findViewById(R.id.selection_icon);
            TypedArray imgs = activity.getResources().obtainTypedArray(R.array.icon_selection);
            int rscId = imgs.getResourceId(position, R.drawable.ic_label_outline_black_24dp);
            target.setCompoundDrawablesWithIntrinsicBounds(rscId, 0, 0, 0);
            imgs.recycle();
            return (row);
        }

        @Override
        public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent)
        {
            return (getView(position, convertView, parent));
        }
    }
}
