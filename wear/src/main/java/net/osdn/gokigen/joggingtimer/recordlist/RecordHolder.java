package net.osdn.gokigen.joggingtimer.recordlist;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.osdn.gokigen.joggingtimer.R;

class RecordHolder extends RecyclerView.ViewHolder
{
    private ImageView iconView = null;
    private TextView mainText = null;
    private TextView subText = null;

    RecordHolder(View itemView)
    {
        super(itemView);
        iconView = itemView.findViewById(R.id.gokigen_icon);
        mainText = itemView.findViewById(R.id.main_text);
        subText = itemView.findViewById(R.id.sub_text);
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
