package net.osdn.gokigen.joggingtimer.utilities;

import net.osdn.gokigen.joggingtimer.R;

/**
 *
 *
 */
public class IconIdProvider
{
    private static int iconIds[] = {
            R.drawable.ic_label_outline_black_24dp	,
            R.drawable.ic_info_outline_black_24dp	,
            R.drawable.ic_warning_black_24dp	,
            R.drawable.ic_thumb_down_black_24dp	,
            R.drawable.ic_thumb_up_black_24dp	,
            R.drawable.ic_star_border_black_24dp	,
            R.drawable.ic_star_half_black_24dp	,
            R.drawable.ic_star_black_24dp	,
            R.drawable.ic_timer_black_24dp	,
            R.drawable.ic_history_black_24dp	,
            R.drawable.ic_update_black_24dp	,
            R.drawable.ic_timer_off_black_24dp	,
            R.drawable.ic_block_black_24dp	,
            R.drawable.ic_do_not_disturb_black_24dp	,
            R.drawable.ic_sentiment_very_dissatisfied_black_24dp	,
            R.drawable.ic_mood_bad_black_24dp	,
            R.drawable.ic_sentiment_dissatisfied_black_24dp	,
            R.drawable.ic_sentiment_neutral_black_24dp	,
            R.drawable.ic_sentiment_satisfied_black_24dp	,
            R.drawable.ic_mood_black_24dp	,
            R.drawable.ic_sentiment_very_satisfied_black_24dp	,
            R.drawable.ic_child_care_black_24dp	,
            R.drawable.ic_flag_black_24dp	,
            R.drawable.ic_directions_walk_black_24dp	,
            R.drawable.ic_directions_run_black_24dp	,
            R.drawable.ic_directions_bike_black_24dp	,
            R.drawable.ic_rowing_black_24dp,
            R.drawable.ic_directions_boat_black_24dp	,
            R.drawable.ic_motorcycle_black_24dp,
            R.drawable.ic_directions_car_black_24dp	,
            R.drawable.ic_directions_transit_black_24dp	,
            R.drawable.ic_flight_black_24dp	,
            R.drawable.ic_terrain_black_24dp,
            R.drawable.ic_beach_access_black_24dp,
            R.drawable.ic_fitness_center_black_24dp,
            R.drawable.ic_golf_course_black_24dp,
            R.drawable.ic_android_black_24dp,
    };

    public static int getIconResourceId(int id)
    {
        try
        {
            return (iconIds[id]);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return (iconIds[0]);
    }
}
