package net.osdn.gokigen.joggingtimer.recordlist;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import net.osdn.gokigen.joggingtimer.R;

class RecordHolder extends RecyclerView.ViewHolder
{
    private final View itemView;
    private final ImageView iconView;
    private final TextView mainText;
    private final TextView subText;

    RecordHolder(View itemView)
    {
        super(itemView);
        iconView = itemView.findViewById(R.id.gokigen_icon);
        mainText = itemView.findViewById(R.id.main_text);
        subText = itemView.findViewById(R.id.sub_text);
        this.itemView = itemView;
    }

    void setOnClickListener(View.OnClickListener listener)
    {
        itemView.setOnClickListener(listener);
    }

    void setOnLongClickListener(View.OnLongClickListener listener)
    {
        itemView.setOnLongClickListener(listener);
    }

    void setIconId(int iconId)
    {
        if (iconId != 0)
        {
            iconView.setImageResource(iconId);
        }
    }

    void setMainText(String message)
    {
        mainText.setText(message);
    }

    void setSubText(String message)
    {
        subText.setText(message);
    }
}
