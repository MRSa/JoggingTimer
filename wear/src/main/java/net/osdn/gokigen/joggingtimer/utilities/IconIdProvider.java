package net.osdn.gokigen.joggingtimer.utilities;

import net.osdn.gokigen.joggingtimer.R;

/**
 *
 *
 */
public class IconIdProvider
{
    private static final int[] iconIds = {
            R.drawable.ic_label_outline_black_24dp	,
            R.drawable.ic_bookmark_border_black_24dp,
            R.drawable.ic_content_paste_black_24dp,
            R.drawable.ic_lightbulb_outline_black_24dp,
            R.drawable.ic_done_black_24dp,
            R.drawable.ic_bookmark_black_24dp,
            R.drawable.ic_book_black_24dp,
            R.drawable.ic_receipt_black_24dp,
            R.drawable.ic_info_outline_black_24dp	,
            R.drawable.ic_check_circle_black_24dp,
            R.drawable.ic_info_black_24dp,
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
            R.drawable.ic_baseline_elderly_24,
            R.drawable.ic_baseline_emoji_people_24,
            R.drawable.ic_baseline_follow_the_signs_24,
            R.drawable.ic_baseline_hail_24,
            R.drawable.ic_baseline_hiking_24,
            R.drawable.ic_baseline_self_improvement_24,
            R.drawable.ic_baseline_pedal_bike_24,
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
