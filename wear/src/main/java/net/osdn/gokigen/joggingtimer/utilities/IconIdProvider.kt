package net.osdn.gokigen.joggingtimer.utilities

import net.osdn.gokigen.joggingtimer.R

/**
 *
 *
 */
object IconIdProvider {
    private val iconIds = intArrayOf(
        R.drawable.ic_label_outline_black_24dp,   // default icon
        R.drawable.alpha_b_box, // R.drawable.ic_board_1_black_24dp,    // reference data B
        R.drawable.alpha_a_box, // R.drawable.ic_board_2_black_24dp,    // reference data A
        R.drawable.alpha_c_box, // R.drawable.ic_board_3_black_24dp,    // reference data C
        R.drawable.ic_done_black_24dp,
        R.drawable.ic_bookmark_black_24dp,
        R.drawable.ic_book_black_24dp,
        R.drawable.ic_receipt_black_24dp,
        R.drawable.ic_info_outline_black_24dp,
        R.drawable.ic_check_circle_black_24dp,
        R.drawable.ic_info_black_24dp,
        R.drawable.ic_warning_black_24dp,
        R.drawable.ic_thumb_down_black_24dp,
        R.drawable.ic_thumb_up_black_24dp,
        R.drawable.ic_star_border_black_24dp,
        R.drawable.ic_star_half_black_24dp,
        R.drawable.ic_star_black_24dp,
        R.drawable.ic_timer_black_24dp,
        R.drawable.ic_history_black_24dp,
        R.drawable.ic_update_black_24dp,
        R.drawable.ic_timer_off_black_24dp,
        R.drawable.ic_block_black_24dp,
        R.drawable.ic_do_not_disturb_black_24dp,
        R.drawable.ic_sentiment_very_dissatisfied_black_24dp,
        R.drawable.ic_mood_bad_black_24dp,
        R.drawable.ic_sentiment_dissatisfied_black_24dp,
        R.drawable.ic_sentiment_neutral_black_24dp,
        R.drawable.ic_sentiment_satisfied_black_24dp,
        R.drawable.ic_mood_black_24dp,
        R.drawable.ic_sentiment_very_satisfied_black_24dp,
        R.drawable.ic_child_care_black_24dp,
        R.drawable.ic_flag_black_24dp,
        R.drawable.ic_directions_walk_black_24dp,
        R.drawable.ic_directions_run_black_24dp,
        R.drawable.ic_directions_bike_black_24dp,
        R.drawable.ic_rowing_black_24dp,
        R.drawable.ic_directions_boat_black_24dp,
        R.drawable.ic_motorcycle_black_24dp,
        R.drawable.ic_directions_car_black_24dp,
        R.drawable.ic_directions_transit_black_24dp,
        R.drawable.ic_flight_black_24dp,
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
        R.drawable.ic_baseline_boy_24,
        R.drawable.ic_baseline_android_24,
        R.drawable.ic_baseline_downhill_skiing_24,
        R.drawable.ic_baseline_escalator_warning_24,
        R.drawable.ic_baseline_wc_24,
        R.drawable.ic_baseline_skateboarding_24,
        R.drawable.ic_baseline_snowboarding_24,
        R.drawable.ic_baseline_snowshoeing_24,
        R.drawable.ic_baseline_sledding_24,
        R.drawable.ic_baseline_snowmobile_24,
        R.drawable.ic_baseline_sports_handball_24,
        R.drawable.ic_baseline_surfing_24,
        R.drawable.ic_baseline_cruelty_free_24,
        R.drawable.ic_baseline_accessibility_24,
        R.drawable.ic_baseline_accessibility_new_24,
        R.drawable.ic_baseline_123_24,
        R.drawable.ic_lightbulb_outline_black_24dp,
        R.drawable.ic_bookmark_border_black_24dp,
        R.drawable.ic_baseline_sick_24,
        R.drawable.baseline_face_24,
        R.drawable.baseline_face_2_24,
        R.drawable.baseline_face_3_24,
        R.drawable.baseline_face_4_24,
        R.drawable.baseline_face_5_24,
        R.drawable.baseline_face_6_24,
        R.drawable.baseline_face_retouching_natural_24,
        R.drawable.baseline_mood_24,
        R.drawable.baseline_mood_bad_24,
        R.drawable.baseline_man_24,
        R.drawable.baseline_man_2_24,
        R.drawable.baseline_man_3_24,
        R.drawable.baseline_man_4_24,
        R.drawable.baseline_woman_24,
        R.drawable.baseline_woman_2_24,
        R.drawable.baseline_ramen_dining_24,
        R.drawable.baseline_wind_power_24,
        R.drawable.baseline_diversity_1_24,
        R.drawable.baseline_diversity_2_24,
        R.drawable.baseline_diversity_3_24,
    )

    fun getIconIdList() : IntArray
    {
        return (iconIds)
    }
    fun getIconResourceId(id: Int): Int
    {
        try
        {
            return iconIds[id]
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return R.drawable.ic_label_outline_black_24dp
    }
}
