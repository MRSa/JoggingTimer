package net.osdn.gokigen.joggingtimer.recorddetail;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.osdn.gokigen.joggingtimer.R;

class DetailHolder  extends RecyclerView.ViewHolder
{
    private final View itemView;
    private ImageView iconView = null;
    private TextView mainText = null;
    private TextView subText = null;

    DetailHolder(View itemView)
    {
        super(itemView);
        iconView = itemView.findViewById(R.id.detail_icon);
        mainText = itemView.findViewById(R.id.label1);
        subText = itemView.findViewById(R.id.label2);
        this.itemView = itemView;
    }

    void setOnClickListener(View.OnClickListener listener)
    {
        itemView.setOnClickListener(listener);
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
